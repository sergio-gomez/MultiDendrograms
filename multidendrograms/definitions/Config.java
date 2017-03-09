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

import javax.swing.JInternalFrame;

import multidendrograms.core.clusterings.HierarchicalClustering;
import multidendrograms.core.definitions.Dendrogram;
import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;
import multidendrograms.types.DendrogramOrientation;
import multidendrograms.types.LabelOrientation;
import multidendrograms.types.MethodType;
import multidendrograms.types.ProximityType;

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

	private HierarchicalClustering clustering = null;
	private ExternalData externalData;
	private SettingsInfo settings;
	private JInternalFrame dendroFrame;
	private DataFile dataFile;

	public Config(final SettingsInfo settings) {
		this.settings = settings;
	}

	public void setExternalData(ExternalData externalData) {
		this.externalData = externalData;
	}

	public ExternalData getExternalData() {
		return this.externalData;
	}

	public String[] getNames() {
		return this.externalData.getNames();
	}

	public SettingsInfo getSettingsInfo() {
		return this.settings;
	}

	public void setDendrogramFrame(final JInternalFrame dendroFrame) {
		this.dendroFrame = dendroFrame;
	}

	public JInternalFrame getDendrogramFrame() {
		return this.dendroFrame;
	}

	public void setDataFile(final DataFile dataFile) {
		this.dataFile = new DataFile(dataFile);
	}

	public DataFile getDataFile() {
		return this.dataFile;
	}

	public ProximityType getProximityType() {
		return this.settings.getProximityType();
	}

	public boolean isDistance() {
		return this.settings.getProximityType().equals(ProximityType.DISTANCE);
	}

	public int getPrecision() {
		return this.settings.getPrecision();
	}

	public MethodType getMethod() {
		return this.settings.getMethod();
	}

	public double getMethodParameter() {
		return this.settings.getMethodParameter();
	}

	public boolean isWeighted() {
		return this.settings.isWeighted();
	}

	public void setHierarchicalClustering(final HierarchicalClustering clustering) {
		this.clustering = clustering;
	}

	public HierarchicalClustering getHierarchicalClustering() {
		return this.clustering;
	}

	public Dendrogram getRoot() {
		return this.clustering.getRoot();
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
		return 5.0;
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

}
