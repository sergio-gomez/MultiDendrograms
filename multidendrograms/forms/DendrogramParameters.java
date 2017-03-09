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

import multidendrograms.core.clusterings.HierarchicalClustering;
import multidendrograms.data.ExternalData;
import multidendrograms.dendrogram.UltrametricMatrix;
import multidendrograms.forms.panels.SettingsPanel;
import multidendrograms.initial.Language;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.MethodType;
import multidendrograms.types.ProximityType;

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
	private ExternalData externalData;

	// MultiDendrogram
	private HierarchicalClustering clustering = null;
	private UltrametricMatrix ultraMatrix = null;

	// Settings on type of proximity, precision and method
	private ProximityType proximityType;
	private int precision;
	private MethodType method;
	private double methodParameter;
	private boolean weighted = false;

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
	private boolean uniformOrigin = true;

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

	public DendrogramParameters(ExternalData externalData, HierarchicalClustering clustering) {
		try {
			this.externalData = externalData;
			this.clustering = clustering;
			SettingsPanel.getConfigPanel(this);
		} catch (Exception e) {
			showError("Error: \n" + e.toString());
		}
	}

	public HierarchicalClustering getHierarchicalClustering() {
		return this.clustering;
	}

	public UltrametricMatrix getUltrametricMatrix() {
		return this.ultraMatrix;
	}

	public void setUltrametricMatrix(UltrametricMatrix ultraMatrix) {
		this.ultraMatrix = ultraMatrix;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(null, message, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}

	public ExternalData getExternalData() {
		return this.externalData;
	}

	public ProximityType getProximityType() {
		return this.proximityType;
	}

	public void setProximityType(ProximityType proximityType) {
		this.proximityType = proximityType;
	}

	public int getPrecision() {
		return this.precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public MethodType getMethod() {
		return this.method;
	}

	public void setMethod(MethodType method) {
		this.method = method;
	}

	public double getMethodParameter() {
		return this.methodParameter;
	}

	public void setMethodParameter(double methodParameter) {
		this.methodParameter = methodParameter;
	}

	public boolean isWeighted() {
		return this.weighted;
	}

	public void setWeighted(boolean weighted) {
		this.weighted = weighted;
	}

	public DendrogramOrientation getDendrogramOrientation() {
		return this.dendroOrientation;
	}

	public void setDendrogramOrientation(DendrogramOrientation dendroOrientation) {
		this.dendroOrientation = dendroOrientation;
	}

	public boolean isBandVisible() {
		return this.bandVisible;
	}

	public void setBandVisible(boolean bandVisible) {
		this.bandVisible = bandVisible;
	}

	public Color getBandColor() {
		return this.bandColor;
	}

	public void setBandColor(Color bandColor) {
		this.bandColor = bandColor;
	}

	public int getNodeRadius() {
		return this.nodeRadius;
	}

	public void setNodeRadius(int nodeRadius) {
		this.nodeRadius = nodeRadius;
	}

	public boolean isNodeNameVisible() {
		return this.nodeNameVisible;
	}

	public void setNodeNameVisible(boolean nodeNameVisible) {
		this.nodeNameVisible = nodeNameVisible;
	}

	public Font getNodeNameFont() {
		return this.nodeNameFont;
	}

	public void setNodeNameFont(Font nodeNameFont) {
		this.nodeNameFont = nodeNameFont;
	}

	public Color getNodeNameColor() {
		return this.nodeNameColor;
	}

	public void setNodeNameColor(Color nodeNameColor) {
		this.nodeNameColor = nodeNameColor;
	}

	public LabelOrientation getNodeNameOrientation() {
		return this.nodeNameOrientation;
	}

	public void setNodeNameOrientation(LabelOrientation nodeNameOrientation) {
		this.nodeNameOrientation = nodeNameOrientation;
	}

	public boolean isUniformOrigin() {
		return this.uniformOrigin;
	}

	public void setUniformOrigin(boolean uniformOrigin) {
		this.uniformOrigin = uniformOrigin;
	}

	public boolean isAxisVisible() {
		return this.axisVisible;
	}

	public void setAxisVisible(boolean axisVisible) {
		this.axisVisible = axisVisible;
	}

	public Color getAxisColor() {
		return this.axisColor;
	}

	public void setAxisColor(Color axisColor) {
		this.axisColor = axisColor;
	}

	public double getAxisMinVal() {
		return this.axisMinVal;
	}

	public void setAxisMinVal(double axisMinVal) {
		this.axisMinVal = axisMinVal;
	}

	public double getAxisMaxVal() {
		return this.axisMaxVal;
	}

	public void setAxisMaxVal(double axisMaxVal) {
		this.axisMaxVal = axisMaxVal;
	}

	public double getAxisIncrement() {
		return this.axisIncrement;
	}

	public void setAxisIncrement(double axisIncrement) {
		this.axisIncrement = axisIncrement;
	}

	public int getAxisTicks() {
		return this.axisTicks;
	}

	public void setAxisTicks(int axisTicks) {
		this.axisTicks = axisTicks;
	}

	public boolean isAxisLabelVisible() {
		return this.axisLabelVisible;
	}

	public void setAxisLabelVisible(boolean axisLabelVisible) {
		this.axisLabelVisible = axisLabelVisible;
	}

	public Font getAxisLabelFont() {
		return this.axisLabelFont;
	}

	public void setAxisLabelFont(Font axisLabelFont) {
		this.axisLabelFont = axisLabelFont;
	}

	public Color getAxisLabelColor() {
		return this.axisLabelColor;
	}

	public void setAxisLabelColor(Color axisLabelColor) {
		this.axisLabelColor = axisLabelColor;
	}

	public int getAxisLabelDecimals() {
		return this.axisLabelDecimals;
	}

	public void setAxisLabelDecimals(int axisLabelDecimals) {
		this.axisLabelDecimals = axisLabelDecimals;
	}

}
