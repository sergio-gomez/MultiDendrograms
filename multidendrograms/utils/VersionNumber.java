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

import java.util.StringTokenizer;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Comparison of version numbers
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class VersionNumber {

	public final static String DEFAULT_VERSION = "0.0.0";

	private String versionStr = DEFAULT_VERSION;
	private int versionL1 = 0;
	private int versionL2 = 0;
	private int versionL3 = 0;

	public VersionNumber() {
	}

	public VersionNumber(final String v) {
		setVersion(v);
	}

	public void setVersion(String versionStr) {
		this.versionStr = versionStr;
		setVersionL1(0);
		setVersionL2(0);
		setVersionL3(0);
		StringTokenizer tok = new StringTokenizer(versionStr, ".");
		try {
			if (tok.hasMoreTokens()) {
				setVersionL1(Integer.parseInt(tok.nextToken()));
			}
			if (tok.hasMoreTokens()) {
				setVersionL2(Integer.parseInt(tok.nextToken()));
			}
			if (tok.hasMoreTokens()) {
				setVersionL3(Integer.parseInt(tok.nextToken()));
			}
		} catch (NumberFormatException e) {
		}
	}

	public String getVersion() {
		return versionStr;
	}

	public void setVersionL1(int versionL1) {
		this.versionL1 = versionL1;
	}

	public int getVersionL1() {
		return versionL1;
	}

	public void setVersionL2(int versionL2) {
		this.versionL2 = versionL2;
	}

	public int getVersionL2() {
		return versionL2;
	}

	public void setVersionL3(int versionL3) {
		this.versionL3 = versionL3;
	}

	public int getVersionL3() {
		return versionL3;
	}

	public boolean newerThan(VersionNumber vn) {
		return ((versionL1 > vn.versionL1) ||
				((versionL1 == vn.versionL1) && (versionL2 > vn.versionL2)) ||
				((versionL1 == vn.versionL1) && (versionL2 == vn.versionL2) && (versionL3 > vn.versionL3)));
	}

	public static boolean newer(VersionNumber vn1, VersionNumber vn2) {
		return vn1.newerThan(vn2);
	}
}
