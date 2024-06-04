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

package be.raft.pelican.application.entities;

import be.raft.pelican.RequestAction;
import be.raft.pelican.application.managers.UserAction;
import be.raft.pelican.entities.User;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface ApplicationUser extends User, IdentifiedEntity {

	String getFirstName();

	String getLastName();

	default String getFullName() {
		return String.format("%s %s", getFirstName(), getLastName());
	}

	String getExternalId();

	UUID getUUID();

	boolean has2FA();

	String getLanguage();

	default Locale getLocale() {
		return Locale.forLanguageTag(getLanguage());
	}

	boolean isRootAdmin();

	RequestAction<List<ApplicationServer>> retrieveServers();

	UserAction edit();

	RequestAction<Void> delete();

	@Override
	String toString();
}
