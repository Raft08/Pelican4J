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

package be.raft.pelican.client.ws.events;

import be.raft.pelican.DataType;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.impl.ClientImpl;
import be.raft.pelican.client.managers.WebSocketManager;
import org.json.JSONObject;

public class BackupCompletedEvent extends Event {

	private final JSONObject json;

	public BackupCompletedEvent(ClientImpl api, ClientServer server, WebSocketManager manager, JSONObject json) {
		super(api, server, manager);
		this.json = json;
	}

	public boolean isSuccessful() {
		return json.getBoolean("is_successful");
	}

	public String getChecksum() {
		return json.getString("checksum");
	}

	public String getChecksumType() {
		return json.getString("checksum_type");
	}

	public long getSize() {
		return json.getLong("file_size");
	}

	public String getSizeFormatted(DataType dataType) {
		return String.format("%.2f %s", getSize() / (dataType.getMbValue() * Math.pow(2, 20)), dataType.name());
	}
}
