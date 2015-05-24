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
import multidendrograms.types.PlotType;
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

	private DendrogramOrientation dendroOrientation;
	private Coordinates<Double> coordinates;
	private Color color = Color.BLACK;
	private Scaling scaling;

	public Figure(final double x, final double y) {
		this.coordinates = new Coordinates<Double>(x, y);
	}

	public Figure(final double x, final double y, final Color color) {
		this.coordinates = new Coordinates<Double>(x, y);
		this.color = color;
	}

	public DendrogramOrientation getDendrogramOrientation() {
		return this.dendroOrientation;
	}

	public void setDendrogramOrientation(final DendrogramOrientation dendroOrientation) {
		this.dendroOrientation = dendroOrientation;
	}

	public Coordinates<Double> getPosReal() {
		return this.coordinates;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public Scaling getScaling() {
		return this.scaling;
	}

	public void setScaling(final Scaling scaling) {
		this.scaling = scaling;
	}

	public abstract void draw(final PlotType plotType, final Graphics2D graphics2D);

}
