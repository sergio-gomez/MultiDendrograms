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
import multidendrograms.errors.MethodError;
import multidendrograms.types.MethodName;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Abstract class for the different clustering methods
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public abstract class Method {

	DistancesMatrix dm;

	public Method(final DistancesMatrix distMatrix) {
		this.dm = distMatrix;
	}

	public static String toName(MethodName method) {
		String str = "";
		switch (method) {
		case SINGLE_LINKAGE:
			str = "Single Linkage";
			break;
		case COMPLETE_LINKAGE:
			str = "Complete Linkage";
			break;
		case UNWEIGHTED_AVERAGE:
			str = "Unweighted Average";
			break;
		case WEIGHTED_AVERAGE:
			str = "Weighted Average";
			break;
		case UNWEIGHTED_CENTROID:
			str = "Unweighted Centroid";
			break;
		case WEIGHTED_CENTROID:
			str = "Weighted Centroid";
			break;
		case WARD:
			str = "Ward";
			break;
		default:
			str = "";
			break;
		}
		return str;
	}

	public static String toShortName(MethodName method) {
		String str = "";
		switch (method) {
		case SINGLE_LINKAGE:
			str = "sl";
			break;
		case COMPLETE_LINKAGE:
			str = "cl";
			break;
		case UNWEIGHTED_AVERAGE:
			str = "ua";
			break;
		case WEIGHTED_AVERAGE:
			str = "wa";
			break;
		case UNWEIGHTED_CENTROID:
			str = "uc";
			break;
		case WEIGHTED_CENTROID:
			str = "wc";
			break;
		case WARD:
			str = "wd";
			break;
		default:
			str = "";
			break;
		}
		return str;
	}

	public static MethodName toMethod(String sMethod) throws MethodError {
		MethodName method;
		String str = sMethod.toUpperCase();

		if      (str.equals("SL") || str.equals("SINGLE_LINKAGE"))
			method = MethodName.SINGLE_LINKAGE;
		else if (str.equals("CL") || str.equals("COMPLETE_LINKAGE"))
			method = MethodName.COMPLETE_LINKAGE;
		else if (str.equals("UA") || str.equals("UNWEIGHTED_AVERAGE"))
			method = MethodName.UNWEIGHTED_AVERAGE;
		else if (str.equals("WA") || str.equals("WEIGHTED_AVERAGE"))
			method = MethodName.WEIGHTED_AVERAGE;
		else if (str.equals("UC") || str.equals("UNWEIGHTED_CENTROID"))
			method = MethodName.UNWEIGHTED_CENTROID;
		else if (str.equals("WC") || str.equals("WEIGHTED_CENTROID"))
			method = MethodName.WEIGHTED_CENTROID;
		else if (str.equals("WD") || str.equals("WARD"))
			method = MethodName.WARD;
		else
		  throw new MethodError("Error: Unknown method: " + str);

		return method;
	}

	public abstract MethodName getMethodName();

	public String getMethodNameString(){
		return Method.toName(getMethodName());
	}

	protected abstract double getAlpha(final Cluster cI, final Cluster subci, final Cluster cJ, final Cluster subcj);

	protected abstract double getBeta(final Cluster cI, final Cluster subci, final Cluster subck, final Cluster cJ);

	protected abstract double gammaTerm(final Cluster cI, final Cluster cJ)
			throws Exception;

	public double distance(final Cluster cI, final Cluster cJ) throws Exception {
		double dist;
		if (this.dm.existsDistance(cI, cJ)) {
			dist = this.dm.getDistance(cI, cJ);
		} else {
			dist = alphaTerm(cI, cJ) + betaTerm(cI, cJ) + gammaTerm(cI, cJ);
		}
		return dist;
	}

	private double alphaTerm(final Cluster cI, final Cluster cJ) throws Exception {
		double alpha, d;
		double dist = 0.0;
		if (cI.isSupercluster() && cJ.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				Cluster subci = cI.getSubcluster(i);
				for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
					Cluster subcj = cJ.getSubcluster(j);
					alpha = getAlpha(cI, subci, cJ, subcj);
					d = this.dm.getDistance(subci, subcj);
					dist += alpha * d;
				}
			}
		} else if (cI.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				Cluster subci = cI.getSubcluster(i);
				alpha = getAlpha(cI, subci, cJ, cJ);
				d = this.dm.getDistance(subci, cJ);
				dist += alpha * d;
			}
		} else if (cJ.isSupercluster()) {
			for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
				Cluster subcj = cJ.getSubcluster(j);
				alpha = getAlpha(cJ, subcj, cI, cI);
				d = this.dm.getDistance(subcj, cI);
				dist += alpha * d;
			}
		}
		return dist;
	}

	private double betaTerm(final Cluster cI, final Cluster cJ) throws Exception {
		double beta, d;
		double dist = 0.0;
		if (cI.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters() - 1; i ++) {
				Cluster subci = cI.getSubcluster(i);
				for (int k = i + 1; k < cI.getNumSubclusters(); k ++) {
					Cluster subck = cI.getSubcluster(k);
					beta = getBeta(cI, subci, subck, cJ);
					d = this.dm.getDistance(subci, subck);
					dist += beta * d;
				}
			}
		}
		if (cJ.isSupercluster()) {
			for (int j = 0; j < cJ.getNumSubclusters() - 1; j ++) {
				Cluster subcj = cJ.getSubcluster(j);
				for (int k = j + 1; k < cJ.getNumSubclusters(); k ++) {
					Cluster subck = cJ.getSubcluster(k);
					beta = getBeta(cJ, subcj, subck, cI);
					d = this.dm.getDistance(subcj, subck);
					dist += beta * d;
				}
			}
		}
		return dist;
	}

	protected double getMinDistance(final Cluster cI, final Cluster cJ) throws Exception {
		double min = Double.MAX_VALUE;
		double dij;

		if (this.dm.existsDistance(cI, cJ)) {
			min = this.dm.getDistance(cI, cJ);
		} else if (cI.isSupercluster() && cJ.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				Cluster subci = cI.getSubcluster(i);
				for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
					Cluster subcj = cJ.getSubcluster(j);
					dij = this.dm.getDistance(subci, subcj);
					min = Math.min(min, dij);
				}
			}
		} else if (cI.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				Cluster subci = cI.getSubcluster(i);
				dij = this.dm.getDistance(subci, cJ);
				min = Math.min(min, dij);
			}
		} else if (cJ.isSupercluster()) {
			for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
				Cluster subcj = cJ.getSubcluster(j);
				dij = this.dm.getDistance(subcj, cI);
				min = Math.min(min, dij);
			}
		}
		return min;
	}

	protected double getMaxDistance(final Cluster cI, final Cluster cJ) throws Exception {
		double max = Double.MIN_VALUE;
		double dij;

		if (this.dm.existsDistance(cI, cJ)) {
			max = this.dm.getDistance(cI, cJ);
		} else if (cI.isSupercluster() && cJ.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				Cluster subci = cI.getSubcluster(i);
				for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
					Cluster subcj = cJ.getSubcluster(j);
					dij = this.dm.getDistance(subci, subcj);
					max = Math.max(max, dij);
				}
			}
		} else if (cI.isSupercluster()) {
			for (int i = 0; i < cI.getNumSubclusters(); i ++) {
				Cluster subci = cI.getSubcluster(i);
				dij = this.dm.getDistance(subci, cJ);
				max = Math.max(max, dij);
			}
		} else if (cJ.isSupercluster()) {
			for (int j = 0; j < cJ.getNumSubclusters(); j ++) {
				Cluster subcj = cJ.getSubcluster(j);
				dij = this.dm.getDistance(subcj, cI);
				max = Math.max(max, dij);
			}
		}
		return max;
	}

}
