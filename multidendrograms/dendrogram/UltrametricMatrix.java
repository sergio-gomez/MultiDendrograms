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
import java.util.Hashtable;

import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.definitions.SymmetricMatrix;
import multidendrograms.core.utils.MathUtils;
import multidendrograms.core.utils.SmartAxis;
import multidendrograms.types.BandHeight;
import multidendrograms.types.OriginType;
import multidendrograms.utils.NumberUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculate and save ultrametric matrix
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 */
public class UltrametricMatrix {

	private int precision;
	private String[] labels;
	private Hashtable<String, Integer> hashLabels;
	private SymmetricMatrix ultraMatrix;

	public UltrametricMatrix(Dendrogram root, String[] externLabels, 
			OriginType originType, BandHeight bandHeight) {
		this.precision = root.precision;
		this.labels = externLabels;
		this.hashLabels = getSorting(externLabels);
		boolean isUniformOrigin = 
				originType.equals(OriginType.UNIFORM_ORIGIN)? true : false;
		boolean useBandBottom = 
				bandHeight.equals(BandHeight.BAND_BOTTOM)? true : false;
		SmartAxis smartAxis = new SmartAxis(root, isUniformOrigin);
		double dendroBottomHeight = root.isDistanceBased? 
				smartAxis.smartMin() : smartAxis.smartMax();
		this.ultraMatrix = new SymmetricMatrix(root.numberOfLeaves());
		calculateUltrametricMatrix(root, dendroBottomHeight, isUniformOrigin, 
				useBandBottom);
	}

	private Hashtable<String, Integer> getSorting(String[] externLabels) {
		Hashtable<String, Integer> sorting = new Hashtable<String, Integer>();
		for (int i = 0; i < externLabels.length; i ++) {
			sorting.put(externLabels[i], i);
		}
		return sorting;
	}

	private void calculateUltrametricMatrix(Dendrogram cluster, 
			double dendroBottomHeight, boolean isUniformOrigin, 
			boolean useBandBottom) {
		int numSubclusters = cluster.numberOfSubclusters();
		if (numSubclusters == 1) {
			double clusterBottomHeight = cluster.getRootBottomHeight();
			double clusterHeight = 
					(Double.isNaN(clusterBottomHeight) || isUniformOrigin) ? 
					MathUtils.round(dendroBottomHeight, this.precision) : 
					MathUtils.round(clusterBottomHeight, this.precision);
			int i = this.hashLabels.get(cluster.getLabel());
			this.ultraMatrix.setElement(i, i, clusterHeight);
		} else {// (numSubclusters > 1)
			double clusterHeight = useBandBottom ? 
					cluster.getRootBottomHeight() : 
					cluster.getRootInternalHeight();
			clusterHeight = MathUtils.round(clusterHeight, this.precision);
			int numLeaves = cluster.numberOfLeaves();
			for (int m = 0; m < numLeaves - 1; m ++) {
				Dendrogram ci = cluster.getLeaf(m);
				int i = this.hashLabels.get(ci.getLabel());
				for (int n = m + 1; n < numLeaves; n ++) {
					Dendrogram cj = cluster.getLeaf(n);
					int j = this.hashLabels.get(cj.getLabel());
					this.ultraMatrix.setElement(i, j, clusterHeight);
				}
			}
			for (int n = 0; n < numSubclusters; n ++) {
				calculateUltrametricMatrix(cluster.getSubcluster(n), 
						dendroBottomHeight, isUniformOrigin, useBandBottom);
			}
		}
	}

	public SymmetricMatrix getMatrix() {
		return this.ultraMatrix;
	}

	public void saveAsTxt(String path) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		String str = "";
		for (int i = 0; i < this.labels.length; i ++) {
			str += this.labels[i] + "\t";
		}
		printWriter.println(str);
		for (int i = 0; i < this.ultraMatrix.numberOfRows(); i ++) {
			str = "";
			for (int j = 0; j < this.ultraMatrix.numberOfRows(); j ++) {
				double clusterHeight = this.ultraMatrix.getElement(i, j);
				str += NumberUtils.format(clusterHeight, this.precision);
				str += "\t";
			}
			printWriter.println(str);
		}
		printWriter.close();
	}

}
