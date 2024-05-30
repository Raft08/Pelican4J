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

package be.raft.pelican.client.managers;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import org.json.JSONObject;

public class ClientServerManager {

	private final ClientServer server;
	private final PteroClientImpl impl;

	public ClientServerManager(ClientServer server, PteroClientImpl impl) {
		this.server = server;
		this.impl = impl;
	}

	public RequestAction<Void> setName(String name) {
		JSONObject obj = new JSONObject().put("name", name);
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Client.RENAME_SERVER.compile(server.getIdentifier()),
				RequestActionImpl.getRequestBody(obj));
	}

	public RequestAction<Void> reinstall() {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Client.REINSTALL_SERVER.compile(server.getIdentifier()));
	}
}
