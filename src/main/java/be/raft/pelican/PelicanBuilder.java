/*
 *    Copyright 2021-2024 Matt Malec, and the Pterodactyl4J contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 *    ============================================================================== 
 * 
 *    Copyright 2024 RaftDev, and the Pelican4J contributors
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package be.raft.pelican;

import be.raft.pelican.application.entities.Application;
import be.raft.pelican.client.entities.Client;
import be.raft.pelican.entities.PelicanApi;
import be.raft.pelican.entities.impl.PelicanApiImpl;
import be.raft.pelican.utils.config.EndpointConfig;
import be.raft.pelican.utils.config.SessionConfig;
import be.raft.pelican.utils.config.ThreadingConfig;
import com.google.common.base.Preconditions;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.concurrent.*;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Used to create new {@link Application} or {@link Client} instances.
 */
public class PelicanBuilder {

	private String applicationUrl;
	private String token;

	private ExecutorService actionPool = null;
	private ExecutorService callbackPool = null;
	private ScheduledExecutorService rateLimitPool = null;
	private ExecutorService supplierPool = null;

	private OkHttpClient httpClient = null;
	private OkHttpClient webSocketClient = null;
	private String userAgent = null;

	@ApiStatus.Internal
	private PelicanBuilder(String applicationUrl, String token) {
		this.applicationUrl = applicationUrl;
		this.token = token;
	}

	/**
	 * Creates a {@link PelicanBuilder} with the predefined panel URL and API key.
	 *
	 * @param url   the URL for your panel.
	 * @param token the API key.
	 * @return the new {@link PelicanBuilder}.
	 **/
	public static PelicanBuilder create(@NotNull String url, @NotNull String token) {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(token);

		Preconditions.checkArgument(!url.isEmpty(), "Url may not be empty!");
		Preconditions.checkArgument(!token.isEmpty(), "Token cannot be empty!");

		Preconditions.checkArgument(
				!token.startsWith("ptlc"),
				"Provided token was created for a Pterodactyl Panel! Pterodactyl Panels are not supported!");

		try {
			URI.create(url).toURL();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid/Incorrect url!");
		}

		return new PelicanBuilder(url, token);
	}

	/**
	 * Sets the panel URL that will be used when sending a request.
	 *
	 * @param url url of the Pterodactyl panel.
	 * @return The builder instance.
	 */
	public PelicanBuilder url(@NotNull String url) {
		Preconditions.checkNotNull(url, "Url may not be null!");

		try {
			URI.create(url).toURL();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid/Incorrect url!");
		}

		this.applicationUrl = url;
		return this;
	}

	/**
	 * Sets the API key that will be used when P4J makes a Request.
	 *
	 * @param token the API key for the user or application.
	 * @return The PteroBuilder instance. Useful for chaining.
	 */
	public PelicanBuilder token(@NotNull String token) {
		Preconditions.checkNotNull(token);
		Preconditions.checkArgument(
				!token.startsWith("ptlc"),
				"Provided token was created for a Pterodactyl Panel! Pterodactyl Panels are not supported!");

		this.token = token;
		return this;
	}

	/**
	 * Sets the {@link okhttp3.OkHttpClient OkHttpClient} that will be used by Pelican4J requester.
	 *
	 * <br>This can be used to set things such as connection timeout and proxy.
	 *
	 * @param client the new {@link okhttp3.OkHttpClient OkHttpClient} to use.
	 * @return the builder instance.
	 */
	public PelicanBuilder httpClient(@NotNull OkHttpClient client) {
		Preconditions.checkNotNull(client);

		this.httpClient = client;
		return this;
	}

	/**
	 * Sets the {@link ExecutorService ExecutorService} that should be used in the P4J request handler.
	 * <p>
	 * <strong>Only change this pool if you know what you're doing.</strong>
	 * This is used to queue the request and finalize its request body for {@link RequestAction#executeAsync()} tasks.
	 * <p>
	 * Default: {@link ThreadPoolExecutor} with 1 thread.
	 *
	 * @param pool The thread pool to use for action handling
	 * @return The builder instance.
	 */
	public PelicanBuilder actionPool(@NotNull ExecutorService pool) {
		Preconditions.checkNotNull(pool);

		this.actionPool = pool;
		return this;
	}

