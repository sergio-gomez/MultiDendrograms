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

import java.util.HashMap;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Assigns numbers to identifiers (0, 1, 2, 3, ...)
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class IdToKey<T> {

	private Integer nextId = 0;
	private final HashMap<T, Integer> keyToInd;

	public IdToKey() {
		keyToInd = new HashMap<T, Integer>();
	}

	public boolean containsKey(final T key) {
		return keyToInd.containsKey(key);
	}

	public Integer getIndex(final T key) {
		return keyToInd.get(key);
	}

	public Integer setIndex(final T key) {
		keyToInd.put(key, nextId);
		nextId++;
		return (nextId - 1);
	}

	public int size() {
		return nextId;
	}

}
