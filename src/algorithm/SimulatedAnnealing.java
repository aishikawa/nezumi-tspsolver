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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import algorithm.neighborhood.Edge;
import algorithm.neighborhood.Neighbor;
import algorithm.neighborhood.Neighborhood;

public class SimulatedAnnealing {
	
	private double TEMPERATURE_START = 100;
	private double TEMPERATURE_END = 1;
	private double COOLING_FACTOR = 0.9;
	private double ALPHA = 1.0;
	
	private Instance instance;
	private Neighborhood[] neighborhoods;
	
	private int[] incumbentTour;
	private long  incumbentCost;
	
	private int[] currentTour;
	private long currentCost;
	
	private boolean verbose = false;
	
	private Random rand;
	
	private long sTime;
	
	public SimulatedAnnealing(Instance i, Neighborhood[] n, Random r) {
		instance = i;
		neighborhoods = n;
		rand = r;
		
		Properties property = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(new File("simulated_annealing.properties"));
			property.load(is);
			TEMPERATURE_START = Double.parseDouble(property.getProperty("TEMPERATURE_START"));
			TEMPERATURE_END = Double.parseDouble(property.getProperty("TEMPERATURE_END"));
			COOLING_FACTOR = Double.parseDouble(property.getProperty("COOLING_FACTOR"));
			ALPHA = Double.parseDouble(property.getProperty("ALPHA"));
		} catch (FileNotFoundException e) {
			System.err.println("\"simulated_annealing.properties\" was not fond");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("error: failed to read simulated annealing property");			
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				System.err.println("error: IOException");
			}
		}

	}
	
	public void setVerbose(boolean v) {
		verbose = v;
	}
	
	public int[] simulatedAnnealing() {
		sTime = System.currentTimeMillis();
		
		RandomTour rt = new RandomTour(instance);
		currentTour = rt.getRandomTour(rand);
		currentCost = instance.getCost(currentTour);
		
		incumbentTour = Arrays.copyOf(currentTour, currentTour.length);
		incumbentCost = currentCost;
		
		double temperture = TEMPERATURE_START;
		while (temperture > TEMPERATURE_END) {
			if (verbose) {
				System.out.println("temperature: "+temperture+"\ttime:"+(System.currentTimeMillis() - sTime)/1000.0);
			}
			for (Neighborhood neighborhood : neighborhoods) {
				search(neighborhood, temperture);
			}
			temperture *= COOLING_FACTOR;
		}
		
		return incumbentTour;
	}
	
	private void search(Neighborhood neighborhood, double temperature) {
		long numEvaluate = 0;
		long size = neighborhood.getSize();
		
		while (numEvaluate < size * ALPHA) {
			numEvaluate += 1;
			Neighbor neighbor = neighborhood.getNextNeighbof(currentTour);
			int[] neighborTour = neighbor.neighbor;
			
			long delta = 0;
			for (Edge e : neighbor.addedEdges) {
				delta += instance.getDistance(e.v1, e.v2);
			}
			for (Edge e : neighbor.removedEdges) {
				delta -= instance.getDistance(e.v1, e.v2);
			}
			long neighborCost = currentCost + delta;
			
			//update incumbent solution
			if (incumbentCost > neighborCost) {
				incumbentCost = neighborCost;
				incumbentTour = Arrays.copyOf(neighborTour, neighborTour.length);
				if (verbose) {
					System.out.println("Incumbent Value "+incumbentCost+"\ttime:"+(System.currentTimeMillis() - sTime)/1000.0);
				}
			}
			boolean move = false;
			if (delta < 0) {
				move = true;
			} else {
				double probability = Math.exp(-delta/temperature);
				if (rand.nextDouble() < probability) {
					move = true;
				}
			}
			if (move) {
				currentTour = neighborTour;
				currentCost = neighborCost;
			}
		}
	}
}
