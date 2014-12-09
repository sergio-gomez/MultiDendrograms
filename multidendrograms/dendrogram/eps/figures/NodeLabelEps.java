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

package multidendrograms.dendrogram.eps.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.dendrogram.figures.Node;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Name of node EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class NodeLabelEps {
	private Color color = Color.BLACK;
	private Font font;
	private Scaling scal;
	private final SimilarityType simType;
	private final LinkedList<Node> nodesList;

	public NodeLabelEps(final LinkedList<Node> nodesList, final SimilarityType tipDades) {
		this.nodesList = nodesList;
		this.simType = tipDades;
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

	public void draw(final DendrogramOrientation orDendo, final LabelOrientation orNoms) {
		double x, y;
		int rotAngle = 0;
		String txt;
		final AffineTransform rot = new AffineTransform();
		final FontRenderContext renderContext = new FontRenderContext(rot,
				true, true);
		final Font ft = this.getFont();
		Font fr;
		TextLayout tl;

		if (orNoms.equals(LabelOrientation.HORIZONTAL))
			rotAngle = 0;
		else if (orNoms.equals(LabelOrientation.OBLIQUE)) {
			if (simType.equals(SimilarityType.WEIGHT)) {
				if (orDendo.equals(DendrogramOrientation.NORTH)
						|| orDendo.equals(DendrogramOrientation.WEST))
					rotAngle = 45;
				else
					rotAngle = -45;
			} else {
				if (orDendo.equals(DendrogramOrientation.EAST)
						|| orDendo.equals(DendrogramOrientation.SOUTH))
					rotAngle = 45;
				else
					rotAngle = -45;
			}
		} else if (orNoms.equals(LabelOrientation.VERTICAL))
			rotAngle = 90;
		else
			rotAngle = 0;

		rot.rotate(Math.toRadians(rotAngle));
		fr = ft.deriveFont(rot);

		// save settings
		EpsUtils.writeLine("gsave");
		EpsUtils.writeLine(EpsUtils.setRGBColor(
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f));

		if (this.getFont().getPSName().equals("Dialog.plain"))
			EpsUtils.writeLine(EpsUtils.scaleSetFont("/ArialMT", this
					.getFont().getSize()));
		else
			EpsUtils.writeLine(EpsUtils.scaleSetFont("/"
					+ this.getFont().getPSName(), this.getFont().getSize()));

		double maxy = 0.0, maxx = 0.0, miny = Double.MAX_VALUE, bigy = 0.0;
		for (final Node c : nodesList) {
			x = c.getPosReal().getX();
			y = c.getPosReal().getY();

			txt = String.valueOf(c.getName());
			tl = new TextLayout(txt, fr, renderContext);

			if (Math.abs(tl.getBounds().getMaxY()) > Math.abs(maxy))
				maxy = tl.getBounds().getMaxY();
			if (Math.abs(tl.getBounds().getY()) > Math.abs(bigy))
				bigy = tl.getBounds().getY();
			if (Math.abs(tl.getBounds().getMinY()) < Math.abs(miny))
				miny = tl.getBounds().getMinY();
			if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
				maxx = tl.getBounds().getMaxX();
		}

		for (final Node c : nodesList) {
			x = c.getPosReal().getX();
			y = c.getPosReal().getY();

			txt = String.valueOf(c.getName());
			tl = new TextLayout(txt, fr, renderContext);

			if ((orDendo == DendrogramOrientation.EAST) || (orDendo == DendrogramOrientation.WEST)) {
				// values range
				y = this.getScaling().getValuesHeight() - c.getPosReal().getX();
				x = this.getScaling().transformX(0);
				y = this.getScaling().transformY(y);
			} else {
				y = this.getScaling().transformY(0);
				x = this.getScaling().transformX(x);
			}

			// correction of text after rotation
			if (LabelOrientation.HORIZONTAL.equals(orNoms)) { /* HORIZONTAL */
				if (DendrogramOrientation.NORTH.equals(orDendo)
						|| DendrogramOrientation.SOUTH.equals(orDendo)) {
					x -= (tl.getBounds().getCenterX());
				}
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

			} else if (LabelOrientation.OBLIQUE.equals(orNoms)) {  /* OBLIQUE */
				if (simType.equals(SimilarityType.WEIGHT)) {
					if (DendrogramOrientation.SOUTH.equals(orDendo))
						y += Math.abs(maxy) - tl.getBounds().getY();
					else if (DendrogramOrientation.WEST.equals(orDendo)) {
						x += Math.abs(maxx) - tl.getBounds().getMaxX();
						y -= tl.getBounds().getHeight();
					}
				} else {
					if (DendrogramOrientation.NORTH.equals(orDendo))
						y += Math.abs(bigy) - tl.getBounds().getMaxY();
					else if (DendrogramOrientation.EAST.equals(orDendo)) {
						x += Math.abs(maxx) - tl.getBounds().getMaxX();
						y -= tl.getBounds().getHeight();
					}
				}
			} else { /* VERTICAL */
				if (simType.equals(SimilarityType.WEIGHT)) {
					if (DendrogramOrientation.SOUTH.equals(orDendo)) {
						y += Math.abs(maxy) - tl.getBounds().getMaxY();
						x += tl.getBounds().getWidth() / 2;
					} else if (DendrogramOrientation.NORTH.equals(orDendo))
						x += tl.getBounds().getWidth() / 2;
					else {
						y -= tl.getBounds().getHeight() / 2;
						x += Math.abs(maxx);
					}
				} else if (simType.equals(SimilarityType.DISTANCE)) {
					if (DendrogramOrientation.NORTH.equals(orDendo)) {
						y += Math.abs(maxy) - tl.getBounds().getMaxY();
						x += tl.getBounds().getWidth() / 2;
					} else if (DendrogramOrientation.SOUTH.equals(orDendo))
						x += tl.getBounds().getWidth() / 2;
					else {
						y -= tl.getBounds().getHeight() / 2;
						x += Math.abs(maxx);
					}
				}
			}

			EpsUtils.writeLine(EpsUtils.bottomLeftTextRotated(
					(float) (EpsUtils.xmin + x), (float) (EpsUtils.ymax + y),
					rotAngle, txt));
		}

		// restore settings
		EpsUtils.writeLine("grestore");
	}
}
