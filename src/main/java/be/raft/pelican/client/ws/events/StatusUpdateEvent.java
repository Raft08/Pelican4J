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

package be.raft.pelican.client.ws.events;

import be.raft.pelican.UtilizationState;
import be.raft.pelican.client.entities.ClientServer;
import be.raft.pelican.client.entities.impl.ClientImpl;
import be.raft.pelican.client.managers.WebSocketManager;

public class StatusUpdateEvent extends Event {

	private final UtilizationState state;

	public StatusUpdateEvent(
            ClientImpl api, ClientServer server, WebSocketManager manager, UtilizationState state) {
		super(api, server, manager);
		this.state = state;
	}

	public UtilizationState getState() {
		return state;
	}
}
