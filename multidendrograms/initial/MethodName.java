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

package multidendrograms.initial;

import multidendrograms.errors.MethodError;
import multidendrograms.types.MethodType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Identification of the clustering algorithm
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public abstract class MethodName {

	public static String toName(MethodType methodType) {
		String str = "";
		switch (methodType) {
		case VERSATILE_LINKAGE:
			str = "Versatile Linkage";
			break;
		case SINGLE_LINKAGE:
			str = "Single Linkage";
			break;
		case COMPLETE_LINKAGE:
			str = "Complete Linkage";
			break;
		case ARITHMETIC_LINKAGE:
			str = "Arithmetic Linkage";
			break;
		case GEOMETRIC_LINKAGE:
			str = "Geometric Linkage";
			break;
		case HARMONIC_LINKAGE:
			str = "Harmonic Linkage";
			break;
		case CENTROID:
			str = "Centroid";
			break;
		case WARD:
			str = "Ward";
			break;
		case BETA_FLEXIBLE:
			str = "Beta Flexible";
			break;
		default:
			str = "";
			break;
		}
		return str;
	}

	public static String toShortName(MethodType methodType) {
		String str = "";
		switch (methodType) {
		case VERSATILE_LINKAGE:
			str = "vl";
			break;
		case SINGLE_LINKAGE:
			str = "sl";
			break;
		case COMPLETE_LINKAGE:
			str = "cl";
			break;
		case ARITHMETIC_LINKAGE:
			str = "al";
			break;
		case GEOMETRIC_LINKAGE:
			str = "gl";
			break;
		case HARMONIC_LINKAGE:
			str = "hl";
			break;
		case CENTROID:
			str = "cd";
			break;
		case WARD:
			str = "wd";
			break;
		case BETA_FLEXIBLE:
			str = "bf";
			break;
		default:
			str = "";
			break;
		}
		return str;
	}

	public static MethodType toMethodType(String sMethodName) throws MethodError {
		MethodType methodType;
		String str = sMethodName.toUpperCase();
		if      (str.equals("VL") || str.equals("VERSATILE_LINKAGE"))
			methodType = MethodType.VERSATILE_LINKAGE;
		else if (str.equals("SL") || str.equals("SINGLE_LINKAGE"))
			methodType = MethodType.SINGLE_LINKAGE;
		else if (str.equals("CL") || str.equals("COMPLETE_LINKAGE"))
			methodType = MethodType.COMPLETE_LINKAGE;
		else if (str.equals("AL") || str.equals("ARITHMETIC_LINKAGE"))
			methodType = MethodType.ARITHMETIC_LINKAGE;
		else if (str.equals("GL") || str.equals("GEOMETRIC_LINKAGE"))
			methodType = MethodType.GEOMETRIC_LINKAGE;
		else if (str.equals("HL") || str.equals("HARMONIC_LINKAGE"))
			methodType = MethodType.HARMONIC_LINKAGE;
		else if (str.equals("CD") || str.equals("CENTROID"))
			methodType = MethodType.CENTROID;
		else if (str.equals("WD") || str.equals("WARD"))
			methodType = MethodType.WARD;
		else if (str.equals("BF") || str.equals("BETA_FLEXIBLE"))
			methodType = MethodType.BETA_FLEXIBLE;
		else
		  throw new MethodError("Error: unknown method '" + str + "'");
		return methodType;
	}

}
