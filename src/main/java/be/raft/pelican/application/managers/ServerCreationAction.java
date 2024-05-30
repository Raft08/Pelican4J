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

package be.raft.pelican.application.managers;

import be.raft.pelican.DataType;
import be.raft.pelican.EnvironmentValue;
import be.raft.pelican.RequestAction;
import be.raft.pelican.application.entities.*;
import java.util.*;

public interface ServerCreationAction extends RequestAction<ApplicationServer> {

	ServerCreationAction setName(String name);

	ServerCreationAction setDescription(String description);

	ServerCreationAction setOwner(ApplicationUser owner);

	ServerCreationAction setEgg(ApplicationEgg egg);

	ServerCreationAction setDockerImage(String dockerImage);

	ServerCreationAction setStartupCommand(String command);

	ServerCreationAction setMemory(long amount, DataType dataType);

	ServerCreationAction setSwap(long amount, DataType dataType);

	ServerCreationAction setDisk(long amount, DataType dataType);

	ServerCreationAction setIO(long amount);

	ServerCreationAction setCPU(long amount);

	ServerCreationAction setThreads(String cores);

	ServerCreationAction setDatabases(long amount);

	ServerCreationAction setAllocations(long amount);

	ServerCreationAction setBackups(long amount);

	ServerCreationAction setEnvironment(Map<String, EnvironmentValue<?>> environment);

	ServerCreationAction setDedicatedIP(boolean dedicatedIP);

	ServerCreationAction setPortRange(Set<Integer> ports);

	default ServerCreationAction setPort(int port) {
		return setPortRange(Collections.singleton(port));
	}

	default ServerCreationAction setAllocations(
			ApplicationAllocation defaultAllocation, ApplicationAllocation... additionalAllocations) {
		return setAllocations(defaultAllocation, Arrays.asList(additionalAllocations));
	}

	default ServerCreationAction setAllocation(ApplicationAllocation defaultAllocation) {
		return setAllocations(defaultAllocation);
	}

	ServerCreationAction setAllocations(
			ApplicationAllocation defaultAllocation, Collection<ApplicationAllocation> additionalAllocations);

	ServerCreationAction startOnCompletion(boolean start);

	ServerCreationAction skipScripts(boolean skip);
}
