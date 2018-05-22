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

package multidendrograms.core.definitions;

import java.util.Arrays;

/******************************************************************************
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Stores a symmetric matrix
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 ******************************************************************************/
public class SymmetricMatrix {

	// Diagonal elements
	private double[] diagonal;
	// Lower triangular elements by columns
	private double[] lowerTriangle;
	// Minimum and maximum lower triangular values
	private double minValue = Double.POSITIVE_INFINITY;
	private double maxValue = Double.NEGATIVE_INFINITY;
	// Maximum number of decimal digits in any element
	private int maxDecimals = 0;

	public SymmetricMatrix(double[] lowerTriangle) {
		int numElements = lowerTriangle.length;
		int numRows = (1 + (int)Math.sqrt(1 + 8 * numElements)) / 2;
		this.diagonal = new double[numRows];
		Arrays.fill(this.diagonal, Double.NaN);
		this.lowerTriangle = lowerTriangle;
		for (int index = 0; index < numElements; index ++) {
			double value = lowerTriangle[index];
			this.minValue = Math.min(this.minValue, value);
			this.maxValue = Math.max(this.maxValue, value);
			countDecimals(value);
		}
	}

	public SymmetricMatrix(double lowerTriangle) {
		this(new double[] {lowerTriangle});
	}

	public SymmetricMatrix(int numRows) {
		this.diagonal = new double[numRows];
		Arrays.fill(this.diagonal, Double.NaN);
		int numElements = (numRows - 1) * numRows / 2;
		this.lowerTriangle = new double[numElements];
		Arrays.fill(this.lowerTriangle, Double.NaN);
	}

	public void setElement(int i, int j, double value) {
		if (i == j) {
			this.diagonal[i] = value;
		} else {
			int index = (i > j) ? getIndex(i, j) : getIndex(j, i);
			this.lowerTriangle[index] = value;
			this.minValue = Math.min(this.minValue, value);
			this.maxValue = Math.max(this.maxValue, value);
		}
		countDecimals(value);
	}

	private void countDecimals(double value) {
		if (!Double.isNaN(value)) {
			double absValue = Math.abs(value);
			long rndValue = Math.round(absValue);
			if (rndValue != absValue) {
				String strAbs = String.valueOf(absValue);
				String strRnd = String.valueOf(rndValue);
				int numDecimals = strAbs.length() - strRnd.length() - 1;
				this.maxDecimals = Math.max(this.maxDecimals, numDecimals);
			}
		}
	}

	public double getElement(int i, int j) {
		if (i == j) {
			return this.diagonal[i];
		} else {
			int index = (i > j) ? getIndex(i, j) : getIndex(j, i);
			return this.lowerTriangle[index];
		}
	}

	private int getIndex(int i, int j) {
		int n = this.diagonal.length;
		return (2 * n - j - 1) * j / 2 + i - j - 1;
	}

	public int numberOfRows() {
		return this.diagonal.length;
	}

	public double minimumValue() {
		return this.minValue;
	}

	public double maximumValue() {
		return this.maxValue;
	}

	public int getPrecision() {
		return this.maxDecimals;
	}

}
