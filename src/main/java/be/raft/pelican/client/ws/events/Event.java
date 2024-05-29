/*
 *    Copyright 2021-2022 Matt Malec, and the Pterodactyl4J contributors
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
 */

package be.raft.pelican.client.ws.events;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.PteroClient;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.client.managers.WebSocketManager;

/**
 * Top-level event type
 * <br>All WebSocket fires are derived from this class.
 *
 * <p>Adapter implementation: {@link be.raft.pelican.client.ws.hooks.ClientSocketListenerAdapter}
 */
public abstract class Event {

	private final PteroClientImpl api;
	private final ClientServer server;
	private final WebSocketManager manager;

	public Event(PteroClientImpl api, ClientServer server, WebSocketManager manager) {
		this.api = api;
		this.server = server;
		this.manager = manager;
	}

	/**
	 * The current {@link be.raft.pelican.client.entities.PteroClient} instance corresponding to this Event
	 *
	 * @return The corresponding {@link be.raft.pelican.client.entities.PteroClient} instance
	 */
	public PteroClient getClient() {
		return api;
	}

	/**
	 * The {@link be.raft.pelican.client.entities.ClientServer} instance corresponding to this Event
	 * <p><b>Note:</b> This is the server at the time of WebSocket creation,
	 * if you need an updated version (like if the install state is changed), use
	 * {@link Event#retrieveServer()} or set {@link be.raft.pelican.client.managers.WebSocketBuilder#freshServer(boolean)} WebSocketBuilder.freshServer(enable)}
	 * to true
	 *
	 * @return The corresponding {@link be.raft.pelican.client.entities.ClientServer} instance
	 *
	 * @see Event#retrieveServer()
	 * @see be.raft.pelican.client.managers.WebSocketBuilder#freshServer(boolean) WebSocketBuilder.freshServer(enable)
	 */
	public ClientServer getServer() {
		return server;
	}

	/**
	 * The {@link be.raft.pelican.client.entities.ClientServer} instance corresponding to this Event
	 * <p>This method should only be used if you need updated information. If you only need basic information (like the name),
	 * use {@link Event#getServer()}
	 *
	 * @return The corresponding {@link be.raft.pelican.client.entities.ClientServer} instance
	 *
	 * @see Event#getServer()
	 */
	public RequestAction<ClientServer> retrieveServer() {
		return api.retrieveServerByIdentifier(server.getIdentifier());
	}

	/**
	 * The current {@link be.raft.pelican.client.managers.WebSocketManager} instance corresponding to this Event
	 * <p>This method is useful if you need to send a command or power response to a given Event
	 *
	 * @return The corresponding {@link be.raft.pelican.client.managers.WebSocketManager} instance
	 */
	public WebSocketManager getWebSocketManager() {
		return manager;
	}
}
