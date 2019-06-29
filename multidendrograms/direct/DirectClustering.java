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

package multidendrograms.direct;

import java.io.IOException;

import multidendrograms.core.clusterings.BetaFlexible;
import multidendrograms.core.clusterings.Centroid;
import multidendrograms.core.clusterings.CompleteLinkage;
import multidendrograms.core.clusterings.HierarchicalClustering;
import multidendrograms.core.clusterings.SingleLinkage;
import multidendrograms.core.clusterings.VersatileLinkage;
import multidendrograms.core.clusterings.Ward;
import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.definitions.SymmetricMatrix;
import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;
import multidendrograms.dendrogram.ConnectedGraph;
import multidendrograms.dendrogram.DendrogramMeasures;
import multidendrograms.dendrogram.ToJson;
import multidendrograms.dendrogram.ToNewick;
import multidendrograms.dendrogram.ToTxt;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.initial.Language;
import multidendrograms.initial.MethodName;
import multidendrograms.types.BandHeight;
import multidendrograms.types.MethodType;
import multidendrograms.types.OriginType;
import multidendrograms.types.ProximityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates and exports the hierarchical clustering without GUI interaction
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 */
public class DirectClustering {

	public static final int AUTO_PRECISION = Integer.MIN_VALUE;
	public static final String MEASURES_SUFIX = "-measures.txt";
	public static final String ULTRAMETRIC_SUFIX = "-ultrametric.txt";
	public static final String GRAPH_SUFIX = "-graph.net";
	public static final String TXT_TREE_SUFIX = "-tree.txt";
	public static final String NEWICK_TREE_SUFIX = "-newick.txt";
	public static final String JSON_TREE_SUFIX = ".json";

	private DataFile dataFile;
	private ExternalData externalData;
	private ProximityType proximityType;
	private int precision;
	private String filePrefix;
	private OriginType originType;
	private BandHeight bandHeight;
	private HierarchicalClustering clustering;
	private UltrametricMatrix ultraMatrix = null;
	private DendrogramMeasures dendroMeasures = null;

	public DirectClustering(String filename, ProximityType proximityType,
			int initialPrecision, MethodType methodType, double methodParameter,
			boolean isWeighted, OriginType originType, BandHeight bandHeight)
	throws Exception {
		this.dataFile = new DataFile(filename);
		try {
			this.externalData = new ExternalData(this.dataFile);
		} catch (Exception e) {
			throw e;
		}
		this.proximityType = proximityType;
		SymmetricMatrix proximityMatrix = this.externalData.getProximityMatrix();
		this.precision = initialPrecision;
		if (this.precision == DirectClustering.AUTO_PRECISION) {
			this.precision = this.externalData.getPrecision();
		}
		this.filePrefix = getFilePrefix(this.dataFile.getPathNameNoExt(),proximityType, this.precision,
		    methodType, methodParameter, isWeighted);
		this.originType = originType;
		this.bandHeight = bandHeight;

		if ((proximityMatrix.minimumValue() < 0.0) &&
				(methodType.equals(MethodType.VERSATILE_LINKAGE) ||
				 methodType.equals(MethodType.GEOMETRIC_LINKAGE))) {
			throw new Exception(Language.getLabel(80));
		}

		System.out.println("Data file        : " + filename);
		System.out.println("Proximity type   : " + proximityType.toString().toLowerCase());
		System.out.println("Precision        : " + this.precision);
		System.out.println("Method name      : " + methodType.toString().toLowerCase());
		System.out.println("Method parameter : " + methodParameter);
		System.out.println("Weighted         : " + isWeighted);
		System.out.println("Origin           : " + this.originType.toString().toLowerCase());
		System.out.println("---");

		this.clustering = newClustering(methodType, proximityMatrix, this.externalData.getNames(),
		    proximityType, this.precision, isWeighted, methodParameter);
		this.clustering.build();
	}

	public static String getFilePrefix(String pathNameNoExt,
			ProximityType proximityType, int precision, MethodType methodType,
			double methodParameter, boolean isWeighted) {
		String prefix = pathNameNoExt;
		if (proximityType.equals(ProximityType.DISTANCE)) {
			prefix += "-d" + precision + "-";
		} else {
			prefix += "-s" + precision + "-";
		}
		if (isWeighted && (methodType.equals(MethodType.VERSATILE_LINKAGE) ||
						   methodType.equals(MethodType.ARITHMETIC_LINKAGE) ||
						   methodType.equals(MethodType.GEOMETRIC_LINKAGE) ||
						   methodType.equals(MethodType.HARMONIC_LINKAGE) ||
						   methodType.equals(MethodType.CENTROID) ||
						   methodType.equals(MethodType.BETA_FLEXIBLE))) {
			prefix += "w";
		}
		prefix += MethodName.toShortName(methodType);
		if (methodType.equals(MethodType.VERSATILE_LINKAGE) ||
			methodType.equals(MethodType.BETA_FLEXIBLE)) {
			prefix += methodParameter;
		}
		return prefix;
	}

	public void printMeasures() {
		if (this.ultraMatrix == null) {
			this.ultraMatrix = new UltrametricMatrix(this.clustering.getRoot(),
					this.externalData.getNames(), this.originType,
					this.bandHeight);
		}
		if (this.dendroMeasures == null) {
			this.dendroMeasures = new DendrogramMeasures(
					this.externalData.getProximityMatrix(),
					this.clustering.getRoot(), this.ultraMatrix.getMatrix());
		}
		System.out.println(DendrogramMeasures.COPHENETIC_CORRELATION_LABEL + " : "
		    + this.dendroMeasures.getCopheneticCorrelation());
		System.out.println(DendrogramMeasures.SQUARED_ERROR_LABEL + "      : "
				+ this.dendroMeasures.getSquaredError());
		System.out.println(DendrogramMeasures.ABSOLUTE_ERROR_LABEL + "     : "
				+ this.dendroMeasures.getAbsoluteError());
		System.out.println(DendrogramMeasures.TREE_BALANCE_LABEL + "            : "
		    + this.dendroMeasures.getTreeBalance());
		System.out.println(DendrogramMeasures.SPACE_DISTORTION_LABEL + "                   : "
				+ this.dendroMeasures.getSpaceDistortion());
//		System.out.println(DendrogramMeasures.DEGREE_CONNECTIVITY_LABEL
//				+ "             : "
//				+ this.dendroMeasures.getDegreeOfConnectivity());
		System.out.println("---");
	}

