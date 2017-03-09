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
 * Centroid agglomerative hierarchical clustering
 *
 * @author Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres
 *
 * @since JDK 6.0
 ******************************************************************************/
public class Centroid extends LanceWilliams {

	private boolean isWeighted;

	public Centroid(SymmetricMatrix proximityMatrix, String[] labels, 
			boolean isDistanceBased, int precision, boolean isWeighted) {
		super(proximityMatrix, labels, isDistanceBased, precision);
		this.isWeighted = isWeighted;
	}

	@Override
	protected double getAlpha(Dendrogram cI, Dendrogram subcI, Dendrogram cJ, 
			Dendrogram subcJ) {
		double alpha = this.isWeighted ? 
			+1.0 / (double)(cI.numberOfSubroots() * cJ.numberOfSubroots()) : 
			+(double)(subcI.numberOfLeaves() * subcJ.numberOfLeaves()) / 
				(double)(cI.numberOfLeaves() * cJ.numberOfLeaves());
		return alpha;
	}

	@Override
	protected double getBeta(Dendrogram cI, Dendrogram subcI, Dendrogram subcK, 
			Dendrogram cJ) {
		double beta = this.isWeighted ? 
			-1.0 / (double)(cI.numberOfSubroots() * cI.numberOfSubroots()) : 
			-(double)(subcI.numberOfLeaves() * subcK.numberOfLeaves()) / 
				(double)(cI.numberOfLeaves() * cI.numberOfLeaves());
		return beta;
	}

}
