/*
 * Copyright (C) Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * <http://www.gnu.org/licenses/>
 */

package multidendrograms.dendrogram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.utils.MathUtils;
import multidendrograms.core.utils.SmartAxis;
import multidendrograms.utils.NumberUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Save dendrogram as JSON text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToJson {

	private Dendrogram root;
	private boolean isUniformOrigin;
	private double dendroBottomHeight;
	private PrintWriter printWriter;

	public ToJson(Dendrogram root, boolean isUniformOrigin) {
		this.root = root;
		this.isUniformOrigin = isUniformOrigin;
		SmartAxis smartAxis = new SmartAxis(root, isUniformOrigin);
		this.dendroBottomHeight = root.isDistanceBased ? 
				smartAxis.smartMin() : smartAxis.smartMax();
	}

	public void saveAsJson(String sPath) throws IOException {
		File file = new File(sPath);
		FileWriter fileWriter = new FileWriter(file);
		printWriter = new PrintWriter(fileWriter);
		showCluster(this.root, this.root.getRootBottomHeight(), "");
		printWriter.println("");
		printWriter.close();
	}

	private void showCluster(Dendrogram cluster, double parentHeight, 
			String blanks) {
		int numSubclusters = cluster.numberOfSubclusters();
		double clusterBottomHeight = cluster.getRootBottomHeight();
		if (numSubclusters == 1) {
			double clusterHeight;
			if (Double.isNaN(clusterBottomHeight) || this.isUniformOrigin) {
				clusterHeight = this.dendroBottomHeight;
			} else {
				clusterHeight = clusterBottomHeight;
			}
			double topHeight = clusterHeight;
			double bottomHeight = clusterHeight;
			String str = blanks + "{\"name\": \"" + cluster.getLabel() + "\", ";
			str += getClusterHeight(clusterHeight) + ", ";
			str += getClusterMargin(topHeight, bottomHeight) + ", ";
			str += getClusterLength(parentHeight, clusterHeight) + ", ";
			str += "\"size\": 1}";
			this.printWriter.print(str);
		} else {
			double clusterHeight = clusterBottomHeight;
			double topHeight = cluster.getRootTopHeight();
			double bottomHeight = cluster.getRootBottomHeight();
			this.printWriter.println(blanks + "{");
			String str = blanks + " \"name\": \"\", ";
			str += getClusterHeight(clusterHeight) + ", ";
			str += getClusterMargin(topHeight, bottomHeight) + ", ";
			str += getClusterLength(parentHeight, clusterHeight) + ",";
			this.printWriter.println(str);
			str = blanks + " \"children\": [";
			this.printWriter.println(str);
			for (int n = 0; n < numSubclusters; n ++) {
				showCluster(cluster.getSubcluster(n), clusterHeight, 
						blanks + "  ");
				if (n < numSubclusters - 1) {
					this.printWriter.print(",");
				}
				this.printWriter.println("");
			}
			this.printWriter.println(blanks + " ]");
			this.printWriter.print(blanks + "}");
		}
	}

	private String getClusterHeight(double clusterHeight) {
		int precision = this.root.precision;
		String sHeight = NumberUtils.format(clusterHeight, precision);
		return "\"height\": " + sHeight;
	}

	private String getClusterMargin(double topHeight, double bottomHeight) {
		int precision = this.root.precision;
		double margin = Math.abs(topHeight - bottomHeight);
		String sMargin = NumberUtils.format(margin, precision);
		return "\"margin\": " + sMargin;
	}

	private String getClusterLength(double parentHeight, double clusterHeight) {
		int precision = this.root.precision;
		double length;
		if (this.root.isDistanceBased) {
			length = MathUtils.round(parentHeight, precision) - 
					 MathUtils.round(clusterHeight, precision);
		} else {
			length = MathUtils.round(clusterHeight, precision) - 
					 MathUtils.round(parentHeight, precision);
		}
 		length = MathUtils.round(length, precision);
		String sLength = NumberUtils.format(length, precision);
		return "\"length\": " + sLength;
	}

}
