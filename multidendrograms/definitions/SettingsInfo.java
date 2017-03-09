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

package multidendrograms.definitions;

import java.awt.Color;
import java.awt.Font;

import multidendrograms.core.utils.MathUtils;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.MethodType;
import multidendrograms.types.OriginType;
import multidendrograms.types.ProximityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Settings available in the user interface
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class SettingsInfo {

	// DATA
	private ProximityType proximityType = ProximityType.DISTANCE;

	// PRECISION
	private int precision = 0;

	// METHOD
	private MethodType method = MethodType.ARITHMETIC_LINKAGE;
	private double methodParameter = 0.0;
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
	private OriginType originType = OriginType.UNIFORM_ORIGIN;

	// AXIS
	private boolean axisVisible = true;
	private Color axisColor = Color.BLACK;
	private double axisMinValue = 0.0;
	private double axisMaxValue = 1.0;
	private double axisIncrement = 0.1;
	private int axisTicks = 10;
	private boolean axisLabelVisible = true;
	private Font axisLabelFont;
	private Color axisLabelColor = Color.BLACK;
	private int axisLabelDecimals = 0;

	public ProximityType getProximityType() {
		return this.proximityType;
	}

	public void setSimilarityType(ProximityType proximityType) {
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

	public DendrogramOrientation getDendrogramAdaptedOrientation() {
		if (this.proximityType.equals(ProximityType.DISTANCE)) {
			return this.dendroOrientation;
		} else {// (this.proximityType.equals(ProximityType.SIMILARITY))
			if (this.dendroOrientation.equals(DendrogramOrientation.NORTH)) {
				return DendrogramOrientation.SOUTH;
			} else if (this.dendroOrientation.equals(DendrogramOrientation.SOUTH)) {
				return DendrogramOrientation.NORTH;
			} else if (this.dendroOrientation.equals(DendrogramOrientation.EAST)) {
				return DendrogramOrientation.WEST;
			} else {// (dendroOrientation.equals(DendrogramOrientation.WEST))
				return DendrogramOrientation.EAST;
			}
		}
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

	public OriginType getOriginType() {
		return this.originType;
	}

	public void setOriginType(OriginType originType) {
		this.originType = originType;
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

	public double getAxisMinValue() {
		return this.axisMinValue;
	}

	public void setAxisMinValue(double axisMinValue) {
		this.axisMinValue = axisMinValue;
	}

	public double getAxisMaxValue() {
		return this.axisMaxValue;
	}

	public void setAxisMaxValue(double axisMaxValue) {
		this.axisMaxValue = axisMaxValue;
	}

	public double getAxisIncrement() {
		return this.axisIncrement;
	}

	public void setAxisIncrement(double axisIncrement) {
		this.axisIncrement = axisIncrement;
	}

	public int getAxisNumberOfTicks() {
		double axisNumTicks = (this.axisMaxValue - this.axisMinValue) / this.axisIncrement;
		axisNumTicks = MathUtils.round(axisNumTicks, 6);
		return (1 + (int)(axisNumTicks));
	}

	public int getAxisTicks() {
		return this.axisTicks;
	}

	public void setAxisTicks(int ticks) {
		this.axisTicks = ticks;
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

	@Override
	public String toString() {
		String str;
		try {
			str = "/// DATA  ///\n";
			str += "Data type: " + getProximityType();
			str += "\nPrecision: " + getPrecision();
			str += "\nMethod: " + getMethod();
			str += "\nWeighted: " + isWeighted();
			str += "\n\n/// TREE  ///\n";
			str += "\nDendrogram orientation: " + getDendrogramOrientation();
			str += "\nBand: " + isBandVisible();
			str += "\n\n/// NAMES ///\n";
			str += "\nNode name: " + isNodeNameVisible();
			str += "\nNode radius: " + getNodeRadius();
			str += "\nNode name orientation: " + getNodeNameOrientation();
			str += "\nNode name font: " + getNodeNameFont();
			str += "\nNode name color: " + getNodeNameColor();
			str += "\n\n/// AXIS  ///\n";
			str += "\nAxis: " + isAxisVisible();
			str += "\nAxis color" + getAxisColor();
			str += "\nAxis minimum value: " + Double.toString(getAxisMinValue());
			str += "\nAxis maximum value: " + Double.toString(getAxisMaxValue());
			str += "\nAxis increment: " + Double.toString(getAxisIncrement());
			str += "\nAxis ticks: " + Double.toString(getAxisTicks());
			str += "\nAxis label: " + isAxisLabelVisible();
			str += "\nAxis label font: " + getAxisLabelFont();
			str += "\nAxis label color" + getAxisLabelColor();
			str += "\nAxis label decimals: " + Integer.toString(getAxisLabelDecimals());
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

}
