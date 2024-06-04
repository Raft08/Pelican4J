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

package be.raft.pelican.requests;

import be.raft.pelican.RequestAction;
import be.raft.pelican.entities.PelicanApi;
import be.raft.pelican.exceptions.RateLimitedException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeferredRequestAction<T> implements RequestAction<T> {

	private final PelicanApi api;
	private final Supplier<? extends T> value;

	public DeferredRequestAction(PelicanApi api, Supplier<? extends T> value) {
		this.api = api;
		this.value = value;
	}

	@Override
	public PelicanApi getP4J() {
		return api;
	}

	@Override
	public T execute(boolean shouldQueue) throws RateLimitedException {
		return value.get();
	}

	@Override
	public void executeAsync(Consumer<? super T> success, Consumer<? super Throwable> failure) {
		CompletableFuture.supplyAsync(value, api.supplierPool())
				.thenAcceptAsync(success == null ? RequestAction.getDefaultSuccess() : success);
	}

	@Override
	public RequestAction<T> deadline(long timestamp) {
		return this;
	}
}
