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

package be.raft.pelican.entities.impl;

import be.raft.pelican.application.entities.Application;
import be.raft.pelican.application.entities.impl.ApplicationImpl;
import be.raft.pelican.client.entities.Client;
import be.raft.pelican.client.entities.impl.ClientImpl;
import be.raft.pelican.entities.PelicanApi;
import be.raft.pelican.requests.Requester;
import be.raft.pelican.utils.config.EndpointConfig;
import be.raft.pelican.utils.config.SessionConfig;
import be.raft.pelican.utils.config.ThreadingConfig;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import okhttp3.OkHttpClient;

public class PelicanApiImpl implements PelicanApi {

	private final Requester requester;

	private final EndpointConfig endpointConfig;
	private final ThreadingConfig threadingConfig;
	private final SessionConfig sessionConfig;

	private final ClientImpl client;
	private final ApplicationImpl application;

	public PelicanApiImpl(EndpointConfig endpointConfig, ThreadingConfig threadingConfig, SessionConfig sessionConfig) {
		this.endpointConfig = endpointConfig;
		this.threadingConfig = threadingConfig;
		this.sessionConfig = sessionConfig;

		this.requester = new Requester(this);

		this.client = new ClientImpl(this);
		this.application = new ApplicationImpl(this);
	}

	@Override
	public String token() {
		return endpointConfig.getToken();
	}

	@Override
	public Requester requester() {
		return requester;
	}

	@Override
	public String url() {
		return endpointConfig.getUrl();
	}

	@Override
	public OkHttpClient httpClient() {
		return sessionConfig.getHttpClient();
	}

	@Override
	public ExecutorService callbackPool() {
		return threadingConfig.getCallbackPool();
	}

	@Override
	public ExecutorService actionPool() {
		return threadingConfig.getActionPool();
	}

	@Override
	public ScheduledExecutorService rateLimitPool() {
		return threadingConfig.getRateLimitPool();
	}

	@Override
	public ExecutorService supplierPool() {
		return threadingConfig.getSupplierPool();
	}

	@Override
	public OkHttpClient socketClient() {
		return sessionConfig.getWebSocketClient();
	}

	@Override
	public String userAgent() {
		return sessionConfig.getUserAgent();
	}

	@Override
	public Application application() {
		return this.application;
	}

	@Override
	public Client client() {
		return this.client;
	}
}
