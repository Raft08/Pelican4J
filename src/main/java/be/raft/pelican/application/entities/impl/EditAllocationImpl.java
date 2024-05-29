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

import be.raft.pelican.application.entities.ApplicationAllocation;
import be.raft.pelican.application.entities.Node;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.AbstractAllocationAction;
import java.util.function.Consumer;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class EditAllocationImpl extends AbstractAllocationAction {

	private final PteroApplicationImpl impl;
	private final ApplicationAllocation allocation;

	public EditAllocationImpl(PteroApplicationImpl impl, Node node, ApplicationAllocation allocation) {
		super(impl, Route.Nodes.CREATE_ALLOCATION.compile(node.getId()));
		this.impl = impl;
		this.allocation = allocation;
	}

	@Override
	public Void execute() {
		RequestActionImpl.onRequestExecute(impl.getP4J(), Route.Nodes.DELETE_ALLOCATION.compile(allocation.getId()))
				.execute();
		return super.execute();
	}

	@Override
	public void executeAsync(Consumer<? super Void> success, Consumer<? super Throwable> failure) {
		RequestActionImpl.onRequestExecute(impl.getP4J(), Route.Nodes.DELETE_ALLOCATION.compile(allocation.getId()))
				.executeAsync();
		super.executeAsync(success, failure);
	}

	@Override
	protected RequestBody finalizeData() {
		JSONObject json = new JSONObject();
		json.put("ip", ip == null ? allocation.getIP() : ip);
		json.put("alias", alias == null ? allocation.getAlias() : alias);
		json.put("ports", portSet);
		return getRequestBody(json);
	}
}
