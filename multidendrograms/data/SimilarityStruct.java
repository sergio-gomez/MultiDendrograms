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
 * Two elements related by a value
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class SimilarityStruct<element> {

	private element c1;
	private element c2;
	private double value;

	public SimilarityStruct(final element c1, final element c2, final double value) {
		this.c1 = c1;
		this.c2 = c2;
		this.value = value;
	}

	public element getC1() {
		return this.c1;
	}

	public void setC1(final element c1) {
		this.c1 = c1;
	}

	public element getC2() {
		return this.c2;
	}

	public void setC2(final element c2) {
		this.c2 = c2;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(final double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return (c1.toString() + "\t" + c2.toString() + "\t" + this.value);
	}

}
