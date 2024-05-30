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

package be.raft.pelican.requests.action;

import be.raft.pelican.Permission;
import be.raft.pelican.client.entities.ClientSubuser;
import be.raft.pelican.client.entities.impl.ClientSubuserImpl;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.client.managers.SubuserAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import java.util.Arrays;
import java.util.EnumSet;

public abstract class AbstractSubuserAction extends RequestActionImpl<ClientSubuser> implements SubuserAction {

	protected EnumSet<Permission> permissions;

	public AbstractSubuserAction(PteroClientImpl impl, Route.CompiledRoute route) {
		super(impl.getP4J(), route, (response, request) -> new ClientSubuserImpl(response.getObject()));
		this.permissions = EnumSet.noneOf(Permission.class);
	}

	@Override
	public SubuserAction setPermissions(Permission... permissions) {
		this.permissions.addAll(Arrays.asList(permissions));
		return this;
	}
}
