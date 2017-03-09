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

package multidendrograms.data;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import multidendrograms.initial.LogManager;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.DistancesMatrix;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Given a text file representing a distances matrix between elements,
 * gets all the distances between elements and stores them into a list
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ExternalData {

	private final DataFile df;
	LinkedList<SimilarityStruct<String>> data;
	private int numClusters = 0;
	private DistancesMatrix distMatrix;
	private int precision = 0;
	private Hashtable<Integer, String> names;

	public ExternalData(final DataFile df) throws Exception {
		this.df = new DataFile(df);

		ReadTxt txt = new ReadTxt(df.getPathName());
		this.data = txt.getData();
		this.numClusters = txt.getNumElements();

		LogManager.LOG.config("Creating a matrix for " + this.numClusters + " clusters");

		final Hashtable<String, Cluster> ht = new Hashtable<String, Cluster>();
		final CountDecimals countDecimals = new CountDecimals();
		this.distMatrix = new DistancesMatrix(this.numClusters);
		Cluster.resetId();
		Iterator<SimilarityStruct<String>> it = this.data.iterator();
		while (it.hasNext()) {
			SimilarityStruct<String> pair = it.next();

			Cluster cluster1;
			String key1 = pair.getC1();
			if (ht.containsKey(key1))
				cluster1 = ht.get(key1);
			else {
				cluster1 = new Cluster();
				cluster1.setName(key1);
				ht.put(key1, cluster1);
			}

			Cluster cluster2;
			String key2 = pair.getC2();
			if (ht.containsKey(key2))
				cluster2 = ht.get(key2);
			else {
				cluster2 = new Cluster();
				cluster2.setName(key2);
				ht.put(key2, cluster2);
			}

			double dist = pair.getValue();
			this.distMatrix.setDistance(cluster1, cluster2, dist);
			if (!Double.isNaN(dist)) {
				countDecimals.inValue(dist);
				if (key1.equals(key2)) {
					cluster1.setRootHeights(dist);
					cluster1.setNodesHeights(dist);
				}
			}
		}
		this.precision = countDecimals.getPrecision();

		this.names = new Hashtable<Integer, String>();
		Enumeration<String> e = ht.keys();
		while (e.hasMoreElements()) {
			String s = e.nextElement();
			this.names.put(ht.get(s).getId(), s);
		}
	}

	public DataFile getDataFile() {
		return this.df;
	}

	public Hashtable<Integer, String> getNames() {
		return this.names;
	}

	public DistancesMatrix getDistancesMatrix() {
		return this.distMatrix;
	}

	public LinkedList<SimilarityStruct<String>> getData() {
		return this.data;
	}

	public int getPrecision() {
		return this.precision;
	}

}
