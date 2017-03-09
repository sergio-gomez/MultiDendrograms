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
import multidendrograms.dendrogram.ToTxt;
import multidendrograms.dendrogram.ToNewick;
import multidendrograms.dendrogram.ToJson;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.methods.Method;
import multidendrograms.methods.BuildDendrogram;
import multidendrograms.types.MethodName;
import multidendrograms.types.OriginType;
import multidendrograms.types.SimilarityType;

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
	private static final String JSON_TREE_SUFIX = ".json";
	private static final String ULTRAMETRIC_SUFIX = "-ultrametric.txt";

	private String dataFileName;
	private SimilarityType simType;
	private MethodName method;
	private int precision;
	private OriginType originType;

	private String infix;
	private DataFile dataFile = null;
	private ExternalData externalData = null;
	private DistancesMatrix distMatrix = null;
	private UltrametricMatrix ultraMatrix = null;

	public DirectClustering(final String dataFileName, final SimilarityType simType,
			final MethodName method, final int precision, final OriginType originType) throws Exception {
		this.simType = simType;
		this.dataFileName = dataFileName;
		this.dataFile = new DataFile(dataFileName);
		try {
			this.externalData = new ExternalData(this.dataFile);
		} catch (Exception e) {
			throw e;
		}
		this.method = method;
		this.infix = "-" + Method.toShortName(method);
		if (precision == DirectClustering.AUTO_PRECISION) {
			this.precision = this.externalData.getPrecision();
		} else {
			this.precision = precision;
			this.infix = this.infix + precision;
		}
		this.originType = originType;

		System.out.println("Data file       : " + this.dataFileName);
		System.out.println("Similarity type : " + this.simType.toString().toLowerCase());
		System.out.println("Method name     : " + this.method.toString().toLowerCase());
		System.out.println("Precision       : " + this.precision);
		System.out.println("Origin          : " + this.originType.toString().toLowerCase());
		System.out.println("---");

		this.distMatrix = this.externalData.getDistancesMatrix();
		while (this.distMatrix.getCardinality() > 1) {
			try {
				BuildDendrogram bd = new BuildDendrogram(this.distMatrix, this.simType, this.method, this.precision);
				this.distMatrix = bd.recalculate();
			} catch (final Exception e) {
				throw e;
			}
		}
	}

	public void saveAsTxt() {
		String outFileName = this.dataFile.getPathNameNoExt() + this.infix + DirectClustering.TXT_TREE_SUFIX;
		ToTxt saveTxt = new ToTxt(this.distMatrix.getRoot(), this.precision);
		try {
			saveTxt.saveAsTxt(outFileName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveAsNewick() {
		String outFileName = this.dataFile.getPathNameNoExt() + this.infix + DirectClustering.NEWICK_TREE_SUFIX;
		Cluster root = this.distMatrix.getRoot();
		ToNewick saveNewick = new ToNewick(root, this.precision, this.simType, this.originType);
		try {
			saveNewick.saveAsNewick(outFileName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveAsJson() {
		String outFileName = this.dataFile.getPathNameNoExt() + this.infix + DirectClustering.JSON_TREE_SUFIX;
		Cluster root = this.distMatrix.getRoot();
		ToJson saveJson = new ToJson(root, this.precision, this.simType, this.originType);
		try {
			saveJson.saveAsJson(outFileName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void saveUltrametric() {
		String outFileName = this.dataFile.getPathNameNoExt() + this.infix + DirectClustering.ULTRAMETRIC_SUFIX;
		if (this.ultraMatrix == null) {
			this.ultraMatrix = new UltrametricMatrix(this.externalData.getData(), this.distMatrix.getRoot(),
					this.precision, this.simType, this.originType);
		}
		try {
			this.ultraMatrix.saveAsTxt(outFileName, this.precision);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void printDeviationMeasures() {
		if (this.ultraMatrix == null) {
			this.ultraMatrix = new UltrametricMatrix(this.externalData.getData(), this.distMatrix.getRoot(),
					this.precision, this.simType, this.originType);
		}

		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		nf.setGroupingUsed(false);

		String errCC = nf.format(this.ultraMatrix.getCopheneticCorrelation());
		String errSE = nf.format(this.ultraMatrix.getSquaredError());
		String errAE = nf.format(this.ultraMatrix.getAbsoluteError());

		System.out.println("Cophenetic Correlation Coefficient : " + errCC);
		System.out.println("Normalized Mean Squared Error      : " + errSE);
		System.out.println("Normalized Mean Absolute Error     : " + errAE);
		System.out.println("---");
	}

}
