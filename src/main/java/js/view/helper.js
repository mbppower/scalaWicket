/* 
 * Copyright 2015 Marcel.Barbosa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var Helper = {
	readContents : function(path) {
		with (new JavaImporter(java.io, java.nio.file)) {
			var path = Paths.get(path);
			var reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
				var buffer = "", line = null;
				while ((line = reader.readLine()) != null) {
					buffer += line;
				}
			}
			finally {
				if(reader)
					reader.close();
			}
			return buffer;
		}
		return null;
	}
}

