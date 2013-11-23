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

package main;

import instance.Instance;
import instance.InstanceCreator;
import instance.InstanceException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import tour.Tour;
import tour.TourException;
import algorithm.LocalSearch;
import algorithm.SimulatedAnnealing;
import algorithm.neighborhood.Neighborhood;
import algorithm.neighborhood.TwoOptNeighborhood;

public class Main {
	
	private static final String usage = "java -jar tsp.jar -instance filename [options]";
	
	public static void main(String[] args) {
		Options options = new Options();
		
		Option help = new Option("help", "print this message");
		options.addOption(help);
		
		Option instanceOpt = OptionBuilder.withArgName("filename")
				.hasArg()
				.withDescription("instance file")
				.create("instance");
		options.addOption(instanceOpt);
		
		Option algorithmOpt = OptionBuilder.withArgName("algorithm")
				.hasArg()
				.withDescription("algorithm")
				.create("algorithm");
		options.addOption(algorithmOpt);
		
		Option randomOpt = OptionBuilder.withArgName("seed")
				.hasArg()
				.withDescription("seed of random number generator")
				.create("seed");
		options.addOption(randomOpt);
		
		Option verboseOpt = OptionBuilder.withDescription("output some information")
				.create("verbose");
		options.addOption(verboseOpt);
		
		Option tourOpt = OptionBuilder.withArgName("filename")
				.hasArg()
				.withDescription("coumpute cost of the tour")
				.create("tour");
		options.addOption(tourOpt);
		
		CommandLineParser parser = new BasicParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			System.exit(1);
		}
		
		if (line.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(usage, options);
			System.exit(0);
		}
		
		String filename = null;
		Instance instance = null;
		InstanceCreator creator = new InstanceCreator();
		if (line.hasOption("instance")) {
			try {
				filename = line.getOptionValue("instance");
				instance = creator.createInstance(filename);
			} catch (FileNotFoundException e) {
				System.err.println("error: instance file " + filename + " was not found");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("IOException");
				System.exit(1);
			} catch (InstanceException e) {
				System.err.println("error: " + e.getMessage());
				System.exit(1);
			}
		} else {
			System.err.println("usage: "+usage);
			System.exit(1);
		}
		
		instance.printInformation();
		
		Random random = new Random();
		if (line.hasOption("seed")) {
			long seed = 0;
			try {
				seed = Long.parseLong(line.getOptionValue("seed"));
			} catch (NumberFormatException e) {
				System.err.println("NumberFormatException");
				System.exit(1);
			}
			random.setSeed(seed);
		}
		
		if (line.hasOption("tour")) {
			Tour tour = null;
			try {
				filename = line.getOptionValue("tour");
				tour = Tour.makeTour(filename);
			} catch (FileNotFoundException e) {
				System.err.println("error: tour file " + filename + " was not found");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("IOException");
				System.exit(1);
			} catch (TourException e) {
				System.err.println("error: "+e.getMessage());
				System.exit(1);
			}
			if (tour.getDimension() != instance.getNumNode()) {
				System.err.println("instance dimension and tour dimension is different");;
				System.exit(1);
			}
			
			long length = instance.getCost(tour.getTour());
			System.out.println("tour length: "+length);
			
		} else if (line.hasOption("algorithm")) {			
			String algorithm = line.getOptionValue("algorithm");
			
			if (algorithm.equals("ls")) {
				System.out.println("start Local Search");
				Neighborhood[] neighborhoods = new Neighborhood[1];
				neighborhoods[0] = new TwoOptNeighborhood(instance.getNumNode());
				
				LocalSearch ls = new LocalSearch(instance, neighborhoods);
				if (line.hasOption("verbose")) {
					ls.setVerbose(true);
				}
				int[] tour = ls.getLocalOptSolution(random);
				long cost = instance.getCost(tour);
				if (cost != ls.getCurrentValue()) {
					System.err.println("There are some bugs in LocalSearch");
					System.exit(1);
				}
				System.out.println("solution");
				System.out.println(Arrays.toString(tour));
				System.out.println("Cost: "+cost);
			} else if (algorithm.equals("sa")) {
				System.out.println("start Simulated Annealing");
				Neighborhood[] neighborhoods = new Neighborhood[1];
				neighborhoods[0] = new TwoOptNeighborhood(instance.getNumNode());
				
				SimulatedAnnealing sa = new SimulatedAnnealing(instance, neighborhoods, random);
				if (line.hasOption("verbose")) {
					sa.setVerbose(true);
				}
				int[] tour = sa.simulatedAnnealing();
				long cost = instance.getCost(tour);
				System.out.println("solution");
				System.out.println(Arrays.toString(tour));
				System.out.println("Cost: "+cost);
			} else {
				System.err.println("Algorithm option error \""+algorithm+"\" ");
			}			
		}
		
	}
}
