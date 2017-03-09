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

import multidendrograms.forms.scrollabledesktop.BaseInternalFrame;
import multidendrograms.methods.Method;
import multidendrograms.types.MethodName;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Internal frames
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DendrogramFrame extends BaseInternalFrame {

	private static final long serialVersionUID = 1L;
	public static int openFrameCount = 0;

	private DendrogramParameters dendroParams;

	public DendrogramFrame(MethodName method, boolean isUpdate) {

		super("", true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable

		setTitle(Method.toName(method));

		if (!isUpdate)
			openFrameCount++;
	}

	public void setDendrogramParameters(final DendrogramParameters dendroParams) {
		this.dendroParams = dendroParams;
	}

	public DendrogramParameters getDendrogramParameters() {
		return dendroParams;
	}

}
