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

package be.raft.pelican.client.entities.impl;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.ClientDatabase;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.managers.ClientDatabaseCreationAction;
import be.raft.pelican.client.managers.ClientDatabaseManager;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public class ClientDatabaseManagerImpl implements ClientDatabaseManager {

	private final ClientServer server;
	private final PteroClientImpl impl;

	public ClientDatabaseManagerImpl(ClientServer server, PteroClientImpl impl) {
		this.server = server;
		this.impl = impl;
	}

	@Override
	public ClientDatabaseCreationAction createDatabase() {
		return new ClientDatabaseCreationActionImpl(server, impl);
	}

	@Override
	public RequestAction<ClientDatabase> resetPassword(ClientDatabase database) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.ClientDatabases.ROTATE_PASSWORD.compile(server.getIdentifier(), database.getId()),
				(response, request) -> new ClientDatabaseImpl(response.getObject(), impl, server));
	}

	@Override
	public RequestAction<Void> deleteDatabase(ClientDatabase database) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.ClientDatabases.DELETE_DATABASE.compile(server.getIdentifier(), database.getId()));
	}
}
