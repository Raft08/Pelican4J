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
import be.raft.pelican.client.entities.PteroClient;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.entities.P4J;
import be.raft.pelican.requests.Requester;
import be.raft.pelican.utils.config.EndpointConfig;
import be.raft.pelican.utils.config.SessionConfig;
import be.raft.pelican.utils.config.ThreadingConfig;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import okhttp3.OkHttpClient;

public class P4JImpl implements P4J {

	private final Requester requester;

	private final EndpointConfig endpointConfig;
	private final ThreadingConfig threadingConfig;
	private final SessionConfig sessionConfig;

	public P4JImpl(EndpointConfig endpointConfig, ThreadingConfig threadingConfig, SessionConfig sessionConfig) {
		this.endpointConfig = endpointConfig;
		this.threadingConfig = threadingConfig;
		this.sessionConfig = sessionConfig;
		this.requester = new Requester(this);
	}

	@Override
	public String getToken() {
		return endpointConfig.getToken();
	}

	@Override
	public Requester getRequester() {
		return requester;
	}

	@Override
	public String getApplicationUrl() {
		return endpointConfig.getUrl();
	}

	@Override
	public OkHttpClient getHttpClient() {
		return sessionConfig.getHttpClient();
	}

	@Override
	public ExecutorService getCallbackPool() {
		return threadingConfig.getCallbackPool();
	}

	@Override
	public ExecutorService getActionPool() {
		return threadingConfig.getActionPool();
	}

	@Override
	public ScheduledExecutorService getRateLimitPool() {
		return threadingConfig.getRateLimitPool();
	}

	@Override
	public ExecutorService getSupplierPool() {
		return threadingConfig.getSupplierPool();
	}

	@Override
	public OkHttpClient getWebSocketClient() {
		return sessionConfig.getWebSocketClient();
	}

	@Override
	public String getUserAgent() {
		return sessionConfig.getUserAgent();
	}

	@Override
	public Application asApplication() {
		return new ApplicationImpl(this);
	}

	@Override
	public PteroClient asClient() {
		return new PteroClientImpl(this);
	}
}
