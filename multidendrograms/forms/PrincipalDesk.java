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

import multidendrograms.definitions.Config;
import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.DendrogramMeasures;
import multidendrograms.dendrogram.ToJson;
import multidendrograms.dendrogram.ToNewick;
import multidendrograms.dendrogram.ToTxt;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.dendrogram.eps.EpsExporter;
import multidendrograms.direct.DirectClustering;
import multidendrograms.forms.children.DendrogramMeasuresBox;
import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.forms.panels.InfoExitPanel;
import multidendrograms.forms.panels.LoadUpdatePanel;
import multidendrograms.forms.panels.SettingsPanel;
import multidendrograms.forms.scrollabledesktop.JScrollableDesktopPane;
import multidendrograms.initial.InitialProperties;
import multidendrograms.initial.Language;
import multidendrograms.initial.LogManager;
import multidendrograms.types.MethodType;
import multidendrograms.types.OriginType;

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
	private static final int minFrmWidth = 700, minFrmHeight = 750;

	private final JPanel panControl, panInfoExit;

	private final JScrollableDesktopPane panDesk;

	private final LoadUpdatePanel panLoadUpdate;

	private final SettingsPanel panSettings;

	private DendrogramFrame currentDendrogramFrame;

	public PrincipalDesk(final String title) {
		super(title);
		LogManager.LOG.info("Creating PrincipalDesk");

		final int frmWidth = InitialProperties.getWidthMainWindow();
		final int frmHeight = InitialProperties.getHeightMainWindow();

		panDesk = new JScrollableDesktopPane();
		panDesk.setBorder(BorderFactory.createTitledBorder(""));

		getContentPane().add(panDesk);

		panControl = new JPanel();
		panControl.setLayout(new BorderLayout());

		panLoadUpdate = new LoadUpdatePanel(this);
		panSettings = new SettingsPanel();
		panInfoExit = new InfoExitPanel(this);

		panControl.add(panLoadUpdate, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(panSettings);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		panControl.add(scrollPane, BorderLayout.CENTER);
		panControl.add(panInfoExit, BorderLayout.SOUTH);

		add(panControl, BorderLayout.WEST);

		JFrame.setDefaultLookAndFeelDecorated(true);
		setMinimumSize(new Dimension(minFrmWidth, minFrmHeight));
		setSize(frmWidth, frmHeight);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					toGoOut();
				}
			});
	}

	public DendrogramFrame createDendrogramFrame(boolean isUpdate, MethodType method) {
		int x, y, width, height, ofc;
		DendrogramFrame dendroFrame;

		if (isUpdate) {
			x = currentDendrogramFrame.getX();
			y = currentDendrogramFrame.getY();
			width = currentDendrogramFrame.getWidth();
			height = currentDendrogramFrame.getHeight();
		} else {
  		if (panDesk.getNumInternalFrames() == 0) {
    		DendrogramFrame.openFrameCount = 1;
  		}
			ofc = DendrogramFrame.openFrameCount;
			x = xOffset + xDelta * ((ofc - 1) / xRep) + xDelta * ((ofc - 1) % xRep);
			y = yOffset + yDelta * ((ofc - 1) % yRep);
			width = InitialProperties.getWidthDendroWindow();
			height = InitialProperties.getHeightDendroWindow();
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

	public void savePicture(BufferedImage buff, String imgFormat, Config cfg) throws Exception {
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " " + imgFormat.toUpperCase(), FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + "." + imgFormat);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			final File fil = new File(path);
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
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " EPS", FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + ".eps");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			try {
				new EpsExporter(cfg, dendroPanel, path);
				LogManager.LOG.info("EPS image saved");
			} catch (Exception e) {
				String msg_err = Language.getLabel(77);
				LogManager.LOG.throwing("PrincipalDesk", "savePostScript(final BufferedImage buff)", e);
				throw new Exception(msg_err);
			}
		}
	}

	public void saveTXT(Config cfg) throws Exception {
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " TXT", FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + DirectClustering.TXT_TREE_SUFIX);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			ToTxt toTXT = new ToTxt(cfg.getRoot());
			toTXT.saveAsTxt(path);
		}
	}

	public void saveNewick(Config cfg) throws Exception {
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " Newick", FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + DirectClustering.NEWICK_TREE_SUFIX);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			SettingsInfo settings = cfg.getSettingsInfo();
			OriginType originType = settings.getOriginType();
			boolean isUniformOrigin = originType.equals(OriginType.UNIFORM_ORIGIN) ? true : false;
			ToNewick toNewick = new ToNewick(cfg.getRoot(), isUniformOrigin);
			toNewick.saveAsNewick(path);
		}
	}

	public void saveJson(Config cfg) throws Exception {
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " JSON", FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + DirectClustering.JSON_TREE_SUFIX);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			SettingsInfo settings = cfg.getSettingsInfo();
			OriginType originType = settings.getOriginType();
			boolean isUniformOrigin = originType.equals(OriginType.UNIFORM_ORIGIN) ? true : false;
			ToJson toJson = new ToJson(cfg.getRoot(), isUniformOrigin);
			toJson.saveAsJson(path);
		}
	}

	public void saveUltrametricTxt(Config cfg) throws Exception {
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " TXT", FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + DirectClustering.ULTRAMETRIC_SUFIX);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			DendrogramParameters dendroParams = this.currentDendrogramFrame.getDendrogramParameters();
			UltrametricMatrix ultrametricMatrix = dendroParams.getUltrametricMatrix();
			ultrametricMatrix.saveAsTxt(path);
		}
	}

	public void saveDendrogramMeasures(Config cfg) throws Exception {
		FileDialog fd = new FileDialog(this, Language.getLabel(75) + " TXT", FileDialog.SAVE);
		String filePrefix = DirectClustering.getFilePrefix(LoadUpdatePanel.getFileNameNoExt(), cfg.getProximityType(),
				cfg.getPrecision(), cfg.getMethod(), cfg.getMethodParameter(), cfg.isWeighted());
		fd.setFile(filePrefix + DirectClustering.MEASURES_SUFIX);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			String path = fd.getDirectory() + fd.getFile();
			DendrogramParameters dendroParams = this.currentDendrogramFrame.getDendrogramParameters();
			UltrametricMatrix ultrametricMatrix = dendroParams.getUltrametricMatrix();
			DendrogramMeasures dendroMeasures =
					new DendrogramMeasures(cfg.getExternalData().getProximityMatrix(), cfg.getRoot(),
							ultrametricMatrix.getMatrix());
			dendroMeasures.save(path);
		}
	}

	public void showDendrogramMeasures(Config cfg) {
		DendrogramParameters dendroParams = this.currentDendrogramFrame.getDendrogramParameters();
		UltrametricMatrix ultrametricMatrix = dendroParams.getUltrametricMatrix();
		DendrogramMeasuresBox dendroMeasuresBox = new DendrogramMeasuresBox(ultrametricMatrix, cfg);
		dendroMeasuresBox.setVisible(true);
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
