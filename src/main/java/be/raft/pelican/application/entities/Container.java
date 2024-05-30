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
import java.util.Map;

/**
 * Represents a Pterodactyl {@link be.raft.pelican.application.entities.Container Container}.
 * This should contain all information provided from the Pterodactyl instance about an Container.
 *
 * @see ApplicationServer#getContainer()
 */
public interface Container {

	/**
	 * The start up command of the ApplicationServer
	 *
	 * @return Never-null String containing the Servers's start up command.
	 */
	String getStartupCommand();

	/**
	 * The Docker image used to run the ApplicationServer
	 *
	 * @return Never-null String containing the Servers's Docker image.
	 */
	String getImage();

	/**
	 * Returns whether the ApplicationServer is currently in an installing state
	 *
	 * @return True - if the Server has finished installing.
	 * @see ApplicationServer#getStatus()
	 */
	boolean isInstalled();

	/**
	 * The map of environment variables for the ApplicationServer
	 *
	 * @return The map of environment variables
	 */
	Map<String, EnvironmentValue<?>> getEnvironment();
}
