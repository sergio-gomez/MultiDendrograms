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
import multidendrograms.types.LabelOrientation;
import multidendrograms.utils.TextBoxSize;
import multidendrograms.definitions.BoxContainer;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Dimensions;
import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.initial.InitialProperties;

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

	private Scaling scalingDendro = null;
	private Scaling scalingBullets = null;
	private Scaling scalingAxis = null;
	private Scaling scalingAxisLabels = null;

	public XYBox(final Config cfg, final double worldWidth, final double worldHeight) {
		final double margin = InitialProperties.getSizeDendroMargin();
		final SettingsInfo settingsInfo = cfg.getSettingsInfo();
		final DendrogramOrientation dendroOrientation = cfg.getDendrogramOrientation();
		final double radius = cfg.getRadius();

		Dimensions<Double> dimBullets = getDimensionsBullets(cfg);
		Dimensions<Double> dimNodesNames = getDimensionsNodesNames(cfg);
		Dimensions<Double> dimAxis = getDimensionsAxis(cfg);
		Dimensions<Double> dimAxisLabels = getDimensionsAxisLabels(cfg);

		double usedWidth;
		double usedHeight;
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			usedWidth = dimAxis.getWidth() + dimAxisLabels.getWidth() + 2 * radius;
			usedHeight = dimNodesNames.getHeight() + dimBullets.getHeight() + 2 * radius;
		} else {
			usedWidth = dimNodesNames.getWidth() + dimBullets.getWidth() + 2 * radius;
			usedHeight = dimAxis.getHeight() + dimAxisLabels.getHeight() + 2 * radius;
		}
		DendrogramOrientation adaptedOrientation = settingsInfo.getDendrogramAdaptedOrientation();
		double freeWidth;
		double freeHeight;
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			freeWidth = worldWidth - (usedWidth + 2 * margin + dimNodesNames.getWidth());
			freeHeight = worldHeight - (usedHeight + 2 * margin);
		} else {
			freeWidth = worldWidth - (usedWidth + 2 * margin);
			freeHeight = worldHeight - (usedHeight + 2 * margin + dimNodesNames.getHeight());
		}

		BoxContainer boxDendro = getBoxDendrogram(cfg, adaptedOrientation, dimNodesNames, usedWidth, usedHeight, 
				freeWidth, freeHeight);
		BoxContainer boxBullets = getBoxBullets(cfg, adaptedOrientation, dimBullets, freeWidth, freeHeight, boxDendro);
		BoxContainer boxAxis = getBoxAxis(cfg, dimAxis, dimAxisLabels, freeWidth, freeHeight, boxDendro);
		BoxContainer boxAxisLabels = getBoxAxisLabels(cfg, dimAxis, dimAxisLabels, freeWidth, freeHeight, boxDendro);

		// situation of boxes in the screen
		boxDendro.increaseCornerY(-worldHeight);
		boxBullets.increaseCornerY(-worldHeight);
		boxAxis.increaseCornerY(-worldHeight);
		boxAxisLabels.increaseCornerY(-worldHeight);

		this.scalingDendro = new Scaling(boxDendro);
		this.scalingBullets = new Scaling(boxBullets);
		this.scalingAxis = new Scaling(boxAxis);
		this.scalingAxisLabels = new Scaling(boxAxisLabels);
	}

	private Dimensions<Double> getDimensionsBullets(final Config cfg) {
		final SettingsInfo settingsInfo = cfg.getSettingsInfo();
		final DendrogramOrientation dendroOrientation = cfg.getDendrogramOrientation();
		final int numNodes = cfg.getHierarchicalClustering().getRoot().numberOfLeaves();
		final double radius = cfg.getRadius();
		
		double nodesRadius = settingsInfo.getNodeRadius();
		double width = 0.0;
		double height = 0.0;
		if (nodesRadius > 0) {
			if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				width = boxNodesWidth(numNodes, radius);
				height = 2 * nodesRadius;
			} else {
				width = 2 * nodesRadius;
				height = boxNodesWidth(numNodes, radius);
			}
		}
		Dimensions<Double> dim = new Dimensions<Double>(width, height);
		return dim;
	}

	private Dimensions<Double> getDimensionsNodesNames(final Config cfg) {
		final SettingsInfo settingsInfo = cfg.getSettingsInfo();
		
		double width = 0.0;
		double height = 0.0;
		if (settingsInfo.isNodeNameVisible()) {
			LabelOrientation nameOrientation = cfg.getNodeNameOrientation();
			int alf;
			if (nameOrientation.equals(LabelOrientation.HORIZONTAL)) {
				alf = 0;
			} else if (nameOrientation.equals(LabelOrientation.OBLIQUE)) {
				alf = 45;
			} else {// (nameOrientation.equals(LabelOrientation.VERTICAL))
				alf = -90;
			}
			final TextBoxSize box = new TextBoxSize(settingsInfo.getNodeNameFont());
			String[] names = cfg.getNames();
			for (int i = 0; i < names.length; i ++) {
				Dimensions<Double> nameDim = box.getBox(alf, names[i]);
				width = Math.max(width, nameDim.getWidth());
				height = Math.max(height, nameDim.getHeight());
			}
		}
		Dimensions<Double> dim = new Dimensions<Double>(width, height);
		return dim;
	}

	private Dimensions<Double> getDimensionsAxis(final Config cfg) {
		final SettingsInfo settingsInfo = cfg.getSettingsInfo();
		final DendrogramOrientation dendroOrientation = cfg.getDendrogramOrientation();
		final double radius = cfg.getRadius();
		final double axisLength = cfg.getAxisMaxValue() - cfg.getAxisMinValue();
		
		double width = 0.0;
		double height = 0.0;
		if (settingsInfo.isAxisVisible()) {
			if (dendroOrientation.equals(DendrogramOrientation.NORTH) || 
					dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				width = 2 * radius;
				height = axisLength;
			} else {
				width = axisLength;
				height = 2 * radius;
			}
		}
		Dimensions<Double> dim = new Dimensions<Double>(width, height);
		return dim;
	}

	private Dimensions<Double> getDimensionsAxisLabels(final Config cfg) {
		final SettingsInfo settingsInfo = cfg.getSettingsInfo();
		final DendrogramOrientation dendroOrientation = cfg.getDendrogramOrientation();
		
		Dimensions<Double> dim = new Dimensions<Double>(0.0, 0.0);
		if (settingsInfo.isAxisLabelVisible()) {
			final TextBoxSize box = new TextBoxSize(settingsInfo.getAxisLabelFont());
			int intMax = (int) Math.round(cfg.getAxisMaxValue());
			String strMax = Integer.toString(intMax);
			int lengthMax = (strMax.trim()).length();
			int axisDecimals = cfg.getAxisLabelDecimals();
			if (dendroOrientation.equals(DendrogramOrientation.NORTH) || 
					dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				if (cfg.isDistance()) {
					dim = box.getBoxPositiveNumber(0, lengthMax, axisDecimals);
				} else {
					dim = box.getBoxNegativeNumber(0, lengthMax, axisDecimals);
				}
			} else {
				if (cfg.isDistance()) {
					dim = box.getBoxPositiveNumber(90, lengthMax, axisDecimals);
				} else {
					dim = box.getBoxNegativeNumber(90, lengthMax, axisDecimals);
				}
			}
		}
		return dim;
	}

	private BoxContainer getBoxDendrogram(final Config cfg, final DendrogramOrientation adaptedOrientation, 
			final Dimensions<Double> dimNodesNames, final double usedWidth, final double usedHeight, 
			final double freeWidth, final double freeHeight) {
		final double margin = InitialProperties.getSizeDendroMargin();
		final int numNodes = cfg.getHierarchicalClustering().getRoot().numberOfLeaves();
		final double radius = cfg.getRadius();
		final double axisMinValue = cfg.getAxisMinValue();
		final double axisMaxValue = cfg.getAxisMaxValue();

		BoxContainer boxDendro = new BoxContainer();
		double x = adaptedOrientation.equals(DendrogramOrientation.WEST) ? margin : (usedWidth + margin);
		double y = adaptedOrientation.equals(DendrogramOrientation.NORTH) ? (usedHeight + margin) : margin;
		boxDendro.setCornerX(x);
		if (adaptedOrientation.equals(DendrogramOrientation.NORTH) || adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
			boxDendro.setCornerY(y);
		} else {
			boxDendro.setCornerY(y + dimNodesNames.getHeight());
		}
		boxDendro.setWidth(freeWidth);
		boxDendro.setHeight(freeHeight);
		if (adaptedOrientation.equals(DendrogramOrientation.NORTH)	|| adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
			boxDendro.setValMinX(0d);
			boxDendro.setValMaxX(boxNodesWidth(numNodes, radius));
			boxDendro.setValMinY(axisMinValue);
			boxDendro.setValMaxY(axisMaxValue);
		} else {
			boxDendro.setValMinX(axisMinValue);
			boxDendro.setValMaxX(axisMaxValue);
			boxDendro.setValMinY(0d);
			boxDendro.setValMaxY(boxNodesWidth(numNodes, radius));
		}
		return boxDendro;
	}

	private BoxContainer getBoxBullets(final Config cfg, final DendrogramOrientation adaptedOrientation, 
			final Dimensions<Double> dimBullets, final double freeWidth, final double freeHeight, final BoxContainer boxDendro) {
		final int numNodes = cfg.getHierarchicalClustering().getRoot().numberOfLeaves();
		final double radius = cfg.getRadius();

		double x, y;
		if (adaptedOrientation.equals(DendrogramOrientation.NORTH)) {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() - radius + 1;
		} else if (adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + freeHeight - radius + 1;
		} else if (adaptedOrientation.equals(DendrogramOrientation.WEST)) {
			x = boxDendro.getCornerX() + freeWidth - radius + 1;
			y = boxDendro.getCornerY();
		} else {// (adaptedOrientation.equals(DendrogramOrientation.EAST))
			x = boxDendro.getCornerX() - radius + 1;
			y = boxDendro.getCornerY();
		}
		double w, h;
		if (adaptedOrientation.equals(DendrogramOrientation.NORTH) || adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
			w = freeWidth;
			h = dimBullets.getHeight();
		} else {
			w = dimBullets.getWidth();
			h = freeHeight;
		}
		
		BoxContainer boxBullets = new BoxContainer();
		boxBullets.setCornerX(x);
		boxBullets.setCornerY(y);
		boxBullets.setWidth(w);
		boxBullets.setHeight(h);
		if (adaptedOrientation.equals(DendrogramOrientation.NORTH)	|| adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
			boxBullets.setValMinX(0d);
			boxBullets.setValMaxX(boxNodesWidth(numNodes, radius));
			boxBullets.setValMinY(0d);
			boxBullets.setValMaxY(radius);
		} else {
			boxBullets.setValMinX(0d);
			boxBullets.setValMaxX(radius);
			boxBullets.setValMinY(0d);
			boxBullets.setValMaxY(boxNodesWidth(numNodes, radius));
		}
		return boxBullets;
	}

	private BoxContainer getBoxAxis(final Config cfg, final Dimensions<Double> dimAxis, final Dimensions<Double> dimAxisLabels, 
			final double freeWidth, final double freeHeight, final BoxContainer boxDendro) {
		final double margin = InitialProperties.getSizeDendroMargin();
		final double radius = cfg.getRadius();
		final DendrogramOrientation dendroOrientation = cfg.getDendrogramOrientation();
		final double axisMinValue = cfg.getAxisMinValue();
		final double axisMaxValue = cfg.getAxisMaxValue();

		double x, y;
		double w, h;
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			x = dimAxisLabels.getWidth() + radius + margin;
			y = boxDendro.getCornerY();
			w = dimAxis.getWidth();
			h = freeHeight;
		} else {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + freeHeight + radius;
			w = freeWidth;
			h = dimAxis.getHeight();
		}
		
		BoxContainer boxAxis = new BoxContainer();
		boxAxis.setCornerX(x);
		boxAxis.setCornerY(y);
		boxAxis.setWidth(w);
		boxAxis.setHeight(h);
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			boxAxis.setValMinX(0d);
			boxAxis.setValMaxX(2d);
			boxAxis.setValMinY(axisMinValue);
			boxAxis.setValMaxY(axisMaxValue);
		} else {
			boxAxis.setValMinX(axisMinValue);
			boxAxis.setValMaxX(axisMaxValue);
			boxAxis.setValMinY(0d);
			boxAxis.setValMaxY(2d);
		}
		return boxAxis;
	}

	private BoxContainer getBoxAxisLabels(final Config cfg, final Dimensions<Double> dimAxis, 
			final Dimensions<Double> dimAxisLabels, final double freeWidth, final double freeHeight, final BoxContainer boxDendro) {
		final double margin = InitialProperties.getSizeDendroMargin();
		final double radius = cfg.getRadius();
		final DendrogramOrientation dendroOrientation = cfg.getDendrogramOrientation();
		final double axisMinValue = cfg.getAxisMinValue();
		final double axisMaxValue = cfg.getAxisMaxValue();

		double x, y;
		double w, h;
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			x = margin;
			y = boxDendro.getCornerY();
			w = dimAxisLabels.getWidth();
			h = freeHeight;
		} else {
			x = boxDendro.getCornerX();
			y = boxDendro.getCornerY() + freeHeight + dimAxis.getHeight() + 2 * radius;
			w = freeWidth;
			h = dimAxisLabels.getHeight();
		}
		
		BoxContainer boxAxisLabels = new BoxContainer();
		boxAxisLabels.setCornerX(x);
		boxAxisLabels.setCornerY(y);
		boxAxisLabels.setWidth(w);
		boxAxisLabels.setHeight(h);
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			boxAxisLabels.setValMinX(0d);
			boxAxisLabels.setValMaxX(dimAxisLabels.getWidth());
			boxAxisLabels.setValMinY(axisMinValue);
			boxAxisLabels.setValMaxY(axisMaxValue);
		} else {
			boxAxisLabels.setValMinX(axisMinValue);
			boxAxisLabels.setValMaxX(axisMaxValue);
			boxAxisLabels.setValMinY(0d);
			boxAxisLabels.setValMaxY(dimAxisLabels.getHeight());
		}
		return boxAxisLabels;
	}

	private double boxNodesWidth(final int numNodes, final double radius) {
		return ((2 * radius * numNodes) + ((numNodes - 1) * radius));
	}

	public Scaling getScalingDendrogram() {
		return this.scalingDendro;
	}

	public Scaling getScalingBullets() {
		return this.scalingBullets;
	}

	public Scaling getScalingAxis() {
		return this.scalingAxis;
	}

	public Scaling getScalingAxisLabels() {
		return this.scalingAxisLabels;
	}

}
