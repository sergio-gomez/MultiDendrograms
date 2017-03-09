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

package multidendrograms.initial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JOptionPane;

import multidendrograms.errors.Warning;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Each label in the program is identified by an index and it is assigned a string
 * that has been previously loaded from a file; the language of the application
 * can be changed just replacing the file to load
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Language {

	private int index = 0;
	private static Vector<String> labels = null;

	public Language(String path) throws Exception {
		try {
			loadLabels(path);
		} catch (Warning w) {
			JOptionPane.showMessageDialog(null, w.getMessage(), "Dendrogram",
					JOptionPane.OK_OPTION);
			LogManager.LOG.warning(w.getMessage());
		} catch (final Exception e) {
			String errMsg = Language.getLabel(68);
			LogManager.LOG.throwing("Language", "Language(String path)", e);
			throw new Exception(errMsg);
		}
	}

	public static String getLabel(int i) {
		return Language.labels.get(i);
	}

	private void loadLabels(String path) throws Exception {
		Language.labels = new Vector<String>();
		try {
			File f = new File(path);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String label;
			while ((label = br.readLine()) != null) {
				Language.labels.add(this.index++, label);
			}
			br.close();
		} catch (final Exception e) {
			String err = e.toString();
			err = "WARNING: cannot load language file (" + path
					+ "). Loading default language\n" + err;
			this.getDefaultLangLabels();
			throw new Warning(err);
		}
	}

	private void getDefaultLangLabels() {
		final String[] str = {
			"Are you sure you want to exit?",//0
			"Color selection",//1
			"Invalid value in 'Precision' field",//2
			"Invalid value in 'Minimum value' field",//3
			"Invalid value in 'Maximum value' field",//4
			"Invalid value in 'Ticks separation' field",//5
			"Invalid value in 'Labels every' field",//6
			"Error",//7
			"The file was not loaded",//8
			"Working data",//9
			"Ambiguous file. It will be taken as a list-like file",//10
			"The loaded file is not compatible with this application",//11
			"Asymmetric data: two different values for the same data pair",//12
			"Error at line",//13
			"The third column must contain a numeric value",//14
			"Missing data values, filling them with the default value in 'md.ini' file",//15
			"Incompatible file",//16
			"The cluster is not in the group",//17
			"Unable to recover the cluster",//18
			"Incompatible min. and max. values",//19
			"File",//20
			"Load",//21
			"View",//22
			"Settings",//23
			"Clustering algorithm",//24
			"Dendrogram",//25
			"Tree orientation",//26
			"Distance",//27
			"Similarity",//28
			"Tree",//29
			"Nodes",//30
			"Show labels",//31
			"Nodes",//32
			"Labels orientation",//33
			"Font",//34
			"Color",//35
			"Axis",//36
			"Show axis",//37
			"Color",//38
			"Show labels",//39
			"Color",//40
			"Minimum value",//41
			"Maximum value",//42
			"Ticks separation",//43
			"Labels every",//44
			"Font",//45
			"Exit",//46
			"An unknown event was received",//47
			"Show bands",//48
			"Labels decimals",//49
			"Invalid value in 'Labels decimals' field",//50
			"Precision",//51
			"Info",//52
			"Algorithm parameter",//53
			"Bold",//54
			"Italic",//55
			"Plain",//56
			"Selected font",//57
			"Font selection",//58
			"Font",//59
			"OK",//60
			"Cancel",//61
			"Root",//62
			"Weighted",//63
			"Unable to process the dendrogram figure",//64
			"File header not found, default values will be loaded",//65
			"Property not found",//66
			"Unable to create the properties file",//67
			"'Algorithm parameter' must lie between -1 and +1",//68
			"Unknown error",//69
			"Unable to recover the cluster",//70
			"Unable to save the distance",//71
			"Unable to recover the matrix",//72
			"Unable to initialize the matrix",//73
			"Unable to load the dendrogram properties",//74
			"Save as",//75
			"Unable to save the image",//76
			"Null image",//77
			"Unable to build the hierarchy tree",//78
			"Save dendrogram as JSON tree",//79
			"Clustering algorithm incompatible with negative data",//80
			"Unable to create the file",//81
			"Existing file. Do you want to overwrite it?",//82
			"Cannot write",//83
			"Cannot load",//84
			"Icon not found",//85
			"Unable to recover the precision value",//86
			"Save dendrogram as Newick tree",//87
			"North",//88
			"South",//89
			"East",//90
			"West",//91
			"Horizontal",//92
			"Oblique",//93
			"Vertical",//94
			"Show dendrogram tree structure",//95
			"Save dendrogram as JPG",//96
			"Save dendrogram as PNG",//97
			"Save dendrogram as TXT",//98
			"Save dendrogram as EPS",//99
			"The number of rows is larger than the number of columns",//100
			"A number not equal to 0 has been found in the diagonal",//101
			"value discarded in line",//102
			"There is no data in the file",//103
			"The columns count is wrong at line",//104
			"of the file",//105
			"File not found",//106
			"Cannot read/write to file",//107
			"Cannot write to file",//108
			"Cannot read from file",//109
			"Update",//110
			"Save dendrogram measures",//111
			"No file loaded",//112
			"Nodes size",//113
			"Type of measure",//114
			"ticks",//115
			"Save ultrametric matrix",//116
			"Show dendrogram measures",//117
			"Version",//118
			"Developers",//119
			"Advisors",//120
			"GNU Lesser Public License",//121
			"Project website",//122
			"Dendrogram measures",//123
			"Manual online",//124
			"A non-numeric character has been found in a row or column larger than 1",//125
			"Additional warning messages skipped",//126
			"Error reading file",//127
			"at line",//128
			"New",//129
			"available at",//130
			"Current version is",//131
			"Uniform origin",//132
			"Equivalences between clustering algorithms"//133
		};

		Language.labels = new Vector<String>();
		this.index = 0;
		for (final String s : str) {
			Language.labels.add(this.index++, s);
		}
	}

}
