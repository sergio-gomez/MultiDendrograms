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
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.Icon;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import multidendrograms.initial.Language;
import multidendrograms.initial.MethodName;
import multidendrograms.utils.NumberUtils;
import multidendrograms.initial.InitialProperties;
import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.core.utils.MathUtils;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Formats;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dendrogram navigation window
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DendrogramTree extends JDialog {

	private static final long serialVersionUID = 1L;
	private final Dendrogram branch;
	private final JTextArea txt;
	private final DefaultMutableTreeNode root;
	private final int precision;
	private String leafPrefix, leafSufix;
	private String numPrefix, numSufix;
	private String bandPrefix, bandSufix;

	public DendrogramTree(Config cfg) throws Exception {
		super();

		this.branch = cfg.getRoot();
		this.precision = cfg.getPrecision();

		this.root = new DefaultMutableTreeNode(Language.getLabel(62));
		final JTree tree = new JTree(this.root);
		tree.setFont(InitialProperties.getFontLabel());

		this.txt = new JTextArea();
		final JScrollPane p = new JScrollPane(tree,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		try {
			calculateHTMLTags();
			showBranch(this.branch, this.root);

			tree.setExpandsSelectedPaths(true);
			this.txt.setEditable(false);
			this.add(p);
			tree.expandPath(new TreePath(this.root));

			int x = 1;
			int rows = tree.getRowCount();
			while ((rows - 1) >= x) {
				tree.expandPath(tree.getPathForRow(x));
				rows = tree.getRowCount();
				x++;
			}

		} catch (Exception e) {
			String errMsg = e.getMessage() + "\n";
			errMsg += Language.getLabel(78);
			throw new Exception(errMsg);
		}

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		Icon ic;
		ic = renderer.getLeafIcon();
		renderer.setLeafIcon(Formats.scaleIcon(ic));
		ic = renderer.getOpenIcon();
		renderer.setOpenIcon(Formats.scaleIcon(ic));
		ic = renderer.getClosedIcon();
		renderer.setClosedIcon(Formats.scaleIcon(ic));
		tree.setCellRenderer(renderer);

		final int frmWidth = InitialProperties.getWidthDendroWindow();
		final int frmHeight = InitialProperties.getHeightDendroWindow();

		this.txt.setSize(frmWidth, frmHeight);
		setVisible(true);
		setTitle(cfg.getDataFile().getName() + " - " + MethodName.toName(cfg.getMethod()));
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	private void showBranch(final Dendrogram cluster, final DefaultMutableTreeNode branch) throws Exception {
		DefaultMutableTreeNode full;
		if (cluster.numberOfSubclusters() == 1) {
			full = new DefaultMutableTreeNode("<html>" + leafPrefix + cluster.getLabel() + leafSufix + "</html>");
		} else {
			double pmin = cluster.getRootBottomHeight();
			double pmax = cluster.getRootTopHeight();
			if (pmin > pmax) {
				double tmp = pmin;
				pmin = pmax;
				pmax = tmp;
			}
			pmin = MathUtils.round(pmin, precision);
			pmax = MathUtils.round(pmax, precision);
			String spmin = NumberUtils.format(pmin, precision);
			String spmax = NumberUtils.format(pmax, precision);
			full = new DefaultMutableTreeNode(
					"<html>"
					+ numPrefix + cluster.numberOfSubclusters() + numSufix
					+ " &nbsp;&nbsp; "
					+ bandPrefix + " [" + spmin + ", " + spmax + "] " + bandSufix
					+ " &nbsp;&nbsp; "
					+ numPrefix + " <i>" + cluster.numberOfLeaves() + "</i> " + numSufix
					+ "</html>");
		}
		branch.add(full);
		if (cluster.numberOfSubclusters() > 1) {
			for (int n = 0; n < cluster.numberOfSubclusters(); n ++) {
				showBranch(cluster.getSubcluster(n), full);
			}
		}
	}

	private void calculateHTMLTags() {
		Color c;
		Font f;

		f = InitialProperties.getFontTreeLeaf();
		c = InitialProperties.getColorTreeLeaf();
		if (f.isBold() && f.isItalic()) {
			leafPrefix = "<b color='" + colorToString(c) + "'><i>";
			leafSufix = "</i></b>";
		} else if (f.isBold()) {
			leafPrefix = "<b color='" + colorToString(c) + "'>";
			leafSufix = "</b>";
		} else if (f.isItalic()) {
			leafPrefix = "<i color='" + colorToString(c) + "'>";
			leafSufix = "</i>";
		} else {
			leafPrefix = "<font color='" + colorToString(c) + "'>";
			leafSufix = "</font>";
		}

		f = InitialProperties.getFontTreeNum();
		c = InitialProperties.getColorTreeNum();
		if (f.isBold() && f.isItalic()) {
			numPrefix = "<b color='" + colorToString(c) + "'><i>";
			numSufix = "</i></b>";
		} else if (f.isBold()) {
			numPrefix = "<b color='" + colorToString(c) + "'>";
			numSufix = "</b>";
		} else if (f.isItalic()) {
			numPrefix = "<i color='" + colorToString(c) + "'>";
			numSufix = "</i>";
		} else {
			numPrefix = "<font color='" + colorToString(c) + "'>";
			numSufix = "</font>";
		}

		f = InitialProperties.getFontTreeBand();
		c = InitialProperties.getColorTreeBand();
		if (f.isBold() && f.isItalic()) {
			bandPrefix = "<b color='" + colorToString(c) + "'><i>";
			bandSufix = "</i></b>";
		} else if (f.isBold()) {
			bandPrefix = "<b color='" + colorToString(c) + "'>";
			bandSufix = "</b>";
		} else if (f.isItalic()) {
			bandPrefix = "<i color='" + colorToString(c) + "'>";
			bandSufix = "</i>";
		} else {
			bandPrefix = "<font color='" + colorToString(c) + "'>";
			bandSufix = "</font>";
		}
	}

	private String colorToString(Color c) {
		return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}
}