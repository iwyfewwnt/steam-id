/*
 * Copyright 2022 u004
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

package io.github.u004.steamid.types;

import io.github.u004.uwutils.UwArray;
import io.github.u004.uwutils.UwMap;
import io.vavr.control.Option;
import io.github.u004.steamid.utils.USteamVanity;

import java.util.Map;

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

	private static final Map<Integer, ESteamVanity> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamVanity.class
	);

	/**
	 * Vanity URL type ID.
	 */
	private final int value;

	/**
	 * Initialize an {@code ESteamVanity} instance.
	 *
	 * @param value		vanity url type ID
	 */
	ESteamVanity(int value) {
		this.value = value;
	}

	/**
	 * Get this vanity URL type ID.
	 *
	 * @return		vanity URL type ID
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamVanity} instance by its type ID.
	 *
	 * @param value		vanity URL type ID
	 * @return			{@code ESteamVanity} instance
	 *					that wrapped in {@link Option}
	 */
	public static Option<ESteamVanity> fromValue(Integer value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamVanity} instance by its index.
	 *
	 * @param index		{@code ESteamVanity} instance index
	 * @return			{@code ESteamVanity} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamVanity> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamVanity} instance by its type ID.
	 *
	 * @param value		vanity URL type ID
	 * @return			{@code ESteamVanity} instance or null
	 */
	public static ESteamVanity fromValueRaw(Integer value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamVanity} instance by its index.
	 *
	 * @param index		{@code ESteamVanity} instance index
	 * @return			{@code ESteamVanity} instance or null
	 */
	public static ESteamVanity fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
