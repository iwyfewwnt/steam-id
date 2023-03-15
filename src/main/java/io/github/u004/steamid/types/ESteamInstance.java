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
import io.github.u004.steamid.utils.USteamInstance;

import java.util.Map;

/**
 * A Steam account instance type enums.
 *
 * <p>Wraps {@link USteamInstance}.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public enum ESteamInstance {

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
	 * An account instance offset in the bit vector.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_INSTANCE_OFFSET}.
	 */
	public static final int OFFSET = USteamBit.ACCOUNT_INSTANCE_OFFSET;

	private static final Map<Integer, ESteamInstance> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamInstance.class
	);

	/**
	 * Account instance type ID.
	 */
	private final int value;

	/**
	 * Initialize a {@code ESteamInstance} instance.
	 *
	 * @param value		account instance type ID
	 */
	ESteamInstance(int value) {
		this.value = value;
	}

	/**
	 * Get this account instance type ID.
	 *
	 * @return	account instance type ID
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamInstance} instance by its type ID.
	 *
	 * @param value		account instance type ID
	 * @return			{@code ESteamInstance} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamInstance> fromValue(Integer value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamInstance} instance by its index.
	 *
	 * @param index		{@code ESteamInstance} instance index
	 * @return			{@code ESteamInstance} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamInstance> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamInstance} instance by its type ID.
	 *
	 * @param value		account instance type ID
	 * @return			{@code ESteamInstance} instance or null
	 */
	public static ESteamInstance fromValueRaw(Integer value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamInstance} instance by its index.
	 *
	 * @param index		{@code ESteamInstance} instance index
	 * @return			{@code ESteamInstance} instance or null
	 */
	public static ESteamInstance fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
