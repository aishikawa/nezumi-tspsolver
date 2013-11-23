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

package algorithm;

import instance.Instance;

import java.util.Random;

import algorithm.neighborhood.Edge;
import algorithm.neighborhood.Neighbor;
import algorithm.neighborhood.Neighborhood;

public class LocalSearch {
	
	private Instance instance;
	private Neighborhood[] neighborhoods;
	
	private long currentValue;
		
	private long startTime;
	private boolean verbose = false;
	
	public LocalSearch(Instance i, Neighborhood[] n) {
		instance = i;
		neighborhoods = n;
	}
	
	public void setVerbose(boolean v) {
		verbose = v;
	}
	
	public int[] getLocalOptSolution(Random rand) {
		startTime = System.currentTimeMillis();
		RandomTour rt = new RandomTour(instance);
		int[] initial = rt.getRandomTour(rand);
		
		return getLocalOptSolution(initial);
	}
	
	public int[] getLocalOptSolution(int[] initial) {
		int[] current = initial;
		currentValue = instance.getCost(current);
		int[] better = null;
		for (Neighborhood neighborhood : neighborhoods) {
			while (true) {
				better = getBetterSolution(current, neighborhood);
				if (better == null) {
					break;
				} else {
					current = better;
				}
			}
		}
		return current;
	}
	
	private int[] getBetterSolution(int[] current, Neighborhood neighborhood) {
		//System.out.println("getBetter");
		int[] ret = null;
		
		long neighborSize = neighborhood.getSize();
		for (long i=0; i<neighborSize; i++) {
			Neighbor neighbor = neighborhood.getNextNeighbof(current);
			long delta = 0;
			for (Edge e : neighbor.addedEdges) {
				delta += instance.getDistance(e.v1, e.v2);
			}
			for (Edge e : neighbor.removedEdges) {
				delta -= instance.getDistance(e.v1, e.v2);
			}
			
			if (delta < 0) {
				ret = neighbor.neighbor;
				currentValue += delta;
				if (verbose) {
					System.out.println("Incumbent value\t"+currentValue+"\t"+(double)(System.currentTimeMillis()-startTime)/1000+" sec.");
				}
				break;
			}
		}
		return ret;
	}
	
	public long getCurrentValue() {
		return currentValue;
	}
	
}
