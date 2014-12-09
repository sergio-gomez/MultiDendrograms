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

	private double width = 0.0, height = 0.0;
	private double minX = 0.0, maxX = 0.0, minY = 0.0, maxY = 0.0;
	private double factorX = 1.0, factorY = 1.0;
	private double translationX = 0.0, translationY = 0.0;

	public Scaling() {
	}

	public Scaling(final double maxX, final double maxY, final double minX,
			final double minY, final double width, final double heigth) {
		this.setWidth(width);
		this.setHeigth(heigth);
		this.setValuesRange(maxX, maxY, minX, minY);
	}

	private void calculateFactors() {
		factorX = width / (maxX - minX);
		factorY = height / (maxY - minY);
	}

	public void setWidth(final double width) {
		this.width = width;
		this.calculateFactors();
	}

	public void setHeigth(final double heigth) {
		this.height = heigth;
		this.calculateFactors();
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getValuesWidth() {
		return maxX - minX;
	}

	public double getValuesHeight() {
		return maxY - minY;
	}

	public double getMinX() {
		return minX;
	}

	public double getMinY() {
		return minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setValuesRange(final double maxX, final double maxY,
			final double minX, final double minY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
		this.calculateFactors();
	}

	public void setTranslationX(final double translationX) {
		this.translationX = translationX;
	}

	public void setTranslationY(final double translationY) {
		this.translationY = translationY;
	}

	public double scaleX(final double x) {
		return (factorX * (x - minX));
	}

	public double scaleY(final double y) {
		return (factorY * (y - minY));
	}

	public double transformX(final double x) {
		return (translationX + this.scaleX(x));
	}

	public double transformY(final double y) {
		return (translationY + (this.scaleY(y)));
	}
}
