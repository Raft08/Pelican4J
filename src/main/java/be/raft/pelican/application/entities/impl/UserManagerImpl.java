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

import be.raft.pelican.RequestAction;
import be.raft.pelican.application.entities.ApplicationUser;
import be.raft.pelican.application.managers.UserAction;
import be.raft.pelican.application.managers.UserManager;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public class UserManagerImpl implements UserManager {

	private final ApplicationImpl impl;

	public UserManagerImpl(ApplicationImpl impl) {
		this.impl = impl;
	}

	@Override
	public UserAction createUser() {
		return new CreateUserImpl(impl);
	}

	@Override
	public UserAction editUser(ApplicationUser user) {
		return new EditUserImpl(user, impl);
	}

	@Override
	public RequestAction<Void> deleteUser(ApplicationUser user) {
		return RequestActionImpl.onRequestExecute(impl.getP4J(), Route.Users.DELETE_USER.compile(user.getId()));
	}
}
