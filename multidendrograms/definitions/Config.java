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

import java.util.Hashtable;

import javax.swing.JInternalFrame;

import multidendrograms.data.DataFile;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.MethodName;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.SimilarityType;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Class that stores all the settings defined at the user GUI
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Config {

	private DistancesMatrix distMatrix = null;
	private Hashtable<Integer, String> names;
	private final SettingsInfo settings;
	private JInternalFrame dendroFrame;
	private DataFile dataFile;
	private final double radius = 5.0;

	public Config(final SettingsInfo settings) {
		this.settings = settings;
	}

	public void setDendrogramFrame(final JInternalFrame dendroFrame) {
		this.dendroFrame = dendroFrame;
	}

	public JInternalFrame getDendrogramFrame() {
		return this.dendroFrame;
	}

	public SettingsInfo getConfigMenu() {
		return this.settings;
	}

	public void setDataFile(final DataFile dataFile) {
		this.dataFile = new DataFile(dataFile);
	}

	public DataFile getDataFile() {
		return this.dataFile;
	}

	public SimilarityType getSimilarityType() {
		return this.settings.getSimilarityType();
	}

	public boolean isDistance() {
		return this.settings.getSimilarityType().equals(SimilarityType.DISTANCE);
	}

	public MethodName getMethod() {
		return this.settings.getMethod();
	}

	public int getPrecision() {
		return this.settings.getPrecision();
	}

	public void setDistancesMatrix(final DistancesMatrix distMatrix) {
		this.distMatrix = distMatrix;
	}

	public DistancesMatrix getDistancesMatrix() {
		return this.distMatrix;
	}

	public Cluster getRoot() {
		return this.distMatrix.getRoot();
	}

	public DendrogramOrientation getDendrogramOrientation() {
		return this.settings.getDendrogramOrientation();
	}

	public void setDendrogramOrientation(final DendrogramOrientation orientation) {
		this.settings.setDendrogramOrientation(orientation);
	}

	public LabelOrientation getNodeNameOrientation() {
		return this.settings.getNodeNameOrientation();
	}

	public double getRadius() {
		return this.radius;
	}

	public double getAxisMinValue() {
		return this.settings.getAxisMinValue();
	}

	public double getAxisMaxValue() {
		return this.settings.getAxisMaxValue();
	}

	public double getAxisIncrement() {
		return this.settings.getAxisIncrement();
	}

	public int getAxisTicks() {
		return this.settings.getAxisTicks();
	}

	public int getAxisLabelDecimals() {
		return this.settings.getAxisLabelDecimals();
	}

	public Hashtable<Integer, String> getNames() {
		return this.names;
	}

	public void setNames(final Hashtable<Integer, String> names) {
		this.names = names;
	}

}
