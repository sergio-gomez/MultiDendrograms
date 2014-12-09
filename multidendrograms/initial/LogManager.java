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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Log manager
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class LogManager {
	public enum LogType {
		TXT, XML;
	}

	public static final Logger LOG = Logger.getLogger("Dendro");

	public LogManager(String sPath, LogType type) {
		FileHandler fh;

		try {
			fh = new FileHandler(sPath);
			LOG.addHandler(fh);
			if (type.equals(LogType.TXT)) {
				SimpleFormatter formatter;
				formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
			} else {
				XMLFormatter formatter;
				formatter = new XMLFormatter();
				fh.setFormatter(formatter);
			}
			LOG.setLevel(Level.CONFIG);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOG.config("Log started");
	}

	public static void setLevel(Level l) {
		LOG.setLevel(l);
	}
}
