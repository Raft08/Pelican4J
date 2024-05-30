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
import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.application.entities.ApplicationUser;

/**
 * Manager providing functionality to modify details for an
 * {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
 *
 * <p><b>Example</b>
 * <pre>{@code
 * manager.setName("Amazing server") // set the server name
 *     .remove(ServerDetailManager.DESCRIPTION) // remove the description
 *     .executeAsync();
 * }</pre>
 *
 * @see ApplicationServer#getDetailManager()
 */
public interface ServerDetailManager extends RequestAction<ApplicationServer> {

	/**
	 * Used to remove the server description
	 */
	long DESCRIPTION = 0x1;
	/**
	 * Used to remove the external id
	 */
	long EXTERNAL_ID = 0x2;

	/**
	 * Removes the fields specified by the provided bit-flag pattern.
	 * You can specify a combination by using a bitwise OR concat of the flag constants.
	 *
	 * <p><b>Example</b>
	 * <pre>{@code
	 *   manager.remove(ServerDetailAction.DESCRIPTION | ServerDetailAction.EXTERNAL_ID).executeAsync();
	 * }</pre>
	 *
	 * <p><b>Flag Constants:</b>
	 * <ul>
	 *     <li>{@link #DESCRIPTION}</li>
	 *     <li>{@link #EXTERNAL_ID}</li>
	 * </ul>
	 *
	 * @param fields Integer value containing the flags to remove.
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager} instance, useful for chaining
	 */
	ServerDetailManager remove(long fields);

	/**
	 * Removes the fields specified by the provided bit-flag patterns.
	 * You can specify a combination by using a bitwise OR concat of the flag constants.
	 *
	 * <p><b>Example</b>
	 * <pre>{@code
	 *   manager.remove(ServerDetailAction.DESCRIPTION, ServerDetailAction.EXTERNAL_ID).executeAsync();
	 * }</pre>
	 *
	 * <p><b>Flag Constants:</b>
	 * <ul>
	 *     <li>{@link #DESCRIPTION}</li>
	 *     <li>{@link #EXTERNAL_ID}</li>
	 * </ul>
	 *
	 * @param fields Integer values containing the flags to remove.
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager}
	 * instance, useful for chaining
	 */
	ServerDetailManager remove(long... fields);

	/**
	 * Removes all the removable elements
	 *
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager}
	 * instance, useful for chaining
	 */
	ServerDetailManager remove();

	/**
	 * Sets the name of this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 *
	 * @param name The new name for the server
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided name is {@code null} or not between 1-191 characters long
	 */
	ServerDetailManager setName(String name);

	/**
	 * Sets the owner of this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 *
	 * @param user The new owner for the server
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided user is {@code null}
	 */
	ServerDetailManager setOwner(ApplicationUser user);

	/**
	 * Sets the description of this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 *
	 * @param description The new description for the server or use {@link ServerDetailManager#remove(long)} to remove
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager}
	 * instance, useful for chaining
	 * @see ServerDetailManager#remove(long)
	 * @see ServerDetailManager#remove(long...)
	 */
	ServerDetailManager setDescription(String description);

	/**
	 * Sets the external id of this {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
	 *
	 * @param id The new external for the server or use {@link ServerDetailManager#remove(long)} to remove
	 * @return The {@link be.raft.pelican.application.managers.ServerDetailManager ServerDetailManager}
	 * instance, useful for chaining
	 * @throws IllegalArgumentException If the provided id is not between 1-191 characters long
	 * @see ServerDetailManager#remove(long)
	 * @see ServerDetailManager#remove(long...)
	 */
	ServerDetailManager setExternalId(String id);
}
