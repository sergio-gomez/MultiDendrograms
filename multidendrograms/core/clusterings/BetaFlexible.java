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

package multidendrograms.core.clusterings;

import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.definitions.SymmetricMatrix;

/******************************************************************************
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Beta-Flexible agglomerative hierarchical clustering
 *
 * @author Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres
 *
 * @since JDK 6.0
 ******************************************************************************/
public class BetaFlexible extends LanceWilliams {

	private boolean isWeighted;
	private double beta;

	public BetaFlexible(SymmetricMatrix proximityMatrix, String[] labels, 
			boolean isDistanceBased, int precision, boolean isWeighted, 
			double beta) {
		super(proximityMatrix, labels, isDistanceBased, precision);
		this.isWeighted = isWeighted;
		this.beta = beta;
	}

	@Override
	protected double getAlpha(Dendrogram cI, Dendrogram subcI, Dendrogram cJ,
			Dendrogram subcJ) {
		double wI = this.isWeighted ? 
				1.0 / (double)cI.numberOfSubroots() : 
				(double)subcI.numberOfLeaves() / (double)cI.numberOfLeaves();
		double wJ = this.isWeighted ? 
				1.0 / (double)cJ.numberOfSubroots() : 
				(double)subcJ.numberOfLeaves() / (double)cJ.numberOfLeaves();
		return wI * wJ * (1.0 - this.beta);
	}

	@Override
	protected double getBeta(Dendrogram cI, Dendrogram subcI, Dendrogram subcK,
			Dendrogram cJ) {
		double w;
		if (this.isWeighted) {
			int nI = cI.numberOfSubroots();
			int nJ = cJ.numberOfSubroots();
			w = 1.0 / (double)((nI - 1) * nI / 2 + (nJ - 1) * nJ / 2);
		} else {
			w = (double)(subcI.numberOfLeaves() * subcK.numberOfLeaves()) 
					/ (sigma(cI) + sigma(cJ));
		}
		return w * this.beta;
	}

	private double sigma(Dendrogram c) {
		int s = c.numberOfLeaves() * c.numberOfLeaves();
		for (int i = 0; i < c.numberOfSubroots(); i ++) {
			Dendrogram subc = c.getSubroot(i);
			s -= subc.numberOfLeaves() * subc.numberOfLeaves();
		}
		return 0.5 * (double)s;
	}

}
