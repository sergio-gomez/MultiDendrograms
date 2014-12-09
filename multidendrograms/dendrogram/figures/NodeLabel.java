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

package multidendrograms.dendrogram.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import multidendrograms.dendrogram.Scaling;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Name of node figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class NodeLabel {
	private Color color = Color.BLACK;
	private Font font;
	private Scaling scal;
	private final SimilarityType simType;
	LinkedList<Node> nodesList;

	public NodeLabel(final LinkedList<Node> nodesList, final SimilarityType simType) {
		this.nodesList = nodesList;
		this.simType = simType;
	}

	public LinkedList<Node> getNodesList() {
		return this.nodesList;
	}

	public Scaling getScaling() {
		return scal;
	}

	public void setScaling(final Scaling scal) {
		this.scal = scal;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color c) {
		this.color = c;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(final Font f) {
		this.font = f;
	}

	public SimilarityType getSimilarityType() {
		return this.simType;
	}

	public void draw(final Graphics2D g, final DendrogramOrientation orDendo,
			final LabelOrientation orNoms) {
		double x, y;
		int rotAngle = 0;
		String txt;
		final FontRenderContext renderContext = new FontRenderContext(null,
				true, true);
		final AffineTransform rot = new AffineTransform();
		final Font ft = this.getFont();
		Font fr;
		TextLayout tl;

		if (orNoms.equals(LabelOrientation.HORIZONTAL))
			rotAngle = 0;
		else if (orNoms.equals(LabelOrientation.OBLIQUE)) {
			if (simType.equals(SimilarityType.WEIGHT)) {
				if (orDendo.equals(DendrogramOrientation.NORTH)
						|| orDendo.equals(DendrogramOrientation.WEST))
					rotAngle = -45;
				else
					rotAngle = 45;
			} else {
				if (orDendo.equals(DendrogramOrientation.EAST)
						|| orDendo.equals(DendrogramOrientation.SOUTH))
					rotAngle = -45;
				else
					rotAngle = 45;
			}
		} else if (orNoms.equals(LabelOrientation.VERTICAL))
			rotAngle = -90;
		else
			rotAngle = 0;

		rot.rotate(Math.toRadians(rotAngle));
		fr = ft.deriveFont(rot);

		final Color color_original = g.getColor();
		g.setColor(this.getColor());

		double maxy = 0.0, maxx = 0.0, bigy = 0.0;// , miny = 0.0;
		for (final Node c : nodesList) {
			x = c.getPosReal().getX();
			y = c.getPosReal().getY();

			txt = String.valueOf(c.getName());
			tl = new TextLayout(txt, fr, renderContext);

			if (Math.abs(tl.getBounds().getMaxY()) > Math.abs(maxy))
				maxy = tl.getBounds().getMaxY();
			if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
				maxx = tl.getBounds().getMaxX();
			if (Math.abs(tl.getBounds().getY()) > Math.abs(bigy))
				bigy = tl.getBounds().getY();
		}

		for (final Node c : nodesList) {
			x = c.getPosReal().getX();
			y = c.getPosReal().getY();

			txt = String.valueOf(c.getName());
			tl = new TextLayout(txt, fr, renderContext);

			if ((orDendo == DendrogramOrientation.EAST) || (orDendo == DendrogramOrientation.WEST)) {
				y = this.getScaling().getValuesHeight() - c.getPosReal().getX();
				x = this.getScaling().transformX(0);
				y = this.getScaling().transformY(y);
			} else {
				y = this.getScaling().transformY(0);
				x = this.getScaling().transformX(x);
			}

			// correction of text after rotation
			if (LabelOrientation.HORIZONTAL.equals(orNoms)) /* HORIZONTAL */
			{
				if (DendrogramOrientation.NORTH.equals(orDendo)
						|| DendrogramOrientation.SOUTH.equals(orDendo))
					x -= (tl.getBounds().getCenterX());
				else // EAST and WEST
				{
					if (simType.equals(SimilarityType.WEIGHT)) {
						if (DendrogramOrientation.WEST.equals(orDendo)) {
							y -= tl.getBounds().getHeight() / 2;
							x += Math.abs(maxx) - tl.getBounds().getMaxX();
						} else
							y += tl.getBounds().getCenterY();
					}
					if (simType.equals(SimilarityType.DISTANCE)) {
						if (DendrogramOrientation.EAST.equals(orDendo)) {
							y -= tl.getBounds().getHeight() / 2;
							x += Math.abs(maxx) - tl.getBounds().getMaxX();
						} else
							y += tl.getBounds().getCenterY();
					}
				}
			} else if (LabelOrientation.OBLIQUE.equals(orNoms)) /* OBLIQUE */
			{
				if (simType.equals(SimilarityType.WEIGHT)) {
					if (DendrogramOrientation.SOUTH.equals(orDendo))
						y += Math.abs(maxy) - tl.getBounds().getMinY();
					else if (DendrogramOrientation.WEST.equals(orDendo)) {
						x += Math.abs(maxx) - tl.getBounds().getMaxX();
						y -= tl.getBounds().getHeight();
					}
				} else {
					if (DendrogramOrientation.NORTH.equals(orDendo))
						y += Math.abs(maxy) - tl.getBounds().getMinY();
					else if (DendrogramOrientation.EAST.equals(orDendo)) {
						x += Math.abs(maxx) - tl.getBounds().getMaxX();
						y -= tl.getBounds().getHeight();
					}
				}
			} else /* VERTICAL */
			{
				if (simType.equals(SimilarityType.WEIGHT)) {
					if (DendrogramOrientation.SOUTH.equals(orDendo)) {
						y += Math.abs(bigy) + tl.getBounds().getY();
						x += tl.getBounds().getWidth() / 2;
					} else if (DendrogramOrientation.NORTH.equals(orDendo))
						x += tl.getBounds().getWidth() / 2;
					else {
						y -= tl.getBounds().getHeight() / 2;
						x -= tl.getBounds().getMinX();
					}
				} else if (simType.equals(SimilarityType.DISTANCE)) {
					if (DendrogramOrientation.NORTH.equals(orDendo)) {
						y += Math.abs(bigy) + tl.getBounds().getY();
						x += tl.getBounds().getWidth() / 2;
					} else if (DendrogramOrientation.SOUTH.equals(orDendo))
						x += tl.getBounds().getWidth() / 2;
					else {
						y -= tl.getBounds().getHeight() / 2;
						x -= tl.getBounds().getMinX();
					}
				}

			}
			g.scale(1, -1);
			tl.draw(g, (float) x, (float) -y);
			g.scale(1, -1);
		}

		g.setColor(color_original);
	}
}
