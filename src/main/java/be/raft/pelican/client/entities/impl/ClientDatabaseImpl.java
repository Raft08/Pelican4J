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
import be.raft.pelican.client.entities.ClientDatabase;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.entities.impl.DatabasePasswordImpl;
import be.raft.pelican.requests.CompletedRequestAction;
import java.util.Optional;
import org.json.JSONObject;

public class ClientDatabaseImpl implements ClientDatabase {

	private final JSONObject json;
	private final JSONObject relationships;

	private final ClientImpl impl;
	private final ClientServer server;

	public ClientDatabaseImpl(JSONObject json, ClientImpl impl, ClientServer server) {
		this.json = json.getJSONObject("attributes");
		this.relationships = json.getJSONObject("attributes").optJSONObject("relationships");
		this.impl = impl;
		this.server = server;
	}

	@Override
	public String getId() {
		return json.getString("id");
	}

	@Override
	public DatabaseHost getHost() {
		return new ClientDatabaseHostImpl(json.getJSONObject("host"));
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
	public String getRemote() {
		return json.getString("connections_from");
	}

	@Override
	public int getMaxConnections() {
		return json.optInt("max_connections");
	}

	@Override
	public RequestAction<String> retrievePassword() {
		if (!json.has("relationships"))
			return server.retrieveDatabaseById(getId()).map(Optional::get).flatMap(ClientDatabase::retrievePassword);
		return new CompletedRequestAction<>(
				impl.getP4J(), new DatabasePasswordImpl(relationships.getJSONObject("password")).getPassword());
	}

	@Override
	public RequestAction<ClientDatabase> resetPassword() {
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
