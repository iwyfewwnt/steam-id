/*
 * Copyright 2023 iwyfewwnt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.iwyfewwnt.steamid.types;

import io.github.iwyfewwnt.steamid.utils.USteamUniverse;
import io.github.iwyfewwnt.uwutils.UwArray;
import io.github.iwyfewwnt.uwutils.UwEnum;
import io.github.iwyfewwnt.uwutils.UwMap;
import io.github.iwyfewwnt.uwutils.UwObject;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam account universe type enums.
 *
 * <p>Wraps {@link USteamUniverse}.
 */
@SuppressWarnings("unused")
public enum ESteamUniverse implements Serializable {

	/**
	 * An account universe type enum - Invalid.
	 *
	 * <p>Wraps {@link USteamUniverse#INVALID}.
	 */
	INVALID(USteamUniverse.INVALID),

	/**
	 * An account universe type enum - Public.
	 *
	 * <p>Wraps {@link USteamUniverse#PUBLIC}.
	 */
	PUBLIC(USteamUniverse.PUBLIC),

	/**
	 * An account universe type enum - Beta.
	 *
	 * <p>wraps {@link USteamUniverse#BETA}.
	 */
	BETA(USteamUniverse.BETA),

	/**
	 * An account universe type enum - Internal.
	 *
	 * <p>Wraps {@link USteamUniverse#INTERNAL}.
	 */
	INTERNAL(USteamUniverse.INTERNAL),

	/**
	 * An account universe type enum - Dev.
	 *
	 * <p>Wraps {@link USteamUniverse#DEV}.
	 */
	DEV(USteamUniverse.DEV),

	/**
	 * An account universe type enum - RC.
	 *
	 * <p>Wraps {@link USteamUniverse#RC}.
	 */
	RC(USteamUniverse.RC);

	/**
	 * A base account universe type according to its utility alternative.
	 *
	 * @see USteamUniverse#BASE
	 */
	public static final ESteamUniverse BASE = INVALID;

	/**
	 * A minimum account universe type according to its utility alternative.
	 *
	 * @see USteamUniverse#MIN
	 */
	public static final ESteamUniverse MIN = PUBLIC;

	/**
	 * A maximum account universe type according ot its utility alternative.
	 *
	 * @see USteamUniverse#MAX
	 */
	public static final ESteamUniverse MAX = RC;

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = ESteamUniverse.class.getSimpleName();

	/**
	 * An array of {@link ESteamUniverse} instances.
	 */
	private static final ESteamUniverse[] VALUES = UwEnum.values(ESteamUniverse.class);

	/**
	 * A map of {@link ESteamUniverse} instances by their identifier field.
	 */
	private static final Map<Integer, ESteamUniverse> MAP_BY_ID = UwMap.newMapByFieldOrNull(
			entry -> entry.id, ESteamUniverse.class
	);

	/**
	 * An account universe type identifier.
	 */
	private final int id;

	/**
	 * Initialize a {@link ESteamUniverse} instance.
	 *
	 * @param id	account universe type identifier
	 */
	ESteamUniverse(int id) {
		this.id = id;
	}

	/**
	 * Get this account universe type identifier.
	 *
	 * @return	account universe type identifeir
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return SIMPLE_NAME + "::" + this.name() + "["
				+ "id=" + this.id
				+ "]";
	}

	/**
	 * Get the account universe type identifier from the provided {@link ESteamUniverse} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUniverse} instance is {@code null}.
	 * </ul>
	 *
	 * @param universe		enum value of the account universe type from which get the identifier
	 * @param defaultValue 	default value to return on failure
	 * @return				account universe type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamUniverse universe, Integer defaultValue) {
		if (universe == null) {
			return defaultValue;
		}

		return universe.getId();
	}

	/**
	 * Get the account universe type identifier from the provided {@link ESteamUniverse} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUniverse} instance is {@code null}.
	 * </ul>
	 *
	 * @param universe				enum value of the account universe type from which get the identifier
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @return						account universe type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamUniverse universe, Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(getIdOrNull(universe), defaultValueSupplier);
	}

	/**
	 * Get the account universe type identifier from the provided {@link ESteamUniverse} instance
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUniverse} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamUniverse#getIdOrElse(ESteamUniverse, Integer)}
	 * w/ {@link USteamUniverse#BASE} as the default value.
	 *
	 * @param universe	enum value of the account universe type from which get the identifier
	 * @return			account universe type identifier or the {@link USteamUniverse#BASE} value
	 */
	public static Integer getIdOrBase(ESteamUniverse universe) {
		return getIdOrElse(universe, USteamUniverse.BASE);
	}

	/**
	 * Get the account universe type identifier from the provided {@link ESteamUniverse} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUniverse} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamUniverse#getIdOrElse(ESteamUniverse, Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @param universe	enum value of the account universe type from which get the identifier
	 * @return			account universe type identifier or {@code null}
	 */
	public static Integer getIdOrNull(ESteamUniverse universe) {
		return getIdOrElse(universe, (Integer) null);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its account universe type identifier
	 * or return a default value if failed.
	 *
	 * @param id			account universe type identifier of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamUniverse} instance or the default value
	 */
	public static ESteamUniverse fromIdOrElse(Integer id, ESteamUniverse defaultValue) {
		return UwMap.getOrElse(id, MAP_BY_ID, defaultValue);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its account universe type identifier
	 * or return a default value if failed.
	 *
	 * @param id					account universe type identifier of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamUniverse} instance or the default value
	 */
	public static ESteamUniverse fromIdOrElse(Integer id, Supplier<ESteamUniverse> defaultValueSupplier) {
		return UwObject.getIfNull(fromIdOrNull(id), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its account universe type identifier
	 * or return the base value if failed.
	 *
	 * <p>Wraps {@link ESteamUniverse#fromIdOrElse(Integer, ESteamUniverse)}
	 * w/ {@link ESteamUniverse#BASE} as the default value.
	 *
	 * @param id	account universe type identifier of the instance
	 * @return		associated {@link ESteamUniverse} instance or the base value
	 */
	public static ESteamUniverse fromIdOrBase(Integer id) {
		return fromIdOrElse(id, BASE);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its account universe type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamUniverse#fromIdOrElse(Integer, ESteamUniverse)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id	account universe type identifier of the instance
	 * @return		associated {@link ESteamUniverse} instance or {@code null}
	 */
	public static ESteamUniverse fromIdOrNull(Integer id) {
		return fromIdOrElse(id, (ESteamUniverse) null);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamUniverse} instance or the default value
	 */
	public static ESteamUniverse fromIndexOrElse(Integer index, ESteamUniverse defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamUniverse} instance or the default value
	 */
	public static ESteamUniverse fromIndexOrElse(Integer index, Supplier<ESteamUniverse> defaultValueSupplier) {
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its index
	 * or return the base value if failed.
	 *
	 * <p>Wraps {@link ESteamUniverse#fromIndexOrElse(Integer, ESteamUniverse)}
	 * w/ {@link ESteamUniverse#BASE} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamUniverse} instance or the base value
	 */
	public static ESteamUniverse fromIndexOrBase(Integer index) {
		return fromIndexOrElse(index, BASE);
	}

	/**
	 * Get an {@link ESteamUniverse} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamUniverse#fromIndexOrElse(Integer, ESteamUniverse)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamUniverse} instance or {@code null}
	 */
	public static ESteamUniverse fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamUniverse) null);
	}
}
