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

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;

import multidendrograms.dendrogram.figures.Band;
import multidendrograms.dendrogram.figures.Line;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Ensure only figures inside the graphical area are stored
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Clipping {

	private final double maxVal, minVal;
	private final int precision;
	private final SimilarityType simType;

	public Clipping(final double maxVal, final double minVal,
			final SimilarityType simType, final int precision) {
		this.maxVal = maxVal;
		this.minVal = minVal;
		this.simType = simType;
		this.precision = precision;
	}

	public LinkedList<Band> clipBands(final LinkedList<Band> bandsList) {
		double posYinf, posYsup;
		double x, y, height, width;
		Color c;

		final LinkedList<Band> clippedBands = new LinkedList<Band>();
		final Iterator<Band> it = bandsList.iterator();

		while (it.hasNext()) {
			final Band rect = it.next();

			x = rect.getPosReal().getX();
			y = rect.getPosReal().getY();
			height = rect.getHeight();
			width = rect.getWidth();
			c = rect.getColor();

			if (simType.equals(SimilarityType.DISTANCE)) {
				posYinf = y;
				posYsup = y + height;
			} else {
				posYinf = y - height;
				posYsup = y;
			}

			if ((posYinf <= maxVal) && (posYsup >= minVal)) {
				if (posYinf < minVal) {
					posYinf = minVal;
				}

				if (posYsup > maxVal) {
					posYsup = maxVal;
				}
				y = posYinf;
				height = (posYsup - posYinf);
				clippedBands.add(new Band(x, y, height, width, precision, c));

			} else if ((posYinf <= minVal) && (posYsup >= maxVal)) {
				y = minVal;
				height = (maxVal - minVal);
				clippedBands.add(new Band(x, y, height, width, precision, c));
			}

		}

		return clippedBands;
	}

	public LinkedList<Line> clipLines(final LinkedList<Line> linesList) {
		double posYinf, posYsup, tmp;

		final LinkedList<Line> clippedLines = new LinkedList<Line>();
		final Iterator<Line> it = linesList.iterator();
		while (it.hasNext()) {
			final Line lin = it.next();
			posYinf = lin.getPosReal().getY();
			posYsup = lin.getLength();
			if (posYinf > posYsup) {
				tmp = posYinf;
				posYinf = posYsup;
				posYsup = tmp;
				lin.setLength(posYsup);
				lin.getPosReal().setY(posYinf);
			}

			if ((posYinf < maxVal) && (posYsup > minVal)) {
				if (posYinf < minVal) {
					lin.getPosReal().setY(minVal);
				}
				if (posYsup > maxVal) {
					lin.setLength(maxVal);
				}
				clippedLines.add(lin);
			} else if ((posYinf <= minVal) && (posYsup >= maxVal)) {
				lin.getPosReal().setY(minVal);
				lin.setLength(maxVal);
				clippedLines.add(lin);
			}
		}

		return clippedLines;
	}
}
