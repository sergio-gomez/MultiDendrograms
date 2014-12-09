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

package multidendrograms.dendrogram;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;
import multidendrograms.definitions.Cluster;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Save dendrogram as Newick tree text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToNewick {

	private final Cluster root;
	private final int precision;
	private final SimilarityType simType;
	private final double heightBottom;
	private PrintWriter printWriter;

	public ToNewick(Cluster root, int precision, SimilarityType simType,
			double heightBottom) {
		this.root = root;
		this.precision = precision;
		this.simType = simType;
		this.heightBottom = heightBottom;
	}

	public void saveAsNewick(String sPath) throws Exception {
		File file;
		FileWriter fileWriter;
		String errMsg;

		file = new File(sPath);
		try {
			fileWriter = new FileWriter(file);
			this.printWriter = new PrintWriter(fileWriter);
			showCluster(this.root, this.root.getSummaryHeight());
			this.printWriter.print(";");
			this.printWriter.close();
		} catch (Exception e) {
			errMsg = Language.getLabel(83);
			LogManager.LOG.throwing("ToNewick.java", "saveAsNewick()", e);
			throw new Exception(errMsg);
		}
	}

	private void showCluster(final Cluster cluster, final double heightParent)
			throws Exception {
		double length;
		String slength;

		double heightCurrent = cluster.getSummaryHeight();
		if (heightCurrent < 0.0) {
			String name = cluster.getName();
			name = name.replace(' ', '_');
			name = name.replace('\'', '"');
			name = name.replace(':', '|');
			name = name.replace(';', '|');
			name = name.replace(',', '|');
			name = name.replace('(', '{');
			name = name.replace(')', '}');
			name = name.replace('[', '{');
			name = name.replace(']', '}');
			this.printWriter.print(name);
			if (this.simType.equals(SimilarityType.DISTANCE)) {
				length = MathUtils.round(heightParent - this.heightBottom, this.precision);
			} else {
				length = MathUtils.round(this.heightBottom - heightParent, this.precision);
			}
			if (length > 0) {
				slength = MathUtils.format(length, this.precision);
				this.printWriter.print(":" + slength);
			}
		} else if (cluster.getNumSubclusters() > 1) {
			this.printWriter.print("(");
			for (int n = 0; n < cluster.getNumSubclusters(); n ++) {
				showCluster(cluster.getSubcluster(n), heightCurrent);
				if (n < cluster.getNumSubclusters() - 1) {
					this.printWriter.print(",");
				}
			}
			this.printWriter.print(")");
			if (this.simType.equals(SimilarityType.DISTANCE)) {
				length = MathUtils.round(heightParent - heightCurrent, this.precision);
			} else {
				length = MathUtils.round(heightCurrent - heightParent, this.precision);
			}
			if (length > 0) {
				slength = MathUtils.format(length, this.precision);
				this.printWriter.print(":" + slength);
			}
		}
	}

}
