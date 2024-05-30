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

package be.raft.pelican.application.entities.impl;

import be.raft.pelican.RequestAction;
import be.raft.pelican.application.entities.*;
import be.raft.pelican.application.managers.NodeManager;
import be.raft.pelican.application.managers.ServerCreationAction;
import be.raft.pelican.application.managers.UserManager;
import be.raft.pelican.entities.P4J;
import be.raft.pelican.requests.PaginationAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.impl.PaginationResponseImpl;
import be.raft.pelican.utils.StreamUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ApplicationImpl implements Application {

	private final P4J api;

	public ApplicationImpl(P4J api) {
		this.api = api;
	}

	public P4J getP4J() {
		return api;
	}

	public RequestAction<ApplicationUser> retrieveUserById(String id) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Users.GET_USER.compile(id),
				(response, request) -> new ApplicationUserImpl(response.getObject(), this));
	}

	@Override
	public RequestAction<ApplicationUser> retrieveUserById(long id) {
		return Application.super.retrieveUserById(id);
	}

	@Override
	public PaginationAction<ApplicationUser> retrieveUsers() {
		return PaginationResponseImpl.onPagination(
				api, Route.Users.LIST_USERS.compile(), (object) -> new ApplicationUserImpl(object, this));
	}

	@Override
	public RequestAction<List<ApplicationUser>> retrieveUsersByUsername(String name, boolean caseSensitive) {
		return RequestActionImpl.onExecute(api, () -> {
			Stream<ApplicationUser> users = retrieveUsers().stream();

			if (caseSensitive) {
				users = users.filter(u -> u.getUserName().contains(name));
			} else {
				users = users.filter(u -> u.getUserName().toLowerCase().contains(name.toLowerCase()));
			}

			return users.collect(StreamUtils.toUnmodifiableList());
		});
	}

	@Override
	public RequestAction<List<ApplicationUser>> retrieveUsersByEmail(String email, boolean caseSensitive) {
		return RequestActionImpl.onExecute(api, () -> {
			Stream<ApplicationUser> users = retrieveUsers().stream();

			if (caseSensitive) {
				users = users.filter(u -> u.getEmail().contains(email));
			} else {
				users = users.filter(u -> u.getEmail().toLowerCase().contains(email.toLowerCase()));
			}

			return users.collect(StreamUtils.toUnmodifiableList());
		});
	}

	@Override
	public UserManager getUserManager() {
		return new UserManagerImpl(this);
	}

	@Override
	public PaginationAction<Node> retrieveNodes() {
		return PaginationResponseImpl.onPagination(
				api, Route.Nodes.LIST_NODES.compile(), (object) -> new NodeImpl(object, this));
	}

	@Override
	public RequestAction<Node> retrieveNodeById(String id) {
		return RequestActionImpl.onRequestExecute(
				api, Route.Nodes.GET_NODE.compile(id), (response, request) -> new NodeImpl(response.getObject(), this));
	}

	@Override
	public RequestAction<Node> retrieveNodeById(long id) {
		return Application.super.retrieveNodeById(id);
	}

	@Override
	public RequestAction<List<Node>> retrieveNodesByName(String name, boolean caseSensitive) {
		return RequestActionImpl.onExecute(api, () -> {
			Stream<Node> nodes = retrieveNodes().stream();

			if (caseSensitive) {
				nodes = nodes.filter(n -> n.getName().contains(name));
			} else {
				nodes = nodes.filter(n -> n.getName().toLowerCase().contains(name.toLowerCase()));
			}

			return nodes.collect(StreamUtils.toUnmodifiableList());
		});
	}

	@Override
	public NodeManager getNodeManager() {
		return new NodeManagerImpl(this);
	}

	@Override
	public PaginationAction<ApplicationAllocation> retrieveAllocationsByNode(Node node) {
		return PaginationResponseImpl.onPagination(
				api,
				Route.Nodes.LIST_ALLOCATIONS.compile(node.getId()),
				(object) -> new ApplicationAllocationImpl(object, this));
	}

	@Override
	public RequestAction<List<ApplicationAllocation>> retrieveAllocations() {
		return RequestActionImpl.onExecute(api, () -> {
			List<ApplicationAllocation> allocations = new ArrayList<>();
			List<Node> nodes = retrieveNodes().all().execute();
			for (Node node : nodes) {
				allocations.addAll(node.retrieveAllocations().execute());
			}
			return Collections.unmodifiableList(allocations);
		});
	}

	@Override
	public RequestAction<ApplicationAllocation> retrieveAllocationById(String id) {
		return retrieveAllocations()
				.map(List::stream)
				.map(stream ->
						stream.filter(a -> a.getId().equals(id)).findFirst().orElse(null));
	}

	@Override
	public RequestAction<ApplicationAllocation> retrieveAllocationById(long id) {
		return Application.super.retrieveAllocationById(id);
	}

	protected RequestAction<ApplicationEgg> retrieveEggById(String nest, String egg) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Nests.GET_EGG.compile(nest, egg),
				(response, request) -> new ApplicationEggImpl(response.getObject()));
	}

	@Override
	public RequestAction<List<ApplicationEgg>> retrieveEggs() {
		return RequestActionImpl.onRequestExecute(api, Route.Nests.GET_EGGS.compile(), (response, request) -> {
			List<ApplicationEgg> eggs = new ArrayList<>();
			JSONObject json = response.getObject();
			for (Object o : json.getJSONArray("data")) {
				JSONObject egg = new JSONObject(o.toString());
				eggs.add(new ApplicationEggImpl(egg));
			}
			return Collections.unmodifiableList(eggs);
		});
	}

	@Override
	public RequestAction<ApplicationEgg> retrieveEggById(@NotNull String id) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Nests.GET_NEST.compile(id),
				(response, request) -> new ApplicationEggImpl(response.getObject()));
	}

	@Override
	public RequestAction<ApplicationEgg> retrieveEggById(long id) {
		return Application.super.retrieveEggById(id);
	}

	@Override
	public PaginationAction<ApplicationServer> retrieveServers() {
		return PaginationResponseImpl.onPagination(
				api, Route.Servers.LIST_SERVERS.compile(), (object) -> new ApplicationServerImpl(this, object));
	}

	@Override
	public RequestAction<ApplicationServer> retrieveServerById(String id) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Servers.GET_SERVER.compile(id),
				(response, request) -> new ApplicationServerImpl(this, response.getObject()));
	}

	@Override
	public RequestAction<ApplicationServer> retrieveServerById(long id) {
		return Application.super.retrieveServerById(id);
	}

	@Override
	public RequestAction<List<ApplicationServer>> retrieveServersByName(String name, boolean caseSensitive) {
		return retrieveServers().all().map(List::stream).map(stream -> stream.filter(
						s -> StreamUtils.compareString(s.getName(), name, caseSensitive))
				.collect(StreamUtils.toUnmodifiableList()));
	}

	@Override
	public RequestAction<List<ApplicationServer>> retrieveServersByOwner(ApplicationUser user) {
		return retrieveServers().all().map(List::stream).map(stream -> stream.filter(
						s -> s.retrieveOwner().map(ISnowflake::getIdLong).execute() == user.getIdLong())
				.collect(StreamUtils.toUnmodifiableList()));
	}

	@Override
	public RequestAction<List<ApplicationServer>> retrieveServersByNode(Node node) {
		return Application.super.retrieveServersByNode(node);
	}

	@Override
	public ServerCreationAction createServer() {
		return new CreateServerImpl(this);
	}
}
