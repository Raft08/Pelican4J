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

package be.raft.pelican.entities;

import be.raft.pelican.application.entities.Application;
import be.raft.pelican.client.entities.Client;
import be.raft.pelican.requests.Requester;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import okhttp3.OkHttpClient;

/**
 * Pelican API interface, holds all the api instances.
 */
public interface PelicanApi {

	/**
	 * Token used for authentication when connecting to the panel.
	 *
	 * @return provided token.
	 */
	String token();

	/**
	 * Get the url used in the api.
	 *
	 * @return provided url.
	 */
	String url();

	/**
	 * Get the user-agent used to make requests to the api.
	 *
	 * @return provided user-agent.
	 */
	String userAgent();

	/**
	 * Retrieve the specified client for HTTP request handling.
	 *
	 * @return provided HTTP client used for making connections.
	 */
	OkHttpClient httpClient();

	/**
	 * Retrieve the specified client for the web-socket.
	 *
	 * @return provided socket client used for making connections.
	 */
	OkHttpClient socketClient();

	/**
	 * Retrieve the requester used for making request to the api.
	 *
	 * @return requester of the api.
	 */
	Requester requester();

	/**
	 * Retrieve the callback pool executor.
	 *
	 * @return callback pool.
	 */
	ExecutorService callbackPool();

	/**
	 * Retrieve the action executor pool used for sending requests to the panel.
	 *
	 * @return action-pool.
	 */
	ExecutorService actionPool();

	/**
	 * Retrieve the rate limit executor.
	 *
	 * @return rate-limit pool.
	 */
	ScheduledExecutorService rateLimitPool();

	/**
	 * Retrieve the supplier pool executor.
	 *
	 * @return supplier pool.
	 */
	ExecutorService supplierPool();

	/**
	 * Retrieve the api client.
	 *
	 * @return the api client.
	 */
	Client client();

	/**
	 * Retrieve the api application.
	 *
	 * @return the api application.
	 */
	Application application();
}
