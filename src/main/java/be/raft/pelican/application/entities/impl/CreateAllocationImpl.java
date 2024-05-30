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

import be.raft.pelican.application.entities.Node;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.AbstractAllocationAction;
import be.raft.pelican.utils.Checks;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class CreateAllocationImpl extends AbstractAllocationAction {

	public CreateAllocationImpl(ApplicationImpl impl, Node node) {
		super(impl, Route.Nodes.CREATE_ALLOCATION.compile(node.getId()));
	}

	@Override
	protected RequestBody finalizeData() {
		JSONObject json = new JSONObject();
		Checks.notBlank(ip, "IP");
		if (alias != null) Checks.check(alias.length() <= 191, "Allocation alias cannot be longer than 191 characters");

		json.put("ip", ip);
		json.put("alias", alias);
		json.put("ports", portSet);
		return getRequestBody(json);
	}
}
