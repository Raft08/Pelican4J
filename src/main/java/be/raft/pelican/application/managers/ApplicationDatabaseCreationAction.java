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

package be.raft.pelican.application.managers;

import be.raft.pelican.RequestAction;
import be.raft.pelican.application.entities.ApplicationDatabase;

/**
 * {@link RequestAction PteroAction} extension designed for the creation of
 * {@link be.raft.pelican.application.entities.ApplicationDatabase ApplicationDatabases}
 *
 * @see ApplicationDatabaseManager#createDatabase
 */
public interface ApplicationDatabaseCreationAction extends RequestAction<ApplicationDatabase> {

	/**
	 * Sets the name for this {@link be.raft.pelican.application.entities.ApplicationDatabase ApplicationDatabase}.
	 *
	 * <br>The panel validates this value using the following regex: {@code /^[A-Za-z0-9_]+$/}
	 *
	 * @param name The name for the database
	 * @return The {@link be.raft.pelican.application.managers.ApplicationDatabaseCreationAction ApplicationDatabaseCreationAction}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided name is {@code null} or not between 1-48 characters long
	 */
	ApplicationDatabaseCreationAction setName(String name);

	/**
	 * Sets the remote connection string for this {@link be.raft.pelican.application.entities.ApplicationDatabase ApplicationDatabase}.
	 *
	 * <br>The panel validates this value using the following regex: {@code /^[0-9%.]{1,15}$/}
	 *
	 * @param remote The remote connection string for the database
	 * @return The {@link be.raft.pelican.application.managers.ApplicationDatabaseCreationAction ApplicationDatabaseCreationAction}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided remote connection string is {@code null} or not between 1-15 characters long
	 */
	ApplicationDatabaseCreationAction setRemote(String remote);

	/**
	 * Sets the database host id for this
	 * {@link be.raft.pelican.application.entities.ApplicationDatabase ApplicationDatabase}.
	 *
	 * @param id The host id
	 * @return The {@link be.raft.pelican.application.managers.ApplicationDatabaseCreationAction ApplicationDatabaseCreationAction}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided id is less than 0
	 */
	ApplicationDatabaseCreationAction setHost(long id);

	/**
	 * Sets the database host id for this
	 * {@link be.raft.pelican.application.entities.ApplicationDatabase ApplicationDatabase}.
	 *
	 * @param id The host id
	 * @return The {@link be.raft.pelican.application.managers.ApplicationDatabaseCreationAction ApplicationDatabaseCreationAction}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided id is less than 0
	 */
	default ApplicationDatabaseCreationAction setHost(String id) {
		return setHost(Long.parseUnsignedLong(id));
	}

	/**
	 * Sets the database host for this
	 * {@link be.raft.pelican.application.entities.ApplicationDatabase ApplicationDatabase}.
	 *
	 * @param host The {@link be.raft.pelican.application.entities.ApplicationDatabase.DatabaseHost DatabaseHost}
	 * @return The {@link be.raft.pelican.application.managers.ApplicationDatabaseCreationAction ApplicationDatabaseCreationAction}
	 * instance, useful for chaining
	 */
	default ApplicationDatabaseCreationAction setHost(ApplicationDatabase.DatabaseHost host) {
		return setHost(host.getNodeIdLong());
	}
}
