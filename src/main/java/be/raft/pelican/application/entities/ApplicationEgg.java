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

import be.raft.pelican.EnvironmentValue;
import be.raft.pelican.application.managers.ServerController;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.managers.ClientServerManager;
import be.raft.pelican.entities.Egg;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a Pterodactyl {@link be.raft.pelican.application.entities.ApplicationEgg ApplicationEgg}.
 * This should contain all information provided from the Pterodactyl instance about an ApplicationEgg.
 */
public interface ApplicationEgg extends Egg, IdentifiedEntity {

	/**
	 * The egg variables assigned to the ApplicationEgg
	 *
	 * @return {@link java.util.Optional Optional} - Type {@link java.util.List List} of {@link EggVariable EggVariables}
	 */
	Optional<List<EggVariable>> getVariables();

	/**
	 * The default variables for this ApplicationEgg
	 *
	 * @return {@link java.util.Optional Optional} - Type {@link java.util.Map Map} of {@link EnvironmentValue EnvironmentVariables}
	 */
	Optional<Map<String, EnvironmentValue<?>>> getDefaultVariableMap();

	/**
	 * The immutable author of the ApplicationEgg formatted as an email address
	 *
	 * @return Never-null String containing the egg author's email
	 */
	String getAuthor();

	/**
	 * The description of the ApplicationEgg
	 *
	 * @return Never-null String containing the egg's description
	 */
	String getDescription();

	/**
	 * The Docker image associated with the ApplicationEgg
	 *
	 * @return Never-null String containing the egg's Docker image.
	 */
	String getDockerImage();

	List<DockerImage> getDockerImages();

	/**
	 * The stop command for the ApplicationEgg
	 * <br>This is ran when a user executes {@link ClientServer#stop()} or hits the <code>Stop</code> button on the panel
	 *
	 * @return Never-null String containing the egg's stop command
	 */
	String getStopCommand();

	/**
	 * The start command for the ApplicationEgg
	 * <br>This is ran when a user executes {@link ClientServer#start()} ()} or hits the <code>Start</code> button on the panel
	 *
	 * @return Never-null String containing the egg's start command
	 */
	String getStartupCommand();

	/**
	 * The installation script for this ApplicationEgg.
	 * <br>This script is ran when a user installs a {@link be.raft.pelican.entities.Server Server} for the first time,
	 * or triggers a reinstall
	 *
	 * @return Never-null installation script
	 * @see ClientServerManager#reinstall()
	 * @see ServerController#reinstall()
	 */
	Script getInstallScript();

	/**
	 * Represents an {@link EggVariable EggVariable} associated with an {@link ApplicationEgg}.
	 */
	interface EggVariable extends Egg.EggVariable, IdentifiedEntity {

		/**
		 * Returns whether the {@link EggVariable EggVariable} is viewable by the end-user.
		 *
		 * @return True - if the variable is viewable by the end-user.
		 */
		boolean isUserViewable();

		/**
		 * Returns whether the {@link EggVariable EggVariable} is editable by the end-user.
		 *
		 * @return True - if the variable is editable by the end-user.
		 */
		boolean isUserEditable();
	}
}
