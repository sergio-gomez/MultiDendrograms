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

package multidendrograms.definitions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Defines an area on the screen: position, size and range of accepted values
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class BoxContainer {

	private double cornerX = 0.0, cornerY = 0.0;

	private double width = 0.0, height = 0.0;

	private double maxValX = 0.0, maxValY = 0.0;

	private double minValX = 0.0, minValY = 0.0;

	public BoxContainer() {
	}

	public BoxContainer(final double cornerX, final double cornerY,
			final double width, final double height, final double maxValX,
			final double maxValY, final double minValX,
			final double minValY) {
		this.cornerX = cornerX;
		this.cornerY = cornerY;
		this.width = width;
		this.height = height;
		this.maxValX = maxValX;
		this.maxValY = maxValY;
		this.minValX = minValX;
		this.minValY = minValY;
	}

	public BoxContainer(final double cornerX, final double cornerY,
			final double width, final double height) {
		this.cornerX = cornerX;
		this.cornerY = cornerY;
		this.width = width;
		this.height = height;
	}

	public double getCornerX() {
		return cornerX;
	}

	public void setCornerX(final double cornerX) {
		this.cornerX = cornerX;
	}

	public double getCornerY() {
		return cornerY;
	}

	public void setCornerY(final double cornerY) {
		this.cornerY = cornerY;
	}

	public void increaseCornerY(final double value) {
		this.cornerY = this.cornerY + value;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(final double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(final double height) {
		this.height = height;
	}

	public double getValMaxX() {
		return maxValX;
	}

	public void setValMaxX(final double maxValX) {
		this.maxValX = maxValX;
	}

	public double getValMaxY() {
		return maxValY;
	}

	public void setValMaxY(final double maxValY) {
		this.maxValY = maxValY;
	}

	public double getValMinX() {
		return minValX;
	}

	public void setValMinX(final double minValX) {
		this.minValX = minValX;
	}

	public double getValMinY() {
		return minValY;
	}

	public void setValMinY(final double minValY) {
		this.minValY = minValY;
	}
}
