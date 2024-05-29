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
import be.raft.pelican.application.entities.Location;
import be.raft.pelican.application.managers.LocationAction;
import be.raft.pelican.application.managers.LocationManager;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public class LocationManagerImpl implements LocationManager {

	private final PteroApplicationImpl impl;

	public LocationManagerImpl(PteroApplicationImpl impl) {
		this.impl = impl;
	}

	@Override
	public LocationAction createLocation() {
		return new CreateLocationImpl(impl);
	}

	@Override
	public LocationAction editLocation(Location location) {
		return new EditLocationImpl(location, impl);
	}

	@Override
	public RequestAction<Void> deleteLocation(Location location) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Locations.DELETE_LOCATION.compile(location.getId()));
	}
}
