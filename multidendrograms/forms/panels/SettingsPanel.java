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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.DendrogramParameters;
import multidendrograms.forms.children.FontSelection;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.MethodName;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.OriginType;
import multidendrograms.types.SimilarityType;
import multidendrograms.utils.SmartAxis;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.SettingsInfo;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Formats;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Settings panel
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class SettingsPanel extends JPanel implements ActionListener, FocusListener,
		InternalFrameListener {

	private static final long serialVersionUID = 1L;

	// Labels to identify the components of the JPanel
	private static JLabel lblTypeMeasure, lblMethod, lblPrecision;
	private static JLabel lblTreeTitle, lblTreeOrientation;
	private static JLabel lblNodesTitle, lblNodesOrientation, lblNodesSize;
	private static JLabel lblAxisTitle, lblAxisMin, lblAxisMax,
			lblAxisSeparation, lblAxisEvery, lblAxisTicks, lblAxisDecimals;

	// Type of measure
	private static JRadioButton rbDistances, rbSimilarities;

	// Clustering algorithm
	private static JComboBox cbMethod;

	// Text box for the precision
	private static JTextField txtPrecision;

	// Orientation of the tree
	private static JComboBox cbTreeOrientation;

	// Radius for the nodes
	private static JComboBox cbNodesSize;

	// Orientation of the labels of nodes
	private static JComboBox cbNodesOrientation;

	// To decide the visibility of components
	private static JCheckBox chkBands, chkNodesLabels, chkUniformOrigin, chkAxis, chkAxisLabels;

	// Text boxes for user inputs
	private static JTextField txtAxisMin, txtAxisMax, txtAxisSeparation,
			txtAxisEvery, txtAxisDecimals;

	// Fonts and colors
	private static JButton btnColorBands, btnFontNodes, btnColorNodes,
			btnFontAxisLabels, btnColorAxis, btnColorAxisLabels;
	private static Font fontNodesLabels, fontAxisLabels;
	private static Color colorBands, colorNodesLabels, colorAxis,
			colorAxisLabels;

	private TreeOrientationPanel pOrientationImg;

	public SettingsPanel(final PrincipalDesk fr) {
		super();
		createComponents();
		fillPanel();
	}

	private void createComponents() {
		lblTypeMeasure = Formats.getFormattedLabel(Language.getLabel(114) + " ");// Type of measure
		rbDistances = Formats.getFormattedRadioButton(Language.getLabel(27), true);// Distances
		rbSimilarities = Formats.getFormattedRadioButton(Language.getLabel(28), false);// Similarities

		lblMethod = Formats.getFormattedLabel(Language.getLabel(24) + " ");// Clustering algorithm
		final String strMethod[] = { "Single Linkage", "Complete Linkage",
				"Unweighted Average", "Weighted Average",
				"Unweighted Centroid", "Weighted Centroid",
				"Ward" };
		cbMethod = Formats.getFormattedComboBox(strMethod);
		cbMethod.setSelectedItem("Unweighted Average");

		lblPrecision = Formats.getFormattedLabel(Language.getLabel(51) + " ");// Precision
		txtPrecision = Formats.getformattedTextField("", 4, getLocale());

		lblTreeTitle = Formats.getFormattedShadedTitleLabel(" " + Language.getLabel(29));// TREE

		lblTreeOrientation = Formats.getFormattedLabel(Language.getLabel(26) + " ");// Tree orientation
		final String strTreeOrientation[] = { Language.getLabel(88),
				Language.getLabel(89), Language.getLabel(90),
				Language.getLabel(91) };
		cbTreeOrientation = Formats.getFormattedComboBox(strTreeOrientation);
		cbTreeOrientation.setSelectedItem(Language.getLabel(88));// North (by default)

		chkBands = Formats.getFormattedCheckBox(Language.getLabel(48));// Show bands
		chkBands.setSelected(InitialProperties.getShowBands());
		colorBands = InitialProperties.getColorDendroBand();

		lblNodesTitle = Formats.getFormattedShadedTitleLabel(" " + Language.getLabel(30));// NODES

		lblNodesSize = Formats.getFormattedLabel(Language.getLabel(113) + " ");// Nodes size
		final String strNodesSize[] = { "0", "2", "3", "4", "5", "6" };
		cbNodesSize = Formats.getFormattedComboBox(strNodesSize);
		cbNodesSize.setSelectedItem("0");

		chkNodesLabels = Formats.getFormattedCheckBox(Language.getLabel(31));// Show labels (nodes)
		chkNodesLabels.setSelected(InitialProperties.getShowNodeLabels());
		fontNodesLabels = InitialProperties.getFontDendroNames();
		colorNodesLabels = InitialProperties.getColorDendroNames();

		lblNodesOrientation = Formats.getFormattedLabel(Language.getLabel(33) + " ");// Labels orientation
		final String strLabelsOrientation[] = { Language.getLabel(94),
				Language.getLabel(92), Language.getLabel(93) };
		cbNodesOrientation = Formats.getFormattedComboBox(strLabelsOrientation);
		cbNodesOrientation.setSelectedItem(Language.getLabel(94));// Vertical (by default)

		chkUniformOrigin = Formats.getFormattedCheckBox(Language.getLabel(132));// Uniform origin
		chkUniformOrigin.setSelected(InitialProperties.getUniformOrigin());

		lblAxisTitle = Formats.getFormattedShadedTitleLabel(" " + Language.getLabel(36));// AXIS

		chkAxis = Formats.getFormattedCheckBox(Language.getLabel(37));// Show axis
		chkAxis.setSelected(InitialProperties.getShowAxis());
		colorAxis = InitialProperties.getColorDendroAxis();

		lblAxisMin = Formats.getFormattedLabel(Language.getLabel(41) + " ");// Minimum value
		txtAxisMin = Formats.getformattedTextField("", 4, getLocale());

		lblAxisMax = Formats.getFormattedLabel(Language.getLabel(42) + " ");// Maximum value
		txtAxisMax = Formats.getformattedTextField("", 4, getLocale());

		lblAxisSeparation = Formats.getFormattedLabel(Language.getLabel(43) + " ");// Tick separation
		txtAxisSeparation = Formats.getformattedTextField("", 4, getLocale());

		chkAxisLabels = Formats.getFormattedCheckBox(Language.getLabel(39));// Show labels (axis)
		chkAxisLabels.setSelected(InitialProperties.getShowAxisLabels());
		fontAxisLabels = InitialProperties.getFontDendroLabels();
		colorAxisLabels = InitialProperties.getColorDendroLabels();

		lblAxisEvery = Formats.getFormattedLabel(Language.getLabel(44));// Labels every
		txtAxisEvery = Formats.getformattedTextField("", 4, getLocale());
		lblAxisTicks = Formats.getFormattedLabel(" " + Language.getLabel(115));// ticks

		lblAxisDecimals = Formats.getFormattedLabel(Language.getLabel(49) + " ");// Labels decimals
		txtAxisDecimals = Formats.getformattedTextField("", 4, getLocale());
	}

	private void fillPanel() {
		setLayout(new GridBagLayout());
		setBorder(Formats.getFormattedTitledBorder(Language.getLabel(23))); // Settings
		final GridBagConstraints c = new GridBagConstraints();
		int gridy = 0;

		// group options
		ButtonGroup optSimType;
		optSimType = new ButtonGroup();
		optSimType.add(rbDistances);
		optSimType.add(rbSimilarities);

		// lbl type measure
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblTypeMeasure, c);
		// opt distances
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(rbDistances, c);
		// opt similarities
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 3);
		add(rbSimilarities, c);
		gridy++;

		// lbl method
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblMethod, c);
		// cb method
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 3);
		add(cbMethod, c);
		gridy++;

		// lbl precision
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblPrecision, c);
		// txt precision
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 3);
		txtPrecision.setName("precision");
		txtPrecision.addFocusListener(this);
		add(txtPrecision, c);
		gridy++;

		// empty space
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(new JLabel(" "), c);
		gridy++;

		// TREE

		// lbl tree title
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 3);
		add(lblTreeTitle, c);
		gridy++;

		// lbl tree orientation
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblTreeOrientation, c);
		// cb tree orientation
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		cbTreeOrientation.setActionCommand("orientacio_dendo");
		cbTreeOrientation.addActionListener(this);
		add(cbTreeOrientation, c);
		// tree drawing
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 2;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);
		pOrientationImg = new TreeOrientationPanel();
		add(pOrientationImg, c);
		gridy++;

		// checkbox bands
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkBands, c);
		// color bands
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnColorBands = Formats.getFormattedButton(Language.getLabel(35));// Color (bands)
		btnColorBands.setActionCommand("color_marge");
		btnColorBands.addActionListener(this);
		add(btnColorBands, c);
		gridy++;

		// empty space
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(new JLabel(" "), c);
		gridy++;

		// NODES

		// lbl nodes title
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 3);
		add(lblNodesTitle, c);
		gridy++;

		// lbl nodes size
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblNodesSize, c);
		// cb nodes size
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(cbNodesSize, c);
		gridy++;

		// chk nodes labels
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkNodesLabels, c);
		// btn font nodes
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnFontNodes = Formats.getFormattedButton(Language.getLabel(34));// Font (nodes)
		btnFontNodes.setActionCommand("font_noms");
		btnFontNodes.addActionListener(this);
		add(btnFontNodes, c);
		// btn color nodes
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 3);
		btnColorNodes = Formats.getFormattedButton(Language.getLabel(35));// Color (nodes)
		btnColorNodes.setActionCommand("color_noms");
		btnColorNodes.addActionListener(this);
		add(btnColorNodes, c);
		gridy++;

		// lbl nodes orientation
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblNodesOrientation, c);
		// cb nodes orientation
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(cbNodesOrientation, c);
		gridy++;

		// chk uniform origin
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkUniformOrigin, c);
		gridy++;

		// empty space
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(new JLabel(" "), c);
		gridy++;

		// AXIS

		// lbl axis title
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 3);
		add(lblAxisTitle, c);
		gridy++;

		// chk axis
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkAxis, c);
		// btn color axis
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnColorAxis = Formats.getFormattedButton(Language.getLabel(38));// Color (axis)
		btnColorAxis.setActionCommand("color_axis");
		btnColorAxis.addActionListener(this);
		add(btnColorAxis, c);
		gridy++;

		// lbl axis min
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblAxisMin, c);
		// txt axis min
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisMin.setName("axis_min");
		txtAxisMin.addFocusListener(this);
		add(txtAxisMin, c);
		gridy++;

		// lbl axis max
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblAxisMax, c);
		// txt axis max
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisMax.setName("axis_max");
		txtAxisMax.addFocusListener(this);
		add(txtAxisMax, c);
		gridy++;

		// lbl axis ticks separation
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblAxisSeparation, c);
		// txt axis ticks separation
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisSeparation.setName("axis_separation");
		txtAxisSeparation.addFocusListener(this);
		add(txtAxisSeparation, c);
		gridy++;

		// chk axis labels
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkAxisLabels, c);
		// btn font axis labels
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnFontAxisLabels = Formats.getFormattedButton(Language.getLabel(45));// Font (axis labels)
		btnFontAxisLabels.setActionCommand("font_axis");
		btnFontAxisLabels.addActionListener(this);
		add(btnFontAxisLabels, c);
		// btn color axis labels
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 3);
		btnColorAxisLabels = Formats.getFormattedButton(Language.getLabel(40));// Color (axis labels)
		btnColorAxisLabels.setActionCommand("color_label");
		btnColorAxisLabels.addActionListener(this);
		add(btnColorAxisLabels, c);
		gridy++;

		// lbl axis labels every
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblAxisEvery, c);
		// txt axis labels every
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisEvery.setName("axis_every");
		txtAxisEvery.addFocusListener(this);
		add(txtAxisEvery, c);
		// txt axis ticks
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisTicks, c);
		gridy++;

		// lbl axis decimals
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 3, 1, 1);
		add(lblAxisDecimals, c);
		// txt axis decimals
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisDecimals.setName("axis_decimals");
		txtAxisDecimals.addFocusListener(this);
		add(txtAxisDecimals, c);
		gridy++;

		// padding
		c.gridx = 0;
		c.gridy = gridy;
		c.weighty = 1.0;
		JLabel jt = new JLabel();
		add(jt, c);
		gridy++;
	}

	private Color changeColorFont(final Color c) {
		Color color = JColorChooser.showDialog(null, Language.getLabel(1), c);// Color selection
		if (color == null) {
			color = c;
		}
		return color;
	}

	public static SimilarityType getSimilarityType() {
		if (rbDistances.isSelected()) {
			return SimilarityType.DISTANCE;
		} else {
			return SimilarityType.SIMILARITY;
		}
	}

	public static MethodName getMethod() {
		return MethodName.values()[cbMethod.getSelectedIndex()];
	}

	public static int getPrecision() {
		int precision = -1;
		String str;

		str = txtPrecision.getText().trim();
		if (str.equals("")) {
			precision = -1;
		} else {
			try {
				precision = Integer.parseInt(str);
				if (precision < 0) {
					precision = -1;
				}
			} catch (NumberFormatException e) {
				precision = -1;
			}
		}
		return precision;
	}

	public static void setPrecision(final int precision) {
		txtPrecision.setText(String.valueOf(precision));
		LoadUpdatePanel.precisionCorrect = true;
	}

	private static double getMinValue() {
		double minValue = -1;
		String str;

		str = txtAxisMin.getText().trim();
		if (str.equals("")) {
			minValue = -1;
		} else {
			try {
				minValue = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				minValue = -1;
			}
		}
		return minValue;
	}

	private static void setMinValue(final double minValue) {
		txtAxisMin.setText(String.valueOf(minValue));
		LoadUpdatePanel.axisMinCorrect = true;
	}

	private static double getMaxValue() {
		double maxValue = -1;
		String str;

		str = txtAxisMax.getText().trim();
		if (str.equals("")) {
			maxValue = -1;
		} else {
			try {
				maxValue = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				maxValue = -1;
			}
		}
		return maxValue;
	}

	private static void setMaxValue(final double maxValue) {
		txtAxisMax.setText(String.valueOf(maxValue));
		LoadUpdatePanel.axisMaxCorrect = true;
	}

	private static double getTicksSeparation() {
		double ticksSeparation = -1;
		String str;

		str = txtAxisSeparation.getText().trim();
		if (str.equals("")) {
			ticksSeparation = -1;
		} else {
			try {
				ticksSeparation = Double.parseDouble(str);
				if (ticksSeparation <= 0) {
					ticksSeparation = -1;
				}
			} catch (NumberFormatException e) {
				ticksSeparation = -1;
			}
		}
		return ticksSeparation;
	}

	private static void setTicksSeparation(final double ticksSeparation) {
		txtAxisSeparation.setText(String.valueOf(ticksSeparation));
		LoadUpdatePanel.axisSeparationCorrect = true;
	}

	private static int getLabelsEvery() {
		int labelsEvery = -1;
		String str;

		str = txtAxisEvery.getText().trim();
		if (str.equals("")) {
			labelsEvery = -1;
		} else {
			try {
				labelsEvery = Integer.parseInt(str);
				if (labelsEvery < 0) {
					labelsEvery = -1;
				}
			} catch (NumberFormatException e) {
				labelsEvery = -1;
			}
		}
		return labelsEvery;
	}

	private static void setLabelsEvery(final int labelsEvery) {
		txtAxisEvery.setText(String.valueOf(labelsEvery));
		LoadUpdatePanel.axisTicksCorrect = true;
	}

	private static int getLabelsDecimals() {
		int labelsDecimals = -1;
		String str;

		str = txtAxisDecimals.getText().trim();
		if (str.equals("")) {
			labelsDecimals = -1;
		} else {
			try {
				labelsDecimals = Integer.parseInt(str);
				if (labelsDecimals < 0) {
					labelsDecimals = -1;
				}
			} catch (NumberFormatException e) {
				labelsDecimals = -1;
			}
		}
		return labelsDecimals;
	}

	private static void setLabelsDecimals(final int labelsDecimals) {
		txtAxisDecimals.setText(String.valueOf(labelsDecimals));
		LoadUpdatePanel.axisDecimalsCorrect = true;
	}

	public static SettingsInfo getSettingsInfo() {
		final SettingsInfo settings = new SettingsInfo();

		// DATA
		if (rbDistances.isSelected()) {
			settings.setSimilarityType(SimilarityType.DISTANCE);
		} else {
			settings.setSimilarityType(SimilarityType.SIMILARITY);
		}

		// METHOD
		settings.setMethod(MethodName.values()[cbMethod.getSelectedIndex()]);

		// PRECISION
		settings.setPrecision(getPrecision());

		// TREE
		settings.setDendrogramOrientation(DendrogramOrientation.values()[cbTreeOrientation.getSelectedIndex()]);

		// BANDS
		settings.setBandVisible(chkBands.isSelected());
		settings.setBandColor(colorBands);

		// NODES
		settings.setNodeNameVisible(chkNodesLabels.isSelected());
		settings.setNodeRadius(Integer.parseInt((String) cbNodesSize.getSelectedItem()));
		settings.setNodeNameOrientation(LabelOrientation.values()[cbNodesOrientation.getSelectedIndex()]);
		settings.setNodeNameFont(fontNodesLabels);
		settings.setNodeNameColor(colorNodesLabels);
		if (chkUniformOrigin.isSelected()) {
			settings.setOriginType(OriginType.UNIFORM_ORIGIN);
		} else {
			settings.setOriginType(OriginType.NON_UNIFORM_ORIGIN);
		}

		// AXIS
		settings.setAxisVisible(chkAxis.isSelected());
		settings.setAxisLabelVisible(chkAxisLabels.isSelected());
		settings.setAxisColor(colorAxis);
		settings.setAxisLabelColor(colorAxisLabels);

		// FONT AXIS LABELS
		settings.setAxisLabelFont(fontAxisLabels);

		// MIN and MAX
		settings.setAxisMinValue(getMinValue());
		settings.setAxisMaxValue(getMaxValue());

		// TICKS SEPARATION
		settings.setAxisIncrement(getTicksSeparation());

		// LABELS EVERY ...
		settings.setAxisTicks(getLabelsEvery());

		// DECIMALS
		settings.setAxisLabelDecimals(getLabelsDecimals());

		return settings;
	}

	public static void getConfigPanel(DendrogramParameters dendroParams) {
		// TYPE OF DATA
		if (rbDistances.isSelected()) {
			dendroParams.setSimilarityType(SimilarityType.DISTANCE);
		} else {
			dendroParams.setSimilarityType(SimilarityType.SIMILARITY);
		}

		// METHOD
		dendroParams.setMethod(MethodName.values()[cbMethod.getSelectedIndex()]);

		// PRECISION
		dendroParams.setPrecision(getPrecision());

		// TREE
		dendroParams.setDendrogramOrientation(DendrogramOrientation.values()[cbTreeOrientation
				.getSelectedIndex()]);

		// BANDS
		dendroParams.setBandVisible(chkBands.isSelected());
		dendroParams.setBandColor(colorBands);

		// NODES
		dendroParams.setNodeRadius(Integer.parseInt((String) cbNodesSize
				.getSelectedItem()));
		dendroParams.setNodeNameVisible(chkNodesLabels.isSelected());
		dendroParams.setNodeNameFont(fontNodesLabels);
		dendroParams.setNodeNameColor(colorNodesLabels);
		dendroParams.setNodeNameOrientation(LabelOrientation.values()[cbNodesOrientation.getSelectedIndex()]);
		dendroParams.setUniformOrigin(chkUniformOrigin.isSelected());

		// AXIS
		dendroParams.setAxisVisible(chkAxis.isSelected());
		dendroParams.setAxisColor(colorAxis);

		// MIN and MAX
		dendroParams.setAxisMinVal(getMinValue());
		dendroParams.setAxisMaxVal(getMaxValue());

		// TICKS SEPARATION
		dendroParams.setAxisIncrement(getTicksSeparation());

		// AXIS LABELS
		dendroParams.setAxisLabelVisible(chkAxisLabels.isSelected());
		dendroParams.setAxisLabelFont(fontAxisLabels);
		dendroParams.setAxisLabelColor(colorAxisLabels);

		// LABELS EVERY ...
		dendroParams.setAxisTicks(getLabelsEvery());

		// DECIMALS
		dendroParams.setAxisLabelDecimals(getLabelsDecimals());
	}

	public static void setConfigPanel(final DendrogramParameters dendroParams) {
		// Type of measure
		rbDistances.setSelected(dendroParams.getSimilarityType().equals(SimilarityType.DISTANCE));
		rbSimilarities.setSelected(dendroParams.getSimilarityType().equals(SimilarityType.SIMILARITY));

		// Clustering algorithm
		cbMethod.setSelectedIndex(dendroParams.getMethod().ordinal());

		// Precision
		setPrecision(dendroParams.getPrecision());

		// DendrogramOrientation of the tree
		cbTreeOrientation.setSelectedIndex(dendroParams.getDendrogramOrientation()
				.ordinal());

		// Bands
		chkBands.setSelected(dendroParams.isBandVisible());
		colorBands = dendroParams.getBandColor();

		// Nodes
		cbNodesSize.setSelectedItem(Integer.toString(dendroParams.getNodeRadius()));
		chkNodesLabels.setSelected(dendroParams.isNodeNameVisible());
		cbNodesOrientation.setSelectedIndex(dendroParams.getNodeNameOrientation().ordinal());
		fontNodesLabels = dendroParams.getNodeNameFont();
		colorNodesLabels = dendroParams.getNodeNameColor();
		chkUniformOrigin.setSelected(dendroParams.isUniformOrigin());

		// Axis
		chkAxis.setSelected(dendroParams.isAxisVisible());
		chkAxisLabels.setSelected(dendroParams.isAxisLabelVisible());
		colorAxis = dendroParams.getAxisColor();
		colorAxisLabels = dendroParams.getAxisLabelColor();

		// Font axis labels
		fontAxisLabels = dendroParams.getAxisLabelFont();

		// MIN
		setMinValue(dendroParams.getAxisMinVal());

		// MAX
		setMaxValue(dendroParams.getAxisMaxVal());

		// Ticks separation
		setTicksSeparation(dendroParams.getAxisIncrement());

		// Labels every ... ticks
		setLabelsEvery(dendroParams.getAxisTicks());

		// Labels decimals
		setLabelsDecimals(dendroParams.getAxisLabelDecimals());
	}

	public static void clearConfigPanel() {
		txtPrecision.setText("");
		LoadUpdatePanel.precisionCorrect = false;
		txtAxisMin.setText("");
		LoadUpdatePanel.axisMinCorrect = false;
		txtAxisMax.setText("");
		LoadUpdatePanel.axisMaxCorrect = false;
		txtAxisSeparation.setText("");
		LoadUpdatePanel.axisSeparationCorrect = false;
		txtAxisEvery.setText("");
		LoadUpdatePanel.axisTicksCorrect = false;
		txtAxisDecimals.setText("");
		LoadUpdatePanel.axisDecimalsCorrect = false;
	}

	public static void adjustValues(final Config cfg) {
		SimilarityType simType = cfg.getSimilarityType();
		int precision = cfg.getPrecision();
		setPrecision(precision);
		SettingsInfo settings = cfg.getConfigMenu();
		OriginType originType = settings.getOriginType();
		Cluster root = cfg.getRoot();
		SmartAxis smartAxis = new SmartAxis(simType, precision, originType, root);
		double min = smartAxis.smartMin();
		double max = smartAxis.smartMax();
		settings.setAxisMinValue(min);
		settings.setAxisMaxValue(max);
		Locale locale = new Locale("en");
		NumberFormat numberFormat = NumberFormat.getInstance(locale);
		numberFormat.setGroupingUsed(false);
		txtAxisMin.setText(numberFormat.format(min));
		LoadUpdatePanel.axisMinCorrect = true;
		txtAxisMax.setText(numberFormat.format(max));
		LoadUpdatePanel.axisMaxCorrect = true;
		double separation = smartAxis.smartTicksSize();
		setTicksSeparation(separation);
		setLabelsEvery(1);
		setLabelsDecimals(precision);
	}

	// ACTION EVENTS

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("orientacio_dendo")) {
			pOrientationImg.setImage(DendrogramOrientation.values()[cbTreeOrientation
					.getSelectedIndex()]);
		} else if (evt.getActionCommand().equals("color_marge")) {
			colorBands = this.changeColorFont(colorBands);
		} else if (evt.getActionCommand().equals("font_noms")) {
			final FontSelection f = new FontSelection(fontNodesLabels);
			f.setVisible(true);
			fontNodesLabels = f.getNewFont();
		} else if (evt.getActionCommand().equals("color_noms")) {
			colorNodesLabels = this.changeColorFont(colorNodesLabels);
		} else if (evt.getActionCommand().equals("color_axis")) {
			colorAxis = this.changeColorFont(colorAxis);
		} else if (evt.getActionCommand().equals("font_axis")) {
			final FontSelection f = new FontSelection(fontAxisLabels);
			f.setVisible(true);
			fontAxisLabels = f.getNewFont();
		} else if (evt.getActionCommand().equals("color_label")) {
			colorAxisLabels = this.changeColorFont(colorAxisLabels);
		} else {
			LogManager.LOG.warning(Language.getLabel(47) + ": " + evt.toString());
		}
	}

	// FOCUS EVENTS

	@Override
	public void focusGained(FocusEvent evt) {
	}

	@Override
	public void focusLost(FocusEvent evt) {
		if (!evt.isTemporary()) {
			if (evt.getComponent().getName().equals("precision")) {
				LoadUpdatePanel.precisionCorrect = checkPrecision();
			} else if (evt.getComponent().getName().equals("axis_min")) {
				LoadUpdatePanel.axisMinCorrect = checkMinValue();
			} else if (evt.getComponent().getName().equals("axis_max")) {
				LoadUpdatePanel.axisMaxCorrect = checkMaxValue();
			} else if (evt.getComponent().getName().equals("axis_separation")) {
				LoadUpdatePanel.axisSeparationCorrect = checkTicksSeparation();
			} else if (evt.getComponent().getName().equals("axis_every")) {
				LoadUpdatePanel.axisTicksCorrect = checkLabelsEvery();
			} else if (evt.getComponent().getName().equals("axis_decimals")) {
				LoadUpdatePanel.axisDecimalsCorrect = checkLabelsDecimals();
			}
			LoadUpdatePanel.enableUpdate();
		}
	}

	private boolean checkPrecision() {
		boolean correct = true;
		String str = txtPrecision.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(2));
		} else {
			try {
				int precision = Integer.parseInt(str);
				if (precision < 0) {
					correct = false;
					showError(Language.getLabel(2));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(2));
			}
		}
		return correct;
	}

	private boolean checkMinValue() {
		boolean correct = true;
		String str = txtAxisMin.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(3));
		} else {
			try {
				double minValue = Double.parseDouble(str);
				if (minValue > getMaxValue()) {
					correct = false;
					showError(Language.getLabel(19));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(3));
			}
		}
		return correct;
	}

	private boolean checkMaxValue() {
		boolean correct = true;
		String str = txtAxisMax.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(4));
		} else {
			try {
				double maxValue = Double.parseDouble(str);
				if (maxValue < getMinValue()) {
					correct = false;
					showError(Language.getLabel(19));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(4));
			}
		}
		return correct;
	}

	private boolean checkTicksSeparation() {
		boolean correct = true;
		String str = txtAxisSeparation.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(5));
		} else {
			try {
				double ticksSeparation = Double.parseDouble(str);
				if (ticksSeparation <= 0) {
					correct = false;
					showError(Language.getLabel(5));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(5));
			}
		}
		return correct;
	}

	private boolean checkLabelsEvery() {
		boolean correct = true;
		String str = txtAxisEvery.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(6));
		} else {
			try {
				int labelsEvery = Integer.parseInt(str);
				if (labelsEvery < 0) {
					correct = false;
					showError(Language.getLabel(6));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(6));
			}
		}
		return correct;
	}

	private boolean checkLabelsDecimals() {
		boolean correct = true;
		String str = txtAxisDecimals.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(50));
		} else {
			try {
				int labelsDecimals = Integer.parseInt(str);
				if (labelsDecimals < 0) {
					correct = false;
					showError(Language.getLabel(50));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(50));
			}
		}
		return correct;
	}

	private static void showError(String message) {
		JOptionPane.showMessageDialog(null, message, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}

	// INTERNAL FRAME EVENTS

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
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

}
