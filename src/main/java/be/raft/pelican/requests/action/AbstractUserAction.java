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

import be.raft.pelican.application.entities.ApplicationUser;
import be.raft.pelican.application.entities.impl.ApplicationUserImpl;
import be.raft.pelican.application.entities.impl.PteroApplicationImpl;
import be.raft.pelican.application.managers.UserAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public abstract class AbstractUserAction extends RequestActionImpl<ApplicationUser> implements UserAction {

	protected String userName;
	protected String email;
	protected String firstName;
	protected String lastName;
	protected String password;

	public AbstractUserAction(PteroApplicationImpl impl, Route.CompiledRoute route) {
		super(impl.getP4J(), route, (response, request) -> new ApplicationUserImpl(response.getObject(), impl));
	}

	@Override
	public UserAction setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	@Override
	public UserAction setEmail(String email) {
		this.email = email;
		return this;
	}

	@Override
	public UserAction setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	@Override
	public UserAction setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	@Override
	public UserAction setPassword(String password) {
		this.password = password;
		return this;
	}
}
