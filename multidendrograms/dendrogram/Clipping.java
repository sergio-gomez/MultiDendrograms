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
import multidendrograms.types.ProximityType;

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

	private final ProximityType simType;
	private final double minValue, maxValue;

	public Clipping(final ProximityType simType, final double minValue, final double maxValue) {
		this.simType = simType;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public LinkedList<Line> clipLines(final LinkedList<Line> lines) {
		final LinkedList<Line> clippedLines = new LinkedList<Line>();
		final Iterator<Line> iter = lines.iterator();
		while (iter.hasNext()) {
			final Line line = iter.next();
			double minY = line.getPosReal().getY();
			double maxY = line.getLength();
			if (minY > maxY) {
				double tmp = minY;
				minY = maxY;
				maxY = tmp;
				line.getPosReal().setY(minY);
				line.setLength(maxY);
			}
			if ((this.minValue < maxY) && (minY < this.maxValue)) {
				if (minY < this.minValue) {
					line.getPosReal().setY(this.minValue);
				}
				if (this.maxValue < maxY) {
					line.setLength(this.maxValue);
				}
				clippedLines.add(line);
			} else if ((minY <= this.minValue) && (this.maxValue <= maxY)) {
				line.getPosReal().setY(this.minValue);
				line.setLength(this.maxValue);
				clippedLines.add(line);
			}
		}
		return clippedLines;
	}

	public LinkedList<Band> clipBands(final LinkedList<Band> bandsList) {
		double posYinf, posYsup;
		double x, y, height, width;
		Color color;

		final LinkedList<Band> clippedBands = new LinkedList<Band>();
		final Iterator<Band> it = bandsList.iterator();
		while (it.hasNext()) {
			final Band rect = it.next();
			x = rect.getPosReal().getX();
			y = rect.getPosReal().getY();
			height = rect.getHeight();
			width = rect.getWidth();
			color = rect.getColor();
			if (simType.equals(ProximityType.DISTANCE)) {
				posYinf = y;
				posYsup = y + height;
			} else {
				posYinf = y - height;
				posYsup = y;
			}
			if ((posYinf <= maxValue) && (posYsup >= minValue)) {
				if (posYinf < minValue) {
					posYinf = minValue;
				}
				if (posYsup > maxValue) {
					posYsup = maxValue;
				}
				y = posYinf;
				height = (posYsup - posYinf);
				clippedBands.add(new Band(x, y, height, width, color));
			} else if ((posYinf <= minValue) && (posYsup >= maxValue)) {
				y = minValue;
				height = (maxValue - minValue);
				clippedBands.add(new Band(x, y, height, width, color));
			}
		}
		return clippedBands;
	}

}
