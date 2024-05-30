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

import be.raft.pelican.EnvironmentValue;
import be.raft.pelican.entities.Egg;
import java.util.List;

/**
 * Represents a Pterodactyl {@link be.raft.pelican.client.entities.ClientEgg ClientEgg}.
 * This should contain all information provided from the Pterodactyl instance about a ClientEgg.
 */
public interface ClientEgg extends Egg {

	/**
	 * The egg variables assigned to the ClientEgg
	 *
	 * @return {@link java.util.List List} - Type of {@link ClientEgg.EggVariable EggVariables}
	 */
	List<EggVariable> getVariables();

	/**
	 * Represents an {@link ClientEgg.EggVariable EggVariable} associated with a {@link ClientEgg}.
	 */
	interface EggVariable extends Egg.EggVariable {

		/**
		 * The server value of the EggVariable. This is the current value used by the server
		 *
		 * @return Never-null {@link be.raft.pelican.EnvironmentValue EnvironmentValue} containing the variable's server value.
		 */
		EnvironmentValue<?> getServerValue();

		/**
		 * Returns whether the {@link ClientEgg.EggVariable EggVariable} is editable
		 *
		 * @return True - if the variable is editable
		 */
		boolean isEditable();
	}
}
