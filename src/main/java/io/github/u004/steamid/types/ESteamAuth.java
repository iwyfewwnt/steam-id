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
import io.github.u004.steamid.utils.USteamAuth;

import java.util.Map;

/**
 * A Steam authentication type enums.
 *
 * <p>Wraps {@link USteamAuth}.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public enum ESteamAuth {

	/**
	 * An authentication type enum - No.
	 *
	 * <p>Wraps {@link USteamAuth#NO}.
	 */
	NO(USteamAuth.NO),

	/**
	 * An authentication type enum - Yes.
	 *
	 * <p>Wraps {@link USteamAuth#YES}.
	 */
	YES(USteamAuth.YES);

	private static final Map<Integer, ESteamAuth> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamAuth.class
	);

	/**
	 * Authentication type ID.
	 */
	private final int value;

	/**
	 * Initialize a {@code ESteamAuth} instance.
	 *
	 * @param value	authentication type ID
	 */
	ESteamAuth(int value) {
		this.value = value;
	}

	/**
	 * Get this authentication type ID.
	 *
	 * @return	authentication type ID
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamAuth} instance by its type ID.
	 *
	 * @param value		authentication type ID
	 * @return			{@code ESteamAuth} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamAuth> fromValue(Integer value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamAuth} instance by its index.
	 *
	 * @param index		{@code ESteamAuth} instance index
	 * @return			{@code ESteamAuth} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamAuth> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamAuth} instance by its type ID.
	 *
	 * @param value		authentication type ID
	 * @return			{@code ESteamAuth} instance or null
	 */
	public static ESteamAuth fromValueRaw(Integer value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamAuth} instance by its index.
	 *
	 * @param index		{@code ESteamAuth} instance index
	 * @return			{@code ESteamAuth} instance or null
	 */
	public static ESteamAuth fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
