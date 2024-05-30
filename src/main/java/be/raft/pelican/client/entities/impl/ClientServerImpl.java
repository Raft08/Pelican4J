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

import be.raft.pelican.PowerAction;
import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.*;
import be.raft.pelican.client.managers.*;
import be.raft.pelican.entities.FeatureLimit;
import be.raft.pelican.entities.Limit;
import be.raft.pelican.entities.impl.FeatureLimitImpl;
import be.raft.pelican.entities.impl.LimitImpl;
import be.raft.pelican.requests.CompletedRequestAction;
import be.raft.pelican.requests.PaginationAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.impl.PaginationResponseImpl;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClientServerImpl implements ClientServer {

	private final JSONObject json;
	private final JSONObject relationships;
	private final PteroClientImpl impl;

	public ClientServerImpl(JSONObject json, PteroClientImpl impl) {
		this.json = json.getJSONObject("attributes");
		this.relationships = json.getJSONObject("attributes").getJSONObject("relationships");
		this.impl = impl;
	}

	@Override
	public boolean isServerOwner() {
		return json.getBoolean("server_owner");
	}

	@Override
	public UUID getUUID() {
		return UUID.fromString(json.getString("uuid"));
	}

	@Override
	public long getInternalIdLong() {
		return json.getLong("internal_id");
	}

	@Override
	public String getIdentifier() {
		return json.getString("identifier");
	}

	@Override
	public String getName() {
		return json.getString("name");
	}

	@Override
	public String getDescription() {
		return json.getString("description");
	}

	@Override
	public Limit getLimits() {
		return new LimitImpl(json.getJSONObject("limits"));
	}

	@Override
	public FeatureLimit getFeatureLimits() {
		return new FeatureLimitImpl(json.getJSONObject("feature_limits"));
	}

	@Override
	public SFTP getSFTPDetails() {
		return new SFTPImpl(json.getJSONObject("sftp_details"));
	}

	@Override
	public String getInvocation() {
		return json.getString("invocation");
	}

	@Override
	public Set<String> getEggFeatures() {
		JSONArray features = json.getJSONArray("egg_features");
		if (features.length() == 0) return Collections.emptySet();

		Set<String> eggFeatures = new HashSet<>();
		features.forEach(o -> eggFeatures.add(o.toString()));
		return Collections.unmodifiableSet(eggFeatures);
	}

	@Override
	public String getNode() {
		return json.getString("node");
	}

	@Override
	public boolean isSuspended() {
		return json.getBoolean("is_suspended");
	}

	@Override
	public boolean isInstalling() {
		return json.getBoolean("is_installing");
	}

	@Override
	public boolean isTransferring() {
		return json.getBoolean("is_transferring");
	}

	@Override
	public WebSocketBuilder getWebSocketBuilder() {
		return new WebSocketBuilder(impl, this);
	}

	@Override
	public List<ClientSubuser> getSubusers() {
		List<ClientSubuser> subusers = new ArrayList<>();
		JSONObject json = relationships.getJSONObject("subusers");
		for (Object o : json.getJSONArray("data")) {
			JSONObject subuser = new JSONObject(o.toString());
			subusers.add(new ClientSubuserImpl(subuser));
		}
		return Collections.unmodifiableList(subusers);
	}

	@Override
	public RequestAction<ClientSubuser> retrieveSubuser(UUID uuid) {
		if (getSubusers().isEmpty())
			return RequestActionImpl.onRequestExecute(
					impl.getP4J(),
					Route.Subusers.GET_SUBUSER.compile(getIdentifier(), uuid.toString()),
					(response, request) -> new ClientSubuserImpl(response.getObject()));
		return new CompletedRequestAction<>(
				impl.getP4J(),
				getSubusers().stream()
						.filter(u -> u.getUUID().equals(uuid))
						.findFirst()
						.get());
	}

	@Override
	public ClientEgg getEgg() {
		return new ClientEggImpl(relationships.getJSONObject("egg"), relationships.getJSONObject("variables"));
	}

	@Override
	public SubuserManager getSubuserManager() {
		return new SubuserManagerImpl(this, impl);
	}

	@Override
	public ClientServerManager getManager() {
		return new ClientServerManager(this, impl);
	}

	@Override
	public PaginationAction<Backup> retrieveBackups() {
		return PaginationResponseImpl.onPagination(
				impl.getP4J(),
				Route.Backups.LIST_BACKUPS.compile(getIdentifier()),
				(object) -> new BackupImpl(object, this));
	}

	@Override
	public RequestAction<Backup> retrieveBackup(UUID uuid) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Backups.GET_BACKUP.compile(getIdentifier(), uuid.toString()),
				(response, request) -> new BackupImpl(response.getObject(), this));
	}

	@Override
	public BackupManager getBackupManager() {
		return new BackupManagerImpl(this, impl);
	}

	@Override
	public RequestAction<List<Schedule>> retrieveSchedules() {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Schedules.LIST_SCHEDULES.compile(getIdentifier()), (response, request) -> {
					JSONObject json = response.getObject();
					List<Schedule> schedules = new ArrayList<>();
					for (Object o : json.getJSONArray("data")) {
						JSONObject schedule = new JSONObject(o.toString());
						schedules.add(new ScheduleImpl(schedule, this, impl));
					}
					return Collections.unmodifiableList(schedules);
				});
	}

	@Override
	public RequestAction<Schedule> retrieveSchedule(String id) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Schedules.GET_SCHEDULE.compile(getIdentifier(), id),
				(response, request) -> new ScheduleImpl(response.getObject(), this, impl));
	}

	@Override
	public ScheduleManager getScheduleManager() {
		return new ScheduleManagerImpl(this, impl);
	}

	@Override
	public RequestAction<Utilization> retrieveUtilization() {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Client.GET_UTILIZATION.compile(getIdentifier()),
				(response, request) -> new UtilizationImpl(response.getObject()));
	}

	@Override
	public RequestAction<Void> setPower(PowerAction powerAction) {
		JSONObject obj = new JSONObject().put("signal", powerAction.name().toLowerCase());
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Client.SET_POWER.compile(getIdentifier()), RequestActionImpl.getRequestBody(obj));
	}

	@Override
	public RequestAction<Void> sendCommand(String command) {
		JSONObject obj = new JSONObject().put("command", command);
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Client.SEND_COMMAND.compile(getIdentifier()),
				RequestActionImpl.getRequestBody(obj));
	}

	@Override
	public FileManager getFileManager() {
		return new FileManagerImpl(this, impl);
	}

	@Override
	public RequestAction<Directory> retrieveDirectory(String path) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.LIST_FILES.compile(getIdentifier(), path),
				(response, request) -> new RootDirectoryImpl(response.getObject(), path, this));
	}

	@Override
	public RequestAction<Directory> retrieveDirectory(Directory previousDirectory, Directory directory) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.LIST_FILES.compile(getIdentifier(), directory.getPath()),
				(response, request) -> new DirectoryImpl(response.getObject(), directory, this));
	}

	@Override
	public RequestAction<List<ClientDatabase>> retrieveDatabases() {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.ClientDatabases.LIST_DATABASES.compile(getIdentifier()), (response, request) -> {
					JSONObject json = response.getObject();
					List<ClientDatabase> databases = new ArrayList<>();
					for (Object o : json.getJSONArray("data")) {
						JSONObject database = new JSONObject(o.toString());
						databases.add(new ClientDatabaseImpl(database, impl, this));
					}
					return Collections.unmodifiableList(databases);
				});
	}

	@Override
	public ClientDatabaseManager getDatabaseManager() {
		return new ClientDatabaseManagerImpl(this, impl);
	}

	@Override
	public List<ClientAllocation> getAllocations() {
		List<ClientAllocation> allocations = new ArrayList<>();
		JSONObject json = relationships.getJSONObject("allocations");
		for (Object o : json.getJSONArray("data")) {
			JSONObject allocation = new JSONObject(o.toString());
			allocations.add(new ClientAllocationImpl(allocation, this));
		}
		return Collections.unmodifiableList(allocations);
	}

	@Override
	public ClientAllocationManager getAllocationManager() {
		return new ClientAllocationManagerImpl(this, impl);
	}

	@Override
	public String toString() {
		return json.toString(4);
	}
}
