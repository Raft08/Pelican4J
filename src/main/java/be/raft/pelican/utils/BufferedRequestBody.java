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

package be.raft.pelican.utils;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import org.jetbrains.annotations.NotNull;

// thank you jda
public final class BufferedRequestBody extends RequestBody {
	private final Source source;
	private final MediaType type;
	private byte[] data;

	public BufferedRequestBody(Source source, MediaType type) {
		this.source = source;
		this.type = type;
	}

	@Override
	public MediaType contentType() {
		return type;
	}

	@Override
	public void writeTo(@NotNull BufferedSink sink) throws IOException {
		if (data != null) {
			sink.write(data);
			return;
		}

		try (BufferedSource s = Okio.buffer(source)) {
			data = s.readByteArray();
			sink.write(data);
		}
	}
}
