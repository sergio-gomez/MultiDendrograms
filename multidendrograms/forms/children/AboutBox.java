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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.imageio.ImageIO;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.initial.Main;
import multidendrograms.initial.InitialProperties;
import multidendrograms.utils.URLLabel;
import multidendrograms.utils.FontUtils;
import multidendrograms.definitions.Formats;

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

	private final String title = Language.getLabel(52);
	private final String logo = Main.LOGO_IMAGE;
	private final String appTitle = Main.PROGRAM;
	private final String affiliation = Main.AFFILIATION;
	private final String version = Main.VERSION;
	private final String jreVersion = Main.JRE_VERSION;
	private final String authors = Main.AUTHORS;
	private final String advisors = Main.ADVISORS;
	private final String homepage = Main.HOMEPAGE_URL;
	private final String manualURL = Main.MANUAL_URL;
	private final String licenseURL = Main.LICENSE_URL;

	private JButton closeButton;
	private JLabel imageLabel;
	private JLabel appTitleLabel;
	private JLabel urvLabel;
	private JLabel versionLabel;
	private JLabel appVersionLabel;
	private JLabel jreVersionLabel;
	private JLabel appJreVersionLabel;
	private JLabel authorsLabel;
	private JLabel appAuthorsLabel;
	private JLabel advisorsLabel;
	private JLabel appAdvisorsLabel;
	private JLabel homepageLabel;
	private URLLabel appHomepageLabel;
	private URLLabel manualLabel;
	private URLLabel licenseLabel;
	private final JLabel jLabel5 = new JLabel();

	public AboutBox(Frame parent) {
		super(parent);
		initComponents();
		getRootPane().setDefaultButton(closeButton);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window = getSize();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
	}

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(title);
		setModal(true);
		setName("aboutBox");
		setResizable(false);

		closeButton = Formats.getFormattedButton(Language.getLabel(60));
		closeButton.setName("closeButton");
		closeButton.addActionListener(this);

		imageLabel = new JLabel();
		imageLabel.setName("imageLabel");

		try {
  		BufferedImage imgOri = ImageIO.read(new File(logo));
			int w = InitialProperties.scaleSize(imgOri.getWidth());
			int h = InitialProperties.scaleSize(imgOri.getHeight());
			Image img = imgOri.getScaledInstance(w, h, Image.SCALE_SMOOTH);
			imageLabel.setIcon(new ImageIcon(img));
		} catch (final IOException e) {
			LogManager.LOG.throwing("AboutBox.java", "initComponents", e);
		} catch (Exception e) {
			LogManager.LOG.throwing("AboutBox.java", "initComponents", e);
		}

		appTitleLabel = Formats.getFormattedBoldLabel(appTitle);
		appTitleLabel.setFont(FontUtils.addStyleIncSize(appTitleLabel.getFont(), Font.BOLD, 4));
		appTitleLabel.setName("appTitleLabel");

		urvLabel = Formats.getFormattedLabel(affiliation);
		urvLabel.setName("urvLabel");

		versionLabel = Formats.getFormattedBoldLabel(Language.getLabel(118));
		versionLabel.setName("versionLabel");

		appVersionLabel = Formats.getFormattedLabel(version);
		appVersionLabel.setName("appVersionLabel");

		jreVersionLabel = Formats.getFormattedBoldLabel(Language.getLabel(135));
		jreVersionLabel.setName("jreVersionLabel");

		appJreVersionLabel = Formats.getFormattedLabel(jreVersion);
		appJreVersionLabel.setName("appJreVersionLabel");

		authorsLabel = Formats.getFormattedBoldLabel(Language.getLabel(119));
		authorsLabel.setName("authorsLabel");

		appAuthorsLabel = Formats.getFormattedLabel(authors);
		appAuthorsLabel.setName("appAuthorsLabel");

		advisorsLabel = Formats.getFormattedBoldLabel(Language.getLabel(120));
		advisorsLabel.setName("advisorsLabel");

		appAdvisorsLabel = Formats.getFormattedLabel(advisors);
		appAdvisorsLabel.setName("appAdvisorsLabel");

		homepageLabel = Formats.getFormattedBoldLabel(Language.getLabel(122));
		homepageLabel.setName("homepageLabel");

		appHomepageLabel = new URLLabel(homepage, homepage);
		appHomepageLabel.setName("appHomepageLabel");
		appHomepageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		manualLabel = new URLLabel(manualURL, Language.getLabel(124));
		manualLabel.setName("manualLabel");
		manualLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		licenseLabel = new URLLabel(licenseURL, Language.getLabel(121));
		licenseLabel.setName("licenseLabel");
		licenseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(imageLabel)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(36, 36, 36)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addGroup(
																						GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												GroupLayout.Alignment.LEADING)
																												.addComponent(appTitleLabel)
																												.addComponent(
																														urvLabel,
																														GroupLayout.DEFAULT_SIZE,
																														394,
																														Short.MAX_VALUE)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(jLabel5)
																																.addGap(28, 28, 28)
																																.addPreferredGap(
																																		LayoutStyle.ComponentPlacement.RELATED,
																																		GroupLayout.DEFAULT_SIZE,
																																		Short.MAX_VALUE)
																																.addGroup(
																																		layout.createParallelGroup(
																																				GroupLayout.Alignment.LEADING)
																																				.addComponent(versionLabel)
																																				.addComponent(jreVersionLabel)
																																				.addComponent(authorsLabel)
																																				.addComponent(advisorsLabel)
																																				.addComponent(homepageLabel)
																																				.addComponent(manualLabel))
																																.addGap(27, 27, 27)
																																.addGroup(
																																		layout.createParallelGroup(
																																				GroupLayout.Alignment.LEADING)
																																				.addComponent(appHomepageLabel)
																																				.addGroup(
																																						layout.createSequentialGroup()
																																								.addGroup(
																																										layout.createParallelGroup(
																																												GroupLayout.Alignment.LEADING)
																																												.addComponent(appAdvisorsLabel)
																																												.addComponent(appVersionLabel)
																																												.addComponent(appJreVersionLabel)
																																												.addComponent(appAuthorsLabel)
																																												.addComponent(licenseLabel))
																																								.addGap(40, 40, 40)))
																																.addGap(25, 25, 25))))))
												.addGroup(
														layout.createSequentialGroup()
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(closeButton)))
								.addContainerGap()));

		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, InitialProperties.scaleSize(237), Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(appTitleLabel)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(urvLabel))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(InitialProperties.scaleSize(63),
																				InitialProperties.scaleSize(63),
																				InitialProperties.scaleSize(63))
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(appVersionLabel)
																				.addComponent(versionLabel))
																.addGap(7, 7, 7)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(appJreVersionLabel)
																				.addComponent(jreVersionLabel))
																.addGap(7, 7, 7)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(appAuthorsLabel)
																				.addComponent(authorsLabel))
																.addGap(7, 7, 7)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(advisorsLabel)
																				.addComponent(appAdvisorsLabel))
																.addGap(7, 7, 7)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(homepageLabel)
																				.addComponent(appHomepageLabel))
																.addGap(InitialProperties.scaleSize(14),
																				InitialProperties.scaleSize(14),
																				InitialProperties.scaleSize(14))
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(manualLabel)
																				.addComponent(licenseLabel))))
								.addGap(InitialProperties.scaleSize(14),
												InitialProperties.scaleSize(14),
												InitialProperties.scaleSize(14))
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(closeButton)
												.addComponent(jLabel5))
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