	public void saveMeasures() {
		if (this.ultraMatrix == null) {
			this.ultraMatrix = new UltrametricMatrix(this.clustering.getRoot(),
					this.externalData.getNames(), this.originType,
					this.bandHeight);
		}
		if (this.dendroMeasures == null) {
			this.dendroMeasures = new DendrogramMeasures(this.externalData.getProximityMatrix(),
					this.clustering.getRoot(), this.ultraMatrix.getMatrix());
		}
		String filename = this.filePrefix + DirectClustering.MEASURES_SUFIX;
		try {
			this.dendroMeasures.save(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveUltrametric() {
		if (this.ultraMatrix == null) {
			this.ultraMatrix = new UltrametricMatrix(this.clustering.getRoot(),
					this.externalData.getNames(), this.originType, this.bandHeight);
		}
		String filename = this.filePrefix + DirectClustering.ULTRAMETRIC_SUFIX;
		try {
			this.ultraMatrix.saveAsTxt(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveGraph() {
		if (this.ultraMatrix == null) {
			this.ultraMatrix = new UltrametricMatrix(this.clustering.getRoot(),
					this.externalData.getNames(), this.originType, this.bandHeight);
		}
		String filename = this.filePrefix + DirectClustering.GRAPH_SUFIX;
		ConnectedGraph connectedGraph =
				new ConnectedGraph(this.externalData.getProximityMatrix(),
				    this.externalData.getNames(), this.proximityType,
				    this.precision, this.ultraMatrix.getMatrix());
		try {
			connectedGraph.saveAsNet(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveAsTxt() {
		String filename = this.filePrefix + DirectClustering.TXT_TREE_SUFIX;
		ToTxt saveTxt = new ToTxt(this.clustering.getRoot());
		try {
			saveTxt.saveAsTxt(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveAsNewick() {
		String filename = this.filePrefix + DirectClustering.NEWICK_TREE_SUFIX;
		Dendrogram root = this.clustering.getRoot();
		boolean isUniformOrigin = this.originType.equals(OriginType.UNIFORM_ORIGIN) ? true : false;
		ToNewick saveNewick = new ToNewick(root, isUniformOrigin);
		try {
			saveNewick.saveAsNewick(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveAsJson() {
		String filename = this.filePrefix + DirectClustering.JSON_TREE_SUFIX;
		Dendrogram root = this.clustering.getRoot();
		boolean isUniformOrigin = this.originType.equals(OriginType.UNIFORM_ORIGIN) ? true : false;
		ToJson saveJson = new ToJson(root, isUniformOrigin);
		try {
			saveJson.saveAsJson(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static HierarchicalClustering newClustering(MethodType methodType,
			SymmetricMatrix proximityMatrix, String[] labels, ProximityType proximityType,
			int precision, boolean isWeighted, double methodParameter) {
		boolean isDistanceBased = proximityType.equals(ProximityType.DISTANCE) ? true : false;
		double power;
		HierarchicalClustering clustering;
		switch (methodType) {
		case VERSATILE_LINKAGE:
			power = inverseSigmoid(methodParameter);
			clustering = new VersatileLinkage(proximityMatrix, labels, isDistanceBased, precision, isWeighted, power);
			break;
		case SINGLE_LINKAGE:
			clustering = new SingleLinkage(proximityMatrix, labels, isDistanceBased, precision);
			break;
		case COMPLETE_LINKAGE:
			clustering = new CompleteLinkage(proximityMatrix, labels, isDistanceBased, precision);
			break;
		case ARITHMETIC_LINKAGE:
			power = +1.0;
			clustering = new VersatileLinkage(proximityMatrix, labels, isDistanceBased, precision, isWeighted, power);
			break;
		case GEOMETRIC_LINKAGE:
			power = 0.0;
			clustering = new VersatileLinkage(proximityMatrix, labels, isDistanceBased, precision, isWeighted, power);
			break;
		case HARMONIC_LINKAGE:
			power = -1.0;
			clustering = new VersatileLinkage(proximityMatrix, labels, isDistanceBased, precision, isWeighted, power);
			break;
		case CENTROID:
			clustering = new Centroid(proximityMatrix, labels, isDistanceBased, precision, isWeighted);
			break;
		case WARD:
			clustering = new Ward(proximityMatrix, labels, isDistanceBased, precision);
			break;
		case BETA_FLEXIBLE:
			clustering = new BetaFlexible(proximityMatrix, labels, isDistanceBased, precision, isWeighted, methodParameter);
			break;
		default:
			clustering = null;
			break;
		}
		return clustering;
	}

	private static double inverseSigmoid(double y) {
		if (y <= -1.0) {
			return Double.NEGATIVE_INFINITY;
		} else if (y >= +1.0) {
			return Double.POSITIVE_INFINITY;
		} else {
			double y1 = 0.1;	// 0 < y1 = sigmoid(1) < 1
			return Math.log((1.0 + y) / (1.0 - y)) / Math.log((1.0 + y1) / (1.0 - y1));
		}
	}

}
