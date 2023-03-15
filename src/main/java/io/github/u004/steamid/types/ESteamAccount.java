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
import io.github.u004.steamid.utils.USteamAccount;
import io.github.u004.steamid.utils.USteamBit;

import java.util.Map;

/**
 * A Steam account type enums.
 *
 * <p>Wraps {@link USteamAccount}.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public enum ESteamAccount {

	/**
	 * An account type enum - Invalid.
	 *
	 * <p>Wraps {@link USteamAccount#INVALID_ID} and {@link USteamAccount#INVALID_CHAR}.
	 */
	INVALID(USteamAccount.INVALID_ID, USteamAccount.INVALID_CHAR),

	/**
	 * An account type enum - Individual.
	 *
	 * <p>Wraps {@link USteamAccount#INDIVIDUAL_ID} and {@link USteamAccount#INDIVIDUAL_CHAR}.
	 */
	INDIVIDUAL(USteamAccount.INDIVIDUAL_ID, USteamAccount.INDIVIDUAL_CHAR),

	/**
	 * An account type enum - Multiseat.
	 *
	 * <p>Wraps {@link USteamAccount#MULTISEAT_ID} and {@link USteamAccount#MULTISEAT_CHAR}.
	 */
	MULTISEAT(USteamAccount.MULTISEAT_ID, USteamAccount.MULTISEAT_CHAR),

	/**
	 * An account type enum - Game Server.
	 *
	 * <p>Wraps {@link USteamAccount#GAME_SERVER_ID} and {@link USteamAccount#GAME_SERVER_CHAR}.
	 */
	GAME_SERVER(USteamAccount.GAME_SERVER_ID, USteamAccount.GAME_SERVER_CHAR),

	/**
	 * An account type enum - Anonymous Game Server.
	 *
	 * <p>Wraps {@link USteamAccount#ANON_GAME_SERVER_ID} and {@link USteamAccount#ANON_GAME_SERVER_CHAR}.
	 */
	ANON_GAME_SERVER(USteamAccount.ANON_GAME_SERVER_ID, USteamAccount.ANON_GAME_SERVER_CHAR),

	/**
	 * An account type enum - Pending.
	 *
	 * <p>Wraps {@link USteamAccount#PENDING_ID} and {@link USteamAccount#PENDING_CHAR}.
	 */
	PENDING(USteamAccount.PENDING_ID, USteamAccount.PENDING_CHAR),

	/**
	 * An account type enum - Content Server.
	 *
	 * <p>Wraps {@link USteamAccount#CONTENT_SERVER_ID} and {@link USteamAccount#CONTENT_SERVER_CHAR}.
	 */
	CONTENT_SERVER(USteamAccount.CONTENT_SERVER_ID, USteamAccount.CONTENT_SERVER_CHAR),

	/**
	 * An account type enum - Clan.
	 *
	 * <p>Wraps {@link USteamAccount#CLAN_ID} and {@link USteamAccount#CLAN_CHAR}.
	 */
	CLAN(USteamAccount.CLAN_ID, USteamAccount.CLAN_CHAR),

	/**
	 * An account type enum - Chat.
	 *
	 * <p>Wraps {@link USteamAccount#CHAT_ID} and {@link USteamAccount#CHAT_CHAR}.
	 */
	CHAT(USteamAccount.CHAT_ID, USteamAccount.CHAT_CHAR),

	/**
	 * An account type enum - Console User/P2P SuperSeeder.
	 *
	 * <p>Wraps {@link USteamAccount#CONSOLE_USER_ID} and {@link USteamAccount#CONSOLE_USER_CHAR}.
	 */
	CONSOLE_USER(USteamAccount.CONSOLE_USER_ID, USteamAccount.CONSOLE_USER_CHAR),

	/**
	 * An account type enum - Anonymous User.
	 *
	 * <p>Wraps {@link USteamAccount#ANON_USER_ID} and {@link USteamAccount#ANON_USER_CHAR}.
	 */
	ANON_USER(USteamAccount.ANON_USER_ID, USteamAccount.ANON_USER_CHAR),

	/**
	 * An account type enum - Unknown.
	 *
	 * <p>Wraps {@link USteamAccount#UNKNOWN_ID} and {@link USteamAccount#UNKNOWN_CHAR}.
	 */
	UNKNOWN(USteamAccount.UNKNOWN_ID, USteamAccount.UNKNOWN_CHAR);

	/**
	 * An account type offset in the bit vector.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_TYPE_OFFSET}.
	 */
	public static final int OFFSET = USteamBit.ACCOUNT_TYPE_OFFSET;

	private static final Map<Integer, ESteamAccount> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamAccount.class
	);

	private static final Map<Character, ESteamAccount> MAP_BY_CHAR = UwMap.newEnumMapByFieldRaw(
			entry -> entry.ch, ESteamAccount.class
	);

	/**
	 * Account type ID.
	 */
	private final int value;

	/**
	 * Account type char.
	 */
	private final char ch;

	/**
	 * Initialize an {@code ESteamAccount} instance.
	 *
	 * @param value	account type ID
	 * @param ch	account type char
	 */
	ESteamAccount(int value, char ch) {
		this.value = value;
		this.ch = ch;
	}

	/**
	 * Get this account type ID.
	 *
	 * @return	account type ID
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Get this account type char.
	 *
	 * @return	account type char
	 */
	public char getChar() {
		return this.ch;
	}

	/**
	 * Get the {@code ESteamAccount} instance by its type ID.
	 *
	 * @param value		account type ID
	 * @return			{@code ESteamAccount} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamAccount> fromValue(Integer value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamAccount} instance by its type char.
	 *
	 * @param value		account type char
	 * @return			{@code ESteamAccount} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamAccount> fromChar(Character value) {
		return UwMap.get(value, MAP_BY_CHAR);
	}

	/**
	 * Get the {@code ESteamAccount} instance by its index.
	 *
	 * @param index		{@code ESteamAccount} instance index
	 * @return			{@code ESteamAccount} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamAccount> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamAccount} instance by its type ID.
	 *
	 * @param value		account type ID
	 * @return			{@code ESteamAccount} instance or null
	 */
	public static ESteamAccount fromValueRaw(Integer value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamAccount} instance by its type char.
	 *
	 * @param value		account type char
	 * @return			{@code ESteamAccount} instance or null
	 */
	public static ESteamAccount fromCharRaw(Character value) {
		return fromChar(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamAccount} instance by its index.
	 *
	 * @param index		{@code ESteamAccount} instance index
	 * @return			{@code ESteamAccount} instance or null
	 */
	public static ESteamAccount fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
