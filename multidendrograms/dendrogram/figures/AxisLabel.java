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
import java.text.NumberFormat;
import java.util.Locale;

import multidendrograms.dendrogram.Scaling;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Name of distance label figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class AxisLabel {
	private final double minVal;
	private double maxVal, width;
	private final double dist, ticks;
	private Color color = Color.BLACK;
	private Font font;
	private Scaling scal;
	private final int precision;

	public AxisLabel(final double minVal, final double maxVal,
			final double width, final double dist, final double ticks,
			final int prec) {
		this.dist = dist;
		this.ticks = ticks;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.width = width;
		this.precision = prec;
	}

	public Scaling getScaling() {
		return scal;
	}

	public void setScaling(final Scaling scal) {
		this.scal = scal;
	}

	public double getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(final double maxVal) {
		this.maxVal = maxVal;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(final double width) {
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

	public void draw(final Graphics2D g, final DendrogramOrientation or,
			final SimilarityType simType) {
		double y, x, inc, max, min;
		float posX, posY;

		final Color color_original = g.getColor();
		final Font font_original = g.getFont();
		final Font ft = this.getFont();
		final Font fr;
		final AffineTransform rot = new AffineTransform();
		final FontRenderContext renderContext = new FontRenderContext(null,
				true, true);
		TextLayout tl;
		String txt;

		g.setColor(this.getColor());

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
						if (or.equals(DendrogramOrientation.WEST)) {
							x = min + (max - x);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, fr, renderContext);

						posX = (float) (x + (tl.getBounds().getWidth() / 2));
						posY = (float) y;
						g.scale(1, -1);
						tl.draw(g, posX, -posY);
						g.scale(1, -1);
						h += (ticks * dist);
					}
				} else {
					h = 0;
					while ((maxVal - h) >= minVal) {
						num = maxVal - h;
						x = this.getScaling().transformX(num);
						if (or.equals(DendrogramOrientation.WEST)) {
							x = min + (max - x);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, fr, renderContext);

						posX = (float) (x + (tl.getBounds().getWidth() / 2));
						posY = (float) y;
						g.scale(1, -1);
						tl.draw(g, posX, -posY);
						g.scale(1, -1);
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
						if (or.equals(DendrogramOrientation.SOUTH)) {
							y = min + (max - y);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);

						posX = (float) (x + (maxx - tl.getBounds().getMaxX()));
						posY = (float) (y - tl.getBounds().getHeight() / 2);

						g.scale(1, -1);
						tl.draw(g, posX, -posY);
						g.scale(1, -1);
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
						if (or.equals(DendrogramOrientation.SOUTH)) {
							y = min + (max - y);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);

						posX = (float) (x + (maxx - tl.getBounds().getMaxX()));
						posY = (int) (y - tl.getBounds().getHeight() / 2);

						g.scale(1, -1);
						tl.draw(g, posX, -posY);
						g.scale(1, -1);
						h += (ticks * dist);
					}
				}
			}
		}

		g.setColor(color_original);
		g.setFont(font_original);
	}

}
