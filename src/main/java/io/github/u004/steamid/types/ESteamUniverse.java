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
import io.github.u004.steamid.utils.USteamBit;
import io.github.u004.steamid.utils.USteamUniverse;

import java.util.Map;

/**
 * A Steam account universe type enums.
 *
 * <p>Wraps {@link USteamUniverse}.
 */
@SuppressWarnings("unused")
public enum ESteamUniverse {

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
	 * An account universe offset in the bit vector.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_UNIVERSE_OFFSET}.
	 */
	public static final int OFFSET = USteamBit.ACCOUNT_UNIVERSE_OFFSET;

	private static final Map<Integer, ESteamUniverse> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamUniverse.class
	);

	/**
	 * An account universe type ID.
	 */
	private final int value;

	/**
	 * Initialize a {@code ESteamUniverse} instance.
	 *
	 * @param value		account universe type ID
	 */
	ESteamUniverse(int value) {
		this.value = value;
	}

	/**
	 * Get this account universe type ID.
	 *
	 * @return		account universe type ID
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamUniverse} instance by its type ID.
	 *
	 * @param value		account universe type ID
	 * @return			{@code ESteamUniverse} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamUniverse> fromValue(Integer value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamUniverse} instance by its index.
	 *
	 * @param index		{@code ESteamUniverse} instance index
	 * @return			{@code ESteamUniverse} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamUniverse> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamUniverse} instance by its type ID.
	 *
	 * @param value		account universe type ID
	 * @return			{@code ESteamUniverse} instance or null
	 */
	public static ESteamUniverse fromValueRaw(Integer value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamUniverse} instance by its index.
	 *
	 * @param index		{@code ESteamUniverse} instance index
	 * @return			{@code ESteamUniverse} instance or null
	 */
	public static ESteamUniverse fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
