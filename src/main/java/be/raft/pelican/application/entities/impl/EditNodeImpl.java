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

import be.raft.pelican.application.entities.Node;
import be.raft.pelican.requests.Route;
import be.raft.pelican.requests.action.AbstractNodeAction;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class EditNodeImpl extends AbstractNodeAction {

	private final Node node;

	EditNodeImpl(ApplicationImpl impl, Node node) {
		super(impl, Route.Nodes.EDIT_NODE.compile(node.getId()));
		this.node = node;
	}

	@Override
	protected RequestBody finalizeData() {
		JSONObject json = new JSONObject();
		json.put("name", name == null ? node.getName() : name);
		json.put("public", isPublic == null ? (node.isPublic() ? "1" : "0") : (isPublic ? "1" : "0"));
		json.put("fqdn", fqdn == null ? node.getFQDN() : fqdn);
		json.put("scheme", secure == null ? node.getScheme() : (secure ? "https" : "http"));
		json.put(
				"behind_proxy",
				isBehindProxy == null ? (node.isBehindProxy() ? "1" : "0") : (isBehindProxy ? "1" : "0"));
		json.put("daemon_base", daemonBase == null ? node.getDaemonBase() : daemonBase);
		json.put("memory", memory == null ? node.getMemoryLong() : Long.parseUnsignedLong(memory));
		json.put(
				"memory_overallocate",
				memoryOverallocate == null
						? node.getMemoryOverallocateLong()
						: Long.parseUnsignedLong(memoryOverallocate));
		json.put("disk", diskSpace == null ? node.getDiskLong() : Long.parseUnsignedLong(diskSpace));
		json.put(
				"disk_overallocate",
				diskSpaceOverallocate == null
						? node.getDiskOverallocateLong()
						: Long.parseUnsignedLong(diskSpaceOverallocate));
		json.put("daemon_listen", daemonListenPort == null ? node.getDaemonListenPort() : daemonListenPort);
		json.put("daemon_sftp", daemonSFTPPort == null ? node.getDaemonSFTPPort() : daemonSFTPPort);
		json.put(
				"throttle",
				throttle == null ? new JSONObject().put("enabled", false) : new JSONObject().put("enabled", throttle));
		return getRequestBody(json);
	}
}
