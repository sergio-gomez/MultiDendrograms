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

	// Number of clusters
	private int numClusters = 0;

	// Distances minimum and maximum among all existing distances
	private double minValue = Double.POSITIVE_INFINITY;
	private double maxValue = Double.NEGATIVE_INFINITY;

	// Storage of distances data
	private Vector<Cluster> arrayClusters;
	private Vector<Vector<Double>> arraysDistances;
	private IdToKey<Integer> keys;

	public DistancesMatrix(final int size) throws Exception {
		arrayClusters = new Vector<Cluster>(size);
		arraysDistances = new Vector<Vector<Double>>(size);
		for (int n = 0; n < size; n ++) {
			try {
				arraysDistances.add(new Vector<Double>((size - n)));
				for (int nn = 0; nn < (size - n); nn ++) {
					arraysDistances.get(n).add(null);
				}
			} catch (final Exception e) {
				String err_msg = e.getMessage() + "\n" + Language.getLabel(73);
				throw new Exception(err_msg);
			}
		}
		keys = new IdToKey<Integer>();
	}

	public boolean existsDistance(final Cluster c1, final Cluster c2) {
		return (keys.containsKey(c1.getId()) && keys.containsKey(c2.getId()));
	}

	public void setDistance(final Cluster c1, final Cluster c2, final double dist) throws Exception {
		int k1;
		if (keys.containsKey(c1.getId())) {
			k1 = keys.getIndex(c1.getId());
		} else {
			k1 = keys.setIndex(c1.getId());
			numClusters ++;
			arrayClusters.add(k1, c1);
		}
		int k2;
		if (keys.containsKey(c2.getId())) {
			k2 = keys.getIndex(c2.getId());
		} else {
			k2 = keys.setIndex(c2.getId());
			numClusters ++;
			arrayClusters.add(k2, c2);
		}
		try {
			if (k1 > k2) {
				int tmp = k2;
				k2 = k1;
				k1 = tmp;
			}
			if ((k1 == k2) && (k2 == 0)) {
				// single cluster matrix
				arraysDistances.get(0).setElementAt(dist, 0);
			} else {
				arraysDistances.get(k1).setElementAt(dist, k2 - k1);
			}
		} catch (final Exception e) {
			String err_msg = e.getMessage() + "\n" + Language.getLabel(71);
			throw new Exception(err_msg);
		}
		if (k1 != k2) {
			minValue = Math.min(minValue, dist);
			maxValue = Math.max(maxValue, dist);
		}
	}

	public double getDistance(final Cluster c1, final Cluster c2) throws Exception {
		double dist = 0.0;
		try {
			int k1 = keys.getIndex(c1.getId());
			int k2 = keys.getIndex(c2.getId());
			if (k1 > k2) {
				int tmp = k2;
				k2 = k1;
				k1 = tmp;
			}
			dist = arraysDistances.get(k1).get(k2 - k1);
		} catch (final Exception e) {
			String err_msg = e.getMessage() + "\n" + Language.getLabel(70);
			throw new Exception(err_msg);
		}
		return dist;
	}

	public int getCardinality() {
		return numClusters;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public Vector<Cluster> getClusters() {
		return arrayClusters;
	}

	public Cluster getCluster(final int position) {
		return arrayClusters.elementAt(position);
	}

	public double[][] getMatrix() throws Exception {
		final double[][] matrix = new double[numClusters][numClusters];
		try {
			for (int i = 0; i < numClusters; i ++) {
				Cluster clusterI = arrayClusters.get(i);
				matrix[i][i] = getDistance(clusterI, clusterI);
				for (int j = i + 1; j < numClusters; j ++) {
					Cluster clusterJ = arrayClusters.get(j);
					matrix[i][j] = getDistance(clusterI, clusterJ);
					matrix[j][i] = matrix[i][j];
				}
			}
		} catch (Exception e) {
			String err_msg = e.getMessage() + "\n" + Language.getLabel(72);
			throw new Exception(err_msg);
		}
		return matrix;
	}

	public Cluster getRoot() {
		return this.getCluster(0);
	}

}
