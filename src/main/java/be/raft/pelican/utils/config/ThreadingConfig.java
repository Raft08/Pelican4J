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

package be.raft.pelican.utils.config;

import be.raft.pelican.utils.NamedThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;

public final class ThreadingConfig {

	private ExecutorService callbackPool;
	private ExecutorService actionPool;
	private ExecutorService supplierPool;
	private ScheduledExecutorService rateLimitPool;

	public ExecutorService getCallbackPool() {
		return callbackPool;
	}

	public void setCallbackPool(ExecutorService callbackPool) {
		if (callbackPool == null) callbackPool = ForkJoinPool.commonPool();
		this.callbackPool = callbackPool;
	}

	public ExecutorService getActionPool() {
		return actionPool;
	}

	public void setActionPool(ExecutorService actionPool) {
		if (actionPool == null) actionPool = Executors.newSingleThreadExecutor(new NamedThreadFactory("Action"));
		this.actionPool = actionPool;
	}

	public ExecutorService getSupplierPool() {
		return supplierPool;
	}

	public void setSupplierPool(ExecutorService supplierPool) {
		if (supplierPool == null) supplierPool = Executors.newFixedThreadPool(3, new NamedThreadFactory("Supplier"));
		this.supplierPool = supplierPool;
	}

	public ScheduledExecutorService getRateLimitPool() {
		return rateLimitPool;
	}

	public void setRateLimitPool(ScheduledExecutorService rateLimitPool) {
		if (rateLimitPool == null)
			rateLimitPool = Executors.newScheduledThreadPool(5, new NamedThreadFactory("RateLimit"));
		this.rateLimitPool = rateLimitPool;
	}
}
