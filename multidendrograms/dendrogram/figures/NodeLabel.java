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
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import multidendrograms.definitions.Coordinates;
import multidendrograms.definitions.SettingsInfo;
import multidendrograms.dendrogram.Scaling;
import multidendrograms.dendrogram.eps.EpsUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.PlotType;

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

	private LinkedList<Node> nodesList;
	private Font font;
	private Color color;
	private LabelOrientation labelOrientation;
	private DendrogramOrientation dendroOrientation;
	private DendrogramOrientation adaptedOrientation;
	private Scaling scalingDendro;

	public NodeLabel(final LinkedList<Node> nodesList, final SettingsInfo settingsInfo, final Scaling scalingDendro) {
		this.nodesList = nodesList;
		this.font = settingsInfo.getNodeNameFont();
		this.color = settingsInfo.getNodeNameColor();
		this.labelOrientation = settingsInfo.getNodeNameOrientation();
		this.dendroOrientation = settingsInfo.getDendrogramOrientation();
		this.adaptedOrientation = settingsInfo.getDendrogramAdaptedOrientation();
		this.scalingDendro = scalingDendro;
	}

	public void draw(final PlotType plotType, final Graphics2D graphics2D) {
		// rotate font
		double rotationAngle = 0.0;
		if (labelOrientation.equals(LabelOrientation.HORIZONTAL)) {
			rotationAngle = 0.0;
		} else if (labelOrientation.equals(LabelOrientation.VERTICAL)) {
			rotationAngle = +90.0;
		} else if (labelOrientation.equals(LabelOrientation.OBLIQUE)) {
			if (adaptedOrientation.equals(DendrogramOrientation.NORTH) || 
					adaptedOrientation.equals(DendrogramOrientation.WEST)) {
				rotationAngle = -45.0;
			} else if (adaptedOrientation.equals(DendrogramOrientation.SOUTH) || 
					adaptedOrientation.equals(DendrogramOrientation.EAST)) {
				rotationAngle = +45.0;
			}
		}
		AffineTransform rotation = new AffineTransform();
		if (plotType.equals(PlotType.PANEL)) {
			rotation.rotate(Math.toRadians(-rotationAngle));
		} else if (plotType.equals(PlotType.EPS)) {
			rotation.rotate(Math.toRadians(rotationAngle));
		}
		Font rotatedFont = font.deriveFont(rotation);
		
		// save settings
		Color originalColor = null;
		if (plotType.equals(PlotType.PANEL)) {
			originalColor = graphics2D.getColor();
			graphics2D.setColor(color);
		} else if (plotType.equals(PlotType.EPS)) {
			EpsUtils.writeLine("gsave");
			EpsUtils.writeLine(EpsUtils.setRGBColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
			String fontPSName = font.getPSName();
			if (fontPSName.equals("Dialog.plain")) {
				EpsUtils.writeLine(EpsUtils.scaleSetFont("/ArialMT", font.getSize()));
			} else {
				EpsUtils.writeLine(EpsUtils.scaleSetFont("/" + fontPSName, font.getSize()));
			}
		}
		
		FontRenderContext renderContext = null;
		if (plotType.equals(PlotType.PANEL)) {
			renderContext = new FontRenderContext(null, true, true);
		} else if (plotType.equals(PlotType.EPS)) {
			renderContext = new FontRenderContext(rotation, true, true);
		}
		for (Node node : nodesList) {
			Coordinates<Double> screen = getNameCoordinates(node, rotatedFont, renderContext);
			double screenX = screen.getX();
			double screenY = screen.getY();
			String nodeName = String.valueOf(node.getName());
			if (plotType.equals(PlotType.PANEL)) {
				TextLayout textLayout = new TextLayout(nodeName, rotatedFont, renderContext);
				graphics2D.scale(1, -1);
				textLayout.draw(graphics2D, (float) (screenX), (float) (-screenY));
				graphics2D.scale(1, -1);
			} else if (plotType.equals(PlotType.EPS)) {
				EpsUtils.writeLine(EpsUtils.bottomLeftTextRotated((float) (EpsUtils.xmin + screenX), 
						(float) (EpsUtils.ymax + screenY), (float) (rotationAngle), nodeName));
			}
		}

		// restore settings
		if (plotType.equals(PlotType.PANEL)) {
			graphics2D.setColor(originalColor);
		} else if (plotType.equals(PlotType.EPS)) {
			EpsUtils.writeLine("grestore");
		}
	}

	private Coordinates<Double> getNameCoordinates(final Node node, final Font rotatedFont, final FontRenderContext renderContext) {
		double bulletSpace = 2 * node.getRadius();
		String nodeName = String.valueOf(node.getName());
		TextLayout textLayout = new TextLayout(nodeName, rotatedFont, renderContext);
		Rectangle2D rectangle = textLayout.getBounds();
		double rectangleWidth = rectangle.getWidth();
		double rectangleHeight = rectangle.getHeight();
		Coordinates<Double> world = node.getPosReal();
		Coordinates<Double> screen = this.scalingDendro.transform(world, dendroOrientation);
		double screenX = screen.getX();
		double screenY = screen.getY();
		if (labelOrientation.equals(LabelOrientation.VERTICAL)) {
			if (adaptedOrientation.equals(DendrogramOrientation.NORTH)) {
				screenX += + rectangleWidth / 2;
				screenY += - bulletSpace - rectangleHeight;
			} else if (adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
				screenX += + rectangleWidth / 2;
				screenY += + bulletSpace;
			} else if (adaptedOrientation.equals(DendrogramOrientation.EAST)) {
				screenX += - bulletSpace;
				screenY += - rectangleHeight / 2;
			} else {// (adaptedOrientation.equals(DendrogramOrientation.WEST))
				screenX += + bulletSpace + rectangleWidth;
				screenY += - rectangleHeight / 2;
			}
		} else if (labelOrientation.equals(LabelOrientation.HORIZONTAL)) {
			if (adaptedOrientation.equals(DendrogramOrientation.NORTH)) {
				screenX += - rectangleWidth / 2;
				screenY += - bulletSpace - rectangleHeight;
			} else if (adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
				screenX += - rectangleWidth / 2;
				screenY += + bulletSpace;
			} else if (adaptedOrientation.equals(DendrogramOrientation.EAST)) {
				screenX += - bulletSpace - rectangleWidth;
				screenY += - rectangleHeight / 2;
			} else {// (adaptedOrientation.equals(DendrogramOrientation.WEST))
				screenX += + bulletSpace;
				screenY += - rectangleHeight / 2;
			}
		} else {// (labelOrientation.equals(LabelOrientation.OBLIQUE))
			if (adaptedOrientation.equals(DendrogramOrientation.NORTH)) {
				screenY += - bulletSpace;
			} else if (adaptedOrientation.equals(DendrogramOrientation.SOUTH)) {
				screenY += + bulletSpace;
			} else if (adaptedOrientation.equals(DendrogramOrientation.EAST)) {
				screenX += - rectangleWidth;
				screenY += - rectangleHeight;
			} else {// (adaptedOrientation.equals(DendrogramOrientation.WEST))
				screenX += + bulletSpace;
			}
		}
		screen.setX(screenX);
		screen.setY(screenY);
		return screen;
	}

}
