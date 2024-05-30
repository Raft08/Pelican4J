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
import be.raft.pelican.client.entities.Backup;
import be.raft.pelican.client.entities.ClientServer;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class BackupImpl implements Backup {

	private final JSONObject json;
	private final ClientServer server;

	public BackupImpl(JSONObject json, ClientServer server) {
		this.json = json.getJSONObject("attributes");
		this.server = server;
	}

	@Override
	public UUID getUUID() {
		return UUID.fromString(json.getString("uuid"));
	}

	@Override
	public String getName() {
		return json.getString("name");
	}

	@Override
	public String getChecksum() {
		return json.optString("checksum", null);
	}

	@Override
	public boolean isSuccessful() {
		return json.getBoolean("is_successful");
	}

	@Override
	public boolean isLocked() {
		return json.getBoolean("is_locked");
	}

	@Override
	public long getSize() {
		return json.getLong("bytes");
	}

	@Override
	public List<String> getIgnoredFiles() {
		return json.getJSONArray("ignored_files").toList().stream()
				.map(Object::toString)
				.collect(Collectors.toList());
	}

	@Override
	public RequestAction<String> retrieveDownloadUrl() {
		return server.getBackupManager().retrieveDownloadUrl(this);
	}

	@Override
	public OffsetDateTime getTimeCompleted() {
		return json.isNull("completed_at") ? null : OffsetDateTime.parse(json.getString("completed_at"));
	}

	@Override
	public OffsetDateTime getTimeCreated() {
		return OffsetDateTime.parse(json.getString("created_at"));
	}

	@Override
	public RequestAction<Backup> toggleLock() {
		return server.getBackupManager().toggleLock(this);
	}

	@Override
	public RequestAction<Void> restore() {
		return server.getBackupManager().restoreBackup(this);
	}

	@Override
	public RequestAction<Void> delete() {
		return server.getBackupManager().deleteBackup(this);
	}

	@Override
	public String toString() {
		return json.toString(4);
	}
}
