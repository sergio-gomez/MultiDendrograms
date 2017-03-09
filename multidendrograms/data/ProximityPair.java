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

package multidendrograms.data;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Two elements and the proximity value between them
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ProximityPair<Element> {

	private Element element1;
	private Element element2;
	private double proximity;

	public ProximityPair(final Element element1, final Element element2, final double proximity) {
		this.element1 = element1;
		this.element2 = element2;
		this.proximity = proximity;
	}

	public Element getElement1() {
		return this.element1;
	}

	public Element getElement2() {
		return this.element2;
	}

	public double getProximity() {
		return this.proximity;
	}

	@Override
	public String toString() {
		return (this.element1.toString() + "\t" + this.element2.toString() + "\t" + this.proximity);
	}

}
