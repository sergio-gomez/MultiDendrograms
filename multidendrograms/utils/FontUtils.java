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

import java.awt.Font;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Modify fonts
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FontUtils {

	public static Font addStyle(Font font, final int style) {
		return font.deriveFont(font.getStyle() | style);
	}

	public static Font incSize(Font font, final float inc) {
		return font.deriveFont(font.getSize() + inc);
	}

	public static Font addStyleIncSize(Font font, final int style, final float inc) {
		return font.deriveFont(font.getStyle() | style, font.getSize() + inc);
	}
}
