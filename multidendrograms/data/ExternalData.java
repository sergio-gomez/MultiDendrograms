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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import multidendrograms.core.definitions.SymmetricMatrix;
import multidendrograms.initial.LogManager;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Given a text file representing a proximity matrix between elements,
 * gets all the proximity values between elements and stores them into a list
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ExternalData {

	private final DataFile dataFile;
	LinkedList<ProximityPair<String>> proximityPairs;
	private int numElements = 0;
	private SymmetricMatrix proximityMatrix;
	private Hashtable<String, Integer> hashNames;
	private LinkedList<String> names;

	public ExternalData(final DataFile dataFile) throws Exception {
		this.dataFile = new DataFile(dataFile);

		ReadTxt txt = new ReadTxt(dataFile.getPathName());
		this.proximityPairs = txt.getData();
		this.numElements = txt.getNumElements();

		LogManager.LOG.config("Creating a matrix for " + this.numElements + " elements");

		this.proximityMatrix = new SymmetricMatrix(this.numElements);
		this.hashNames = new Hashtable<String, Integer>();
		this.names = new LinkedList<String>();
		int nextId = 0;
		Iterator<ProximityPair<String>> iterPairs = this.proximityPairs.iterator();
		while (iterPairs.hasNext()) {
			ProximityPair<String> pair = iterPairs.next();

			int id1;
			String name1 = pair.getElement1();
			if (this.hashNames.containsKey(name1)) {
				id1 = this.hashNames.get(name1);
			} else {
				id1 = nextId;
				nextId ++;
				this.hashNames.put(name1, id1);
				this.names.add(name1);
			}
			
			int id2;
			String name2 = pair.getElement2();
			if (this.hashNames.containsKey(name2)) {
				id2 = this.hashNames.get(name2);
			} else {
				id2 = nextId;
				nextId ++;
				this.hashNames.put(name2, id2);
				this.names.add(name2);
			}
			
			double proximity = pair.getProximity();
			this.proximityMatrix.setElement(id1, id2, proximity);
		}
	}

	public DataFile getDataFile() {
		return this.dataFile;
	}

	public String[] getNames() {
		return this.names.toArray(new String[this.names.size()]);
	}

	public int getNumberOfElements() {
		return this.numElements;
	}

	public SymmetricMatrix getProximityMatrix() {
		return this.proximityMatrix;
	}

	public LinkedList<ProximityPair<String>> getData() {
		return this.proximityPairs;
	}

	public int getPrecision() {
		return this.proximityMatrix.getPrecision();
	}

}
