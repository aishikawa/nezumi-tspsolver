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

package instance;


abstract public class Instance {
	protected String name;
	protected String type;
	protected String comment;
	
	protected String edgeWeightType;
	
	protected int dimension;	
	
	protected Instance(String n, String t, String c, String e, int d) {
		name = n;
		type = t;
		comment = c;
		edgeWeightType = e;
		dimension = d;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String edgeWeightType() {
		return edgeWeightType;
	}
	
	public int getNumNode() {
		return dimension;
	}
	
	abstract public long getDistance(int from, int to);
	
	public long getCost(int[] tour) {
		long cost = 0;
		for (int i=0; i<dimension-1; i++) {
			cost += getDistance(tour[i], tour[i+1]);
		}
		cost += getDistance(tour[dimension-1], tour[0]);
		
		return cost;
	}

	public void printInformation() {
		System.out.println("Instance");
		System.out.println(" Name: "+name);
		System.out.println(" "+comment);
		System.out.println(" Dimension: "+dimension);
	}
}
