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

package be.raft.pelican.client.entities.impl;

import be.raft.pelican.ClientType;
import be.raft.pelican.PowerAction;
import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.Account;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.PteroClient;
import be.raft.pelican.client.entities.Utilization;
import be.raft.pelican.entities.P4J;
import be.raft.pelican.requests.PaginationAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.impl.PaginationResponseImpl;
import be.raft.pelican.utils.StreamUtils;
import java.util.List;
import java.util.stream.Stream;
import org.json.JSONObject;

public class PteroClientImpl implements PteroClient {

	private final P4J api;

	public PteroClientImpl(P4J api) {
		this.api = api;
	}

	public P4J getP4J() {
		return api;
	}

	@Override
	public RequestAction<Account> retrieveAccount() {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Accounts.GET_ACCOUNT.compile(),
				(response, request) -> new AccountImpl(response.getObject(), this));
	}

	@Override
	public RequestAction<Void> setPower(ClientServer server, PowerAction powerAction) {
		JSONObject obj = new JSONObject().put("signal", powerAction.name().toLowerCase());
		return RequestActionImpl.onRequestExecute(
				api, Route.Client.SET_POWER.compile(server.getIdentifier()), RequestActionImpl.getRequestBody(obj));
	}

	@Override
	public RequestAction<Void> sendCommand(ClientServer server, String command) {
		JSONObject obj = new JSONObject().put("command", command);
		return RequestActionImpl.onRequestExecute(
				api, Route.Client.SEND_COMMAND.compile(server.getIdentifier()), RequestActionImpl.getRequestBody(obj));
	}

	@Override
	public RequestAction<Utilization> retrieveUtilization(ClientServer server) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Client.GET_UTILIZATION.compile(server.getIdentifier()),
				(response, request) -> new UtilizationImpl(response.getObject()));
	}

	@Override
	public PaginationAction<ClientServer> retrieveServers(ClientType type) {
		Route.CompiledRoute route = Route.Client.LIST_SERVERS.compile();
		return PaginationResponseImpl.onPagination(
				api,
				type == ClientType.NONE ? route : route.withQueryParams("type", type.toString()),
				(object) -> new ClientServerImpl(object, this));
	}

	@Override
	public RequestAction<ClientServer> retrieveServerByIdentifier(String identifier) {
		return RequestActionImpl.onRequestExecute(
				api,
				Route.Client.GET_SERVER.compile(identifier),
				(response, request) -> new ClientServerImpl(response.getObject(), this));
	}

	@Override
	public RequestAction<List<ClientServer>> retrieveServersByName(String name, boolean caseSensitive) {
		return RequestActionImpl.onExecute(api, () -> {
			Stream<ClientServer> servers = retrieveServers().stream();

			if (caseSensitive) {
				servers = servers.filter(s -> s.getName().contains(name));
			} else {
				servers = servers.filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()));
			}

			return servers.collect(StreamUtils.toUnmodifiableList());
		});
	}
}
