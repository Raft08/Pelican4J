/*
 *    Copyright 2024 Matt Malec, and the Pterodactyl4J contributors
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

import be.raft.pelican.application.entities.DockerImage;

public class DockerImageImpl implements DockerImage {
	private final String name;
	private final String image;

	public DockerImageImpl(String name, String image) {
		this.name = name;
		this.image = image;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getImage() {
		return image;
	}
}
