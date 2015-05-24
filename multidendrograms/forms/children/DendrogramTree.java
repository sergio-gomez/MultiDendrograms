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
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.methods.Method;
import multidendrograms.utils.MathUtils;
import multidendrograms.definitions.Cluster;
import multidendrograms.definitions.Config;

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
	private final Cluster branch;
	private final JTextArea txt;
	private final DefaultMutableTreeNode root;
	private final int precision;
	private String leafPrefix, leafPostfix;
	private String numPrefix, numPostfix;
	private String bandPrefix, bandPostfix;

	public DendrogramTree(final Cluster c, final Config cfg) throws Exception {
		super();

		this.branch = c;
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

		final int frmWidth = InitialProperties.getWidthDendroWindow();
		final int frmHeight = InitialProperties.getHeightDendroWindow();

		this.txt.setSize(frmWidth, frmHeight);
		setVisible(true);
		setTitle(cfg.getDataFile().getName() + " - " + Method.toName(cfg.getMethod()));
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	private void showBranch(final Cluster cluster, final DefaultMutableTreeNode branch) throws Exception {
		DefaultMutableTreeNode full;
		if (cluster.getNumSubclusters() == 1) {
			full = new DefaultMutableTreeNode("<html>" + leafPrefix + cluster.getName() + leafPostfix + "</html>");
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
			String spmin = MathUtils.format(pmin, precision);
			String spmax = MathUtils.format(pmax, precision);
			full = new DefaultMutableTreeNode(
					"<html>"
					+ numPrefix + cluster.getNumSubclusters() + numPostfix
					+ " &nbsp;&nbsp; "
					+ bandPrefix + " [" + spmin + ", " + spmax + "] " + bandPostfix
					+ " &nbsp;&nbsp; "
					+ numPrefix + " <i>" + cluster.getNumLeaves() + "</i> " + numPostfix
					+ "</html>");
		}
		branch.add(full);
		if (cluster.getNumSubclusters() > 1) {
			for (int n = 0; n < cluster.getNumSubclusters(); n ++) {
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
			leafPostfix = "</i></b>";
		} else if (f.isBold()) {
			leafPrefix = "<b color='" + colorToString(c) + "'>";
			leafPostfix = "</b>";
		} else if (f.isItalic()) {
			leafPrefix = "<i color='" + colorToString(c) + "'>";
			leafPostfix = "</i>";
		} else {
			leafPrefix = "<font color='" + colorToString(c) + "'>";
			leafPostfix = "</font>";
		}

		f = InitialProperties.getFontTreeNum();
		c = InitialProperties.getColorTreeNum();
		if (f.isBold() && f.isItalic()) {
			numPrefix = "<b color='" + colorToString(c) + "'><i>";
			numPostfix = "</i></b>";
		} else if (f.isBold()) {
			numPrefix = "<b color='" + colorToString(c) + "'>";
			numPostfix = "</b>";
		} else if (f.isItalic()) {
			numPrefix = "<i color='" + colorToString(c) + "'>";
			numPostfix = "</i>";
		} else {
			numPrefix = "<font color='" + colorToString(c) + "'>";
			numPostfix = "</font>";
		}

		f = InitialProperties.getFontTreeBand();
		c = InitialProperties.getColorTreeBand();
		if (f.isBold() && f.isItalic()) {
			bandPrefix = "<b color='" + colorToString(c) + "'><i>";
			bandPostfix = "</i></b>";
		} else if (f.isBold()) {
			bandPrefix = "<b color='" + colorToString(c) + "'>";
			bandPostfix = "</b>";
		} else if (f.isItalic()) {
			bandPrefix = "<i color='" + colorToString(c) + "'>";
			bandPostfix = "</i>";
		} else {
			bandPrefix = "<font color='" + colorToString(c) + "'>";
			bandPostfix = "</font>";
		}
	}

	private String colorToString(Color c) {
		return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}
}
