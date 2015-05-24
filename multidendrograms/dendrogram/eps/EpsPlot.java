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

import java.util.Iterator;
import java.util.LinkedList;

import multidendrograms.dendrogram.Clipping;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.figures.Axis;
import multidendrograms.dendrogram.figures.AxisLabel;
import multidendrograms.dendrogram.figures.Band;
import multidendrograms.dendrogram.figures.Line;
import multidendrograms.dendrogram.figures.Node;
import multidendrograms.dendrogram.figures.NodeLabel;
import multidendrograms.forms.XYBox;
import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.PlotType;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.SettingsInfo;

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

	private Config cfg = null;

	private LinkedList<Node> nodesList;
	private LinkedList<Line> linesList;
	private LinkedList<Band> bandsList;

	private DendrogramPanel dendroPanel;
	private final int xMax, yMax;

	public EpsPlot(DendrogramPanel dendroPanel, Config cfg, int xMax, int yMax) {
		this.dendroPanel = dendroPanel;
		this.nodesList = dendroPanel.getNodesList();
		this.linesList = dendroPanel.getLinesList();
		this.bandsList = dendroPanel.getBandsList();
		this.xMax = xMax;
		this.yMax = yMax;
		setConfig(cfg);
	}

	public void setConfig(final Config cfg) {
		this.cfg = cfg;
	}

	public DendrogramPanel getDendrogramPanel() {
		return this.dendroPanel;
	}

	public void setDendrogramPanel(DendrogramPanel dendroPanel) {
		this.dendroPanel = dendroPanel;
	}

	public void drawDendro() {
		final double worldWidth = this.xMax - 72.0;
		final double worldHeight = this.yMax - 72.0;

		XYBox boxes = new XYBox(this.cfg, worldWidth, worldHeight);
		Scaling scalingDendro = boxes.getScalingDendrogram();
		Scaling scalingBullets = boxes.getScalingBullets();
		Scaling scalingAxis = boxes.getScalingAxis();
		Scaling scalingAxisLabels = boxes.getScalingAxisLabels();
		
		DendrogramOrientation dendroOrientation = this.cfg.getDendrogramOrientation();
		if ((dendroOrientation == DendrogramOrientation.NORTH) || (dendroOrientation == DendrogramOrientation.SOUTH)) {
			double height = scalingBullets.getHeight() / 2.0;
			scalingBullets.setHeight(height);
		} else {// ((dendroOrientation == DendrogramOrientation.EAST) || (dendroOrientation == DendrogramOrientation.WEST))
			double width = scalingBullets.getWidth() / 2.0;
			scalingBullets.setWidth(width);
		}
		
		drawDendrogram(scalingDendro);
		drawBullets(scalingBullets, scalingDendro);
		drawNodesNames(scalingDendro);
		drawAxis(scalingAxis);
		drawAxisLabels(scalingAxisLabels);		
	}

	private void drawDendrogram(final Scaling scalingDendro) {
		final SettingsInfo settingsInfo = this.cfg.getConfigMenu();
		final Clipping clipping = new Clipping(this.cfg.getSimilarityType(), this.cfg.getAxisMinValue(), 
				this.cfg.getAxisMaxValue());
		
		// draw interior of bands
		final Iterator<Band> iterIB = clipping.clipBands(this.bandsList).iterator();
		while (iterIB.hasNext()) {
			Band band = iterIB.next();
			Band bandEps = new Band(band.getPosReal().getX(), band.getPosReal().getY(),
					band.getHeight(), band.getWidth(), band.getColor());
			bandEps.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
			bandEps.setScaling(scalingDendro);
			bandEps.setColor(settingsInfo.getBandColor());
			bandEps.setFilled(true);
			bandEps.draw(PlotType.EPS, null);
		}

		// draw lines
		Iterator<Line> iterL = clipping.clipLines(this.linesList).iterator();
		while (iterL.hasNext()) {
			Line line = iterL.next();
			Line lineEps = new Line(line.getPosReal().getX(), line.getPosReal().getY(), 
					line.getLength(), line.getColor());
			lineEps.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
			lineEps.setScaling(scalingDendro);
			lineEps.draw(PlotType.EPS, null);
		}

		// draw band borders
		Iterator<Band> iterBB = clipping.clipBands(this.bandsList).iterator();
		while (iterBB.hasNext()) {
			Band band = iterBB.next();
			Band bandEps = new Band(band.getPosReal().getX(), band.getPosReal().getY(),
					band.getHeight(), band.getWidth(), band.getColor());
			bandEps.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
			bandEps.setScaling(scalingDendro);
			bandEps.setColor(settingsInfo.getBandColor());
			bandEps.setFilled(false);
			bandEps.draw(PlotType.EPS, null);
		}
	}

	private void drawBullets(final Scaling scalingBullets, final Scaling scalingDendro) {
		final SettingsInfo settingsInfo = this.cfg.getConfigMenu();
		
		if (settingsInfo.getNodeRadius() > 0) {
			Iterator<Node> iter = this.nodesList.iterator();
			while (iter.hasNext()) {
				Node node = iter.next();
				Node nodeEps = new Node(node.getPosReal().getX(), node.getPosReal().getY(), node.getRadius(), node.getName());
				nodeEps.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
				nodeEps.setScaling(scalingBullets);
				nodeEps.setScalingDendrogram(scalingDendro);
				nodeEps.draw(PlotType.EPS, null);
			}
		}
	}

	private void drawNodesNames(final Scaling scalingDendro) {
		final SettingsInfo settingsInfo = this.cfg.getConfigMenu();
		if (settingsInfo.isNodeNameVisible()) {
			NodeLabel nodeLabel = new NodeLabel(this.nodesList, settingsInfo, scalingDendro);
			nodeLabel.draw(PlotType.EPS, null);
		}
	}

	private void drawAxis(final Scaling scalingAxis) {
		final SettingsInfo settingsInfo = this.cfg.getConfigMenu();
		if (settingsInfo.isAxisVisible() && (this.cfg.getAxisTicks() > 0)) {
			Axis axis = new Axis(settingsInfo, scalingAxis);
			axis.draw(PlotType.EPS, null);
		}
	}

	private void drawAxisLabels(final Scaling scalingAxisLabels) {
		final SettingsInfo settingsInfo = this.cfg.getConfigMenu();
		if (settingsInfo.isAxisLabelVisible() && (this.cfg.getAxisTicks() > 0)) {
			AxisLabel axisLabel = new AxisLabel(settingsInfo, scalingAxisLabels);
			axisLabel.draw(PlotType.EPS, null);
		}
	}

}
