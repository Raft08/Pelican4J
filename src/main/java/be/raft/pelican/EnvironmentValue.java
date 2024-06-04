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

package be.raft.pelican;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Represents a value used in communication with an {@link be.raft.pelican.application.entities.ApplicationServer ApplicationServer}.
 */
public class EnvironmentValue<T> {

	private final T value;

	private EnvironmentValue(T value) {
		this.value = value;
	}

	/**
	 * Loads any type into an EnvironmentValue
	 *
	 * @param value The value to load
	 * @return An EnvironmentValue instance for the provided object
	 */
	public static <T> EnvironmentValue<T> of(T value) {
		return new EnvironmentValue<>(value);
	}

	/**
	 * Returns a Collector used to convert the EnvironmentValue from the environment variables map to a String
	 *
	 * <p>This is helpful when we need to pass an environment variable map of String values instead of
	 * EnvironmentValues to the Pterodactyl API
	 *
	 * @return The collector
	 */
	public static Collector<Map.Entry<String, EnvironmentValue<?>>, ?, Map<String, String>> collector() {
		return Collectors.toMap(
				Map.Entry::getKey, e -> e.getValue().getAsString().orElse("null"));
	}

	/**
	 * Resolves any type
	 *
	 * @return {@link java.util.Optional} with a possible value
	 */
	public Optional<T> get() {
		return Optional.of(value);
	}

	/**
	 * Resolves a {@link java.lang.String}
	 *
	 * @return {@link java.util.Optional} with a possible String value
	 */
	public Optional<String> getAsString() {
		if (value instanceof String) return Optional.of((String) value);
		return get().map(String::valueOf);
	}

	/**
	 * Resolves an {@link java.lang.Integer}
	 *
	 * <p>Note that the method will not return a present optional if the number includes a leading sign, either positive or negative.
	 * If the String passes the numeric test, it may still generate a NumberFormatException
	 * when parsed by Integer.parseInt, e.g. if the value is outside the range
	 * for int respectively.
	 *
	 * @return {@link java.util.Optional} with a possible Integer value
	 */
	public Optional<Integer> getAsInteger() {
		if (value instanceof Integer) return Optional.of((Integer) value);
		return getAsString()
				.flatMap(value -> value.chars().allMatch(Character::isDigit)
						? Optional.of(Integer.parseInt(value))
						: Optional.empty());
	}

	@Override
	public String toString() {
		return get().toString();
	}
}
