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

import be.raft.pelican.PelicanBuilder;
import be.raft.pelican.RequestAction;
import be.raft.pelican.application.managers.NodeManager;
import be.raft.pelican.application.managers.ServerCreationAction;
import be.raft.pelican.application.managers.UserManager;
import be.raft.pelican.requests.PaginationAction;
import be.raft.pelican.utils.StreamUtils;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The core of PteroApplication. All parts of the the PteroApplication API can be accessed starting from this class.
 *
 * @see PelicanBuilder PteroBuilder
 */
public interface Application {

	/**
	 * Retrieves all of the ApplicationUsers from the Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read</b> access.
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationUser ApplicationUsers}
	 */
	PaginationAction<ApplicationUser> retrieveUsers();

	/**
	 * Retrieves an individual ApplicationUser represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The user id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the user cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.ApplicationUser ApplicationUsers}
	 */
	RequestAction<ApplicationUser> retrieveUserById(String id);

	/**
	 * Retrieves an individual ApplicationUser represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The user id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the user cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.ApplicationUser ApplicationUsers}
	 */
	default RequestAction<ApplicationUser> retrieveUserById(long id) {
		return retrieveUserById(Long.toUnsignedString(id));
	}

	/**
	 * Retrieves ApplicationUsers matching the provided username from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read</b> access.
	 *
	 * @param  name
	 *         The username
	 * @param caseSensitive
	 * 		   True - If P4J should search using case sensitivity
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the user cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationUser ApplicationUsers}
	 */
	RequestAction<List<ApplicationUser>> retrieveUsersByUsername(String name, boolean caseSensitive);

	/**
	 * Retrieves ApplicationUsers matching the provided email from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read</b> access.
	 *
	 * @param  email
	 *         The email
	 * @param caseSensitive
	 * 		   True - If P4J should search using case sensitivity
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the user cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationUser ApplicationUsers}
	 */
	RequestAction<List<ApplicationUser>> retrieveUsersByEmail(String email, boolean caseSensitive);

	/**
	 * Returns the {@link be.raft.pelican.application.managers.UserManager UserManager}, used to create, edit, and delete ApplicationUsers from the Pterodactyl instance
	 * <br>Executing any of the containing methods requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read &amp; Write</b> access.
	 *
	 * @return The User Manager
	 */
	UserManager getUserManager();

	/**
	 * Retrieves all of the Nodes from the Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nodes</b> permission with <b>Read</b> access.
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.Node Nodes}
	 */
	PaginationAction<Node> retrieveNodes();

	/**
	 * Retrieves an individual Node represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nodes</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The node id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the node cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.Node Node}
	 */
	RequestAction<Node> retrieveNodeById(String id);

	/**
	 * Retrieves an individual Node represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nodes</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The node id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the node cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.Node Node}
	 */
	default RequestAction<Node> retrieveNodeById(long id) {
		return retrieveNodeById(Long.toUnsignedString(id));
	}

	/**
	 * Retrieves Nodes matching the provided name from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nodes</b> permission with <b>Read</b> access.
	 *
	 * @param  name
	 *         The name
	 * @param caseSensitive
	 * 		   True - If P4J should search using case sensitivity
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.Node Nodes}
	 */
	RequestAction<List<Node>> retrieveNodesByName(String name, boolean caseSensitive);

	/**
	 * Returns the {@link be.raft.pelican.application.managers.NodeManager NodeManager}, used to create, edit, and delete Nodes from the Pterodactyl instance
	 * <br>Executing any of the containing methods requires an <b>Application API key</b> with the <b>Users</b> permission with <b>Read &amp; Write</b> access.
	 *
	 * @return The Node Manager
	 */
	NodeManager getNodeManager();

	/**
	 * Retrieves an individual Allocation represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Allocations</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The allocation id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the allocation cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link ApplicationAllocation Allocation}
	 */
	RequestAction<ApplicationAllocation> retrieveAllocationById(String id);

	/**
	 * Retrieves an individual Allocation represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Allocations</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The allocation id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the allocation cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link ApplicationAllocation Allocation}
	 */
	default RequestAction<ApplicationAllocation> retrieveAllocationById(long id) {
		return retrieveAllocationById(Long.toUnsignedString(id));
	}

	/**
	 * Retrieves Allocations from the provided {@link be.raft.pelican.application.entities.Node Node} from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Allocations</b> permission with <b>Read</b> access.
	 *
	 * @param  node
	 *         The node
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link ApplicationAllocation Allocations}
	 */
	PaginationAction<ApplicationAllocation> retrieveAllocationsByNode(Node node);

