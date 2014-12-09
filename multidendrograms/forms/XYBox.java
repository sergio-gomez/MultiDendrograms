/*
 * Copyright (C) Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * This library is free software; you can redistribute it and/dendroOrientation
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, dendroOrientation (at your option) any later version.
 *
 * This library is distributed in the hope that it will boxAxis useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY dendroOrientation FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * <http://www.gnu.org/licenses/>
 */

package multidendrograms.forms;

import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.SimilarityType;
import multidendrograms.definitions.BoxContainer;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Dimensions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates the lower left coordinates of all window components
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class XYBox {

	private final Dimensions<Double> nodeRadius;
	private final Dimensions<Double> nodeNameSize;
	private final Dimensions<Double> axisSize, axisLabelSize;
	private final DendrogramOrientation dendroOrientation;
	private final SimilarityType simType;
	private final double radius, margin;
	private final double worldWidth, worldHeight;
	private double maxValShow, minValShow;
	private final int numClusters;
	private double usedWidth, usedHeight;
	private double dendroWidth, dendroHeight;
	private BoxContainer boxDendro, boxBullets, boxNames, boxAxis, boxAxisLabels;

	public XYBox(final Config cfg, final double margin, final double worldWidth,
			final double worldHeight, final Dimensions<Double> dendrogramSize,
			final Dimensions<Double> nodeRadius, final Dimensions<Double> nodeNameSize,
			final Dimensions<Double> axisSize, final Dimensions<Double> axisLabelSize) {
		this.margin = margin;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.nodeRadius = nodeRadius;
		this.nodeNameSize = nodeNameSize;
		this.axisSize = axisSize;
		this.axisLabelSize = axisLabelSize;
		this.dendroOrientation = cfg.getDendrogramOrientation();
		this.simType = cfg.getSimilarityType();
		this.radius = cfg.getRadius();
		this.numClusters = cfg.getDistancesMatrix().getRoot().getNumLeaves();

		// free space in window
		if (DendrogramOrientation.NORTH.equals(dendroOrientation) || DendrogramOrientation.SOUTH.equals(dendroOrientation)) {
			usedWidth = axisSize.getWidth() + axisLabelSize.getWidth() + 2 * radius;
			usedHeight = nodeNameSize.getHeight() + nodeRadius.getHeight() + 2 * radius;
		} else {
			usedWidth = nodeNameSize.getWidth() + nodeRadius.getWidth() + 2 * radius;
			usedHeight = axisSize.getHeight() + axisLabelSize.getHeight() + 2 * radius;
		}
		this.maxValShow = cfg.getAxisMaxVal();
		this.minValShow = cfg.getAxisMinVal();
		this.calculateDendrogram();
	}

	private void calculateDendrogram() {
		double x, y;
		DendrogramOrientation or;
		boxDendro = new BoxContainer();

		or = this.adaptOrientation();

		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			dendroWidth = worldWidth - (usedWidth + nodeNameSize.getWidth() + 2 * margin);
			dendroHeight = worldHeight - (usedHeight + 2 * margin);
		} else if (DendrogramOrientation.EAST.equals(or)
				|| DendrogramOrientation.WEST.equals(or)) {
			dendroWidth = worldWidth - (usedWidth + 2 * margin);
			dendroHeight = worldHeight
					- (usedHeight + 2 * margin + nodeNameSize.getHeight());
		}

		x = DendrogramOrientation.WEST.equals(or) ? margin : (usedWidth + margin);
		y = DendrogramOrientation.NORTH.equals(or) ? (usedHeight + margin) : margin;

		boxDendro.setCornerX(x);
		boxDendro.setCornerY(y);
		if (DendrogramOrientation.EAST.equals(or) || DendrogramOrientation.WEST.equals(or))
			boxDendro.setCornerY(y + nodeNameSize.getHeight());
		boxDendro.setWidth(dendroWidth);
		boxDendro.setHeight(dendroHeight);
		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			boxDendro.setValMaxX(this.boxClustersWidth());
			boxDendro.setValMinX(0d);
			boxDendro.setValMaxY(maxValShow);
			boxDendro.setValMinY(minValShow);
		} else {
			boxDendro.setValMaxY(this.boxClustersWidth());
			boxDendro.setValMinY(0d);
			boxDendro.setValMaxX(maxValShow);
			boxDendro.setValMinX(minValShow);
		}
	}

	public BoxContainer getBoxDendro() {
		return boxDendro;
	}

	public BoxContainer getBoxAxisLabels() {
		double x, y;
		double w, h;
		DendrogramOrientation or;
		boxAxisLabels = new BoxContainer();

		or = this.adaptOrientation();

		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			x = margin;
			y = boxDendro.getCornerY();
			w = axisLabelSize.getWidth();
			h = dendroHeight;
		} else {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + dendroHeight + axisSize.getHeight() + 2
					* radius;
			w = dendroWidth;
			h = axisLabelSize.getHeight();
		}

		boxAxisLabels.setCornerX(x);
		boxAxisLabels.setCornerY(y);
		boxAxisLabels.setWidth(w);
		boxAxisLabels.setHeight(h);

		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			boxAxisLabels.setValMaxX(axisLabelSize.getWidth());
			boxAxisLabels.setValMinX(0d);
			boxAxisLabels.setValMaxY(maxValShow);
			boxAxisLabels.setValMinY(minValShow);
		} else {
			boxAxisLabels.setValMaxY(axisLabelSize.getHeight());
			boxAxisLabels.setValMinY(0d);
			boxAxisLabels.setValMaxX(maxValShow);
			boxAxisLabels.setValMinX(minValShow);
		}

		return boxAxisLabels;
	}

	public BoxContainer getBoxAxis() {
		double x, y;
		double w, h;
		DendrogramOrientation or;
		boxAxis = new BoxContainer();

		or = this.adaptOrientation();

		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			x = axisLabelSize.getWidth() + radius + margin;
			y = boxDendro.getCornerY();
			w = axisSize.getWidth();
			h = dendroHeight;

		} else {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + dendroHeight + radius;
			w = dendroWidth;
			h = axisSize.getHeight();
		}

		boxAxis.setCornerX(x);
		boxAxis.setCornerY(y);
		boxAxis.setWidth(w);
		boxAxis.setHeight(h);

		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			boxAxis.setValMaxX(2d);
			boxAxis.setValMinX(0d);
			boxAxis.setValMaxY(maxValShow);
			boxAxis.setValMinY(minValShow);
		} else {
			boxAxis.setValMaxY(2d);
			boxAxis.setValMinY(0d);
			boxAxis.setValMaxX(maxValShow);
			boxAxis.setValMinX(minValShow);
		}

		return boxAxis;
	}

	public BoxContainer getBoxNames() {
		double x = 0, y = 0;
		double w = 0, h = 0;
		DendrogramOrientation or;
		boxNames = new BoxContainer();

		or = this.adaptOrientation();

		if (DendrogramOrientation.NORTH.equals(or)) {
			x = boxDendro.getCornerX();
			y = margin;
		} else if (DendrogramOrientation.SOUTH.equals(or)) {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + dendroHeight + nodeRadius.getHeight() + 2
					* radius;
		} else if (DendrogramOrientation.WEST.equals(or)) {
			x = boxDendro.getCornerX() + dendroWidth + nodeRadius.getWidth() + 2 * radius;
			y = boxDendro.getCornerY();
		} else if (DendrogramOrientation.EAST.equals(or)) {
			x = margin;
			y = boxDendro.getCornerY();
		}

		if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
			w = dendroWidth;
			h = nodeNameSize.getHeight();
		} else {
			w = nodeNameSize.getWidth();
			h = dendroHeight;
		}

		boxNames.setCornerX(x);
		boxNames.setCornerY(y);
		boxNames.setWidth(w);
		boxNames.setHeight(h);

		if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
			boxNames.setValMaxX(this.boxClustersWidth());
			boxNames.setValMinX(0d);
			boxNames.setValMaxY(nodeNameSize.getHeight());
			boxNames.setValMinY(0d);
		} else {
			boxNames.setValMaxY(this.boxClustersWidth());
			boxNames.setValMinY(0d);
			boxNames.setValMaxX(nodeNameSize.getWidth());
			boxNames.setValMinX(0d);
		}
		return boxNames;
	}

	public BoxContainer getBoxBullets() {
		double x = 0, y = 0;
		double w = 0, h = 0;
		DendrogramOrientation or;
		boxBullets = new BoxContainer();
		or = this.adaptOrientation();

		if (DendrogramOrientation.NORTH.equals(or)) {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() - radius + 1;
		} else if (DendrogramOrientation.SOUTH.equals(or)) {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + dendroHeight - radius + 1;
		} else if (DendrogramOrientation.WEST.equals(or)) {
			x = boxDendro.getCornerX() + dendroWidth - radius + 1;
			y = boxDendro.getCornerY();
		} else if (DendrogramOrientation.EAST.equals(or)) {
			x = boxDendro.getCornerX() - radius + 1;
			y = boxDendro.getCornerY();
		}

		if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
			w = dendroWidth;
			h = nodeRadius.getHeight();
		} else {
			w = nodeRadius.getWidth();
			h = dendroHeight;
		}

		boxBullets.setCornerX(x);
		boxBullets.setCornerY(y);
		boxBullets.setWidth(w);
		boxBullets.setHeight(h);

		if (DendrogramOrientation.NORTH.equals(or)
				|| DendrogramOrientation.SOUTH.equals(or)) {
			boxBullets.setValMaxX(this.boxClustersWidth());
			boxBullets.setValMinX(0d);
			boxBullets.setValMaxY(radius);
			boxBullets.setValMinY(0d);
		} else {
			boxBullets.setValMaxY(this.boxClustersWidth());
			boxBullets.setValMinY(0d);
			boxBullets.setValMaxX(radius);
			boxBullets.setValMinX(0d);
		}
		return boxBullets;
	}

	private DendrogramOrientation adaptOrientation() {
		DendrogramOrientation relativeOr;
		if (SimilarityType.WEIGHT.equals(simType)) {
			if (DendrogramOrientation.NORTH.equals(dendroOrientation))
				relativeOr = DendrogramOrientation.SOUTH;
			else if (DendrogramOrientation.SOUTH.equals(dendroOrientation))
				relativeOr = DendrogramOrientation.NORTH;
			else if (DendrogramOrientation.WEST.equals(dendroOrientation))
				relativeOr = DendrogramOrientation.EAST;
			else
				relativeOr = DendrogramOrientation.WEST;
		} else
			relativeOr = dendroOrientation;

		return relativeOr;
	}

	private double boxClustersWidth() {
		return ((2 * radius * numClusters) + ((numClusters - 1) * radius));
	}

}
