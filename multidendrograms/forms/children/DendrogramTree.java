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
	private final int prec;
	private final Config cfg;

	public DendrogramTree(final Cluster c, final Config cfg)
			throws Exception {
		super();

		this.branch = c;
		this.prec = cfg.getPrecision();
		this.cfg = cfg;

		this.root = new DefaultMutableTreeNode(Language.getLabel(62));
		final JTree tree = new JTree(this.root);
		this.txt = new JTextArea();
		final JScrollPane p = new JScrollPane(tree,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		try {
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

		final int frmWidth = InitialProperties.getWidth_frmDesk();
		final int frmHeight = InitialProperties.getHeight_frmDesk();

		this.txt.setSize(frmWidth, frmHeight);
		setVisible(true);
		setTitle(cfg.getDataFile().getName() + " - "
				+ Method.toName(cfg.getMethod()));
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	private void showBranch(final Cluster c, final DefaultMutableTreeNode branch)
			throws Exception {
		double pmin, pmax, tmp;
		String spmin, spmax;

		DefaultMutableTreeNode full;
		if (c.getNumSubclusters() == 1) {
			full = new DefaultMutableTreeNode("<html><b COLOR='#888888'>"
					+ c.getName() + "</b></html>");
		} else {
			pmin = c.getHeight();
			if (cfg.isDistance()) {
				pmax = pmin + c.getAgglomeration();
			} else {
				pmax = pmin - c.getAgglomeration();
			}
			if (pmin > pmax) {
				tmp = pmin;
				pmin = pmax;
				pmax = tmp;
			}
			pmin = MathUtils.round(pmin, prec);
			pmax = MathUtils.round(pmax, prec);
			spmin = MathUtils.format(pmin, prec);
			spmax = MathUtils.format(pmax, prec);
			full = new DefaultMutableTreeNode("<html>" + c.getNumSubclusters()
					+ " &nbsp;&nbsp;<b COLOR='#000000'>[" + spmin + ", " + spmax + "]</b>&nbsp;&nbsp; "
					+ "<i>" + c.getNumLeaves() + "</i></html>");
		}
		branch.add(full);
		if (c.getNumSubclusters() > 1) {
			for (int n = 0; n < c.getNumSubclusters(); n ++) {
				showBranch(c.getSubcluster(n), full);
			}
		}
	}
}
