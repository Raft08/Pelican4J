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

package be.raft.pelican.utils.config;

import be.raft.pelican.utils.Checks;

public final class EndpointConfig {

	private final String url;
	private final String token;

	public EndpointConfig(String url, String token) {
		Checks.notBlank(token, "API Key");
		Checks.notBlank(url, "Application URL");
		this.url = url;
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public String getToken() {
		return token;
	}
}