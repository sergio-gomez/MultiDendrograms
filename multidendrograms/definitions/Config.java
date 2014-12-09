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

import multidendrograms.initial.LogManager;

import multidendrograms.data.DataFile;
import multidendrograms.data.ExternalData;

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

	private DistancesMatrix dm = null;
	private Hashtable<Integer, String> names;
	private final SettingsInfo settings;
	private JInternalFrame dendroFrame;
	private DataFile dataFile;
	private double radius = 5.0;
	private double maxValOrigin = 0;

	public Config(final SettingsInfo settings) {
		this.settings = settings;
	}

	public void setDendrogramFrame(final JInternalFrame dendro) {
		this.dendroFrame = dendro;
	}

	public JInternalFrame getDendrogramFrame() {
		return dendroFrame;
	}

	public SettingsInfo getConfigMenu() {
		return this.settings;
	}

	public void setDataFile(final DataFile df) {
		this.dataFile = new DataFile(df);
	}

	public DataFile getDataFile() {
		return dataFile;
	}

	public SimilarityType getSimilarityType() {
		return settings.getSimilarityType();
	}

	public boolean isDistance() {
		return settings.getSimilarityType().equals(SimilarityType.DISTANCE);
	}

	public MethodName getMethod() {
		return settings.getMethod();
	}

	public int getPrecision() {
		return settings.getPrecision();
	}

	public void setDistancesMatrix(final DistancesMatrix dm) {
		this.dm = dm;
		if ((dm != null) && (!isDistance()) && (maxValOrigin == 0)) {
			maxValOrigin = dm.maxValue();
		}
	}

	public DistancesMatrix getDistancesMatrix() {
		return dm;
	}

	public DistancesMatrix readDistancesMatrix() {
		DistancesMatrix dm = null;
		ExternalData ed;

		try {
			ed = new ExternalData(dataFile, false);
			dm = ed.getDistancesMatrix();
		} catch (Exception e) {
			LogManager.LOG.throwing("Config", "getMatriuDistancies", e);
		}
		return dm;
	}

	public DendrogramOrientation getDendrogramOrientation() {
		return settings.getDendrogramOrientation();
	}

	public void setOrientacioDendo(DendrogramOrientation or) {
		settings.setDendrogramOrientation(or);
	}

	public LabelOrientation getNodeNameOrientation() {
		return settings.getNodeNameOrientation();
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(final double radius) {
		this.radius = radius;
	}

	public double getAxisMaxVal() {
		return settings.getAxisMaxVal();
	}

	public double getAxisMinVal() {
		return settings.getAxisMinVal();
	}

	public double getTop() {
		if ((dm != null) && (this.isDistance())) {
			return dm.getRoot().getTop();
		} else {
			return maxValOrigin;
		}
	}

	public double getBase() {
		if (!this.isDistance()) {
			return dm.getRoot().getBase();
		} else {
			return maxValOrigin;
		}
	}

	public double getAxisIncrement() {
		return settings.getAxisIncrement();
	}

	public int getAxisTicks() {
		return settings.getAxisTicks();
	}

	public int getAxisLabelDecimals() {
		return settings.getAxisLabelDecimals();
	}

	public Hashtable<Integer, String> getNames() {
		return names;
	}

	public void setNames(final Hashtable<Integer, String> names) {
		this.names = names;
	}

}
