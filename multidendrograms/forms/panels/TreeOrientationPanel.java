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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import multidendrograms.initial.LogManager;
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

	File f;

	int or = 0;

	public TreeOrientationPanel() {
		super();
		final String s = "img/Tree_N.jpg";
		f = new File(s);
	}

	public void setImage(final DendrogramOrientation i) {
		final String s1 = "img/Tree_N.jpg";
		final String s2 = "img/Tree_S.jpg";
		final String s3 = "img/Tree_E.jpg";
		final String s4 = "img/Tree_W.jpg";
		switch (i) {
		case NORTH:
			f = new File(s1);
			or = 0;
			break;
		case SOUTH:
			f = new File(s2);
			or = 0;
			break;
		case EAST:
			f = new File(s3);
			or = 1;
			break;
		case WEST:
			f = new File(s4);
			or = 1;
			break;
		}
		this.repaint();
	}

	@Override
	public void paint(final Graphics arg0) {
		int x, y, w, h;

		super.paint(arg0);
		final Graphics2D g2 = (Graphics2D) arg0;
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
			if (or == 0) {
				w = 50;
				h = 47;
			} else {
				w = 47;
				h = 50;
			}
			x = (this.getWidth() - w) / 2;
			y = (this.getHeight() - h) / 2;
			g2.drawImage(img, x, y, w, h, null);
		} catch (final IOException e) {
			LogManager.LOG.throwing("TreeOrientationPanel.java", "paint", e);
		} catch (Exception e) {
			LogManager.LOG.throwing("TreeOrientationPanel.java", "paint", e);
		}
	}

}
