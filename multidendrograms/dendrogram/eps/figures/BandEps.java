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

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Band EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class BandEps extends Figure {
	private double height, width;
	private boolean filled;

	public BandEps(final double x, final double y, final double height,
			final double width, final int precision) {
		super(x, y, precision, Color.GRAY);
		this.height = height;
		this.width = width;
		this.filled = true;
	}

	public BandEps(final double x, final double y, final double height,
			final double width, final int precision, final Color c) {
		super(x, y, precision, c);
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

	public void setFilled(final boolean w) {
		this.filled = w;
	}

	@Override
	public void draw(final DendrogramOrientation or) {
		double x1, y1, w, h;
		double xx1, yy1, ww, hh;
		int prec = getPrecision();

		xx1 = this.getPosReal().getX();
		yy1 = MathUtils.round(this.getPosReal().getY(), prec);
		ww = this.getPosReal().getX() + this.getWidth();
		hh = MathUtils
				.round(this.getPosReal().getY() + this.getHeight(), prec);

		if (or == DendrogramOrientation.EAST) {
			y1 = yy1;
			yy1 = (this.getScaling().getMaxY() - xx1)
					+ this.getScaling().getMinY();
			xx1 = y1;

			h = hh;
			hh = (this.getScaling().getMaxY() - ww)
					+ this.getScaling().getMinY();
			ww = h;

			y1 = yy1;
			yy1 = hh;
			hh = y1;
		} else if (or == DendrogramOrientation.WEST) {
			// rotation of P(x,y)
			x1 = xx1;
			xx1 = (this.getScaling().getMaxX() - yy1)
					+ this.getScaling().getMinX();
			yy1 = (this.getScaling().getMaxY() - x1)
					+ this.getScaling().getMinY();

			// rotation of P(x',y')
			w = ww;
			ww = (this.getScaling().getMaxX() - hh)
					+ this.getScaling().getMinX();
			hh = (this.getScaling().getMaxY() - w)
					+ this.getScaling().getMinY();

			// translation of origin
			y1 = yy1;
			yy1 = hh;
			hh = y1;

			x1 = xx1;
			xx1 = ww;
			ww = x1;

		} else if (or == DendrogramOrientation.SOUTH) {
			// rotation of P(x,y)
			yy1 = (this.getScaling().getMaxY() - yy1)
					+ this.getScaling().getMinY();

			hh = (this.getScaling().getMaxY() - hh)
					+ this.getScaling().getMinY();
			h = yy1;
			yy1 = hh;
			hh = h;
		} else {
			// nothing to do in North
		}

		// scaling
		x1 = this.getScaling().transformX(xx1);
		y1 = this.getScaling().transformY(yy1);
		w = this.getScaling().transformX(ww);
		h = this.getScaling().transformY(hh);

		// drawing
		EpsUtils.writeLine("gsave");
		if (filled) {
			EpsUtils.writeLine(EpsUtils.setRGBColor(
					this.getColor().getRed() / 255f,
					this.getColor().getGreen() / 255f, this.getColor()
							.getBlue() / 255f));
			EpsUtils
					.writeLine(EpsUtils.fRectangle(
							(float) (EpsUtils.xmin + x1),
							(float) (EpsUtils.ymax + y1),
							(float) (EpsUtils.xmin + w),
							(float) (EpsUtils.ymax + h)));
		} else {
			Color color = Color.BLACK;
			EpsUtils.writeLine(EpsUtils.setRGBColor(color.getRed() / 255f,
					color.getGreen() / 255f, color.getBlue() / 255f));
			EpsUtils
					.writeLine(EpsUtils.dRectangle(
							(float) (EpsUtils.xmin + x1),
							(float) (EpsUtils.ymax + y1),
							(float) (EpsUtils.xmin + w),
							(float) (EpsUtils.ymax + h)));
		}
		EpsUtils.writeLine("grestore");

	}

	@Override
	public void draw(Graphics2D g, DendrogramOrientation or) {
	}
}
