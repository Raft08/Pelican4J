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

package be.raft.pelican.application.entities.impl;

import be.raft.pelican.PteroAction;
import be.raft.pelican.application.entities.Node;
import be.raft.pelican.application.managers.NodeAction;
import be.raft.pelican.application.managers.NodeManager;
import be.raft.pelican.requests.PteroActionImpl;
import be.raft.pelican.requests.Route;

public class NodeManagerImpl implements NodeManager {

	private final PteroApplicationImpl impl;

	public NodeManagerImpl(PteroApplicationImpl impl) {
		this.impl = impl;
	}

	@Override
	public NodeAction createNode() {
		return new CreateNodeImpl(impl);
	}

	@Override
	public NodeAction editNode(Node node) {
		return new EditNodeImpl(impl, node);
	}

	@Override
	public PteroAction<Void> deleteNode(Node node) {
		return PteroActionImpl.onRequestExecute(impl.getP4J(), Route.Nodes.DELETE_NODE.compile(node.getId()));
	}
}
