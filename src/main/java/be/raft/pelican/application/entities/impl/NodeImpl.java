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
import be.raft.pelican.application.entities.ApplicationAllocation;
import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.application.entities.Node;
import be.raft.pelican.application.managers.ApplicationAllocationManager;
import be.raft.pelican.application.managers.NodeAction;
import be.raft.pelican.requests.CompletedPaginationAction;
import be.raft.pelican.requests.CompletedRequestAction;
import be.raft.pelican.requests.PaginationAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class NodeImpl implements Node {

	private final JSONObject json;
	private final JSONObject relationships;
	private final ApplicationImpl impl;

	public NodeImpl(JSONObject json, ApplicationImpl impl) {
		this.json = json.getJSONObject("attributes");
		this.relationships = json.getJSONObject("attributes").optJSONObject("relationships");
		this.impl = impl;
	}

	@Override
	public boolean isPublic() {
		return json.getBoolean("public");
	}

	@Override
	public String getName() {
		return json.getString("name");
	}

	@Override
	public String getDescription() {
		return json.optString("description", null);
	}

	@Override
	public ApplicationAllocationManager getAllocationManager() {
		return new ApplicationAllocationManagerImpl(this, impl);
	}

	@Override
	public String getFQDN() {
		return json.getString("fqdn");
	}

	@Override
	public String getScheme() {
		return json.getString("scheme");
	}

	@Override
	public boolean isBehindProxy() {
		return json.getBoolean("behind_proxy");
	}

	@Override
	public boolean hasMaintanceMode() {
		return json.getBoolean("maintenance_mode");
	}

	@Override
	public long getMemoryLong() {
		return json.getLong("memory");
	}

	@Override
	public long getMemoryOverallocateLong() {
		return json.getLong("memory_overallocate");
	}

	@Override
	public long getDiskLong() {
		return json.getLong("disk");
	}

	@Override
	public long getDiskOverallocateLong() {
		return json.getLong("disk_overallocate");
	}

	@Override
	public long getUploadLimitLong() {
		return json.getLong("upload_size");
	}

	@Override
	public long getAllocatedMemoryLong() {
		return json.getJSONObject("allocated_resources").getLong("memory");
	}

	@Override
	public long getAllocatedDiskLong() {
		return json.getJSONObject("allocated_resources").getLong("disk");
	}

	@Override
	public String getDaemonListenPort() {
		return Long.toUnsignedString(json.getLong("daemon_listen"));
	}

	@Override
	public String getDaemonSFTPPort() {
		return Long.toUnsignedString(json.getLong("daemon_sftp"));
	}

	@Override
	public String getDaemonBase() {
		return json.getString("daemon_base");
	}

	@Override
	public RequestAction<List<ApplicationServer>> retrieveServers() {
		if (!json.has("relationships")) return impl.retrieveServersByNode(this);

		List<ApplicationServer> servers = new ArrayList<>();
		JSONObject json = relationships.getJSONObject("servers");
		for (Object o : json.getJSONArray("data")) {
			JSONObject server = new JSONObject(o.toString());
			servers.add(new ApplicationServerImpl(impl, server));
		}
		return new CompletedRequestAction<>(impl.getP4J(), Collections.unmodifiableList(servers));
	}

	@Override
	public PaginationAction<ApplicationAllocation> retrieveAllocations() {
		if (!json.has("relationships")) return impl.retrieveAllocationsByNode(this);

		List<ApplicationAllocation> allocations = new ArrayList<>();
		JSONObject json = relationships.getJSONObject("allocations");
		for (Object o : json.getJSONArray("data")) {
			JSONObject allocation = new JSONObject(o.toString());
			allocations.add(new ApplicationAllocationImpl(allocation, impl));
		}
		return new CompletedPaginationAction<>(impl.getP4J(), Collections.unmodifiableList(allocations));
	}

	@Override
	public RequestAction<Configuration> retrieveConfiguration() {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Nodes.GET_CONFIGURATION.compile(getId()),
				(response, request) -> new NodeConfigurationImpl(response.getObject()));
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
	public String toString() {
		return json.toString(4);
	}

	@Override
	public NodeAction edit() {
		return new EditNodeImpl(impl, this);
	}

	@Override
	public RequestAction<Void> delete() {
		return RequestActionImpl.onRequestExecute(impl.getP4J(), Route.Nodes.DELETE_NODE.compile(getId()));
	}
}
