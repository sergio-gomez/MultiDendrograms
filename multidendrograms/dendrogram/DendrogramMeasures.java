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
import java.text.NumberFormat;
import java.util.Locale;

import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.definitions.SymmetricMatrix;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates and exports dendrogram measures
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 */
public class DendrogramMeasures {

	public static final String COPHENETIC_CORRELATION_LABEL = "Cophenetic Correlation Coefficient";
	public static final String SQUARED_ERROR_LABEL = "Normalized Mean Squared Error";
	public static final String ABSOLUTE_ERROR_LABEL = "Normalized Mean Absolute Error";
	public static final String TREE_BALANCE_LABEL = "Normalized Tree Balance";
	public static final String SPACE_DISTORTION_LABEL = "Space Distortion";
	public static final String DEGREE_CONNECTIVITY_LABEL = "Degree of Connectivity";

	private String copheneticCorrelation;
	private String squaredError;
	private String absoluteError;
	private String treeBalance;
	private String spaceDistortion;
	private String degreeConnectivity;

	public DendrogramMeasures(SymmetricMatrix proxMatrix, Dendrogram root, SymmetricMatrix ultraMatrix) {
		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		nf.setGroupingUsed(false);
		double ccc = copheneticCorrelationCoefficient(proxMatrix, ultraMatrix);
		this.copheneticCorrelation = Double.isNaN(ccc)? "NaN" : nf.format(ccc);
		this.squaredError = nf.format(normalizedMeanError(2, proxMatrix, ultraMatrix));
		this.absoluteError = nf.format(normalizedMeanError(1, proxMatrix, ultraMatrix));
		this.treeBalance = nf.format(root.normalizedTreeBalance());
		this.spaceDistortion = nf.format(spaceDistortion(proxMatrix, ultraMatrix));
		this.degreeConnectivity = nf.format(degreeOfConnectivity(proxMatrix, root.isDistanceBased, ultraMatrix));
	}

	public String getCopheneticCorrelation() {
		return this.copheneticCorrelation;
	}

	public String getSquaredError() {
		return this.squaredError;
	}

	public String getAbsoluteError() {
		return this.absoluteError;
	}

	public String getTreeBalance() {
		return this.treeBalance;
	}

	public String getSpaceDistortion() {
		return this.spaceDistortion;
	}

	public String getDegreeOfConnectivity() {
		return this.degreeConnectivity;
	}

	public void save(String path) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println(DendrogramMeasures.COPHENETIC_CORRELATION_LABEL + " : " + this.copheneticCorrelation);
		printWriter.println(DendrogramMeasures.SQUARED_ERROR_LABEL + "      : " + this.squaredError);
		printWriter.println(DendrogramMeasures.ABSOLUTE_ERROR_LABEL + "     : " + this.absoluteError);
		printWriter.println(DendrogramMeasures.TREE_BALANCE_LABEL + "            : " + this.treeBalance);
		printWriter.println(DendrogramMeasures.SPACE_DISTORTION_LABEL + "                   : " + this.spaceDistortion);
		printWriter.close();
	}

	private double copheneticCorrelationCoefficient(
			SymmetricMatrix proxMatrix, SymmetricMatrix ultraMatrix) {
		int numRows = proxMatrix.numberOfRows();
		int numValues = (numRows - 1) * numRows / 2;

		double avgProx = 0.0;
		double avgUltra = 0.0;
		for (int i = 0; i < numRows - 1; i ++) {
			for (int j = i + 1; j < numRows; j ++) {
				avgProx += proxMatrix.getElement(i, j);
				avgUltra += ultraMatrix.getElement(i, j);
			}
		}
		avgProx /= (double)numValues;
		avgUltra /= (double)numValues;

		double cccNum = 0.0;
		double sigmaProx = 0.0;
		double sigmaUltra = 0.0;
		for (int i = 0; i < numRows - 1; i ++) {
			for (int j = i + 1; j < numRows; j ++) {
				double proxIJ = proxMatrix.getElement(i, j);
				double ultraIJ = ultraMatrix.getElement(i, j);
				cccNum += (proxIJ - avgProx) * (ultraIJ - avgUltra);
				sigmaProx += (proxIJ - avgProx) * (proxIJ - avgProx);
				sigmaUltra += (ultraIJ - avgUltra) * (ultraIJ - avgUltra);
			}
		}
		cccNum /= (double)numValues;
		sigmaProx = Math.sqrt(sigmaProx / (double)numValues);
		sigmaUltra = Math.sqrt(sigmaUltra / (double)numValues);
		if ((sigmaProx == 0.0) || (sigmaUltra == 0.0)) {
			return Double.NaN;
		} else {
			return cccNum / (sigmaProx * sigmaUltra);
		}
	}

	private double normalizedMeanError(double power, SymmetricMatrix proxMatrix,
	    SymmetricMatrix ultraMatrix) {
		double num = 0.0;
		double den = 0.0;
		for (int i = 0; i < proxMatrix.numberOfRows() - 1; i ++) {
			for (int j = i + 1; j < proxMatrix.numberOfRows(); j ++) {
				double proxIJ = proxMatrix.getElement(i, j);
				double ultraIJ = ultraMatrix.getElement(i, j);
				num += Math.pow(Math.abs(proxIJ - ultraIJ), power);
				den += Math.pow(Math.abs(proxIJ), power);
			}
		}
		return num / den;
	}

	private double spaceDistortion(SymmetricMatrix proxMatrix, SymmetricMatrix ultraMatrix) {
		return (ultraMatrix.maximumValue() - ultraMatrix.minimumValue()) /
				(proxMatrix.maximumValue() - proxMatrix.minimumValue());
	}

	private double degreeOfConnectivity(SymmetricMatrix proxMatrix, boolean isDistanceBased,
	    SymmetricMatrix ultraMatrix) {
		int maxEdges = 0;
		int numEdges = 0;
		for (int i = 0; i < proxMatrix.numberOfRows(); i ++) {
			for (int j = i + 1; j < proxMatrix.numberOfRows(); j ++) {
				maxEdges ++;
				double proxIJ = proxMatrix.getElement(i, j);
				double ultraIJ = ultraMatrix.getElement(i, j);
				if (( isDistanceBased && (proxIJ <= ultraIJ)) || (!isDistanceBased && (proxIJ >= ultraIJ))) {
					numEdges ++;
				}
			}
		}
		return (double)numEdges / (double)maxEdges;
	}

}
