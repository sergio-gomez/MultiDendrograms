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

package multidendrograms.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.dendrogram.ToNewick;
import multidendrograms.dendrogram.ToTxt;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.dendrogram.eps.EpsExporter;
import multidendrograms.forms.panels.SettingsPanel;
import multidendrograms.forms.panels.LoadUpdatePanel;
import multidendrograms.forms.panels.InfoExitPanel;
import multidendrograms.forms.scrollabledesktop.JScrollableDesktopPane;
import multidendrograms.forms.children.DeviationMeasuresBox;
import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.types.MethodName;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.MathUtils;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.Config;
import multidendrograms.methods.Method;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Main frame window
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class PrincipalDesk extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int xOffset = 8, yOffset = 8;
	private static final int xDelta = 14, yDelta = 14;
	private static final int xRep = 100, yRep = 20;
	private static final int minFrmWidth = 665, minFrmHeight = 660;

	private final JPanel panControl, panInfoExit;

	private final JScrollableDesktopPane panDesk;

	private final LoadUpdatePanel panLoadUpdate;

	private final SettingsPanel panSettings;

	private Config cfg;
	private DendrogramFrame currentDendrogramFrame;

	public PrincipalDesk(final String title) {
		super(title);
		LogManager.LOG.info("Creating PrincipalDesk");

		final int frmWidth = InitialProperties.getWidth_frmPrincipal();
		final int frmHeight = InitialProperties.getHeight_frmPrincipal();

		panDesk = new JScrollableDesktopPane();
		panDesk.setBorder(BorderFactory.createTitledBorder(""));

		this.getContentPane().add(panDesk);

		panControl = new JPanel();
		panControl.setLayout(new BorderLayout());

		panLoadUpdate = new LoadUpdatePanel(this);
		panSettings = new SettingsPanel(this);
		panInfoExit = new InfoExitPanel(this);

		panControl.add(panLoadUpdate, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(panSettings);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		panControl.add(scrollPane, BorderLayout.CENTER);
		panControl.add(panInfoExit, BorderLayout.SOUTH);

		this.add(panControl, BorderLayout.WEST);

		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setMinimumSize(new Dimension(minFrmWidth, minFrmHeight));
		this.setSize(frmWidth, frmHeight);
//		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
//		this.setBounds(25, 50, screenSize.width - 50, screenSize.height - 75);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					toGoOut();
				}
			});
	}

	public Config getConfig() {
		cfg = new Config(SettingsPanel.getSettingsInfo());
		cfg.setDistancesMatrix(panLoadUpdate.getDistancesMatrix());
		if (cfg.getAxisMaxVal() == 0) {
			cfg.getConfigMenu().setAxisMaxVal(cfg.getTop());
		}
		return cfg;
	}

	public DendrogramFrame createDendrogramFrame(boolean isUpdate, MethodName method) {
		int x, y, width, height, ofc;
		DendrogramFrame dendroFrame;

		ofc = DendrogramFrame.openFrameCount;
		if (isUpdate) {
			x = currentDendrogramFrame.getX();
			y = currentDendrogramFrame.getY();
			width = currentDendrogramFrame.getWidth();
			height = currentDendrogramFrame.getHeight();
		} else {
			x = xOffset + xDelta * ((ofc - 1) / xRep) + xDelta * ((ofc - 1) % xRep);
			y = yOffset + yDelta * ((ofc - 1) % yRep);
			width = InitialProperties.getWidth_frmDesk();
			height = InitialProperties.getHeight_frmDesk();
		}
		dendroFrame = new DendrogramFrame(method, isUpdate);
		dendroFrame.setSize(width, height);
		dendroFrame.setBackground(Color.BLUE);
		dendroFrame.setLayout(new BorderLayout());
		dendroFrame.addInternalFrameListener(panLoadUpdate);

		panDesk.add(dendroFrame, x, y);
		return dendroFrame;
	}

	public void toGoOut() {
		final String msg = Language.getLabel(0);
		int opt;
		opt = JOptionPane.showConfirmDialog(null, msg, Language.getLabel(46), JOptionPane.YES_NO_OPTION);
		if (opt == JOptionPane.YES_OPTION) {
			LogManager.LOG.info("Exit");
			System.exit(0);
		}
	}

	public void savePicture(final BufferedImage buff, final String imgFormat, Config cfg) throws Exception {
		String sPath;
		String sNameNoExt = LoadUpdatePanel.getFileNameNoExt();
		String sInfix = "-" + Method.toShortName(cfg.getMethod()) + cfg.getPrecision();
		final FileDialog fd = new FileDialog(this, Language.getLabel(75) + " " + imgFormat.toUpperCase(), FileDialog.SAVE);
		fd.setFile(sNameNoExt + sInfix + "." + imgFormat);
		fd.setVisible(true);

		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			final File fil = new File(sPath);
			try {
				ImageIO.write(buff, imgFormat, fil);
				LogManager.LOG.info("Image saved");
			} catch (final IOException e) {
				String msg_err = Language.getLabel(76);
				LogManager.LOG.throwing("PrincipalDesk", "savePicture(final BufferedImage buff, final String tipus)", e);
				throw new Exception(msg_err);
			} catch (Exception e) {
				String msg_err = Language.getLabel(77);
				LogManager.LOG.throwing("PrincipalDesk", "savePicture(final BufferedImage buff, final String tipus)", e);
				throw new Exception(msg_err);
			}
		}
	}

	public void savePostScript(DendrogramPanel dendroPanel, Config cfg) throws Exception {
		String sPath;
		String sNameNoExt = LoadUpdatePanel.getFileNameNoExt();
		String sInfix = "-" + Method.toShortName(cfg.getMethod()) + cfg.getPrecision();
		final FileDialog fd = new FileDialog(this, Language.getLabel(75) + " EPS", FileDialog.SAVE);
		fd.setFile(sNameNoExt + sInfix + ".eps");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			try {
				new EpsExporter(cfg, dendroPanel, sPath);
				LogManager.LOG.info("EPS image saved");
			} catch (Exception e) {
				String msg_err = Language.getLabel(77);
				LogManager.LOG.throwing("PrincipalDesk", "savePostScript(final BufferedImage buff)", e);
				throw new Exception(msg_err);
			}
		}
	}

	public void saveTXT(Cluster root, Config cfg) throws Exception {
		String sPath;
		String sNameNoExt = LoadUpdatePanel.getFileNameNoExt();
		String sInfix = "-" + Method.toShortName(cfg.getMethod()) + cfg.getPrecision();
		FileDialog fd = new FileDialog(this, Language.getLabel(80) + " TXT", FileDialog.SAVE);
		fd.setFile(sNameNoExt + sInfix + "-tree.txt");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			ToTxt saveTXT = new ToTxt(root, cfg.getPrecision(), cfg.getSimilarityType());
			saveTXT.saveAsTxt(sPath);
		}
	}

	public void saveNewick(Cluster root, Config cfg) throws Exception {
		String sPath;
		String sNameNoExt = LoadUpdatePanel.getFileNameNoExt();
		String sInfix = "-" + Method.toShortName(cfg.getMethod()) + cfg.getPrecision();
		FileDialog fd;
		double heightBottom, heightMin, heightMax, extraSpace;
		ToNewick toNewick;
		int precision = cfg.getPrecision();

		fd = new FileDialog(this, Language.getLabel(80) + " Newick", FileDialog.SAVE);
		fd.setFile(sNameNoExt + sInfix + "-newick.txt");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			if (cfg.getSimilarityType().equals(SimilarityType.DISTANCE)) {
				heightBottom = 0.0;
			} else {
				heightMin = cfg.getBase();
				heightMax = cfg.getTop();
				extraSpace = (heightMax - heightMin) * (0.05 * MathUtils.round((heightMax - heightMin),	precision));
				extraSpace = MathUtils.round(extraSpace, precision);
				heightBottom = heightMax + extraSpace;
			}
			toNewick = new ToNewick(root, precision, cfg.getSimilarityType(), heightBottom);
			toNewick.saveAsNewick(sPath);
		}
	}

	public void saveUltrametricTXT(Config cfg) throws Exception {
		String sPath;
		String sNameNoExt = LoadUpdatePanel.getFileNameNoExt();
		String sInfix = "-" + Method.toShortName(cfg.getMethod()) + cfg.getPrecision();
		FileDialog fd = new FileDialog(this, Language.getLabel(80) + " TXT", FileDialog.SAVE);
		fd.setFile(sNameNoExt + sInfix + "-ultrametric.txt");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			UltrametricMatrix ultraMatrix = currentDendrogramFrame.getDendrogramParameters().getUltrametricMatrix();
			ultraMatrix.saveAsTXT(sPath, cfg.getPrecision());
		}
	}

	public void showUltrametricErrors(Config cfg) {
		UltrametricMatrix ultraMatrix = currentDendrogramFrame.getDendrogramParameters().getUltrametricMatrix();
		DeviationMeasuresBox box = new DeviationMeasuresBox(this, ultraMatrix, cfg);
		box.setVisible(true);
	}

	public SettingsPanel getPanMenu() {
		return this.panSettings;
	}

	public JScrollableDesktopPane getPanDesk() {
		return this.panDesk;
	}

	public void setCurrentFrame(DendrogramFrame internalFrame) {
		this.currentDendrogramFrame = internalFrame;
	}

	public LoadUpdatePanel getLoadUpdatePanel() {
		return this.panLoadUpdate;
	}

	public int getNumDendrogramFrames() {
		return this.panDesk.getNumInternalFrames();
	}

}