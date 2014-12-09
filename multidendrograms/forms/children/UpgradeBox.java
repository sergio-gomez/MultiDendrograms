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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
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
 * Upgrade dialog
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class UpgradeBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private String version, versionWeb;
	private JLabel verWebLabel;
	private URLLabel homeLabel;
	private JLabel verLabel;
	private JButton okButton;

	private final Font font = new Font("Arial", Font.PLAIN, 12);

	public UpgradeBox(final String version, final String versionWeb) {
		super();
		this.version = version;
		this.versionWeb = versionWeb;
		initComponents();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(Main.PROGRAM);
		setModal(false);
		setName(Main.PROGRAM);
		setResizable(false);
		setModal(true);

		verWebLabel = new JLabel();
		homeLabel = new URLLabel(Main.HOMEPAGE_URL, Language.getLabel(122).toLowerCase());
		verLabel = new JLabel();
		okButton = new JButton();

		verWebLabel.setFont(font);
		verWebLabel.setText(Language.getLabel(129) + " " + Main.PROGRAM + " " + versionWeb
				+ " " + Language.getLabel(130) + " ");
		verWebLabel.setName("verWebLabel");

		homeLabel.setFont(font);
		homeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		verLabel.setFont(font);
		verLabel.setText(Language.getLabel(131) + " " + Main.PROGRAM + " " + version);
		verLabel.setName("verLabel");

		okButton.setText(Language.getLabel(60));
		okButton.setName("okButton");
		okButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(verWebLabel)
										.addComponent(homeLabel)
										.addContainerGap())
						.addGroup(
								layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(verLabel)
										.addContainerGap())
						.addGroup(
								GroupLayout.Alignment.CENTER,
								layout.createSequentialGroup()
										.addComponent(okButton)));

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(homeLabel)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(verWebLabel)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
						.addComponent(verLabel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
						.addComponent(okButton).addContainerGap());

		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		dispose();
	}
}
