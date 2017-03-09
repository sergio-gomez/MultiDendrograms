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

package multidendrograms.core.utils;

/******************************************************************************
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Rounding of numeric values
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 ******************************************************************************/
public class MathUtils {

	public static double round(double number, int precision) {
		double factor = Math.pow(10, precision);
		double x = number * factor;
		double r = Math.round(x);
		if (r < x) {
			double epsilon = 1.0e-4;
			double rs = Math.round(x + epsilon);
			if (rs - r > 0.5) {
				r = rs;
			}
		}
		return r / factor;
	}

}
