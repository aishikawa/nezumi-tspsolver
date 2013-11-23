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

import java.util.Arrays;

public class TwoOptNeighborhood implements Neighborhood {

	private int numCity;
	private int firstIndex;
	private int secondIndex;
	
	public TwoOptNeighborhood(int n) {
		numCity = n;
		firstIndex = 0;
		secondIndex = 2;
	}

	private void reverse(int[] path) {
		int length = path.length;
		for (int i=0; i<length/2; i++) {
			int temp = path[i];
			path[i] = path[length - i - 1];
			path[length - i - 1] = temp;
		}
	}
	
	@Override
	public long getSize() {
		return (long)numCity * (numCity - 1) / 2 - numCity;		
	}

	@Override
	public Neighbor getNextNeighbof(int[] current) {
		int[] neighbor = Arrays.copyOf(current, current.length);
		
		int[] path = Arrays.copyOfRange(current, firstIndex, secondIndex);
		reverse(path);
		for (int i=0; i<path.length; i++) {
			neighbor[firstIndex + i] = path[i];
		}
		
		Neighbor ret = new Neighbor();
		ret.current = current;
		ret.neighbor = neighbor;
		if (firstIndex != 0) {
			ret.removedEdges.add(new Edge(current[firstIndex - 1], current[firstIndex]));
			ret.addedEdges.add(new Edge(current[firstIndex - 1], current[secondIndex - 1]));
		} else {
			ret.removedEdges.add(new Edge(current[numCity-1], current[firstIndex]));
			ret.addedEdges.add(new Edge(current[numCity-1], current[secondIndex-1]));
		}
		ret.removedEdges.add(new Edge(current[secondIndex-1], current[secondIndex]));
		ret.addedEdges.add(new Edge(current[firstIndex], current[secondIndex]));
		
		nextIndex();
		
		return ret;
	}
	
	private void nextIndex() {
		secondIndex += 1;
		if (secondIndex >= numCity) {
			firstIndex += 1;
			if (firstIndex >= numCity - 2) {
				firstIndex = 0;
			}
			secondIndex = firstIndex + 2;
		}
		
		
		if (firstIndex == 0 && secondIndex == numCity - 1) {
			nextIndex();
		}

	}

}
