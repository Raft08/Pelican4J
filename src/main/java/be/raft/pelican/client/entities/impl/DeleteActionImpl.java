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
import be.raft.pelican.client.entities.GenericFile;
import be.raft.pelican.client.managers.DeleteAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class DeleteActionImpl extends RequestActionImpl<Void> implements DeleteAction {

	private final List<GenericFile> files;

	public DeleteActionImpl(ClientServer server, ClientImpl impl) {
		super(impl.getP4J(), Route.Files.DELETE_FILES.compile(server.getIdentifier()));
		this.files = new ArrayList<>();
	}

	@Override
	public DeleteAction addFile(GenericFile file) {
		files.add(file);
		return this;
	}

	@Override
	public DeleteAction addFiles(GenericFile file, GenericFile... files) {
		this.files.add(file);

		if (files.length > 0) this.files.addAll(Arrays.asList(files));

		return this;
	}

	@Override
	public DeleteAction clearFiles() {
		files.clear();
		return this;
	}

	@Override
	protected RequestBody finalizeData() {
		List<String> array = files.stream().map(GenericFile::getPath).collect(Collectors.toList());

		JSONObject json = new JSONObject().put("root", "/").put("files", array);
		return getRequestBody(json);
	}
}
