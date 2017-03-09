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
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import multidendrograms.definitions.Config;
import multidendrograms.definitions.Formats;
import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.Clipping;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.figures.Axis;
import multidendrograms.dendrogram.figures.AxisLabel;
import multidendrograms.dendrogram.figures.Band;
import multidendrograms.dendrogram.figures.Line;
import multidendrograms.dendrogram.figures.Node;
import multidendrograms.dendrogram.figures.NodeLabel;
import multidendrograms.forms.PrincipalDesk;
import multidendrograms.forms.XYBox;
import multidendrograms.initial.Language;
import multidendrograms.initial.LogManager;
import multidendrograms.types.PlotType;

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

	private Config cfg = null;

	private LinkedList<Node> nodesList;
	private LinkedList<Line> linesList;
	private LinkedList<Band> bandsList;

	protected DendrogramPanel dendroPanel;
	private final PrincipalDesk frm; // main window

	public DendrogramPanel(final PrincipalDesk frm) {
		super();
		this.frm = frm;
		initComponentsMenu();
		this.dendroPanel = this;
	}

	private void initComponentsMenu() {
		al = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				if (evt.getActionCommand().equals(Language.getLabel(96))) {
					// Save dendrogram as JPG
					try {
						final BufferedImage buff = DendrogramPanel.this.draw();
						frm.savePicture(buff, "jpg", cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(97))) {
					// Save dendrogram as PNG
					try {
						final BufferedImage buff = DendrogramPanel.this.draw();
						frm.savePicture(buff, "png", cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(95))) {
					// Show dendrogram details
					try {
						new DendrogramTree(cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(76);
						JOptionPane.showMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(98))) {
					// Save dendrogram as TXT
					try {
						frm.saveTXT(cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(87))) {
					// Save dendrogram as Newick tree
					try {
						frm.saveNewick(cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(79))) {
					// Save dendrogram as JSON tree
					try {
						frm.saveJson(cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(99))) {
					// Save dendrogram as EPS
					try {
						frm.savePostScript(dendroPanel, cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(116))) {
					// Save ultrametric matrix as TXT
					try {
						frm.saveUltrametricTxt(cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(111))) {
					// Save dendrogram measures
					try {
						frm.saveDendrogramMeasures(cfg);
					} catch (Exception e) {
						LogManager.LOG.throwing("DendrogramPanel", "initComponentsMenu()", e);
						String errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(frm.getPanDesk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(117))) {
					// Show dendrogram measures
					frm.showDendrogramMeasures(cfg);
				}
			}
		};

		menu = new JPopupMenu();
		final JMenuItem me0 = Formats.getFormattedMenuItem(Language.getLabel(117)); // Show dendrogram characteristics
		final JMenuItem me1 = Formats.getFormattedMenuItem(Language.getLabel(95));  // Show dendrogram details
		final JMenuItem me2 = Formats.getFormattedMenuItem(Language.getLabel(111)); // Save dendrogram characteristics
		final JMenuItem me3 = Formats.getFormattedMenuItem(Language.getLabel(116)); // Save ultrametric matrix as TXT
		final JMenuItem me4 = Formats.getFormattedMenuItem(Language.getLabel(98));  // Save dendrogram as TXT
		final JMenuItem me5 = Formats.getFormattedMenuItem(Language.getLabel(87));  // Save dendrogram as Newick tree
		final JMenuItem me6 = Formats.getFormattedMenuItem(Language.getLabel(79));  // Save dendrogram as JSON tree
		final JMenuItem me7 = Formats.getFormattedMenuItem(Language.getLabel(96));  // Save dendrogram as JPG
		final JMenuItem me8 = Formats.getFormattedMenuItem(Language.getLabel(97));  // Save dendrogram as PNG
		final JMenuItem me9 = Formats.getFormattedMenuItem(Language.getLabel(99));  // Save dendrogram as EPS

		me0.addActionListener(al);
		me1.addActionListener(al);
		me2.addActionListener(al);
		me3.addActionListener(al);
		me4.addActionListener(al);
		me5.addActionListener(al);
		me6.addActionListener(al);
		me7.addActionListener(al);
		me8.addActionListener(al);
		me9.addActionListener(al);

		menu.add(me0);
		menu.add(me1);
		menu.addSeparator();
		menu.add(me2);
		menu.add(me3);
		menu.add(me4);
		menu.add(me5);
		menu.add(me6);
		menu.addSeparator();
		menu.add(me7);
		menu.add(me8);
		menu.add(me9);

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
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
	}

	public LinkedList<Node> getNodesList() {
		return this.nodesList;
	}

	public LinkedList<Line> getLinesList() {
		return this.linesList;
	}

	public LinkedList<Band> getBandsList() {
		return this.bandsList;
	}

	public void setNodesList(final LinkedList<Node> list) {
		this.nodesList = list;
		LogManager.LOG.finest("Number of nodes: (" + list.size() + ")");
	}

	public void setLinesList(final LinkedList<Line> list) {
		this.linesList = list;
		LogManager.LOG.finest("Number of nodes: (" + list.size() + ")");
	}

	public void setBandsList(final LinkedList<Band> list) {
		this.bandsList = list;
		LogManager.LOG.finest("Number of nodes: (" + list.size() + ")");
	}

	@Override
	public void update(final Graphics arg0) {
		super.update(arg0);
	}

	@Override
	public void paint(final Graphics arg0) {
		super.paint(arg0);
		final Graphics2D g2d = (Graphics2D) arg0;
		drawDendro(g2d);
	}

	private BufferedImage draw() {
		final double worldWidth = getSize().getWidth();
		final double worldHeight = getSize().getHeight();
		final BufferedImage buff = new BufferedImage((int) worldWidth, (int) worldHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = buff.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, (int) worldWidth, (int) worldHeight);
		drawDendro(g2d);
		g2d.dispose();
		return buff;
	}

	private void drawDendro(final Graphics2D g2d) {
		final double worldWidth = getSize().getWidth();
		final double worldHeight = getSize().getHeight();

		XYBox boxes = new XYBox(this.cfg, worldWidth, worldHeight);
		Scaling scalingDendro = boxes.getScalingDendrogram();
		Scaling scalingBullets = boxes.getScalingBullets();
		Scaling scalingAxis = boxes.getScalingAxis();
		Scaling scalingAxisLabels = boxes.getScalingAxisLabels();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// invert Y axis
		g2d.scale(1, -1);
		g2d.setBackground(Color.GREEN);

		drawDendrogram(g2d, scalingDendro);
		drawBullets(g2d, scalingBullets, scalingDendro);
		drawNodesNames(g2d, scalingDendro);
		drawAxis(g2d, scalingAxis);
		drawAxisLabels(g2d, scalingAxisLabels);
	}

	private void drawDendrogram(final Graphics2D g2d, final Scaling scalingDendro) {
		final SettingsInfo settingsInfo = this.cfg.getSettingsInfo();
		final Clipping clipping = new Clipping(this.cfg.getProximityType(), this.cfg.getAxisMinValue(),
				this.cfg.getAxisMaxValue());

		// draw interior of bands
		final Iterator<Band> iterIB = clipping.clipBands(this.bandsList).iterator();
		while (iterIB.hasNext()) {
			Band band = iterIB.next();
			band.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
			band.setScaling(scalingDendro);
			band.setColor(settingsInfo.getBandColor());
			band.setFilled(true);
			band.draw(PlotType.PANEL, g2d);
		}

		// draw lines
		Iterator<Line> iterL = clipping.clipLines(this.linesList).iterator();
		while (iterL.hasNext()) {
			Line line = iterL.next();
			line.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
			line.setScaling(scalingDendro);
			line.draw(PlotType.PANEL, g2d);
		}

		// draw band borders
		Iterator<Band> iterBB = clipping.clipBands(this.bandsList).iterator();
		while (iterBB.hasNext()) {
			Band band = iterBB.next();
			band.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
			band.setScaling(scalingDendro);
			band.setColor(settingsInfo.getBandColor());
			band.setFilled(false);
			band.draw(PlotType.PANEL, g2d);
		}
	}

	private void drawBullets(final Graphics2D g2d, final Scaling scalingBullets, final Scaling scalingDendro) {
		final SettingsInfo settingsInfo = this.cfg.getSettingsInfo();
		if (settingsInfo.getNodeRadius() > 0) {
			Iterator<Node> iter = this.nodesList.iterator();
			while (iter.hasNext()) {
				Node node = iter.next();
				node.setDendrogramOrientation(this.cfg.getDendrogramOrientation());
				node.setScaling(scalingBullets);
				node.setScalingDendrogram(scalingDendro);
				node.draw(PlotType.PANEL, g2d);
			}
		}
	}

	private void drawNodesNames(final Graphics2D g2D, final Scaling scalingDendro) {
		final SettingsInfo settingsInfo = this.cfg.getSettingsInfo();
		if (settingsInfo.isNodeNameVisible()) {
			NodeLabel nodeLabel = new NodeLabel(this.nodesList, settingsInfo, scalingDendro);
			nodeLabel.draw(PlotType.PANEL, g2D);
		}
	}

	private void drawAxis(final Graphics2D g2D, final Scaling scalingAxis) {
		final SettingsInfo settingsInfo = this.cfg.getSettingsInfo();
		if (settingsInfo.isAxisVisible() && (this.cfg.getAxisTicks() > 0)) {
			Axis axis = new Axis(settingsInfo, scalingAxis);
			axis.draw(PlotType.PANEL, g2D);
		}
	}

	private void drawAxisLabels(final Graphics2D g2D, final Scaling scalingAxisLabels) {
		final SettingsInfo settingsInfo = this.cfg.getSettingsInfo();
		if (settingsInfo.isAxisLabelVisible() && (this.cfg.getAxisTicks() > 0)) {
			AxisLabel axisLabel = new AxisLabel(settingsInfo, scalingAxisLabels);
			axisLabel.draw(PlotType.PANEL, g2D);
		}
	}

}
