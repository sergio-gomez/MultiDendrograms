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

package multidendrograms.dendrogram;

import multidendrograms.definitions.BoxContainer;
import multidendrograms.definitions.Coordinates;
import multidendrograms.types.DendrogramOrientation;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Transforms world coordinates to screen coordinates
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Scaling {

	private double screenOriginX, screenOriginY;
	private double screenWidth, screenHeight;
	private double worldMinX, worldMaxX, worldMinY, worldMaxY;

	public Scaling(final double worldMaxX, final double worldMaxY, final double worldMinX,
			final double worldMinY, final double screenWidth, final double screenHeight) {
		this.screenOriginX = 0.0;
		this.screenOriginY = 0.0;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.worldMinX = worldMinX;
		this.worldMaxX = worldMaxX;
		this.worldMinY = worldMinY;
		this.worldMaxY = worldMaxY;
	}

	public Scaling(BoxContainer box) {
		this.screenOriginX = box.getCornerX();
		this.screenOriginY = box.getCornerY();
		this.screenWidth = box.getWidth();
		this.screenHeight = box.getHeight();
		this.worldMinX = box.getValMinX();
		this.worldMaxX = box.getValMaxX();
		this.worldMinY = box.getValMinY();
		this.worldMaxY = box.getValMaxY();
	}

	public void setWidth(final double screenWidth) {
		this.screenWidth = screenWidth;
	}

	public double getWidth() {
		return this.screenWidth;
	}

	public void setHeight(final double screenHeight) {
		this.screenHeight = screenHeight;
	}

	public double getHeight() {
		return this.screenHeight;
	}

	public double getValuesWidth() {
		return (this.worldMaxX - this.worldMinX);
	}

	public double getValuesHeight() {
		return (this.worldMaxY - this.worldMinY);
	}

	public double getMinX() {
		return this.worldMinX;
	}

	public double getMaxX() {
		return this.worldMaxX;
	}

	public double getMinY() {
		return this.worldMinY;
	}

	public double getMaxY() {
		return this.worldMaxY;
	}

	public double scaleX(final double worldX) {
		return (this.screenWidth * (worldX - this.worldMinX) / (this.worldMaxX - this.worldMinX));
	}

	public double scaleY(final double worldY) {
		return (this.screenHeight * (worldY - this.worldMinY) / (this.worldMaxY - this.worldMinY));
	}

	public double transformX(final double worldX) {
		return (this.screenOriginX + scaleX(worldX));
	}

	public double transformY(final double worldY) {
		return (this.screenOriginY + scaleY(worldY));
	}

	public Coordinates<Double> transform(final Coordinates<Double> world, final DendrogramOrientation dendroOrientation) {
		double worldX = world.getX();
		double worldY = world.getY();
		if (dendroOrientation == DendrogramOrientation.EAST) {
			// inversion
			double x = worldY;
			worldY = this.worldMaxY - worldX;
			worldX = x;
		} else if (dendroOrientation == DendrogramOrientation.WEST) {
			// inversion
			double y = this.worldMaxY - worldX;
			worldX = this.worldMinX + (this.worldMaxX - worldY);
			worldY = y;
		} else if (dendroOrientation == DendrogramOrientation.SOUTH) {
			// translation
			worldY = this.worldMinY + (this.worldMaxY - worldY);
		}
		double screenX = transformX(worldX);
		double screenY = transformY(worldY);
		Coordinates<Double> screen = new Coordinates<Double>(screenX, screenY);
		return screen;
	}

}
