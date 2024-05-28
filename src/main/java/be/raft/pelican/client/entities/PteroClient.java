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

package be.raft.pelican.client.entities;

import be.raft.pelican.ClientType;
import be.raft.pelican.PowerAction;
import be.raft.pelican.PteroAction;
import be.raft.pelican.requests.PaginationAction;
import java.util.List;

public interface PteroClient {

	/**
	 * Retrieves the Pterodactyl user account belonging to the API key
	 *
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link be.raft.pelican.client.entities.Account Account}
	 */
	PteroAction<Account> retrieveAccount();

	/**
	 * Sets the power of a {@link be.raft.pelican.client.entities.ClientServer ClientServer}
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link Void}
	 *
	 * @deprecated Use {@link ClientServer#setPower(PowerAction)} instead
	 */
	@Deprecated
	PteroAction<Void> setPower(ClientServer server, PowerAction powerAction);

	/**
	 * Sends a command to a {@link be.raft.pelican.client.entities.ClientServer ClientServer}
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link Void}
	 *
	 * @deprecated Use {@link ClientServer#sendCommand(String)} instead
	 */
	@Deprecated
	PteroAction<Void> sendCommand(ClientServer server, String command);

	/**
	 * Retrieves the utilization of a {@link be.raft.pelican.client.entities.ClientServer ClientServer}
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link be.raft.pelican.client.entities.Utilization Utilization}
	 *
	 * @deprecated Use {@link ClientServer#retrieveUtilization()} instead
	 */
	@Deprecated
	PteroAction<Utilization> retrieveUtilization(ClientServer server);

	/**
	 * Retrieves all the ClientServers from the Pterodactyl instance
	 *
	 * @param type
	 *        Type for the appended type parameter (NONE, ADMIN, ADMIN-ALL, OWNER)
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.client.entities.ClientServer ClientServers}
	 */
	PaginationAction<ClientServer> retrieveServers(ClientType type);

	default PaginationAction<ClientServer> retrieveServers() {
		return retrieveServers(ClientType.NONE);
	}

	/**
	 * Retrieves an individual ClientServer represented by the provided identifier from Pterodactyl instance
	 *
	 * @param  identifier
	 *         The server identifier (first 8 characters of the uuid)
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the server cannot be found
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link be.raft.pelican.client.entities.ClientServer ClientServer}
	 */
	PteroAction<ClientServer> retrieveServerByIdentifier(String identifier);

	/**
	 * Retrieves ClientServers matching the provided name from Pterodactyl instance
	 *
	 * @param  name
	 *         The name
	 * @param caseSensitive
	 * 		   True - If P4J should search using case sensitivity
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect
	 *
	 * @return {@link be.raft.pelican.PteroAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.client.entities.ClientServer ClientServers}
	 */
	PteroAction<List<ClientServer>> retrieveServersByName(String name, boolean caseSensitive);
}
