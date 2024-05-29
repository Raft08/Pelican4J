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

package be.raft.pelican.client.entities;

import be.raft.pelican.PowerAction;
import be.raft.pelican.RequestAction;
import be.raft.pelican.client.managers.*;
import be.raft.pelican.entities.Server;
import be.raft.pelican.requests.PaginationAction;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ClientServer extends Server {

	boolean isServerOwner();

	long getInternalIdLong();

	default String getInternalId() {
		return Long.toUnsignedString(getInternalIdLong());
	}

	SFTP getSFTPDetails();

	String getInvocation();

	Set<String> getEggFeatures();

	ClientEgg getEgg();

	String getNode();

	boolean isSuspended();

	boolean isInstalling();

	boolean isTransferring();

	ClientServerManager getManager();

	RequestAction<Utilization> retrieveUtilization();

	RequestAction<Void> setPower(PowerAction powerAction);

	default RequestAction<Void> start() {
		return setPower(PowerAction.START);
	}

	default RequestAction<Void> stop() {
		return setPower(PowerAction.STOP);
	}

	default RequestAction<Void> restart() {
		return setPower(PowerAction.RESTART);
	}

	default RequestAction<Void> kill() {
		return setPower(PowerAction.KILL);
	}

	RequestAction<Void> sendCommand(String command);

	WebSocketBuilder getWebSocketBuilder();

	List<ClientSubuser> getSubusers();

	RequestAction<ClientSubuser> retrieveSubuser(UUID uuid);

	default RequestAction<ClientSubuser> retrieveSubuser(String uuid) {
		return retrieveSubuser(UUID.fromString(uuid));
	}

	SubuserManager getSubuserManager();

	PaginationAction<Backup> retrieveBackups();

	RequestAction<Backup> retrieveBackup(UUID uuid);

	default RequestAction<Backup> retrieveBackup(String uuid) {
		return retrieveBackup(UUID.fromString(uuid));
	}

	BackupManager getBackupManager();

	RequestAction<List<Schedule>> retrieveSchedules();

	default RequestAction<Schedule> retrieveSchedule(long id) {
		return retrieveSchedule(Long.toUnsignedString(id));
	}

	RequestAction<Schedule> retrieveSchedule(String id);

	ScheduleManager getScheduleManager();

	FileManager getFileManager();

	default RequestAction<Directory> retrieveDirectory() {
		return retrieveDirectory("/");
	}

	RequestAction<Directory> retrieveDirectory(Directory previousDirectory, Directory directory);

	RequestAction<Directory> retrieveDirectory(String path);

	RequestAction<List<ClientDatabase>> retrieveDatabases();

	default RequestAction<Optional<ClientDatabase>> retrieveDatabaseById(String id) {
		return retrieveDatabases()
				.map(List::stream)
				.map(stream -> stream.filter(db -> db.getId().equals(id)).findFirst());
	}

	default RequestAction<Optional<ClientDatabase>> retrieveDatabaseByName(String name, boolean caseSensitive) {
		return retrieveDatabases().map(List::stream).map(stream -> stream.filter(db -> caseSensitive
						? db.getName().contains(name)
						: db.getName().toLowerCase().contains(name.toLowerCase()))
				.findFirst());
	}

	ClientDatabaseManager getDatabaseManager();

	List<ClientAllocation> getAllocations();

	default ClientAllocation getPrimaryAllocation() {
		return getAllocations().stream()
				.filter(ClientAllocation::isDefault)
				.findFirst()
				.get();
	}

	default Optional<ClientAllocation> getAllocationByPort(int port) {
		return getAllocations().stream()
				.filter(allocation -> allocation.getPortInt() == port)
				.findFirst();
	}

	default Optional<ClientAllocation> getAllocationById(long id) {
		return getAllocations().stream()
				.filter(allocation -> allocation.getIdLong() == id)
				.findFirst();
	}

	ClientAllocationManager getAllocationManager();
}
