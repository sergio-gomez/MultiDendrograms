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

package multidendrograms.definitions;

import java.util.Vector;

import multidendrograms.initial.Language;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Stores the distances matrix and its clustering, and implements methods for its management
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DistancesMatrix {

	// Type of data stored in the matrix
	private SimilarityType simType = SimilarityType.DISTANCE;

	// When it's 'true', it indicates that the matrix has a single element
	private boolean isSingle = false;

	private int numElems = 0;

	// Distances minimum and maximum among all existing distances
	private double minValue = Double.MAX_VALUE;
	private double maxValue = Double.MIN_VALUE;

	// Storage of distance data
	private Vector<Cluster> arrClusters;
	private Vector<Vector<Double>> arrDistances;
	private IdToKey<Integer> claus;

	public DistancesMatrix(int size, final SimilarityType simType) throws Exception {
		this.simType = simType;
		if (size < 2) {
			this.isSingle = true;
			size = 2;
		} else {
			this.isSingle = false;
		}
		initialize(size);
	}

	public DistancesMatrix(int size) throws Exception {
		this.simType = SimilarityType.DISTANCE;
		if (size < 2) {
			this.isSingle = true;
			size = 2;
		} else {
			this.isSingle = false;
		}
		initialize(size);
	}

	private void initialize(final int size) throws Exception {
		arrClusters = new Vector<Cluster>(size);
		arrDistances = new Vector<Vector<Double>>(size);
		for (int n = 0; n < size; n++) {
			try {
				arrDistances.add(new Vector<Double>((size - n - 1)));
				for (int nn = 0; nn < (size - n - 1); nn++) {
					arrDistances.get(n).add(null);
				}
			} catch (final Exception e) {
				String err_msg;
				err_msg = e.getMessage();
				err_msg += "\n" + Language.getLabel(73);
				throw new Exception(err_msg);
			}
		}
		claus = new IdToKey<Integer>();
	}

	public boolean isUnari() {
		return isSingle;
	}

	public boolean existsDistance(final Cluster ci, final Cluster cii) {
		return (claus.containsKey(ci.getId()) && claus.containsKey(cii.getId()));
	}

	public void setDistance(final Cluster ci, final Cluster cii, final double dis) throws Exception {
		int k1, k2, tmp;

		if (claus.containsKey(ci.getId())) {
			k1 = claus.getIndex(ci.getId());
		} else {
			k1 = claus.setIndex(ci.getId());
			numElems++;
			arrClusters.add(k1, ci);
		}
		if (claus.containsKey(cii.getId())) {
			k2 = claus.getIndex(cii.getId());
		} else {
			k2 = claus.setIndex(cii.getId());
			numElems++;
			arrClusters.add(k2, cii);
		}
		try {
			if (k1 > k2) {
				tmp = k2;
				k2 = k1;
				k1 = tmp;
			}
			if ((k1 == k2) && (k1 == 0)) {
				// single cluster matrix
				arrDistances.get(k1).setElementAt(new Double(dis), k1);
			} else {
				arrDistances.get(k1).setElementAt(new Double(dis), k2 - k1 - 1);
			}
		} catch (final Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(71);
			throw new Exception(err_msg);
		}
		if (dis < minValue || minValue == Double.MAX_VALUE) {
			minValue = new Double(dis);
		}
		if (dis > maxValue || maxValue == Double.MIN_VALUE) {
			maxValue = new Double(dis);
		}
	}

	public void setDistance(final Cluster ci) throws Exception {
		if (numElems == 0) {
			this.setDistance(ci, ci, 0);
			isSingle = true;
			numElems = 1;
		}
	}

	public double getDistance(final Cluster i, final Cluster ii) throws Exception {
		int tmp;
		int k1 = 0, k2 = 0;
		double dis = 0.0;

		try {
			k1 = claus.getIndex(i.getId());
			k2 = claus.getIndex(ii.getId());
			if (k1 > k2) {
				tmp = k2;
				k2 = k1;
				k1 = tmp;
			}
			dis = arrDistances.get(k1).get(k2 - k1 - 1);
		} catch (final Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(70);
			throw new Exception(err_msg);
		}
		return dis;
	}

	public int getCardinality() {
		return numElems;
	}

	public Double minValue() {
		return minValue;
	}

	public Double maxValue() {
		return maxValue;
	}

	public Vector<Cluster> getClusters() {
		return arrClusters;
	}

	public Cluster getCluster(final int pos) {
		return arrClusters.elementAt(pos);
	}

	public double[][] getMatrix() throws Exception {
		final double[][] matrix = new double[numElems][numElems];
		try {
			for (int n = 0; n < numElems - 1; n++) {
				for (int m = n + 1; m < numElems; m++) {
					matrix[n][m] = this.getDistance(arrClusters.get(n), arrClusters.get(m));
					matrix[m][n] = matrix[n][m];
				}
			}
		} catch (Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(72);
			throw new Exception(err_msg);
		}
		return matrix;
	}

	public Cluster getRoot() {
		return this.getCluster(0);
	}

	public SimilarityType getSimilarityType() {
		return simType;
	}

	public void setSimilarityType(final SimilarityType simType) {
		this.simType = simType;
	}

	public boolean isDistancesType() {
		return simType.equals(SimilarityType.DISTANCE);
	}

}
