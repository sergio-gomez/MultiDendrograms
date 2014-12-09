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

import multidendrograms.dendrogram.Scaling;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.definitions.Coordinates;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Abstract class for all figures to be drawn in the graphical area
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public abstract class Figure {

	private Color color = Color.BLACK;
	private Coordinates<Double> coords;
	private Scaling scale;
	private final int precision;

	public Figure(final double x, final double y, final int precision) {
		this.coords = new Coordinates<Double>(x, y);
		this.precision = precision;
	}

	public Figure(final double x, final double y, final int p, final Color c) {
		this.coords = new Coordinates<Double>(x, y);
		this.color = c;
		this.precision = p;
	}

	public Coordinates<Double> getPosReal() {
		return this.coords;
	}

	public void setPosReal(final Coordinates<Double> pos) {
		this.coords = pos;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(final Color c) {
		this.color = c;
	}

	public Scaling getScaling() {
		return this.scale;
	}

	public void setScaling(final Scaling s) {
		this.scale = s;
	}

	public int getPrecision() {
		return this.precision;
	}

	public abstract void draw(DendrogramOrientation or);

	public abstract void draw(Graphics2D g, DendrogramOrientation or);
}
