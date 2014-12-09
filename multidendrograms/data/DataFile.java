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

package multidendrograms.data;

import java.io.File;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Stores name and path of a file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DataFile {

	private String name = "";
	private String path = "";
	private String nameNoExt = "";

	public DataFile(final String fileName) {
		File f = new File(fileName);
		f = f.getAbsoluteFile();
		this.name = f.getName();
		this.path = f.getParent();
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		this.nameNoExt = getFileNameWithoutExtension(name);
	}

	public DataFile(final String name, final String path) {
		this.name = name;
		this.path = path;
		this.nameNoExt = getFileNameWithoutExtension(name);
	}

	public DataFile(final DataFile fd) {
		this.name = fd.name;
		this.path = fd.path;
		this.nameNoExt = fd.nameNoExt;
	}

	public DataFile() {
	};

	private String getFileNameWithoutExtension(String fileName) {
		int whereDot = fileName.lastIndexOf('.');
		if (0 < whereDot && whereDot <= fileName.length() - 2) {
			return fileName.substring(0, whereDot);
		}
		return "";
	}

	public String getName() {
		return name;
	}

	public String getNameNoExt() {
		return nameNoExt;
	}

	public void setName(final String name) {
		this.name = name;
		this.nameNoExt = getFileNameWithoutExtension(name);
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getPathName() {
		return path + name;
	}

	public String getPathNameNoExt() {
		return path + nameNoExt;
	}

}