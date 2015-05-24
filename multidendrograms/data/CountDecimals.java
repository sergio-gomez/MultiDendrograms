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
 * Stores the largest number of decimal digits in a sequence of numbers
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class CountDecimals {

	private int maxNumDecimals = 0;

	public int inValue(final double value) {
		double absValue = Math.abs(value);
		long rndValue = Math.round(absValue);
		if (rndValue != absValue) {
			String sRndValue = String.valueOf(rndValue);
			String sAbsValue = String.valueOf(absValue);
			int numDecimals = sAbsValue.length() - sRndValue.length() - 1;
			this.maxNumDecimals = Math.max(this.maxNumDecimals, numDecimals);
		}
		return this.maxNumDecimals;
	}

	public void restart() {
		this.maxNumDecimals = 0;
	}

	public int getPrecision() {
		return this.maxNumDecimals;
	}
}
