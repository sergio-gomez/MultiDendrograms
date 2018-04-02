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
import multidendrograms.types.OriginType;
import multidendrograms.types.ProximityType;
import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.utils.MathUtils;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Coordinates;
import multidendrograms.definitions.SettingsInfo;

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

	private static ProximityType proximityType;
	private final int precision;
	private final double radius;
	private final boolean bandVisible;
	private final OriginType originType;
	private final double dendroBottomHeight;
	private int next = 0;

	private final LinkedList<Node> nodesList = new LinkedList<Node>();
	private final LinkedList<Line> linesList = new LinkedList<Line>();
	private final LinkedList<Band> bandsList = new LinkedList<Band>();

	public DendrogramPlot(final Dendrogram tree, final Config cfg) throws Exception {
		DendrogramPlot.proximityType = cfg.getProximityType();
		this.precision = cfg.getPrecision();
		this.radius = cfg.getRadius();
		SettingsInfo settings = cfg.getSettingsInfo();
		this.bandVisible = settings.isBandVisible();
		this.originType = settings.getOriginType();
		this.dendroBottomHeight = DendrogramPlot.proximityType.equals(ProximityType.DISTANCE) ?
				cfg.getAxisMinValue() : cfg.getAxisMaxValue();
		branchNodeCoordinates(tree);
	}

	private Coordinates<Double> branchNodeCoordinates(final Dendrogram cluster) throws Exception {
		Coordinates<Double> position = new Coordinates<Double>(0.0, 0.0);
		double rootBottomHeight = cluster.getRootBottomHeight();
		double rootTopHeight = cluster.getRootTopHeight();
		int numSubclusters = cluster.numberOfSubclusters();
		if (numSubclusters == 1) {
			this.next ++;
			double x = this.radius * ((3 * this.next) - 1);
			position.setX(x);
			if (Double.isNaN(rootBottomHeight) || this.originType.equals(OriginType.UNIFORM_ORIGIN)) {
				position.setY(this.dendroBottomHeight);
			} else {
				double y;
				if (DendrogramPlot.proximityType.equals(ProximityType.DISTANCE)) {
					y = Math.max(rootBottomHeight, this.dendroBottomHeight);
				} else {// (DendrogramPlot.proximityType.equals(ProximityType.SIMILARITY))
					y = Math.min(rootBottomHeight, this.dendroBottomHeight);
				}
				position.setY(y);
			}
			this.nodesList.add(new Node(position.getX(), position.getY(), this.radius, cluster.getLabel()));
		} else {// (numSubclusters > 1)
			rootTopHeight = MathUtils.round(rootTopHeight, precision);
			rootBottomHeight = MathUtils.round(rootBottomHeight, precision);
			double rootIncrHeight = this.bandVisible ? Math.abs(rootTopHeight - rootBottomHeight) : 0.0;
			rootIncrHeight = MathUtils.round(rootIncrHeight, precision);
			double xMin = Double.MAX_VALUE;
			double xMax = Double.MIN_VALUE;
			for (int n = 0; n < numSubclusters; n ++) {
				try {
					position = branchNodeCoordinates(cluster.getSubcluster(n));
				} catch (Exception e) {
					String errMsg = Language.getLabel(64) + "\n" + e.getMessage();
					LogManager.LOG.throwing(errMsg, "Branch(final Cluster c)", e);
					throw new Exception(errMsg);
				}
				double x = position.getX();
				double y = MathUtils.round(position.getY(), precision);
				xMin = Math.min(xMin, x);
				xMax = Math.max(xMax, x);
				// Store line
				this.linesList.add(new Line(x, y, rootBottomHeight));
				LogManager.LOG.finer("new Line: (" + x + ", " + y + ", " + rootBottomHeight + ", " + this.precision + ")");
			}
			position.setX((xMin + xMax) / 2.0);
			if (this.bandVisible) {
				position.setY(rootTopHeight);
			} else {
				position.setY(rootBottomHeight);
			}
			// Store band
			this.bandsList.add(new Band(xMin, rootBottomHeight, rootIncrHeight, xMax - xMin));
			LogManager.LOG.finer("Band: (" + xMin + ", " + rootBottomHeight + ", " + rootIncrHeight + ", " + (xMax - xMin));
		}
		return position;
	}

	public LinkedList<Node> getNodesList() {
		return this.nodesList;
	}

	public LinkedList<Line> getLinesList() {
		return this.linesList;
	}

	public LinkedList<Band> getBandsList() {
		return this.bandsList;
	}

}
