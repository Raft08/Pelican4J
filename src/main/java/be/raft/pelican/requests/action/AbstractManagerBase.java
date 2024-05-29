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

package be.raft.pelican.requests.action;

import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.application.entities.impl.ApplicationServerImpl;
import be.raft.pelican.application.entities.impl.PteroApplicationImpl;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public abstract class AbstractManagerBase extends RequestActionImpl<ApplicationServer> {

	protected long set = 0;

	public AbstractManagerBase(PteroApplicationImpl impl, Route.CompiledRoute route) {
		super(impl.getP4J(), route, (response, request) -> new ApplicationServerImpl(impl, response.getObject()));
	}

	protected boolean shouldUpdate(long bit) {
		return (set & bit) == bit;
	}

	protected void reset() {
		this.set = 0;
	}
}
