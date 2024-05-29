/*
 *    Copyright 2021-2022 Matt Malec, and the Pterodactyl4J contributors
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
 */

package be.raft.pelican.application.entities.impl;

import be.raft.pelican.RequestAction;
import be.raft.pelican.application.entities.*;
import be.raft.pelican.application.managers.LocationManager;
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
import org.json.JSONObject;

public class PteroApplicationImpl implements PteroApplication {

	private final P4J api;

	public PteroApplicationImpl(P4J api) {
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
	public RequestAction<List<Node>> retrieveNodesByLocation(Location location) {
		return retrieveNodes().all().map(List::stream).map(stream -> stream.filter(
						n -> n.retrieveLocation().map(ISnowflake::getIdLong).execute() == location.getIdLong())
				.collect(StreamUtils.toUnmodifiableList()));
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
	public RequestAction<ApplicationEgg> retrieveEggById(Nest nest, String id) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Nests.GET_EGG.compile(nest.getId(), id),
				(response, request) -> new ApplicationEggImpl(response.getObject(), this));
	}

	protected RequestAction<ApplicationEgg> retrieveEggById(String nest, String egg) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Nests.GET_EGG.compile(nest, egg),
				(response, request) -> new ApplicationEggImpl(response.getObject(), this));
	}

	@Override
	public RequestAction<List<ApplicationEgg>> retrieveEggs() {
		return RequestActionImpl.onExecute(api, () -> {
			List<Nest> nests = retrieveNests().all().execute();
			List<ApplicationEgg> eggs = new ArrayList<>();
			for (Nest nest : nests) {
				eggs.addAll(nest.retrieveEggs().execute());
			}
			return Collections.unmodifiableList(eggs);
		});
	}

	@Override
	public RequestAction<List<ApplicationEgg>> retrieveEggsByNest(Nest nest) {
		return RequestActionImpl.onRequestExecute(
				api, Route.Nests.GET_EGGS.compile(nest.getId()), (response, request) -> {
					List<ApplicationEgg> eggs = new ArrayList<>();
					JSONObject json = response.getObject();
					for (Object o : json.getJSONArray("data")) {
						JSONObject egg = new JSONObject(o.toString());
						eggs.add(new ApplicationEggImpl(egg, this));
					}
					return Collections.unmodifiableList(eggs);
				});
	}

	@Override
	public RequestAction<Nest> retrieveNestById(String id) {
		return RequestActionImpl.onRequestExecute(
				api, Route.Nests.GET_NEST.compile(id), (response, request) -> new NestImpl(response.getObject(), this));
	}

	@Override
	public PaginationAction<Nest> retrieveNests() {
		return PaginationResponseImpl.onPagination(
				api, Route.Nests.LIST_NESTS.compile(), (object) -> new NestImpl(object, this));
	}

	@Override
	public RequestAction<List<Nest>> retrieveNestsByName(String name, boolean caseSensitive) {
		return RequestActionImpl.onExecute(api, () -> {
			Stream<Nest> nests = retrieveNests().stream();

			if (caseSensitive) {
				nests = nests.filter(n -> n.getName().contains(name));
			} else {
				nests = nests.filter(n -> n.getName().toLowerCase().contains(name.toLowerCase()));
			}

			return nests.collect(StreamUtils.toUnmodifiableList());
		});
	}

	@Override
	public RequestAction<List<Nest>> retrieveNestsByAuthor(String author, boolean caseSensitive) {
		return RequestActionImpl.onExecute(api, () -> {
			Stream<Nest> nests = retrieveNests().stream();

			if (caseSensitive) {
				nests = nests.filter(n -> n.getAuthor().contains(author));
			} else {
				nests = nests.filter(n -> n.getAuthor().toLowerCase().contains(author.toLowerCase()));
			}

			return nests.collect(StreamUtils.toUnmodifiableList());
		});
	}

	@Override
	public PaginationAction<Location> retrieveLocations() {
		return PaginationResponseImpl.onPagination(
				api, Route.Locations.LIST_LOCATIONS.compile(), (object) -> new LocationImpl(object, this));
	}

	@Override
	public RequestAction<Location> retrieveLocationById(String id) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Locations.GET_LOCATION.compile(id),
				((response, request) -> new LocationImpl(response.getObject(), this)));
	}

	@Override
	public RequestAction<List<Location>> retrieveLocationsByShortCode(String name, boolean caseSensitive) {
		return retrieveLocations().all().map(List::stream).map(stream -> stream.filter(
						l -> StreamUtils.compareString(l.getShortCode(), name, caseSensitive))
				.collect(StreamUtils.toUnmodifiableList()));
	}

	@Override
	public LocationManager getLocationManager() {
		return new LocationManagerImpl(this);
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
	public ServerCreationAction createServer() {
		return new CreateServerImpl(this);
	}
}
