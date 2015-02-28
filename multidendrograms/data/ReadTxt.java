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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import multidendrograms.errors.IncompatibleFileError;
import multidendrograms.initial.Language;
import multidendrograms.initial.LogManager;
import multidendrograms.initial.InitialProperties;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Reads a text file containing a distances matrix in either list or matrix
 * format, with and without headers
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ReadTxt {

	private static final double NULL = -1.0;
	private final String fileName;
	private final LinkedList<String[]> dataList;
	private boolean check;
	private int numElements = 0;
	private String[] names = null;
	private LinkedList<SimilarityStruct<String>> data;
	private double missingValue = InitialProperties.getMissingValue();

	public ReadTxt(final String filePath, boolean check) throws Exception {
		fileName = filePath;
		dataList = saveInMemory();
		this.check = check;
		int numLines = dataList.size();
		int numCols = dataList.get(0).length;
		// Matrix or list format
		if (numCols != 3) {
			// Lower triangular or general matrix
			data = this.readMatrix();
		} else if (numCols == 3) {
			if ((numLines != 3) && (numLines != 4)) {
				data = this.readList();
			} else if (numLines == 4) {
				data = this.readMatrix();
			} else if (numLines == 3) {
				boolean typeL, typeM;
				LinkedList<SimilarityStruct<String>> lstL, lstM;
				try {
					typeL = true;
					lstL = this.readList();
				} catch (final Exception e) {
					typeL = false;
					lstL = null;
				}
				try {
					typeM = true;
					lstM = this.readMatrix();
				} catch (final Exception e) {
					typeM = false;
					lstM = null;
				}
				if (typeL && typeM) {
					// Unable to determine format
					final String msg = Language.getLabel(10);
					JOptionPane.showMessageDialog(null, msg, "Warning",
							JOptionPane.WARNING_MESSAGE);
					data = lstL;
				} else if (typeL) {
					data = lstL;
				} else if (typeM) {
					data = lstM;
				} else {
					throw new IncompatibleFileError(Language.getLabel(11));
				}
			}
		}

		if (LogManager.LOG.getLevel().equals(Level.FINER)) {
			LogManager.LOG.finer("---------- DATA ----------");
			for (final SimilarityStruct<?> s : data) {
				LogManager.LOG.finer(s.getC1() + "\t" + s.getC2() + "\t"
						+ s.getVal());
			}
		}
	}

	public int getNumElements() {
		return numElements;
	}

	public LinkedList<SimilarityStruct<String>> getData() {
		return data;
	}

	private LinkedList<String[]> saveInMemory() throws IncompatibleFileError, IOException {
		final LinkedList<String[]> lstData = new LinkedList<String[]>();

		final File f = new File(fileName);
		if (!f.exists()) {
			throw new IOException(Language.getLabel(106) + ": '" + fileName + "'");
		}

		int numLine = 1;
		try {

			final FileReader freader = new FileReader(f);
			BufferedReader buff = new BufferedReader(freader);

			// Read first line
			String line;
			if ((line = buff.readLine()) == null) {
				// Empty file
				throw new IncompatibleFileError(Language.getLabel(103) + " '"
						+ f.getName() + "'");
			}
			lstData.add(readLine(f, numLine, line));

			// Read the rest of the lines
			while ((line = buff.readLine()) != null) {
				numLine ++;
				lstData.add(readLine(f, numLine, line));
			}

			buff.close();

		} catch (IOException e) {
			throw new IOException(Language.getLabel(127) + " '" + fileName + "' "
					+ Language.getLabel(128) + " " + numLine);
		}

		return lstData;
	}

	private String[] readLine(File f, int numLine, String line) throws IncompatibleFileError {
		final String delims = " ,;|\t\n";
		StringTokenizer st = new StringTokenizer(line, delims);
		int numCols = st.countTokens();
		if (numCols < 1) {
			throw new IncompatibleFileError(Language.getLabel(104) + " "
					+ numLine + " " + Language.getLabel(105) + " '"
					+ f.getName() + "'");
		}
		String[] dataLine = new String[numCols];
		for (int col = 0; col < numCols; col ++) {
			dataLine[col] = st.nextToken();
		}
		return dataLine;
	}

	private LinkedList<SimilarityStruct<String>> readList() throws Exception {
		// Temporary list and hash table
		LinkedList<SimilarityStruct<String>> lstTmp = new LinkedList<SimilarityStruct<String>>();
		final Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		int ind = 0;
		int numLine = 1;
		Double value = null;
		int numWarnings = 0;
		for (final String[] s : dataList) {
			String a = s[0];
			String b = s[1];
			try {
				value = Double.parseDouble(s[2]);
			} catch (final NumberFormatException e) {
				// Data type error in third column
				throw new IncompatibleFileError(Language.getLabel(13)+ " " + numLine + ". "
						+ Language.getLabel(14));
			}
			if (!ht.containsKey(a)) {
				ht.put(a, ind ++);
			}
			if (!ht.containsKey(b)) {
				ht.put(b, ind ++);
			}
			if (a.equals(b)) {
				numWarnings ++;
				if (numWarnings < 3) {
					String msg = Language.getLabel(101) + ", " + Language.getLabel(102)	+ " " + numLine;
					LogManager.LOG.warning(msg);
				} else if (numWarnings == 3) {
					String msg = Language.getLabel(126);
					LogManager.LOG.warning(msg);
				}
			} else {
				lstTmp.add(new SimilarityStruct<String>(a, b, value));
			}
			numLine ++;
		}

		// Elements names
		numElements = ht.size();
		names = new String[numElements];
		final Enumeration<String> e = ht.keys();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			names[ht.get(name)] = name;
		}

		// Initialize matrix
		double[][] table = new double[numElements][numElements];
		for (int i = 0; i < numElements; i ++) {
			for (int j = 0; j < numElements; j ++) {
				table[i][j] = ReadTxt.NULL;
			}
		}

		LinkedList<SimilarityStruct<String>> lst = new LinkedList<SimilarityStruct<String>>();
		for (final SimilarityStruct<?> s : lstTmp) {
			int i = ht.get(s.getC1());
			int j = ht.get(s.getC2());
			value = s.getVal();
			if (i > j) {
				int aux = i;
				i = j;
				j = aux;
			}
			if (table[i][j] == ReadTxt.NULL) {
				table[i][j] = value;
				lst.add(new SimilarityStruct<String>(names[i], names[j], value));
			}
		}

		boolean first = true;
		for (int i = 0; i < numElements - 1; i ++) {
			for (int j = i + 1; j < numElements; j ++) {
				// Unassigned distances
				if (table[i][j] == ReadTxt.NULL) {
					if (first) {
						String msg = Language.getLabel(15) + ": " + missingValue;
						LogManager.LOG.warning(msg);
						first = false;
					}
					table[i][j] = missingValue;
					lst.add(new SimilarityStruct<String>(names[i], names[j], missingValue));
				}
			}
		}

		return lst;
	}

	private LinkedList<SimilarityStruct<String>> readMatrix() throws Exception {
		LinkedList<SimilarityStruct<String>> lst = null;
		boolean columnHeaders;
		boolean lowerTriangular;

		Iterator<String[]> iter = dataList.iterator();
		int numLines = dataList.size();
		int numCols = Math.max(dataList.get(0).length, dataList.get(numLines-1).length);
		int numLine = 1;
		if (numLines < numCols) {
			// Headers in first column
			columnHeaders = true;
			numElements = numLines;
			names = new String[numElements];
			if (dataList.get(0).length == 2) {
				// Lower triangular matrix
				lowerTriangular = true;
			} else {
				lowerTriangular = false;
			}
		} else {
			// (numLines >= numCols)
			columnHeaders = false;
			numElements = numCols;
			if (numLines > numCols) {
				// Headers in the first line
				names = iter.next();
				numLine ++;
				if (dataList.get(1).length == 1) {
					// Lower triangular matrix
					lowerTriangular = true;
				} else {
					lowerTriangular = false;
				}
			} else {
				// Without headers (numLines == numCols)
				names = new String[numElements];
				for (int n = 1; n <= numElements; n ++) {
					names[n-1] = Integer.toString(n);
				}
				if (dataList.get(0).length == 1) {
					// Lower triangular matrix
					lowerTriangular = true;
				} else {
					lowerTriangular = false;
				}
			}
		}
		lst = readTable(columnHeaders, lowerTriangular, numCols, numLine, iter);
		return lst;
	}

	private LinkedList<SimilarityStruct<String>> readTable(boolean columnHeaders, boolean lowerTriangular, int numCols,
			int numLine, Iterator<String[]> iter) throws IncompatibleFileError {
		double[][] table = new double[numElements][numElements];
		int row = 0;
		int numWarnings = 0;
		while (iter.hasNext()) {
			if (!columnHeaders && (row >= numCols)) {
				throw new IncompatibleFileError(Language.getLabel(100));
			}
			String[] tmp = iter.next();
			for (int col = 0; col < tmp.length; col ++) {
				if (columnHeaders && (col == 0)) {
					names[row] = tmp[0];
				} else if ((columnHeaders && (col == row + 1)) || (!columnHeaders && (col == row))) {
					table[row][row] = ReadTxt.NULL;
					if (check && (Double.parseDouble(tmp[col]) != 0)) {
						numWarnings ++;
						if (numWarnings < 3) {
							String msg = Language.getLabel(101) + ", " + Language.getLabel(102)	+ " " + numLine;
							LogManager.LOG.warning(msg);
						} else if (numWarnings == 3) {
							String msg = Language.getLabel(126);
							LogManager.LOG.warning(msg);
						}
					}
				} else if (columnHeaders) {
					try {
						table[row][col-1] = Double.parseDouble(tmp[col]);
						if (lowerTriangular) {
							table[col-1][row] = table[row][col-1];
						}
					} catch (NumberFormatException e) {
						throw new IncompatibleFileError(Language.getLabel(125));
					}
				} else if (!columnHeaders) {
					try {
						table[row][col] = Double.parseDouble(tmp[col]);
						if (lowerTriangular) {
							table[col][row] = table[row][col];
						}
					} catch (NumberFormatException e) {
						throw new IncompatibleFileError(Language.getLabel(125));
					}
				}
			}
			numLine ++;
			row ++;
		}

		// Check symmetry
		LinkedList<SimilarityStruct<String>> lst = new LinkedList<SimilarityStruct<String>>();
		for (int i = 0; i < numElements - 1; i ++) {
			for (int j = i + 1; j < numElements; j ++) {
				if (table[i][j] == table[j][i]) {
					lst.add(new SimilarityStruct<String>(names[i], names[j], table[i][j]));
				} else {
					// Non-symmetric matrix error
					throw new IncompatibleFileError(Language.getLabel(12));
				}
			}
		}
		return lst;
	}

}
