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

import multidendrograms.core.definitions.Dendrogram;

/******************************************************************************
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculate smart axis bounds and ticks
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 ******************************************************************************/
public class SmartAxis {

	private enum NiceType {NICE_FLOOR, NICE_CEIL, NICE_ROUND}

	private double min, max, ticksSize;

	public SmartAxis(Dendrogram root, boolean isUniformOrigin) {
		this.min = getDendroMinHeight(root, isUniformOrigin);
		this.max = getDendroMaxHeight(root, isUniformOrigin);
		roundAxisLimits();
		roundTicks();
	}

	public double smartMin() {
		return this.min;
	}

	public double smartMax() {
		return this.max;
	}

	public double smartTicksSize() {
		return this.ticksSize;
	}

	private double getDendroMinHeight(Dendrogram root, 
			boolean isUniformOrigin) {
		double bandsMinHeight = root.getBandsMinHeight();
		double nodesMinHeight = root.getNodesMinHeight();
		double minHeight;
		if (Double.isNaN(nodesMinHeight) || isUniformOrigin) {
			minHeight = bandsMinHeight;
		} else {
			minHeight = Math.min(bandsMinHeight, nodesMinHeight);
		}
		if (root.isDistanceBased) {
			minHeight = Math.min(minHeight, 0.0);
		}
		return minHeight;
	}

	private double getDendroMaxHeight(Dendrogram root, 
			boolean isUniformOrigin) {
		double bandsMaxHeight = root.getBandsMaxHeight();
		double nodesMaxHeight = root.getNodesMaxHeight();
		double maxHeight;
		if (Double.isNaN(nodesMaxHeight) || isUniformOrigin) {
			maxHeight = bandsMaxHeight;
		} else {
			maxHeight = Math.max(bandsMaxHeight, nodesMaxHeight);
		}
		return maxHeight;
	}

	private void roundAxisLimits() {
		if (this.min == this.max) {
			switch (sign(this.min)) {
			case 0:
				this.min = -1.0;
				this.max = +1.0;
				break;
			case +1:
				this.min /= 2.0;
				this.max *= 2.0;
				break;
			case -1:
				this.min *= 2.0;
				this.max /= 2.0;
				break;
			}
		}
		int nrange;
		if (sign(this.min) == sign(this.max)) {
			nrange = (int) -Math.rint(Math.log10(Math.abs(
				2 * (this.max - this.min) / (this.max + this.min))));
			nrange = Math.max(nrange, 0);
		} else {
			nrange = 0;
		}
		this.min = niceNum(this.min, nrange, NiceType.NICE_FLOOR);
		this.max = niceNum(this.max, nrange, NiceType.NICE_CEIL);
		if (sign(this.min) == sign(this.max)) {
			if (this.max / this.min > 5.0) {
				this.min = 0.0;
			} else if (this.min / this.max > 5.0) {
				this.max = 0.0;
			}
		}
	}

	private void roundTicks() {
		final int numTicks = 10;
		this.ticksSize = niceNum((this.max - this.min) / (numTicks - 1), 
				0, NiceType.NICE_ROUND);
	}

	private double niceNum(double x, int nrange, NiceType round) {
		long xsign;
		double f, y, fexp, rx, sx;

		if (x == 0.0) {
			return 0.0;
		}

		xsign = sign(x);
		x = Math.abs(x);

		fexp = Math.floor(Math.log10(x)) - nrange;
		sx = x / Math.pow(10.0, fexp) / 10.0;  // scaled x
		rx = Math.floor(sx);                   // rounded x
		f = 10.0 * (sx - rx);                  // fraction between 0 and 10

		if ((round == NiceType.NICE_FLOOR && xsign == +1) || 
			(round == NiceType.NICE_CEIL  && xsign == -1)) {
			y = (int) Math.floor(f);
		} else if ((round == NiceType.NICE_FLOOR && xsign == -1) || 
				   (round == NiceType.NICE_CEIL  && xsign == +1)) {
			y = (int) Math.ceil(f);
		} else {                               // round == NiceType.NICE_ROUND
			if (f < 1.5)
				y = 1;
			else if (f < 3.0)
				y = 2;
			else if (f < 7.0)
				y = 5;
			else
				y = 10;
		}

		sx = rx + (double) y / 10.0;

		return xsign * sx * 10.0 * Math.pow(10.0, fexp);
	}

	private int sign(double x) {
		return (int)Math.round(Math.signum(x));
	}

}
