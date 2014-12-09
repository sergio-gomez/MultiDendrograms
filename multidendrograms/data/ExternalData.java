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
	private boolean check;
	private Hashtable<Integer, String> names;
	private DistancesMatrix distMatrix;
	LinkedList<SimilarityStruct<String>> data;
	private int numClusters = 0;
	private int precision = 0;

	public ExternalData(final DataFile df, boolean check) throws Exception {
		this.df = new DataFile(df);
		this.check = check;
		fillDistancesMatrix();
	}

	private void fillDistancesMatrix() throws Exception {
		SimilarityStruct<String> pair;
		Iterator<SimilarityStruct<String>> it;
		final CountDecimals cp = new CountDecimals();

		DistancesMatrix dm;

		readData();

		LogManager.LOG.config("Creating a matrix for " + numClusters + " clusters");

		Cluster.resetId();
		dm = new DistancesMatrix(numClusters);
		it = data.iterator();

		final Hashtable<String, Cluster> ht = new Hashtable<String, Cluster>();
		Cluster c1, c2;

		while (it.hasNext()) {
			pair = it.next();

			if (ht.containsKey(pair.getC1()))
				c1 = ht.get(pair.getC1());
			else {
				c1 = new Cluster();
				c1.setName(pair.getC1());
				ht.put(pair.getC1(), c1);
			}

			if (ht.containsKey(pair.getC2()))
				c2 = ht.get(pair.getC2());
			else {
				c2 = new Cluster();
				c2.setName(pair.getC2());
				ht.put(pair.getC2(), c2);
			}
			cp.inValue(pair.getVal());
			dm.setDistance(c1, c2, pair.getVal());
		}
		precision = cp.getPrecision();

		distMatrix = dm;
		names = new Hashtable<Integer, String>();
		String s;
		Enumeration<String> e = ht.keys();
		while (e.hasMoreElements())
			s = e.nextElement();

		e = ht.keys();
		while (e.hasMoreElements()) {
			s = e.nextElement();
			names.put(ht.get(s).getId(), s);
		}
	}

	private void readData() throws Exception {
		ReadTxt txt = new ReadTxt(df.getPathName(), check);
		data = txt.getData();
		numClusters = txt.getNumElements();
	}

	public DataFile getDataFile() {
		return df;
	}

	public Hashtable<Integer, String> getNames() {
		return names;
	}

	public DistancesMatrix getDistancesMatrix() {
		return distMatrix;
	}

	public LinkedList<SimilarityStruct<String>> getData() {
		return data;
	}

	public int getPrecision() {
		return precision;
	}

}
