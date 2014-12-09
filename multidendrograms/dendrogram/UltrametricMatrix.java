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
import multidendrograms.utils.DeviationMeasures;
import multidendrograms.utils.MathUtils;

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
	private PrintWriter pw;
	private LinkedList<SimilarityStruct<String>> originalData;
	private Cluster root;
	private int precision;
	private String[] names;
	private Hashtable<String, Integer> htNames;
	private int size;
	private double[][] ultraMatrix = null;
	private double[][] originalMatrix = null;

	public UltrametricMatrix(LinkedList<SimilarityStruct<String>> originalData, Cluster root, int precision) {
		this.originalData = originalData;
		this.root = root;
		this.precision = precision;

		this.size = root.getNumLeaves();
		this.ultraMatrix = new double[size][size];
		this.originalMatrix = new double[size][size];

		sortNamesByLeaf();
		calculateUltrametricMatrix(root);
		calculateOriginalMatrix();
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

	private void calculateUltrametricMatrix(final Cluster c) {
		if (c.getNumSubclusters() > 1) {
			double height = MathUtils.round(c.getSummaryHeight(), this.precision);
			List<Cluster> leaves = c.getLeaves();
			for (int i = 0; i < leaves.size(); i ++) {
				Cluster ci = leaves.get(i);
				int posi = this.htNames.get(ci.getName());
				this.ultraMatrix[posi][posi] = 0.0;
				for (int j = i + 1; j < leaves.size(); j ++) {
					Cluster cj = leaves.get(j);
					int posj = this.htNames.get(cj.getName());
					this.ultraMatrix[posi][posj] = height;
					this.ultraMatrix[posj][posi] = height;
				}
			}
			for (int n = 0; n < c.getNumSubclusters(); n ++) {
				try {
					calculateUltrametricMatrix(c.getSubcluster(n));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void calculateOriginalMatrix() {
		int posi, posj;
		SimilarityStruct<String> sim;

		for (int s = 0; s < this.originalData.size(); s ++) {
			sim = this.originalData.get(s);
			posi = this.htNames.get(sim.getC1());
			posj = this.htNames.get(sim.getC2());
			this.originalMatrix[posi][posj] = sim.getVal();
			this.originalMatrix[posj][posi] = this.originalMatrix[posi][posj];
		}
		for (int i = 0; i < this.size; i ++) {
			this.originalMatrix[i][i] = 0.0;
		}
	}

	public void saveAsTXT(String path, int precision) throws Exception {
		try {
			File f = new File(path);
			FileWriter fw = new FileWriter(f);
			pw = new PrintWriter(fw);
			printUltrametricMatrix(precision);
			pw.close();
		} catch (Exception e) {
			String msg_err = Language.getLabel(81);
			LogManager.LOG.throwing("UltrametricTXT.java", "saveFile()", e);
			e.printStackTrace();
			throw new Exception(msg_err);
		}
	}

	private void printUltrametricMatrix(int prec) {
		String str = "";

		for (int i = 0; i < names.length; i++)
			str += names[i] + "\t";
		pw.println(str);

		int n = ultraMatrix.length;
		for (int i = 0; i < n; i++) {
			str = "";
			for (int j = 0; j < n; j++)
				str += MathUtils.format(ultraMatrix[i][j], prec) + "\t";
			pw.println(str);
		}
	}

	public double getCopheneticCorrelation() {
		return DeviationMeasures.getCopheneticCorrelation(originalMatrix, ultraMatrix);
	}

	public double getSquaredError() {
		return DeviationMeasures.getSquaredError(originalMatrix, ultraMatrix);
	}

	public double getAbsoluteError() {
		return DeviationMeasures.getAbsoluteError(originalMatrix, ultraMatrix);
	}

	public void showMatrix(double[][] matriu) {
		String cad = "";
		int n = matriu.length;
		for (int i = 0; i < n; i++) {
			cad = "";
			for (int j = 0; j < n; j++)
				cad += matriu[i][j] + "\t";
			System.out.println(cad);
		}
	}
}
