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
 * Unweighted Centroid clustering algorithm
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class UnweightedCentroid extends Method {

	public UnweightedCentroid(final DistancesMatrix distMatrix) {
		super(distMatrix);
	}

	@Override
	public MethodName getMethodName() {
		return MethodName.UNWEIGHTED_CENTROID;
	}

	@Override
	protected double getAlpha(final Cluster cI, final Cluster subci, final Cluster cJ, final Cluster subcj) {
		double res = (double)(subci.getNumLeaves() * subcj.getNumLeaves())
				/ (double)(cI.getNumLeaves() * cJ.getNumLeaves());
		return res;
	}

	@Override
	protected double getBeta(final Cluster cI, final Cluster subci, final Cluster subck, final Cluster cJ) {
		double res = -(double)(subci.getNumLeaves() * subck.getNumLeaves())
				/ (double)(cI.getNumLeaves() * cI.getNumLeaves());
		return res;
	}

	@Override
	protected double gammaTerm(final Cluster cI, final Cluster cJ) {
		return 0.0;
	}

}
