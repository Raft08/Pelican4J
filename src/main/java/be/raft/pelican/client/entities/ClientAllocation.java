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

package be.raft.pelican.client.entities;

import be.raft.pelican.RequestAction;
import be.raft.pelican.entities.Allocation;

/**
 * Represents a Pterodactyl {@link be.raft.pelican.client.entities.ClientAllocation ClientAllocation}.
 * This should contain all information provided from the Pterodactyl instance about a ClientAllocation associated with its
 * {@link be.raft.pelican.client.entities.ClientServer ClientServer}.
 */
public interface ClientAllocation extends Allocation {

	/**
	 * Returns if this is the default Allocation for the server.
	 *
	 * @return True - if the allocation is the default one
	 */
	boolean isDefault();

	/**
	 * Set the note for this Allocation.
	 *
	 * @param note The note for this Allocation
	 * @return {@link RequestAction PteroAction} -
	 * Type {@link be.raft.pelican.client.entities.ClientAllocation ClientAllocation}
	 */
	RequestAction<ClientAllocation> setNote(String note);

	/**
	 * Set this Allocation as the default/primary Allocation for the
	 * {@link be.raft.pelican.client.entities.ClientServer ClientServer}
	 *
	 * @return {@link RequestAction PteroAction} -
	 * Type {@link be.raft.pelican.client.entities.ClientAllocation ClientAllocation}
	 */
	RequestAction<ClientAllocation> setPrimary();

	/**
	 * Unassign this Allocation.
	 *
	 * @return {@link RequestAction PteroAction}
	 * @throws IllegalArgumentException If the provided allocation is the default
	 */
	RequestAction<Void> unassign();
}
