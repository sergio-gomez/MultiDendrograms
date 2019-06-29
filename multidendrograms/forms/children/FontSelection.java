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

package multidendrograms.forms.children;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.definitions.Formats;
import multidendrograms.utils.FontUtils;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Font selection dialog
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FontSelection extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Font font;
	private final Font initFont;
	private JComboBox<String> cbSizes, cbFonts;
	private JCheckBox chkBold, chkItalic;
	private final JButton btnAccept, btnCancel;
	private JLabel lblSelectedFont;

	private void initialize() {
		final String[] sizes = { "6", "7", "8", "9", "10", "11", "12", "14", "16", "18", "20"};
		cbSizes = new JComboBox<String>(sizes);
		cbSizes.setEditable(true);
		cbSizes.setSelectedItem(String.valueOf(font.getSize()));
		cbSizes.setFont(FontUtils.addStyle(InitialProperties.getFontLabel(), Font.BOLD));

		final String[] fonts = FontSelection.getSystemFonts();
		cbFonts = new JComboBox<String>(fonts);
		cbFonts.setSelectedItem(font.getName());
		cbFonts.setFont(FontUtils.addStyle(InitialProperties.getFontLabel(), Font.BOLD));

		chkBold = Formats.getFormattedCheckBox(Language.getLabel(54)); // Bold
		chkBold.setSelected(font.isBold());

		chkItalic = Formats.getFormattedCheckBox(Language.getLabel(55)); // Italics
		chkItalic.setSelected(font.isItalic());

		lblSelectedFont = new JLabel(Language.getLabel(57)); // Selected font
		lblSelectedFont.setAlignmentX(SwingConstants.CENTER);
		lblSelectedFont.setFont(font);
	}

	private static String[] getSystemFonts() {
		final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return env.getAvailableFontFamilyNames();
	}

	public FontSelection(final Font fnt) {
		super();
		this.setTitle(Language.getLabel(58));
		this.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		this.font = fnt;
		this.initFont = fnt;

		this.initialize();

		final Container container = this.getContentPane();
		this.setLayout(new GridBagLayout());
		((JPanel) container).setBorder(Formats.getFormattedTitledBorder(Language.getLabel(59)));

		final GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1.0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		this.add(lblSelectedFont, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		cbFonts.addActionListener(this);
		container.add(cbFonts, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		c.gridy = 2;
		cbSizes.addActionListener(this);
		container.add(cbSizes, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		chkBold.addActionListener(this);
		container.add(chkBold, c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		chkItalic.addActionListener(this);
		container.add(chkItalic, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.RELATIVE;

		btnAccept = Formats.getFormattedBoldButton(Language.getLabel(60)); // ACCEPT
		btnAccept.addActionListener(this);
		container.add(btnAccept, c);

		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 1.0;
		c.weighty = 0;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;

		btnCancel = Formats.getFormattedBoldButton(Language.getLabel(61)); // CANCEL
		btnCancel.addActionListener(this);
		container.add(btnCancel, c);

		this.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(Language.getLabel(60))) {
			this.font = this.giveFont();
			this.dispose();
		} else if (e.getActionCommand().equals(Language.getLabel(61))) {
			this.font = initFont;
			this.dispose();
		} else {
			this.font = this.giveFont();
			this.lblSelectedFont.setFont(font);

		}
	}

	public Font getNewFont() {
		return font;
	}

	private Font giveFont() {
		int style = 0;
		final int size = Integer.valueOf((String) cbSizes.getSelectedItem());

		if (chkItalic.isSelected()) {
			style = Font.ITALIC;
		}
		if (chkBold.isSelected()) {
			style += Font.BOLD;
		}
		if (style == 0) {
			style = Font.PLAIN;
		}

		final String nomFont = (String) cbFonts.getSelectedItem();

		return new Font(nomFont, style, size);

	}
}
