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

import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.Schedule;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.AbstractScheduleTaskAction;

public class CreateScheduleTaskImpl extends AbstractScheduleTaskAction {

	public CreateScheduleTaskImpl(ClientServer server, Schedule schedule, PteroClientImpl impl) {
		super(
				impl,
				schedule,
				Route.Schedules.CREATE_TASK.compile(server.getUUID().toString(), schedule.getId()));
	}
}
