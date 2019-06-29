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

package multidendrograms.forms.panels;

import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import multidendrograms.initial.LogManager;
import multidendrograms.initial.InitialProperties;
import multidendrograms.types.DendrogramOrientation;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Tree orientation images
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class TreeOrientationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final String s_N = "img/Tree_N.png";
	private final String s_S = "img/Tree_S.png";
	private final String s_E = "img/Tree_E.png";
	private final String s_W = "img/Tree_W.png";

	private ImageIcon img_N;
	private ImageIcon img_S;
	private ImageIcon img_E;
	private ImageIcon img_W;

	private ImageIcon curImg;

	public TreeOrientationPanel() {
		super();
		img_N = prepareImage(s_N);
		img_S = prepareImage(s_S);
		img_E = prepareImage(s_E);
		img_W = prepareImage(s_W);
		curImg = img_N;
		int w = curImg.getIconWidth();
		int h = curImg.getIconHeight();
		Dimension dim = new Dimension(w, h);
		setMinimumSize(dim);
		setPreferredSize(dim);
	}

	public void setImage(final DendrogramOrientation orientation) {
		switch (orientation) {
		case NORTH:
			curImg = img_N;
			break;
		case SOUTH:
			curImg = img_S;
			break;
		case EAST:
			curImg = img_E;
			break;
		case WEST:
			curImg = img_W;
			break;
		}
		this.repaint();
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		curImg.paintIcon(this, g, 0, 0);
	}

	private ImageIcon prepareImage(final String s) {
  	BufferedImage imgOri;
  	ImageIcon img = null;
		try {
			imgOri = ImageIO.read(new File(s));
			int w = InitialProperties.scaleSize(imgOri.getWidth());
			int h = InitialProperties.scaleSize(imgOri.getHeight());
			img = new ImageIcon(imgOri.getScaledInstance(w, h, Image.SCALE_SMOOTH));
		} catch (final IOException e) {
			LogManager.LOG.throwing("TreeOrientationPanel.java", "paint", e);
		} catch (Exception e) {
			LogManager.LOG.throwing("TreeOrientationPanel.java", "paint", e);
		}
		return img;
	}
}
