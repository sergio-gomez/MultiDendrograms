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
 * Save dendrogram as JSON text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToJson {

	private final Cluster root;
	private final int precision;
	private final SimilarityType simType;
	private final OriginType originType;
	private final double dendroBottomHeight;
	private PrintWriter printWriter;

	public ToJson(Cluster root, int precision, final SimilarityType simType, final OriginType originType) {
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

	public void saveAsJson(String sPath) throws Exception {
		File file = new File(sPath);
		try {
			FileWriter fileWriter = new FileWriter(file);
			printWriter = new PrintWriter(fileWriter);
			showCluster(this.root, this.root.getRootBottomHeight(), 0);
			printWriter.println("");
			printWriter.close();
		} catch (Exception e) {
			String errMsg = Language.getLabel(83);
			LogManager.LOG.throwing("ToTxt.java", "saveFile()", e);
			throw new Exception(errMsg);
		}
	}

	private void showCluster(final Cluster cluster, final double parentHeight, final int level) throws Exception {
		final int numSubclusters = cluster.getNumSubclusters();
		final double clusterBottomHeight = cluster.getRootBottomHeight();
		double clusterHeight, topHeight, bottomHeight;
		String indent = getIndentation(level);
		if (numSubclusters == 1) {
			if (Double.isNaN(clusterBottomHeight) || this.originType.equals(OriginType.UNIFORM_ORIGIN)) {
				clusterHeight = this.dendroBottomHeight;
			} else {
				clusterHeight = clusterBottomHeight;
			}
			topHeight = clusterHeight;
			bottomHeight = clusterHeight;
			String str = indent + "{\"name\": \"" + cluster.getName() + "\", ";
			str += getClusterHeight(clusterHeight) + ", ";
			str += getClusterMargin(topHeight, bottomHeight) + ", ";
			str += getClusterLength(parentHeight, clusterHeight) + ", ";
			str += "\"size\": 1}";
			printWriter.print(str);
		} else {
			clusterHeight = clusterBottomHeight;
			topHeight = cluster.getRootTopHeight();
			bottomHeight = cluster.getRootBottomHeight();
			printWriter.println(indent + "{");
			String str = indent + " \"name\": \"\", ";
			str += getClusterHeight(clusterHeight) + ", ";
			str += getClusterMargin(topHeight, bottomHeight) + ", ";
			str += getClusterLength(parentHeight, clusterHeight) + ",";
			printWriter.println(str);
			str = indent + " \"children\": [";
			printWriter.println(str);
			for (int n = 0; n < numSubclusters; n++) {
				showCluster(cluster.getSubcluster(n), clusterHeight, level + 1);
				if (n < numSubclusters - 1) {
  				printWriter.print(",");
  			}
  			printWriter.println("");
			}
			printWriter.println(indent + " ]");
			printWriter.print(indent + "}");
		}
	}

	private String getClusterHeight(final double clusterHeight) {
		String sHeight = MathUtils.format(clusterHeight, precision);
		return "\"height\": " + sHeight;
	}

	private String getClusterMargin(final double topHeight, final double bottomHeight) {
		double margin = Math.abs(topHeight - bottomHeight);
		String sMargin = MathUtils.format(margin, precision);
		return "\"margin\": " + sMargin;
	}

	private String getClusterLength(final double parentHeight, final double clusterHeight) {
		double length;
		if (this.simType.equals(SimilarityType.DISTANCE)) {
			length = MathUtils.round(parentHeight, precision) - MathUtils.round(clusterHeight, precision);
		} else {
			length = MathUtils.round(clusterHeight, precision) - MathUtils.round(parentHeight, precision);
		}
 		length = MathUtils.round(length, precision);
		String sLength = MathUtils.format(length, precision);
		return "\"length\": " + sLength;
	}

	private String getIndentation(final int level) {
		String str = "";
		for (int n = 0; n < level; n ++) {
			str += "  ";
		}
		return str;
	}

}
