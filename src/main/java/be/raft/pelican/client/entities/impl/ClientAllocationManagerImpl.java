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

package be.raft.pelican.client.entities.impl;

import static be.raft.pelican.requests.RequestActionImpl.getRequestBody;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.ClientAllocation;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.managers.ClientAllocationManager;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.utils.Checks;
import org.json.JSONObject;

public class ClientAllocationManagerImpl implements ClientAllocationManager {

	private final ClientServer server;
	private final PteroClientImpl impl;

	public ClientAllocationManagerImpl(ClientServer server, PteroClientImpl impl) {
		this.server = server;
		this.impl = impl;
	}

	@Override
	public RequestAction<ClientAllocation> setNote(ClientAllocation allocation, String note) {
		JSONObject json = new JSONObject().put("notes", note);

		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.ClientAllocations.SET_NOTE.compile(server.getIdentifier(), allocation.getId()),
				getRequestBody(json),
				(response, request) -> new ClientAllocationImpl(response.getObject(), server));
	}

	@Override
	public RequestAction<Void> unassignAllocation(ClientAllocation allocation) {
		Checks.check(!allocation.isDefault(), "Cannot unassign default Allocation");
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.ClientAllocations.DELETE_ALLOCATION.compile(server.getIdentifier(), allocation.getId()));
	}

	@Override
	public RequestAction<ClientAllocation> assignAllocation() {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.ClientAllocations.ASSIGN_ALLOCATION.compile(server.getIdentifier()),
				(response, request) -> new ClientAllocationImpl(response.getObject(), server));
	}

	@Override
	public RequestAction<ClientAllocation> setPrimary(ClientAllocation allocation) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.ClientAllocations.SET_PRIMARY.compile(server.getIdentifier(), allocation.getId()),
				(response, request) -> new ClientAllocationImpl(response.getObject(), server));
	}
}
