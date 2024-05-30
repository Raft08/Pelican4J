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

package be.raft.pelican.client.ws.handle;

import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.client.managers.WebSocketManager;

public abstract class ClientSocketHandler {

	private final PteroClientImpl client;
	private final WebSocketManager manager;
	private ClientServer server;

	public ClientSocketHandler(PteroClientImpl client, ClientServer server, WebSocketManager manager) {
		this.client = client;
		this.server = server;
		this.manager = manager;
	}

	protected PteroClientImpl getClient() {
		return client;
	}

	protected ClientServer getServer() {
		return server;
	}

	public ClientSocketHandler setServer(ClientServer server) {
		this.server = server;
		return this;
	}

	protected WebSocketManager getManager() {
		return manager;
	}

	public abstract void handleInternally(String content);
}
