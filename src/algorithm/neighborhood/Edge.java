/*
 * Copyright 2013 A.Ishikawa
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package algorithm.neighborhood;

public class Edge {
	public int v1;
	public int v2;
	
	public Edge(int a, int b) {
		v1 = a;
		v2 = b;
	}

	@Override
	public int hashCode() {
		return v1 << 16 + v2;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Edge && v1 == ((Edge)o).v1 && v2 == ((Edge)o).v2;
	}
}
