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
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.Directory;
import be.raft.pelican.client.entities.DownloadableFile;
import be.raft.pelican.client.entities.File;
import be.raft.pelican.client.managers.*;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Requester;
import be.raft.pelican.requests.Route;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class FileManagerImpl implements FileManager {

	private final ClientServer server;
	private final PteroClientImpl impl;

	public FileManagerImpl(ClientServer server, PteroClientImpl impl) {
		this.server = server;
		this.impl = impl;
	}

	@Override
	public CreateDirectoryAction createDirectory() {
		return new CreateDirectoryActionImpl(server, impl);
	}

	@Override
	public RequestAction<Void> createFile(Directory directory, String name, String content) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.WRITE_FILE.compile(server.getIdentifier(), directory.getPath() + "/" + name),
				RequestBody.create(Requester.MEDIA_TYPE_PLAIN, content));
	}

	@Override
	public UploadFileAction upload(Directory directory) {
		return new UploadFileActionImpl(server, directory, impl);
	}

	@Override
	public RenameAction rename() {
		return new RenameActionImpl(server, impl);
	}

	@Override
	public CompressAction compress() {
		return new CompressActionImpl(server, impl);
	}

	@Override
	public RequestAction<Void> decompress(File compressedFile) {
		return new DecompressActionImpl(server, compressedFile, impl);
	}

	@Override
	public DeleteAction delete() {
		return new DeleteActionImpl(server, impl);
	}

	@Override
	public RequestAction<String> retrieveContent(File file) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.GET_CONTENTS.compile(server.getIdentifier(), file.getPath()),
				(response, request) -> response.getRawObject());
	}

	@Override
	public RequestAction<DownloadableFile> retrieveDownload(File file) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.DOWNLOAD_FILE.compile(server.getIdentifier(), file.getPath()),
				(response, request) -> new DownloadableFile(
						impl.getP4J(),
						file,
						response.getObject().getJSONObject("attributes").getString("url")));
	}

	@Override
	public RequestAction<Void> write(File file, String content) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.WRITE_FILE.compile(server.getIdentifier(), file.getPath()),
				RequestBody.create(Requester.MEDIA_TYPE_PLAIN, content));
	}

	@Override
	public RequestAction<Void> copy(File file) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Files.COPY_FILE.compile(server.getIdentifier()),
				RequestActionImpl.getRequestBody(new JSONObject().put("location", file.getPath())));
	}
}
