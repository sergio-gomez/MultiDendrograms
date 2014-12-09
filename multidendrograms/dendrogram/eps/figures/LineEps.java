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

package multidendrograms.dendrogram.eps.figures;

import java.awt.Color;
import java.awt.Graphics2D;

import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.dendrogram.figures.Figure;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.utils.MathUtils;
import multidendrograms.definitions.Coordinates;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Line EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class LineEps extends Figure {
	private double length;

	public LineEps(final double x, final double y, final double length,
			final int precision) {
		super(x, y, precision);
		this.length = length;
	}

	public LineEps(final double x, final double y, final double length,
			final int precision, final Color c) {
		super(x, y, precision, c);
		this.length = length;
	}

	public LineEps(final Coordinates<Double> pos, final double length,
			final int precision) {
		super(pos.getX(), pos.getY(), precision);
		this.length = length;
	}

	public LineEps(final Coordinates<Double> pos, final double length,
			final int precision, final Color c) {
		super(pos.getX(), pos.getY(), precision, c);
		this.length = length;
	}

	public double getLength() {
		return length;
	}

	public void length(final double length) {
		this.length = length;
	}

	@Override
	public void draw(final DendrogramOrientation or) {
		double x1, y1, x2, y2;
		double xx1, yy1, xx2, yy2;
		double maxVal, minVal;
		int prec = getPrecision();

		// position adjusted to precision
		xx1 = this.getPosReal().getX();
		yy1 = MathUtils.round(this.getPosReal().getY(), prec);
		xx2 = this.getPosReal().getX();
		yy2 = MathUtils.round(this.getLength(), prec);

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

		EpsUtils.writeLine("gsave");
		EpsUtils.writeLine(EpsUtils.setRGBColor(
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f));
		if (or == DendrogramOrientation.NORTH) {
			EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + x1),
					(float) (EpsUtils.ymax + y1 - 0.5),
					(float) (EpsUtils.xmin + x2),
					(float) (EpsUtils.ymax + y2 + 0.5)));
		} else if (or == DendrogramOrientation.SOUTH) {
			EpsUtils.writeLine(EpsUtils.dLine((float) (EpsUtils.xmin + x1),
					(float) (EpsUtils.ymax + y1 + 0.5),
					(float) (EpsUtils.xmin + x2),
					(float) (EpsUtils.ymax + y2 - 0.5)));
		} else if (or == DendrogramOrientation.EAST) {
			EpsUtils.writeLine(EpsUtils.dLine(
					(float) (EpsUtils.xmin + x1 - 0.5),
					(float) (EpsUtils.ymax + y1),
					(float) (EpsUtils.xmin + x2 + 0.5),
					(float) (EpsUtils.ymax + y2)));
		} else if (or == DendrogramOrientation.WEST) {
			EpsUtils.writeLine(EpsUtils.dLine(
					(float) (EpsUtils.xmin + x1 + 0.5),
					(float) (EpsUtils.ymax + y1),
					(float) (EpsUtils.xmin + x2 - 0.5),
					(float) (EpsUtils.ymax + y2)));
		}
		EpsUtils.writeLine("grestore");

	}

	@Override
	public void draw(final Graphics2D g, final DendrogramOrientation or) {
	}

}
