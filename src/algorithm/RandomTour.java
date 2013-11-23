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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTour {
	public Instance instance;
	
	public RandomTour(Instance i) {
		instance = i;
	}
	
	public int[] getRandomTour(Random rand) {
		int numNode = instance.getNumNode();
		
		List<Integer> temp = new ArrayList<Integer>(numNode);		
		for (int i=0; i<numNode; i++) {
			temp.add(i+1);
		}
		
		Collections.shuffle(temp, rand);
		
		int[] tour = new int[numNode];
		for (int i=0; i<numNode; i++) {
			tour[i] = temp.get(i);					
		}
		
		return tour;
	}
}
