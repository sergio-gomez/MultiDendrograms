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

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import multidendrograms.initial.LogManager;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.utils.MathUtils;
import multidendrograms.definitions.Coordinates;

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

	public Line(final Coordinates<Double> pos, final double length, final int precision) {
		super(pos.getX(), pos.getY(), precision);
		this.length = length;
	}

	public double getLength() {
		return length;
	}

	public void setLength(final double length) {
		this.length = length;
	}

	@Override
	public void draw(final Graphics2D g, final DendrogramOrientation or) {
		double x1, y1, x2, y2;
		double xx1, yy1, xx2, yy2;
		double maxVal, minVal;
		int prec = getPrecision();

		LogManager.LOG.finest("Orientation: " + or.toString());
		LogManager.LOG.finest("Precision: " + prec);

		// position adjusted to precision
		xx1 = this.getPosReal().getX();
		yy1 = MathUtils.round(this.getPosReal().getY(), prec);
		xx2 = this.getPosReal().getX();
		yy2 = MathUtils.round(this.getLength(), prec);
		LogManager.LOG.finest("Coord. Real: x1=" + xx1 + "    y1=("
				+ getPosReal().getY() + ") " + yy1 + "   x2= " + xx2
				+ "    y2= (" + getLength() + ")" + yy2);

		maxVal = this.getScaling().getMaxY();
		minVal = this.getScaling().getMinY();
		if (or == DendrogramOrientation.EAST) {
			// inversion
			x1 = yy1;
			yy1 = (maxVal - xx1);
			xx1 = x1;

			x2 = yy2;
			yy2 = (maxVal - xx2);
			xx2 = x2;
		} else if (or == DendrogramOrientation.WEST) {
			// inversion
			y1 = (maxVal - xx1);
			xx1 = this.getScaling().getMinX()
					+ (this.getScaling().getMaxX() - yy1);
			yy1 = y1;

			y2 = (maxVal - xx2);
			xx2 = this.getScaling().getMinX()
					+ (this.getScaling().getMaxX() - yy2);
			yy2 = y2;
		} else if (or == DendrogramOrientation.SOUTH) {
			// translation
			yy1 = minVal + (maxVal - yy1);
			yy2 = minVal + (maxVal - yy2);
		}

		x1 = this.getScaling().transformX(xx1);
		y1 = this.getScaling().transformY(yy1);
		x2 = this.getScaling().transformX(xx2);
		y2 = this.getScaling().transformY(yy2);

		g.setColor(this.getColor());
		g.draw(new Line2D.Double(x1, y1, x2, y2));

		LogManager.LOG.finer("draw Line2D: (" + x1 + ", " + y1 + ", " + x2 + ", "
				+ y2 + ")");
	}

	@Override
	public void draw(DendrogramOrientation or) {
	}
}
