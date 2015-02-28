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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import multidendrograms.initial.LogManager.LogType;
import multidendrograms.utils.VersionNumber;
import multidendrograms.direct.DirectClustering;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.children.UpgradeBox;
import multidendrograms.types.SimilarityType;
import multidendrograms.types.MethodName;
import multidendrograms.methods.Method;
import multidendrograms.errors.MethodError;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Main of MultiDendrograms application
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Main {

	public static final String PROGRAM = "MultiDendrograms";
	public static final String VERSION = "3.2.1";
	public static final String VERSION_SHORT = "3.2";
	public static final String AUTHORS = "Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres";
	public static final String ADVISORS = "Sergio Gomez, Alberto Fernandez";
	public static final String AFFILIATION = "Universitat Rovira i Virgili, Tarragona (Spain)";
	public static final String HOMEPAGE_URL = "http://deim.urv.cat/~sergio.gomez/multidendrograms.php";
	public static final String LICENSE_URL = "http://www.gnu.org/licenses/lgpl.html";
	public static final String MANUAL_URL = "http://deim.urv.cat/~sergio.gomez/download.php?f=multidendrograms-" + VERSION_SHORT + "-manual.pdf";
	public static final String LAST_VERSION_URL = "http://deim.urv.cat/~sergio.gomez/download.php?f=multidendrograms-last.txt";
	public static final String LOGO_IMAGE = "img/logo.png";

	private static final LogType LOG_XML = LogType.XML;
	private static final String LOG_FILE = "logs/md_log.xml";
	private static final String CONFIGURATION_FILE = "ini/dendo.ini";

	private static VersionNumber vn, vnWeb;

	public Main() {
		final String title = InitialProperties.getTitleDesk();
		PrincipalDesk desk = new PrincipalDesk(title);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = desk.getSize();
		desk.setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
		desk.setVisible(true);
	}

	public static void main(final String[] args) throws Exception {
		Level logLevel = Level.WARNING;
		Boolean isDirect = false;
		String fileName = "";
		SimilarityType simType = SimilarityType.DISTANCE;
		MethodName method = MethodName.UNWEIGHTED_AVERAGE;
		int precision = DirectClustering.AUTO_PRECISION;
		String arg;

		int i = 0;
		while (i < args.length) {
			arg = args[i].toUpperCase();
			if (arg.equals("-H") || arg.equals("-HELP")) {
				showSyntax();
				return;
			} else if (arg.equals("-LOGLEVEL")) {
				try {
					i++;
					arg = args[i].toUpperCase();
					if      (arg.equals("OFF"))
						logLevel = Level.OFF;
					else if (arg.equals("SEVERE"))
						logLevel = Level.SEVERE;
					else if (arg.equals("WARNING"))
						logLevel = Level.WARNING;
					else if (arg.equals("INFO"))
						logLevel = Level.INFO;
					else if (arg.equals("CONFIG"))
						logLevel = Level.CONFIG;
					else if (arg.equals("FINE"))
						logLevel = Level.FINE;
					else if (arg.equals("FINER"))
						logLevel = Level.FINER;
					else if (arg.equals("FINEST"))
						logLevel = Level.FINEST;
					else if (arg.equals("ALL"))
						logLevel = Level.ALL;
					else {
						System.out.println("Error: unknown log level '" + args[i] + "'");
						showSyntax();
						return;
					}
				} catch (Exception e) {
					showSyntax();
					return;
				}
			} else if (arg.equals("-DIRECT")) {
				try {
					// data file name
					i++;
					fileName = args[i];
					// similarity type
					i++;
					arg = args[i].toUpperCase();
					if      (arg.equals("D") || arg.equals("DISTANCES"))
						simType = SimilarityType.DISTANCE;
					else if (arg.equals("W") || arg.equals("WEIGHTS"))
						simType = SimilarityType.WEIGHT;
					else {
						System.out.println("Error: unknown similarity type '" + args[i] + "'");
						showSyntax();
						return;
					}
					// agglomeration type
					i++;
					try {
						method = Method.toMethod(args[i]);
					} catch (MethodError e) {
						System.out.println("Error: unknown method name '" + args[i] + "'");
						showSyntax();
						return;
					}
					// precision
					i++;
					if (i < args.length) {
						precision = Integer.parseInt(args[i]);
					}
					isDirect = true;
				} catch (Exception e) {
					showSyntax();
					return;
				}
			} else {
				showSyntax();
				return;
			}
			i++;
		}

		new LogManager(Main.LOG_FILE, Main.LOG_XML);
		LogManager.LOG.setLevel(logLevel);
		LogManager.LOG.fine("Start program");

		// initial properties
		try {
			MainProperties props = new MainProperties(Main.CONFIGURATION_FILE);
			InitialProperties ip = new InitialProperties();
			ip.setParametres(props);
		} catch (Exception e) {
			LogManager.LOG.severe(e.getMessage());
			if (!isDirect) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						Main.PROGRAM, JOptionPane.OK_OPTION);
			}
		}

		// language file
		try {
			new Language(InitialProperties.getSPath_language());
			LogManager.LOG.config("Language loaded: "
					+ InitialProperties.getSPath_language());
		} catch (Exception e) {
			LogManager.LOG.warning("Loading default language");
			if (!isDirect) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						Main.PROGRAM, JOptionPane.OK_OPTION);
			}
		}

		if (hasUpgrade()) {
			if (isDirect) {
				String str = Language.getLabel(129) + " " + Main.PROGRAM + " " + vnWeb.getVersion() + " "
				+ Language.getLabel(130) + " " + Main.HOMEPAGE_URL;
				System.out.println(str);
				System.out.println("---");
			} else {
				UpgradeBox upgr = new UpgradeBox(vn.getVersion(), vnWeb.getVersion());
				upgr.setVisible(true);
			}
		}

		if (isDirect) {
			try {
				DirectClustering dirClus = new DirectClustering(fileName, simType, method, precision);
				dirClus.saveAsTxt();
				dirClus.saveAsNewick();
				dirClus.saveUltrametric();
				dirClus.printDeviationMeasures();
			} catch (Exception e) {
				if (precision == DirectClustering.AUTO_PRECISION) {
					System.out.println("Parameters: -direct " + fileName + " " + simType + " " + method);
				} else {
					System.out.println("Parameters: -direct " + fileName + " " + simType + " " + method + " " + precision);
				}
				LogManager.LOG.severe(e.getMessage());
			}
		} else {
			try {
				new Main();
			} catch (Exception e) {
				LogManager.LOG.severe(e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage(),
						Main.PROGRAM, JOptionPane.OK_OPTION);
			}
		}
	}

	private static void showSyntax() {
		System.out.println("Usage: java -jar multidendrograms.jar [ options ]");
		System.out.println("");
		System.out.println("    -loglevel  LEVEL");
		System.out.println("        LEVEL     : verbosity level of the logger");
		System.out.println("                      OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL");
		System.out.println("");
		System.out.println("    -direct  FILE_NAME  SIM_TYPE  METHOD  [ PRECISION ]");
		System.out.println("        direct calculation of multidendrogram without graphic interface");
		System.out.println("        FILE_NAME : name of the data file name");
		System.out.println("        SIM_TYPE  : similarity type");
		System.out.println("                      D, DISTANCES,");
		System.out.println("                      W, WEIGHTS");
		System.out.println("        METHOD    : agglomeration type");
		System.out.println("                      SL, SINGLE_LINKAGE,");
		System.out.println("                      CL, COMPLETE_LINKAGE,");
		System.out.println("                      UA, UNWEIGHTED_AVERAGE,");
		System.out.println("                      WA, WEIGHTED_AVERAGE,");
		System.out.println("                      UC, UNWEIGHTED_CENTROID,");
		System.out.println("                      WC, WEIGHTED_CENTROID,");
		System.out.println("                      WD, WARD");
		System.out.println("        PRECISION : number of decimal significant digits, auto if missing value");
		System.out.println("");
		System.out.println("    -h | -help");
		System.out.println("        syntax help");
		System.out.println("");
		System.out.println("");
		System.out.println("Examples: java -jar multidendrograms.jar");
		System.out.println("          java -jar multidendrograms.jar -loglevel OFF");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt D CL");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt D CL 3");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt DISTANCES Complete_Linkage 3");
	}

	private static boolean hasUpgrade() {
		boolean upgradeable = false;
		try {
			URL url = new URL(Main.LAST_VERSION_URL);
			InputStreamReader istream = new InputStreamReader(url.openStream());
			BufferedReader in = new BufferedReader(istream);
			String verWeb = in.readLine();
			vn = new VersionNumber(Main.VERSION);
			vnWeb = new VersionNumber(verWeb);
			if (vnWeb.newerThan(vn)) {
				upgradeable = true;
			}
		} catch (Exception e) {
			upgradeable = false;
//			e.printStackTrace();
		}
		return upgradeable;
	}
}
