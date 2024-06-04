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
import be.raft.pelican.client.entities.Backup;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.managers.BackupAction;
import be.raft.pelican.client.managers.BackupManager;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;

public class BackupManagerImpl implements BackupManager {

	private final ClientServer server;
	private final ClientImpl impl;

	public BackupManagerImpl(ClientServer server, ClientImpl impl) {
		this.server = server;
		this.impl = impl;
	}

	@Override
	public BackupAction createBackup() {
		return new CreateBackupImpl(server, impl);
	}

	@Override
	public RequestAction<String> retrieveDownloadUrl(Backup backup) {
		return new RequestActionImpl<>(
				impl.getP4J(),
				Route.Backups.DOWNLOAD_BACKUP.compile(
						server.getIdentifier(), backup.getUUID().toString()),
				(response, request) ->
						response.getObject().getJSONObject("attributes").getString("url"));
	}

	@Override
	public RequestAction<Void> restoreBackup(Backup backup) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Backups.RESTORE_BACKUP.compile(
						server.getIdentifier(), backup.getUUID().toString()));
	}

	@Override
	public RequestAction<Backup> toggleLock(Backup backup) {
		return new RequestActionImpl<>(
				impl.getP4J(),
				Route.Backups.LOCK_BACKUP.compile(
						server.getIdentifier(), backup.getUUID().toString()),
				(response, request) -> new BackupImpl(response.getObject(), server));
	}

	@Override
	public RequestAction<Void> deleteBackup(Backup backup) {
		return RequestActionImpl.onRequestExecute(
				impl.getP4J(),
				Route.Backups.DELETE_BACKUP.compile(
						server.getIdentifier(), backup.getUUID().toString()));
	}
}
