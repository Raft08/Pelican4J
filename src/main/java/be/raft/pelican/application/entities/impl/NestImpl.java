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
import be.raft.pelican.application.entities.ApplicationEgg;
import be.raft.pelican.application.entities.ApplicationServer;
import be.raft.pelican.application.entities.Nest;
import be.raft.pelican.requests.CompletedPteroAction;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;

public class NestImpl implements Nest {

	private final JSONObject json;
	private final JSONObject relationships;
	private final PteroApplicationImpl impl;

	public NestImpl(JSONObject json, PteroApplicationImpl impl) {
		this.json = json.getJSONObject("attributes");
		this.relationships = json.getJSONObject("attributes").optJSONObject("relationships");
		this.impl = impl;
	}

	@Override
	public String getUUID() {
		return json.getString("uuid");
	}

	@Override
	public String getAuthor() {
		return json.getString("author");
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
	public PteroAction<List<ApplicationEgg>> retrieveEggs() {
		if (!json.has("relationships")) return impl.retrieveEggsByNest(this);

		List<ApplicationEgg> eggs = new ArrayList<>();
		JSONObject json = relationships.getJSONObject("eggs");

		for (Object o : json.getJSONArray("data")) {
			JSONObject egg = new JSONObject(o.toString());

			if (egg.isNull("attributes")) continue;

			eggs.add(new ApplicationEggImpl(egg, impl));
		}
		return new CompletedPteroAction<>(impl.getP4J(), Collections.unmodifiableList(eggs));
	}

	@Override
	public Optional<List<ApplicationServer>> getServers() {
		if (!json.has("relationships")) return Optional.empty();

		List<ApplicationServer> servers = new ArrayList<>();
		JSONObject json = relationships.getJSONObject("servers");

		for (Object o : json.getJSONArray("data")) {
			JSONObject server = new JSONObject(o.toString());

			if (server.isNull("attributes")) continue;

			servers.add(new ApplicationServerImpl(impl, server));
		}
		return Optional.of(Collections.unmodifiableList(servers));
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
}
