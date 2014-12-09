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

import multidendrograms.definitions.Cluster;
import multidendrograms.initial.Language;
import multidendrograms.initial.LogManager;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Save dendrogram as text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToTxt {
	private final Cluster root;
	private final int precision;
	private final SimilarityType simType;
	private PrintWriter printWriter;

	public ToTxt(Cluster root, int precision, SimilarityType simType) {
		this.root = root;
		this.precision = precision;
		this.simType = simType;
	}

	public void saveAsTxt(String sPath) throws Exception {
		File file = new File(sPath);
		try {
			FileWriter fileWriter = new FileWriter(file);
			this.printWriter = new PrintWriter(fileWriter);
			showCluster(this.root, 0);
			this.printWriter.close();
		} catch (Exception e) {
			String errMsg = Language.getLabel(83);
			LogManager.LOG.throwing("ToTxt.java", "saveFile()", e);
			throw new Exception(errMsg);
		}
	}

	private void showCluster(final Cluster clus, final int level)
			throws Exception {
		double pmin, pmax, tmp;
		String spmin, spmax;
		String str = getIndentation(level);
		if (clus.getHeight() < 0.0) {
			str += "*  " + clus.getName();
		} else {
			pmin = clus.getHeight();
			if (simType.equals(SimilarityType.DISTANCE)) {
				pmax = pmin + clus.getAgglomeration();
			} else {
				pmax = pmin - clus.getAgglomeration();
			}
			if (pmin > pmax) {
				tmp = pmin;
				pmin = pmax;
				pmax = tmp;
			}
			pmin = MathUtils.round(pmin, precision);
			pmax = MathUtils.round(pmax, precision);
			spmin = MathUtils.format(pmin, precision);
			spmax = MathUtils.format(pmax, precision);
			str += "+ " + clus.getNumSubclusters() + "  [" + spmin + ", " + spmax + "]  " + clus.getNumLeaves();
		}
		// write
		printWriter.println(str);
		if (clus.getNumSubclusters() > 1) {
			for (int n = 0; n < clus.getNumSubclusters(); n ++) {
				showCluster(clus.getSubcluster(n), level + 1);
			}
		}
	}

	private String getIndentation(final int level) {
		String str = "";
		for (int n = 0; n < level; n ++) {
			str += "  ";
		}
		return str;
	}

}
