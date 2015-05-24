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

import multidendrograms.definitions.Coordinates;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.initial.LogManager;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.PlotType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Line figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Line extends Figure {

	private double length;

	public Line(final double x, final double y, final double length) {
		super(x, y);
		this.length = length;
	}

	public Line(final double x, final double y, final double length, final Color color) {
		super(x, y, color);
		this.length = length;
	}

	public double getLength() {
		return length;
	}

	public void setLength(final double length) {
		this.length = length;
	}

	@Override
	public void draw(final PlotType plotType, final Graphics2D graphics2D) {
		double worldX1 = getPosReal().getX();
		double worldY1 = getPosReal().getY();
		double worldX2 = worldX1;
		double worldY2 = this.length;
		Coordinates<Double> world1 = new Coordinates<Double>(worldX1, worldY1);
		Coordinates<Double> world2 = new Coordinates<Double>(worldX2, worldY2);
		Scaling scaling = getScaling();
		DendrogramOrientation dendroOrientation = getDendrogramOrientation();
		Coordinates<Double> screen1 = scaling.transform(world1, dendroOrientation);
		Coordinates<Double> screen2 = scaling.transform(world2, dendroOrientation);
		double screenX1 = screen1.getX();
		double screenY1 = screen1.getY();
		double screenX2 = screen2.getX();
		double screenY2 = screen2.getY();

		Color color = getColor();
		if (plotType.equals(PlotType.PANEL)) {
			graphics2D.setColor(color);
			graphics2D.draw(new Line2D.Double(screenX1, screenY1, screenX2, screenY2));
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine("gsave");
			EpsUtils.writeLine(EpsUtils.setRGBColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
			if (dendroOrientation.equals(DendrogramOrientation.NORTH)) {
				EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + screenX1), (float) (EpsUtils.ymax + screenY1 - 0.5), 
						(float) (EpsUtils.xmin + screenX2), (float) (EpsUtils.ymax + screenY2 + 0.5)));
			} else if (dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + screenX1), (float) (EpsUtils.ymax + screenY1 + 0.5), 
						(float) (EpsUtils.xmin + screenX2), (float) (EpsUtils.ymax + screenY2 - 0.5)));
			} else if (dendroOrientation.equals(DendrogramOrientation.EAST)) {
				EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + screenX1 - 0.5), (float) (EpsUtils.ymax + screenY1), 
						(float) (EpsUtils.xmin + screenX2 + 0.5), (float) (EpsUtils.ymax + screenY2)));
			} else if (dendroOrientation.equals(DendrogramOrientation.WEST)) {
				EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + screenX1 + 0.5), (float) (EpsUtils.ymax + screenY1), 
						(float) (EpsUtils.xmin + screenX2 - 0.5), (float) (EpsUtils.ymax + screenY2)));
			}
			EpsUtils.writeLine("grestore");
		}

		LogManager.LOG.finest("Real coord.: x1 = " + worldX1 + "    y1 = " + worldY1 
				+ "    x2 = " + worldX2 + "    y2 = " + worldY2);
		LogManager.LOG.finer("draw Line2D: (" + screenX1 + ", " + screenY1 + ", " + screenX2 + ", " + screenY2 + ")");
	}

}
