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

import io.github.iwyfewwnt.steamid.utils.USteamVanity;
import io.github.iwyfewwnt.uwutils.*;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam vanity URL type enums.
 *
 * <p>Wraps {@link USteamVanity}.
 */
@SuppressWarnings("unused")
public enum ESteamVanity {

	/**
	 * A vanity URL type enum - Individual.
	 *
	 * <p>Wraps {@link USteamVanity#INDIVIDUAL}.
	 */
	INDIVIDUAL(USteamVanity.INDIVIDUAL),

	/**
	 * A vanity URL type enum - Group.
	 *
	 * <p>Wraps {@link USteamVanity#GROUP}.
	 */
	GROUP(USteamVanity.GROUP),

	/**
	 * A vanity URL type enum - Game Group.
	 *
	 * <p>Wraps {@link USteamVanity#GAME_GROUP}.
	 */
	GAME_GROUP(USteamVanity.GAME_GROUP);

	/**
	 * A minimum vanity URL type according to its utility alternative.
	 *
	 * @see USteamVanity#MIN
	 */
	public static final ESteamVanity MIN = INDIVIDUAL;

	/**
	 * A maximum vanity URL type according to its utility alternative.
	 *
	 * @see USteamVanity#MAX
	 */
	public static final ESteamVanity MAX = GAME_GROUP;

	private static final Class<ESteamVanity> CLASS = ESteamVanity.class;

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = CLASS.getSimpleName();

	/**
	 * An array of {@link ESteamVanity} instances.
	 */
	private static final ESteamVanity[] VALUES = UwEnum.values(CLASS);

	/**
	 * A map of {@link ESteamVanity} instances by their identifier field.
	 */
	private static final Map<Integer, ESteamVanity> MAP_BY_ID = UwMap.createByFieldOrNull(
			entry -> entry.id, VALUES
	);

	/**
	 * A vanity URL type identifier.
	 */
	private final int id;

	/**
	 * A {@link #toString()} cache.
	 */
	private volatile String stringCache;

	/**
	 * A {@link #stringCache} mutex.
	 */
	private final Object stringCacheMutex;

	/**
	 * Initialize an {@link ESteamVanity} instance.
	 *
	 * @param id	vanity URL type identifier
	 */
	ESteamVanity(int id) {
		this.id = id;

		this.stringCacheMutex = new Object();
	}

	/**
	 * Get this vanity URL type identifier.
	 *
	 * @return	vanity URL type identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		//noinspection DuplicatedCode
		if (this.stringCache != null) {
			return this.stringCache;
		}

		synchronized (this.stringCacheMutex) {
			if (this.stringCache != null) {
				return this.stringCache;
			}

			return (this.stringCache = SIMPLE_NAME + "::" + this.name() + "["
					+ "id=" + this.id
					+ "]");
		}
	}

	/**
	 * Get the vanity URL type identifier from the provided {@link ESteamVanity} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamVanity} instance is {@code null}.
	 * </ul>
	 *
	 * @param vanity		enum value of the vanity URL type from which get the identifier
	 * @param defaultValue  default value to return on failure
	 * @return				vanity URL type identifier or the defualt value
	 */
	public static Integer getIdOrElse(ESteamVanity vanity, Integer defaultValue) {
		return UwObject.ifNotNull(vanity, ESteamVanity::getId, defaultValue);
	}

	/**
	 * Get the vanity URL type identifier from the provided {@link ESteamVanity} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamVanity} instance is {@code null}.
	 * </ul>
	 *
	 * @param vanity				enum value of the vanity URL type from which get the identifier
	 * @param defaultValueSupplier  supplier from which get the default value
	 * @return						vanity URL type identifier or the defualt value
	 */
	public static Integer getIdOrElse(ESteamVanity vanity, Supplier<Integer> defaultValueSupplier) {
		return UwObject.ifNull(getIdOrNull(vanity), defaultValueSupplier);
	}

	/**
	 * Get the vanity URL type identifier from the provided {@link ESteamVanity} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamVanity} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getIdOrElse(ESteamVanity, Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @param vanity	enum value of the vanity URL type from which get the identifier
	 * @return			vanity URL type identifier or {@code null}
	 */
	public static Integer getIdOrNull(ESteamVanity vanity) {
		return getIdOrElse(vanity, (Integer) null);
	}

	/**
	 * Get an {@link ESteamVanity} instance by its vanity URL type identifier
	 * or return a default value if failed.
	 *
	 * @param id			vanity URL type identifier of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamVanity} instance or the default value
	 */
	public static ESteamVanity fromIdOrElse(Integer id, ESteamVanity defaultValue) {
		return UwMap.getOrElse(id, MAP_BY_ID, defaultValue);
	}

	/**
	 * Get an {@link ESteamVanity} instance by its vanity URL type identifier
	 * or return a default value if failed.
	 *
	 * @param id					vanity URL type identifier of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamVanity} instance or the defualt value
	 */
	public static ESteamVanity fromIdOrElse(Integer id, Supplier<ESteamVanity> defaultValueSupplier) {
		return UwObject.ifNull(fromIdOrNull(id), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamVanity} instance by its vanity URL type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromIdOrElse(Integer, ESteamVanity)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id	vanity URL type identifier of the instance
	 * @return		associated {@link ESteamVanity} instance or {@code null}
	 */
	public static ESteamVanity fromIdOrNull(Integer id) {
		return fromIdOrElse(id, (ESteamVanity) null);
	}

	/**
	 * Get an {@link ESteamVanity} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamVanity} instance or the default value
	 */
	public static ESteamVanity fromIndexOrElse(Integer index, ESteamVanity defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamVanity} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamVanity} instance or the default value
	 */
	public static ESteamVanity fromIndexOrElse(Integer index, Supplier<ESteamVanity> defaultValueSupplier) {
		return UwObject.ifNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamVanity} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromIndexOrElse(Integer, ESteamVanity)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamVanity} instance or {@code null}
	 */
	public static ESteamVanity fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamVanity) null);
	}
}
