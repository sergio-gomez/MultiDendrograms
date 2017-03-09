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

import java.util.LinkedList;

import multidendrograms.initial.Language;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Cluster information
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Cluster {

	// Incremented for each cluster to ensure that there are no repetitions
	private static int lastId = 0;

	// Variables that define the cluster characteristics
	private Integer id;
	private String name;
	private double rootBottomHeight = Double.NaN;
	private double rootTopHeight = Double.NaN;

	// Global minimum and maximum heights
	private double nodesMinHeight = Double.NaN;
	private double nodesMaxHeight = Double.NaN;
	private double bandsMinHeight = Double.NaN;
	private double bandsMaxHeight = Double.NaN;

	// To know if it is a supercluster
	private boolean supercluster = true;

	// Lists
	private LinkedList<Cluster> subclustersList;
	private LinkedList<Cluster> leavesList;

	public Cluster() {
		this.id = ++ Cluster.lastId;
		this.name = Integer.toString(this.id);
		this.subclustersList = new LinkedList<Cluster>();
		this.leavesList = new LinkedList<Cluster>();
	}

	public static void resetId() {
		Cluster.lastId = 0;
	}

	public Integer getId() {
		return this.id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setRootHeights(final double height) {
		this.rootBottomHeight = height;
		this.rootTopHeight = height;
	}

	public double getRootBottomHeight() {
		return this.rootBottomHeight;
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

	public void setSupercluster(final boolean supercluster) {
		this.supercluster = supercluster;
	}

	public boolean isSupercluster() {
		return this.supercluster;
	}

	public void addSubcluster(final Cluster subc) throws Exception {
		try {
			this.subclustersList.addLast(subc);
			if (subc.getNumSubclusters() == 1) {
				this.leavesList.add(subc);
			} else {
				this.leavesList.addAll(subc.getLeaves());
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage() + "\n" + Language.getLabel(74));
		}
		try {
			if (Double.isNaN(this.nodesMinHeight)) {
				this.nodesMinHeight = subc.nodesMinHeight;
			} else if (!Double.isNaN(subc.nodesMinHeight)) {
				this.nodesMinHeight = Math.min(this.nodesMinHeight, subc.nodesMinHeight);
			}
			if (Double.isNaN(this.nodesMaxHeight)) {
				this.nodesMaxHeight = subc.nodesMaxHeight;
			} else if (!Double.isNaN(subc.nodesMaxHeight)) {
				this.nodesMaxHeight = Math.max(this.nodesMaxHeight, subc.nodesMaxHeight);
			}
			if (!Double.isNaN(subc.bandsMinHeight)) {
				this.bandsMinHeight = Math.min(this.bandsMinHeight, subc.bandsMinHeight);
			}
			if (!Double.isNaN(subc.bandsMaxHeight)) {
				this.bandsMaxHeight = Math.max(this.bandsMaxHeight, subc.bandsMaxHeight);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage() + "\n" + Language.getLabel(75));
		}
	}

	public int getNumSubclusters() {
		if (this.subclustersList.size() == 0) {
			return 1;
		} else {
			return this.subclustersList.size();
		}
	}

	public Cluster getSubcluster(final int position) throws Exception {
		Cluster c;
		if (this.subclustersList.isEmpty() && (position == 0)) {
			c = this;
		} else if (position < this.subclustersList.size()) {
			try {
				c = this.subclustersList.get(position);
			} catch (Exception e) {
				throw new Exception(Language.getLabel(18));
			}
		} else {
			c = null;
			throw new Exception(Language.getLabel(17));
		}
		return c;
	}

	public LinkedList<Cluster> getLeaves() {
		return this.leavesList;
	}

	public int getNumLeaves() {
		if (this.leavesList.size() == 0) {
			return 1;
		} else {
			return this.leavesList.size();
		}
	}

}
