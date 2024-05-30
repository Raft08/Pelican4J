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

import be.raft.pelican.entities.P4J;
import be.raft.pelican.requests.Request;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Response;
import be.raft.pelican.requests.Route;
import java.util.function.BiFunction;

public abstract class AbstractDatabaseAction<T> extends RequestActionImpl<T> {

	protected String name;
	protected String remote;

	public AbstractDatabaseAction(P4J api, Route.CompiledRoute route, BiFunction<Response, Request<T>, T> handler) {
		super(api, route, handler);
	}
}
