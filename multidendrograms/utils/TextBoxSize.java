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
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import multidendrograms.definitions.Dimensions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculation of width and height of text
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class TextBoxSize {

	AffineTransform rot;

	Font font = new Font("Helvetica", Font.BOLD, 10);

	String s;

	public TextBoxSize(final Font font) {
		this.font = font;
	}

	public Dimensions<Double> getBox(final double angle, final String s) {
		Dimensions<Double> dim;
		TextLayout tl;
		final AffineTransform rot = new AffineTransform();
		final FontRenderContext renderContext = new FontRenderContext(null, true, true);
		final Font ft = font;
		Font fr;

		rot.rotate(Math.toRadians(angle));
		fr = ft.deriveFont(rot);

		tl = new TextLayout(s, fr, renderContext);

		dim = new Dimensions<Double>(tl.getBounds().getWidth(), tl.getBounds().getHeight());
		return dim;
	}

	public Dimensions<Double> getBoxPositiveNumber(final double angle,
			final int num_ent, final int num_dec) {
		String tmp = "";

		for (int n = 0; n < num_ent; n++) {
			tmp += 0;
		}

		if (num_dec > 0) {
			tmp += ",";
			for (int n = 0; n < num_dec; n++) {
				tmp += "0";
			}
		}
		return this.getBox(angle, tmp);
	}

	public Dimensions<Double> getBoxNegativeNumber(final double angle,
			final int num_ent, final int num_dec) {
		String tmp = "-";

		for (int n = 0; n < num_ent; n++) {
			tmp += 0;
		}

		if (num_dec > 0) {
			tmp += ",";
			for (int n = 0; n < num_dec; n++) {
				tmp += "0";
			}
		}
		return this.getBox(angle, tmp);
	}
}
