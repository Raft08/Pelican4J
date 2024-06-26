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

import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.Cron;
import be.raft.pelican.client.entities.Schedule;
import be.raft.pelican.client.entities.impl.PteroClientImpl;
import be.raft.pelican.client.entities.impl.ScheduleImpl;
import be.raft.pelican.client.managers.ScheduleAction;
import be.raft.pelican.requests.PteroActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.utils.CronUtils;

public abstract class AbstractScheduleAction extends PteroActionImpl<Schedule> implements ScheduleAction {

	protected String name;
	protected Boolean active;
	protected Boolean whenServerIsOnline;
	protected Cron cron;
	protected String minute;
	protected String hour;
	protected String dayOfWeek;
	protected String dayOfMonth;
	protected String month;

	public AbstractScheduleAction(PteroClientImpl impl, ClientServer server, Route.CompiledRoute route) {
		super(impl.getP4J(), route, (response, request) -> new ScheduleImpl(response.getObject(), server, impl));
	}

	@Override
	public ScheduleAction setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public ScheduleAction setActive(boolean active) {
		this.active = active;
		return this;
	}

	@Override
	public ScheduleAction setWhenServerIsOnline(boolean whenServerIsOnline) {
		this.whenServerIsOnline = whenServerIsOnline;
		return this;
	}

	@Override
	public ScheduleAction setCron(Cron cron) {
		this.cron = cron;
		return this;
	}

	@Override
	public ScheduleAction setCronExpression(String expression) {
		this.cron = CronUtils.ofExpression(expression);
		return this;
	}

	@Override
	public ScheduleAction setMinute(String minute) {
		this.minute = minute;
		return this;
	}

	@Override
	public ScheduleAction setHour(String hour) {
		this.hour = hour;
		return this;
	}

	@Override
	public ScheduleAction setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	@Override
	public ScheduleAction setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
		return this;
	}

	@Override
	public ScheduleAction setMonth(String month) {
		this.month = month;
		return this;
	}
}
