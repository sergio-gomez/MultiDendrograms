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
import java.io.IOException;
import java.io.PrintWriter;

import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.utils.MathUtils;
import multidendrograms.utils.NumberUtils;

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

	private Dendrogram root;
	private PrintWriter printWriter;

	public ToTxt(Dendrogram root) {
		this.root = root;
	}

	public void saveAsTxt(String path) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file);
		this.printWriter = new PrintWriter(fileWriter);
		showCluster(this.root, "");
		this.printWriter.close();
	}

	private void showCluster(Dendrogram cluster, String blanks) {
		String str = blanks;
		if (cluster.numberOfSubclusters() == 1) {
			str += "*  " + cluster.getLabel();
		} else {
			double pmin = cluster.getRootBottomHeight();
			double pmax = cluster.getRootTopHeight();
			if (pmin > pmax) {
				double tmp = pmin;
				pmin = pmax;
				pmax = tmp;
			}
			int precision = this.root.precision;
			pmin = MathUtils.round(pmin, precision);
			pmax = MathUtils.round(pmax, precision);
			String spmin = NumberUtils.format(pmin, precision);
			String spmax = NumberUtils.format(pmax, precision);
			str += "+ " + cluster.numberOfSubclusters() + "  [" + spmin + 
					", " + spmax + "]  " + cluster.numberOfLeaves();
		}
		this.printWriter.println(str);
		if (cluster.numberOfSubclusters() > 1) {
			for (int n = 0; n < cluster.numberOfSubclusters(); n ++) {
				showCluster(cluster.getSubcluster(n), blanks + "  ");
			}
		}
	}

}
