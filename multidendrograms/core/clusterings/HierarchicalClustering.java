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

package multidendrograms.core.clusterings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.definitions.SymmetricMatrix;
import multidendrograms.core.utils.MathUtils;

/******************************************************************************
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Abstract class for agglomerative hierarchical clustering
 *
 * @author Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres
 *
 * @since JDK 6.0
 ******************************************************************************/
public abstract class HierarchicalClustering {

	protected boolean isDistanceBased;
	private int precision;

	private int nextClusterId = 1;
	private Dendrogram[] roots;
	protected HashMap<Integer, Integer> rootsToIndexes;
	private SymmetricMatrix rootsMatrix;

	private static final int NULL_GROUP = 0;

	public HierarchicalClustering(SymmetricMatrix proximityMatrix, 
			String[] labels, boolean isDistanceBased, int precision) {
		this.isDistanceBased = isDistanceBased;
		this.precision = precision;
		// Initialize roots
		int numElements = proximityMatrix.numberOfRows();
		this.roots = new Dendrogram[numElements];
		for (int n = 0; n < numElements; n ++) {
			Dendrogram root = new Dendrogram(this.nextClusterId, labels[n], 
					isDistanceBased, precision);
			this.nextClusterId ++;
			double proximity = proximityMatrix.getElement(n, n);
			if (!Double.isNaN(proximity)) {
				root.setRootHeights(proximity);
				root.setNodesHeights(proximity);
			}
			this.roots[n] = root;
		}
		mapRootsToIndexes();
		this.rootsMatrix = proximityMatrix;
	}

	public void build() {
		while (numberOfRoots() > 1) {
			iteration();
		}
	}

	public void iteration() {
		int[] groups = groupRoots();
		Dendrogram[] newRoots = createNewRoots(groups);
		updateInternalProximities(newRoots);
		this.rootsMatrix = newProximityMatrix(groups, newRoots);
		this.roots = newRoots;
		mapRootsToIndexes();
	}

	public int numberOfRoots() {
		return this.roots.length;
	}

	public Dendrogram getRoot() {
		return this.roots[0];
	}

	private int[] groupRoots() {
		// Initialize groups
		int numRoots = this.roots.length;
		int[] groups = new int[numRoots];
		Arrays.fill(groups, NULL_GROUP);
		// Put each root in a group.
		// If they have to be merged, they are put in the same group.
		int nextGroupId = 1;
		for (int i = 0; i < numRoots - 1; i ++) {
			for (int j = i + 1; j < numRoots; j ++) {
				double proximity = this.rootsMatrix.getElement(i, j);
				if (isInRange(proximity)) {
					// Merge groups at minimum distance (or maximum similarity)
					if ((groups[i] == NULL_GROUP) && 
						(groups[j] == NULL_GROUP)) {
						// If both roots do not have any group identifier, 
						// then new group identifier
						groups[i] = nextGroupId;
						groups[j] = nextGroupId;
						nextGroupId ++;
					} else if ((groups[i] == NULL_GROUP) && 
							   (groups[j] != NULL_GROUP)) {
						groups[i] = groups[j];
					} else if ((groups[i] != NULL_GROUP) && 
							   (groups[j] == NULL_GROUP)) {
						groups[j] = groups[i];
					} else {
						// Both roots have group identifiers
						if (groups[i] != groups[j]) {
							// Merge the two roots in the same group
							int minGroupId = Math.min(groups[i], groups[j]);
							int maxGroupId = Math.max(groups[i], groups[j]);
							for (int k = 0; k < numRoots; k ++) {
								if (groups[k] == maxGroupId) {
									groups[k] = minGroupId;
								}
							}
						}
					}
				}
			}
		}
		return groups;
	}

	private boolean isInRange(double proximity) {
		final double epsilon = 1.0 / Math.pow(10, this.precision + 1);
		double value = MathUtils.round(proximity, this.precision);
		double bound = MathUtils.round(groupingProximity(), this.precision);
		return (Math.abs(value - bound) < epsilon);
	}

	private Dendrogram[] createNewRoots(int[] groups) {
		LinkedHashMap<Integer, Dendrogram> groupsToRoots = 
				new LinkedHashMap<Integer, Dendrogram>();
		for (int n = 0; n < groups.length; n ++) {
			Dendrogram cluster = this.roots[n];
			if (groups[n] == NULL_GROUP) {
				cluster.setSupercluster(false);
				groupsToRoots.put(-cluster.getIdentifier(), cluster);
			} else {
				// Add cluster to the corresponding supercluster
				if (groupsToRoots.containsKey(groups[n])) {
					Dendrogram supercluster = groupsToRoots.get(groups[n]);
					supercluster.addSubcluster(cluster);
				} else {
					String label = Integer.toString(this.nextClusterId);
					Dendrogram newSupercluster = new Dendrogram(
							this.nextClusterId, label, this.isDistanceBased, 
							this.precision);
					this.nextClusterId ++;
					newSupercluster.setRootHeights(groupingProximity());
					newSupercluster.setBandsHeights(groupingProximity());
					newSupercluster.addSubcluster(cluster);
					groupsToRoots.put(groups[n], newSupercluster);
				}
			}
		}
		Dendrogram[] newRoots = new Dendrogram[groupsToRoots.size()];
		int n = 0;
		Iterator<Dendrogram> iter = groupsToRoots.values().iterator();
		while (iter.hasNext()) {
			newRoots[n] = iter.next();
			n ++;
		}
		return newRoots;
	}

