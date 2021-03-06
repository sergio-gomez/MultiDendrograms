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

package multidendrograms.definitions;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Component;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Locale;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import multidendrograms.initial.InitialProperties;
import multidendrograms.utils.FontUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Defines the formats for each component, which are loaded from the
 * configuration file or from the default parameters
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Formats {

	public static TitledBorder getFormattedTitledBorder(final String caption) {
		TitledBorder tb = BorderFactory.createTitledBorder(caption);
		tb.setTitleFont(InitialProperties.getFontTitle());
		return tb;
	}

	public static JCheckBox getFormattedCheckBox(final String caption) {
		final JCheckBox chk = new JCheckBox(caption);
		chk.setFont(InitialProperties.getFontCheckBox());
		chk.setForeground(InitialProperties.getColorCheckBox());
		scaleCheckBoxIcon(chk);
		return chk;
	}

	public static JComboBox<String> getFormattedComboBox(final String[] s) {
		final JComboBox<String> cb = new JComboBox<String>(s);
		cb.setFont(InitialProperties.getFontComboBox());
		cb.setForeground(InitialProperties.getColorComboBox());
		cb.setBackground(InitialProperties.getColorComboBoxBackground());
		return cb;
	}

	public static JLabel getFormattedShadedTitleLabel(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(InitialProperties.getFontTitle());
		lbl.setForeground(InitialProperties.getColorTitle());
		lbl.setBackground(InitialProperties.getColorTitleBackground());
		lbl.setOpaque(true);
		return lbl;
	}

	public static JLabel getFormattedTitleLabel(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(InitialProperties.getFontTitle());
		lbl.setForeground(InitialProperties.getColorTitle());
		lbl.setOpaque(true);
		return lbl;
	}

	public static JLabel getFormattedTitleItalicsLabel(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(FontUtils.addStyle(InitialProperties.getFontTitle(), Font.ITALIC));
		lbl.setForeground(InitialProperties.getColorTitle());
		lbl.setOpaque(true);
		return lbl;
	}

	public static JMenuItem getFormattedMenuItem(final String caption) {
		final JMenuItem mi = new JMenuItem(caption);
		mi.setFont(InitialProperties.getFontMenuItem());
		mi.setForeground(InitialProperties.getColorMenuItem());
		mi.setOpaque(false);
		return mi;
	}

	public static JButton getFormattedButton(final String caption) {
		final JButton btn = new JButton(caption);
		btn.setFont(InitialProperties.getFontButton());
		btn.setForeground(InitialProperties.getColorButton());
		btn.setOpaque(false);
		return btn;
	}

	public static JButton getFormattedBoldButton(final String caption) {
		final JButton btn = new JButton(caption);
		btn.setFont(FontUtils.addStyle(InitialProperties.getFontButton(), Font.BOLD));
		btn.setForeground(InitialProperties.getColorButton());
		btn.setOpaque(false);
		return btn;
	}

	public static JLabel getFormattedLabel(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(InitialProperties.getFontLabel());
		lbl.setForeground(InitialProperties.getColorLabel());
		lbl.setOpaque(false);
		return lbl;
	}

	public static JLabel getFormattedBoldLabel(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(FontUtils.addStyle(InitialProperties.getFontLabel(), Font.BOLD));
		lbl.setForeground(InitialProperties.getColorLabel());
		lbl.setOpaque(false);
		return lbl;
	}

	public static JRadioButton getFormattedRadioButton(final String caption, final boolean selec) {
		final JRadioButton opt = new JRadioButton(caption, selec);
		opt.setFont(InitialProperties.getFontRadioButton());
		opt.setForeground(InitialProperties.getColorRadioButton());
		scaleRadioButtonIcon(opt);
		return opt;
	}

	public static JTextField getFormattedTextField(final String caption, final int size, Locale loc) {
		final JTextField txt = new JTextField(caption, size);
		txt.setFont(InitialProperties.getFontTextField());
		txt.setForeground(InitialProperties.getColorTextField());
		txt.setBackground(InitialProperties.getColorTextFieldBackground());
		txt.setLocale(loc);
		return txt;
	}

	public static JTextField getFormattedTextField() {
		final JTextField txt = new JTextField();
		txt.setFont(InitialProperties.getFontTextField());
		txt.setForeground(InitialProperties.getColorTextField());
		return txt;
	}

	public static Icon scaleIcon(Icon ic) {
  	return scaleIcon(ic, null);
	}

	public static Icon scaleIcon(Icon ic, Component comp) {
		if (InitialProperties.hasScaling()) {
			BufferedImage img = new BufferedImage(ic.getIconWidth(), ic.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = img.createGraphics();
			try {
				if (comp == null) {
					ic.paintIcon(new JLabel(), graphics, 0, 0);
				} else {
  				ic.paintIcon(comp, graphics, 0, 0);
				}
			} finally {
				graphics.dispose();
			}
			ImageIcon newImg = new ImageIcon(img);
			int width = InitialProperties.scaleSize(img.getWidth());
			int height = InitialProperties.scaleSize(img.getHeight());
			return new ImageIcon(newImg.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		} else {
  		return ic;
		}
	}

  public static void scaleCheckBoxIcon(JCheckBox checkbox) {
		if (InitialProperties.hasScaling()) {
			boolean previousState = checkbox.isSelected();

			checkbox.setSelected(false);
			Icon boxIcon = UIManager.getIcon("CheckBox.icon");
			checkbox.setIcon(scaleIcon(boxIcon, checkbox));

			checkbox.setSelected(true);
			Icon checkedBoxIcon = UIManager.getIcon("CheckBox.icon");
			checkbox.setSelectedIcon(scaleIcon(checkedBoxIcon, checkbox));

			checkbox.setSelected(previousState);
		}
  }

  public static void scaleRadioButtonIcon(JRadioButton radioButton) {
		if (InitialProperties.hasScaling()) {
			boolean previousState = radioButton.isSelected();

			radioButton.setSelected(false);
			Icon radioIcon = UIManager.getIcon("RadioButton.icon");
			radioButton.setIcon(scaleIcon(radioIcon, radioButton));

			radioButton.setSelected(true);
			Icon selectedRadioIcon = UIManager.getIcon("RadioButton.icon");
			radioButton.setSelectedIcon(scaleIcon(selectedRadioIcon, radioButton));

			radioButton.setSelected(previousState);
		}
  }

}
