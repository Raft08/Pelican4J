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

import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.Directory;
import be.raft.pelican.client.managers.CreateDirectoryAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.utils.Checks;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class CreateDirectoryActionImpl extends RequestActionImpl<Void> implements CreateDirectoryAction {

	private String name;
	private Directory rootDirectory;

	public CreateDirectoryActionImpl(ClientServer server, ClientImpl impl) {
		super(impl.getP4J(), Route.Files.CREATE_FOLDER.compile(server.getIdentifier()));
	}

	@Override
	public CreateDirectoryAction setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public CreateDirectoryAction setRoot(Directory directory) {
		this.rootDirectory = directory;
		return this;
	}

	@Override
	protected RequestBody finalizeData() {
		Checks.notBlank(name, "Directory Name");
		Checks.notNull(rootDirectory, "Root Directory");

		JSONObject json = new JSONObject().put("name", name).put("root", rootDirectory.getPath());
		return getRequestBody(json);
	}
}
