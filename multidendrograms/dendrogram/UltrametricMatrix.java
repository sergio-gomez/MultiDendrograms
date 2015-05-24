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
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.data.SimilarityStruct;
import multidendrograms.definitions.Cluster;
import multidendrograms.types.OriginType;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.DeviationMeasures;
import multidendrograms.utils.MathUtils;
import multidendrograms.utils.SmartAxis;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculate and save ultrametric matrix
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class UltrametricMatrix {

	private LinkedList<SimilarityStruct<String>> originalData;
	private Cluster root;
	private int precision;
	private OriginType originType;
	private double dendroBottomHeight;
	private int size;
	private Hashtable<String, Integer> htNames;
	private String[] names;
	private double[][] originalMatrix = null;
	private double[][] ultraMatrix = null;

	public UltrametricMatrix(final LinkedList<SimilarityStruct<String>> originalData, final Cluster root,
			final int precision, final SimilarityType simType, final OriginType originType) {
		this.originalData = originalData;
		this.root = root;
		this.precision = precision;
		this.originType = originType;
		SmartAxis smartAxis = new SmartAxis(simType, precision, originType, root);
		if (simType.equals(SimilarityType.DISTANCE)) {
			this.dendroBottomHeight = smartAxis.smartMin();
		} else {// (simType.equals(SimilarityType.SIMILARITY))
			this.dendroBottomHeight = smartAxis.smartMax();
		}
		this.size = root.getNumLeaves();
		this.originalMatrix = new double[this.size][this.size];
		this.ultraMatrix = new double[this.size][this.size];
		sortNamesByLeaf();
		calculateOriginalMatrix();
		calculateUltrametricMatrix(root);
	}

	private void sortNamesByLeaf() {
		List<Cluster> leavesList = this.root.getLeaves();
		List<String> namesList = new LinkedList<String>();
		this.htNames = new Hashtable<String, Integer>();
		for (int i = 0; i < leavesList.size(); i ++) {
			namesList.add((leavesList.get(i)).getName());
		}
		this.names = new String[this.size];
		for (int i = 0; i < namesList.size(); i ++) {
			this.htNames.put(namesList.get(i), i);
			this.names[i] = namesList.get(i);
		}
	}

	private void calculateOriginalMatrix() {
		// Initialize diagonal elements
		for (int i = 0; i < this.size; i ++) {
			this.originalMatrix[i][i] = Double.NaN;
		}
		for (int n = 0; n < this.originalData.size(); n ++) {
			SimilarityStruct<String> sim = this.originalData.get(n);
			int i = this.htNames.get(sim.getC1());
			int j = this.htNames.get(sim.getC2());
			this.originalMatrix[i][j] = sim.getValue();
			this.originalMatrix[j][i] = this.originalMatrix[i][j];
		}
		// Unassigned diagonal elements
		for (int i = 0; i < this.size; i ++) {
			if (Double.isNaN(this.originalMatrix[i][i]) || this.originType.equals(OriginType.UNIFORM_ORIGIN)) {
				this.originalMatrix[i][i] = this.dendroBottomHeight;
			}
		}
	}

	private void calculateUltrametricMatrix(final Cluster cluster) {
		double clusterBottomHeight = cluster.getRootBottomHeight();
		int numSubclusters = cluster.getNumSubclusters();
		if (numSubclusters == 1) {
			double clusterHeight;
			if (Double.isNaN(clusterBottomHeight) || this.originType.equals(OriginType.UNIFORM_ORIGIN)) {
				clusterHeight = MathUtils.round(this.dendroBottomHeight, this.precision);
			} else {
				clusterHeight = MathUtils.round(clusterBottomHeight, this.precision);
			}
			int i = this.htNames.get(cluster.getName());
			this.ultraMatrix[i][i] = clusterHeight;
		} else {// (numSubclusters > 1)
			double clusterHeight = MathUtils.round(clusterBottomHeight, this.precision);
			List<Cluster> leaves = cluster.getLeaves();
			for (int m = 0; m < leaves.size(); m ++) {
				Cluster ci = leaves.get(m);
				int i = this.htNames.get(ci.getName());
				for (int n = m + 1; n < leaves.size(); n ++) {
					Cluster cj = leaves.get(n);
					int j = this.htNames.get(cj.getName());
					this.ultraMatrix[i][j] = clusterHeight;
					this.ultraMatrix[j][i] = clusterHeight;
				}
			}
			for (int n = 0; n < numSubclusters; n ++) {
				try {
					calculateUltrametricMatrix(cluster.getSubcluster(n));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

    public double[][] getUltraMatrix() {
        return ultraMatrix;
    }

	public void saveAsTxt(String path, int precision) throws Exception {
		try {
			File f = new File(path);
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			String str = "";
			for (int i = 0; i < this.names.length; i ++) {
				str += this.names[i] + "\t";
			}
			pw.println(str);
			for (int i = 0; i < this.ultraMatrix.length; i ++) {
				str = "";
				for (int j = 0; j < this.ultraMatrix.length; j ++) {
					str += MathUtils.format(this.ultraMatrix[i][j], precision) + "\t";
				}
				pw.println(str);
			}
			pw.close();
		} catch (Exception e) {
			String msg_err = Language.getLabel(81);
			LogManager.LOG.throwing("UltrametricTXT.java", "saveFile()", e);
			e.printStackTrace();
			throw new Exception(msg_err);
		}
	}

	public double getCopheneticCorrelation() {
		return DeviationMeasures.getCopheneticCorrelation(this.originalMatrix, this.ultraMatrix);
	}

	public double getSquaredError() {
		return DeviationMeasures.getSquaredError(this.originalMatrix, this.ultraMatrix);
	}

	public double getAbsoluteError() {
		return DeviationMeasures.getAbsoluteError(this.originalMatrix, this.ultraMatrix);
	}

	public void showMatrix(double[][] matrix) {
		String str = "";
		for (int i = 0; i < matrix.length; i ++) {
			str = "";
			for (int j = 0; j < matrix.length; j ++) {
				str += matrix[i][j] + "\t";
			}
			System.out.println(str);
		}
	}
}
