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

import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Axis EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class AxisEps {
	private double yMin, yMax;
	private final double dist;
	private Color color = Color.BLACK;
	private Scaling scal;

	public AxisEps(final double yMin, final double yMax, final double dist,
			final double ticks) {
		this.yMin = yMin;
		this.yMax = yMax;
		this.dist = dist;
	}

	public Scaling getScaling() {
		return scal;
	}

	public void setScaling(final Scaling scal) {
		this.scal = scal;
	}

	public double getYmax() {
		return yMax;
	}

	public void setYmax(final double yMax) {
		this.yMax = yMax;
	}

	public double getYmin() {
		return yMin;
	}

	public void setYmin(final double yMin) {
		this.yMin = yMin;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color c) {
		this.color = c;
	}

	public double getDist() {
		return this.dist;
	}

	public void draw(final DendrogramOrientation or, final SimilarityType simType,
			final int ticks) {
		double x0, x1, x2;
		double y0, y1, y2;
		double y, x, inc, n;

		EpsUtils.writeLine("gsave");
		EpsUtils.writeLine(EpsUtils.setRGBColor(
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f));

		if (or.equals(DendrogramOrientation.WEST) || or.equals(DendrogramOrientation.EAST)) {
			inc = this.getScaling().scaleX(yMin + dist);
			inc -= this.getScaling().scaleX(yMin);
		} else {
			inc = this.getScaling().scaleY(yMin + dist);
			inc -= this.getScaling().scaleY(yMin);
		}
		if (inc > 0) {
			x1 = this.getScaling().scaleX(0);
			y1 = this.getScaling().scaleY(0);

			if (or.equals(DendrogramOrientation.EAST) || or.equals(DendrogramOrientation.WEST)) {
				/* WEST and EAST */
				x0 = this.getScaling().transformX(yMin);
				x1 = x0;
				x2 = this.getScaling().transformX(yMax);

				y0 = this.getScaling().transformY(0);
				y1 = this.getScaling().transformY(1);
				y2 = this.getScaling().transformY(2);

				EpsUtils.writeLine(EpsUtils.dLine(
						(float) (EpsUtils.xmin + x0),
						(float) (EpsUtils.ymax + y1),
						(float) (EpsUtils.xmin + x2),
						(float) (EpsUtils.ymax + y1)));

				y0 = this.getScaling().transformY(0.5);
				y2 = this.getScaling().transformY(1.5);
				n = 0;
				if (simType.equals(SimilarityType.DISTANCE)) {
					/* DISTANCES */
					if (DendrogramOrientation.EAST.equals(or)) {
						x = x0;
						while (x <= (this.getScaling().transformX(yMax))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(0)),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(2))));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y0),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y2)));
							}
							x += inc;
							n++;
						}
					} else {
						x = x2;
						while (x >= (this.getScaling().transformX(yMin))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(0)),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(2))));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y0),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y2)));
							}
							x -= inc;
							n++;
						}
					}
				} else {
					/* WEIGHTS */
					if (DendrogramOrientation.EAST.equals(or)) {
						x = x2;
						while (x >= (this.getScaling().transformX(yMin))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(0)),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(2))));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y0),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y2)));
							}
							x -= inc;
							n++;
						}
					} else {
						x = x0;
						while (x <= (this.getScaling().transformX(yMax))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(0)),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + this
												.getScaling().transformY(2))));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y0),
										(float) (EpsUtils.xmin + x),
										(float) (EpsUtils.ymax + y2)));
							}
							x += inc;
							n++;
						}
					}
				}
			} else if (or.equals(DendrogramOrientation.SOUTH)
					|| or.equals(DendrogramOrientation.NORTH)) {
				/* SOUTH and NORTH */
				x0 = this.getScaling().transformX(0);
				x1 = this.getScaling().transformX(1);
				x2 = this.getScaling().transformX(2);

				y0 = this.getScaling().transformY(yMin);
				y1 = this.getScaling().transformY(0);
				y2 = this.getScaling().transformY(yMax);

				EpsUtils.writeLine(EpsUtils.dLine(
						(float) (EpsUtils.xmin + x1),
						(float) (EpsUtils.ymax + y0),
						(float) (EpsUtils.xmin + x1),
						(float) (EpsUtils.ymax + y2)));

				x0 = this.getScaling().transformX(0.5);
				x2 = this.getScaling().transformX(1.5);

				n = 0;
				if (simType.equals(SimilarityType.DISTANCE)) {
					/* DISTANCES */
					if (DendrogramOrientation.NORTH.equals(or)) {
						y = y0;
						while (y <= (this.getScaling().transformY(yMax))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(0)),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(2)),
										(float) (EpsUtils.ymax + y)));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x0),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + x2),
										(float) (EpsUtils.ymax + y)));
							}
							y += inc;
							n++;
						}
					} else {
						y = y2;
						while (y >= (this.getScaling().transformY(yMin))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(0)),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(2)),
										(float) (EpsUtils.ymax + y)));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x0),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + x2),
										(float) (EpsUtils.ymax + y)));
							}
							y -= inc;
							n++;
						}
					}

				} else {
					/* WEIGHTS */
					if (DendrogramOrientation.NORTH.equals(or)) {
						y = y2;
						while (y >= (this.getScaling().transformY(yMin))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(0)),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(2)),
										(float) (EpsUtils.ymax + y)));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x0),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + x2),
										(float) (EpsUtils.ymax + y)));
							}
							y -= inc;
							n++;
						}
					} else {
						y = y0;
						while (y <= (this.getScaling().transformY(yMax))) {
							if ((n % ticks) == 0) {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(0)),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + this
												.getScaling().transformX(2)),
										(float) (EpsUtils.ymax + y)));
							} else {
								EpsUtils.writeLine(EpsUtils.dLine(
										(float) (EpsUtils.xmin + x0),
										(float) (EpsUtils.ymax + y),
										(float) (EpsUtils.xmin + x2),
										(float) (EpsUtils.ymax + y)));
							}
							y += inc;
							n++;
						}
					}
				}
			}
		}

		EpsUtils.writeLine("grestore");
	}

}
