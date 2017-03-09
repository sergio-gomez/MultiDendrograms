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

package multidendrograms.methods;

import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.DistancesMatrix;
import multidendrograms.types.MethodName;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Weighted Centroid clustering algorithm
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class WeightedCentroid extends Method {

	public WeightedCentroid(final DistancesMatrix distMatrix) {
		super(distMatrix);
	}

	@Override
	public MethodName getMethodName() {
		return MethodName.WEIGHTED_CENTROID;
	}

	@Override
	protected double getAlpha(final Cluster cI, final Cluster subci, final Cluster cJ, final Cluster subcj) {
		int nI = cI.isSupercluster() ? cI.getNumSubclusters() : 1;
		int nJ = cJ.isSupercluster() ? cJ.getNumSubclusters() : 1;
		double res = 1.0 / (double)(nI * nJ);
		return res;
	}

	@Override
	protected double getBeta(final Cluster cI, final Cluster subci, final Cluster subck, final Cluster cJ) {
		int nI = cI.isSupercluster() ? cI.getNumSubclusters() : 1;
		double res = -1.0 / (double)(nI * nI);
		return res;
	}

	@Override
	protected double gammaTerm(final Cluster cI, final Cluster cJ) {
		return 0.0;
	}

}
