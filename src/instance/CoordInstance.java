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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Util;

abstract public class CoordInstance extends Instance {
	
	protected Map<Integer, Coord2d> points;

	protected CoordInstance(String n, String t, String c, String e, int d, List<String> data) {
		super(n, t, c, e, d);
		
		points = new HashMap<Integer, Coord2d>();
				
		int nodeCoordSectionStartLine = -1;
		int lineNum = 0;
		for (String line : data) {
			if (line.startsWith("NODE_COORD_SECTION")) {
				nodeCoordSectionStartLine = lineNum;
				break;
			}
			lineNum += 1;
		}
		
		List<String> nodeCoordSection = data.subList(nodeCoordSectionStartLine + 1, nodeCoordSectionStartLine + 1 + d);
		
		for (String line : nodeCoordSection) {
			line = Util.removeSpaces(line);				
			String[] a = line.split("\\s+");
				
			int index = Integer.parseInt(a[0]);
			double x = Double.parseDouble(a[1]);
			double y = Double.parseDouble(a[2]);
			Coord2d coord = new Coord2d(x, y);				
			points.put(index, coord);
		}
	}
}
