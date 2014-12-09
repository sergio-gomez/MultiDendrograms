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

package multidendrograms.dendrogram.eps;

import multidendrograms.forms.children.DendrogramPanel;
import multidendrograms.definitions.Config;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Save dendrogram to EPS
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EpsExporter {
	private static final String EPS_PROLOG = "./ini/eps_prolog.txt";
	private static final String EPS_CREATOR = "./ini/eps_prolog.txt";
	private static final String EPS_ORIENTATION = "Portrait";

	private final EpsPlot dendroEpsPlot;

	// Bounding Box coordinates
	private final int xmin;
	private final int xmax;
	private final int ymin;
	private final int ymax;

	public EpsExporter(Config cfg, DendrogramPanel dendroPanel, String path) {

		this.xmin = 72;
		this.ymin = 72;
		this.xmax = dendroPanel.getWidth() + 72;
		this.ymax = dendroPanel.getHeight() + 72;

		new EpsUtils(xmin, ymin, xmax, ymax);
		dendroEpsPlot = new EpsPlot(dendroPanel, cfg, xmax, ymax);
		writeEPS(path);
	}

	public void writeEPS(String epsPath) {
		EpsUtils.open(epsPath);
		EpsUtils.writeComments(EPS_CREATOR, EPS_ORIENTATION);
		EpsUtils.writeProlog(EPS_PROLOG);
		this.writeBodyIni();
		EpsUtils.writeBodyEnd();
		EpsUtils.close();
	}

	public void writeBodyIni() {
		EpsUtils.writeLine("");
		EpsUtils.writeLine("%%Page: 1 1");
		EpsUtils.writeLine(EpsUtils.setLineWidth(1.0f));
		EpsUtils.writeLine("[] 0 setdash");
		dendroEpsPlot.drawDendro();
	}

}
