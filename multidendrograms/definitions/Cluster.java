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
	private String name = "";
	private double height = -1.0;
	private double agglomeration = 0.0;
	private double summaryHeight = -1.0;
	private double top = 0.0;
	private double base = 0.0;

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

	public void setHeight(final double ht) {
		this.height = ht;
		checkValues();
	}

	public double getHeight() {
		return this.height;
	}

	public void setAgglomeration(final double agglom) {
		this.agglomeration = agglom;
		checkValues();
	}

	private void checkValues() {
		this.base = this.height;
		if ((this.height + this.agglomeration) > this.top) {
			this.top = (this.height + this.agglomeration);
		}
		if ((this.height - this.agglomeration) < this.base) {
			this.base = (this.height - this.agglomeration);
		}
	}

	public double getAgglomeration() {
		return this.agglomeration;
	}
	
	public void setSummaryHeight(double value) {
		this.summaryHeight = value;
	}
	
	public double getSummaryHeight() {
		return this.summaryHeight;
	}
	
	public Double getTop() {
		return this.top;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getBase() {
		return this.base;
	}

	public void setSupercluster(final boolean b) {
		this.supercluster = b;
	}

	public boolean isSupercluster() {
		return this.supercluster;
	}

	public void addCluster(final Cluster c) throws Exception {
		try {
			if (c.getNumSubclusters() == 1) {
				this.leavesList.add(c);
			} else {
				this.leavesList.addAll(c.getLeaves());
			}
			this.subclustersList.addLast(c);
		} catch (Exception e) {
			throw new Exception(e.getMessage() + "\n" + Language.getLabel(74));
		}
		try {
			if (c.getTop() > this.top) {
				this.top = c.getTop();
			}
			if (c.getBase() < this.base) {
				this.base = c.getBase();
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

	public Cluster getSubcluster(final int pos) throws Exception {
		Cluster c;
		if (this.subclustersList.isEmpty() && (pos == 0)) {
			c = this;
		} else if (pos < this.subclustersList.size()) {
			try {
				c = this.subclustersList.get(pos);
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
