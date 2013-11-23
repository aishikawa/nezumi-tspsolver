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

public class Euc2dInstance extends CoordInstance {

	public Euc2dInstance(String n, String t, String c, String e, int d, List<String> data) {
		super(n, t, c, e, d, data);
	}
	
	@Override
	public long getDistance(int from, int to) {
		Coord2d p1 = points.get(from);
		Coord2d p2 = points.get(to);
		double xd = p1.x - p2.x;
		double yd = p1.y - p2.y;
		long d = (long)(Math.sqrt(xd*xd + yd*yd) + 0.5);
		return d;
	}

}
