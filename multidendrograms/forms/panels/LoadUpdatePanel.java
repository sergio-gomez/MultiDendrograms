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

import javax.swing.SwingConstants;
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

import multidendrograms.core.clusterings.HierarchicalClustering;
import multidendrograms.core.definitions.SymmetricMatrix;
import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Formats;
import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.DendrogramPlot;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.direct.DirectClustering;
import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.forms.DendrogramFrame;
import multidendrograms.forms.DendrogramParameters;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.scrollabledesktop.DesktopConstants;
import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.types.BandHeight;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.MethodType;

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
	public static boolean methodParameterCorrect = false;
	public static boolean axisMinCorrect = false;
	public static boolean axisMaxCorrect = false;
	public static boolean axisSeparationCorrect = false;
	public static boolean axisTicksCorrect = false;
	public static boolean axisDecimalsCorrect = false;

	// Internal frame currently active
	private DendrogramFrame currentDendrogramFrame = null;

	// File with the input data
	private static DataFile dataFile = null;
	private ExternalData externalData;

	// MultiDendrogram
	private HierarchicalClustering clustering = null;

	// SwingWorker MultiDendrogram computation
	class MDComputation extends SwingWorker<Void, Void> {
		private final String action;
		private final int nbElements;

		public MDComputation(final String action, final int nbElements) {
			this.action = action;
			this.nbElements = nbElements;
		}

		@Override
		public Void doInBackground() {
			// Initialize progress property
			int progress = 0;
			setProgress(progress);
			while (clustering.numberOfRoots() > 1) {
				try {
					clustering.iteration();
					progress = 100 * (nbElements - clustering.numberOfRoots()) / (nbElements - 1);
					setProgress(progress);
				} catch (final Exception e) {
					showError(e.getMessage());
				}
			}
			return null;
		}

		@Override
		public void done() {
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

		setBorder(Formats.getFormattedTitledBorder(Language.getLabel(20))); // File

		// load
		strLoad = Language.getLabel(21); // Load
		btnLoad = Formats.getFormattedBoldButton(strLoad);
		btnLoad.addActionListener(this);

		// update
		strUpdate = Language.getLabel(110); // Update
		btnUpdate = Formats.getFormattedBoldButton(strUpdate);
		btnUpdate.addActionListener(this);
		btnUpdate.setEnabled(false);

		// Width
		int btnWidth = InitialProperties.scaleSize(100);
		int lblWidth = 2 * btnWidth + 3;

		// file name
		txtFileName = Formats.getFormattedTextField();
		txtFileName.addActionListener(this);
		txtFileName.setEditable(false);
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

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGap(6, 6, 6)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(layout.createSequentialGroup()
								.addComponent(btnLoad, btnWidth, btnWidth, 2 * btnWidth)
								.addGap(3, 3, 3)
								.addComponent(btnUpdate, btnWidth, btnWidth, 2 * btnWidth))
						.addGroup(GroupLayout.Alignment.CENTER,
								layout.createSequentialGroup().addComponent(txtFileName, lblWidth, lblWidth, 2 * lblWidth))
						.addGroup(GroupLayout.Alignment.CENTER,
								layout.createSequentialGroup().addComponent(progressBar)))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGap(6, 6, 6));

		layout.linkSize(SwingConstants.HORIZONTAL, btnLoad, btnUpdate);
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
		if (precisionCorrect && methodParameterCorrect && axisMinCorrect
				&& axisMaxCorrect && axisSeparationCorrect && axisTicksCorrect
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

	@Override
	public void actionPerformed(final ActionEvent evt) {
		String action = null;
		DataFile tmpDataFile;
		boolean withData = false;
		DendrogramParameters dendroParams;
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
			if ((SettingsPanel.getProximityType() == dendroParams.getProximityType())
					&& (SettingsPanel.getPrecision() == dendroParams.getPrecision())
					&& (SettingsPanel.getMethod() == dendroParams.getMethod())
					&& (SettingsPanel.getMethodParameter() == dendroParams.getMethodParameter())
					&& (SettingsPanel.isWeighted() == dendroParams.isWeighted())) {
				action = "Redraw";
			} else {
				action = "Reload";
			}
			withData = true;
		}
		if (withData && (action.equals("Load") || action.equals("Reload"))) {
			try {
				this.externalData = new ExternalData(dataFile);
				SymmetricMatrix proximityMatrix = this.externalData.getProximityMatrix();
				MethodType methodType = SettingsPanel.getMethod();
				if ((proximityMatrix.minimumValue() < 0.0) &&
						(methodType.equals(MethodType.VERSATILE_LINKAGE) ||
						 methodType.equals(MethodType.GEOMETRIC_LINKAGE))) {
					buttonClicked = false;
					showError(Language.getLabel(80));
				} else {
					if (action.equals("Load")) {
						SettingsPanel.setPrecision(this.externalData.getPrecision());
					}
					this.clustering = null;
					try {
						this.clustering = DirectClustering.newClustering(methodType, proximityMatrix,
								this.externalData.getNames(), SettingsPanel.getProximityType(),
								SettingsPanel.getPrecision(), SettingsPanel.isWeighted(),
								SettingsPanel.getMethodParameter());
						this.progressBar.setBorderPainted(true);
						this.progressBar.setString(null);
						this.principalDesk.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						// Instances of javax.swing.SwingWorker are not reusable,
						// so we create new instances as needed.
						mdComputation = new MDComputation(action, this.externalData.getNumberOfElements());
						mdComputation.addPropertyChangeListener(this);
						mdComputation.execute();
					} catch (final Exception e2) {
						buttonClicked = false;
						showError(e2.getMessage());
					}
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
		show(action, SettingsPanel.getMethod());
		currentDendrogramFrame.doDefaultCloseAction();
		show(action, SettingsPanel.getMethod());
		setFileName(dataFile.getName());
		btnUpdate.setEnabled(true);
		buttonClicked = false;
	}

	private void show(String action, MethodType method) {
		try {
			boolean isUpdate = !action.equals("Load");
			DendrogramFrame dendroFrame = this.principalDesk.createDendrogramFrame(isUpdate, method);
			SettingsInfo settingsInfo = SettingsPanel.getSettingsInfo();
			Config cfg = new Config(settingsInfo);
			cfg.setDendrogramFrame(dendroFrame);
			cfg.setDataFile(dataFile);
			cfg.setHierarchicalClustering(this.clustering);
			cfg.setExternalData(this.externalData);
			if (!cfg.isDistance()) {
				if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.NORTH)) {
					cfg.setDendrogramOrientation(DendrogramOrientation.SOUTH);
				} else if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.SOUTH)) {
					cfg.setDendrogramOrientation(DendrogramOrientation.NORTH);
				} else if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.EAST)) {
					cfg.setDendrogramOrientation(DendrogramOrientation.WEST);
				} else if (cfg.getDendrogramOrientation().equals(DendrogramOrientation.WEST)) {
					cfg.setDendrogramOrientation(DendrogramOrientation.EAST);
				}
			}
			DendrogramParameters dendroParams = new DendrogramParameters(this.externalData, this.clustering);
			dendroFrame.setDendrogramParameters(dendroParams);
			// Title for the child window
			String title = dataFile.getName() + " - " + dendroFrame.getTitle();
			dendroFrame.setTitle(title);
			dendroFrame.getAssociatedButton().setText(title);
			dendroFrame.getAssociatedButton().setToolTipText(title);
			// Load the window to show the dendrogram
			DendrogramPanel dendroPanel =
					new DendrogramPanel(this.principalDesk);
			dendroFrame.add(dendroPanel);
			// Call SettingsPanel -> internalFrameActivated()
			dendroFrame.setVisible(true);
			if (action.equals("Load") || action.equals("Reload")) {
				SettingsPanel.adjustValues(cfg);
			}
			this.principalDesk.setCurrentFrame(dendroFrame);
			// Convert tree into figures
			DendrogramPlot dendroPlot = new DendrogramPlot(this.clustering.getRoot(), cfg);
			UltrametricMatrix ultraMatrix = new UltrametricMatrix(this.clustering.getRoot(),
					this.externalData.getNames(), settingsInfo.getOriginType(), BandHeight.BAND_BOTTOM);
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

	private void showError(String message) {
		JOptionPane.showMessageDialog(null, message, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		this.currentDendrogramFrame = (DendrogramFrame) e.getSource();
		btnUpdate.setEnabled(true);
		if (!buttonClicked) {
			this.principalDesk.setCurrentFrame(this.currentDendrogramFrame);
			DendrogramParameters dendroParams = this.currentDendrogramFrame.getDendrogramParameters();
			this.externalData = dendroParams.getExternalData();
			dataFile = this.externalData.getDataFile();
			setFileName(dataFile.getName());
			this.clustering = dendroParams.getHierarchicalClustering();
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
