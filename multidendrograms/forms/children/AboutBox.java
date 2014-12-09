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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;

import multidendrograms.initial.Language;
import multidendrograms.initial.Main;
import multidendrograms.utils.URLLabel;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * About MultiDendrograms dialog
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class AboutBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final String title = "Info";
	private final String appTitle = Main.PROGRAM;
	private final String version = Main.VERSION;
	private final String authors = Main.AUTHORS;
	private final String advisors = Main.ADVISORS;
	private final String affiliation = Main.AFFILIATION;
	private final String homepage = Main.HOMEPAGE_URL;
	private final String licenseURL = Main.LICENSE_URL;
	private final String manualURL = Main.MANUAL_URL;
	private final String logo = Main.LOGO_IMAGE;

	private final JButton closeButton = new JButton();
	private final JLabel appTitleLabel = new JLabel();
	private final JLabel appVersionLabel = new JLabel();
	private final JLabel appAuthorsLabel = new JLabel();
	private final URLLabel appHomepageLabel = new URLLabel(homepage, homepage);
	private final JLabel urvLabel = new JLabel();
	private final JLabel imageLabel = new JLabel();
	private final JLabel advisorsLabel = new JLabel();
	private final JLabel versionLabel = new JLabel();
	private final JLabel authorsLabel = new JLabel();
	private final JLabel appAdvisorsLabel = new JLabel();
	private final JLabel jLabel5 = new JLabel();
	private final JLabel homepageLabel = new JLabel();
	private final URLLabel manualLabel = new URLLabel(manualURL,
			Language.getLabel(124));
	private final URLLabel licenseLabel = new URLLabel(licenseURL,
			Language.getLabel(121));

	private final Font font1 = new Font("Arial", Font.BOLD, 12);
	private final Font font2 = new Font("Arial", Font.PLAIN, 12);

	public AboutBox(Frame parent) {
		super(parent);
		initComponents();
		getRootPane().setDefaultButton(closeButton);

		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = getSize();
		setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
	}

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(title);
		setModal(true);
		setName("aboutBox");
		setResizable(false);

		closeButton.addActionListener(this);
		closeButton.setText(Language.getLabel(60));
		closeButton.setName("closeButton");

		appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(
				appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD,
				appTitleLabel.getFont().getSize() + 4));
		appTitleLabel.setText(appTitle);
		appTitleLabel.setName("appTitleLabel");

		appVersionLabel.setFont(font2);
		appVersionLabel.setText(version);
		appVersionLabel.setName("appVersionLabel");

		appAuthorsLabel.setFont(font2);
		appAuthorsLabel.setText(authors);
		appAuthorsLabel.setName("appAuthorsLabel");

		appAdvisorsLabel.setFont(font2);
		appAdvisorsLabel.setText(advisors);
		appAdvisorsLabel.setName("appAdvisorsLabel");

		appHomepageLabel.setFont(font2);
		appHomepageLabel.setName("appHomepageLabel");
		appHomepageLabel.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));

		urvLabel.setFont(font2);
		urvLabel.setText(affiliation);
		urvLabel.setName("urvLabel");

		imageLabel.setIcon(new ImageIcon(logo));
		imageLabel.setName("imageLabel");

		advisorsLabel.setFont(font1);
		advisorsLabel.setText(Language.getLabel(120));
		advisorsLabel.setName("advisorsLabel");

		versionLabel.setFont(font1);
		versionLabel.setText(Language.getLabel(118));
		versionLabel.setName("versionLabel");

		authorsLabel.setFont(font1);
		authorsLabel.setText(Language.getLabel(119));
		authorsLabel.setName("authorsLabel");

		homepageLabel.setFont(font1);
		homepageLabel.setText(Language.getLabel(122));
		homepageLabel.setName("homepageLabel");

		manualLabel.setName("manualLabel");
		manualLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		licenseLabel.setName("licenseLabel");
		licenseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(imageLabel)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(36, 36,
																		36)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addGroup(
																						GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addPreferredGap(
																										LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												GroupLayout.Alignment.LEADING)
																												.addComponent(
																														appTitleLabel)
																												.addComponent(
																														urvLabel,
																														GroupLayout.DEFAULT_SIZE,
																														394,
																														Short.MAX_VALUE)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(
																																		jLabel5)
																																.addPreferredGap(
																																		LayoutStyle.ComponentPlacement.RELATED,
																																		GroupLayout.DEFAULT_SIZE,
																																		Short.MAX_VALUE)
																																.addGroup(
																																		layout.createParallelGroup(
																																				GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						versionLabel)
																																				.addComponent(
																																						authorsLabel)
																																				.addComponent(
																																						advisorsLabel)
																																				.addComponent(
																																						homepageLabel))
																																.addGap(27,
																																		27,
																																		27)
																																.addGroup(
																																		layout.createParallelGroup(
																																				GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						appHomepageLabel)
																																				.addGroup(
																																						layout.createSequentialGroup()
																																								.addGroup(
																																										layout.createParallelGroup(
																																												GroupLayout.Alignment.LEADING)
																																												.addComponent(
																																														appAdvisorsLabel)
																																												.addComponent(
																																														appVersionLabel)
																																												.addComponent(
																																														appAuthorsLabel))
																																								.addGap(53,
																																										53,
																																										53)))
																																.addGap(86,
																																		86,
																																		86))))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(66,
																										66,
																										66)
																								.addComponent(
																										manualLabel)
																								.addGap(107,
																										107,
																										107)
																								.addComponent(
																										licenseLabel))))
												.addGroup(
														layout.createSequentialGroup()
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		closeButton)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(imageLabel,
						GroupLayout.PREFERRED_SIZE, 225,
						Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(
																		appTitleLabel)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		urvLabel))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(68, 68,
																		68)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						appVersionLabel)
																				.addComponent(
																						versionLabel))
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						appAuthorsLabel)
																				.addComponent(
																						authorsLabel))
																.addGap(8, 8, 8)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						advisorsLabel)
																				.addComponent(
																						appAdvisorsLabel))
																.addGap(7, 7, 7)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						homepageLabel)
																				.addComponent(
																						appHomepageLabel))))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(manualLabel)
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.TRAILING)
																.addComponent(
																		closeButton)
																.addGroup(
																		layout.createSequentialGroup()
																				.addComponent(
																						licenseLabel)
																				.addGap(37,
																						37,
																						37)
																				.addComponent(
																						jLabel5))))
								.addContainerGap(
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		dispose();
	}
}
