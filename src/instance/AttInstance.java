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

public class AttInstance extends CoordInstance {

	protected AttInstance(String n, String t, String c, String e, int d, List<String> data) {
		super(n, t, c, e, d, data);
	}

	@Override
	public long getDistance(int from, int to) {
		double xd = points.get(from).x - points.get(to).x;
		double yd = points.get(from).y - points.get(to).y;
		double r = Math.sqrt( (xd*xd + yd*yd) / 10.0);
		long t = (long)(r+0.5);
		long d = t;
		if (t < r) {
			d = t + 1;
		}
		return d;
	}

}
