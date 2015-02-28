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

import multidendrograms.initial.Main;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * It defines all the parameters of the application and it initializes them
 * with the values in the file dendo.ini, or with default values if the file is missing
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class InitialProperties {

	private static String sPath_language = "";

	private static double missingValue = 0.0;

	private static double margin = 15;

	private static int width_frmPrincipal = 800;

	private static int height_frmPrincipal = 690;

	private static double radius = 5;

	private static double factorAxis = 10.0;

	private static int factorTicks = 10;

	private static int width_frmDesk = 400;

	private static int height_frmDesk = 400;

	private static String titleWin = Main.PROGRAM;

	private static String titleDesk = Main.PROGRAM;

	private static Color color_title_font = Color.BLACK;
	private static Color color_title_background = Color.GRAY;
	private static Font fontMenuTitle = new Font("Arial", Font.BOLD, 10);

	private static Color color_jtxt_background = Color.LIGHT_GRAY;
	private static Color color_jtxt_font = Color.BLACK;
	private static Font fontMenuTXT = new Font("Arial", Font.BOLD, 10);

	private static Color color_jarea_background = Color.LIGHT_GRAY;
	private static Color color_jarea_font = Color.BLACK;
	private static Font fontMenuAREA = new Font("Arial", Font.BOLD, 10);

	private static Color color_cb_background = Color.LIGHT_GRAY;
	private static Color color_cb_font = Color.BLACK;
	private static Font fontMenuCB = new Font("Arial", Font.BOLD, 10);

	private static Color color_label_font = Color.BLACK;
	private static Font fontMenuLabel = new Font("Arial", Font.BOLD, 10);

	private static Color color_chk_font = Color.BLACK;

	private static Font fontMenuCHK = new Font("Arial", Font.BOLD, 10);

	private static Color colorBand = Color.LIGHT_GRAY;

	private static Color color_opt_font = Color.BLACK;
	private static Font fontMenuOPT = new Font("Arial", Font.BOLD, 10);

	private static Font fontNames = new Font("Arial", Font.PLAIN, 10);

	private static Font fontAxis = new Font("Arial", Font.PLAIN, 10);

	private static Color colorNames = Color.BLACK;

	private static Color colorAxis = Color.BLACK;;

	private static Color colorLabels = Color.BLACK;

	public InitialProperties() {
		LogManager.LOG.info("Creat nou objecte");
	}

	public static Color getColorBand() {
		return InitialProperties.colorBand;
	}

	public static void setColorBand(final Color color) {
		InitialProperties.colorBand = color;
	}

	public static int getHeight_frmPrincipal() {
		return InitialProperties.height_frmPrincipal;
	}

	public static double getRadius() {
		return InitialProperties.radius;
	}

	public void setRadius(final double radius) {
		InitialProperties.radius = radius;
	}

	public static double getFactorAxis() {
		return factorAxis;
	}

	public static void setFactorAxis(double factorAxis) {
		InitialProperties.factorAxis = factorAxis;
	}

	public static int getFactorTicks() {
		return factorTicks;
	}

	public static void setFactorTicks(int factorTicks) {
		InitialProperties.factorTicks = factorTicks;
	}

	public static String getTitleWin() {
		return InitialProperties.titleWin;
	}

	public void setTitleWin(final String title) {
		InitialProperties.titleWin = title;
	}

	public static String getTitleDesk() {
		return InitialProperties.titleDesk;
	}

	public static void setTitleDesk(final String titleDesk) {
		InitialProperties.titleDesk = titleDesk;
	}

	public void setWidth_frmDesk(final int width_frmArrel) {
		InitialProperties.width_frmDesk = width_frmArrel;
	}

	public static int getWidth_frmPrincipal() {
		return InitialProperties.width_frmPrincipal;
	}

	public static Color getColor_title_background() {
		return InitialProperties.color_title_background;
	}

	public static void setColor_title_background(
			final Color color_title_background) {
		InitialProperties.color_title_background = color_title_background;
	}

	public static Color getColor_title_font() {
		return InitialProperties.color_title_font;
	}

	public static void setColor_title_font(final Color color_title_font) {
		InitialProperties.color_title_font = color_title_font;
	}

	public static Font getFontMenuTitle() {
		return InitialProperties.fontMenuTitle;
	}

	public static void setFontMenuTitle(final Font fontMenuTitle) {
		InitialProperties.fontMenuTitle = fontMenuTitle;
	}

	public static double getMissingValue() {
		return InitialProperties.missingValue;
	}

	public static void setMissingValue(final double missingValue) {
		InitialProperties.missingValue = missingValue;
	}

	public static double getMargin() {
		return InitialProperties.margin;
	}

	public static void setMargin(final double margin) {
		InitialProperties.margin = margin;
	}

	public static String getSPath_language() {
		return InitialProperties.sPath_language;
	}

	public static void setSPath_language(final String path_language) {
		InitialProperties.sPath_language = path_language;
	}

	public static Color getColorAxis() {
		return InitialProperties.colorAxis;
	}

	public static void setColorAxis(final Color colorAxis) {
		InitialProperties.colorAxis = colorAxis;
	}

	public static Color getColorLabels() {
		return InitialProperties.colorLabels;
	}

	public static void setColorLabels(final Color colorLabels) {
		InitialProperties.colorLabels = colorLabels;
	}

	public static Color getColorNames() {
		return InitialProperties.colorNames;
	}

	public static void setColorNames(final Color colorNames) {
		InitialProperties.colorNames = colorNames;
	}

	public static Font getFontAxis() {
		return InitialProperties.fontAxis;
	}

	public static void setFontAxis(final Font fontAxis) {
		InitialProperties.fontAxis = fontAxis;
	}

	public static Font getFontNames() {
		return InitialProperties.fontNames;
	}

	public static void setFontNames(final Font fontNames) {
		InitialProperties.fontNames = fontNames;
	}

	public static Color getColor_jarea_background() {
		return InitialProperties.color_jarea_background;
	}

	public static void setColor_jarea_background(
			final Color color_jarea_background) {
		InitialProperties.color_jarea_background = color_jarea_background;
	}

	public static Color getColor_jarea_font() {
		return InitialProperties.color_jarea_font;
	}

	public static void setColor_jarea_font(final Color color_jarea_font) {
		InitialProperties.color_jarea_font = color_jarea_font;
	}

	public static Font getFontMenuAREA() {
		return InitialProperties.fontMenuAREA;
	}

	public static void setFontMenuAREA(final Font fontMenuAREA) {
		InitialProperties.fontMenuAREA = fontMenuAREA;
	}

	public static Color getColor_jtxt_background() {
		return InitialProperties.color_jtxt_background;
	}

	public static void setColor_jtxt_background(
			final Color color_jtxt_background) {
		InitialProperties.color_jtxt_background = color_jtxt_background;
	}

	public static Color getColor_jtxt_font() {
		return InitialProperties.color_jtxt_font;
	}

	public static void setColor_jtxt_font(final Color color_jtxt_font) {
		InitialProperties.color_jtxt_font = color_jtxt_font;
	}

	public static Font getFontMenuTXT() {
		return InitialProperties.fontMenuTXT;
	}

	public static void setFontMenuTXT(final Font fontMenuTXT) {
		InitialProperties.fontMenuTXT = fontMenuTXT;
	}

	public static Color getColor_opt_font() {
		return InitialProperties.color_opt_font;
	}

	public static void setColor_opt_font(final Color color_opt_font) {
		InitialProperties.color_opt_font = color_opt_font;
	}

	public static Font getFontMenuOPT() {
		return InitialProperties.fontMenuOPT;
	}

	public static void setFontMenuOPT(final Font fontMenuOPT) {
		InitialProperties.fontMenuOPT = fontMenuOPT;
	}

	public static Color getColor_cb_background() {
		return InitialProperties.color_cb_background;
	}

	public static void setColor_cb_background(final Color color_cb_background) {
		InitialProperties.color_cb_background = color_cb_background;
	}

	public static Color getColor_cb_font() {
		return InitialProperties.color_cb_font;
	}

	public static void setColor_cb_font(final Color color_cb_font) {
		InitialProperties.color_cb_font = color_cb_font;
	}

	public static Font getFontMenuCB() {
		return InitialProperties.fontMenuCB;
	}

	public static void setFontMenuCB(final Font fontMenuCB) {
		InitialProperties.fontMenuCB = fontMenuCB;
	}

	public static Color getColor_chk_font() {
		return InitialProperties.color_chk_font;
	}

	public static void setColor_chk_font(final Color color_chk_font) {
		InitialProperties.color_chk_font = color_chk_font;
	}

	public static Font getFontMenuCHK() {
		return InitialProperties.fontMenuCHK;
	}

	public static void setFontMenuCHK(final Font fontMenuCHK) {
		InitialProperties.fontMenuCHK = fontMenuCHK;
	}

	public static Color getColor_label_font() {
		return InitialProperties.color_label_font;
	}

	public static void setColor_label_font(final Color color_label_font) {
		InitialProperties.color_label_font = color_label_font;
	}

	public static Font getFontMenuLabel() {
		return InitialProperties.fontMenuLabel;
	}

	public static void setFontMenuLabel(final Font fontMenuLabel) {
		InitialProperties.fontMenuLabel = fontMenuLabel;
	}

	public static int getWidth_frmDesk() {
		return InitialProperties.width_frmDesk;
	}

	public static int getHeight_frmDesk() {
		return InitialProperties.height_frmDesk;
	}

	public void setParametres(final MainProperties ap) {
		String s_tmp;
		Font f;
		Color c;

		try {
			s_tmp = MainProperties.getProperty("language");
			if (s_tmp != null) {
				LogManager.LOG.config("Loading language: " + s_tmp);
				InitialProperties.sPath_language = s_tmp;
			}
		} catch (final Exception e) {
			LogManager.LOG.warning("Language property not found");
			InitialProperties.sPath_language = "";
		}
		;

		try {
			s_tmp = MainProperties.getProperty("missingValue");
			if (s_tmp != null) {
				InitialProperties.missingValue = Double.parseDouble(s_tmp);
			}
		} catch (final Exception e) {
			InitialProperties.missingValue = 0.0;
		}

		try {
			s_tmp = MainProperties.getProperty("height_frmPrincipal");
			if (s_tmp != null) {
				InitialProperties.height_frmPrincipal = Integer
						.parseInt(s_tmp);
			}
		} catch (final Exception e) {
			InitialProperties.height_frmPrincipal = 690;
		}

		try {
			s_tmp = MainProperties.getProperty("width_frmPrincipal");
			if (s_tmp != null) {
				InitialProperties.width_frmPrincipal = Integer
						.parseInt(s_tmp);
			}
		} catch (final Exception e) {
			InitialProperties.width_frmPrincipal = 800;
		}

		try {
			s_tmp = MainProperties.getProperty("radius");
			if (s_tmp != null) {
				InitialProperties.radius = Double.parseDouble(s_tmp);
			}
		} catch (final Exception e) {
			InitialProperties.radius = 5.0;
		}
		;

		try {
			s_tmp = MainProperties.getProperty("height_frmDesk");
			if (s_tmp != null) {
				InitialProperties.height_frmDesk = Integer.parseInt(s_tmp);
			}
		} catch (final Exception e) {
			InitialProperties.height_frmDesk = 400;
		}
		;

		try {
			s_tmp = MainProperties.getProperty("width_frmDesk");
			if (s_tmp != null) {
				InitialProperties.width_frmDesk = Integer.parseInt(s_tmp);
			}
		} catch (final Exception e) {
			InitialProperties.width_frmDesk = 400;
		}
		;

		c = this.getColor("colorBand");
		if (c != null) {
			InitialProperties.colorBand = c;
		}

		f = this.getFont("fontNames");
		if (f != null) {
			InitialProperties.fontNames = f;
		}
		f = this.getFont("fontAxis");
		if (f != null) {
			InitialProperties.fontAxis = f;
		}

		c = this.getColor("colorNames");
		if (c != null) {
			InitialProperties.colorNames = c;
		}
		c = this.getColor("colorAxis");
		if (c != null) {
			InitialProperties.colorAxis = c;
		}
		c = this.getColor("colorLabels");
		if (c != null) {
			InitialProperties.colorLabels = c;
		}

		c = this.getColor("color_title_font");
		if (c != null) {
			InitialProperties.color_title_font = c;
		}
		c = this.getColor("color_title_background");
		if (c != null) {
			InitialProperties.color_title_background = c;
		}

		f = this.getFont("fontMenuTitle");
		if (f != null) {
			InitialProperties.fontMenuTitle = f;
		}

		c = this.getColor("color_jtxt_font");
		if (c != null) {
			InitialProperties.color_jtxt_font = c;
		}
		c = this.getColor("color_jtxt_background");
		if (c != null) {
			InitialProperties.color_jtxt_background = c;
		}

		f = this.getFont("fontMenuTXT");
		if (f != null) {
			InitialProperties.fontMenuTXT = f;
		}

		c = this.getColor("color_label_font");
		if (c != null) {
			InitialProperties.color_label_font = c;
		}

		f = this.getFont("fontMenuLabel");
		if (f != null) {
			InitialProperties.fontMenuLabel = f;
		}

		c = this.getColor("color_chk_font");
		if (c != null) {
			InitialProperties.color_chk_font = c;
		}

		f = this.getFont("fontMenuCHK");
		if (f != null) {
			InitialProperties.fontMenuCHK = f;
		}

		c = this.getColor("color_opt_font");
		if (c != null) {
			InitialProperties.color_opt_font = c;
		}

		f = this.getFont("fontMenuOPT");
		if (f != null) {
			InitialProperties.fontMenuOPT = f;
		}

		c = this.getColor("color_cb_font");
		if (c != null) {
			InitialProperties.color_cb_font = c;
		}
		c = this.getColor("color_cb_background");
		if (c != null) {
			InitialProperties.color_cb_background = c;
		}

		f = this.getFont("fontMenuCB");
		if (f != null) {
			InitialProperties.fontMenuCB = f;
		}
		c = this.getColor("color_jarea_font");
		if (c != null) {
			InitialProperties.color_jarea_font = c;
		}
		c = this.getColor("color_jarea_background");
		if (c != null) {
			InitialProperties.color_jarea_background = c;
		}

		f = this.getFont("fontMenuAREA");
		if (f != null) {
			InitialProperties.fontMenuAREA = f;
		}
	}

	private Font getFont(final String label) {
		String lbl1, lbl2, lbl3;
		String s_size, s_name, s_style;
		int i_size, i_style;
		Font f = null;

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
			f = null;
			LogManager.LOG.warning("No font assigned to label " + label);
		}
		return f;
	}

	private Color getColor(final String label) {
		String lbl1, lbl2, lbl3;
		String s_r, s_g, s_b;
		int i_r, i_g, i_b;
		Color c = null;

		lbl1 = label + "_R";
		lbl2 = label + "_G";
		lbl3 = label + "_B";

		try {
			s_r = MainProperties.getProperty(lbl1);
			s_b = MainProperties.getProperty(lbl2);
			s_g = MainProperties.getProperty(lbl3);

			if ((s_r != null) && (s_g != null) && (s_b != null)) {
				i_r = Integer.parseInt(s_r);
				i_g = Integer.parseInt(s_g);
				i_b = Integer.parseInt(s_b);

				c = new Color(i_r, i_g, i_b);
			}
		} catch (final Exception e) {
			c = null;
			LogManager.LOG.warning("No color assigned to label " + label);
		}
		;
		return c;
	}

}