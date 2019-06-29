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
 * Abstract class for Lance &amp; Williams' agglomerative hierarchical clustering
 *
 * @author Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres
 *
 * @since JDK 6.0
 ******************************************************************************/
public abstract class LanceWilliams extends HierarchicalClustering {

	public LanceWilliams(SymmetricMatrix proximityMatrix, String[] labels,
			boolean isDistanceBased, int precision) {
		super(proximityMatrix, labels, isDistanceBased, precision);
	}

	@Override
	protected double calculateProximity(Dendrogram cI, Dendrogram cJ) {
		return alphaTerm(cI, cJ) + betaTerm(cI, cJ) + betaTerm(cJ, cI);
	}

	private double alphaTerm(Dendrogram cI, Dendrogram cJ) {
		double proximity = 0.0;
		for (int i = 0; i < cI.numberOfSubroots(); i ++) {
			Dendrogram subcI = cI.getSubroot(i);
			for (int j = 0; j < cJ.numberOfSubroots(); j ++) {
				Dendrogram subcJ = cJ.getSubroot(j);
				double alpha = getAlpha(cI, subcI, cJ, subcJ);
				double prox = rootsProximity(subcI, subcJ);
				proximity += alpha * prox;
			}
		}
		return proximity;
	}

	protected abstract double getAlpha(Dendrogram cI, Dendrogram subcI,
			Dendrogram cJ, Dendrogram subcJ);

	private double betaTerm(Dendrogram cI, Dendrogram cJ) {
		double proximity = 0.0;
		for (int i = 0; i < cI.numberOfSubroots() - 1; i ++) {
			Dendrogram subcI = cI.getSubroot(i);
			for (int k = i + 1; k < cI.numberOfSubroots(); k ++) {
				Dendrogram subcK = cI.getSubroot(k);
				double beta = getBeta(cI, subcI, subcK, cJ);
				double prox = rootsProximity(subcI, subcK);
				proximity += beta * prox;
			}
		}
		return proximity;
	}

	protected abstract double getBeta(Dendrogram cI, Dendrogram subcI,
			Dendrogram subcK, Dendrogram cJ);

}
