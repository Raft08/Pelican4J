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

package be.raft.pelican.requests.action.operator;

import be.raft.pelican.RequestAction;
import be.raft.pelican.exceptions.PteroException;
import be.raft.pelican.utils.ExceptionUtils;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// big thanks to JDA for this tremendous code

public class FlatMapErrorRequestAction<T> extends RequestActionOperator<T, T> {

	private final Predicate<? super Throwable> check;
	private final Function<? super Throwable, ? extends RequestAction<? extends T>> map;

	public FlatMapErrorRequestAction(
			RequestAction<T> action,
			Predicate<? super Throwable> check,
			Function<? super Throwable, ? extends RequestAction<? extends T>> map) {
		super(action);
		this.check = check;
		this.map = map;
	}

	@Override
	public void executeAsync(Consumer<? super T> success, Consumer<? super Throwable> failure) {
		action.executeAsync(success, (error) -> {
			try {
				if (check.test(error)) {
					RequestAction<? extends T> then = map.apply(error);
					if (then == null)
						doFailure(failure, new IllegalStateException("FlatMapError operand is null", error));
					else then.executeAsync(success, failure);
				} else doFailure(failure, error);
			} catch (Throwable e) {
				doFailure(failure, ExceptionUtils.appendCause(e, error));
			}
		});
	}

	@Override
	public T execute(boolean shouldQueue) {
		try {
			return action.execute(shouldQueue);
		} catch (Throwable error) {
			try {
				if (check.test(error)) {
					RequestAction<? extends T> then = map.apply(error);
					if (then == null) throw new IllegalStateException("FlatMapError operand is null", error);
					return then.execute(shouldQueue);
				}
			} catch (Throwable e) {
				if (e instanceof IllegalStateException && e.getCause() == error) throw (IllegalStateException) e;
				else fail(ExceptionUtils.appendCause(e, error));
			}
			fail(error);
		}
		throw new AssertionError("Unreachable");
	}

	private void fail(Throwable error) {
		if (error instanceof PteroException) throw (PteroException) error;
		else throw new RuntimeException(error);
	}
}
