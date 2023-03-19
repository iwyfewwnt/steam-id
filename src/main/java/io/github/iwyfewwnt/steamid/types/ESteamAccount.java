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

import io.github.iwyfewwnt.steamid.utils.USteamAccount;
import io.github.iwyfewwnt.uwutils.UwArray;
import io.github.iwyfewwnt.uwutils.UwEnum;
import io.github.iwyfewwnt.uwutils.UwMap;
import io.github.iwyfewwnt.uwutils.UwObject;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam account type enums.
 *
 * <p>Wraps {@link USteamAccount}.
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
	 * An array of {@link ESteamAccount} instances.
	 */
	private static final ESteamAccount[] VALUES = UwEnum.values(ESteamAccount.class);

	/**
	 * A map of {@link ESteamAccount} instances by their identifier field.
	 */
	private static final Map<Integer, ESteamAccount> MAP_BY_ID = UwMap.newMapByFieldOrNull(
			entry -> entry.id, ESteamAccount.class
	);

	/**
	 * A map of {@link ESteamAccount} instances by their character field.
	 */
	private static final Map<Character, ESteamAccount> MAP_BY_CHAR = UwMap.newMapByFieldOrNull(
			entry -> entry.ch, ESteamAccount.class
	);

	/**
	 * An account type identifier.
	 */
	private final int id;

	/**
	 * An account type character.
	 */
	private final char ch;

	/**
	 * Initialize an {@link ESteamAccount} instance.
	 *
	 * @param id	account type identifier
	 * @param ch	account type character
	 */
	ESteamAccount(int id, char ch) {
		this.id = id;
		this.ch = ch;
	}

	/**
	 * Get this account type identifier.
	 *
	 * @return	account type identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get this account type character.
	 *
	 * @return	account type character
	 */
	public char getChar() {
		return this.ch;
	}

	/**
	 * Get the account type identifier from the provided {@link ESteamAccount} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAccount} instance is {@code null}.
	 * </ul>
	 *
	 * @param account		enum value of the account type from which get the identifier
	 * @param defualtValue	default value to return on failure
	 * @return				account type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamAccount account, Integer defualtValue) {
		if (account == null) {
			return defualtValue;
		}

		return account.getId();
	}

	/**
	 * Get the account type identifier from the provided {@link ESteamAccount} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAccount} instance is {@code null}.
	 * </ul>
	 *
	 * @param account				enum value of the account type from which get the identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamAccount account, Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(getIdOrNull(account), defaultValueSupplier);
	}

	/**
	 * Get the account type identifier from the provided {@link ESteamAccount} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAccount} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamAccount#getIdOrElse(ESteamAccount, Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @param account	enum value of the account type from which get the identifier
	 * @return			account type identifier or {@code null}
	 */
	public static Integer getIdOrNull(ESteamAccount account) {
		return getIdOrElse(account, (Integer) null);
	}

	/**
	 * Get the account type character from the provided {@link ESteamAccount} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAccount} instance is {@code null}.
	 * </ul>
	 *
	 * @param account		enum value of the account type from which get the character
	 * @param defaultValue	default value to return on failure
	 * @return				account type character or the default value
	 */
	public static Character getCharOrElse(ESteamAccount account, Character defaultValue) {
		if (account == null) {
			return defaultValue;
		}

		return account.getChar();
	}

	/**
	 * Get the account type character from the provided {@link ESteamAccount} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAccount} instance is {@code null}.
	 * </ul>
	 *
	 * @param account				enum value of the account type from which get the character
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account type character or the default value
	 */
	public static Character getCharOrElse(ESteamAccount account, Supplier<Character> defaultValueSupplier) {
		return UwObject.getIfNull(getCharOrNull(account), defaultValueSupplier);
	}

	/**
	 * Get the account type character from the provided {@link ESteamAccount} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAccount} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamAccount#getCharOrElse(ESteamAccount, Character)}
	 * w/ {@code null} as the default value.
	 *
	 * @param account	enum value of the account type from which get the character
	 * @return			account type character or {@code null}
	 */
	public static Character getCharOrNull(ESteamAccount account) {
		return getCharOrElse(account, (Character) null);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its account type identifier
	 * or return a default value if failed.
	 *
	 * @param id			account type identifier of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamAccount} instance or the default values
	 */
	public static ESteamAccount fromIdOrElse(Integer id, ESteamAccount defaultValue) {
		return UwMap.getOrElse(id, MAP_BY_ID, defaultValue);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its account type identifier
	 * or return a default value if failed.
	 *
	 * @param id					account type identifier of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamAccount} instance or the default values
	 */
	public static ESteamAccount fromIdOrElse(Integer id, Supplier<ESteamAccount> defaultValueSupplier) {
		return UwObject.getIfNull(fromIdOrNull(id), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its account type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamAccount#fromIdOrElse(Integer, ESteamAccount)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id	account type identifier of the instance
	 * @return		associated {@link ESteamAccount} instance or {@code null}
	 */
	public static ESteamAccount fromIdOrNull(Integer id) {
		return fromIdOrElse(id, (ESteamAccount) null);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its account type character
	 * or return a default value if failed.
	 *
	 * @param ch			account type character of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamAccount} instance or the default value
	 */
	public static ESteamAccount fromCharOrElse(Character ch, ESteamAccount defaultValue) {
		return UwMap.getOrElse(ch, MAP_BY_CHAR, defaultValue);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its account type character
	 * or return a default value if failed.
	 *
	 * @param ch					account type character of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamAccount} instance or the default value
	 */
	public static ESteamAccount fromCharOrElse(Character ch, Supplier<ESteamAccount> defaultValueSupplier) {
		return UwObject.getIfNull(fromCharOrNull(ch), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its account type character
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamAccount#fromCharOrElse(Character, ESteamAccount)}
	 * w/ {@code null} as the default value.
	 *
	 * @param ch	account type character of the instance
	 * @return		associated {@link ESteamAccount} instance or {@code null}
	 */
	public static ESteamAccount fromCharOrNull(Character ch) {
		return fromCharOrElse(ch, (ESteamAccount) null);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamAccount} instance or the default value
	 */
	public static ESteamAccount fromIndexOrElse(Integer index, ESteamAccount defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamAccount} instance or the default value
	 */
	public static ESteamAccount fromIndexOrElse(Integer index, Supplier<ESteamAccount> defaultValueSupplier) {
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamAccount} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamAccount#fromIndexOrElse(Integer, ESteamAccount)}
	 * w/ {@code null} as the default alue.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamAccount} instance or {@code null}
	 */
	public static ESteamAccount fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamAccount) null);
	}
}
