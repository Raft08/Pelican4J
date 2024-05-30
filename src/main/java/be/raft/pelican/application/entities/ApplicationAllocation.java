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

import be.raft.pelican.entities.Allocation;
import java.util.Optional;

/**
 * Represents a Pterodactyl {@link be.raft.pelican.application.entities.ApplicationAllocation ApplicationAllocation}.
 * This should contain all information provided from the Pterodactyl instance about a ApplicationAllocation.
 */
public interface ApplicationAllocation extends Allocation {

	/**
	 * Returns if this Allocation is assigned to an {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}
	 *
	 * @return True - if the allocation is assigned
	 */
	boolean isAssigned();

	/**
	 * The ApplicationServer associated with this Allocation
	 * <br>This method will not return a present Optional if there are no available relationships or if the Allocation is not assigned
	 *
	 * @return {@link java.util.Optional Optional} - Type {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}
	 */
	Optional<ApplicationServer> getServer();

	/**
	 * The {@link be.raft.pelican.application.entities.Node Node} associated with this Allocation
	 * <br>This method will not return a present Optional if there are no available relationships
	 *
	 * @return {@link java.util.Optional Optional} - Type {@link be.raft.pelican.application.entities.Node Node}
	 */
	Optional<Node> getNode();
}
