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

package be.raft.pelican.client.entities.impl;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.Schedule;
import be.raft.pelican.client.managers.ScheduleTaskAction;
import be.raft.pelican.client.managers.ScheduleTaskManager;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public class ScheduleTaskManagerImpl implements ScheduleTaskManager {

	private final ClientServer server;
	private final Schedule schedule;
	private final ClientImpl impl;

	public ScheduleTaskManagerImpl(ClientServer server, Schedule schedule, ClientImpl impl) {
		this.server = server;
		this.schedule = schedule;
		this.impl = impl;
	}

	@Override
	public ScheduleTaskAction createTask() {
		return new CreateScheduleTaskImpl(server, schedule, impl);
	}

	@Override
	public ScheduleTaskAction editTask(Schedule.ScheduleTask task) {
		return new EditScheduleTaskImpl(server, schedule, task, impl);
	}

	@Override
	public RequestAction<Void> deleteTask(Schedule.ScheduleTask task) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Schedules.DELETE_TASK.compile(server.getUUID().toString(), schedule.getId(), task.getId()));
	}
}
