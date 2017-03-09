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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;

import multidendrograms.definitions.Config;
import multidendrograms.definitions.Formats;
import multidendrograms.dendrogram.DendrogramMeasures;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.initial.Language;
import multidendrograms.initial.MethodName;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dialog to show dendrogram measures
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DendrogramMeasuresBox extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	public DendrogramMeasuresBox(UltrametricMatrix ultrametricMatrix, Config cfg) {
		super();
		initComponents(ultrametricMatrix, cfg);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
					(screenSize.height - windowSize.height) / 2);
	}

	private void initComponents(UltrametricMatrix ultrametricMatrix, Config cfg) {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(Language.getLabel(123));
		setModal(false);
		setName("DendrogramCharacteristicsBox");
		setResizable(false);
		
		JLabel nameLabel = Formats.getFormattedTitleItalicsLabel(cfg.getDataFile().getName() 
				+ " - " + MethodName.toName(cfg.getMethod()));
		JLabel blankLabel = Formats.getFormattedLabel("");
		
		JLabel tbLabel = Formats.getFormattedBoldLabel(DendrogramMeasures.TREE_BALANCE_LABEL);
		JLabel ccLabel = Formats.getFormattedBoldLabel(DendrogramMeasures.COPHENETIC_CORRELATION_LABEL);
		JLabel seLabel = Formats.getFormattedBoldLabel(DendrogramMeasures.SQUARED_ERROR_LABEL);
		JLabel aeLabel = Formats.getFormattedBoldLabel(DendrogramMeasures.ABSOLUTE_ERROR_LABEL);
		JLabel sdLabel = Formats.getFormattedBoldLabel(DendrogramMeasures.SPACE_DISTORTION_LABEL);
		
		DendrogramMeasures dendroMeasures = 
				new DendrogramMeasures(cfg.getExternalData().getProximityMatrix(), cfg.getRoot(), 
						ultrametricMatrix.getMatrix());
		JLabel tbValue = Formats.getFormattedLabel(dendroMeasures.getTreeBalance());
		JLabel ccValue = Formats.getFormattedLabel(dendroMeasures.getCopheneticCorrelation());
		JLabel seValue = Formats.getFormattedLabel(dendroMeasures.getSquaredError());
		JLabel aeValue = Formats.getFormattedLabel(dendroMeasures.getAbsoluteError());
		JLabel sdValue = Formats.getFormattedLabel(dendroMeasures.getSpaceDistortion());
		
		JButton okButton = Formats.getFormattedButton(Language.getLabel(60));
		okButton.addActionListener(this);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.LEADING, 
					layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(nameLabel)
						.addContainerGap())
				.addGroup(
					layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(tbLabel)
								.addComponent(blankLabel)
								.addComponent(ccLabel)
								.addComponent(seLabel)
								.addComponent(aeLabel)
								.addComponent(sdLabel))
						.addGap(20, 20, 20)
						.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(tbValue)
								.addComponent(blankLabel)
								.addComponent(ccValue)
								.addComponent(seValue)
								.addComponent(aeValue)
								.addComponent(sdValue))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER,
					layout.createSequentialGroup()
						.addComponent(okButton)));
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(nameLabel)
					.addGap(18, 18, 18)
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(tbValue)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(blankLabel)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(ccValue)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(seValue)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(aeValue)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(sdValue))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(tbLabel)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(blankLabel)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(ccLabel)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(seLabel)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(aeLabel)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(sdLabel)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
					.addComponent(okButton).addContainerGap()));
		
		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		dispose();
	}

}
