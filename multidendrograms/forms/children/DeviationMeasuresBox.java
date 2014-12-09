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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;

import multidendrograms.initial.Language;
import multidendrograms.methods.Method;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.definitions.Config;
import multidendrograms.dendrogram.UltrametricMatrix;


/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Deviation measures dialog
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DeviationMeasuresBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JLabel cccLabel;
	private JLabel cccValue;
	private JLabel nmaeLabel;
	private JLabel nmaeValue;
	private JLabel nmseLabel;
	private JLabel nmseValue;
	private JButton okButton;

	private final Font font1 = new Font("Arial", Font.BOLD | Font.ITALIC, 12);
	private final Font font2 = new Font("Arial", Font.PLAIN, 12);
	private final Font font3 = new Font("Arial", Font.BOLD, 12);

	private UltrametricMatrix ultraMatrix;
	private Config cfg;

	public DeviationMeasuresBox(PrincipalDesk principalDesk, UltrametricMatrix ultraMatrix, Config cfg) {
		super();
		this.ultraMatrix = ultraMatrix;
		this.cfg = cfg;
		initComponents();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(Language.getLabel(123));
		setModal(false);
		setName("DeviationMeasuresBox");
		setResizable(false);

		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		nf.setGroupingUsed(false);

		String errCC = nf.format(ultraMatrix.getCopheneticCorrelation());
		String errSE = nf.format(ultraMatrix.getSquaredError());
		String errAE = nf.format(ultraMatrix.getAbsoluteError());

		nameLabel = new JLabel();
		cccLabel = new JLabel();
		nmseLabel = new JLabel();
		nmaeLabel = new JLabel();
		cccValue = new JLabel();
		nmseValue = new JLabel();
		nmaeValue = new JLabel();
		okButton = new JButton();

		nameLabel.setFont(font1);
		nameLabel.setText(cfg.getDataFile().getName() + " - " + Method.toName(cfg.getMethod()));
		nameLabel.setName("nameLabel");

		cccLabel.setFont(font3);
		cccLabel.setText("Cophenetic Correlation Coefficient:");
		cccLabel.setName("cccLabel");

		nmseLabel.setFont(font3);
		nmseLabel.setText("Normalized Mean Squared Error:");
		nmseLabel.setName("nmseLabel");

		nmaeLabel.setFont(font3);
		nmaeLabel.setText("Normalized Mean Absolute Error:");
		nmaeLabel.setName("nmaeLabel");

		cccValue.setFont(font2);
		cccValue.setText(errCC);
		cccValue.setName("cccValue");

		nmseValue.setFont(font2);
		nmseValue.setText(errSE);
		nmseValue.setName("nmseValue");

		nmaeValue.setFont(font2);
		nmaeValue.setText(errAE);
		nmaeValue.setName("nmaeValue");

		okButton.setText(Language.getLabel(60));
		okButton.setName("okButton");
		okButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.LEADING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(nameLabel)
								.addContainerGap())
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING,
												false)
												.addComponent(cccLabel)
												.addComponent(nmseLabel)
												.addComponent(nmaeLabel))
								.addGap(20, 20, 20)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.TRAILING)
												.addComponent(cccValue)
												.addComponent(nmseValue)
												.addComponent(nmaeValue))
								.addContainerGap(
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addGroup(
						GroupLayout.Alignment.CENTER,
						layout.createSequentialGroup()
								.addComponent(okButton)));

		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(nameLabel)
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		cccValue)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmseValue)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmaeValue))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		cccLabel)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmseLabel)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmaeLabel)))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										20, Short.MAX_VALUE)
								.addComponent(okButton).addContainerGap()));

		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		dispose();
	}
}
