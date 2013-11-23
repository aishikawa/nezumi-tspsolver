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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import util.Util;

public class InstanceCreator {
	
	public Instance createInstance() throws IOException, InstanceException {
		Instance instance = null;
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			instance = createInstance(br);
		} finally {
			br.close();
		}				
		
		return instance;
	}
	
	public Instance createInstance(String filename) throws FileNotFoundException, IOException, InstanceException {		
		Instance instance = null;
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filename));
			instance = createInstance(br);
		} finally {
			if (br != null) {
				br.close();
			}
		}
		
		return instance;
	}
	
	private Instance createInstance(BufferedReader br) throws IOException, InstanceException {
		List<String> data = new LinkedList<String>();
		
		String line = br.readLine();
		while (line != null) {
			data.add(line);
			line = br.readLine();
		}
		
		return createInstance(data);
	}
	
	private Instance createInstance(List<String> data) throws InstanceException {
		Instance instance = null;
		
		String name = null;;
		String comment = null;
		String type = null;
		int dimension = 0;
		String edgeWeightType = null;
		String edgeWeightFormat = null;
		
		for (String line : data) {
			String[] a = line.split(":", 2);
			if (a.length == 2) {
				for (int i=0; i<2; i++) {
					a[i] = Util.removeSpaces(a[i]);
				}
			}
			
			if (a[0].equals("NAME")) {
				name = a[1];
			} else if (a[0].equals("COMMENT")) {
				comment = a[1];
			} else if (a[0].equals("TYPE")) {
				type = a[1];
				if (type.startsWith("TSP") == false) {
					throw new InstanceException("This solver cannot solve "+type+" problem");
				}
			} else if (a[0].equals("DIMENSION")) {
				dimension = Integer.parseInt(a[1]);
			} else if (a[0].equals("EDGE_WEIGHT_TYPE")) {
				edgeWeightType = a[1];
			} else if (a[0].equals("EDGE_WEIGHT_FORMAT")) {
				edgeWeightFormat = a[1];
			}
		}
		
		if (edgeWeightType.equals("EUC_2D")) {
			instance = new Euc2dInstance(name, type, comment, edgeWeightType, dimension, data);
		} else if (edgeWeightType.equals("CEIL_2D")) {
			instance = new Ceil2dInstance(name, type, comment, edgeWeightType, dimension, data);
		} else if (edgeWeightType.equals("ATT")) {
			instance = new AttInstance(name, type, comment, edgeWeightType, dimension, data);
		} else if (edgeWeightType.equals("GEO")) {
			instance = new GeoInstance(name, type, comment, edgeWeightType, dimension, data);
		} else if (edgeWeightType.equals("EXPLICIT")) {
			instance = new ExplicitInstance(name, type, comment, edgeWeightType, dimension, data, edgeWeightFormat);
		} else {
			throw new InstanceException("This solver does not support EDGE_WEIGHT_TYPE "+edgeWeightType);
		}
		
		return instance;
	}
	
}
