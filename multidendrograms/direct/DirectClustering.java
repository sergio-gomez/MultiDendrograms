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

import java.text.NumberFormat;
import java.util.Locale;

import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.DistancesMatrix;
import multidendrograms.dendrogram.ToNewick;
import multidendrograms.dendrogram.ToTxt;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.methods.Method;
import multidendrograms.methods.BuildDendrogram;
import multidendrograms.types.MethodName;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates and exports the hierarchical clustering without GUI interaction
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DirectClustering {

	public static final int AUTO_PRECISION = Integer.MIN_VALUE;
	private static final String TXT_TREE_SUFIX = "-tree.txt";
	private static final String NEWICK_TREE_SUFIX = "-newick.txt";
	private static final String ULTRAMETRIC_SUFIX = "-ultrametric.txt";

	private String dataFileName;
	private SimilarityType simType;
	private MethodName method;
	private int precision;

	private String infix;
	private DataFile dataFile = null;
	private ExternalData data = null;
	private DistancesMatrix distMatrix = null;
	private UltrametricMatrix ultraMatrix = null;

	public DirectClustering(final String dataFileName, final SimilarityType simType,
			final MethodName method) throws Exception {
		this(dataFileName, simType, method, AUTO_PRECISION);
	}

	public DirectClustering(final String dataFileName, final SimilarityType simType,
			final MethodName method, final int precision) throws Exception {
		this.dataFileName = dataFileName;
		this.simType = simType;
		this.method = method;
		this.precision = precision;
		this.infix = "-" + Method.toShortName(method);
		if (precision != AUTO_PRECISION) {
			this.infix = this.infix + precision;
		}
		doClustering();
	}

	private void doClustering() throws Exception {
		dataFile = new DataFile(dataFileName);
		try {
			data = new ExternalData(dataFile, true);
		} catch (Exception e) {
			throw e;
		}
		if (precision == AUTO_PRECISION) {
			precision = data.getPrecision();
		}

		System.out.println("Data file       : " + dataFileName);
		System.out.println("Similarity type : " + simType.toString().toLowerCase());
		System.out.println("Method name     : " + method.toString().toLowerCase());
		System.out.println("Precision       : " + precision);
		System.out.println("---");

		distMatrix = data.getDistancesMatrix();
		distMatrix.setSimilarityType(simType);
		double minBase = Double.MAX_VALUE;
		double base;

		BuildDendrogram bd;
		while (distMatrix.getCardinality() > 1) {
			try {
				bd = new BuildDendrogram(distMatrix, simType, method, precision);
				distMatrix = bd.recalculate();
				base = distMatrix.getRoot().getBase();
				if ((base < minBase) && (base != Double.MAX_VALUE)) {
					minBase = base;
				}
			} catch (final Exception e) {
				throw e;
			}
		}
		Cluster root = distMatrix.getRoot();
		root.setBase(minBase);
		if (!method.equals(MethodName.UNWEIGHTED_CENTROID)
				&& !method.equals(MethodName.WEIGHTED_CENTROID)) {
			BuildDendrogram.avoidReversals(root, root.getSummaryHeight(), simType);
		}
	}

	public void saveAsTxt() {
		String outFileName = dataFile.getPathNameNoExt() + this.infix + TXT_TREE_SUFIX;
		ToTxt saveTxt = new ToTxt(distMatrix.getRoot(), precision, simType);
		try {
			saveTxt.saveAsTxt(outFileName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveAsNewick() {
		String outFileName = dataFile.getPathNameNoExt() + this.infix + NEWICK_TREE_SUFIX;
		double heightBottom, heightMin, heightMax, extraSpace;
		if (simType.equals(SimilarityType.DISTANCE)) {
			heightBottom = 0.0;
		} else {
			heightMin = distMatrix.getRoot().getBase();
			heightMax = distMatrix.maxValue();
			extraSpace = (heightMax - heightMin)
					* (0.05 * MathUtils.round((heightMax - heightMin),
							precision));
			extraSpace = MathUtils.round(extraSpace, precision);
			heightBottom = heightMax + extraSpace;
		}
		ToNewick saveNewick = new ToNewick(distMatrix.getRoot(), precision, simType, heightBottom);
		try {
			saveNewick.saveAsNewick(outFileName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveUltrametric() {
		String outFileName = dataFile.getPathNameNoExt() + this.infix + ULTRAMETRIC_SUFIX;
		if (ultraMatrix == null) {
			ultraMatrix = new UltrametricMatrix(data.getData(), distMatrix.getRoot(), precision);
		}
		try {
			ultraMatrix.saveAsTXT(outFileName, precision);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void printDeviationMeasures() {
		if (ultraMatrix == null) {
			ultraMatrix = new UltrametricMatrix(data.getData(), distMatrix.getRoot(), precision);
		}

		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		nf.setGroupingUsed(false);

		String errCC = nf.format(ultraMatrix.getCopheneticCorrelation());
		String errSE = nf.format(ultraMatrix.getSquaredError());
		String errAE = nf.format(ultraMatrix.getAbsoluteError());

		System.out.println("Cophenetic Correlation Coefficient : " + errCC);
		System.out.println("Normalized Mean Squared Error      : " + errSE);
		System.out.println("Normalized Mean Absolute Error     : " + errAE);
		System.out.println("---");
	}
}
