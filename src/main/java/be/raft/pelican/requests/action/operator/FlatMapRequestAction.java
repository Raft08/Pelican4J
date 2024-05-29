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

package be.raft.pelican.requests.action.operator;

import be.raft.pelican.RequestAction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// big thanks to JDA for this tremendous code

public class FlatMapRequestAction<I, O> extends RequestActionOperator<I, O> {

	private final Function<? super I, ? extends RequestAction<O>> function;
	private final Predicate<? super I> condition;

	public FlatMapRequestAction(
			RequestAction<I> action,
			Predicate<? super I> condition,
			Function<? super I, ? extends RequestAction<O>> function) {
		super(action);
		this.function = function;
		this.condition = condition;
	}

	@Override
	public void executeAsync(Consumer<? super O> success, Consumer<? super Throwable> failure) {
		action.executeAsync(
				(result) -> {
					if (condition != null && !condition.test(result)) return;
					RequestAction<O> then = function.apply(result);
					if (then == null) doFailure(failure, new IllegalStateException("FlatMap operand is null"));
					else then.executeAsync(success, failure);
				},
				failure);
	}

	@Override
	public O execute(boolean shouldQueue) {
		return function.apply(action.execute(shouldQueue)).execute(shouldQueue);
	}
}
