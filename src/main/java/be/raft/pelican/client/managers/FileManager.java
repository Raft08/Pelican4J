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

package be.raft.pelican.client.managers;

import be.raft.pelican.PteroAction;
import be.raft.pelican.client.entities.Directory;
import be.raft.pelican.client.entities.DownloadableFile;
import be.raft.pelican.client.entities.File;

public interface FileManager {

	CreateDirectoryAction createDirectory();

	PteroAction<Void> createFile(Directory directory, String name, String content);

	UploadFileAction upload(Directory directory);

	RenameAction rename();

	CompressAction compress();

	PteroAction<Void> decompress(File compressedFile);

	DeleteAction delete();

	PteroAction<String> retrieveContent(File file);

	PteroAction<DownloadableFile> retrieveDownload(File file);

	PteroAction<Void> write(File file, String content);

	PteroAction<Void> copy(File file);
}
