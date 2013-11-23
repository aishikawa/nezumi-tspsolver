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

import util.Util;

public class ExplicitInstance extends Instance {

	private long[][] distance;
	
	protected ExplicitInstance(String n, String t, String c, String e, int d, List<String> data, String edgeWeightFormat) throws InstanceException {
		super(n, t, c, e, d);
		makeDistance(data, edgeWeightFormat);
	}
	
	private void makeDistance(List<String> data, String edgeWeightFormat) throws InstanceException {
		distance = new long[dimension][dimension];
		
		int edgeWeightSectionStartLine = -1;
		int lineNum = 0;
		for (String line : data) {
			if (line.startsWith("EDGE_WEIGHT_SECTION")) {
				edgeWeightSectionStartLine = lineNum;
				break;
			}
			lineNum += 1;
		}
		if (edgeWeightFormat.equals("FULL_MATRIX")) {
			int numData = dimension * dimension;
			long d[] = readEdgeWeight(data, edgeWeightSectionStartLine+1, numData);

			int index = 0;
			for (int from=0; from<dimension; from++) {
				for (int to=0; to<dimension; to++) {
					distance[from][to] = d[index];
					index += 1;
				}
			} 
		} else if (edgeWeightFormat.equals("UPPER_ROW")) {
			int numData = (dimension * dimension - dimension) / 2;
			long d[] = readEdgeWeight(data, edgeWeightSectionStartLine+1, numData);
			
			int index = 0;
			for (int from=0; from<dimension; from++) {
				for (int to=from+1; to<dimension; to++) {
					distance[from][to] = distance[to][from] = d[index];
					index += 1;
				}
			}
		} else if (edgeWeightFormat.equals("UPPER_DIAG_ROW")) {
			int numData = (dimension * dimension - dimension) / 2 + dimension;
			long d[] = readEdgeWeight(data, edgeWeightSectionStartLine+1, numData);
			
			int index = 0;
			for (int from=0; from<dimension; from++) {
				for (int to=from; to<dimension; to++) {
					distance[from][to] = distance[to][from] = d[index];
					index += 1;
				}
			}
		} else if (edgeWeightFormat.equals("LOWER_DIAG_ROW")) {
			int numData = (dimension * dimension - dimension) / 2 + dimension;
			long d[] = readEdgeWeight(data, edgeWeightSectionStartLine+1, numData);
			
			int index = 0;
			for (int from=0; from<dimension; from++) {
				for (int to=0; to<=from; to++) {
					distance[from][to] = distance[to][from] = d[index];
					index += 1;
				}
			}
		} else {
			throw new InstanceException("This solver does not support EDGE_WEIGHT_FORMAT: "+edgeWeightFormat);
		}
	}
	
	private long[] readEdgeWeight(List<String> data, int startLine, int numData) {
		int dataIndex = 0;
		int lineIndex = startLine;
		long[] d = new long[numData];
		
		while (dataIndex != numData) {
			String line = data.get(lineIndex);
			String str = Util.removeSpaces(line);
			String a[] = str.split("\\s+");
			
			for (int i=0; i<a.length; i++) {
				d[dataIndex] = Long.parseLong(a[i]);
				dataIndex += 1;
			}
			lineIndex += 1;
		}
		
		return d;
	}
	
	@Override
	public long getDistance(int from, int to) {
		return distance[from-1][to-1];
	}

}
