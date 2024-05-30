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

import be.raft.pelican.EnvironmentValue;
import be.raft.pelican.RequestAction;
import be.raft.pelican.application.entities.ApplicationEgg;
import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.client.entities.ClientServer;
import java.util.Map;

/**
 * Manager providing functionality to modify the startup configuration for an
 * {@link ApplicationServer ApplicationServer}.
 *
 * <p><b>Example</b>
 * <pre>{@code
 * manager.setEgg(egg) // set the egg for the server
 *     .setImage("ghcr.io/pterodactyl/yolks:java_8") // set the docker image to yolks
 *     .executeAsync();
 * }</pre>
 *
 * @see ApplicationServer#getStartupManager()
 */
public interface ServerStartupManager extends RequestAction<ApplicationServer> {

	// The following bits are used internally, but are here in case something user-facing is added in the future

	long STARTUP_COMMAND = 0x1;
	long ENVIRONMENT = 0x2;
	long EGG = 0x4;
	long IMAGE = 0x8;
	long SKIP_SCRIPTS = 0x10;

	/**
	 * Sets the start up command of this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 *
	 * @param command The new startup command for the server
	 * @return The {@link be.raft.pelican.application.managers.ServerStartupManager ServerStartupManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided command is {@code null}
	 */
	ServerStartupManager setStartupCommand(String command);

	/**
	 * Sets the egg environment variables of this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 * <br>This method will not remove any existing environment variables, it will only replace them.
	 *
	 * @param environment The updated map
	 * @return The {@link be.raft.pelican.application.managers.ServerStartupManager ServerStartupManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided environment map is {@code null}
	 * @see EnvironmentValue
	 */
	ServerStartupManager setEnvironment(Map<String, EnvironmentValue<?>> environment);

	/**
	 * Sets the egg to use with this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 *
	 * <p><b>Note:</b> You will need to restart the server for the changes to take effect.
	 *
	 * @param egg The new egg for the server
	 * @return The {@link be.raft.pelican.application.managers.ServerStartupManager ServerStartupManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided egg is {@code null}
	 * @see ClientServer#restart()
	 */
	ServerStartupManager setEgg(ApplicationEgg egg);

	/**
	 * Sets the Docker image for the {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer} to use.
	 *
	 * <p><b>Note:</b> You will need to restart the server for the changes to take effect.
	 *
	 * @param dockerImage The new Docker image for the server
	 * @return The {@link be.raft.pelican.application.managers.ServerStartupManager ServerStartupManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided image is {@code null} or longer then 191 characters
	 * @see ClientServer#restart()
	 */
	ServerStartupManager setImage(String dockerImage);

	/**
	 * Enables/Disables Pterodactyl to run the egg install scripts for {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 * <p><b>Note:</b> This will have no effect on a server that's already installed, so it's here for consistancy.
	 *
	 * <p>Default: <b>disabled (false)</b>
	 *
	 * @param skipScripts True - will not run egg install scripts
	 * @return The {@link be.raft.pelican.application.managers.ServerStartupManager ServerStartupManager}
	 * instance, useful for chaining
	 */
	ServerStartupManager setSkipScripts(boolean skipScripts);
}
