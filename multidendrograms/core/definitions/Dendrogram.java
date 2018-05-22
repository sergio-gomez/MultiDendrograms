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

import java.util.LinkedList;

/******************************************************************************
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dendrogram information
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 ******************************************************************************/
public class Dendrogram {

	// Variables that define the dendrogram root
	private int identifier;
	private String label;
	private double rootBottomHeight = Double.NaN;
	private double rootInternalHeight = Double.NaN;
	private double rootTopHeight = Double.NaN;

	// Parameters used to build the dendrogram
	public final boolean isDistanceBased;
	public final int precision;

	// Global minimum and maximum heights
	private double nodesMinHeight = Double.NaN;
	private double nodesMaxHeight = Double.NaN;
	private double bandsMinHeight = Double.NaN;
	private double bandsMaxHeight = Double.NaN;

	// To know if it is a supercluster
	private boolean isSupercluster = true;

	// Lists
	private LinkedList<Dendrogram> subclustersList;
	private LinkedList<Dendrogram> leavesList;

	public Dendrogram(int identifier, String label, boolean isDistanceBased, 
			int precision) {
		this.identifier = identifier;
		this.label = label;
		this.isDistanceBased = isDistanceBased;
		this.precision = precision;
		this.subclustersList = new LinkedList<Dendrogram>();
		this.leavesList = new LinkedList<Dendrogram>();
	}

	public int getIdentifier() {
		return this.identifier;
	}

	public String getLabel() {
		return this.label;
	}

	public void setRootHeights(final double height) {
		this.rootBottomHeight = height;
		this.rootInternalHeight = height;
		this.rootTopHeight = height;
	}

	public double getRootBottomHeight() {
		return this.rootBottomHeight;
	}

	public void setRootInternalHeight(final double rootInternalHeight) {
		this.rootInternalHeight = rootInternalHeight;
	}

	public double getRootInternalHeight() {
		return this.rootInternalHeight;
	}

	public void setRootTopHeight(final double rootTopHeight) {
		this.rootTopHeight = rootTopHeight;
		this.bandsMinHeight = Math.min(this.bandsMinHeight, rootTopHeight);
		this.bandsMaxHeight = Math.max(this.bandsMaxHeight, rootTopHeight);
	}

	public double getRootTopHeight() {
		return this.rootTopHeight;
	}

	public void setNodesHeights(final double height) {
		this.nodesMinHeight = height;
		this.nodesMaxHeight = height;
	}

	public void setBandsHeights(final double height) {
		this.bandsMinHeight = height;
		this.bandsMaxHeight = height;
	}

	public double getNodesMinHeight() {
		return this.nodesMinHeight;
	}

	public double getNodesMaxHeight() {
		return this.nodesMaxHeight;
	}

	public double getBandsMinHeight() {
		return this.bandsMinHeight;
	}

	public double getBandsMaxHeight() {
		return this.bandsMaxHeight;
	}

	public void setSupercluster(boolean isSupercluster) {
		this.isSupercluster = isSupercluster;
	}

	public boolean isSupercluster() {
		return this.isSupercluster;
	}

	public void addSubcluster(final Dendrogram subc) {
		this.subclustersList.addLast(subc);
		if (subc.numberOfSubclusters() == 1) {
			this.leavesList.add(subc);
		} else {
			this.leavesList.addAll(subc.leavesList);
		}
		if (Double.isNaN(this.nodesMinHeight)) {
			this.nodesMinHeight = subc.nodesMinHeight;
		} else if (!Double.isNaN(subc.nodesMinHeight)) {
			this.nodesMinHeight = 
					Math.min(this.nodesMinHeight, subc.nodesMinHeight);
		}
		if (Double.isNaN(this.nodesMaxHeight)) {
			this.nodesMaxHeight = subc.nodesMaxHeight;
		} else if (!Double.isNaN(subc.nodesMaxHeight)) {
			this.nodesMaxHeight = 
					Math.max(this.nodesMaxHeight, subc.nodesMaxHeight);
		}
		if (!Double.isNaN(subc.bandsMinHeight)) {
			this.bandsMinHeight = 
					Math.min(this.bandsMinHeight, subc.bandsMinHeight);
		}
		if (!Double.isNaN(subc.bandsMaxHeight)) {
			this.bandsMaxHeight = 
					Math.max(this.bandsMaxHeight, subc.bandsMaxHeight);
		}
	}

	public int numberOfSubclusters() {
		if (this.subclustersList.size() == 0) {
			return 1;
		} else {
			return this.subclustersList.size();
		}
	}

	public int numberOfSubroots() {
		if (this.isSupercluster) {
			return this.subclustersList.size();
		} else {
			return 1;
		}
	}

	public Dendrogram getSubcluster(int position) {
		Dendrogram c;
		if (this.subclustersList.isEmpty() && (position == 0)) {
			c = this;
		} else if (position < this.subclustersList.size()) {
			c = this.subclustersList.get(position);
		} else {
			c = null;
		}
		return c;
	}

	public Dendrogram getSubroot(int position) {
		Dendrogram c;
		if (this.isSupercluster) {
			c = this.subclustersList.get(position);
		} else if (position == 0) {
			c = this;
		} else {
			c = null;
		}
		return c;
	}

	public int numberOfLeaves() {
		if (this.leavesList.size() == 0) {
			return 1;
		} else {
			return this.leavesList.size();
		}
	}

	public Dendrogram getLeaf(int position) {
		Dendrogram c = null;
		if (this.leavesList.isEmpty() && (position == 0)) {
			c = this;
		} else if (position < this.leavesList.size()) {
			c = this.leavesList.get(position);
		}
		return c;
	}

	public double normalizedTreeBalance() {
		EntropyTuple et = accumulateEntropy();
		if (et.numJunctions == 0) {
			return 0.0;
		} else {
			double balance = et.sumEntropy / (double)et.numJunctions;
			double minBalance = minimumTreeBalance();
			return (balance - minBalance) / (1.0 - minBalance);
		}
	}

	private double minimumTreeBalance() {
		int numLeaves = numberOfLeaves();
		double balance = Math.log(numLeaves);
		for (int n = 2; n < numLeaves; n ++) {
			balance += Math.log(n) / (double)(n + 1);
		}
		return balance / ((double)(numLeaves - 1) * Math.log(2));
	}

	private class EntropyTuple {
		double sumEntropy = 0.0;
		int numJunctions = 0;
	}

	private EntropyTuple accumulateEntropy() {
		EntropyTuple et = new EntropyTuple();
		int numSubclusters = numberOfSubclusters();
		if (numSubclusters > 1) {
			et.sumEntropy = rootEntropy();
			et.numJunctions = 1;
			for (int i = 0; i < numSubclusters; i ++) {
				Dendrogram cI = this.subclustersList.get(i);
				EntropyTuple etI = cI.accumulateEntropy();
				et.sumEntropy += etI.sumEntropy;
				et.numJunctions += etI.numJunctions;
			}
		}
		return et;
	}

	private double rootEntropy() {
		double h = 0.0;
		int numSubclusters = numberOfSubclusters();
		if (numSubclusters > 1) {
			int numLeaves = numberOfLeaves();
			for (int i = 0; i < numSubclusters; i ++) {
				Dendrogram cI = this.subclustersList.get(i);
				double p = (double)cI.numberOfLeaves() / (double)numLeaves;
				h -= p * Math.log(p);
			}
			h /= Math.log(numSubclusters);
		}
		return h;
	}

}
