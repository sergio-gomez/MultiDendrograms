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
import multidendrograms.types.OriginType;
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
	public static final String VERSION = "4.0.0";
	public static final String VERSION_SHORT = "4.0";
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
	private static final String CONFIGURATION_FILE = "ini/md.ini";

	private static VersionNumber vn, vnWeb;
	private static Boolean isDirect = false;

	public Main() {
		final String title = Main.PROGRAM;
		PrincipalDesk desk = new PrincipalDesk(title);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = desk.getSize();
		desk.setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
		desk.setVisible(true);
	}

	public static void main(final String[] args) throws Exception {
		Level logLevel = Level.WARNING;
		String fileName = "";
		SimilarityType simType = SimilarityType.DISTANCE;
		MethodName method = MethodName.UNWEIGHTED_AVERAGE;
		int precision = DirectClustering.AUTO_PRECISION;
		OriginType originType = OriginType.UNIFORM_ORIGIN;

		int i = 0;
		while (i < args.length) {
			String arg = args[i].toUpperCase();
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
					// proximity type
					i++;
					arg = args[i].toUpperCase();
					if (arg.equals("D") || arg.equals("DIST") || arg.equals("DISTANCE") || arg.equals("DISTANCES")) {
						simType = SimilarityType.DISTANCE;
					} else if (arg.equals("S") || arg.equals("SIM") || arg.equals("SIMILARITY") || arg.equals("SIMILARITIES")) {
						simType = SimilarityType.SIMILARITY;
					} else {
						System.out.println("Error: unknown proximity type '" + args[i] + "'");
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
					// precision and/or origin type
					i++;
					if (i < args.length) {
						try {
							precision = Integer.parseInt(args[i]);
							i++;
						} catch (NumberFormatException e) {}
						if (i < args.length) {
							arg = args[i].toUpperCase();
							if (arg.equals("UO") || arg.equals("UNIFORM_ORIGIN")) {
								originType = OriginType.UNIFORM_ORIGIN;
							} else if (arg.equals("NUO") || arg.equals("NON_UNIFORM_ORIGIN")) {
								originType = OriginType.NON_UNIFORM_ORIGIN;
							} else {
								System.out.println("Error: unknown origin type '" + args[i] + "'");
								showSyntax();
								return;
							}
						}
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
			new MainProperties(Main.CONFIGURATION_FILE);
			new InitialProperties();
		} catch (Exception e) {
			LogManager.LOG.severe(e.getMessage());
			if (!isDirect) {
				JOptionPane.showMessageDialog(null, e.getMessage(), Main.PROGRAM, JOptionPane.OK_OPTION);
			}
		}

		// language file
		try {
			new Language(InitialProperties.getLanguage());
			LogManager.LOG.config("Language loaded: " + InitialProperties.getLanguage());
		} catch (Exception e) {
			LogManager.LOG.warning("Loading default language");
			if (!isDirect) {
				JOptionPane.showMessageDialog(null, e.getMessage(), Main.PROGRAM, JOptionPane.OK_OPTION);
			}
		}

    Thread checkVersion = new Thread() {
	    public void run() {
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
		  }
  	};
  	checkVersion.start();

		if (isDirect) {
			try {
				DirectClustering dirClus = new DirectClustering(fileName, simType, method, precision, originType);
				dirClus.saveAsTxt();
				dirClus.saveAsNewick();
				dirClus.saveUltrametric();
				dirClus.printDeviationMeasures();
			} catch (Exception e) {
				String parameters = "Parameters: -direct " + fileName + " " + simType + " " + method;
				if (precision != DirectClustering.AUTO_PRECISION) {
					parameters += " " + precision;
				}
				System.out.println(parameters);
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
		System.out.println("    -h | -help");
		System.out.println("        Syntax help");
		System.out.println("");
		System.out.println("    -loglevel  LEVEL");
		System.out.println("        Sets de verbosity level of the logger");
		System.out.println("        LEVEL     : verbosity level, one of");
		System.out.println("                      OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL");
		System.out.println("        Default value of LEVEL: WARNING");
		System.out.println("");
		System.out.println("    -direct  FILE_NAME  PROX_TYPE  METHOD  [ PRECISION ]  [ ORIGIN ]");
		System.out.println("        Direct calculation of the multidendrogram without graphic interface");
		System.out.println("        FILE_NAME : name of the data file");
		System.out.println("        PROX_TYPE : proximity type, one of");
		System.out.println("                      D, DIST, DISTANCE, DISTANCES");
		System.out.println("                      S, SIM, SIMILARITY, SIMILARITIES");
		System.out.println("        METHOD    : agglomeration type, one of");
		System.out.println("                      SL, SINGLE_LINKAGE");
		System.out.println("                      CL, COMPLETE_LINKAGE");
		System.out.println("                      UA, UNWEIGHTED_AVERAGE");
		System.out.println("                      WA, WEIGHTED_AVERAGE");
		System.out.println("                      UC, UNWEIGHTED_CENTROID");
		System.out.println("                      WC, WEIGHTED_CENTROID");
		System.out.println("                      WD, WARD");
		System.out.println("        PRECISION : number of decimal significant digits, auto if missing value");
		System.out.println("        ORIGIN    : origin type, one of");
		System.out.println("                      UO, UNIFORM_ORIGIN");
		System.out.println("                      NUO, NON_UNIFORM_ORIGIN");
		System.out.println("        Default value of ORIGIN: UNIFORM_ORIGIN");
		System.out.println("");
		System.out.println("");
		System.out.println("Examples: java -jar multidendrograms.jar");
		System.out.println("          java -jar multidendrograms.jar -loglevel OFF");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt DISTANCES Complete_Linkage 3");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt D CL");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt D CL 3");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt D CL UO");
		System.out.println("          java -jar multidendrograms.jar -direct data.txt D CL 3 NUO");
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
		}
		return upgradeable;
	}

}
