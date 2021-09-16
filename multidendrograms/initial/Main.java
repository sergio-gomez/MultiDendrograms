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
import javax.swing.ToolTipManager;

import multidendrograms.initial.LogManager.LogType;
import multidendrograms.utils.VersionNumber;
import multidendrograms.direct.DirectClustering;
import multidendrograms.errors.MethodError;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.children.UpgradeBox;
import multidendrograms.types.BandHeight;
import multidendrograms.types.MethodType;
import multidendrograms.types.OriginType;
import multidendrograms.types.ProximityType;

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
	public static final String VERSION = "5.2.0";
	public static final String VERSION_SHORT = "5.2";
	public static final String AUTHORS = "Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres";
	public static final String ADVISORS = "Sergio Gomez, Alberto Fernandez";
	public static final String AFFILIATION = "Universitat Rovira i Virgili, Tarragona (Spain)";
	public static final String HOMEPAGE_URL = "https://deim.urv.cat/~sergio.gomez/multidendrograms.php";
	public static final String LICENSE_URL = "https://www.gnu.org/licenses/lgpl.html";
	public static final String MANUAL_URL = "https://deim.urv.cat/~sergio.gomez/download.php?f=multidendrograms-" + VERSION_SHORT + "-manual.pdf";
	public static final String LAST_VERSION_URL = "https://deim.urv.cat/~sergio.gomez/download.php?f=multidendrograms-last.txt";
	public static final String LOGO_IMAGE = "img/logo.png";
	public static final String JRE_VERSION = System.getProperty("java.specification.version");

	private static final LogType LOG_XML = LogType.XML;
	private static final String LOG_FILE = "logs/md_log.xml";
	private static final String CONFIGURATION_FILE = "ini/md.ini";
	private static final int TOOLTIP_DISMISS_DELAY = 20000;

	private static VersionNumber vn, vnWeb;
	private static Boolean isHelp = false;
	private static Boolean isDirect = false;

	public Main() {
		final String title = Main.PROGRAM;
		PrincipalDesk desk = new PrincipalDesk(title);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = desk.getSize();
		ToolTipManager.sharedInstance().setDismissDelay(TOOLTIP_DISMISS_DELAY);
		desk.setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
		desk.setVisible(true);
	}

	public static void main(final String[] args) throws Exception {
		Level logLevel = Level.WARNING;
		String fileName = "";
		ProximityType proximityType = ProximityType.DISTANCE;
		int precision = DirectClustering.AUTO_PRECISION;
		MethodType methodType = MethodType.ARITHMETIC_LINKAGE;
		double methodParameter = 0.0;
		boolean isWeighted = false;
		OriginType originType = OriginType.UNIFORM_ORIGIN;

		int i = 0;
		while (i < args.length) {
			String arg = args[i].toUpperCase();
			if (arg.equals("-H") || arg.equals("-HELP")) {
				isHelp = true;
				isDirect = true;
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
						proximityType = ProximityType.DISTANCE;
					} else if (arg.equals("S") || arg.equals("SIM") || arg.equals("SIMILARITY") || arg.equals("SIMILARITIES")) {
						proximityType = ProximityType.SIMILARITY;
					} else {
						System.out.println("Error: unknown proximity type '" + args[i] + "'");
						showSyntax();
						return;
					}
					// precision and method type
					i++;
					try {
						precision = Integer.parseInt(args[i]);
						i++;
					} catch (NumberFormatException e) {}
					try {
						methodType = MethodName.toMethodType(args[i]);
					} catch (MethodError e) {
						System.out.println("Error: unknown method name '" + args[i] + "'");
						showSyntax();
						return;
					}
					// method parameter, weighted and origin type
					i++;
					if (i < args.length) {
						try {
							methodParameter = Double.parseDouble(args[i]);
							if ((methodType == MethodType.BETA_FLEXIBLE) &&
									((methodParameter < -1.0) || (+1.0 < methodParameter))) {
								System.out.println(Language.getLabel(68));
								showSyntax();
								return;
							}
							i++;
						} catch (NumberFormatException e) {}
						if (i < args.length) {
							arg = args[i].toUpperCase();
							if (arg.equals("W") || arg.equals("WEIGHTED")) {
								isWeighted = true;
								i++;
							} else if (arg.equals("UW") || arg.equals("UNWEIGHTED")) {
								isWeighted = false;
								i++;
							}
						}
						if (i < args.length) {
							arg = args[i].toUpperCase();
							if (arg.equals("UO") || arg.equals("UNIFORM_ORIGIN")) {
								originType = OriginType.UNIFORM_ORIGIN;
							} else if (arg.equals("NUO") || arg.equals("NON_UNIFORM_ORIGIN")) {
								originType = OriginType.NON_UNIFORM_ORIGIN;
							} else {
								System.out.println("Error: unknown parameter '" + args[i] + "'");
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

		if (isHelp) {
			showSyntax();
			checkVersion.start();
			return;
		} else {
			checkVersion.start();
		}

		if (isDirect) {
			// check Centroid and Ward only for Distance, not for Similarity
			if ((proximityType == ProximityType.SIMILARITY) &&
			    ((methodType == MethodType.CENTROID) || (methodType == MethodType.WARD))) {
  			System.out.println("Error: " + Language.getLabel(134));
  			showSyntax();
				return;
			}

			try {
				DirectClustering dirClus = new DirectClustering(fileName, proximityType, precision,
				    methodType, methodParameter, isWeighted, originType, BandHeight.BAND_BOTTOM);
				dirClus.printMeasures();
				dirClus.saveMeasures();
				dirClus.saveUltrametric();
				dirClus.saveAsTxt();
				dirClus.saveAsNewick();
				dirClus.saveAsJson();
			} catch (Exception e) {
				String parameters = "Parameters: -direct " + fileName + " " + proximityType;
				if (precision != DirectClustering.AUTO_PRECISION) {
					parameters += " " + precision;
				}
				if (isWeighted) {
					parameters += " WEIGHTED";
				}
				parameters += " " + methodType;
				if (methodType.equals(MethodType.VERSATILE_LINKAGE) ||
					methodType.equals(MethodType.BETA_FLEXIBLE)) {
					parameters += " " + methodParameter;
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
		System.out.println("                      Default value for LEVEL: WARNING");
		System.out.println("");
		System.out.println("    -direct  FILE_NAME  PROX_TYPE  [ PRECISION ]  METHOD  [ METHOD_P ]  [ WEIGHTED ]  [ ORIGIN ]");
		System.out.println("        Direct calculation of the multidendrogram without graphic interface");
		System.out.println("        FILE_NAME : name of the data file");
		System.out.println("        PROX_TYPE : proximity type, one of");
		System.out.println("                      D, DIST, DISTANCE, DISTANCES");
		System.out.println("                      S, SIM, SIMILARITY, SIMILARITIES");
		System.out.println("        PRECISION : number of decimal significant digits, auto if missing value");
		System.out.println("        METHOD    : agglomeration type, one of");
		System.out.println("                      VL, VERSATILE_LINKAGE");
		System.out.println("                      SL, SINGLE_LINKAGE");
		System.out.println("                      CL, COMPLETE_LINKAGE");
		System.out.println("                      AL, ARITHMETIC_LINKAGE");
		System.out.println("                      GL, GEOMETRIC_LINKAGE");
		System.out.println("                      HL, HARMONIC_LINKAGE");
		System.out.println("                      CD, CENTROID");
		System.out.println("                      WD, WARD");
		System.out.println("                      BF, BETA_FLEXIBLE");
		System.out.println("        METHOD_P  : method parameter necessary for");
		System.out.println("                      VL, VERSATILE_LINKAGE");
		System.out.println("                      BF, BETA_FLEXIBLE, between -1 and +1");
		System.out.println("                      Default value for METHOD_P: 0");
		System.out.println("        WEIGHTED  : weighted method, one of");
		System.out.println("                      W, WEIGHTED");
		System.out.println("                      UW, UNWEIGHTED");
		System.out.println("                      Default value for WEIGHTED: UNWEIGHTED");
		System.out.println("        ORIGIN    : origin type, one of");
		System.out.println("                      UO, UNIFORM_ORIGIN");
		System.out.println("                      NUO, NON_UNIFORM_ORIGIN");
		System.out.println("                      Default value for ORIGIN: UNIFORM_ORIGIN");
		System.out.println("");
		System.out.println("");
		System.out.println("Equivalences between clustering algorithms:");
		System.out.println("    Arithmetic Linkage Unweighted = UPGMA = unweighted average");
		System.out.println("    Versatile Linkage (param  1)  = Arithmetic Linkage");
		System.out.println("    Versatile Linkage (param  0)  = Geometric Linkage");
		System.out.println("    Versatile Linkage (param -1)  = Harmonic Linkage");
		System.out.println("    Beta Flexible     (param  0)  = Arithmetic Linkage");
		System.out.println("");
		System.out.println("CENTROID and WARD only available for DISTANCE, not for SIMILARITY");
		System.out.println("");
		System.out.println("");
		System.out.println("Examples:");
		System.out.println("    java -jar multidendrograms.jar");
		System.out.println("    java -jar multidendrograms.jar -loglevel OFF");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt DISTANCES 3 Ward");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt SIMILARITIES 3 Complete_Linkage");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt D CL");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt D 3 CL");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt D 3 Versatile_Linkage +2");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt D 3 VL -3.5 W");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt D CL UO");
		System.out.println("    java -jar multidendrograms.jar -direct data.txt D 3 CL NUO");
		System.out.println("");
	}

	private static boolean hasUpgrade() {
  	final boolean check = true;
		boolean upgradeable = false;
		try {
  		if (check) {
				URL url = new URL(Main.LAST_VERSION_URL);
				InputStreamReader istream = new InputStreamReader(url.openStream());
				BufferedReader in = new BufferedReader(istream);
				String verWeb = in.readLine();
				vn = new VersionNumber(Main.VERSION);
				vnWeb = new VersionNumber(verWeb);
				if (vnWeb.newerThan(vn)) {
					upgradeable = true;
				}
			}
		} catch (Exception e) {
			upgradeable = false;
		}
		return upgradeable;
	}

}
