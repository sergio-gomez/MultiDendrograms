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

package multidendrograms.methods;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.Vector;

import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.DistancesMatrix;
import multidendrograms.initial.LogManager;
import multidendrograms.types.MethodName;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates one step of the hierarchical clustering
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class BuildDendrogram {

	private final DistancesMatrix distMatrix;

	private final SimilarityType simType;
	private final MethodName methodName;
	private final int precision;

	private int numElems = 0;
	private double minVal, maxVal;
	private Vector<Integer> groups;
	private int nextGroup = 0;

	private final Integer nullGroup = new Integer(0);

	public BuildDendrogram(final DistancesMatrix distances, final SimilarityType simType,
			final MethodName method, final int precision) {
		LogManager.LOG.info("DistancesMatrix" + distances);
		this.distMatrix = distances;
		this.distMatrix.setSimilarityType(simType);
		this.simType = simType;
		this.methodName = method;
		this.precision = precision;
		this.numElems = this.distMatrix.getCardinality();
		this.minVal = this.distMatrix.minValue();
		this.maxVal = this.distMatrix.maxValue();
		this.groups = new Vector<Integer>(this.numElems);
		for (int n = 0; n < this.numElems; n ++) {
			this.groups.add(n, this.nullGroup);
		}
	}

	public DistancesMatrix recalculate() throws Exception {
		groupClusters();
		LinkedHashMap<Integer, Cluster> lhm = createNewClusters();
		Vector<Cluster> clusters = updateInternalDistances(lhm);
		DistancesMatrix newDistMatrix = newDistancesMatrix(clusters);
		setRootBase(clusters, newDistMatrix.getRoot());
		return newDistMatrix;
	}

	private void groupClusters() throws Exception{
		// Put each cluster in a group.
		// If they have to be joined, they are put in the same group.
		final Vector<Cluster> clusters = this.distMatrix.getClusters();
		int numElements = this.distMatrix.getCardinality();
		for (int i = 0; i < numElements; i ++) {
			// Group of the cluster, if any, otherwise null
			Integer groupI = new Integer(this.groups.get(i));
			// Element "i" compared with all elements below it
			for (int j = i + 1; j < numElements; j ++) {
				Integer groupJ = new Integer(this.groups.get(j));
				double dist = this.distMatrix.getDistance(clusters.get(i), clusters.get(j));
				dist = MathUtils.round(dist, this.precision);
				if (isInRange(dist)) {
					// Merging groups at minimum distance
					if (groupI.equals(this.nullGroup)
							&& groupJ.equals(this.nullGroup)) {
						// If cluster does not have a group identifier, then new group id
						groupI = ++ this.nextGroup;
						this.groups.set(i, new Integer(groupI));
						this.groups.set(j, new Integer(groupI));
					} else if (groupI.equals(this.nullGroup)
							&& (!groupJ.equals(this.nullGroup))) {
						groupI = new Integer(groupJ);
						this.groups.set(i, new Integer(groupI));
					} else if ((!groupI.equals(this.nullGroup))
							&& groupJ.equals(this.nullGroup)) {
						this.groups.set(j, new Integer(groupI));
					} else {
						// Both clusters have group identifiers
						if (!groupI.equals(groupJ)) {
							// Join the two clusters in the same group
							for (int n = 0; n < this.numElems; n ++) {
								if (this.groups.get(n).equals(groupJ)) {
									this.groups.set(n, new Integer(groupI));
								}
							}
						}
					}
				}
			}
		}

		if (LogManager.LOG.isLoggable(Level.INFO)) {
			String str = "GROUPS\n";
			for (int n = 0; n < this.numElems; n ++) {
				str += "Id: " + clusters.get(n).getId() + "   --->   " + this.groups.get(n)
						+ "\n";
			}
			LogManager.LOG.info(str);
		}

	}

	private boolean isInRange(final double dist) {
		double min, max, val;
		boolean inRange;
		final double epsilon = 1.0 / Math.pow(10, this.precision + 1);

		if (this.simType.equals(SimilarityType.DISTANCE)) {
			min = MathUtils.round(this.minVal, this.precision);
			val = MathUtils.round(dist, this.precision);
			inRange = (Math.abs(val - min) < epsilon);
		} else {
			max = MathUtils.round(this.maxVal, this.precision);
			val = MathUtils.round(dist, this.precision);
			inRange = (Math.abs(max - val) < epsilon);
		}
		return inRange;
	}

	private LinkedHashMap<Integer, Cluster> createNewClusters() throws Exception {
		LinkedHashMap<Integer, Cluster> lhm = new LinkedHashMap<Integer, Cluster>();
		for (int i = 0; i < this.groups.size(); i ++) {
			Integer id = this.groups.get(i);
			Cluster ci = this.distMatrix.getCluster(i);
			if (id == this.nullGroup) {
				ci.setSupercluster(false);
				lhm.put(ci.hashCode(), ci);
			} else {
				// Add cluster to the corresponding supercluster
				if (lhm.containsKey(id)) {
					lhm.get(id).addCluster(ci);
				} else {
					Cluster newCluster = new Cluster();
					// Height of the grouping
					if (this.simType.equals(SimilarityType.DISTANCE)) {
						newCluster.setHeight(this.minVal);
					} else {
						newCluster.setHeight(this.maxVal);
					}
					newCluster.addCluster(ci);
					lhm.put(id, newCluster);
				}
			}
		}
		return lhm;
	}

	private Vector<Cluster> updateInternalDistances(final LinkedHashMap<Integer,
			Cluster> lhm) throws Exception {
		Vector<Cluster> clusters = new Vector<Cluster>(lhm.size());
		final Iterator<Cluster> iter = lhm.values().iterator();
		while (iter.hasNext()) {
			Cluster c = iter.next();
			if (c.isSupercluster()) {
				double minHeight = c.getHeight();
				c.setSummaryHeight(minHeight);
				int numSubclusters = c.getNumSubclusters();
				if (numSubclusters <= 2) {
					c.setAgglomeration(0.0);
				} else {
					double maxHeight;
					if (this.simType.equals(SimilarityType.DISTANCE)) {
						maxHeight = Double.MIN_VALUE;
					} else {
						maxHeight = Double.MAX_VALUE;
					}
					for (int i = 0; i < numSubclusters - 1; i ++) {
						Cluster subci = c.getSubcluster(i);
						for (int j = i + 1; j < numSubclusters; j ++) {
							Cluster subcj = c.getSubcluster(j);
							double dij = this.distMatrix.getDistance(subci, subcj);
							if (this.simType.equals(SimilarityType.DISTANCE)) {
								maxHeight = Math.max(maxHeight, dij);
							} else {
								maxHeight = Math.min(maxHeight, dij);
							}
						}
					}
					c.setAgglomeration(Math.abs(maxHeight - minHeight));
				}
			}
			clusters.add(c);
		}
		return clusters;
	}

	private DistancesMatrix newDistancesMatrix(final Vector<Cluster> clusters)
			throws Exception {
		final Method mt = getMethod();
		final int numClusters = clusters.size();
		DistancesMatrix newDistMatrix = new DistancesMatrix(numClusters, this.simType);
		if (numClusters == 1) {
			// Matrix with one element
			newDistMatrix.setDistance(clusters.get(0));
		} else {
			// (numClusters > 1)
			for (int i = 0; i < numClusters - 1; i ++) {
				Cluster ci = clusters.get(i);
				for (int j = i + 1; j < numClusters; j ++) {
					Cluster cj = clusters.get(j);
					// Calculations only if there is a new cluster or group
					double dist;
					if (ci.isSupercluster() || cj.isSupercluster()) {
						dist = mt.distance(ci, cj);
					} else {
						dist = this.distMatrix.getDistance(ci, cj);
					}
					newDistMatrix.setDistance(ci, cj, dist);
				}
			}
		}

		if (LogManager.LOG.getLevel().equals(Level.FINER)) {
			System.out.println("\nMatrix created.\n");
			for (int i = 0; i < numClusters - 1; i ++) {
				for (int j = i + 1; j < numClusters; j ++) {
					LogManager.LOG.finer("Distance between "
							+ newDistMatrix.getCluster(i).getId()
							+ " and "
							+ newDistMatrix.getCluster(j).getId()
							+ " = "
							+ newDistMatrix.getDistance(newDistMatrix.getCluster(i), newDistMatrix.getCluster(j)));
				}
			}
			System.out.println("\n");
		}

		return newDistMatrix;
	}

	private void setRootBase(final Vector<Cluster> clusters, final Cluster root) {
		double minBase = Double.MAX_VALUE;
		for (int i = 0; i < clusters.size(); i++) {
			double base = clusters.get(i).getBase();
			minBase = Math.min(minBase, base);
		}
		root.setBase(minBase);
	}

	private Method getMethod() {
		Method m;
		switch (this.methodName) {
		case SINGLE_LINKAGE:
			m = new SingleLinkage(this.distMatrix);
			break;
		case COMPLETE_LINKAGE:
			m = new CompleteLinkage(this.distMatrix);
			break;
		case UNWEIGHTED_AVERAGE:
			m = new UnweightedAverage(this.distMatrix);
			break;
		case WEIGHTED_AVERAGE:
			m = new WeightedAverage(this.distMatrix);
			break;
		case UNWEIGHTED_CENTROID:
			m = new UnweightedCentroid(this.distMatrix);
			break;
		case WEIGHTED_CENTROID:
			m = new WeightedCentroid(this.distMatrix);
			break;
		case WARD:
			m = new Ward(this.distMatrix);
			break;
		default:
			m = null;
			break;
		}
		return m;
	}

	public static void avoidReversals(final Cluster c, final double heightParent,
			final SimilarityType simType) throws Exception {
		int numSubclusters = c.getNumSubclusters();
		if (numSubclusters > 1) {
			double heightCurrent = c.getSummaryHeight();
			if (c.isSupercluster()) {
				if ((simType.equals(SimilarityType.DISTANCE)
						&& (heightCurrent > heightParent))
						|| (simType.equals(SimilarityType.WEIGHT)
						&& (heightCurrent < heightParent))) {
					heightCurrent = heightParent;
					c.setSummaryHeight(heightCurrent);
				}
			}
			for (int n = 0; n < numSubclusters; n ++) {
				avoidReversals(c.getSubcluster(n), heightCurrent, simType);
			}
		}
	}

}
