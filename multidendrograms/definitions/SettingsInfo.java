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

import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.MethodName;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.SimilarityType;

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
	private SimilarityType simType = SimilarityType.DISTANCE;

	// METHOD
	private MethodName method = MethodName.UNWEIGHTED_AVERAGE;

	// PRECISION
	private int precision = 0;

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
		return this.bandVisible;
	}

	public void setBandVisible(final boolean bandVisible) {
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

	public void setAxisTicks(final int ticks) {
		this.axisTicks = ticks;
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

	@Override
	public String toString() {
		String str;
		try {
			str = "/// DATA  ///\n";
			str += "Data type: " + this.getSimilarityType();
			str += "\nMethod: " + this.getMethod();
			str += "\nPrecision: " + this.getPrecision();
			str += "\n\n/// TREE  ///\n";
			str += "\nDendrogram orientation: " + this.getDendrogramOrientation();
			str += "\nBand: " + this.isBandVisible();
			str += "\n\n/// NAMES ///\n";
			str += "\nNode name: " + this.isNodeNameVisible();
			str += "\nNode radius: " + this.getNodeRadius();
			str += "\nNode name orientation: " + this.getNodeNameOrientation();
			str += "\nNode name font: " + this.getNodeNameFont();
			str += "\nNode name color: " + this.getNodeNameColor();
			str += "\n\n/// AXIS  ///\n";
			str += "\nAxis: " + this.isAxisVisible();
			str += "\nAxis color" + this.getAxisColor();
			str += "\nAxis minimum value: " + Double.toString(this.getAxisMinVal());
			str += "\nAxis maximum value: " + Double.toString(this.getAxisMaxVal());
			str += "\nAxis increment: " + Double.toString(this.getAxisIncrement());
			str += "\nAxis ticks: " + Double.toString(this.getAxisTicks());
			str += "\nAxis label: " + this.isAxisLabelVisible();
			str += "\nAxis label font: " + this.getAxisLabelFont();
			str += "\nAxis label color" + this.getAxisLabelColor();
			str += "\nAxis label decimals: " + Integer.toString(this.getAxisLabelDecimals());
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

}
