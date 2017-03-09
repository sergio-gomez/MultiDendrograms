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

package multidendrograms.utils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculation of deviation measures
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DeviationMeasures {

	public static double getCopheneticCorrelation(double[][] distsMatrix, double[][] ultraMatrix) {
		int size = distsMatrix.length;
		int numDists = 0;

		double avgDists = 0.0, avgUltra = 0.0;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				avgDists += distsMatrix[i][j];
				avgUltra += ultraMatrix[i][j];
				numDists++;
			}
		}
		avgDists /= numDists;
		avgUltra /= numDists;

		double ccNum = 0.0;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				ccNum += (distsMatrix[i][j] - avgDists) * (ultraMatrix[i][j] - avgUltra);
			}
		}
		ccNum /= numDists;

		double ccDen = 0.0;
		double sigmaDists = 0.0, sigmaUltra = 0.0;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				sigmaDists += (distsMatrix[i][j] - avgDists) * (distsMatrix[i][j] - avgDists);
				sigmaUltra += (ultraMatrix[i][j] - avgUltra) * (ultraMatrix[i][j] - avgUltra);
			}
		}
		sigmaDists = Math.sqrt(sigmaDists / numDists);
		sigmaUltra = Math.sqrt(sigmaUltra / numDists);
		ccDen = sigmaDists * sigmaUltra;

		return ccNum / ccDen;
	}

	public static double getSquaredError(double[][] distsMatrix, double[][] ultraMatrix) {
		int size = distsMatrix.length;
		double num = 0.0, den = 0.0;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				num += (distsMatrix[i][j] - ultraMatrix[i][j]) * (distsMatrix[i][j] - ultraMatrix[i][j]);
				den += distsMatrix[i][j] * distsMatrix[i][j];
			}
		}
		return num / den;
	}

	public static double getAbsoluteError(double[][] distsMatrix, double[][] ultraMatrix) {
		int size = distsMatrix.length;
		double num = 0.0, den = 0.0;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				num += Math.abs(distsMatrix[i][j] - ultraMatrix[i][j]);
				den += Math.abs(distsMatrix[i][j]);
			}
		}
		return num / den;
	}

}
