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

import be.raft.pelican.PowerAction;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.client.ws.WebSocketAction;
import be.raft.pelican.client.ws.WebSocketClient;
import be.raft.pelican.client.ws.events.connection.DisconnectedEvent;
import be.raft.pelican.client.ws.hooks.IClientListenerManager;
import be.raft.pelican.utils.AwaitableClientListener;

public class WebSocketManager {

	private final PteroClientImpl api;
	private final WebSocketClient client;
	private final IClientListenerManager eventManager;

	public WebSocketManager(
			PteroClientImpl api, ClientServer server, IClientListenerManager eventManager, boolean freshServer) {
		this.api = api;
		this.eventManager = eventManager;
		this.client = new WebSocketClient(api, server, freshServer, this);
		connect();
	}

	public IClientListenerManager getEventManager() {
		return eventManager;
	}

	private void connect() {
		Thread thread = new Thread(client, "P4J-ClientWS");
		thread.start();
	}

	public void reconnect() {
		if (client.isConnected()) {
			AwaitableClientListener listener = AwaitableClientListener.create(
					DisconnectedEvent.class, api.getP4J().getSupplierPool());

			eventManager.register(listener);

			client.shutdown();

			listener.await(ignored -> {
				eventManager.unregister(listener);
				connect();
			});

		} else connect();
	}

	public void shutdown() {
		client.shutdown();
	}

	public void authenticate() {
		client.sendAuthenticate();
	}

	public void authenticate(String token) {
		client.sendAuthenticate(token);
	}

	public void request(RequestAction action) {
		client.send(WebSocketAction.create(action.data, null));
	}

	public void setPower(PowerAction power) {
		client.send(
				WebSocketAction.create(WebSocketAction.SET_STATE, power.name().toLowerCase()));
	}

	public void sendCommand(String command) {
		client.send(WebSocketAction.create(WebSocketAction.SEND_COMMAND, command));
	}

	public enum RequestAction {
		LOGS(WebSocketAction.SEND_LOGS),
		STATS(WebSocketAction.SEND_STATS);

		public final String data;

		RequestAction(String data) {
			this.data = data;
		}
	}
}
