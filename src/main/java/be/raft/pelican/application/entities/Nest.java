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

package be.raft.pelican.application.entities;

import be.raft.pelican.RequestAction;
import java.util.List;
import java.util.Optional;

public interface Nest extends ISnowflake {

	String getUUID();

	String getAuthor();

	String getName();

	String getDescription();

	RequestAction<List<ApplicationEgg>> retrieveEggs();

	Optional<List<ApplicationServer>> getServers();
}
