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
import be.raft.pelican.application.entities.ApplicationDatabase;
import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.entities.impl.DatabasePasswordImpl;
import be.raft.pelican.requests.CompletedRequestAction;
import java.time.OffsetDateTime;
import org.json.JSONObject;

public class ApplicationDatabaseImpl implements ApplicationDatabase {

	private final JSONObject json;
	private final JSONObject relationships;
	private final ApplicationServer server;
	private final ApplicationImpl impl;

	public ApplicationDatabaseImpl(JSONObject json, ApplicationServer server, ApplicationImpl impl) {
		this.json = json.getJSONObject("attributes");
		this.relationships = json.getJSONObject("attributes").optJSONObject("relationships");
		this.server = server;
		this.impl = impl;
	}

	@Override
	public RequestAction<ApplicationServer> retrieveServer() {
		return new CompletedRequestAction<>(impl.getP4J(), server);
	}

	@Override
	public long getServerIdLong() {
		return json.getLong("server");
	}

	@Override
	public RequestAction<DatabaseHost> retrieveHost() {
		if (!json.has("relationships"))
			return server.retrieveDatabaseById(getIdLong()).flatMap(ApplicationDatabase::retrieveHost);
		return new CompletedRequestAction<>(
				impl.getP4J(), new ApplicationDatabaseHostImpl(relationships.getJSONObject("host"), impl));
	}

	@Override
	public long getHostIdLong() {
		return json.getLong("host");
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
	public String getName() {
		return json.getString("database");
	}

	@Override
	public String getUserName() {
		return json.getString("username");
	}

	@Override
	public String getRemote() {
		return json.getString("remote");
	}

	@Override
	public int getMaxConnections() {
		return json.optInt("max_connections");
	}

	@Override
	public RequestAction<String> retrievePassword() {
		if (!json.has("relationships"))
			return server.retrieveDatabaseById(getIdLong()).flatMap(ApplicationDatabase::retrievePassword);
		return new CompletedRequestAction<>(
				impl.getP4J(), new DatabasePasswordImpl(relationships.getJSONObject("password")).getPassword());
	}

	@Override
	public RequestAction<Void> resetPassword() {
		return server.getDatabaseManager().resetPassword(this);
	}

	@Override
	public RequestAction<Void> delete() {
		return server.getDatabaseManager().deleteDatabase(this);
	}

	@Override
	public String toString() {
		return json.toString(4);
	}
}
