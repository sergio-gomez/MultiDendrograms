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

package multidendrograms.dendrogram;

import java.util.LinkedList;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.dendrogram.figures.Band;
import multidendrograms.dendrogram.figures.Line;
import multidendrograms.dendrogram.figures.Node;
import multidendrograms.types.SimilarityType;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Coordinates;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Convert the dendrogram to geometric figures
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DendrogramPlot {

	private static SimilarityType simType;
	private final int precision;
	private final double radius;
	private final double axisMaxVal;
	private double posNodes = 0.0;
	private int next = 0;

	private final LinkedList<Node> nodesList = new LinkedList<Node>();
	private final LinkedList<Line> linesList = new LinkedList<Line>();
	private final LinkedList<Band> bandsList = new LinkedList<Band>();

	public DendrogramPlot(final Cluster tree, final Config cf) throws Exception {
		this.radius = cf.getRadius();
		this.precision = cf.getPrecision();
		this.axisMaxVal = cf.getAxisMaxVal();
		DendrogramPlot.simType = cf.getSimilarityType();
		if (DendrogramPlot.simType.equals(SimilarityType.DISTANCE)) {
			this.posNodes = 0.0;
		} else {
			this.posNodes = this.axisMaxVal;
		}
		branchNodeCoordinates(tree, cf.getConfigMenu().isBandVisible());
	}

	private Coordinates<Double> leafNodeCoordinates(final Cluster c) {
		double x;
		final Coordinates<Double> pos = new Coordinates<Double>(0.0, 0.0);
		this.next ++;
		x = this.radius * ((3 * this.next) - 1);
		pos.setX(x);
		pos.setY(this.posNodes);
		this.nodesList.add(new Node(pos, this.radius, this.precision, c.getName()));
		return pos;
	}

	private Coordinates<Double> branchNodeCoordinates(final Cluster c, final boolean band) throws Exception {
		Coordinates<Double> pos = new Coordinates<Double>(0.0, 0.0);

		if ((c.getNumSubclusters() == 1) && (c.getNumLeaves() == 1)) {
			pos = this.leafNodeCoordinates(c);
		} else {
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (int n = 0; n < c.getNumSubclusters(); n ++) {
				try {
					pos = this.branchNodeCoordinates(c.getSubcluster(n), band);
				} catch (Exception e) {
					String errMsg = Language.getLabel(64) + "\n" + e.getMessage();
					LogManager.LOG.throwing(errMsg, "Branca(final Cluster c)", e);
					throw new Exception(errMsg);
				}
				// Store line
				if (band) {
					this.linesList.add(new Line(pos, c.getHeight(), this.precision));
					LogManager.LOG.finer("new Line: (" + pos.getX() + ", " + pos.getY()
							+ ", " + c.getHeight() + ", " + this.precision + ")");
				} else {
					this.linesList.add(new Line(pos, c.getSummaryHeight(), this.precision));
					LogManager.LOG.finer("new Line: (" + pos.getX() + ", " + pos.getY()
							+ ", " + c.getSummaryHeight() + ", " + this.precision + ")");
				}
				min = min > pos.getX() ? pos.getX() : min;
				max = max < pos.getX() ? pos.getX() : max;
			}

			// Store band
			if (band) {
				this.bandsList.add(new Band(min, c.getHeight(), c.getAgglomeration(), (max - min), this.precision));
				LogManager.LOG.finer("Band: (" + min + ", " + c.getHeight() + ", " + c.getAgglomeration() + ", " + (max - min));
			} else {
				this.bandsList.add(new Band(min, c.getSummaryHeight(), 0, (max - min), this.precision));
				LogManager.LOG.finer("Band: (" + min + ", " + c.getSummaryHeight() + ", " + 0 + ", " + (max - min));
			}
			pos.setX((min + max) / 2);
			if (DendrogramPlot.simType.equals(SimilarityType.DISTANCE)) {
				if (band) {
					pos.setY(c.getHeight() + c.getAgglomeration());
				} else {
					pos.setY(c.getSummaryHeight());
				}
			} else {
				if (band) {
					pos.setY(c.getHeight() - c.getAgglomeration());
				} else {
					pos.setY(c.getSummaryHeight());
				}
			}
		}
		return pos;
	}

	public LinkedList<Node> getNodesList() {
		return nodesList;
	}

	public LinkedList<Line> getLinesList() {
		return linesList;
	}

	public LinkedList<Band> getBandsList() {
		return bandsList;
	}

}
