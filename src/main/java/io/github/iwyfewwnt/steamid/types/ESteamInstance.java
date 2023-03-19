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

import io.github.iwyfewwnt.steamid.utils.USteamInstance;
import io.github.iwyfewwnt.uwutils.*;

import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * A Steam account instance type enums.
 *
 * <p>Wraps {@link USteamInstance}.
 */
@SuppressWarnings("unused")
public enum ESteamInstance implements Serializable {

	/**
	 * An account instance type enum - All.
	 *
	 * <p>Wraps {@link USteamInstance#ALL}.
	 */
	ALL(USteamInstance.ALL),

	/**
	 * An account instance type enum - Desktop.
	 *
	 * <p>Wraps {@link USteamInstance#DESKTOP}.
	 */
	DESKTOP(USteamInstance.DESKTOP),

	/**
	 * An account instance type enum - Console.
	 *
	 * <p>Wraps {@link USteamInstance#CONSOLE}.
	 */
	CONSOLE(USteamInstance.CONSOLE),

	/**
	 * An account instance type enum - Web.
	 *
	 * <p>Wraps {@link USteamInstance#WEB}.
	 */
	WEB(USteamInstance.WEB),

	/**
	 * A chat flag enum - Clan.
	 *
	 * <p>Wraps {@link USteamInstance#CLAN}.
	 */
	CLAN(USteamInstance.CLAN),

	/**
	 * A chat flag enum - Lobby.
	 *
	 * <p>Wraps {@link USteamInstance#LOBBY}.
	 */
	LOBBY(USteamInstance.LOBBY),

	/**
	 * A chat flag enum - Matchmaking Lobby.
	 *
	 * <p>Wraps {@link USteamInstance#MM_LOBBY}.
	 */
	MM_LOBBY(USteamInstance.MM_LOBBY);

	/**
	 * A minimum account instance type according to its utility alternative.
	 *
	 * @see USteamInstance#MIN
	 */
	public static final ESteamInstance MIN = ALL;

	/**
	 * A maximum account instance type according to its utility alternative.
	 *
	 * @see USteamInstance#MAX
	 */
	public static final ESteamInstance MAX = WEB;

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = ESteamInstance.class.getSimpleName();

	/**
	 * An array of {@link ESteamInstance} instances.
	 */
	private static final ESteamInstance[] VALUES = UwEnum.values(ESteamInstance.class);

	/**
	 * A map of {@link ESteamInstance} instances by their identifier field.
	 */
	private static final Map<Integer, ESteamInstance> MAP_BY_ID = UwMap.newMapByFieldOrNull(
			entry -> entry.id, ESteamInstance.class
	);

	/**
	 * An account instance type identifier.
	 */
	private final int id;

	/**
	 * Initialize a {@link ESteamInstance} instance.
	 *
	 * @param id	account instance type identifier
	 */
	ESteamInstance(int id) {
		this.id = id;
	}

	/**
	 * Get this account instance type identifeir.
	 *
	 * @return	account instance type identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringJoiner(", ", SIMPLE_NAME + "[", "]")
				.add("id=" + this.id)
				.toString();
	}

	/**
	 * Get the account instance type identifier from the provided {@link ESteamInstance} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamInstance} instance is {@code null}.
	 * </ul>
	 *
	 * @param instance		enum value of the account instance type from which get the identifier
	 * @param defaultValue	default value to return on failure
	 * @return				account instance type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamInstance instance, Integer defaultValue) {
		if (instance == null) {
			return defaultValue;
		}

		return instance.getId();
	}

	/**
	 * Get the account instance type identifier from the provided {@link ESteamInstance} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamInstance} instance is {@code null}.
	 * </ul>
	 *
	 * @param instance				enum value of the account instance type from which get the identifier
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @return						account instance type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamInstance instance, Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(getIdOrNull(instance), defaultValueSupplier);
	}

	/**
	 * Get the account instance type identifier from the provided {@link ESteamInstance} instance
	 * or return the {@value USteamInstance#ALL} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamInstance} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamInstance#getIdOrElse(ESteamInstance, Integer)}
	 * w/ {@link USteamInstance#ALL} as the default value.
	 *
	 * @param instance	enum value of the account instance type from which get the identifier
	 * @return			account instance type identifier or the {@value USteamInstance#ALL} value
	 */
	public static Integer getIdOrAll(ESteamInstance instance) {
		return getIdOrElse(instance, USteamInstance.ALL);
	}

	/**
	 * Get the account instance type identifier from the provided {@link ESteamInstance} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamInstance} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamInstance#getIdOrElse(ESteamInstance, Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @param instance	enum value of the account instance type from which get the identifier
	 * @return			account instance type identifier or {@code null}
	 */
	public static Integer getIdOrNull(ESteamInstance instance) {
		return getIdOrElse(instance, (Integer) null);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return a default value if failed.
	 *
	 * @param id			account instance type identifier of the instance
	 * @param defaultValue	default value to return of failure
	 * @return				associated {@link ESteamInstance} instance or the default value
	 */
	public static ESteamInstance fromIdOrElse(Integer id, ESteamInstance defaultValue) {
		return UwMap.getOrElse(id, MAP_BY_ID, defaultValue);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return a default value if failed.
	 *
	 * @param id					account instance type identifier of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamInstance} instance or the default value
	 */
	public static ESteamInstance fromIdOrElse(Integer id, Supplier<ESteamInstance> defaultValueSupplier) {
		return UwObject.getIfNull(fromIdOrNull(id), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return the {@link ESteamInstance#ALL} value if failed.
	 *
	 * <p>Wraps {@link ESteamInstance#fromIdOrElse(Integer, ESteamInstance)}
	 * w/ {@link ESteamInstance#ALL} as the default value.
	 *
	 * @param id			account instance type identifier of the instance
	 * @return				associated {@link ESteamInstance} instance or the {@link ESteamInstance#ALL} value
	 */
	public static ESteamInstance fromIdOrAll(Integer id) {
		return fromIdOrElse(id, ALL);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamInstance#fromIdOrElse(Integer, ESteamInstance)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id			account instance type identifier of the instance
	 * @return				associated {@link ESteamInstance} instance or {@code null}
	 */
	public static ESteamInstance fromIdOrNull(Integer id) {
		return fromIdOrElse(id, (ESteamInstance) null);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return of failure
	 * @return				associated {@link ESteamInstance} instance or the default value
	 */
	public static ESteamInstance fromIndexOrElse(Integer index, ESteamInstance defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	suppleir from which get the default value
	 * @return						associated {@link ESteamInstance} instance or the defualt value
	 */
	public static ESteamInstance fromIndexOrElse(Integer index, Supplier<ESteamInstance> defaultValueSupplier) {
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return the {@link ESteamInstance#ALL} value if failed.
	 *
	 * <p>Wraps {@link ESteamInstance#fromIndexOrElse(Integer, ESteamInstance)}
	 * w/ {@link ESteamInstance#ALL} as the default value.
	 *
	 * @param index			index of the instance
	 * @return				associated {@link ESteamInstance} instance or the {@link ESteamInstance#ALL} value
	 */
	public static ESteamInstance fromIndexOrAll(Integer index) {
		return fromIndexOrElse(index, ALL);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamInstance#fromIndexOrElse(Integer, ESteamInstance)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index			index of the instance
	 * @return				associated {@link ESteamInstance} instance or {@code null}
	 */
	public static ESteamInstance fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamInstance) null);
	}
}
