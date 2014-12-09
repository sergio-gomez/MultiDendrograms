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

package multidendrograms.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Rounding and format of numeric values
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class MathUtils {

	static NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

	public static double round(final double num, final int prec) {
		final double epsilon = 1.0e-4;
		double r, rs, x, result;
		double factor;

		factor = Math.pow(10, prec);
		x = num * factor;
		r = Math.round(x);
		if (r < x) {
			rs = Math.round(x + epsilon);
			if (rs - r > 0.5) {
				r = rs;
			}
		}
		return r / factor;
	}

	public static String format(final double num, final int prec) {
		nf.setMinimumFractionDigits(prec);
		nf.setMaximumFractionDigits(prec);
		nf.setGroupingUsed(false);

		return nf.format(num);
	}

}