	private double groupingProximity() {
		return this.isDistanceBased ? this.rootsMatrix.minimumValue() : 
									  this.rootsMatrix.maximumValue();
	}

	private void updateInternalProximities(Dendrogram[] newRoots) {
		for (int n = 0; n < newRoots.length; n ++) {
			Dendrogram root = newRoots[n];
			int numSubroots = root.numberOfSubroots();
			if (numSubroots > 2) {
				double rootTopHeight = this.isDistanceBased ? 
						Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
				for (int i = 0; i < numSubroots - 1; i ++) {
					Dendrogram subrI = root.getSubroot(i);
					for (int j = i + 1; j < numSubroots; j ++) {
						Dendrogram subrJ = root.getSubroot(j);
						double proximity = rootsProximity(subrI, subrJ);
						rootTopHeight = this.isDistanceBased ? 
							Math.max(rootTopHeight, proximity) : 
							Math.min(rootTopHeight, proximity);
					}
				}
				root.setRootTopHeight(rootTopHeight);
				double rootInternalHeight = calculateInternalProximity(root);
				root.setRootInternalHeight(rootInternalHeight);
			}
		}
	}

	private SymmetricMatrix newProximityMatrix(int[] groups, 
			Dendrogram[] newRoots) {
		int numRoots = newRoots.length;
		SymmetricMatrix newMatrix = new SymmetricMatrix(numRoots);
		for (int i = 0; i < numRoots; i ++) {
			Dendrogram rootI = newRoots[i];
			double proximity = rootI.getRootBottomHeight();
			newMatrix.setElement(i, i, proximity);
			for (int j = i + 1; j < numRoots; j ++) {
				Dendrogram rootJ = newRoots[j];
				// Calculations only if there is a new cluster
				if (rootI.isSupercluster() || rootJ.isSupercluster()) {
					proximity = calculateProximity(rootI, rootJ);
				} else {
					proximity = rootsProximity(rootI, rootJ);
				}
				newMatrix.setElement(i, j, proximity);
			}
		}
		return newMatrix;
	}

	private void mapRootsToIndexes() {
		this.rootsToIndexes = new HashMap<Integer, Integer>();
		for (int n = 0; n < this.roots.length; n ++) {
			Dendrogram root = this.roots[n];
			this.rootsToIndexes.put(root.getIdentifier(), n);
		}
	}

	protected double rootsProximity(Dendrogram root1, Dendrogram root2) {
		int index1 = this.rootsToIndexes.get(root1.getIdentifier());
		int index2 = this.rootsToIndexes.get(root2.getIdentifier());
		return this.rootsMatrix.getElement(index1, index2);
	}

	protected abstract double calculateProximity(Dendrogram cI, Dendrogram cJ);

	protected double minimumProximity(Dendrogram cI, Dendrogram cJ) {
		double minimum = Double.POSITIVE_INFINITY;
		for (int i = 0; i < cI.numberOfSubroots(); i ++) {
			Dendrogram subcI = cI.getSubroot(i);
			for (int j = 0; j < cJ.numberOfSubroots(); j ++) {
				Dendrogram subcJ = cJ.getSubroot(j);
				double proximity = rootsProximity(subcI, subcJ);
				minimum = Math.min(minimum, proximity);
			}
		}
		return minimum;
	}

	protected double maximumProximity(Dendrogram cI, Dendrogram cJ) {
		double maximum = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < cI.numberOfSubroots(); i ++) {
			Dendrogram subcI = cI.getSubroot(i);
			for (int j = 0; j < cJ.numberOfSubroots(); j ++) {
				Dendrogram subcJ = cJ.getSubroot(j);
				double proximity = rootsProximity(subcI, subcJ);
				maximum = Math.max(maximum, proximity);
			}
		}
		return maximum;
	}

	protected double calculateInternalProximity(Dendrogram c) {
		return c.getRootBottomHeight();
	}

	protected double minimumInternalProximity(Dendrogram c) {
		double minimum = Double.POSITIVE_INFINITY;
		int numSubroots = c.numberOfSubroots();
		for (int i = 0; i < numSubroots - 1; i ++) {
			Dendrogram subcI = c.getSubroot(i);
			for (int j = i + 1; j < numSubroots; j ++) {
				Dendrogram subcJ = c.getSubroot(j);
				double proximity = rootsProximity(subcI, subcJ);
				minimum = Math.min(minimum, proximity);
			}
		}
		return minimum;
	}

	protected double maximumInternalProximity(Dendrogram c) {
		double maximum = Double.NEGATIVE_INFINITY;
		int numSubroots = c.numberOfSubroots();
		for (int i = 0; i < numSubroots - 1; i ++) {
			Dendrogram subcI = c.getSubroot(i);
			for (int j = i + 1; j < numSubroots; j ++) {
				Dendrogram subcJ = c.getSubroot(j);
				double proximity = rootsProximity(subcI, subcJ);
				maximum = Math.max(maximum, proximity);
			}
		}
		return maximum;
	}

}
