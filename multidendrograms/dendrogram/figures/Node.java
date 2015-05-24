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

import multidendrograms.definitions.Coordinates;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.PlotType;

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
	private String name;
	private Scaling scalingDendro;

	public Node(final double x, final double y, final double radius, final String name) {
		super(x, y);
		this.radius = radius;
		this.name = name;
	}

	public double getRadius() {
		return this.radius;
	}

	public String getName() {
		return this.name;
	}

	public void setScalingDendrogram(final Scaling scalingDendro) {
		this.scalingDendro = scalingDendro;
	}

	@Override
	public void draw(final PlotType plotType, final Graphics2D graphics2D) {
		double worldX = getPosReal().getX();
		double worldY = getPosReal().getY();
		Coordinates<Double> world = new Coordinates<Double>(worldX, worldY);
		DendrogramOrientation dendroOrientation = getDendrogramOrientation();
		Coordinates<Double> screen = this.scalingDendro.transform(world, dendroOrientation);
		double screenX = screen.getX();
		double screenY = screen.getY();
		
		Scaling scalingBullets = getScaling();
		double r1 = scalingBullets.scaleX(this.radius);
		double r2 = scalingBullets.scaleY(this.radius);
		double rr = Math.min(r1, r2);
		if (plotType.equals(PlotType.PANEL)) {
			screenX = screenX - (rr / 2d);
			screenY = screenY - (rr / 2d);
			graphics2D.fill(new Ellipse2D.Double(screenX, screenY, rr, rr));
		} else if (plotType.equals(PlotType.EPS)) {
			EpsUtils.writeLine(EpsUtils.fCircle((float) (EpsUtils.xmin + screenX), (float) (EpsUtils.ymax + screenY), (float) rr));
		}
	}

}
