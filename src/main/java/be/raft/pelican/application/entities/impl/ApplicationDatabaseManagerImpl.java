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

package be.raft.pelican.application.entities.impl;

import be.raft.pelican.PteroAction;
import be.raft.pelican.application.entities.ApplicationDatabase;
import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.application.managers.ApplicationDatabaseCreationAction;
import be.raft.pelican.application.managers.ApplicationDatabaseManager;
import be.raft.pelican.requests.PteroActionImpl;
import be.raft.pelican.requests.Route;

public class ApplicationDatabaseManagerImpl implements ApplicationDatabaseManager {

	private final ApplicationServer server;
	private final PteroApplicationImpl impl;

	public ApplicationDatabaseManagerImpl(ApplicationServer server, PteroApplicationImpl impl) {
		this.server = server;
		this.impl = impl;
	}

	@Override
	public ApplicationDatabaseCreationAction createDatabase() {
		return new ApplicationDatabaseCreationActionImpl(server, impl);
	}

	@Override
	public PteroAction<Void> resetPassword(ApplicationDatabase database) {
		return PteroActionImpl.onRequestExecute(
				impl.getP4J(), Route.Databases.RESET_PASSWORD.compile(server.getId(), database.getId()));
	}

	@Override
	public PteroAction<Void> deleteDatabase(ApplicationDatabase database) {
		return PteroActionImpl.onRequestExecute(
				impl.getP4J(), Route.Databases.DELETE_DATABASE.compile(server.getId(), database.getId()));
	}
}
