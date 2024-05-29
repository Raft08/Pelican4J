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

import be.raft.pelican.application.entities.Location;
import be.raft.pelican.application.entities.impl.LocationImpl;
import be.raft.pelican.application.entities.impl.PteroApplicationImpl;
import be.raft.pelican.application.managers.LocationAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public abstract class AbstractLocationAction extends RequestActionImpl<Location> implements LocationAction {

	protected String shortCode;
	protected String description;

	public AbstractLocationAction(PteroApplicationImpl impl, Route.CompiledRoute route) {
		super(impl.getP4J(), route, (response, request) -> new LocationImpl(response.getObject(), impl));
	}

	@Override
	public LocationAction setShortCode(String shortCode) {
		this.shortCode = shortCode;
		return this;
	}

	@Override
	public LocationAction setDescription(String description) {
		this.description = description;
		return this;
	}
}
