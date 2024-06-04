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

package be.raft.pelican.application.entities;

import be.raft.pelican.RequestAction;
import be.raft.pelican.entities.Database;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Database} for an {@link ApplicationServer}.
 */
public interface ApplicationDatabase extends Database, IdentifiedEntity {

	/**
	 * Retrieve the {@link ApplicationServer} this is from.
	 *
	 * @return Server the database is from
	 */
	RequestAction<ApplicationServer> retrieveServer();

	/**
	 * Retrieve the server id as a long.
	 *
	 * @return Server id
	 */
	long getServerIdLong();

	/**
	 * Retrieve the server id as a {@link String}.
	 *
	 * @return Server id
	 */
	@NotNull
	default String getServerId() {
		return Long.toUnsignedString(getServerIdLong());
	}

	/**
	 * Retrieve the {@link DatabaseHost}.
	 *
	 * @return Host
	 */
	RequestAction<DatabaseHost> retrieveHost();

	/**
	 * Retrieve the host id as a long.
	 *
	 * @return Host id
	 */
	long getHostIdLong();

	/**
	 * Retrieve the host id as a {@link String}.
	 *
	 * @return Host id
	 */
	@NotNull
	default String getHostId() {
		return Long.toUnsignedString(getHostIdLong());
	}

	/**
	 * Reset the password of the database.
	 *
	 * @return Void
	 */
	RequestAction<Void> resetPassword();

	/**
	 * Represents a {@link DatabaseHost} for a {@link ApplicationServer}.
	 */
	interface DatabaseHost extends Database.DatabaseHost, IdentifiedEntity {

		/**
		 * Retrieve the name associated with this {@link DatabaseHost}.
		 *
		 * @return Name
		 */
		String getName();

		/**
		 * Retrieve the username associated with this {@link DatabaseHost}.
		 *
		 * @return Username
		 */
		String getUserName();

		/**
		 * Retrieve the node id for this {@link DatabaseHost}.
		 *
		 * @return Node id
		 */
		long getNodeIdLong();

		/**
		 * Retrieve the node id for this {@link DatabaseHost} as a {@link String}.
		 *
		 * @return Node id
		 */
		default String getNodeId() {
			return Long.toUnsignedString(getNodeIdLong());
		}

		/**
		 * Retrieve the {@link Node} associated with this {@link DatabaseHost}.
		 *
		 * @return Node
		 */
		RequestAction<Node> retrieveNode();
	}
}
