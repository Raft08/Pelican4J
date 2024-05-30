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
import be.raft.pelican.client.entities.PteroClient;
import be.raft.pelican.requests.Requester;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import okhttp3.OkHttpClient;

public interface P4J {

	String getToken();

	Requester getRequester();

	String getApplicationUrl();

	OkHttpClient getHttpClient();

	ExecutorService getCallbackPool();

	ExecutorService getActionPool();

	ScheduledExecutorService getRateLimitPool();

	ExecutorService getSupplierPool();

	OkHttpClient getWebSocketClient();

	String getUserAgent();

	PteroClient asClient();

	Application asApplication();
}
