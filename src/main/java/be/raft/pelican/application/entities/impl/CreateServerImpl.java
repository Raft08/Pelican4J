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

package be.raft.pelican.application.entities.impl;

import be.raft.pelican.DataType;
import be.raft.pelican.EnvironmentValue;
import be.raft.pelican.application.entities.*;
import be.raft.pelican.application.managers.ServerCreationAction;
import be.raft.pelican.requests.RequestActionImpl;
import be.raft.pelican.requests.Route;
import be.raft.pelican.utils.Checks;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class CreateServerImpl extends RequestActionImpl<ApplicationServer> implements ServerCreationAction {

	private String name;
	private String description;
	private ApplicationUser owner;
	private ApplicationEgg egg;
	private String dockerImage;
	private String startupCommand;
	private long memory;
	private long swap;
	private long disk;
	private long cpu = 0L;
	private long io = 500L;
	private String threads;
	private long databases = 0L;
	private long allocations = 0L;
	private long backups = 0L;
	private Map<String, EnvironmentValue<?>> environment;
	private Set<Integer> portRange;
	private boolean useDedicatedIP;
	private boolean startOnCompletion;
	private boolean skipScripts;
	private ApplicationAllocation defaultAllocation;
	private Collection<ApplicationAllocation> additionalAllocations;

	private final ApplicationImpl impl;

	public CreateServerImpl(ApplicationImpl impl) {
		super(
				impl.getP4J(),
				Route.Servers.CREATE_SERVER.compile(),
				(response, request) -> new ApplicationServerImpl(impl, response.getObject()));
		this.environment = new HashMap<>();
		this.impl = impl;
	}

	@Override
	public ServerCreationAction setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public ServerCreationAction setDescription(String description) {
		this.description = description;
		return this;
	}

	@Override
	public ServerCreationAction setOwner(ApplicationUser owner) {
		this.owner = owner;
		return this;
	}

	@Override
	public ServerCreationAction setEgg(ApplicationEgg egg) {
		this.egg = egg;
		return this;
	}

	@Override
	public ServerCreationAction setDockerImage(String dockerImage) {
		this.dockerImage = dockerImage;
		return this;
	}

	@Override
	public ServerCreationAction setStartupCommand(String command) {
		this.startupCommand = command;
		return this;
	}

	@Override
	public ServerCreationAction setMemory(long amount, DataType dataType) {
		this.memory = convert(amount, dataType);
		return this;
	}

	@Override
	public ServerCreationAction setSwap(long amount, DataType dataType) {
		this.swap = convert(amount, dataType);
		return this;
	}

	@Override
	public ServerCreationAction setDisk(long amount, DataType dataType) {
		this.disk = convert(amount, dataType);
		return this;
	}

	@Override
	public ServerCreationAction setIO(long amount) {
		this.io = amount;
		return this;
	}

	@Override
	public ServerCreationAction setThreads(String cores) {
		this.threads = cores;
		return this;
	}

	@Override
	public ServerCreationAction setCPU(long amount) {
		this.cpu = amount;
		return this;
	}

	@Override
	public ServerCreationAction setDatabases(long amount) {
		this.databases = amount;
		return this;
	}

	@Override
	public ServerCreationAction setAllocations(long amount) {
		this.allocations = amount;
		return this;
	}

	@Override
	public ServerCreationAction setBackups(long amount) {
		this.backups = amount;
		return this;
	}

	@Override
	public ServerCreationAction setEnvironment(Map<String, EnvironmentValue<?>> environment) {
		this.environment = environment;
		return this;
	}

	@Override
	public ServerCreationAction setDedicatedIP(boolean dedicatedIP) {
		this.useDedicatedIP = dedicatedIP;
		return this;
	}

	@Override
	public ServerCreationAction setPortRange(Set<Integer> ports) {
		this.portRange = ports;
		return this;
	}

	@Override
	public ServerCreationAction startOnCompletion(boolean start) {
		this.startOnCompletion = start;
		return this;
	}

	@Override
	public ServerCreationAction skipScripts(boolean skip) {
		this.skipScripts = skip;
		return this;
	}

	@Override
	public ServerCreationAction setAllocations(
			ApplicationAllocation defaultAllocation, Collection<ApplicationAllocation> additionalAllocations) {
		this.defaultAllocation = defaultAllocation;
		this.additionalAllocations = additionalAllocations;
		return this;
	}

	@Override
	protected RequestBody finalizeData() {
		Checks.check(memory > 4, "The minimum memory limit is 4 MB.");
		Checks.notNull(owner, "Owner");
		Checks.notNull(egg, "Egg and Nest");

		if (defaultAllocation != null)
			Checks.check(
					portRange == null,
					"You need to set both a port range and Location, or set only an Allocation instead.");

		Map<String, Object> env = new HashMap<>();
		environment.forEach((k, v) -> env.put(k, v.get().orElse(null)));
		egg.getDefaultVariableMap()
				.orElseGet(() -> impl.retrieveEggById(egg.getId())
						.execute()
						.getDefaultVariableMap()
						.get())
				.forEach((k, v) -> env.putIfAbsent(k, v.get().orElse(null)));
		JSONObject featureLimits = new JSONObject()
				.put("databases", databases)
				.put(
						"allocations",
						allocations == 0 && additionalAllocations != null
								? additionalAllocations.size() + 1
								: allocations)
				.put("backups", backups);
		JSONObject limits = new JSONObject()
				.put("memory", memory)
				.put("swap", swap)
				.put("disk", disk)
				.put("io", io)
				.put("cpu", cpu)
				.put("threads", threads);
		JSONObject allocation = new JSONObject()
				.put("default", defaultAllocation != null ? defaultAllocation.getIdLong() : null)
				.put(
						"additional",
						(additionalAllocations != null && !additionalAllocations.isEmpty())
								? additionalAllocations.stream()
										.map(ApplicationAllocation::getIdLong)
										.collect(Collectors.toList())
								: null);
		JSONObject deploy = new JSONObject()
				.put("dedicated_ip", useDedicatedIP)
				.put(
						"port_range",
						portRange != null
								? portRange.stream()
										.map(Integer::toUnsignedString)
										.collect(Collectors.toList())
								: null);
		JSONObject obj = new JSONObject()
				.put("name", name)
				.put("description", description)
				.put("user", owner.getId())
				.put("egg", egg.getId())
				.put("docker_image", dockerImage != null ? dockerImage : egg.getDockerImage())
				.put("startup", startupCommand != null ? startupCommand : egg.getStartupCommand())
				.put("limits", limits)
				.put("feature_limits", featureLimits)
				.put("environment", env)
				.put("deploy", (portRange != null) ? deploy : null)
				.put("allocation", allocation)
				.put("start_on_completion", startOnCompletion)
				.put("skip_scripts", skipScripts);
		return getRequestBody(obj);
	}

	private long convert(long amount, DataType dataType) {
		if (dataType != DataType.MB) amount = amount * dataType.getMbValue();
		return amount;
	}
}
