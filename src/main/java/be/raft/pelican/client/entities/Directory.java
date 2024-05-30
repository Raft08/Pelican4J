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

package be.raft.pelican.client.entities;

import be.raft.pelican.RequestAction;
import be.raft.pelican.client.managers.CompressAction;
import be.raft.pelican.client.managers.DeleteAction;
import be.raft.pelican.client.managers.RenameAction;
import be.raft.pelican.client.managers.UploadFileAction;
import java.util.List;
import java.util.Optional;

public interface Directory extends GenericFile {

	List<GenericFile> getFiles();

	default Optional<GenericFile> getGenericFileByName(String name, boolean caseSensitive) {
		return getFiles().stream()
				.filter(f ->
						caseSensitive ? f.getName().equals(name) : f.getName().equalsIgnoreCase(name))
				.findFirst();
	}

	default Optional<File> getFileByName(String name, boolean caseSensitive) {
		return getGenericFileByName(name, caseSensitive).map(f -> (File) f);
	}

	default Optional<File> getFileByName(String name) {
		return getFileByName(name, false);
	}

	default Optional<Directory> getDirectoryByName(String name, boolean caseSensitive) {
		return getGenericFileByName(name, caseSensitive).map(d -> (Directory) d);
	}

	default Optional<Directory> getDirectoryByName(String name) {
		return getDirectoryByName(name, false);
	}

	RequestAction<Void> createFolder(String folder);

	RequestAction<Void> createFile(String name, String content);

	RequestAction<Directory> into(Directory directory);

	RequestAction<Directory> back();

	UploadFileAction upload();

	DeleteAction deleteFiles();

	RenameAction rename();

	CompressAction compress();

	RequestAction<Void> decompress(File compressedFile);
}
