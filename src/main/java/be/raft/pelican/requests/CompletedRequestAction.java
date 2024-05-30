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

package be.raft.pelican.requests;

import be.raft.pelican.RequestAction;
import be.raft.pelican.entities.P4J;
import be.raft.pelican.exceptions.RateLimitedException;
import java.util.function.Consumer;

public class CompletedRequestAction<T> implements RequestAction<T> {

	private final P4J api;
	protected final T value;
	protected final Throwable error;

	public CompletedRequestAction(P4J api, T value, Throwable error) {
		this.api = api;
		this.value = value;
		this.error = error;
	}

	public CompletedRequestAction(P4J api, T value) {
		this(api, value, null);
	}

	public CompletedRequestAction(P4J api, Throwable error) {
		this(api, null, error);
	}

	@Override
	public P4J getP4J() {
		return api;
	}

	@Override
	public T execute(boolean shouldQueue) throws RateLimitedException {
		if (error != null) {
			if (error instanceof RateLimitedException) throw (RateLimitedException) error;
			if (error instanceof RuntimeException) throw (RuntimeException) error;
			throw new IllegalStateException(error);
		}
		return value;
	}

	@Override
	public void executeAsync(Consumer<? super T> success, Consumer<? super Throwable> failure) {
		if (error == null) {
			if (success == null) RequestAction.getDefaultSuccess().accept(value);
			else success.accept(value);
		} else {
			if (failure == null) RequestAction.getDefaultFailure().accept(error);
			else failure.accept(error);
		}
	}

	@Override
	public RequestAction<T> deadline(long timestamp) {
		return this;
	}
}