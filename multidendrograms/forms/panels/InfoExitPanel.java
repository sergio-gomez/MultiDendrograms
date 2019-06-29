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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.children.AboutBox;
import multidendrograms.definitions.Formats;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * About and Exit buttons
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class InfoExitPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton btnInfo, btnExit;
	private final PrincipalDesk fr;
	private String sInfo, sExit;

	private void loadLanguage() {
		sInfo = Language.getLabel(52); // Info
		sExit = Language.getLabel(46); // exit
	}

	public InfoExitPanel(final PrincipalDesk fr) {
		super();
		this.fr = fr;
		this.loadLanguage();
		this.getPanel();
		this.setVisible(true);
	}

	private void getPanel() {

		// Info
		btnInfo = Formats.getFormattedBoldButton(sInfo); // Info
		btnInfo.addActionListener(this);

		// Exit
		btnExit = Formats.getFormattedBoldButton(sExit); // Exit
		btnExit.addActionListener(this);

		// Width
		int width = InitialProperties.scaleSize(100);

		// layout
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
						.addGap(5, 5, 5)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnInfo, width, width, 2 * width)
						.addGap(3, 3, 3)
						.addComponent(btnExit, width, width, 2 * width)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(5, 5, 5));

		layout.linkSize(SwingConstants.HORIZONTAL, btnInfo, btnExit);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGap(8, 8, 8)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addComponent(btnInfo)
										.addComponent(btnExit))
						.addGap(8, 8, 8));
}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(sExit)) { // exit
			fr.toGoOut();
		}
		else if (e.getActionCommand().equals(sInfo)) { // info
			AboutBox a = new AboutBox(fr);
			a.setVisible(true);
		}
	}
}
