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

package multidendrograms.forms.panels;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import multidendrograms.initial.Language;
import multidendrograms.methods.BuildDendrogram;
import multidendrograms.dendrogram.DendrogramPlot;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.forms.DendrogramFrame;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.DendrogramParameters;
import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.forms.scrollabledesktop.DesktopConstants;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.MethodName;
import multidendrograms.types.SimilarityType;
import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;
import multidendrograms.data.SimilarityStruct;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.DistancesMatrix;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Load and Update buttons
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class LoadUpdatePanel extends JPanel implements ActionListener,
		InternalFrameListener, PropertyChangeListener, DesktopConstants {

	private static final long serialVersionUID = 1L;

	// Desktop where the dendrogram is to be shown
	private final PrincipalDesk principalDesk;

	protected LoadUpdatePanel luPanel;

	// Text to show in the buttons
	private String strLoad, strUpdate;

	// Buttons Load and Update
	private static JButton btnLoad, btnUpdate;

	// Text box for the file name
	private static JTextField txtFileName;

	// Progress bar for MultiDendrogram computation
	private JProgressBar progressBar;

	// Indicates if the buttons Load or Update are being clicked
	public static boolean buttonClicked = false;

	// Indicate if the text fields have correct values
	public static boolean precisionCorrect = false;
	public static boolean axisMinCorrect = false;
	public static boolean axisMaxCorrect = false;
	public static boolean axisSeparationCorrect = false;
	public static boolean axisTicksCorrect = false;
	public static boolean axisDecimalsCorrect = false;

	// Internal frame currently active
	private DendrogramFrame currentDendrogramFrame = null;

	// File with the input data
	private static DataFile dataFile = null;
	private ExternalData data;

	// MultiDendrogram
	private DistancesMatrix distMatrix = null;

	// SwingWorker MultiDendrogram computation
	class MDComputation extends SwingWorker<Void, Void> {
		private final String action;
		private final SimilarityType simType;
		private final MethodName method;
		private final int precision;
		private final int nbElements;
		private double minBase;

		public MDComputation(final String action, final SimilarityType simType,
				final MethodName method, final int precision, final int nbElements,
				double minBase) {
			this.action = action;
			this.simType = simType;
			this.method = method;
			this.precision = precision;
			this.nbElements = nbElements;
			this.minBase = minBase;
		}

		@Override
		public Void doInBackground() {
			BuildDendrogram bd;
			DistancesMatrix mdNew;
			double b;
			int progress;

			// Initialize progress property
			progress = 0;
			setProgress(progress);
			while (distMatrix.getCardinality() > 1) {
				try {
					bd = new BuildDendrogram(distMatrix, simType, method, precision);
					mdNew = bd.recalculate();
					distMatrix = mdNew;
					b = distMatrix.getRoot().getBase();
					if ((b < minBase) && (b != Double.MAX_VALUE)) {
						minBase = b;
					}
					progress = 100 * (nbElements - distMatrix.getCardinality()) / (nbElements - 1);
					setProgress(progress);
				} catch (final Exception e) {
					showError(e.getMessage());
				}
			}
			return null;
		}

		@Override
		public void done() {
			Cluster root = distMatrix.getRoot();
			root.setBase(minBase);
			if (!method.equals(MethodName.UNWEIGHTED_CENTROID) 
					&& !method.equals(MethodName.WEIGHTED_CENTROID)) {
				try {
					BuildDendrogram.avoidReversals(root, root.getSummaryHeight(), simType);
				} catch (final Exception e) {
					showError(e.getMessage());
				}
			}
			showCalls(action);
			progressBar.setString("");
			progressBar.setBorderPainted(false);
			progressBar.setValue(0);
			principalDesk.setCursor(null); // turn off the wait cursor
		}
	}

	public LoadUpdatePanel(final PrincipalDesk fr) {
		super();
		this.principalDesk = fr;
		this.luPanel = this;
		this.fillPanel();
		this.setVisible(true);
	}

	private void fillPanel() {

		this.setBorder(BorderFactory.createTitledBorder(Language.getLabel(20))); // File

		// load
		strLoad = Language.getLabel(21); // Load
		btnLoad = new JButton(strLoad);
		btnLoad.addActionListener(this);

		// update
		strUpdate = Language.getLabel(110); // Update
		btnUpdate = new JButton(strUpdate);
		btnUpdate.addActionListener(this);
		btnUpdate.setEnabled(false);

		// file name
		txtFileName = new JTextField();
		txtFileName.addActionListener(this);
		txtFileName.setEditable(false);
		txtFileName.setColumns(21);
		txtFileName.setHorizontalAlignment(JTextField.LEFT);
		setFileName(Language.getLabel(112)); // No file loaded

		// progress bar
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setBorderPainted(false);
		progressBar.setValue(0);

		// layout
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
						.addGap(6, 6, 6)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(btnLoad, 90, 90, 90)
														.addGap(3, 3, 3)
														.addComponent(btnUpdate, 90, 90, 90))
										.addGroup(GroupLayout.Alignment.CENTER,
												layout.createSequentialGroup()
														.addComponent(txtFileName))
										.addGroup(GroupLayout.Alignment.CENTER,
												layout.createSequentialGroup()
														.addComponent(progressBar)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(6, 6, 6));

//		layout.linkSize(SwingConstants.HORIZONTAL, btnLoad, btnUpdate);
		layout.linkSize(SwingConstants.HORIZONTAL, txtFileName, progressBar);
	
		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGap(1, 1, 1)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addComponent(btnLoad)
										.addComponent(btnUpdate))
						.addGap(3, 3, 3)
						.addComponent(txtFileName)
						.addGap(3, 3, 3)
						.addComponent(progressBar)
						.addGap(1, 1, 1));

}

	public static void enableUpdate() {
		if (precisionCorrect && axisMinCorrect && axisMaxCorrect
				&& axisSeparationCorrect && axisTicksCorrect
				&& axisDecimalsCorrect) {
			btnUpdate.setEnabled(true);
		} else {
			btnUpdate.setEnabled(false);
		}
	}

	public static String getFileName() {
		String name = "";
		if (dataFile != null) {
			name = dataFile.getName();
		}
		return name;
	}

	public static String getFileNameNoExt() {
		String name = "";
		if (dataFile != null) {
			name = dataFile.getNameNoExt();
		}
		return name;
	}

	public static void setFileName(String name) {
		txtFileName.setText(name);
		txtFileName.setCaretPosition(0);
		txtFileName.setToolTipText(name);
	}

	public DistancesMatrix getDistancesMatrix() {
		return data.getDistancesMatrix();
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		String action = null;
		DataFile tmpDataFile;
		boolean withData = false;
		DendrogramParameters dendroParams;
		double minBase;
		MDComputation mdComputation;

		if (evt.getActionCommand().equals(strLoad)) {
			// LOAD
			buttonClicked = true;
			action = "Load";
			// Load data from file
			if (dataFile == null) {
				tmpDataFile = getDataFile();
			} else {
				// Last directory
				tmpDataFile = getDataFile(dataFile.getPath());
			}
			if (tmpDataFile == null) {
				// Cancel pressed
				withData = false;
			} else {
				dataFile = tmpDataFile;
				withData = true;
			}
		} else if (evt.getActionCommand().equals(strUpdate)) {
			// UPDATE
			buttonClicked = true;
			dendroParams = currentDendrogramFrame.getDendrogramParameters();
			if ((SettingsPanel.getSimilarityType() == dendroParams.getSimilarityType())
					&& (SettingsPanel.getMethod() == dendroParams.getMethod())
					&& (SettingsPanel.getPrecision() == dendroParams.getPrecision())) {
				action = "Redraw";
			} else {
				action = "Reload";
			}
			withData = true;
		}
		if (withData && (action.equals("Load") || action.equals("Reload"))) {
			try {
				data = new ExternalData(dataFile, true);
				if (action.equals("Load")) {
					SettingsPanel.setPrecision(data.getPrecision());
				}
				distMatrix = null;
				try {
					distMatrix = data.getDistancesMatrix();
					minBase = Double.MAX_VALUE;
					progressBar.setBorderPainted(true);
					progressBar.setString(null);
					principalDesk.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// Instances of javax.swing.SwingWorker are not reusable,
					// so we create new instances as needed.
					mdComputation = new MDComputation(action,
							SettingsPanel.getSimilarityType(), SettingsPanel.getMethod(),
							SettingsPanel.getPrecision(),
							distMatrix.getCardinality(), minBase);
					mdComputation.addPropertyChangeListener(this);
					mdComputation.execute();
				} catch (final Exception e2) {
					buttonClicked = false;
					showError(e2.getMessage());
				}
			} catch (Exception e1) {
				buttonClicked = false;
				showError(e1.getMessage());
			}
		} else if (withData && action.equals("Redraw")) {
			showCalls(action);
		} else {
			buttonClicked = false;
		}
	}

	private void showCalls(final String action) {
		if (action.equals("Reload") || action.equals("Redraw")) {
			currentDendrogramFrame.doDefaultCloseAction();
		}
		show(action, SettingsPanel.getMethod(), SettingsPanel.getPrecision());
		currentDendrogramFrame.doDefaultCloseAction();
		show(action, SettingsPanel.getMethod(), SettingsPanel.getPrecision());
		setFileName(dataFile.getName());
		btnUpdate.setEnabled(true);
		buttonClicked = false;
	}

	public void show(String action, final MethodName method, final int precision) {
		boolean isUpdate;
		DendrogramFrame dendroFrame;
		Config cfg;
		DendrogramParameters dendroParams;
		DendrogramPanel dendroPanel;
		DendrogramPlot dendroPlot;

		isUpdate = !action.equals("Load");
		try {
			dendroFrame = principalDesk.createDendrogramFrame(isUpdate, method);
			cfg = principalDesk.getConfig();
			cfg.setDendrogramFrame(dendroFrame);
			cfg.setDataFile(dataFile);
			cfg.setDistancesMatrix(distMatrix);
			cfg.setNames(data.getNames());
			if (!cfg.isDistance()) {
				if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.NORTH)) {
					cfg.setOrientacioDendo(DendrogramOrientation.SOUTH);
				} else if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.SOUTH)) {
					cfg.setOrientacioDendo(DendrogramOrientation.NORTH);
				} else if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.EAST)) {
					cfg.setOrientacioDendo(DendrogramOrientation.WEST);
				} else if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.WEST)) {
					cfg.setOrientacioDendo(DendrogramOrientation.EAST);
				}
			}
			dendroParams = new DendrogramParameters(dataFile, distMatrix);
			dendroFrame.setDendrogramParameters(dendroParams);
			// Title for the child window
			String title = dataFile.getName() + " - " + dendroFrame.getTitle();
			dendroFrame.setTitle(title);
			dendroFrame.getAssociatedButton().setText(title);
			dendroFrame.getAssociatedButton().setToolTipText(title);
			// Load the window to show the dendrogram
			dendroPanel = new DendrogramPanel(principalDesk);
			dendroFrame.add(dendroPanel);
			// Call SettingsPanel -> internalFrameActivated()
			dendroFrame.setVisible(true);
			if (action.equals("Load") || action.equals("Reload")) {
				SettingsPanel.adjustValues(cfg);
			}
			principalDesk.setCurrentFrame(dendroFrame);
			// Convert tree into figures
			dendroPlot = new DendrogramPlot(distMatrix.getRoot(), cfg);
			LinkedList<SimilarityStruct<String>> data = dendroParams.getExternalData().getData();
			UltrametricMatrix ultraMatrix = new UltrametricMatrix(data, distMatrix.getRoot(), precision);
			dendroParams.setUltrametricMatrix(ultraMatrix);
			// Pass figures to the window
			dendroPanel.setNodesList(dendroPlot.getNodesList());
			dendroPanel.setLinesList(dendroPlot.getLinesList());
			dendroPanel.setBandsList(dendroPlot.getBandsList());
			dendroPanel.setConfig(cfg);
		} catch (final Exception e) {
			e.printStackTrace();
			showError(e.getMessage());
		}
	}

	private DataFile getDataFile() {
		return this.getDataFile(System.getProperty("user.dir"));
	}

	private DataFile getDataFile(final String sPath) {
		final FileDialog fd = new FileDialog(principalDesk, Language.getLabel(9),
				FileDialog.LOAD);
		DataFile dataFile;

		dataFile = new DataFile();
		fd.setDirectory(sPath);
		fd.setVisible(true);
		if (fd.getFile() == null) {
			dataFile = null;
		} else {
			dataFile.setName(fd.getFile());
			dataFile.setPath(fd.getDirectory());
		}
		return dataFile;
	}

	private void showError(final String msg) {
		JOptionPane.showMessageDialog(null, msg, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}


	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		DendrogramParameters dendroParams;

		currentDendrogramFrame = (DendrogramFrame) e.getSource();
		btnUpdate.setEnabled(true);
		if (!buttonClicked) {
			principalDesk.setCurrentFrame(currentDendrogramFrame);
			dendroParams = currentDendrogramFrame.getDendrogramParameters();
			data = dendroParams.getExternalData();
			dataFile = data.getDataFile();
			setFileName(dataFile.getName());
			distMatrix = dendroParams.getDistancesMatrix();
			SettingsPanel.setConfigPanel(dendroParams);
		}
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		btnUpdate.setEnabled(false);
		setFileName(Language.getLabel(112)); // No file loaded
		if (!buttonClicked) {
			SettingsPanel.clearConfigPanel();
		}
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		if (principalDesk.getNumDendrogramFrames() < MAX_FRAMES) {
			btnLoad.setEnabled(true);
		} else {
			btnLoad.setEnabled(false);
		}
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		if (principalDesk.getNumDendrogramFrames() < MAX_FRAMES) {
			btnLoad.setEnabled(true);
		} else {
			btnLoad.setEnabled(false);
		}
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "progress") {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

}
