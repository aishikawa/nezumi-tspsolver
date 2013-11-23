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

import java.util.List;

public class GeoInstance extends CoordInstance {

	private static final double PI = 3.141592;
	private static final double RRR = 6378.388;
	
	public GeoInstance(String n, String t, String c, String e, int d, List<String> data) {
		super(n, t, c, e, d, data);
	}
	
	@Override
	public long getDistance(int from, int to) {

		Coord2d f = points.get(from);
		Coord2d t = points.get(to);
		
		double fromLat = convertToGeographicCoordinate(f.x);
		double fromLong = convertToGeographicCoordinate(f.y);
		double toLat = convertToGeographicCoordinate(t.x);
		double toLong = convertToGeographicCoordinate(t.y);
		
		double q1 = Math.cos(fromLong - toLong);
		double q2 = Math.cos(fromLat - toLat);
		double q3 = Math.cos(fromLat + toLat);
		
		return (long) (RRR * Math.acos(0.5*((1.0+q1)*q2 - (1.0-q1)*q3)) + 1.0);
	}
	
	
	private double convertToGeographicCoordinate(double x) {
		
		double deg = (int)x;
		double min = x - deg;
		
		return PI * (deg + 5.0 * min / 3.0) / 180.0;
	}

}
