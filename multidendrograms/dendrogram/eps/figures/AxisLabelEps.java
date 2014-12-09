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
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.Locale;

import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Name of distance label EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class AxisLabelEps {
	private final double minVal;
	private double maxVal, width;
	private final double dist, ticks;
	private Color color = Color.BLACK;
	private Font font;
	private Scaling scal;
	private final int precision; // precision

	public AxisLabelEps(final double minVal, final double maxVal,
			final double width, final double dist, final double tics,
			final int prec) {
		this.dist = dist;
		this.ticks = tics;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.width = width;
		this.precision = prec;
	}

	public Scaling getScaling() {
		return scal;
	}

	public void setScaling(final Scaling e) {
		this.scal = e;
	}

	public double getAlcada() {
		return maxVal;
	}

	public void setAlcada(final double maxVal) {
		this.maxVal = maxVal;
	}

	public double getAmple() {
		return width;
	}

	public void setAmple(final double width) {
		this.width = width;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color c) {
		this.color = c;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(final Font f) {
		this.font = f;
	}

	public void dibuixa(final DendrogramOrientation or, final SimilarityType simType) {
		double y, x, inc, max, min;
		int posX, posY;

		// final Color color_original = g.getColor();
		// final Font font_original = g.getFont();
		final Font ft = this.getFont();
		final Font fr;
		final AffineTransform rot = new AffineTransform();
		final FontRenderContext renderContext = new FontRenderContext(null,
				true, true);
		TextLayout tl;
		String txt;

		EpsUtils.writeLine("gsave");
		EpsUtils.writeLine(EpsUtils.setRGBColor(
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f));
		if (this.getFont().getPSName().equals("Dialog.plain"))
			EpsUtils.writeLine(EpsUtils.scaleSetFont("/ArialMT", this
					.getFont().getSize()));
		else
			EpsUtils.writeLine(EpsUtils.scaleSetFont("/"
					+ this.getFont().getPSName(), this.getFont().getSize()));

		inc = this.getScaling().scaleY(minVal + dist);
		inc -= this.getScaling().scaleY(minVal);

		if (inc > 0) { // always inc > 0

			NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
			nf.setMinimumFractionDigits(precision);
			nf.setMaximumFractionDigits(precision);
			nf.setGroupingUsed(false);

			if (or.equals(DendrogramOrientation.WEST) || or.equals(DendrogramOrientation.EAST)) {
				rot.rotate(Math.toRadians(-90));
				fr = ft.deriveFont(rot);
				y = this.getScaling().transformY(0);
				min = x = this.getScaling().transformX(minVal);
				max = x = this.getScaling().transformX(maxVal);

				double h, num;
				h = ticks * dist;

				if (simType.equals(SimilarityType.DISTANCE)) {
					h = 0;
					while ((minVal + h) <= maxVal) {
						num = minVal + h;
						x = this.getScaling().transformX(num);
						if (or.equals(DendrogramOrientation.WEST))
							x = min + (max - x);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, fr, renderContext);

						posX = (int) x + (int) (tl.getBounds().getWidth() / 2);
						posY = (int) y;

						EpsUtils.writeLine(EpsUtils.bottomLeftTextRotated(
								(EpsUtils.xmin + posX),
								(EpsUtils.ymax + posY), 90, txt));

						h += (ticks * dist);
					}
				} else {
					h = 0;
					while ((maxVal - h) >= minVal) {
						num = maxVal - h;
						x = this.getScaling().transformX(num);
						if (or.equals(DendrogramOrientation.WEST))
							x = min + (max - x);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, fr, renderContext);

						posX = (int) x + (int) (tl.getBounds().getWidth() / 2);
						posY = (int) y;

						EpsUtils.writeLine(EpsUtils.bottomLeftTextRotated(
								(EpsUtils.xmin + posX),
								(EpsUtils.ymax + posY), 90, txt));

						h += (ticks * dist);
					}
				}

			} else if (or.equals(DendrogramOrientation.NORTH)
					|| or.equals(DendrogramOrientation.SOUTH)) {
				min = y = this.getScaling().transformY(minVal);
				max = y = this.getScaling().transformY(maxVal);
				x = this.getScaling().transformX(0);
				double h, num;
				h = ticks * dist;
				h = minVal;

				if (simType.equals(SimilarityType.DISTANCE)) {

					double maxx = 0.0;
					while (h <= maxVal) {
						num = h;
						y = this.getScaling().transformY(num);
						if (or.equals(DendrogramOrientation.SOUTH)) {
							y = min + (max - y);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);
						if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
							maxx = tl.getBounds().getMaxX();
						h += (ticks * dist);
					}

					h = minVal;
					while (h <= maxVal) {
						num = h;
						y = this.getScaling().transformY(num);
						if (or.equals(DendrogramOrientation.SOUTH))
							y = min + (max - y);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);

						posX = (int) (x + (maxx - tl.getBounds().getMaxX()));
						posY = (int) (y - tl.getBounds().getHeight() / 2);

						EpsUtils.writeLine(EpsUtils.bottomLeftText(
								(EpsUtils.xmin + posX),
								(EpsUtils.ymax + posY), txt));

						h += (ticks * dist);
					}
				} else {

					h = 0;
					double maxx = 0.0;
					while (MathUtils.round((maxVal - h), 10) >= minVal) {
						num = h;
						y = this.getScaling().transformY(num);
						if (or.equals(DendrogramOrientation.SOUTH)) {
							y = min + (max - y);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);
						if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
							maxx = tl.getBounds().getMaxX();
						h += (ticks * dist);
					}

					h = 0;
					while (MathUtils.round((maxVal - h), 10) >= minVal) {
						num = maxVal - h;
						y = this.getScaling().transformY(num);
						if (or.equals(DendrogramOrientation.SOUTH))
							y = min + (max - y);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);

						posX = (int) (x + (maxx - tl.getBounds().getMaxX()));
						posY = (int) (y - tl.getBounds().getHeight() / 2);

						EpsUtils.writeLine(EpsUtils.bottomLeftText(
								(EpsUtils.xmin + posX),
								(EpsUtils.ymax + posY), txt));

						h += (ticks * dist);
					}
				}
			}
		}

		EpsUtils.writeLine("grestore");
	}

}
