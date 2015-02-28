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
import java.awt.geom.Ellipse2D;

import multidendrograms.types.DendrogramOrientation;
import multidendrograms.definitions.Coordinates;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Node figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Node extends Figure {
	private double radius;
	private String name = "";

	public Node(final Coordinates<Double> pos, final double radius, final int precision, final String name) {
		super(pos.getX(), pos.getY(), precision);
		this.radius = radius;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(final double radius) {
		this.radius = radius;
	}

	@Override
	public void draw(final Graphics2D g, final DendrogramOrientation or) {
		double x, y, r1, r2, rr;

		rr = this.getRadius();
		r1 = this.getScaling().scaleX(rr);
		r2 = this.getScaling().scaleY(rr);
		rr = (r1 <= r2) ? r1 : r2;

		if ((or == DendrogramOrientation.EAST) || (or == DendrogramOrientation.WEST)) {
			y = this.getScaling().getValuesHeight() - this.getPosReal().getX();
			x = this.getScaling().transformX(0d);
			y = this.getScaling().transformY(y);
			y -= (rr / 2d);
		} else {
			x = this.getScaling().transformX(this.getPosReal().getX());
			y = this.getScaling().transformY(0d);
			x -= (rr / 2d);
		}

		g.fill(new Ellipse2D.Double(x, y, rr, rr));
	}

	@Override
	public void draw(DendrogramOrientation or) {
	}
}