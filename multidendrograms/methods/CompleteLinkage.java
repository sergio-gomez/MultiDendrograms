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
import multidendrograms.initial.Language;
import multidendrograms.types.MethodName;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Complete Linkage clustering algorithm
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class CompleteLinkage extends Method {

	public CompleteLinkage(final DistancesMatrix distMatrix) {
		super(distMatrix);
	}

	@Override
	public MethodName getMethodName() {
		return MethodName.COMPLETE_LINKAGE;
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
		return 0.0;
	}

	@Override
	protected double gammaTerm(final Cluster cI, final Cluster cJ) throws Exception {
		double dif, dist;
		double gamma = getAlpha(cI, null, cJ, null);
		if (this.dm.isDistancesType()) {
			dif = getMaxDistance(cI, cJ);
		} else {
			dif = getMinDistance(cI, cJ);
		}
		double res = 0.0;
		if (cI.isSupercluster() && cJ.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
					dist = this.dm.getDistance(cI.getSubcluster(i), cJ.getSubcluster(j));
					res += gamma * (dif - dist);
				}
			}
		} else if (cI.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				dist = this.dm.getDistance(cI.getSubcluster(i), cJ);
				res += gamma * (dif - dist);
			}
		} else if (cJ.isSupercluster()) {
			for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
				dist = this.dm.getDistance(cJ.getSubcluster(j), cI);
				res += gamma * (dif - dist);
			}
		} else {
			throw new Exception(Language.getLabel(69));
		}
		return res;
	}

}
