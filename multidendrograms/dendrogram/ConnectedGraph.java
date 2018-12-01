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

import multidendrograms.core.definitions.SymmetricMatrix;
import multidendrograms.types.ProximityType;
import multidendrograms.utils.NumberUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Prune and save connected graph
 *
 * @author Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * @since JDK 6.0
 */
public class ConnectedGraph {

	private SymmetricMatrix proxiMatrix;
	private String[] labels;
	private ProximityType proxiType;
	private int precision;
	private SymmetricMatrix ultraMatrix;

	public ConnectedGraph(SymmetricMatrix proximityMatrix, String[] labels, ProximityType proximityType,
	    int precision, SymmetricMatrix ultrametricMatrix) {
		this.proxiMatrix = proximityMatrix;
		this.labels = labels;
		this.proxiType = proximityType;
		this.precision = precision;
		this.ultraMatrix = ultrametricMatrix;
	}

	public void saveAsNet(String path) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println("*Vertices " + this.labels.length);
		for (int i = 0; i < this.labels.length; i ++) {
			printWriter.println((i + 1) + " " + this.labels[i]);
		}
		boolean isDistanceBased = this.proxiType.equals(ProximityType.DISTANCE) ? true : false;
		printWriter.println("*Edges");
		for (int i = 0; i < this.proxiMatrix.numberOfRows(); i ++) {
			for (int j = i + 1; j < this.proxiMatrix.numberOfRows(); j ++) {
				double proxiIJ = this.proxiMatrix.getElement(i, j);
				double ultraIJ = this.ultraMatrix.getElement(i, j);
				if (( isDistanceBased && (proxiIJ <= ultraIJ)) || (!isDistanceBased && (proxiIJ >= ultraIJ))) {
					printWriter.println((i + 1) + " " + (j + 1) + " " + NumberUtils.format(proxiIJ, this.precision));
				}
			}
		}
		printWriter.close();
	}

}
