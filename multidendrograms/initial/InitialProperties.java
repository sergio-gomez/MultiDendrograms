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

import java.awt.Color;
import java.awt.Font;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * It defines all the parameters of the application and it initializes them
 * with the values in the file md.ini, or with default values if the file is missing
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class InitialProperties {

	// Language
	private static String language = "ini/lang_english.l";

	// Data
	private static double missingValue = 0.0;

	// Windows
	private static int widthMainWindow = 800;
	private static int heightMainWindow = 690;
	private static int widthDendroWindow = 400;
	private static int heightDendroWindow = 400;

	// Dendrograms
	private static Font fontDendroNames = new Font("Arial", Font.PLAIN, 10);
	private static Color colorDendroNames = Color.BLACK;
	private static Font fontDendroLabels = new Font("Arial", Font.PLAIN, 10);
	private static Color colorDendroLabels = Color.BLACK;
	private static Color colorDendroAxis = Color.BLACK;;
	private static Color colorDendroBand = Color.LIGHT_GRAY;
	private static double sizeDendroMargin = 15.0;

	// Settings
	private static boolean showBands = true;
	private static boolean showNodeLabels = true;
	private static boolean uniformOrigin = true;
	private static boolean showAxis = true;
	private static boolean showAxisLabels = true;

	// Label
	private static Font fontLabel = new Font("Arial", Font.PLAIN, 10);
	private static Color colorLabel = Color.BLACK;

	// Title label
	private static Font fontTitle = new Font("Arial", Font.BOLD, 10);
	private static Color colorTitle = Color.BLACK;
	private static Color colorTitleBackground = Color.GRAY;

	// TextField
	private static Font fontTextField = new Font("Arial", Font.PLAIN, 10);
	private static Color colorTextField = Color.BLACK;
	private static Color colorTextFieldBackground = Color.LIGHT_GRAY;

	// MenuItem
	private static Font fontMenuItem = new Font("Arial", Font.PLAIN, 10);
	private static Color colorMenuItem = Color.BLACK;

	// Button
	private static Font fontButton = new Font("Arial", Font.PLAIN, 10);
	private static Color colorButton = Color.BLACK;

	// ComboBox
	private static Font fontComboBox = new Font("Arial", Font.PLAIN, 10);
	private static Color colorComboBox = Color.BLACK;
	private static Color colorComboBoxBackground = Color.LIGHT_GRAY;

	// CheckBox
	private static Font fontCheckBox = new Font("Arial", Font.PLAIN, 10);
	private static Color colorCheckBox = Color.BLACK;

	// RadioButton
	private static Font fontRadioButton = new Font("Arial", Font.PLAIN, 10);
	private static Color colorRadioButton = Color.BLACK;

	// Tree
	private static Font fontTreeLeaf = new Font("Arial", Font.BOLD, 10);
	private static Color colorTreeLeaf = Color.BLACK;
	private static Font fontTreeNum = new Font("Arial", Font.PLAIN, 10);
	private static Color colorTreeNum = Color.BLACK;
	private static Font fontTreeBand = new Font("Arial", Font.PLAIN, 10);
	private static Color colorTreeBand = Color.BLACK;


	public InitialProperties() {
		LogManager.LOG.info("Initializing InitialProperties");
		setProperties();
	}


	public static String getLanguage() {
		return InitialProperties.language;
	}

	public static void setLanguage(final String language) {
		InitialProperties.language = language;
	}


	public static double getMissingValue() {
		return InitialProperties.missingValue;
	}

	public static void setMissingValue(final double missingValue) {
		InitialProperties.missingValue = missingValue;
	}


	public static int getHeightMainWindow() {
		return InitialProperties.heightMainWindow;
	}

	public static int getWidthMainWindow() {
		return InitialProperties.widthMainWindow;
	}

	public static int getHeightDendroWindow() {
		return InitialProperties.heightDendroWindow;
	}

	public static int getWidthDendroWindow() {
		return InitialProperties.widthDendroWindow;
	}


	public static Font getFontDendroNames() {
		return InitialProperties.fontDendroNames;
	}

	public static void setFontDendroNames(final Font fontDendroNames) {
		InitialProperties.fontDendroNames = fontDendroNames;
	}

	public static Color getColorDendroNames() {
		return InitialProperties.colorDendroNames;
	}

	public static void setColorDendroNames(final Color colorDendroNames) {
		InitialProperties.colorDendroNames = colorDendroNames;
	}

	public static Font getFontDendroLabels() {
		return InitialProperties.fontDendroLabels;
	}

	public static void setFontDendroLabels(final Font fontDendroLabels) {
		InitialProperties.fontDendroLabels = fontDendroLabels;
	}

	public static Color getColorDendroLabels() {
		return InitialProperties.colorDendroLabels;
	}

	public static void setColorDendroLabels(final Color colorDendroLabels) {
		InitialProperties.colorDendroLabels = colorDendroLabels;
	}

	public static Color getColorDendroAxis() {
		return InitialProperties.colorDendroAxis;
	}

	public static void setColorDendroAxis(final Color colorDendroAxis) {
		InitialProperties.colorDendroAxis = colorDendroAxis;
	}

	public static Color getColorDendroBand() {
		return InitialProperties.colorDendroBand;
	}

	public static void setColorDendroBand(final Color colorDendroBand) {
		InitialProperties.colorDendroBand = colorDendroBand;
	}

	public static double getSizeDendroMargin() {
		return InitialProperties.sizeDendroMargin;
	}

	public static boolean getShowBands() {
		return InitialProperties.showBands;
	}

	public static boolean getShowNodeLabels() {
		return InitialProperties.showNodeLabels;
	}

	public static boolean getUniformOrigin() {
		return InitialProperties.uniformOrigin;
	}

	public static boolean getShowAxis() {
		return InitialProperties.showAxis;
	}

	public static boolean getShowAxisLabels() {
		return InitialProperties.showAxisLabels;
	}

	public static Font getFontLabel() {
		return InitialProperties.fontLabel;
	}

	public static void setFontLabel(final Font fontLabel) {
		InitialProperties.fontLabel = fontLabel;
	}

	public static Color getColorLabel() {
		return InitialProperties.colorLabel;
	}

	public static void setColorLabel(final Color colorLabel) {
		InitialProperties.colorLabel = colorLabel;
	}


	public static Font getFontTitle() {
		return InitialProperties.fontTitle;
	}

	public static void setFontTitle(final Font fontTitle) {
		InitialProperties.fontTitle = fontTitle;
	}

	public static Color getColorTitle() {
		return InitialProperties.colorTitle;
	}

	public static void setColorTitle(final Color colorTitle) {
		InitialProperties.colorTitle = colorTitle;
	}

	public static Color getColorTitleBackground() {
		return InitialProperties.colorTitleBackground;
	}

	public static void setColorTitleBackground(
			final Color colorTitleBackground) {
		InitialProperties.colorTitleBackground = colorTitleBackground;
	}


	public static Font getFontTextField() {
		return InitialProperties.fontTextField;
	}

	public static void setFontTextField(final Font fontTextField) {
		InitialProperties.fontTextField = fontTextField;
	}

	public static Color getColorTextField() {
		return InitialProperties.colorTextField;
	}

	public static void setColorTextField(final Color colorTextField) {
		InitialProperties.colorTextField = colorTextField;
	}

	public static Color getColorTextFieldBackground() {
		return InitialProperties.colorTextFieldBackground;
	}

	public static void setColorTextFieldBackground(
			final Color colorTextFieldBackground) {
		InitialProperties.colorTextFieldBackground = colorTextFieldBackground;
	}


	public static Font getFontMenuItem() {
		return InitialProperties.fontMenuItem;
	}

	public static void setFontMenuItem(final Font fontMenuItem) {
		InitialProperties.fontMenuItem = fontMenuItem;
	}

	public static Color getColorMenuItem() {
		return InitialProperties.colorMenuItem;
	}

	public static void setColorMenuItem(final Color colorMenuItem) {
		InitialProperties.colorMenuItem = colorMenuItem;
	}


	public static Font getFontButton() {
		return InitialProperties.fontButton;
	}

	public static void setFontButton(final Font fontButton) {
		InitialProperties.fontButton = fontButton;
	}

	public static Color getColorButton() {
		return InitialProperties.colorButton;
	}

	public static void setColorButton(final Color colorButton) {
		InitialProperties.colorButton = colorButton;
	}


	public static Font getFontComboBox() {
		return InitialProperties.fontComboBox;
	}

	public static void setFontComboBox(final Font fontComboBox) {
		InitialProperties.fontComboBox = fontComboBox;
	}

	public static Color getColorComboBox() {
		return InitialProperties.colorComboBox;
	}

	public static void setColorComboBox(final Color colorComboBox) {
		InitialProperties.colorComboBox = colorComboBox;
	}

	public static Color getColorComboBoxBackground() {
		return InitialProperties.colorComboBoxBackground;
	}

	public static void setColorComboBoxBackground(final Color colorComboBoxBackground) {
		InitialProperties.colorComboBoxBackground = colorComboBoxBackground;
	}


	public static Font getFontCheckBox() {
		return InitialProperties.fontCheckBox;
	}

	public static void setFontCheckBox(final Font fontCheckBox) {
		InitialProperties.fontCheckBox = fontCheckBox;
	}

	public static Color getColorCheckBox() {
		return InitialProperties.colorCheckBox;
	}

	public static void setColorCheckBox(final Color colorCheckBox) {
		InitialProperties.colorCheckBox = colorCheckBox;
	}


	public static Font getFontRadioButton() {
		return InitialProperties.fontRadioButton;
	}

	public static void setFontRadioButton(final Font fontRadioButton) {
		InitialProperties.fontRadioButton = fontRadioButton;
	}

	public static Color getColorRadioButton() {
		return InitialProperties.colorRadioButton;
	}

	public static void setColorRadioButton(final Color colorRadioButton) {
		InitialProperties.colorRadioButton = colorRadioButton;
	}


	public static Font getFontTreeLeaf() {
		return InitialProperties.fontTreeLeaf;
	}

	public static void setFontTreeLeaf(final Font fontTreeLeaf) {
		InitialProperties.fontTreeLeaf = fontTreeLeaf;
	}

	public static Color getColorTreeLeaf() {
		return InitialProperties.colorTreeLeaf;
	}

	public static void setColorTreeLeaf(final Color colorTreeLeaf) {
		InitialProperties.colorTreeLeaf = colorTreeLeaf;
	}

	public static Font getFontTreeNum() {
		return InitialProperties.fontTreeNum;
	}

	public static void setFontTreeNum(final Font fontTreeNum) {
		InitialProperties.fontTreeNum = fontTreeNum;
	}

	public static Color getColorTreeNum() {
		return InitialProperties.colorTreeNum;
	}

	public static void setColorTreeNum(final Color colorTreeNum) {
		InitialProperties.colorTreeNum = colorTreeNum;
	}

	public static Font getFontTreeBand() {
		return InitialProperties.fontTreeBand;
	}

	public static void setFontTreeBand(final Font fontTreeBand) {
		InitialProperties.fontTreeBand = fontTreeBand;
	}

	public static Color getColorTreeBand() {
		return InitialProperties.colorTreeBand;
	}

	public static void setColorTreeBand(final Color colorTreeBand) {
		InitialProperties.colorTreeBand = colorTreeBand;
	}


	public void setProperties() {
		// Language
		InitialProperties.language = this.getString("language", InitialProperties.language);

		// Data
		InitialProperties.missingValue = this.getDouble("missingValue", InitialProperties.missingValue);

		// Windows
		InitialProperties.heightMainWindow = this.getInteger("heightMainWindow", InitialProperties.heightMainWindow);
		InitialProperties.widthMainWindow = this.getInteger("widthMainWindow", InitialProperties.widthMainWindow);
		InitialProperties.heightDendroWindow = this.getInteger("heightDendroWindow", InitialProperties.heightDendroWindow);
		InitialProperties.widthDendroWindow = this.getInteger("widthDendroWindow", InitialProperties.widthDendroWindow);

		// Dendrograms
		InitialProperties.fontDendroNames = this.getFont("fontDendroNames", InitialProperties.fontDendroNames);
		InitialProperties.colorDendroNames = this.getColor("colorDendroNames", InitialProperties.colorDendroNames);
		InitialProperties.fontDendroLabels = this.getFont("fontDendroLabels", InitialProperties.fontDendroLabels);
		InitialProperties.colorDendroLabels = this.getColor("colorDendroLabels", InitialProperties.colorDendroLabels);
		InitialProperties.colorDendroAxis = this.getColor("colorDendroAxis", InitialProperties.colorDendroAxis);
		InitialProperties.colorDendroBand = this.getColor("colorDendroBand", InitialProperties.colorDendroBand);
		InitialProperties.sizeDendroMargin = this.getDouble("sizeDendroMargin", InitialProperties.sizeDendroMargin);

		// Settings
		InitialProperties.showBands = this.getBoolean("showBands", InitialProperties.showBands);
		InitialProperties.showNodeLabels = this.getBoolean("showNodeLabels", InitialProperties.showNodeLabels);
		InitialProperties.uniformOrigin = this.getBoolean("uniformOrigin", InitialProperties.uniformOrigin);
		InitialProperties.showAxis = this.getBoolean("showAxis", InitialProperties.showAxis);
		InitialProperties.showAxisLabels = this.getBoolean("showAxisLabels", InitialProperties.showAxisLabels);

		// Label
		InitialProperties.fontLabel = this.getFont("fontLabel", InitialProperties.fontLabel);
		InitialProperties.colorLabel = this.getColor("colorLabel", InitialProperties.colorLabel);

		// Title label
		InitialProperties.fontTitle = this.getFont("fontTitle", InitialProperties.fontTitle);
		InitialProperties.colorTitle = this.getColor("colorTitle", InitialProperties.colorTitle);
		InitialProperties.colorTitleBackground = this.getColor("colorTitleBackground", InitialProperties.colorTitleBackground);

		// TextField
		InitialProperties.fontTextField = this.getFont("fontTextField", InitialProperties.fontTextField);
		InitialProperties.colorTextField = this.getColor("colorTextField", InitialProperties.colorTextField);
		InitialProperties.colorTextFieldBackground = this.getColor("colorTextFieldBackground", InitialProperties.colorTextFieldBackground);

		// MenuItem
		InitialProperties.fontMenuItem = this.getFont("fontMenuItem", InitialProperties.fontMenuItem);
		InitialProperties.colorMenuItem = this.getColor("colorMenuItem", InitialProperties.colorMenuItem);

		// Button
		InitialProperties.fontButton = this.getFont("fontButton", InitialProperties.fontButton);
		InitialProperties.colorButton = this.getColor("colorButton", InitialProperties.colorButton);

		// ComboBox
		InitialProperties.fontComboBox = this.getFont("fontComboBox", InitialProperties.fontComboBox);
		InitialProperties.colorComboBox = this.getColor("colorComboBox", InitialProperties.colorComboBox);
		InitialProperties.colorComboBoxBackground = this.getColor("colorComboBoxBackground", InitialProperties.colorComboBoxBackground);

		// CheckBox
		InitialProperties.fontCheckBox = this.getFont("fontCheckBox", InitialProperties.fontCheckBox);
		InitialProperties.colorCheckBox = this.getColor("colorCheckBox", InitialProperties.colorCheckBox);

		// RadioButton
		InitialProperties.fontRadioButton = this.getFont("fontRadioButton", InitialProperties.fontRadioButton);
		InitialProperties.colorRadioButton = this.getColor("colorRadioButton", InitialProperties.colorRadioButton);

		// Tree
		InitialProperties.fontTreeLeaf = this.getFont("fontTreeLeaf", InitialProperties.fontTreeLeaf);
		InitialProperties.colorTreeLeaf = this.getColor("colorTreeLeaf", InitialProperties.colorTreeLeaf);
		InitialProperties.fontTreeNum = this.getFont("fontTreeNum", InitialProperties.fontTreeNum);
		InitialProperties.colorTreeNum = this.getColor("colorTreeNum", InitialProperties.colorTreeNum);
		InitialProperties.fontTreeBand = this.getFont("fontTreeBand", InitialProperties.fontTreeBand);
		InitialProperties.colorTreeBand = this.getColor("colorTreeBand", InitialProperties.colorTreeBand);
	}

	private String getString(final String label, String defaultString) {
		String s = defaultString;

		try {
			s = MainProperties.getProperty(label);
		} catch (final Exception e) {
			s = defaultString;
			LogManager.LOG.warning("Unable to assign string from label '" + label + "', using the default value");
		}
		return s;
	}

	private int getInteger(final String label, int defaultInteger) {
		String s_int;
		int i = defaultInteger;

		try {
			s_int = MainProperties.getProperty(label);
			if (s_int != null) {
				i = Integer.parseInt(s_int);
			}
		} catch (final Exception e) {
			i = defaultInteger;
			LogManager.LOG.warning("Unable to assign integer from label '" + label + "', using the default value");
		}
		return i;
	}

	private double getDouble(final String label, double defaultDouble) {
		String s_double;
		double d = defaultDouble;

		try {
			s_double = MainProperties.getProperty(label);
			if (s_double != null) {
				d = Double.parseDouble(s_double);
			}
		} catch (final Exception e) {
			d = defaultDouble;
			LogManager.LOG.warning("Unable to assign double from label '" + label + "', using the default value");
		}
		return d;
	}

	private boolean getBoolean(final String label, boolean defaultBoolean) {
		String s_bool;
		boolean b = defaultBoolean;
		int val;

		try {
			s_bool = MainProperties.getProperty(label);
			if (s_bool != null) {
				val = Integer.parseInt(s_bool);
				b = (val == 0) ? false : true;
			}
		} catch (final Exception e) {
			b = defaultBoolean;
			LogManager.LOG.warning("Unable to assign booean from label '" + label + "', using the default value");
		}
		return b;
	}

	private Font getFont(final String label, Font defaultFont) {
		String lbl1, lbl2, lbl3;
		String s_size, s_name, s_style;
		int i_size, i_style;
		Font f = defaultFont;

		lbl1 = label + "_size";
		lbl2 = label + "_name";
		lbl3 = label + "_style";
		try {
			s_size = MainProperties.getProperty(lbl1);
			s_name = MainProperties.getProperty(lbl2);
			s_style = MainProperties.getProperty(lbl3);
			if ((s_size != null) && (s_name != null) && (s_style != null)) {
				i_style = Integer.parseInt(s_style);
				i_size = Integer.parseInt(s_size);
				f = new Font(s_name, i_style, i_size);
			}
		} catch (final Exception e) {
			f = defaultFont;
			LogManager.LOG.warning("Unable to assign font from label '" + label + "', using the default font");
		}
		return f;
	}

	private Color getColor(final String label, Color defaultColor) {
		String lbl1, lbl2, lbl3;
		String s_r, s_g, s_b;
		int i_r, i_g, i_b;
		Color c = defaultColor;

		lbl1 = label + "_R";
		lbl2 = label + "_G";
		lbl3 = label + "_B";

		try {
			s_r = MainProperties.getProperty(lbl1);
			s_g = MainProperties.getProperty(lbl2);
			s_b = MainProperties.getProperty(lbl3);

			if ((s_r != null) && (s_g != null) && (s_b != null)) {
				i_r = Integer.parseInt(s_r);
				i_g = Integer.parseInt(s_g);
				i_b = Integer.parseInt(s_b);

				c = new Color(i_r, i_g, i_b);
			}
		} catch (final Exception e) {
			c = defaultColor;
			LogManager.LOG.warning("Unable to assign color from label '" + label + "', using the default color");
		}
		;
		return c;
	}

}