	/**
	 * Sets the {@link ExecutorService ExecutorService} that should be used in
	 * the P4J callback handler which consists of {@link RequestAction PteroAction} callbacks.
	 * <p>
	 * <strong>Only change this pool if you know what you're doing.</strong>
	 * This is used to handle callbacks of {@link RequestAction#executeAsync()}, similarly it is used to
	 * finish {@link RequestAction#execute()} tasks which build on queue.
	 *
	 * <p>Default: {@link ForkJoinPool#commonPool()}
	 *
	 * @param pool the thread pool to use for callback handling
	 * @return The builder instance.
	 */
	public PelicanBuilder callbackPool(@NotNull ExecutorService pool) {
		Preconditions.checkNotNull(pool);

		this.callbackPool = pool;
		return this;
	}

	/**
	 * Sets the {@link ScheduledExecutorService ScheduledExecutorService} that should be used in
	 * the P4J rate limiter. Changing this can affect the P4J behavior for PteroAction execution
	 * and should be handled carefully.
	 * <p>
	 * <strong>Only change this pool if you know what you're doing.</strong>
	 * This is used by the rate limiter to handle backoff delays by using scheduled executions.
	 *
	 * <p>Default: {@link ScheduledThreadPoolExecutor} with 5 threads.
	 *
	 * @param pool the thread pool to use for rate limiting
	 * @return The builder instance.
	 */
	public PelicanBuilder rateLimitPool(@NotNull ScheduledExecutorService pool) {
		Preconditions.checkNotNull(pool);

		this.rateLimitPool = pool;
		return this;
	}

	/**
	 * Sets the {@link ExecutorService ExecutorService} that should be used in
	 * the P4J Action CompletableFutures.
	 * <p>
	 * <strong>Only change this pool if you know what you're doing.</strong>
	 * This is used to execute Suppliers mainly used by PteroActions that aren't requests.
	 *
	 * <p>Default: {@link ThreadPoolExecutor} with 3 threads.
	 *
	 * @param pool The thread pool to use for CompletableFutures.
	 * @return The instance of the builder.
	 */
	public PelicanBuilder supplierPool(@NotNull ExecutorService pool) {
		Preconditions.checkNotNull(pool);

		this.supplierPool = pool;
		return this;
	}

	/**
	 * Sets the {@link okhttp3.OkHttpClient OkHttpClient} that will be used by P4Js websocket client.
	 * <br>This can be used to set things such as connection timeout and proxy.
	 *
	 * @param client the new {@link okhttp3.OkHttpClient OkHttpClient} to use.
	 * @return The instance of the builder.
	 */
	public PelicanBuilder socketClient(@NotNull OkHttpClient client) {
		Preconditions.checkNotNull(client);

		this.webSocketClient = client;
		return this;
	}

	/**
	 * Sets the user-agent that will be used when making a request.
	 *
	 * @param userAgent the user agent.
	 * @return The instance of the builder.
	 */
	public PelicanBuilder userAgent(@NotNull String userAgent) {
		Preconditions.checkNotNull(userAgent);
		Preconditions.checkArgument(!userAgent.isEmpty(), "User-Agent cannot be empty!");

		this.userAgent = userAgent;
		return this;
	}

	/**
	 * Create the api wrapper with the provided settings of the builder.
	 *
	 * @return newly created api instance with the settings of the builder.
	 */
	public PelicanApi create() {
		EndpointConfig endpointConfig = new EndpointConfig(applicationUrl, token);

		ThreadingConfig threadingConfig = new ThreadingConfig();
		threadingConfig.setCallbackPool(callbackPool);
		threadingConfig.setActionPool(actionPool);
		threadingConfig.setRateLimitPool(rateLimitPool);
		threadingConfig.setSupplierPool(supplierPool);

		SessionConfig sessionConfig = new SessionConfig(httpClient, webSocketClient);
		sessionConfig.setUserAgent(userAgent);

		return new PelicanApiImpl(endpointConfig, threadingConfig, sessionConfig);
	}
}
