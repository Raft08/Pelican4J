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

import be.raft.pelican.PteroAction;
import be.raft.pelican.application.entities.ApplicationDatabase;
import be.raft.pelican.application.entities.Node;
import java.time.OffsetDateTime;
import org.json.JSONObject;

public class ApplicationDatabaseHostImpl implements ApplicationDatabase.DatabaseHost {

	private final JSONObject json;
	private final PteroApplicationImpl impl;

	public ApplicationDatabaseHostImpl(JSONObject json, PteroApplicationImpl impl) {
		this.json = json.getJSONObject("attributes");
		this.impl = impl;
	}

	@Override
	public String getName() {
		return json.getString("name");
	}

	@Override
	public String getUserName() {
		return json.getString("username");
	}

	@Override
	public long getNodeIdLong() {
		return json.optLong("node");
	}

	@Override
	public PteroAction<Node> retrieveNode() {
		return impl.retrieveNodeById(getNodeIdLong());
	}

	@Override
	public long getIdLong() {
		return json.getLong("id");
	}

	@Override
	public OffsetDateTime getCreationDate() {
		return OffsetDateTime.parse(json.optString("created_at"));
	}

	@Override
	public OffsetDateTime getUpdatedDate() {
		return OffsetDateTime.parse(json.optString("updated_at"));
	}

	@Override
	public String getAddress() {
		return json.getString("host");
	}

	@Override
	public int getPort() {
		return json.getInt("port");
	}

	@Override
	public String toString() {
		return json.toString(4);
	}
}
