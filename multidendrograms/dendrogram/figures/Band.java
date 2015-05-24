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
import java.awt.geom.Rectangle2D;

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
 * Band figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Band extends Figure {

	private double height, width;
	private boolean filled;

	public Band(final double x, final double y, final double height, final double width) {
		super(x, y, Color.GRAY);
		this.height = height;
		this.width = width;
		this.filled = true;
	}

	public Band(final double x, final double y, final double height, final double width, final Color color) {
		super(x, y, color);
		this.height = height;
		this.width = width;
		this.filled = true;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(final double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(final double width) {
		this.width = width;
	}

	public boolean getFilled() {
		return filled;
	}

	public void setFilled(final boolean filled) {
		this.filled = filled;
	}

	@Override
	public void draw(final PlotType plotType, final Graphics2D graphics2D) {
		double x1 = getPosReal().getX();
		double y1 = getPosReal().getY();
		double x2 = x1 + this.width;
		double y2 = y1 + this.height;

		LogManager.LOG.finest("Real coord.: x1 = " + x1 + "    y1 = "
				+ y1 + "    x2 = " + x2 + "    y2 = " + y2);

		Scaling scale = getScaling();
		double minX = scale.getMinX();
		double maxX = scale.getMaxX();
		double minY = scale.getMinY();
		double maxY = scale.getMaxY();
		DendrogramOrientation dendroOrientation = getDendrogramOrientation();
		if (dendroOrientation == DendrogramOrientation.EAST) {
			double yy1 = y1;
			y1 = minY + (maxY - x1);
			x1 = yy1;
			double yy2 = y2;
			y2 = minY + (maxY - x2);
			x2 = yy2;
			yy1 = y1;
			y1 = y2;
			y2 = yy1;
		} else if (dendroOrientation == DendrogramOrientation.WEST) {
			// rotation of P(x,y)
			double xx1 = x1;
			x1 = minX + (maxX - y1);
			y1 = minY + (maxY - xx1);
			// rotation of P(x',y')
			double xx2 = x2;
			x2 = minX + (maxX - y2);
			y2 = minY + (maxY - xx2);
			// translation of origin
			xx1 = x1;
			x1 = x2;
			x2 = xx1;
			double yy1 = y1;
			y1 = y2;
			y2 = yy1;
		} else if (dendroOrientation == DendrogramOrientation.SOUTH) {
			// rotation of P(x,y)
			y1 = minY + (maxY - y1);
			y2 = minY + (maxY - y2);
			double yy2 = y1;
			y1 = y2;
			y2 = yy2;
		}

		// scaling
		x1 = scale.transformX(x1);
		y1 = scale.transformY(y1);
		x2 = scale.transformX(x2);
		y2 = scale.transformY(y2);

		// drawing
		if (plotType.equals(PlotType.PANEL)) {
			Color originalColor = graphics2D.getColor();
			if (this.filled) {
				graphics2D.setPaint(getColor());
				graphics2D.setColor(getColor());
				graphics2D.fill(new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
			} else {
				graphics2D.setColor(Color.BLACK);
				graphics2D.draw(new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
			}
			graphics2D.setColor(originalColor);
		} else {// (plotType.equals(PlotType.EPS))
			EpsUtils.writeLine("gsave");
			Color color = (this.filled) ? getColor() : Color.BLACK;
			EpsUtils.writeLine(EpsUtils.setRGBColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
			if (this.filled) {
				EpsUtils.writeLine(EpsUtils.fRectangle((float) (EpsUtils.xmin + x1), (float) (EpsUtils.ymax + y1), 
						(float) (EpsUtils.xmin + x2), (float) (EpsUtils.ymax + y2)));
			} else {
				EpsUtils.writeLine(EpsUtils.dRectangle((float) (EpsUtils.xmin + x1), (float) (EpsUtils.ymax + y1), 
						(float) (EpsUtils.xmin + x2), (float) (EpsUtils.ymax + y2)));
			}
			EpsUtils.writeLine("grestore");
		}

		LogManager.LOG.finest("draw Rectangle2D(" + x1 + ", " + y1 + ", "
				+ (x2 - x1) + ", " + (y2 - y1) + ")");
	}

}
