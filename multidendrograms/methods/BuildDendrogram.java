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

	private int numClusters;
	private double groupingHeight;
	private Vector<Integer> groups;
	private int nextGroup = 0;

	private final Integer nullGroup = new Integer(0);

	public BuildDendrogram(final DistancesMatrix distMatrix, final SimilarityType simType,
			final MethodName methodName, final int precision) {
		LogManager.LOG.info("DistancesMatrix" + distMatrix);
		this.distMatrix = distMatrix;
		this.simType = simType;
		this.methodName = methodName;
		this.precision = precision;
		this.numClusters = this.distMatrix.getCardinality();
		this.groupingHeight = simType.equals(SimilarityType.DISTANCE) ? 
				distMatrix.getMinValue() : distMatrix.getMaxValue();
		this.groups = new Vector<Integer>(this.numClusters);
		for (int n = 0; n < this.numClusters; n ++) {
			this.groups.add(n, this.nullGroup);
		}
	}

	public DistancesMatrix recalculate() throws Exception {
		groupClusters();
		LinkedHashMap<Integer, Cluster> lhm = createNewClusters();
		Vector<Cluster> clusters = updateInternalDistances(lhm);
		DistancesMatrix newDistMatrix = newDistancesMatrix(clusters);
		return newDistMatrix;
	}

	private void groupClusters() throws Exception{
		// Put each cluster in a group.
		// If they have to be joined, they are put in the same group.
		final Vector<Cluster> clusters = this.distMatrix.getClusters();
		for (int i = 0; i < this.numClusters - 1; i ++) {
			Cluster clusterI = clusters.get(i);
			Integer groupI = this.groups.get(i);
			// Element "i" compared with all elements below it
			for (int j = i + 1; j < this.numClusters; j ++) {
				Cluster clusterJ = clusters.get(j);
				Integer groupJ = this.groups.get(j);
				double dist = this.distMatrix.getDistance(clusterI, clusterJ);
				if (isInRange(dist)) {
					// Merge groups at minimum distance (or maximum similarity)
					if (groupI.equals(this.nullGroup)
							&& groupJ.equals(this.nullGroup)) {
						// If both clusters do not have any group identifier, then new group id
						groupI = ++ this.nextGroup;
						this.groups.set(i, new Integer(groupI));
						this.groups.set(j, new Integer(groupI));
					} else if (groupI.equals(this.nullGroup)
							&& (!groupJ.equals(this.nullGroup))) {
						this.groups.set(i, new Integer(groupJ));
					} else if ((!groupI.equals(this.nullGroup))
							&& groupJ.equals(this.nullGroup)) {
						this.groups.set(j, new Integer(groupI));
					} else {
						// Both clusters have group identifiers
						if (!groupI.equals(groupJ)) {
							// Join the two clusters in the same group
							for (int k = 0; k < this.numClusters; k ++) {
								Integer groupK = this.groups.get(k); 
								if (groupK.equals(groupJ)) {
									this.groups.set(k, new Integer(groupI));
								}
							}
						}
					}
				}
			}
		}

		if (LogManager.LOG.isLoggable(Level.INFO)) {
			String str = "GROUPS\n";
			for (int n = 0; n < this.numClusters; n ++) {
				str += "Id: " + clusters.get(n).getId() + "   --->   " + this.groups.get(n) + "\n";
			}
			LogManager.LOG.info(str);
		}

	}

	private boolean isInRange(final double dist) {
		final double epsilon = 1.0 / Math.pow(10, this.precision + 1);
		double value = MathUtils.round(dist, this.precision);
		double bound = MathUtils.round(this.groupingHeight, this.precision);
		boolean inRange = (Math.abs(value - bound) < epsilon);
		return inRange;
	}

	private LinkedHashMap<Integer, Cluster> createNewClusters() throws Exception {
		LinkedHashMap<Integer, Cluster> lhm = new LinkedHashMap<Integer, Cluster>();
		for (int n = 0; n < this.groups.size(); n ++) {
			Cluster cluster = this.distMatrix.getCluster(n);
			Integer group = this.groups.get(n);
			if (group.equals(this.nullGroup)) {
				cluster.setSupercluster(false);
				lhm.put(cluster.hashCode(), cluster);
			} else {
				// Add cluster to the corresponding supercluster
				if (lhm.containsKey(group)) {
					Cluster supercluster = lhm.get(group);
					supercluster.addSubcluster(cluster);
				} else {
					Cluster newCluster = new Cluster();
					newCluster.setRootHeights(this.groupingHeight);
					newCluster.setBandsHeights(this.groupingHeight);
					newCluster.addSubcluster(cluster);
					lhm.put(group, newCluster);
				}
			}
		}
		return lhm;
	}

	private Vector<Cluster> updateInternalDistances(final LinkedHashMap<Integer,Cluster> lhm) throws Exception {
		Vector<Cluster> clusters = new Vector<Cluster>(lhm.size());
		final Iterator<Cluster> iter = lhm.values().iterator();
		while (iter.hasNext()) {
			Cluster cluster = iter.next();
			if (cluster.isSupercluster()) {
				int numSubclusters = cluster.getNumSubclusters();
				if (numSubclusters > 2) {
					double rootTopHeight = this.simType.equals(SimilarityType.DISTANCE) ? 
							Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
					for (int i = 0; i < numSubclusters - 1; i ++) {
						Cluster subcI = cluster.getSubcluster(i);
						for (int j = i + 1; j < numSubclusters; j ++) {
							Cluster subcJ = cluster.getSubcluster(j);
							double dij = this.distMatrix.getDistance(subcI, subcJ);
							if (this.simType.equals(SimilarityType.DISTANCE)) {
								rootTopHeight = Math.max(rootTopHeight, dij);
							} else {
								rootTopHeight = Math.min(rootTopHeight, dij);
							}
						}
					}
					cluster.setRootTopHeight(rootTopHeight);
				}
			}
			clusters.add(cluster);
		}
		return clusters;
	}

	private DistancesMatrix newDistancesMatrix(final Vector<Cluster> clusters)
			throws Exception {
		final Method method = getMethod();
		final int numClusters = clusters.size();
		DistancesMatrix newDistMatrix = new DistancesMatrix(numClusters);
		for (int i = 0; i < numClusters; i ++) {
			Cluster clusterI = clusters.get(i);
			double dist = clusterI.getRootBottomHeight();
			newDistMatrix.setDistance(clusterI, clusterI, dist);
			for (int j = i + 1; j < numClusters; j ++) {
				Cluster clusterJ = clusters.get(j);
				// Calculations only if there is a new cluster or group
				if (clusterI.isSupercluster() || clusterJ.isSupercluster()) {
					dist = method.distance(clusterI, clusterJ);
				} else {
					dist = this.distMatrix.getDistance(clusterI, clusterJ);
				}
				newDistMatrix.setDistance(clusterI, clusterJ, dist);
			}
		}

		if (LogManager.LOG.getLevel().equals(Level.FINER)) {
			System.out.println("\nMatrix created.\n");
			for (int i = 0; i < numClusters - 1; i ++) {
				Cluster clusterI = newDistMatrix.getCluster(i);
				for (int j = i + 1; j < numClusters; j ++) {
					Cluster clusterJ = newDistMatrix.getCluster(j);
					LogManager.LOG.finer("Distance between "
							+ clusterI.getId() + " and " + clusterJ.getId() + " = "
							+ newDistMatrix.getDistance(clusterI, clusterJ));
				}
			}
			System.out.println("\n");
		}

		return newDistMatrix;
	}

	private Method getMethod() {
		Method m;
		switch (this.methodName) {
		case SINGLE_LINKAGE:
			m = new SingleLinkage(this.distMatrix, this.simType);
			break;
		case COMPLETE_LINKAGE:
			m = new CompleteLinkage(this.distMatrix, this.simType);
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

}
