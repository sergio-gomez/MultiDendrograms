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

package multidendrograms.dendrogram.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.PlotType;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Axis label figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class AxisLabel {

	private Color color;
	private SimilarityType simType;
	private DendrogramOrientation dendroOrientation;
	private double minValue;
	private double maxValue;
	private double increment;
	private int ticksGroup;
	private int numLabels;
	private FontRenderContext renderContext;
	private NumberFormat numberFormat;
	private Scaling scaling;
	private double x0;
	private double yMin;
	private double yMax;
	private double rotationAngle;
	private Font font;
	private double labelsMaxWidth;

	public AxisLabel(final SettingsInfo settingsInfo, final Scaling scaling) {
		this.color = settingsInfo.getAxisLabelColor();
		this.simType = settingsInfo.getSimilarityType();
		this.dendroOrientation = settingsInfo.getDendrogramOrientation();
		this.minValue = settingsInfo.getAxisMinValue();
		this.maxValue = settingsInfo.getAxisMaxValue();
		this.increment = settingsInfo.getAxisIncrement();
		this.ticksGroup = settingsInfo.getAxisTicks();
		int numTicks = settingsInfo.getAxisNumberOfTicks();
		this.numLabels = 1 + (numTicks - 1) / ticksGroup;
		this.renderContext = new FontRenderContext(null, true, true);
		int labelsDecimals = settingsInfo.getAxisLabelDecimals();
		this.numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
		this.numberFormat.setMinimumFractionDigits(labelsDecimals);
		this.numberFormat.setMaximumFractionDigits(labelsDecimals);
		this.numberFormat.setGroupingUsed(false);
		this.scaling = scaling;
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			this.x0 = scaling.transformX(0);
			this.yMin = scaling.transformY(minValue);
			this.yMax = scaling.transformY(maxValue);
			this.rotationAngle = 0.0;
		} else {// (dendroOrientation.equals(DendrogramOrientation.EAST) || dendroOrientation.equals(DendrogramOrientation.WEST))
			this.x0 = scaling.transformY(0);
			this.yMin = scaling.transformX(minValue);
			this.yMax = scaling.transformX(maxValue);
			this.rotationAngle = 90.0;
		}
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(Math.toRadians(-rotationAngle));
		Font axisFont = settingsInfo.getAxisLabelFont();
		this.font = axisFont.deriveFont(rotation);
		setLabelsMaximumWidth();
	}

	private void setLabelsMaximumWidth() {
		labelsMaxWidth = 0.0;
		double height;
		if (simType.equals(SimilarityType.DISTANCE)) {
			height = minValue;
		} else {
			height = maxValue;
		}
		for (int n = 0; n < numLabels; n ++) {
			String heightLabel = String.valueOf(numberFormat.format(height));
			TextLayout textLayout = new TextLayout(heightLabel, font, renderContext);
			Rectangle2D rectangle = textLayout.getBounds();
			if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				labelsMaxWidth = Math.max(labelsMaxWidth, rectangle.getWidth());
			} else {// (dendroOrientation.equals(DendrogramOrientation.EAST) || dendroOrientation.equals(DendrogramOrientation.WEST))
				labelsMaxWidth = Math.max(labelsMaxWidth, rectangle.getHeight());
			}
			if (simType.equals(SimilarityType.DISTANCE)) {
				height += (ticksGroup * increment);
			} else {
				height -= (ticksGroup * increment);
			}
		}
	}

	public void draw(final PlotType plotType, final Graphics2D graphics2D) {
		Color originalColor = null;
		Font originalFont = null;
		if (plotType.equals(PlotType.PANEL)) {
			originalColor = graphics2D.getColor();
			graphics2D.setColor(color);
			originalFont = graphics2D.getFont();
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine("gsave");
			EpsUtils.writeLine(EpsUtils.setRGBColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
			String fontPSName = font.getPSName();
			if (fontPSName.equals("Dialog.plain")) {
				EpsUtils.writeLine(EpsUtils.scaleSetFont("/ArialMT", font.getSize()));
			} else {
				EpsUtils.writeLine(EpsUtils.scaleSetFont("/" + fontPSName, font.getSize()));
			}
		}

		double height;
		if (simType.equals(SimilarityType.DISTANCE)) {
			height = minValue;
		} else {
			height = maxValue;
		}
		for (int n = 0; n < numLabels; n ++) {
			String heightLabel = String.valueOf(numberFormat.format(height));
			TextLayout textLayout = new TextLayout(heightLabel, font, renderContext);
			Rectangle2D rectangle = textLayout.getBounds();
			float screenX;
			float screenY;
			if (dendroOrientation.equals(DendrogramOrientation.NORTH)) {
				screenX = (float) (x0 + (labelsMaxWidth - rectangle.getWidth()));
				screenY = (float) (scaling.transformY(height) - (rectangle.getHeight() / 2.0));
			} else if (dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				screenX = (float) (x0 + (labelsMaxWidth - rectangle.getWidth()));
				screenY = (float) (yMin + (yMax - scaling.transformY(height)) - (rectangle.getHeight() / 2.0));
			} else if (dendroOrientation.equals(DendrogramOrientation.EAST)) {
				screenX = (float) (scaling.transformX(height) + (rectangle.getWidth() / 2.0));
				screenY = (float) (x0 + (labelsMaxWidth - rectangle.getHeight()));
			} else {// (dendroOrientation.equals(DendrogramOrientation.WEST))
				screenX = (float) (yMin + (yMax - scaling.transformX(height)) + (rectangle.getWidth() / 2.0));
				screenY = (float) (x0 + (labelsMaxWidth - rectangle.getHeight()));
			}
			if (plotType.equals(PlotType.PANEL)) {
				graphics2D.scale(1, -1);
				textLayout.draw(graphics2D, screenX, -screenY);
				graphics2D.scale(1, -1);
			} else {// (plotType.equals(PlotType.EPS))
				EpsUtils.writeLine(EpsUtils.bottomLeftTextRotated((EpsUtils.xmin + screenX), (EpsUtils.ymax + screenY), 
						(float) (rotationAngle), heightLabel));
			}
			if (simType.equals(SimilarityType.DISTANCE)) {
				height += (ticksGroup * increment);
			} else {
				height -= (ticksGroup * increment);
			}
		}
		
		if (plotType.equals(PlotType.PANEL)) {
			graphics2D.setColor(originalColor);
			graphics2D.setFont(originalFont);
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine("grestore");
		}
	}

}
