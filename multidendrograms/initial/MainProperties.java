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

package multidendrograms.initial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Properties;

import multidendrograms.errors.PropertiesError;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Reads configuration file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class MainProperties {

	private static HashMap<Object, Object> properties;

	public MainProperties(final String configurationFile) throws Exception {
		LogManager.LOG.info("Created instance of MainProperties");

		try {
			final FileInputStream f = new FileInputStream(configurationFile);
			final Properties props = new Properties();
			props.load(f);
			f.close();
			MainProperties.properties = new HashMap<Object, Object>(props);
		} catch (final FileNotFoundException e) {
			LogManager.LOG.warning("Init file not found: " + e);
			throw new FileNotFoundException("Init file not found");
		} catch (final Exception e) {
			String msg_err = "ERROR making properties file " + "\n"
					+ e.getStackTrace();
			LogManager.LOG
					.throwing("MainProperties", "MainProperties()", e);
			throw new Exception(msg_err);
		}
	}

	public static String getProperty(final String name) throws PropertiesError {
		final String valor = (String) MainProperties.properties.get(name);
		if (valor == null) {
			String msg_err = Language.getLabel(66) + " " + name;
			LogManager.LOG.warning(msg_err);
			throw new PropertiesError(msg_err);
		}
		return valor;
	}
}