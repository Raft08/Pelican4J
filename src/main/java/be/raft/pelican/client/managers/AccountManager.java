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

package be.raft.pelican.client.managers;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.entities.impl.ClientImpl;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class AccountManager {

	private final ClientImpl impl;

	public AccountManager(ClientImpl impl) {
		this.impl = impl;
	}

	public RequestAction<String> get2FAImage() {
		return new RequestActionImpl<>(
				impl.getP4J(), Route.Accounts.GET_2FA_CODE.compile(), (response, request) -> response.getObject()
						.getJSONObject("data")
						.getString("image_url_data"));
	}

	public RequestAction<Set<String>> enable2FA(int code) {
		JSONObject obj = new JSONObject().put("code", code);
		return new RequestActionImpl<>(
				impl.getP4J(),
				Route.Accounts.ENABLE_2FA.compile(),
				RequestActionImpl.getRequestBody(obj),
				(response, request) -> Collections.unmodifiableSet(
						response.getObject().getJSONObject("attributes").getJSONArray("tokens").toList().stream()
								.map(Object::toString)
								.collect(Collectors.toSet())));
	}

	public RequestAction<Void> disable2FA(String password) {
		JSONObject obj = new JSONObject().put("password", password);
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Accounts.DISABLE_2FA.compile(), RequestActionImpl.getRequestBody(obj));
	}

	public RequestAction<Void> updateEmail(String newEmail, String password) {
		JSONObject obj = new JSONObject().put("email", newEmail).put("password", password);
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Accounts.UPDATE_EMAIL.compile(), RequestActionImpl.getRequestBody(obj));
	}

	public RequestAction<Void> updatePassword(String currentPassword, String newPassword) {
		JSONObject obj = new JSONObject()
				.put("current_password", currentPassword)
				.put("password", newPassword)
				.put("password_confirmation", newPassword);
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(), Route.Accounts.UPDATE_PASSWORD.compile(), RequestActionImpl.getRequestBody(obj));
	}
}
