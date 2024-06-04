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
import be.raft.pelican.client.managers.UploadFileAction;
import be.raft.pelican.entities.PelicanApi;
import be.raft.pelican.exceptions.FileUploadException;
import be.raft.pelican.requests.*;
import be.raft.pelican.requests.Request;
import be.raft.pelican.requests.Response;
import be.raft.pelican.requests.Route;
import be.raft.pelican.utils.BufferedRequestBody;
import be.raft.pelican.utils.Checks;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import okhttp3.*;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class UploadFileActionImpl extends RequestActionImpl<Void> implements UploadFileAction {

	protected final Set<InputStream> ownedResources;
	private final PelicanApi pelicanAPI;
	private final Directory directory;
	private final Map<String, InputStream> files;

	public UploadFileActionImpl(ClientServer server, Directory directory, ClientImpl impl) {
		super(impl.getP4J(), Route.Files.UPLOAD_FILE.compile(server.getIdentifier()));
		this.pelicanAPI = impl.getP4J();
		this.directory = directory;
		this.files = new HashMap<>();
		this.ownedResources = new HashSet<>();
	}

	@Override
	public UploadFileAction addFile(InputStream data, String name) {
		Checks.notNull(data, "Data");
		Checks.notBlank(name, "Name");
		files.put(name, data);
		return this;
	}

	@Override
	public UploadFileAction addFile(File file, String name) {
		Checks.notNull(file, "File");
		Checks.check(file.exists() && file.canRead(), "Provided file either does not exist or cannot be read from!");
		try {
			FileInputStream data = new FileInputStream(file);
			ownedResources.add(data);
			return addFile(data, name);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public UploadFileAction clearFiles() {
		files.clear();
		clearResources();
		return this;
	}

	private RequestBody fileMultipart() {
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

		for (Map.Entry<String, InputStream> entry : files.entrySet()) {
			RequestBody body = new BufferedRequestBody(Okio.source(entry.getValue()), Requester.MEDIA_TYPE_OCTET);
			builder.addFormDataPart("files", entry.getKey(), body);
		}

		files.clear();
		ownedResources.clear();
		return builder.build();
	}

	@Override
	public void handleSuccess(Response response, Request<Void> request) {
		JSONObject json = response.getObject();
		String url = json.getJSONObject("attributes").getString("url");

		okhttp3.Request req = new okhttp3.Request.Builder()
				.url(String.format("%s&directory=%s", url, directory.getPath()))
				.addHeader("User-Agent", pelicanAPI.userAgent())
				.post(fileMultipart())
				.build();

		OkHttpClient requester = pelicanAPI.httpClient();
		requester.newCall(req).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				request.onFailure(new UncheckedIOException(e));
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
				if (response.isSuccessful()) {
					request.onSuccess(null);
					try {
						response.close();
					} catch (Exception ignored) {
					}
				} else {
					Response uploadResponse = new Response(response, -1);
					request.onFailure(new FileUploadException(response.code(), uploadResponse.getObject()));
					try {
						response.close();
					} catch (Exception ignored) {
					}
				}
			}
		});
	}

	private void clearResources() {
		for (InputStream ownedResource : ownedResources) {
			try {
				ownedResource.close();
			} catch (IOException ex) {
				if (!ex.getMessage().toLowerCase().contains("closed"))
					LOGGER.error("Encountered IOException trying to close owned resource", ex);
			}
		}
		ownedResources.clear();
	}
}
