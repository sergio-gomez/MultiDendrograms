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

package multidendrograms.dendrogram.eps;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import multidendrograms.initial.InitialProperties;
import multidendrograms.dendrogram.Clipping;
import multidendrograms.dendrogram.ScalingBox;
import multidendrograms.dendrogram.eps.figures.AxisEps;
import multidendrograms.dendrogram.eps.figures.AxisLabelEps;
import multidendrograms.dendrogram.eps.figures.BandEps;
import multidendrograms.dendrogram.eps.figures.LineEps;
import multidendrograms.dendrogram.eps.figures.NodeEps;
import multidendrograms.dendrogram.eps.figures.NodeLabelEps;
import multidendrograms.dendrogram.figures.Band;
import multidendrograms.dendrogram.figures.Line;
import multidendrograms.dendrogram.figures.Node;
import multidendrograms.forms.XYBox;
import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.utils.TextBoxSize;
import multidendrograms.definitions.BoxContainer;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Dimensions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculation of the EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EpsPlot {

	private ScalingBox scaledDendrogram = null;
	private ScalingBox scaledBullets = null;
	private ScalingBox scaledAxis = null;
	private ScalingBox scaledNames = null;
	private ScalingBox scaledAxisLabels = null;

	// axis labels
	double axisLabelWidth = 0.0;
	double axisLabelHeight = 0.0;

	// node labels
	double nameWidth = 0.0;
	double nameHeight = 0.0;

	// node bullet size
	double bulletsWidth = 0.0;
	double bulletsHeight = 0.0;

	// axis
	double axisWidth = 0.0;
	double axisHeight = 0.0;

	// dendrogram size
	double dendroWidth = 0.0;
	double dendroHeight = 0.0;

	// clipping of dendrogram
	private double axisMaxVal;
	private double axisMinVal;

	private Config cfg = null;

	private int numClusters;
	private double radius;

	private DendrogramOrientation dendroOrientation = DendrogramOrientation.NORTH;
	private LabelOrientation nodeNameOrientation = LabelOrientation.VERTICAL;
	private String strMax = "";

	private LinkedList<Node> nodesList;
	private LinkedList<Line> linesList;
	private LinkedList<Band> bandsList;

	private DendrogramPanel dendroPanel;
	private final int xmax, ymax;

	public EpsPlot(DendrogramPanel dendroPanel, Config cfg, int xmax, int ymax) {
		this.dendroPanel = dendroPanel;
		this.nodesList = dendroPanel.getNodesList();
		this.linesList = dendroPanel.getLinesList();
		this.bandsList = dendroPanel.getBandsList();
		this.xmax = xmax;
		this.ymax = ymax;
		setConfig(cfg);
	}

	public void setConfig(final Config cfg) {
		this.cfg = cfg;

		radius = cfg.getRadius();
		numClusters = cfg.getDistancesMatrix().getRoot().getNumLeaves();
		dendroOrientation = cfg.getDendrogramOrientation();
		nodeNameOrientation = cfg.getNodeNameOrientation();

		if (cfg.getAxisMaxVal() > 0.0)
			axisMaxVal = cfg.getAxisMaxVal();
		else
			axisMaxVal = cfg.getTop();

		if (cfg.getAxisMinVal() > 0.0)
			axisMinVal = cfg.getAxisMinVal();
		else {
			if (cfg.isDistance())
				axisMinVal = 0.0;
			else
				axisMinVal = cfg.getAxisMinVal();
		}
	}

	public DendrogramPanel getDendrogramPanel() {
		return this.dendroPanel;
	}

	public void setDendrogramPanel(DendrogramPanel dendroPanel) {
		this.dendroPanel = dendroPanel;
	}

	private double boxClustersWidth() {
		return ((2 * radius * numClusters) + ((numClusters - 1) * radius));
	}

	private void translateScreen(final BoxContainer b, final double h_mon) {
		double h;
		h = h_mon - b.getCornerY();
		b.setCornerY(-h);
	}

	private void setWidths() {
		final DendrogramOrientation or = cfg.getDendrogramOrientation();

		if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
			dendroWidth = this.boxClustersWidth();
			dendroHeight = axisMaxVal - axisMinVal;
		} else {
			dendroWidth = axisMaxVal - axisMinVal;
			dendroHeight = this.boxClustersWidth();
		}

		if (cfg.getConfigMenu().isAxisVisible()) {

			if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
				axisWidth = 2 * radius;
				axisHeight = axisMaxVal - axisMinVal;
			} else {
				axisHeight = 2 * radius;
				axisWidth = axisMaxVal - axisMinVal;
			}
		} else {
			axisWidth = 0;
			axisHeight = 0;
		}

		if ((radius = cfg.getConfigMenu().getNodeRadius()) > 0) {
			if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
				bulletsWidth = this.boxClustersWidth();
				bulletsHeight = radius;
			} else {
				bulletsWidth = radius;
				bulletsHeight = this.boxClustersWidth();
			}
		} else {
			bulletsWidth = 0;
			bulletsHeight = 0;
		}

		if (cfg.getConfigMenu().isAxisLabelVisible()) {
			final TextBoxSize bf = new TextBoxSize(cfg.getConfigMenu().getAxisLabelFont());
			String txt;
			int ent;
			Dimensions<Double> dim;
			ent = (int) Math.round(axisMaxVal);
			txt = Integer.toString(ent);
			if (DendrogramOrientation.EAST.equals(or) || DendrogramOrientation.WEST.equals(or)) {
				if (cfg.isDistance())
					dim = bf.getBoxPositiveNumber(90, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
				else
					dim = bf.getBoxNegativeNumber(90, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
			} else {
				if (cfg.isDistance())
					dim = bf.getBoxPositiveNumber(0, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
				else
					dim = bf.getBoxNegativeNumber(0, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
			}
			axisLabelWidth = dim.getWidth();
			axisLabelHeight = dim.getHeight();
		} else {
			axisLabelWidth = 0;
			axisLabelHeight = 0;
		}

		if (cfg.getConfigMenu().isNodeNameVisible()) {
			int alf;
			final TextBoxSize bf = new TextBoxSize(cfg.getConfigMenu().getNodeNameFont());
			String tmp;
			Dimensions<Double> dim;

			if (cfg.getNodeNameOrientation().equals(LabelOrientation.HORIZONTAL))
				alf = 0;
			else if (cfg.getNodeNameOrientation().equals(LabelOrientation.OBLIQUE))
				alf = 45;
			else
				alf = -90;
			if (strMax.equals("")) {
				final Enumeration<String> el = cfg.getNames().elements();
				while (el.hasMoreElements()) {
					tmp = el.nextElement();
					if (tmp.length() > strMax.length())
						strMax = tmp;
				}
			}
			dim = bf.getBox(alf, strMax);

			nameWidth = dim.getWidth();
			nameHeight = dim.getHeight();
		} else {
			nameWidth = 0;
			nameHeight = 0;
		}
	}

	public void drawDendro() {
		BoxContainer boxDendrogram, boxBullets, boxAxis, boxAxisLabels, boxNames;

		final double margin = InitialProperties.getMargin(); // 15
		final double worldWidth, worldHeight;

		worldWidth = this.xmax - 72;
		worldHeight = this.ymax - 72;

		this.setWidths();

		Dimensions<Double> dimDendro, dimBullets, dimName, dimAxis, dimAxisLabel;

		dimDendro = new Dimensions<Double>(dendroWidth, dendroHeight);
		dimBullets = new Dimensions<Double>(bulletsWidth, bulletsHeight);
		dimName = new Dimensions<Double>(nameWidth, nameHeight);
		dimAxis = new Dimensions<Double>(axisWidth, axisHeight);
		dimAxisLabel = new Dimensions<Double>(axisLabelWidth, axisLabelHeight);

		final XYBox posBox = new XYBox(cfg, margin, worldWidth, worldHeight,
				dimDendro, dimBullets, dimName, dimAxis, dimAxisLabel);

		boxDendrogram = posBox.getBoxDendro();
		boxBullets = posBox.getBoxBullets();
		boxAxis = posBox.getBoxAxis();
		boxAxisLabels = posBox.getBoxAxisLabels();
		boxNames = posBox.getBoxNames();

		this.translateScreen(boxDendrogram, worldHeight);
		this.translateScreen(boxBullets, worldHeight);
		this.translateScreen(boxAxis, worldHeight);
		this.translateScreen(boxAxisLabels, worldHeight);
		this.translateScreen(boxNames, worldHeight);

		scaledDendrogram = new ScalingBox(boxDendrogram);
		if (cfg.getConfigMenu().getNodeRadius() > 0)
			scaledBullets = new ScalingBox(boxBullets);
		if (cfg.getConfigMenu().isNodeNameVisible())
			scaledNames = new ScalingBox(boxNames);
		if (cfg.getConfigMenu().isAxisVisible())
			scaledAxis = new ScalingBox(boxAxis);
		if (cfg.getConfigMenu().isAxisLabelVisible())
			scaledAxisLabels = new ScalingBox(boxAxisLabels);

		final Clipping clp = new Clipping(axisMaxVal, axisMinVal,
				cfg.getSimilarityType(), cfg.getPrecision());

		// draw interior of bands
		Band bnd;
		BandEps bndEps;
		final Iterator<Band> itb = clp.clipBands(bandsList).iterator();
		while (itb.hasNext()) {
			bnd = itb.next();
			bndEps = new BandEps(bnd.getPosReal().getX(), bnd.getPosReal().getY(),
					bnd.getHeight(), bnd.getWidth(), bnd.getPrecision(),
					bnd.getColor());
			bndEps.setScaling(scaledDendrogram);
			bndEps.setColor(cfg.getConfigMenu().getBandColor());
			bndEps.setFilled(true);
			bndEps.draw(dendroOrientation);
		}

		// draw lines
		Line lin;
		LineEps linEps = null;
		final Iterator<Line> itl = clp.clipLines(linesList).iterator();
		while (itl.hasNext()) {
			lin = itl.next();
			linEps = new LineEps(lin.getPosReal().getX(), lin.getPosReal()
					.getY(), lin.getLength(), lin.getPrecision(),
					lin.getColor());
			linEps.setScaling(scaledDendrogram);
			linEps.draw(dendroOrientation);
		}

		// draw band borders
		final Iterator<Band> itb2 = clp.clipBands(bandsList).iterator();
		while (itb2.hasNext()) {
			bnd = itb2.next();
			bndEps = new BandEps(bnd.getPosReal().getX(), bnd.getPosReal().getY(),
					bnd.getHeight(), bnd.getWidth(), bnd.getPrecision(),
					bnd.getColor());
			bndEps.setScaling(scaledDendrogram);
			bndEps.setColor(cfg.getConfigMenu().getBandColor());
			bndEps.setFilled(false);
			bndEps.draw(dendroOrientation);
		}

		// draw nodes
		if (cfg.getConfigMenu().getNodeRadius() > 0) {
			Node nod;
			NodeEps nodEps;
			final Iterator<Node> itn = nodesList.iterator();
			while (itn.hasNext()) {
				nod = itn.next();
				nodEps = new NodeEps(nod.getPosReal().getX(), nod.getPosReal()
						.getY(), nod.getRadius(), nod.getPrecision(),
						nod.getName());
				nodEps.setScaling(scaledBullets);
				nodEps.draw(dendroOrientation);
			}
		}

		// draw node names
		if (cfg.getConfigMenu().isNodeNameVisible()) {
			NodeLabelEps nodLabEps;
			nodLabEps = new NodeLabelEps(nodesList, cfg.getSimilarityType());
			nodLabEps.setScaling(scaledNames);
			nodLabEps.setColor(cfg.getConfigMenu().getNodeNameColor());
			nodLabEps.setFont(cfg.getConfigMenu().getNodeNameFont());
			nodLabEps.draw(dendroOrientation, nodeNameOrientation);
		}

		// draw axis
		if (cfg.getConfigMenu().isAxisVisible()) {
			AxisEps ax;
			if (dendroOrientation.equals(DendrogramOrientation.WEST)
					|| dendroOrientation.equals(DendrogramOrientation.EAST))
				ax = new AxisEps(boxAxis.getValMinX(),
						boxAxis.getValMaxX(), cfg.getAxisIncrement(),
						cfg.getAxisTicks());
			else
				ax = new AxisEps(boxAxis.getValMinY(),
						boxAxis.getValMaxY(), cfg.getAxisIncrement(),
						cfg.getAxisTicks());

			ax.setScaling(scaledAxis);
			ax.setColor(cfg.getConfigMenu().getAxisColor());
			ax.draw(dendroOrientation, cfg.getSimilarityType(), cfg.getAxisTicks());
		}

		// draw axis labels
		if (cfg.getConfigMenu().isAxisLabelVisible() && cfg.getAxisTicks() > 0) {
			AxisLabelEps axLabEps;
			if (dendroOrientation.equals(DendrogramOrientation.WEST)
					|| dendroOrientation.equals(DendrogramOrientation.EAST)) {
				axLabEps = new AxisLabelEps(boxAxisLabels.getValMinX(),
						boxAxisLabels.getValMaxX(),
						boxAxisLabels.getValMaxY(), cfg.getAxisIncrement(),
						cfg.getAxisTicks(), cfg.getAxisLabelDecimals());
			} else {
				axLabEps = new AxisLabelEps(boxAxisLabels.getValMinY(),
						boxAxisLabels.getValMaxY(),
						boxAxisLabels.getValMaxX(), cfg.getAxisIncrement(),
						cfg.getAxisTicks(), cfg.getAxisLabelDecimals());
			}
			axLabEps.setScaling(scaledAxisLabels);
			axLabEps.setColor(cfg.getConfigMenu().getAxisLabelColor());
			axLabEps.setFont(cfg.getConfigMenu().getAxisLabelFont());
			axLabEps.dibuixa(dendroOrientation, cfg.getSimilarityType());
		}
	}

}
