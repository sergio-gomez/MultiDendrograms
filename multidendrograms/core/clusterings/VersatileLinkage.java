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
 * Versatile-Linkage agglomerative hierarchical clustering
 *
 * @author Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres
 *
 * @since JDK 6.0
 ******************************************************************************/
public class VersatileLinkage extends HierarchicalClustering {

	private boolean isWeighted;
	private double power;

	public VersatileLinkage(SymmetricMatrix proximityMatrix, String[] labels, 
			boolean isDistanceBased, int precision, boolean isWeighted, 
			double power) {
		super(proximityMatrix, labels, isDistanceBased, precision);
		this.isWeighted = isWeighted;
		if (this.isDistanceBased) {
			this.power = +power;
		} else {
			this.power = -power;
		}
	}

	@Override
	protected double calculateProximity(Dendrogram cI, Dendrogram cJ) {
		if (this.power == Double.NEGATIVE_INFINITY) {
			return minimumProximity(cI, cJ);
		} else if (this.power == Double.POSITIVE_INFINITY) {
			return maximumProximity(cI, cJ);
		} else if (this.power == 0.0) {
			return geometricMean(cI, cJ);
		} else {
			return generalizedMean(cI, cJ);
		}
	}

	private double geometricMean(Dendrogram cI, Dendrogram cJ) {
		int numSubrootsI = cI.numberOfSubroots();
		int numSubrootsJ = cJ.numberOfSubroots();
		int numLeavesI = cI.numberOfLeaves();
		int numLeavesJ = cJ.numberOfLeaves();
		double proximity = 1.0;
		for (int i = 0; i < numSubrootsI; i ++) {
			Dendrogram subcI = cI.getSubroot(i);
			double wI = this.isWeighted ? 
					1.0 / (double)numSubrootsI : 
					(double)subcI.numberOfLeaves() / (double)numLeavesI;
			for (int j = 0; j < numSubrootsJ; j ++) {
				Dendrogram subcJ = cJ.getSubroot(j);
				double wJ = this.isWeighted ? 
						1.0 / (double)numSubrootsJ : 
						(double)subcJ.numberOfLeaves() / (double)numLeavesJ;
				double prox = rootsProximity(subcI, subcJ);
				proximity *= Math.pow(prox, wI * wJ);
			}
		}
		return proximity;
	}

	private double generalizedMean(Dendrogram cI, Dendrogram cJ) {
		int numSubrootsI = cI.numberOfSubroots();
		int numSubrootsJ = cJ.numberOfSubroots();
		int numLeavesI = cI.numberOfLeaves();
		int numLeavesJ = cJ.numberOfLeaves();
		double proximity = 0.0;
		for (int i = 0; i < numSubrootsI; i ++) {
			Dendrogram subcI = cI.getSubroot(i);
			double wI = this.isWeighted ? 
					1.0 / (double)numSubrootsI : 
					(double)subcI.numberOfLeaves() / (double)numLeavesI;
			for (int j = 0; j < numSubrootsJ; j ++) {
				Dendrogram subcJ = cJ.getSubroot(j);
				double wJ = this.isWeighted ? 
						1.0 / (double)numSubrootsJ : 
						(double)subcJ.numberOfLeaves() / (double)numLeavesJ;
				double prox = rootsProximity(subcI, subcJ);
				proximity += wI * wJ * Math.pow(prox, this.power);
			}
		}
		proximity = Math.pow(proximity, 1.0 / this.power);
		return proximity;
	}

}
