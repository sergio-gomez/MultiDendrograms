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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.Language;
import multidendrograms.initial.InitialProperties;
import multidendrograms.dendrogram.Clipping;
import multidendrograms.dendrogram.ScalingBox;
import multidendrograms.dendrogram.figures.Axis;
import multidendrograms.dendrogram.figures.AxisLabel;
import multidendrograms.dendrogram.figures.Band;
import multidendrograms.dendrogram.figures.Line;
import multidendrograms.dendrogram.figures.Node;
import multidendrograms.dendrogram.figures.NodeLabel;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.XYBox;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.utils.TextBoxSize;
import multidendrograms.definitions.BoxContainer;
import multidendrograms.definitions.Config;
import multidendrograms.definitions.Dimensions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dendrogram frame
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DendrogramPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ActionListener al;
	private JPopupMenu menu;

	private ScalingBox scaledDendrogram = null;
	private ScalingBox scaledBullets = null;
	private ScalingBox scaledAxis = null;
	private ScalingBox scaledNames = null;
	private ScalingBox scaledAxisLabels = null;

	// axis labels
	double axisLabelWidth = 0.0;
	double axisLabelHeight = 0.0;

	// node labels
	double nameWidth = 0.0;
	double nameHeight = 0.0;

	// node bullet size
	double bulletsWidth = 0.0;
	double bulletsHeight = 0.0;

	// axis
	double axisWidth = 0.0;
	double axisHeight = 0.0;

	// dendrogram size
	double dendroWidth = 0.0;
	double dendroHeight = 0.0;

	// clipping of dendrogram
	private double axisMaxVal;
	private double axisMinVal;

	private Config cfg = null;

	private int numClusters;
	private double radius;

	private DendrogramOrientation dendroOrientation = DendrogramOrientation.NORTH;
	private LabelOrientation nodeNameOrientation = LabelOrientation.HORIZONTAL;
	private String strMax = "";

	private LinkedList<Node> nodesList;
	private LinkedList<Line> linesList;
	private LinkedList<Band> bandsList;

	protected DendrogramPanel dendroPanel;
	private final PrincipalDesk frm; // main window

	public DendrogramPanel(final PrincipalDesk frm) {
		super();
		this.frm = frm;
		this.initComponentsMenu();
		this.dendroPanel = this;
	}

	private void initComponentsMenu() {
		al = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				String errMsg;

				if (evt.getActionCommand().equals(Language.getLabel(96))) {
					// SAVE TO JPG
					try {
						final BufferedImage buff = DendrogramPanel.this.draw();
						frm.savePicture(buff, "jpg", cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(97))) {
					// SAVE TO PNG
					try {
						final BufferedImage buff = DendrogramPanel.this.draw();
						frm.savePicture(buff, "png", cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(95))) {
					// VIEW TREE
					try {
						new DendrogramTree(cfg.getDistancesMatrix().getRoot(), cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						errMsg = Language.getLabel(76);
						JOptionPane.showMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(98))) {
					// SAVE TO TXT
					try {
						frm.saveTXT(cfg.getDistancesMatrix().getRoot(), cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(87))) {
					// SAVE TO NEWICK
					try {
						frm.saveNewick(cfg.getDistancesMatrix().getRoot(), cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(99))) {
					// SAVE TO EPS
					try {
						frm.savePostScript(dendroPanel, cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(116))) {
					// SAVE ULTRAMETRIC AS TXT
					try {
						frm.saveUltrametricTXT(cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(117))) {
					// SHOW ULTRAMETRIC ERRORS
					try {
						frm.showUltrametricErrors(cfg);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};

		menu = new JPopupMenu();
		final JMenuItem me0 = new JMenuItem();
		final JMenuItem me1 = new JMenuItem();
		final JMenuItem me2 = new JMenuItem();
		final JMenuItem me3 = new JMenuItem();
		final JMenuItem me4 = new JMenuItem();
		final JMenuItem me5 = new JMenuItem();
		final JMenuItem me6 = new JMenuItem();
		final JMenuItem me7 = new JMenuItem();

		me0.setText(Language.getLabel(87)); // save newick
		me1.setText(Language.getLabel(95)); // show dendrogram details
		me2.setText(Language.getLabel(98)); // save txt
		me3.setText(Language.getLabel(96)); // save jpg
		me4.setText(Language.getLabel(97)); // save png
		me5.setText(Language.getLabel(99)); // save eps
		me6.setText(Language.getLabel(116)); // save ultrametric as txt
		me7.setText(Language.getLabel(117)); // show ultrametric details

		me0.addActionListener(al);
		me1.addActionListener(al);
		me2.addActionListener(al);
		me3.addActionListener(al);
		me4.addActionListener(al);
		me5.addActionListener(al);
		me6.addActionListener(al);
		me7.addActionListener(al);

		menu.add(me7);
		menu.add(me1);
		menu.addSeparator();
		menu.add(me6);
		menu.add(me2);
		menu.add(me0);
		menu.addSeparator();
		menu.add(me3);
		menu.add(me4);
		menu.add(me5);

		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	@Override
	protected void processMouseEvent(final MouseEvent evt) {
		if (evt.isPopupTrigger())
			menu.show(evt.getComponent(), evt.getX(), evt.getY());
		else
			super.processMouseEvent(evt);
	}

	public void setConfig(final Config cfg) {
		this.cfg = cfg;

		radius = cfg.getRadius();
		numClusters = cfg.getDistancesMatrix().getRoot().getNumLeaves();
		dendroOrientation = cfg.getDendrogramOrientation();
		nodeNameOrientation = cfg.getNodeNameOrientation();

		axisMaxVal = cfg.getAxisMaxVal();
		axisMinVal = cfg.getAxisMinVal();
	}

	public LinkedList<Node> getNodesList() {
		return nodesList;
	}

	public LinkedList<Line> getLinesList() {
		return linesList;
	}

	public LinkedList<Band> getBandsList() {
		return bandsList;
	}

	public void setNodesList(final LinkedList<Node> lst) {
		this.nodesList = lst;
		LogManager.LOG.finest("Number of nodes: (" + lst.size() + ")");
	}

	public void setLinesList(final LinkedList<Line> lst) {
		this.linesList = lst;
		LogManager.LOG.finest("Number of nodes: (" + lst.size() + ")");
	}

	public void setBandsList(final LinkedList<Band> lst) {
		this.bandsList = lst;
		LogManager.LOG.finest("Number of nodes: (" + lst.size() + ")");
	}

	@Override
	public void update(final Graphics arg0) {
		super.update(arg0);
	}

	private double boxClustersWidth() {
		return ((2 * radius * numClusters) + ((numClusters - 1) * radius));
	}

	private void translateScreen(final BoxContainer b, final double height) {
		double h;
		h = height - b.getCornerY();
		b.setCornerY(-h);
	}

	private void setWidths(final Graphics2D g) {
		final DendrogramOrientation or = cfg.getDendrogramOrientation();

		/* dendrogram */
		if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
			dendroWidth = this.boxClustersWidth();
			dendroHeight = axisMaxVal - axisMinVal;
		} else {
			dendroWidth = axisMaxVal - axisMinVal;
			dendroHeight = this.boxClustersWidth();
		}

		/* axis */
		if (cfg.getConfigMenu().isAxisVisible()) {
			/* size of the axis */
			if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
				axisWidth = 2 * radius; // east and west
				axisHeight = axisMaxVal - axisMinVal;
			} else {
				axisHeight = 2 * radius; // north and south
				axisWidth = axisMaxVal - axisMinVal;
			}
		} else {
			axisWidth = 0;
			axisHeight = 0;
		}

		/* show the nodesList */
		double rr = cfg.getConfigMenu().getNodeRadius();
		if ((rr = cfg.getConfigMenu().getNodeRadius()) > 0) {
			if (DendrogramOrientation.NORTH.equals(or) || DendrogramOrientation.SOUTH.equals(or)) {
				bulletsWidth = this.boxClustersWidth();
				bulletsHeight = 2 * rr;
			} else {
				bulletsWidth = 2 * rr;
				bulletsHeight = this.boxClustersWidth();
			}
		} else {
			bulletsWidth = 0;
			bulletsHeight = 0;
		}

		/* show the labels of the axis */
		if (cfg.getConfigMenu().isAxisLabelVisible()) {
			final TextBoxSize bf = new TextBoxSize(cfg.getConfigMenu().getAxisLabelFont());
			String txt;
			int ent;
			Dimensions<Double> dim;
			ent = (int) Math.round(axisMaxVal);
			txt = Integer.toString(ent);
			if (DendrogramOrientation.EAST.equals(or) || DendrogramOrientation.WEST.equals(or)) {
				if (cfg.isDistance()) {
					dim = bf.getBoxPositiveNumber(90, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
				} else {
					dim = bf.getBoxNegativeNumber(90, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
				}
			} else {
				if (cfg.isDistance()) {
					dim = bf.getBoxPositiveNumber(0, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
				} else {
					dim = bf.getBoxNegativeNumber(0, (txt.trim()).length(),
							cfg.getAxisLabelDecimals());
				}
			}
			axisLabelWidth = dim.getWidth();
			axisLabelHeight = dim.getHeight();
		} else {
			axisLabelWidth = 0;
			axisLabelHeight = 0;
		}

		/* names of the nodes */
		if (cfg.getConfigMenu().isNodeNameVisible()) {
			int alf;
			final TextBoxSize bf = new TextBoxSize(cfg.getConfigMenu().getNodeNameFont());
			String tmp;
			Dimensions<Double> dim, dim_tmp;

			/* size of labels of nodes */
			if (cfg.getNodeNameOrientation().equals(LabelOrientation.HORIZONTAL))
				alf = 0;
			else if (cfg.getNodeNameOrientation().equals(LabelOrientation.OBLIQUE))
				alf = 45;
			else
				alf = -90;
			dim = new Dimensions<Double>(0.0, 0.0);
			if (strMax.equals("")) {
				final Enumeration<String> el = cfg.getNames().elements();
				while (el.hasMoreElements()) {
					tmp = el.nextElement();
					dim_tmp = bf.getBox(alf, tmp);
					if (dim_tmp.getWidth() > dim.getWidth())
						dim.setWidth(dim_tmp.getWidth());
					if (dim_tmp.getHeight() > dim.getHeight())
						dim.setHeight(dim_tmp.getHeight());
				}
			}

			nameWidth = dim.getWidth();
			nameHeight = dim.getHeight();
		} else {
			nameWidth = 0;
			nameHeight = 0;
		}
	}

	@Override
	public void paint(final Graphics arg0) {
		super.paint(arg0);
		final Graphics2D g2d = (Graphics2D) arg0;
		this.drawDendro(g2d);
	}

	private BufferedImage draw() {
		Graphics2D g2d;
		final double worldWidth = this.getSize().getWidth();
		final double worldHeight = this.getSize().getHeight();
		final BufferedImage buff = new BufferedImage((int) worldWidth, (int) worldHeight, BufferedImage.TYPE_INT_RGB);
		g2d = buff.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, (int) worldWidth, (int) worldHeight);
		this.drawDendro(g2d);
		g2d.dispose();

		return buff;
	}

	private void drawDendro(final Graphics2D g2d) {
		BoxContainer boxDendogram, boxBullets, boxAxis, boxAxisLabels, boxNames;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		/* Symmetric margin where it won't be able to paint anything */
		final double margin = InitialProperties.getMargin();// 15

		/* World size */
		final double worldWidth = this.getSize().getWidth();
		final double worldHeight = this.getSize().getHeight();

		// recover sizes
		this.setWidths(g2d);

		// sizes
		Dimensions<Double> dimDendro, dimBullets, dimName, dimAxis, dimAxisLabel;

		dimDendro = new Dimensions<Double>(dendroWidth, dendroHeight);
		dimBullets = new Dimensions<Double>(bulletsWidth, bulletsHeight);
		dimName = new Dimensions<Double>(nameWidth, nameHeight);
		dimAxis = new Dimensions<Double>(axisWidth, axisHeight);
		dimAxisLabel = new Dimensions<Double>(axisLabelWidth, axisLabelHeight);

		/* free space and positions */
		final XYBox posBox = new XYBox(cfg, margin, worldWidth, worldHeight,
		    dimDendro, dimBullets, dimName, dimAxis, dimAxisLabel);

		// boxes
		boxDendogram = posBox.getBoxDendro();
		boxBullets = posBox.getBoxBullets();
		boxNames = posBox.getBoxNames();
		boxAxis = posBox.getBoxAxis();
		boxAxisLabels = posBox.getBoxAxisLabels();

		// situation of boxes in the screen
		this.translateScreen(boxDendogram, worldHeight);
		this.translateScreen(boxBullets, worldHeight);
		this.translateScreen(boxAxis, worldHeight);
		this.translateScreen(boxAxisLabels, worldHeight);
		this.translateScreen(boxNames, worldHeight);

		// invert y axis
		g2d.scale(1, -1);
		g2d.setBackground(Color.GREEN);

		/* scaling factors */
		scaledDendrogram = new ScalingBox(boxDendogram);
		if (cfg.getConfigMenu().getNodeRadius() > 0)
			scaledBullets = new ScalingBox(boxBullets);
		if (cfg.getConfigMenu().isNodeNameVisible())
			scaledNames = new ScalingBox(boxNames);
		if (cfg.getConfigMenu().isAxisVisible())
			scaledAxis = new ScalingBox(boxAxis);
		if (cfg.getConfigMenu().isAxisLabelVisible())
			scaledAxisLabels = new ScalingBox(boxAxisLabels);

		// clipping
		final Clipping clp = new Clipping(axisMaxVal, axisMinVal,
				cfg.getSimilarityType(), cfg.getPrecision());

		// draw interior of bands
		Band bnd;
		final Iterator<Band> itb = clp.clipBands(bandsList).iterator();
		while (itb.hasNext()) {
			bnd = itb.next();
			bnd.setScaling(scaledDendrogram);
			bnd.setColor(cfg.getConfigMenu().getBandColor());
			bnd.setFilled(true);
			bnd.draw(g2d, dendroOrientation);
		}

		// draw lines
		Line lin;
		final Iterator<Line> itl = (Iterator<Line>) clp.clipLines(linesList).iterator();
		while (itl.hasNext()) {
			lin = itl.next();
			lin.setScaling(scaledDendrogram);
			lin.draw(g2d, dendroOrientation);
		}

		// draw band borders
		final Iterator<Band> itb2 = (Iterator<Band>) clp.clipBands(bandsList).iterator();
		while (itb2.hasNext()) {
			bnd = itb2.next();
			bnd.setScaling(scaledDendrogram);
			bnd.setColor(cfg.getConfigMenu().getBandColor());
			bnd.setFilled(false);
			bnd.draw(g2d, dendroOrientation);
		}

		// draw nodes
		if (cfg.getConfigMenu().getNodeRadius() > 0) {
			final Iterator<Node> itc = (Iterator<Node>) nodesList.iterator();
			while (itc.hasNext()) {
				final Node nod = itc.next();
				nod.setScaling(scaledBullets);
				nod.draw(g2d, dendroOrientation);
			}
		}

		// draw node names
		if (cfg.getConfigMenu().isNodeNameVisible()) {
			NodeLabel nodLab;
			nodLab = new NodeLabel(nodesList, cfg.getSimilarityType());
			nodLab.setScaling(scaledNames);
			nodLab.setColor(cfg.getConfigMenu().getNodeNameColor());
			nodLab.setFont(cfg.getConfigMenu().getNodeNameFont());
			nodLab.draw(g2d, dendroOrientation, nodeNameOrientation);
		}

		// draw axis
		if (cfg.getConfigMenu().isAxisVisible()) {
			Axis ax;
			if (dendroOrientation.equals(DendrogramOrientation.WEST)
					|| dendroOrientation.equals(DendrogramOrientation.EAST))
				ax = new Axis(boxAxis.getValMinX(),
						boxAxis.getValMaxX(), cfg.getAxisIncrement(),
						cfg.getAxisTicks());
			else
				ax = new Axis(boxAxis.getValMinY(),
						boxAxis.getValMaxY(), cfg.getAxisIncrement(),
						cfg.getAxisTicks());

			ax.setScaling(scaledAxis);
			ax.setColor(cfg.getConfigMenu().getAxisColor());
			ax.draw(g2d, dendroOrientation, cfg.getSimilarityType(),
					cfg.getAxisTicks());
		}

		// draw axis labels
		if (cfg.getConfigMenu().isAxisLabelVisible() && cfg.getAxisTicks() > 0) {
			AxisLabel axLab;
			if (dendroOrientation.equals(DendrogramOrientation.WEST)
					|| dendroOrientation.equals(DendrogramOrientation.EAST)) {
				axLab = new AxisLabel(boxAxisLabels.getValMinX(),
						boxAxisLabels.getValMaxX(),
						boxAxisLabels.getValMaxY(), cfg.getAxisIncrement(),
						cfg.getAxisTicks(), cfg.getAxisLabelDecimals());
			} else {
				axLab = new AxisLabel(boxAxisLabels.getValMinY(),
						boxAxisLabels.getValMaxY(),
						boxAxisLabels.getValMaxX(), cfg.getAxisIncrement(),
						cfg.getAxisTicks(), cfg.getAxisLabelDecimals());
			}
			axLab.setScaling(scaledAxisLabels);
			axLab.setColor(cfg.getConfigMenu().getAxisLabelColor());
			axLab.setFont(cfg.getConfigMenu().getAxisLabelFont());
			axLab.draw(g2d, dendroOrientation, cfg.getSimilarityType());
		}
	}

}
