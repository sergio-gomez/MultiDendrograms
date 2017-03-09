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
 * Save dendrogram as Newick tree text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToNewick {

	private Dendrogram root;
	private boolean isUniformOrigin;
	private double dendroBottomHeight;
	private PrintWriter printWriter;

	public ToNewick(Dendrogram root, boolean isUniformOrigin) {
		this.root = root;
		this.isUniformOrigin = isUniformOrigin;
		SmartAxis smartAxis = new SmartAxis(root, isUniformOrigin);
		this.dendroBottomHeight = root.isDistanceBased ? 
				smartAxis.smartMin() : smartAxis.smartMax();
	}

	public void saveAsNewick(String path) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file);
		this.printWriter = new PrintWriter(fileWriter);
		showCluster(this.root, this.root.getRootBottomHeight());
		this.printWriter.print(";");
		this.printWriter.close();
	}

	private void showCluster(Dendrogram cluster, double parentHeight) {
		int numSubclusters = cluster.numberOfSubclusters();
		double clusterBottomHeight = cluster.getRootBottomHeight();
		double clusterHeight;
		if (numSubclusters == 1) {
			String name = adaptName(cluster.getLabel());
			this.printWriter.print(name);
			if (Double.isNaN(clusterBottomHeight) || this.isUniformOrigin) {
				clusterHeight = this.dendroBottomHeight;
			} else {
				clusterHeight = clusterBottomHeight;
			}
		} else {// (numSubclusters > 1)
			clusterHeight = clusterBottomHeight;
			this.printWriter.print("(");
			for (int n = 0; n < numSubclusters; n ++) {
				showCluster(cluster.getSubcluster(n), clusterHeight);
				if (n < numSubclusters - 1) {
					this.printWriter.print(",");
				}
			}
			this.printWriter.print(")");
		}
		printClusterLength(parentHeight, clusterHeight);
	}

	private String adaptName(String originalName) {
		String newName = new String(originalName);
		newName = newName.replace(' ', '_');
		newName = newName.replace('\'', '"');
		newName = newName.replace(':', '|');
		newName = newName.replace(';', '|');
		newName = newName.replace(',', '|');
		newName = newName.replace('(', '{');
		newName = newName.replace(')', '}');
		newName = newName.replace('[', '{');
		newName = newName.replace(']', '}');
		return newName;
	}

	private void printClusterLength(double parentHeight, double clusterHeight) {
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
		if (length > 0.0) {
			String sLength = NumberUtils.format(length, precision);
			this.printWriter.print(":" + sLength);
		}
	}

}
