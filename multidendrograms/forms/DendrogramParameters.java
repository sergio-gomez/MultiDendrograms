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

package multidendrograms.forms;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;

import multidendrograms.initial.Language;
import multidendrograms.forms.panels.SettingsPanel;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.MethodName;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.SimilarityType;
import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;
import multidendrograms.definitions.DistancesMatrix;
import multidendrograms.dendrogram.UltrametricMatrix;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dendrogram parameters
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DendrogramParameters {

	// Input data
	private ExternalData extData;

	// MultiDendrogram
	private DistancesMatrix distMatrix = null;
	private UltrametricMatrix ultraMatrix = null;

	// Settings on type of data, method, and precision
	private SimilarityType simType;
	private MethodName method;
	private int precision;

	// TREE
	private DendrogramOrientation dendroOrientation = DendrogramOrientation.NORTH;
	private boolean bandVisible = true;
	private Color bandColor = Color.LIGHT_GRAY;

	// NODES
	private int nodeRadius = 0;
	private boolean nodeNameVisible = true;
	private Font nodeNameFont;
	private Color nodeNameColor;
	private LabelOrientation nodeNameOrientation = LabelOrientation.VERTICAL;

	// AXIS
	private boolean axisVisible = true;
	private Color axisColor = Color.BLACK;
	private double axisMinVal = 0.0;
	private double axisMaxVal = 1.0;
	private double axisIncrement = 0.1;
	private int axisTicks = 10;
	private boolean axisLabelVisible = true;
	private Font axisLabelFont;
	private Color axisLabelColor = Color.BLACK;
	private int axisLabelDecimals = 0;

	public DendrogramParameters(final DataFile fitx, final DistancesMatrix distMatrix) {
		try {
			this.extData = new ExternalData(fitx, false);
			this.distMatrix = distMatrix;
			SettingsPanel.getConfigPanel(this);
		} catch (Exception e) {
			showError("Error: \n" + e.toString());
		}
	}

	public DistancesMatrix getDistancesMatrix() {
		return distMatrix;
	}

	public UltrametricMatrix getUltrametricMatrix() {
		return ultraMatrix;
	}

	public void setUltrametricMatrix(UltrametricMatrix ultraMatrix) {
		this.ultraMatrix = ultraMatrix;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(null, message, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}

	public ExternalData getExternalData() {
		return extData;
	}

	public SimilarityType getSimilarityType() {
		return simType;
	}

	public void setSimilarityType(final SimilarityType simType) {
		this.simType = simType;
	}

	public MethodName getMethod() {
		return method;
	}

	public void setMethod(final MethodName method) {
		this.method = method;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(final int precision) {
		this.precision = precision;
	}

	public DendrogramOrientation getDendrogramOrientation() {
		return dendroOrientation;
	}

	public void setDendrogramOrientation(final DendrogramOrientation dendroOrientation) {
		this.dendroOrientation = dendroOrientation;
	}

	public boolean isBandVisible() {
		return bandVisible;
	}

	public void setBandVisible(final boolean bandVisible) {
		this.bandVisible = bandVisible;
	}

	public Color getBandColor() {
		return bandColor;
	}

	public void setBandColor(final Color bandColor) {
		this.bandColor = bandColor;
	}

	public int getNodeRadius() {
		return nodeRadius;
	}

	public void setNodeRadius(final int nodeRadius) {
		this.nodeRadius = nodeRadius;
	}

	public boolean isNodeNameVisible() {
		return nodeNameVisible;
	}

	public void setNodeNameVisible(final boolean nodeNameVisible) {
		this.nodeNameVisible = nodeNameVisible;
	}

	public Font getNodeNameFont() {
		return nodeNameFont;
	}

	public void setNodeNameFont(final Font nodeNameFont) {
		this.nodeNameFont = nodeNameFont;
	}

	public Color getNodeNameColor() {
		return nodeNameColor;
	}

	public void setNodeNameColor(final Color nodeNameColor) {
		this.nodeNameColor = nodeNameColor;
	}

	public LabelOrientation getNodeNameOrientation() {
		return nodeNameOrientation;
	}

	public void setNodeNameOrientation(final LabelOrientation nodeNameOrientation) {
		this.nodeNameOrientation = nodeNameOrientation;
	}

	public boolean isAxisVisible() {
		return axisVisible;
	}

	public void setAxisVisible(final boolean axisVisible) {
		this.axisVisible = axisVisible;
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public void setAxisColor(final Color axisColor) {
		this.axisColor = axisColor;
	}

	public double getAxisMinVal() {
		return axisMinVal;
	}

	public void setAxisMinVal(final double axisMinVal) {
		this.axisMinVal = axisMinVal;
	}

	public double getAxisMaxVal() {
		return axisMaxVal;
	}

	public void setAxisMaxVal(final double axisMaxVal) {
		this.axisMaxVal = axisMaxVal;
	}

	public double getAxisIncrement() {
		return axisIncrement;
	}

	public void setAxisIncrement(final double axisIncrement) {
		this.axisIncrement = axisIncrement;
	}

	public int getAxisTicks() {
		return axisTicks;
	}

	public void setAxisTicks(final int axisTicks) {
		this.axisTicks = axisTicks;
	}

	public boolean isAxisLabelVisible() {
		return axisLabelVisible;
	}

	public void setAxisLabelVisible(final boolean axisLabelVisible) {
		this.axisLabelVisible = axisLabelVisible;
	}

	public Font getAxisLabelFont() {
		return axisLabelFont;
	}

	public void setAxisLabelFont(final Font axisLabelFont) {
		this.axisLabelFont = axisLabelFont;
	}

	public Color getAxisLabelColor() {
		return axisLabelColor;
	}

	public void setAxisLabelColor(final Color axisLabelColor) {
		this.axisLabelColor = axisLabelColor;
	}

	public int getAxisLabelDecimals() {
		return axisLabelDecimals;
	}

	public void setAxisLabelDecimals(final int axisLabelDecimals) {
		this.axisLabelDecimals = axisLabelDecimals;
	}

}