	/**
	 * Retrieves all of the Allocations from the Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Allocations</b> permission with <b>Read</b> access.
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link ApplicationAllocation Allocations}
	 */
	RequestAction<List<ApplicationAllocation>> retrieveAllocations();

	/**
	 * Retrieves all of the ApplicationEggs from the Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nests</b> and <b>Eggs</b> permissions with <b>Read</b> access.
	 *
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationEgg ApplicationEggs}
	 */
	RequestAction<List<ApplicationEgg>> retrieveEggs();

	/**
	 * Retrieves an individual ApplicationEgg represented by the provided {@link be.raft.pelican.application.entities.Nest Nest} and id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nests</b> and <b>Eggs</b> permissions with <b>Read</b> access.
	 *
	 * @param  nest
	 *         The nest
	 *
	 * @param  id The (integer) identifier of the egg
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the egg cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.ApplicationEgg ApplicationEgg}
	 */
	RequestAction<ApplicationEgg> retrieveEggById(@NotNull String id);

	/**
	 * Retrieves an individual ApplicationEgg represented by the provided {@link be.raft.pelican.application.entities.Nest Nest} and id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Nests</b> and <b>Eggs</b> permissions with <b>Read</b> access.
	 *
	 * @param  nest
	 *         The nest
	 *
	 * @param  id
	 * 		   The id of the egg from in nest
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the egg cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.ApplicationEgg ApplicationEgg}
	 */
	default RequestAction<ApplicationEgg> retrieveEggById(long id) {
		return retrieveEggById(Long.toUnsignedString(id));
	}

	/**
	 * Retrieves all of the ApplicationServers from the Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Servers</b> permissions with <b>Read</b> access.
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServers}
	 */
	PaginationAction<ApplicationServer> retrieveServers();

	/**
	 * Retrieves an individual ApplicationServer represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Servers</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the server cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}
	 */
	RequestAction<ApplicationServer> retrieveServerById(String id);

	/**
	 * Retrieves an individual ApplicationServer represented by the provided id from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Servers</b> permission with <b>Read</b> access.
	 *
	 * @param  id
	 *         The id
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @throws be.raft.pelican.exceptions.NotFoundException
	 * 		   If the server cannot be found
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}
	 */
	default RequestAction<ApplicationServer> retrieveServerById(long id) {
		return retrieveServerById(Long.toUnsignedString(id));
	}

	/**
	 * Retrieves ApplicationServers matching the provided name from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Servers</b> permission with <b>Read</b> access.
	 *
	 * @param  name
	 *         The name
	 * @param caseSensitive
	 * 		   True - If P4J should search using case sensitivity
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServers}
	 */
	RequestAction<List<ApplicationServer>> retrieveServersByName(String name, boolean caseSensitive);

	/**
	 * Retrieves ApplicationServers owned by the provided {@link be.raft.pelican.application.entities.ApplicationUser ApplicationUser} from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Servers</b> and <b>Users</b> permissions with <b>Read</b> access.
	 *
	 * @param  user
	 *         The owner
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServers}
	 */
	RequestAction<List<ApplicationServer>> retrieveServersByOwner(ApplicationUser user);

	/**
	 * Retrieves ApplicationServers running on the provided {@link be.raft.pelican.application.entities.Node Node} from Pterodactyl instance
	 * <br>This requires an <b>Application API key</b> with the <b>Servers</b> and <b>Nodes</b> permissions with <b>Read</b> access.
	 *
	 * @param  node
	 *         The node
	 *
	 * @throws be.raft.pelican.exceptions.LoginException
	 *         If the API key is incorrect or doesn't have the required permissions
	 *
	 * @return {@link RequestAction PteroAction} - Type {@link java.util.List List} of {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServers}
	 */
	default RequestAction<List<ApplicationServer>> retrieveServersByNode(Node node) {
		return retrieveServers().map(List::stream).map(stream -> stream.filter(
						s -> s.retrieveNode().map(ISnowflake::getIdLong).execute() == node.getIdLong())
				.collect(StreamUtils.toUnmodifiableList()));
	}

	/**
	 * Returns a {@link be.raft.pelican.application.managers.ServerCreationAction}, used to create servers on the Pterodactyl instance
	 * <br>Creating a server requires an <b>Application API key</b> with the <b>Locations</b>, <b>Nests</b> and <b>Nodes</b> with <b>Read</b> access
	 * and <b>Servers</b> and <b>Users</b> permission with <b>Read &amp; Write</b> access.
	 *
	 * @return The ServerAction used to create servers
	 */
	ServerCreationAction createServer();
}
