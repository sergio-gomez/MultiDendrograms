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
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.PlotType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Axis figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Axis {

	private Color color;
	private DendrogramOrientation dendroOrientation;
	private int numTicks;
	private int ticksGroup;
	private double x00;
	private double x05;
	private double x10;
	private double x15;
	private double x20;
	private double yMin;
	private double yMax;
	private double yIncr;

	public Axis(final SettingsInfo settingsInfo, final Scaling scaling) {
		this.color = settingsInfo.getAxisColor();
		this.dendroOrientation = settingsInfo.getDendrogramAdaptedOrientation();
		double minValue = settingsInfo.getAxisMinValue();
		double maxValue = settingsInfo.getAxisMaxValue();
		double increment = settingsInfo.getAxisIncrement();
		this.numTicks = settingsInfo.getAxisNumberOfTicks();
		this.ticksGroup = settingsInfo.getAxisTicks();
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			this.x00 = scaling.transformX(0.0);
			this.x05 = scaling.transformX(0.5);
			this.x10 = scaling.transformX(1.0);
			this.x15 = scaling.transformX(1.5);
			this.x20 = scaling.transformX(2.0);
			this.yMin = scaling.transformY(minValue);
			this.yMax = scaling.transformY(maxValue);
			this.yIncr = scaling.transformY(minValue + increment) - scaling.transformY(minValue);
		} else {// (dendroOrientation.equals(DendrogramOrientation.EAST) || dendroOrientation.equals(DendrogramOrientation.WEST))
			this.x00 = scaling.transformY(0.0);
			this.x05 = scaling.transformY(0.5);
			this.x10 = scaling.transformY(1.0);
			this.x15 = scaling.transformY(1.5);
			this.x20 = scaling.transformY(2.0);
			this.yMin = scaling.transformX(minValue);
			this.yMax = scaling.transformX(maxValue);
			this.yIncr = scaling.transformX(minValue + increment) - scaling.transformX(minValue);
		}
	}

	public void draw(final PlotType plotType, final Graphics2D graphics2D) {
		Color originalColor = null;
		if (plotType.equals(PlotType.PANEL)) {
			originalColor = graphics2D.getColor();
			graphics2D.setColor(color);
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine("gsave");
			EpsUtils.writeLine(EpsUtils.setRGBColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
		}

		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
			drawLine(plotType, graphics2D, x10, yMin, x10, yMax);
		} else {// (dendroOrientation.equals(DendrogramOrientation.EAST) || dendroOrientation.equals(DendrogramOrientation.WEST))
			drawLine(plotType, graphics2D, yMin, x10, yMax, x10);
		}

		double y;
		if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.EAST)) {
			y = yMin;
		} else {// (dendroOrientation.equals(DendrogramOrientation.SOUTH) || dendroOrientation.equals(DendrogramOrientation.WEST))
			y = yMax;
		}
		for (int n = 0; n < numTicks; n ++) {
			if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				if ((n % ticksGroup) == 0) {
					drawLine(plotType, graphics2D, x00, y, x20, y);
				} else {
					drawLine(plotType, graphics2D, x05, y, x15, y);
				}
			} else {
				// (dendroOrientation.equals(DendrogramOrientation.EAST) || dendroOrientation.equals(DendrogramOrientation.WEST))
				if ((n % ticksGroup) == 0) {
					drawLine(plotType, graphics2D, y, x00, y, x20);
				} else {
					drawLine(plotType, graphics2D, y, x05, y, x15);
				}
			}
			if (dendroOrientation.equals(DendrogramOrientation.NORTH) || dendroOrientation.equals(DendrogramOrientation.EAST)) {
				y += yIncr;
			} else {
				// (dendroOrientation.equals(DendrogramOrientation.SOUTH) || dendroOrientation.equals(DendrogramOrientation.WEST))
				y -= yIncr;
			}
		}

		if (plotType.equals(PlotType.PANEL)) {
			graphics2D.setColor(originalColor);
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine("grestore");
		}
	}

	private void drawLine(final PlotType plotType, final Graphics2D graphics2D, final double x0, final double y0, 
			final double x1, final double y1) {
		if (plotType.equals(PlotType.PANEL)) {
			graphics2D.draw(new Line2D.Double(x0, y0, x1, y1));
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + x0), (float) (EpsUtils.ymax + y0), 
					(float) (EpsUtils.xmin + x1), (float) (EpsUtils.ymax + y1)));
		}
	}

}
