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
import be.raft.pelican.client.entities.ClientDatabase;

/**
 * {@link RequestAction PteroAction} extension designed for the creation of
 * {@link be.raft.pelican.client.entities.ClientDatabase ClientDatabases}
 *
 * @see ClientDatabaseManager#createDatabase
 */
public interface ClientDatabaseCreationAction extends RequestAction<ClientDatabase> {

	/**
	 * Sets the name for this {@link be.raft.pelican.client.entities.ClientDatabase ClientDatabase}.
	 *
	 * <br>The panel validates this value using the following regex: {@code /^[A-Za-z0-9_]+$/}
	 *
	 * @param name The name for the database
	 * @return The {@link be.raft.pelican.client.managers.ClientDatabaseCreationAction ClientDatabaseCreationAction}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided name is {@code null} or not between 1-48 characters long
	 */
	ClientDatabaseCreationAction setName(String name);

	/**
	 * Sets the remote connection string for this {@link be.raft.pelican.client.entities.ClientDatabase ClientDatabase}.
	 *
	 * <br>The panel validates this value using the following regex: {@code /^[0-9%.]{1,15}$/}
	 *
	 * @param remote The remote connection string for the database
	 * @return The {@link be.raft.pelican.client.managers.ClientDatabaseCreationAction ClientDatabaseCreationAction}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided remote connection string is {@code null} or not between 1-15 characters long
	 */
	ClientDatabaseCreationAction setRemote(String remote);
}
