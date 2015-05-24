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
import multidendrograms.types.OriginType;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;
import multidendrograms.utils.SmartAxis;
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
	private final OriginType originType;
	private final double dendroBottomHeight;
	private PrintWriter printWriter;

	public ToNewick(final Cluster root, final int precision, final SimilarityType simType,
			final OriginType originType) {
		this.root = root;
		this.precision = precision;
		this.simType = simType;
		this.originType = originType;
		SmartAxis smartAxis = new SmartAxis(simType, precision, originType, root);
		if (simType.equals(SimilarityType.DISTANCE)) {
			this.dendroBottomHeight = smartAxis.smartMin();
		} else {// (simType.equals(SimilarityType.SIMILARITY))
			this.dendroBottomHeight = smartAxis.smartMax();
		}
	}

	public void saveAsNewick(String path) throws Exception {
		try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file);
            saveAsNewick(new PrintWriter(fileWriter));
		} catch (Exception e) {
			String errMsg = Language.getLabel(83);
			LogManager.LOG.throwing("ToNewick.java", "saveAsNewick()", e);
			throw new Exception(errMsg);
		}
	}

    public void saveAsNewick(PrintWriter printWriter) throws Exception {
        try {
            this.printWriter = new PrintWriter(printWriter);
            printCluster(this.root, this.root.getRootBottomHeight());
            this.printWriter.print(";");
            this.printWriter.close();
        } catch (Exception e) {
            String errMsg = Language.getLabel(83);
            LogManager.LOG.throwing("ToNewick.java", "saveAsNewick()", e);
            throw new Exception(errMsg);
        }
    }

	private void printCluster(final Cluster cluster, final double parentHeight)
			throws Exception {
		final int numSubclusters = cluster.getNumSubclusters();
		final double clusterBottomHeight = cluster.getRootBottomHeight();
		double clusterHeight;
		if (numSubclusters == 1) {
			String name = adaptName(cluster.getName());
			this.printWriter.print(name);
			if (Double.isNaN(clusterBottomHeight) || this.originType.equals(OriginType.UNIFORM_ORIGIN)) {
				clusterHeight = this.dendroBottomHeight;
			} else {
				clusterHeight = clusterBottomHeight;
			}
		} else {// (numSubclusters > 1)
			clusterHeight = clusterBottomHeight;
			this.printWriter.print("(");
			for (int n = 0; n < numSubclusters; n ++) {
				printCluster(cluster.getSubcluster(n), clusterHeight);
				if (n < numSubclusters - 1) {
					this.printWriter.print(",");
				}
			}
			this.printWriter.print(")");
		}
		printClusterLength(parentHeight, clusterHeight);
	}

	private String adaptName(final String originalName) {
		String newName = new String(originalName);
		newName = newName.replace(' ', '_');
		newName = newName.replace('\'', '"');
		newName = newName.replace(':', '|');
		newName = newName.replace(';', '|');
		newName = newName.replace(',', '|');
		newName = newName.replace('(', '{');
		newName = newName.replace(')', '}');
		newName = newName.replace('[', '{');
		newName = newName.replace(']', '}');
		return newName;
	}

	private void printClusterLength(final double parentHeight,
			final double clusterHeight) {
		double length;
		if (this.simType.equals(SimilarityType.DISTANCE)) {
			length = MathUtils.round(parentHeight - clusterHeight, this.precision);
		} else {
			length = MathUtils.round(clusterHeight - parentHeight, this.precision);
		}
		if (length > 0.0) {
			String sLength = MathUtils.format(length, this.precision);
			this.printWriter.print(":" + sLength);
		}
	}

}
