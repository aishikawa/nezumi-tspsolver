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

package tour;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import util.Util;

public class Tour {
	private String name;
	private String comment;
	private String type;
	
	private int dimension;
	
	private int[] tour;
	
	public String getName() {
		return name;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String getType() {
		return type;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public int[] getTour() {
		return Arrays.copyOf(tour, dimension);
	}
	
	public static Tour makeTour(String filename) throws FileNotFoundException, IOException, TourException {
		Tour tour = new Tour();
		List<Integer> cityList = new LinkedList<Integer>(); 
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			boolean inTourSection = false;
			int index = 0;
			String line = br.readLine();
			while (line != null) {
				String[] a = line.split(":", 2);
				if (a.length == 2) {
					for (int i=0; i<2; i++) {
						a[i] = Util.removeSpaces(a[i]);
					}
				}

				if (inTourSection) {
					String str = Util.removeSpaces(line);
					String[] array = str.split("\\s+");
					for (String cityStr : array) {
						int city = Integer.parseInt(cityStr);
						if (city != -1) {
							if (tour.tour != null) {
								tour.tour[index] = city;
							} else {
								cityList.add(city);
							}
							index += 1;
						} else {
							inTourSection = false;
						}
					}
				} else {
					if (a[0].equals("NAME")) {
						tour.name = a[1];
					} else if (a[0].equals("COMMENT")) {
						tour.comment = a[1];
					} else if (a[0].equals("TYPE")) {
						tour.type = a[1];
						if (a[1].equals("TOUR") == false) {
							throw new TourException("");
						}
					} else if (a[0].equals("DIMENSION")) {
						tour.dimension = Integer.parseInt(a[1]);
						tour.tour = new int[tour.dimension];
					} else if (a[0].equals("TOUR_SECTION")){
						inTourSection = true;
					}
				}
				line = br.readLine();
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		
		if (tour.tour == null) {
			tour.tour = new int[cityList.size()];
			tour.dimension = cityList.size();
			int index = 0;
			for (int c : cityList) {
				tour.tour[index] = c;
				index += 1;
			}
		}
		
		return tour;
	}
}
