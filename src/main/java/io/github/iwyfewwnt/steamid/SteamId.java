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

package io.github.iwyfewwnt.steamid;

import io.github.iwyfewwnt.steamid.types.ESteamAccount;
import io.github.iwyfewwnt.steamid.types.ESteamInstance;
import io.github.iwyfewwnt.steamid.types.ESteamUniverse;
import io.github.iwyfewwnt.steamid.utils.*;
import io.github.iwyfewwnt.uwutils.UwObject;
import io.github.iwyfewwnt.uwutils.UwString;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.regex.Matcher;

/**
 * A Steam ID representation.
 *
 * <p>{@code SteamId} is the class that
 * represents Steam ID as an entity and
 * includes a lot of methods to convert
 * and parse to/from any Steam-like string.
 */
@SuppressWarnings("unused")
public final class SteamId implements Serializable, Cloneable {

	/**
	 * A base unique account type-32 identifier value.
	 */
	public static final int BASE_XUID = 0x00000000;

	/**
	 * A minimum unique account type-32 identifier value.
	 */
	public static final int MIN_XUID = 0x00000001;

	/**
	 * A maximum unique account type-32 identifier value.
	 */
	public static final int MAX_XUID = 0x7FFFFFFF;

	/**
	 * A base unique account type-64 identifier value.
	 */
	public static final long BASE_ID64 = 0x0110000100000000L;

	/**
	 * A minimum unique account type-64 identifier value.
	 */
	public static final long MIN_ID64 = 0x0110000100000001L;

	/**
	 * A maximum unique account type-64 identifier value.
	 */
	public static final long MAX_ID64 = 0x01100001FFFFFFFFL;

	/**
	 * A minimum unique account type-2 identifier value.
	 */
	public static final int MIN_ID2 = 0x00000000;

	/**
	 * A maximum unique account type-2 identifier value.
	 */
	public static final int MAX_ID2 = 0x3FFFFFFF;

	/**
	 * A Steam ID2 format string.
	 *
	 * <p>Arguments in order:
	 * <ul>
	 *     <li>Decimal :: Account universe type identifier.
	 *     <li>Decimal :: Account authentication type identifier.
	 *     <li>Decimal :: Account type-2 identifier.
	 * </ul>
	 *
	 * @see <a href="https://vk.cc/ch9ea2">Steam ID2 as Represented in Computer Programs</a>
	 */
	private static final String ID2_FMT = "STEAM_%d:%d:%d";

	/**
	 * A Steam ID3 format string.
	 *
	 * <p>Arguments in order:
	 * <ul>
	 *     <li>Character :: Account type character.
	 *     <li>Decimal :: Account universe type identifier.
	 *     <li>Decimal :: Account type-32 identifier.
	 *     <li>String :: Account instance string.
	 * </ul>
	 */
	private static final String ID3_FMT = "[%c:%d:%d%s]";

	/**
	 * A unique account type-32 identifier.
	 */
	private final Integer xuid;

	/**
	 * An enum account universe type.
	 */
	private final ESteamUniverse universe;

	/**
	 * An enum account instance type.
	 */
	private final ESteamInstance instance;

	/**
	 * An enum account type.
	 */
	private final ESteamAccount account;

	/**
	 * Intialize a {@link SteamId} instance.
	 *
	 * <p>Defines a copy constructor.
	 *
	 * @param xuid		integer value of the account type-32 identifier
	 * @param universe	enum value of the account universe type
	 * @param instance	enum value of the account instance type
	 * @param account	enum value of the account type
	 */
	private SteamId(Integer xuid, ESteamUniverse universe, ESteamInstance instance, ESteamAccount account) {
		this.xuid = xuid;
		this.universe = universe;
		this.instance = instance;
		this.account = account;
	}

	/**
	 * Intialize a {@link SteamId} instance.
	 *
	 * <p>Defines a copy constructor.
	 *
	 * @param xuid		integer value of the account type-32 identifier
	 * @param universe	integer value of the account universe type
	 * @param instance	integer value of the account instance type
	 * @param account	integer value of the account type
	 */
	private SteamId(Integer xuid, Integer universe, Integer instance, Integer account) {
		this(xuid, ESteamUniverse.fromIdOrNull(universe), ESteamInstance.fromIdOrNull(instance), ESteamAccount.fromIdOrNull(account));
	}

	/**
	 * Intialize a {@link SteamId} instance.
	 *
	 * <p>Defines a copy constructor.
	 *
	 * @param xuid		integer value of the account type-32 identifier
	 * @param universe	integer value of the account universe type
	 * @param instance	integer value of the account instance type
	 * @param account	character value of the account type
	 */
	private SteamId(Integer xuid, Integer universe, Integer instance, Character account) {
		this(xuid, ESteamUniverse.fromIdOrNull(universe), ESteamInstance.fromIdOrNull(instance), ESteamAccount.fromCharOrNull(account));
	}

	/**
	 * Intialize a {@link SteamId} instance.
	 *
	 * <p>Wraps {@link SteamId#SteamId(Integer, ESteamUniverse, ESteamInstance, ESteamAccount)}
	 * w/ {@link ESteamUniverse#PUBLIC}, {@link ESteamInstance#DESKTOP}, and {@link ESteamAccount#INDIVIDUAL}.
	 *
	 * @param xuid		integer value of the account type-32 identifier
	 */
	private SteamId(Integer xuid) {
		this(xuid, ESteamUniverse.PUBLIC, ESteamInstance.DESKTOP, ESteamAccount.INDIVIDUAL);
	}

	/**
	 * Get this unique account type-32 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#xuid} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				long value of the unique account type-32 identifier or the default value
	 */
	public Integer getXuidOrElse(Integer defaultValue) {
		return UwObject.getIfNull(this.xuid, defaultValue);
	}

	/**
	 * Get this unique account type-32 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#xuid} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						long value of the unique account type-32 identifier or the default value
	 */
	public Integer getXuidOrElse(Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(this.getXuidOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this unique account type-32 identifier
	 * or return {@code 0} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#xuid} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getXuidOrElse(Integer)}
	 * w/ {@code 0} as the default value.
	 *
	 * @return	long value of the unique account type-32 identifier or {@code 0}
	 */
	public Integer getXuidOrZero() {
		return this.getXuidOrElse(0);
	}

	/**
	 * Get this unique account type-32 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#xuid} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getXuidOrElse(Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	long value of the unique account type-32 identifier or {@code null}
	 */
	public Integer getXuidOrNull() {
		return this.getXuidOrElse((Integer) null);
	}

	/**
	 * Get this account universe enum type instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account universe enum type instance or the default value
	 */
	public ESteamUniverse getUniverseOrElse(ESteamUniverse defaultValue) {
		return UwObject.getIfNull(this.universe, defaultValue);
	}

	/**
	 * Get this account universe enum type instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account universe enum type instance or the default value
	 */
	public ESteamUniverse getUniverseOrElse(Supplier<ESteamUniverse> defaultValueSupplier) {
		return UwObject.getIfNull(this.getUniverseOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account universe enum type instance
	 * or return an invalid value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getUniverseOrElse(ESteamUniverse)}
	 * w/ {@link ESteamUniverse#INVALID} as the default value.
	 *
	 * @return	account universe enum type instance
	 */
	public ESteamUniverse getUniverseOrInvalid() {
		return this.getUniverseOrElse(ESteamUniverse.INVALID);
	}

	/**
	 * Get this account universe enum type instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getUniverseOrElse(ESteamUniverse)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account universe enum type instance or {@code null}
	 */
	public ESteamUniverse getUniverseOrNull() {
		return this.getUniverseOrElse((ESteamUniverse) null);
	}

	/**
	 * Get this account universe type identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account universe type identifier or the default value
	 */
	public Integer getUniverseIdOrElse(Integer defaultValue) {
		if (this.universe == null) {
			return defaultValue;
		}

		return universe.getId();
	}

	/**
	 * Get this account universe type identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account universe type identifier or the default value
	 */
	public Integer getUniverseIdOrElse(Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(this.getUniverseIdOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account universe type identifier
	 * or return an invalid value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getUniverseIdOrElse(Integer)}
	 * w/ {@link USteamUniverse#INVALID} as the default value.
	 *
	 * @return	account universe type identifier
	 */
	public Integer getUniverseIdOrInvalid() {
		return this.getUniverseIdOrElse(USteamUniverse.INVALID);
	}

	/**
	 * Get this account universe type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getUniverseIdOrElse(Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account universe type identifier or {@code null}
	 */
	public Integer getUniverseIdOrNull() {
		return this.getUniverseIdOrElse((Integer) null);
	}

	/**
	 * Get this account instance enum type
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account instance enum type or the default value
	 */
	public ESteamInstance getInstanceOrElse(ESteamInstance defaultValue) {
		return UwObject.getIfNull(this.instance, defaultValue);
	}

	/**
	 * Get this account instance enum type
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account instance enum type or the default value
	 */
	public ESteamInstance getInstanceOrElse(Supplier<ESteamInstance> defaultValueSupplier) {
		return UwObject.getIfNull(this.getInstanceOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account instance enum type
	 * or return the {@link ESteamInstance#ALL} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getInstanceOrElse(ESteamInstance)}
	 * w/ {@link ESteamInstance#ALL} as the default value.
	 *
	 * @return	account instance enum type
	 */
	public ESteamInstance getInstanceOrAll() {
		return this.getInstanceOrElse(ESteamInstance.ALL);
	}

	/**
	 * Get this account instance enum type
	 * or return {@code null} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getInstanceOrElse(ESteamInstance)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account instance enum type or {@code null}
	 */
	public ESteamInstance getInstanceOrNull() {
		return this.getInstanceOrElse((ESteamInstance) null);
	}

	/**
	 * Get this account instance type identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account instance type identifier or the default value
	 */
	public Integer getInstanceIdOrElse(Integer defaultValue) {
		if (this.instance == null) {
			return defaultValue;
		}

		return this.instance.getId();
	}

	/**
	 * Get this account instance type identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account instance type identifier or the default value
	 */
	public Integer getInstanceIdOrElse(Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(this.getInstanceIdOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account instance type identifier
	 * or return the {@link USteamInstance#ALL} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getInstanceIdOrElse(Integer)}
	 * w/ {@link USteamInstance#ALL} as the default value.
	 *
	 * @return	account instance type identifier
	 */
	public Integer getInstanceIdOrAll() {
		return this.getInstanceIdOrElse(USteamInstance.ALL);
	}

	/**
	 * Get this account instance type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getInstanceIdOrElse(Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account instance type identifier or {@code null}
	 */
	public Integer getInstanceIdOrNull() {
		return this.getInstanceIdOrElse((Integer) null);
	}

	/**
	 * Get this account type enum type
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account type enum type or the default value
	 */
	public ESteamAccount getAccountTypeOrElse(ESteamAccount defaultValue) {
		return UwObject.getIfNull(this.account, defaultValue);
	}

	/**
	 * Get this account type enum type
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account type enum type or the default value
	 */
	public ESteamAccount getAccountTypeOrElse(Supplier<ESteamAccount> defaultValueSupplier) {
		return UwObject.getIfNull(this.getAccountTypeOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account type enum type
	 * or return the {@link ESteamAccount#INVALID} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeOrElse(ESteamAccount)}
	 * w/ {@link ESteamAccount#INVALID} as the default value.
	 *
	 * @return	account type enum type
	 */
	public ESteamAccount getAccountTypeOrInvalid() {
		return this.getAccountTypeOrElse(ESteamAccount.INVALID);
	}

	/**
	 * Get this account type enum type
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeOrElse(ESteamAccount)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account type enum type or {@code null}
	 */
	public ESteamAccount getAccountTypeOrNull() {
		return this.getAccountTypeOrElse((ESteamAccount) null);
	}

	/**
	 * Get this account type identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account type identifier or the defualt value
	 */
	public Integer getAccountTypeIdOrElse(Integer defaultValue) {
		if (this.account == null) {
			return defaultValue;
		}

		return this.account.getId();
	}

	/**
	 * Get this account type identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default
	 * @return						account type identifier or the defualt value
	 */
	public Integer getAccountTypeIdOrElse(Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(this.getAccountTypeIdOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account type identifier
	 * or return an invalid value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeIdOrElse(Integer)}
	 * w/ {@link USteamAccount#INVALID_ID} as the default value.
	 *
	 * @return	account type identifier
	 */
	public Integer getAccountTypeIdOrInvalid() {
		return this.getAccountTypeIdOrElse(USteamAccount.INVALID_ID);
	}

	/**
	 * Get this account type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeIdOrElse(Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account type identifier or {@code null}
	 */
	public Integer getAccountTypeIdOrNull() {
		return this.getAccountTypeIdOrElse((Integer) null);
	}

	/**
	 * Get this account type character
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account type character or the default value
	 */
	public Character getAccountTypeCharOrElse(Character defaultValue) {
		if (this.account == null) {
			return defaultValue;
		}

		return this.account.getChar();
	}

	/**
	 * Get this account type character
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account type character or the default value
	 */
	public Character getAccountTypeCharOrElse(Supplier<Character> defaultValueSupplier) {
		return UwObject.getIfNull(this.getAccountTypeCharOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account type character
	 * or return an invalid value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeCharOrElse(Character)}
	 * w/ {@link USteamAccount#INVALID_CHAR} as the default value.
	 *
	 * @return	account type character
	 */
	public Character getAccountTypeCharOrInvalid() {
		return this.getAccountTypeCharOrElse(USteamAccount.INVALID_CHAR);
	}

	/**
	 * Get this account type character
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeCharOrElse(Character)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account type character or {@code null}
	 */
	public Character getAccountTypeCharOrNull() {
		return this.getAccountTypeCharOrElse((Character) null);
	}

	/**
	 * Check if this instance is valid.
	 *
	 * @return	boolean value that describes validity of this instance
	 */
	public boolean isValid() {
		if (this.xuid == null || this.universe == null
				|| this.instance == null || this.account == null) {
			return false;
		}

		//noinspection ConstantConditions
		if (this.xuid < BASE_XUID && this.xuid > MAX_XUID) {
			return false;
		}

		if (this.universe == ESteamUniverse.INVALID
				|| this.account == ESteamAccount.INVALID) {
			return false;
		}

		if (this.xuid < MIN_XUID) {
			return this.account != ESteamAccount.INDIVIDUAL
					&& this.account != ESteamAccount.GAME_SERVER
					&& (this.account != ESteamAccount.CLAN || this.instance == ESteamInstance.ALL);
		}

		return true;
	}

	/**
	 * Check if this instance is invalid.
	 *
	 * @return	boolean value that describes validity of this instance
	 */
	public boolean isNotValid() {
		return !this.isValid();
	}


	/**
	 * Get this instance as a static account key
	 * or return a default value if failed.
	 *
	 * <p>Used for grouping accounts with different account instances.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance could is invalid.
	 *     <li>Catched {@link ArithmeticException}.
	 * </ul>
	 *
	 * @param defaultValue 	default value to return on failure.
	 * @return 				long value of the static key or the default value
	 */
	public Long toStaticKeyOrElse(Long defaultValue) {
		if (this.isNotValid()) {
			return defaultValue;
		}

		BigInteger universe = BigInteger.valueOf(this.universe.getId())
				.shiftLeft(USteamBit.ACCOUNT_UNIVERSE_OFFSET);

		BigInteger account = BigInteger.valueOf(this.account.getId())
				.shiftLeft(USteamBit.ACCOUNT_ID_OFFSET);

		try {
			return BigInteger.valueOf(this.xuid)
					.add(universe)
					.add(account)
					.longValueExact();
		} catch (ArithmeticException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Get this instance as a static account key
	 * or return a default value if failed.
	 *
	 * <p>Used for grouping accounts with different account instances.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance could is invalid.
	 *     <li>Catched {@link ArithmeticException}.
	 * </ul>
	 *
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @return 						long value of the static key or the default value
	 */
	public Long toStaticKeyOrElse(Supplier<Long> defaultValueSupplier) {
		return UwObject.getIfNull(this.toStaticKeyOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a static account key
	 * or return {@code 0L} value if failed.
	 *
	 * <p>Used for grouping accounts with different account instances.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 *     <li>Catched {@link ArithmeticException}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toStaticKeyOrElse(Long)}
	 * w/ {@code 0L} as the default value.
	 *
	 * @return 				long value of the static key or {@code 0L}
	 */
	public Long toStaticKeyOrZero() {
		return this.toStaticKeyOrElse(0L);
	}

	/**
	 * Get this instance as a static account key
	 * or return {@code null} if failed.
	 *
	 * <p>Used for grouping accounts with different account instances.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 *     <li>Catched {@link ArithmeticException}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toStaticKeyOrElse(Long)}
	 * w/ {@code null} as the default value.
	 *
	 * @return 				long value of the static key or {@code null}
	 */
	public Long toStaticKeyOrNull() {
		return this.toStaticKeyOrElse((Long) null);
	}

	/**
	 * Get this instance as a unique account type-64 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return 				long value of the unique account type-64 identifier or the default value
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 64-bit Systems</a>
	 */
	public Long toSteam64OrElse(Long defaultValue) {
		if (this.isNotValid()) {
			return defaultValue;
		}

		long xuid = this.xuid;
		long instance = this.instance.getId();
		long account = this.account.getId();
		long universe = this.account.getId();

		return xuid << USteamBit.ACCOUNT_ID_OFFSET
				| instance << USteamBit.ACCOUNT_INSTANCE_OFFSET
				| account << USteamBit.ACCOUNT_TYPE_OFFSET
				| universe << USteamBit.ACCOUNT_UNIVERSE_OFFSET;
	}

	/**
	 * Get this instance as a unique account type-64 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return 						long value of the unique account type-64 identifier or the default value
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 64-bit Systems</a>
	 */
	public Long toSteam64OrElse(Supplier<Long> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteam64OrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a unique account type-64 identifier
	 * or return {@code 0L} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam64OrElse(Long)}
	 * w/ {@code 0L} as the default value.
	 *
	 * @return 				long value of the unique account type-64 identifier or {@code 0L}
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 64-bit Systems</a>
	 */
	public Long toSteam64OrZero() {
		return this.toSteam64OrElse(0L);
	}

	/**
	 * Get this instance as a unique account type-64 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam64OrElse(Long)}
	 * w/ {@code null} as the default value.
	 *
	 * @return 				long value of the unique account type-64 identifier or {@code null}
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 64-bit Systems</a>
	 */
	public Long toSteam64OrNull() {
		return this.toSteam64OrElse((Long) null);
	}

	/**
	 * Get this instance as a unique account type-2 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the unique account type-2 identifier or the default value
	 */
	public String toSteam2OrElse(String defaultValue) {
		if (this.isNotValid()) {
			return defaultValue;
		}

		int x = this.universe.getId();
		int y = this.xuid & 1;
		int z = this.xuid >> 1;

		//noinspection UnnecessaryLocalVariable
		String id2 = String.format(ID2_FMT, x, y, z);

//		if (!isSteamId2Valid(id2)) {
//			return defaultValue;
//		}

		return id2;
	}

	/**
	 * Get this instance as a unique account type-2 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the unique account type-2 identifier or the default value
	 */
	public String toSteam2OrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteam2OrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a unique account type-2 identifier
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam2OrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return				string value of the unique account type-2 identifier or the empty one
	 */
	public String toSteam2OrEmpty() {
		return this.toSteam2OrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a unique account type-2 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam2OrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return				string value of the unique account type-2 identifier or {@code null}
	 */
	public String toSteam2OrNull() {
		return this.toSteam2OrElse((String) null);
	}

	/**
	 * Get this instance as a unique account type-3 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the unique account type-3 identifier or the default value
	 */
	public String toSteam3OrElse(String defaultValue) {
		if (this.isNotValid()) {
			return defaultValue;
		}

		boolean bInstance = false;

		char ch = this.account.getChar();

		switch (this.account) {
			case CHAT:
				switch (this.instance) {
					case CLAN:
						ch = USteamAccount.CLAN_CHAT_CHAR;
						break;

					case LOBBY:
						ch = USteamAccount.LOBBY_CHAT_CHAR;
						break;
				}
				break;

			case ANON_GAME_SERVER:
			case MULTISEAT:
				bInstance = true;
				break;
		}

		int universe = this.universe.getId();
		int instance = this.instance.getId();

		return String.format(ID3_FMT, ch, universe, this.xuid, bInstance ? ":" + instance : "");
	}

	/**
	 * Get this instance as a unique account type-3 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the unique account type-3 identifier or the default value
	 */
	public String toSteam3OrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteam3OrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a unique account type-3 identifier
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam3OrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the unique account type-3 identifier or the empty one
	 */
	public String toSteam3OrEmpty() {
		return this.toSteam3OrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a unique account type-3 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam3OrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	string value of the unique account type-3 identifier or {@code null}
	 */
	public String toSteam3OrNull() {
		return this.toSteam3OrElse((String) null);
	}

	/**
	 * Get this instance as a Steam invite code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This intance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the invite code or the default value
	 */
	public String toInviteCodeOrElse(String defaultValue) {
		if (this.isNotValid()) {
			return defaultValue;
		}

		return USteamInvite.fromXuidOrElse(this.xuid, defaultValue);
	}

	/**
	 * Get this instance as a Steam invite code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This intance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the invite code or the default value
	 */
	public String toInviteCodeOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toInviteCodeOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a Steam invite code
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This intance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toInviteCodeOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return				string value of the invite code or the empty one
	 */
	public String toInviteCodeOrEmpty() {
		return this.toInviteCodeOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a Steam invite code
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This intance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toInviteCodeOrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return				string value of the invite code or {@code null}
	 */
	public String toInviteCodeOrNull() {
		return this.toInviteCodeOrElse((String) null);
	}

	/**
	 * Get this instance as a CS:GO interface-friendly friend code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the friend code or the default value.
	 */
	public String toCsgoCodeOrElse(String defaultValue) {
		if (this.isNotValid()) {
			return defaultValue;
		}

		return USteamCsgo.fromXuidOrElse(this.xuid, defaultValue);
	}

	/**
	 * Get this instance as a CS:GO interface-friendly friend code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	suppleir from which get the default value
	 * @return						string value of the friend code or the default value.
	 */
	public String toCsgoCodeOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toCsgoCodeOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a CS:GO interface-friendly friend code
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toCsgoCodeOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the friend code or the empty one
	 */
	public String toCsgoCodeOrEmpty() {
		return this.toCsgoCodeOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a CS:GO interface-friendly friend code
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toCsgoCodeOrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	string value of the friend code or {@code null}
	 */
	public String toCsgoCodeOrNull() {
		return this.toCsgoCodeOrElse((String) null);
	}

	/**
	 * Get this instance as a Steam /profiles/%id-64% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the URL or the default value
	 */
	public String toSteam64UrlOrElse(String defaultValue) {
		Long id64 = this.toSteam64OrNull();

		if (id64 == null) {
			return defaultValue;
		}

		return USteamUrl.PROFILE + id64;
	}

	/**
	 * Get this instance as a Steam /profiles/%id-64% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the URL or the default value
	 */
	public String toSteam64UrlOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteam64UrlOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a Steam /profiles/%id-64% URL
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam64UrlOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the URL or the empty one
	 */
	public String toSteam64UrlOrEmpty() {
		return this.toSteam64UrlOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a Steam /profiles/%id-64% URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam64UrlOrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	string value of the URL or {@code null}
	 */
	public String toSteam64UrlOrNull() {
		return this.toSteam64UrlOrElse((String) null);
	}

	/**
	 * Get this instance as a Steam /profiles/%id-3% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the URL or the default value
	 */
	public String toSteam3UrlOrElse(String defaultValue) {
		String id3 = this.toSteam3OrNull();

		if (id3 == null) {
			return defaultValue;
		}

		return USteamUrl.PROFILE + id3;
	}

	/**
	 * Get this instance as a Steam /profiles/%id-3% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the URL or the default value
	 */
	public String toSteam3UrlOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteam3UrlOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a Steam /profiles/%id-3% URL
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam3UrlOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the URL or the empty one
	 */
	public String toSteam3UrlOrEmpty() {
		return this.toSteam3UrlOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a Steam /profiles/%id-3% URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam3UrlOrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	string value of the URL or {@code null}
	 */
	public String toSteam3UrlOrNull() {
		return this.toSteam3UrlOrElse((String) null);
	}

	/**
	 * Get this instance as a Steam /user/%invite-code% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure.
	 * @return				string value of the user URL or the default value
	 */
	public String toSteamUserUrlOrElse(String defaultValue) {
		String inviteCode = this.toInviteCodeOrNull();

		if (inviteCode == null) {
			return defaultValue;
		}

		return USteamUrl.USER + inviteCode;
	}

	/**
	 * Get this instance as a Steam /user/%invite-code% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the user URL or the default value
	 */
	public String toSteamUserUrlOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteamUserUrlOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a Steam /user/%invite-code% URL
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteamUserUrlOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the user URL or the empty one
	 */
	public String toSteamUserUrlOrEmpty() {
		return this.toSteamUserUrlOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a Steam /user/%invite-code% URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteamUserUrlOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the user URL or {@code null}
	 */
	public String toSteamUserUrlOrNull() {
		return this.toSteamUserUrlOrElse((String) null);
	}

	/**
	 * Get this instance as a Steam /p/%invite-code% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the invite URL or the default value
	 */
	public String toSteamInviteUrlOrElse(String defaultValue) {
		String inviteCode = this.toInviteCodeOrNull();

		if (inviteCode == null) {
			return defaultValue;
		}

		return USteamUrl.INVITE + inviteCode;
	}

	/**
	 * Get this instance as a Steam /p/%invite-code% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the invite URL or the default value
	 */
	public String toSteamInviteUrlOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteamInviteUrlOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a Steam /p/%invite-code% URL
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteamInviteUrlOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the invite URL or the empty one
	 */
	public String toSteamInviteUrlOrEmpty() {
		return this.toSteamUserUrlOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a Steam /p/%invite-code% URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteamInviteUrlOrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	string value of the invite URL or {@code null}
	 */
	public String toSteamInviteUrlOrNull() {
		return this.toSteamInviteUrlOrElse((String) null);
	}

	/**
	 * Get this instance as a Steam China /profiles/%id-64% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				string value of the URL or the default value
	 */
	public String toSteam64ChinaUrlOrElse(String defaultValue) {
		Long id64 = this.toSteam64OrNull();

		if (id64 == null) {
			return defaultValue;
		}

		return USteamUrl.CHINA + id64;
	}

	/**
	 * Get this instance as a Steam China /profiles/%id-64% URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						string value of the URL or the default value
	 */
	public String toSteam64ChinaUrlOrElse(Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(this.toSteam64ChinaUrlOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this instance as a Steam China /profiles/%id-64% URL
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam64ChinaUrlOrElse(String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @return	string value of the URL or the empty one
	 */
	public String toSteam64ChinaUrlOrEmpty() {
		return this.toSteam64ChinaUrlOrElse(UwString.EMPTY);
	}

	/**
	 * Get this instance as a Steam China /profiles/%id-64% URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>This instance is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#toSteam64ChinaUrlOrElse(String)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	string value of the URL or {@code null}
	 */
	public String toSteam64ChinaUrlOrNull() {
		return this.toSteam64ChinaUrlOrElse((String) null);
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #xuid} value.
	 *
	 * @param xuid	new integer value of the unique account type-32 identifier
	 * @return		new {@link SteamId} instance or this if xuids are equal
	 */
	public SteamId withXuid(Integer xuid) {
		if (Objects.equals(this.xuid, xuid)) {
			return this;
		}

		return new SteamId(xuid, this.universe, this.instance, this.account);
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #universe} value.
	 *
	 * @param universe	new enum value of the account universe type
	 * @return			new {@link SteamId} instance or this if enum universes are equal
	 */
	public SteamId withUniverse(ESteamUniverse universe) {
		if (this.universe == universe) {
			return this;
		}

		return new SteamId(this.xuid, universe, this.instance, this.account);
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #universe} value.
	 *
	 * @param universe	new integer value of the account universe type
	 * @return			new {@link SteamId} instance or this if universes are equal
	 */
	public SteamId withUniverse(Integer universe) {
		return this.withUniverse(ESteamUniverse.fromIdOrNull(universe));
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #instance} value.
	 *
	 * @param instance	new enum value of the account instance type
	 * @return			new {@link SteamId} instance or this if enum instances are equal
	 */
	public SteamId withInstance(ESteamInstance instance) {
		if (this.instance == instance) {
			return this;
		}

		return new SteamId(this.xuid, this.universe, instance, this.account);
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #instance} value.
	 *
	 * @param instance	new integer value of the account instance type
	 * @return			new {@link SteamId} instance or this if instances are equal
	 */
	public SteamId withInstance(Integer instance) {
		return this.withInstance(ESteamInstance.fromIdOrNull(instance));
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #account} value.
	 *
	 * @param account	new enum value of the account type
	 * @return			new {@link SteamId} instance or this if enum account types are equal
	 */
	public SteamId withAccount(ESteamAccount account) {
		if (this.account == account) {
			return this;
		}

		return new SteamId(this.xuid, this.universe, this.instance, account);
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #account} value.
	 *
	 * @param account	new integer value of the account type
	 * @return			new {@link SteamId} instance or this if account types are equal
	 */
	public SteamId withAccount(Integer account) {
		return this.withAccount(ESteamAccount.fromIdOrNull(account));
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #account} value.
	 *
	 * @param account	new character value of the account type
	 * @return			new {@link SteamId} instance or this if account types are equal
	 */
	public SteamId withAccount(Character account) {
		return this.withAccount(ESteamAccount.fromCharOrNull(account));
	}

	/**
	 * Copy this {@link SteamId} instance.
	 *
	 * @return	new {@link SteamId} instance w/ the same field values
	 */
	public SteamId copy() {
		return new SteamId(this.xuid, this.universe, this.instance, this.account);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		SteamId that = (SteamId) obj;

		return Objects.equals(this.xuid, that.xuid)
				&& this.universe == that.universe
				&& this.instance == that.instance
				&& this.account == that.account;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.xuid, this.universe, this.instance, this.account);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
				.add("xuid=" + this.xuid)
				.add("universe=" + this.universe)
				.add("instance=" + this.instance)
				.add("account=" + this.account)
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ignored) {
		}

		return this.copy();
	}

	/**
	 * Check if the provided account type-32 identifier is valid.
	 *
	 * @param xuid	integer value of the account type-32 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamXuidValid(Integer xuid) {
		return xuid != null
				&& xuid >= MIN_XUID;
//				&& xuid <= MAX_XUID;
	}

	/**
	 * Check if the provided account type-32 identifier is valid.
	 *
	 * @param xuid	string value of the account type-32 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamXuidValid(String xuid) {
		if (xuid == null) {
			return false;
		}

		try {
			return isSteamXuidValid(Integer.parseUnsignedInt(xuid));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Check if the provided account type-32 identifier is invalid.
	 *
	 * @param xuid	integer value of the account type-32 identifier
	 * @return		boolean value that descibes invalidity of the identifier
	 */
	public static boolean isSteamXuidInvalid(Integer xuid) {
		return !isSteamXuidValid(xuid);
	}

	/**
	 * Check if the provided account type-32 identifier is invalid.
	 *
	 * @param xuid	string value of the account type-32 identifier
	 * @return		booolean value that describes invalidity of the identifier
	 */
	public static boolean isSteamXuidInvalid(String xuid) {
		return !isSteamXuidValid(xuid);
	}

	/**
	 * Check if the provided account type-64 identifier is valid.
	 *
	 * @param id64	long value of the account type-64 identifier
	 * @return		boolean value that descibes validity of the identifier
	 */
	public static boolean isSteamId64Valid(Long id64) {
		return id64 != null
				&& id64 >= MIN_ID64
				&& id64 <= MAX_ID64;
	}

	/**
	 * Check if the provided account type-64 identifier is valid.
	 *
	 * @param id64	string value of the account type-64 identifier
	 * @return		boolean value that descibes validity of the identifier
	 */
	public static boolean isSteamId64Valid(String id64) {
		if (id64 == null) {
			return false;
		}

		try {
			return isSteamId64Valid(Long.parseUnsignedLong(id64));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Check if the provided account type-64 identifier is invalid.
	 *
	 * @param id64	long value of the account type-64 identifier
	 * @return		boolean value that descibes invalidity of the identifier
	 */
	public static boolean isSteamId64Invalid(Long id64) {
		return !isSteamId64Valid(id64);
	}

	/**
	 * Check if the provided account type-64 identifier is invalid.
	 *
	 * @param id64	string value of the account type-64 identifier
	 * @return		boolean value that descibes invalidity of the identifier
	 */
	public static boolean isSteamId64Invalid(String id64) {
		return !isSteamId64Valid(id64);
	}

	/**
	 * Check if the provided account type-2 identifier is valid.
	 *
	 * @param id2	integer value of the type-2 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamId2Valid(Integer id2) {
		return id2 != null
				&& id2 >= MIN_ID2
				&& id2 <= MAX_ID2;
	}

	/**
	 * Check if the provided account type-2 identifier is valid.
	 *
	 * @param id2	string value of the type-2 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamId2Valid(String id2) {
		if (id2 == null) {
			return false;
		}

		Matcher m = USteamPattern.ID2.matcher(id2);

		if (!m.matches()) {
			return false;
		}

		try {
			id2 = m.group(USteamRegex.Group.ID);
			return isSteamId2Valid(Integer.parseUnsignedInt(id2));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Check if the provided account type-2 identifier is invalid.
	 *
	 * @param id2	integer value of the type-2 identifier
	 * @return		boolean value that describes invalidity of the identifier
	 */
	public static boolean isSteamId2Invalid(Integer id2) {
		return !isSteamId2Valid(id2);
	}

	/**
	 * Check if the provided account type-2 identifier is invalid.
	 *
	 * @param id2	string value of the type-2 identifier
	 * @return		boolean value that describes invalidity of the identifier
	 */
	public static boolean isSteamId2Invalid(String id2) {
		return !isSteamId2Valid(id2);
	}

	/**
	 * Check if the provided account type-3 identifier is valid.
	 *
	 * @param id3	string value of the account type-3 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamId3Valid(String id3) {
		if (id3 == null) {
			return false;
		}

		Matcher m = USteamPattern.ID3.matcher(id3);

		if (!m.matches()) {
			return false;
		}

		try {
			id3 = m.group(USteamRegex.Group.ID);
			return isSteamXuidValid(Integer.parseUnsignedInt(id3));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Check if the provided account type-3 identifier is invalid.
	 *
	 * @param id3	string value of the account type-3 identifier
	 * @return		boolean value that describes invalidity of the identifier
	 */
	public static boolean isSteamId3Invalid(String id3) {
		return !isSteamId3Valid(id3);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam account type-32 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * @param xuid			integer value of the account type-32 identifier
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamXuidOrElse(Integer xuid, SteamId defaultValue) {
		if (isSteamXuidInvalid(xuid)) {
			return defaultValue;
		}

		return new SteamId(xuid);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam account type-32 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * @param xuid					integer value of the account type-32 identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamXuidOrElse(Integer xuid, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteamXuidOrNull(xuid), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam account type-32 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteamXuidOrElse(Integer, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param xuid	integer value of the account type-32 identifier
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteamXuidOrNull(Integer xuid) {
		return fromSteamXuidOrElse(xuid, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam account type-32 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * @param xuid			string value of the account type-32 identifier
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamXuidOrElse(String xuid, SteamId defaultValue) {
		try {
			int id = Integer.parseUnsignedInt(xuid);
			return fromSteamXuidOrElse(id, defaultValue);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Create a {@link SteamId} instance from the Steam account type-32 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * @param xuid					string value of the account type-32 identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamXuidOrElse(String xuid, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteamXuidOrNull(xuid), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam account type-32 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteamXuidOrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param xuid	string value of the account type-32 identifier
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteamXuidOrNull(String xuid) {
		return fromSteamXuidOrElse(xuid, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-64 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-64 identifier is invalid.
	 * </ul>
	 *
	 * @param id64			long value of the account type-64 identifier
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam64OrElse(Long id64, SteamId defaultValue) {
		if (isSteamId64Invalid(id64)) {
			return defaultValue;
		}

		int xuid = USteamBit.getAccountXuid(id64);
		int universe = USteamBit.getAccountUniverse(id64);
		int instance = USteamBit.getAccountInstance(id64);
		int account = USteamBit.getAccountType(id64);

		return new SteamId(xuid, universe, instance, account);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-64 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-64 identifier is invalid.
	 * </ul>
	 *
	 * @param id64					long value of the account type-64 identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam64OrElse(Long id64, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteam64OrNull(id64), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-64 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-64 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteam64OrElse(Long, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id64	long value of the account type-64 identifier
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteam64OrNull(Long id64) {
		return fromSteam64OrElse(id64, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-64 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-64 identifier is invalid.
	 * </ul>
	 *
	 * @param id64			string value of the account type-64 identifier
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam64OrElse(String id64, SteamId defaultValue) {
		if (id64 == null) {
			return defaultValue;
		}

		id64 = id64.trim();

		try {
			long val = Long.parseUnsignedLong(id64);
			return fromSteam64OrElse(val, defaultValue);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Create a {@link SteamId} instance from the account type-64 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-64 identifier is invalid.
	 * </ul>
	 *
	 * @param id64					string value of the account type-64 identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam64OrElse(String id64, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteam64OrNull(id64), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-64 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-64 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteam64OrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id64	string value of the account type-64 identifier
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteam64OrNull(String id64) {
		return fromSteam64OrElse(id64, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-2 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-2 identifier is invalid.
	 * </ul>
	 *
	 * @param id2			string value of the account type-2 identifier
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam2OrElse(String id2, SteamId defaultValue) {
		if (id2 == null) {
			return defaultValue;
		}

		id2 = id2.trim();
		Matcher m = USteamPattern.ID2.matcher(id2);

		if (!m.matches()) {
			return defaultValue;
		}

		try {
			int id = Integer.parseUnsignedInt(m.group(USteamRegex.Group.ID));
			int auth = Integer.parseUnsignedInt(m.group(USteamRegex.Group.AUTH));

			return new SteamId((id << 1) | auth);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Create a {@link SteamId} instance from the account type-2 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-2 identifier is invalid.
	 * </ul>
	 *
	 * @param id2					string value of the account type-2 identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam2OrElse(String id2, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteam2OrNull(id2), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-2 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-2 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteam2OrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id2	string value of the account type-2 identifier
	 * @return		new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam2OrNull(String id2) {
		return fromSteam2OrElse(id2, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-3 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-3 identifier is invalid.
	 * </ul>
	 *
	 * @param id3			string value of the account type-3 identifier
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam3OrElse(String id3, SteamId defaultValue) {
		if (id3 == null) {
			return defaultValue;
		}

		id3 = id3.trim();
		Matcher m = USteamPattern.ID3.matcher(id3);

		if (!m.matches()) {
			return defaultValue;
		}

		try {
			int xuid = Integer.parseUnsignedInt(USteamRegex.Group.ID);
			int universe = Integer.parseUnsignedInt(m.group(USteamRegex.Group.UNIVERSE));
			int instance = USteamInstance.DESKTOP;
			char account = m.group(USteamRegex.Group.ACCOUNT).charAt(0);

			try {
				instance = Integer.parseUnsignedInt(m.group(USteamRegex.Group.INSTANCE));
			} catch (NumberFormatException ignored) {
				switch (account) {
					case USteamAccount.CLAN_CHAR:
					case USteamAccount.CHAT_CHAR:
					case USteamAccount.CLAN_CHAT_CHAR:
					case USteamAccount.LOBBY_CHAT_CHAR:
						instance = USteamInstance.ALL;
						break;
				}
			}

			switch (account) {
				case USteamAccount.CLAN_CHAT_CHAR:
				case USteamAccount.LOBBY_CHAT_CHAR:
					account = USteamAccount.CHAT_CHAR;
					break;
			}

			return new SteamId(xuid, universe, instance, account);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Create a {@link SteamId} instance from the account type-3 identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-3 identifier is invalid.
	 * </ul>
	 *
	 * @param id3					string value of the account type-3 identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteam3OrElse(String id3, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteam3OrNull(id3), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the account type-3 identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Account type-3 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteam3OrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id3			string value of the account type-3 identifier
	 * @return				new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteam3OrNull(String id3) {
		return fromSteam3OrElse(id3, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam invite code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam invite code is invalid.
	 * </ul>
	 *
	 * @param code			string value of the invite code
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromInviteCodeOrElse(String code, SteamId defaultValue) {
		Integer xuid = USteamInvite.toXuidOrNull(code);

		if (xuid == null) {
			return defaultValue;
		}

		return new SteamId(xuid);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam invite code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam invite code is invalid.
	 * </ul>
	 *
	 * @param code					string value of the invite code
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromInviteCodeOrElse(String code, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromInviteCodeOrNull(code), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam invite code
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam invite code is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromInviteCodeOrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param code	string value of the invite code
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromInviteCodeOrNull(String code) {
		return fromInviteCodeOrElse(code, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the interface-friendly CS:GO friend code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is invalid.
	 * </ul>
	 *
	 * @param code			string value of the friend code
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromCsgoCodeOrElse(String code, SteamId defaultValue) {
		Integer xuid = USteamCsgo.toXuidOrNull(code);

		if (code == null) {
			return defaultValue;
		}

		return new SteamId(xuid);
	}

	/**
	 * Create a {@link SteamId} instance from the interface-friendly CS:GO friend code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is invalid.
	 * </ul>
	 *
	 * @param code					string value of the friend code
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromCsgoCodeOrElse(String code, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromCsgoCodeOrNull(code), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the interface-friendly CS:GO friend code
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromCsgoCodeOrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param code	string value of the friend code
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromCsgoCodeOrNull(String code) {
		return fromCsgoCodeOrElse(code, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam /profiles/ URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam /profiles/ URL is invalid.
	 * </ul>
	 *
	 * @param url			string value of the /profiles/ URL
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamProfileUrlOrElse(String url, SteamId defaultValue) {
		if (url == null) {
			return defaultValue;
		}

		url = url.trim();
		Matcher m = USteamPattern.PROFILE_URL.matcher(url);

		if (!m.matches()) {
			return defaultValue;
		}

		String id = m.group(USteamRegex.Group.ID);

		try {
			return fromSteam64OrElse(Long.parseUnsignedLong(id), defaultValue);
		} catch (NumberFormatException ignored) {
		}

		return fromSteam3OrElse(id, defaultValue);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam /profiles/ URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam /profiles/ URL is invalid.
	 * </ul>
	 *
	 * @param url					string value of the /profiles/ URL
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamProfileUrlOrElse(String url, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteamProfileUrlOrNull(url), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam /profiles/ URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam /profiles/ URL is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteamProfileUrlOrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param url	string value of the /profiles/ URL
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteamProfileUrlOrNull(String url) {
		return fromSteamProfileUrlOrElse(url, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam /user/ URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam /user/ URL is invalid.
	 * </ul>
	 *
	 * @param url			string value of the /user/ URL
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamUserUrlOrElse(String url, SteamId defaultValue) {
		if (url == null) {
			return defaultValue;
		}

		url = url.trim();
		Matcher m = USteamPattern.USER_URL.matcher(url);

		if (!m.matches()) {
			return defaultValue;
		}

		String code = m.group(USteamRegex.Group.ID);
		return fromInviteCodeOrElse(code, defaultValue);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam /user/ URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam /user/ URL is invalid.
	 * </ul>
	 *
	 * @param url					string value of the /user/ URL
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamUserUrlOrElse(String url, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteamUserUrlOrNull(url), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam /user/ URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam /user/ URL is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteamUserUrlOrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param url	string value of the /user/ URL
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteamUserUrlOrNull(String url) {
		return fromSteamUserUrlOrElse(url, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>URL doesn't match with /profiles/.
	 *     <li>URL doesn't match with /user/.
	 *     <li>Account type-64 identifier is invalid.
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * @param url			string value of the URL
	 * @param defaultValue	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamUrlOrElse(String url, SteamId defaultValue) {
		SteamId val = fromSteamProfileUrlOrNull(url);

		if (val == null) {
			return fromSteamUserUrlOrElse(url, defaultValue);
		}

		return val;
	}

	/**
	 * Create a {@link SteamId} instance from the Steam URL
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>URL doesn't match with /profiles/.
	 *     <li>URL doesn't match with /user/.
	 *     <li>Account type-64 identifier is invalid.
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * @param url					string value of the URL
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamUrlOrElse(String url, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteamUrlOrNull(url), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam URL
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>URL doesn't match with /profiles/.
	 *     <li>URL doesn't match with /user/.
	 *     <li>Account type-64 identifier is invalid.
	 *     <li>Account type-32 identifier is invalid.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteamUrlOrElse(String, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param url	string value of the URL
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteamUrlOrNull(String url) {
		return fromSteamUrlOrElse(url, (SteamId) null);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam related object
	 * or return a default value if failed.
	 *
	 * <p>Wraps all these methods in order:
	 * <ul>
	 *     <li>{@link SteamId#fromSteamXuidOrElse(Integer, SteamId)}
	 *     <li>{@link SteamId#fromSteam64OrElse(Long, SteamId)}
	 *     <li>{@link SteamId#fromSteamXuidOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam64OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam2OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam3OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromInviteCodeOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromCsgoCodeOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteamUrlOrElse(String, SteamId)}
	 * </ul>
	 *
	 * <p>Possible failure case:
	 * <ul>
	 *     <li>Object type doesn't match w/ Integer, Long, and String classes
	 *     <li>Provided argument is invalid for the all implemented methods
	 * </ul>
	 *
	 * @param obj			Steam related object
	 * @param defaultValue 	default value to return on failure
	 * @return				new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamAnyOrElse(Object obj, SteamId defaultValue) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof Integer) {
			return fromSteamXuidOrElse((Integer) obj, defaultValue);
		}

		if (obj instanceof Long) {
			return fromSteam64OrElse((Long) obj, defaultValue);
		}

		if (obj instanceof String) {
			String str = (String) obj;

			SteamId val = fromSteamXuidOrNull(str);
			if (val != null) {
				return val;
			}

			val = fromSteam64OrNull(str);
			if (val != null) {
				return val;
			}

			val = fromSteam2OrNull(str);
			if (val != null) {
				return val;
			}

			val = fromSteam3OrNull(str);
			if (val != null) {
				return val;
			}

			val = fromInviteCodeOrNull(str);
			if (val != null) {
				return val;
			}

			val = fromCsgoCodeOrNull(str);
			if (val != null) {
				return val;
			}

			val = fromSteamUrlOrNull(str);
			if (val != null) {
				return val;
			}
		}

		return defaultValue;
	}

	/**
	 * Create a {@link SteamId} instance from the Steam related object
	 * or return a default value if failed.
	 *
	 * <p>Wraps all these methods in order:
	 * <ul>
	 *     <li>{@link SteamId#fromSteamXuidOrElse(Integer, SteamId)}
	 *     <li>{@link SteamId#fromSteam64OrElse(Long, SteamId)}
	 *     <li>{@link SteamId#fromSteamXuidOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam64OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam2OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam3OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromInviteCodeOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromCsgoCodeOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteamUrlOrElse(String, SteamId)}
	 * </ul>
	 *
	 * <p>Possible failure case:
	 * <ul>
	 *     <li>Object type doesn't match w/ Integer, Long, and String classes
	 *     <li>Provided argument is invalid for the all implemented methods
	 * </ul>
	 *
	 * @param obj					value of the Steam related object
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @return						new {@link SteamId} instance or the default value
	 */
	public static SteamId fromSteamAnyOrElse(Object obj, Supplier<SteamId> defaultValueSupplier) {
		return UwObject.getIfNull(fromSteamAnyOrNull(obj), defaultValueSupplier);
	}

	/**
	 * Create a {@link SteamId} instance from the Steam related object
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps all these methods in order:
	 * <ul>
	 *     <li>{@link SteamId#fromSteamXuidOrElse(Integer, SteamId)}
	 *     <li>{@link SteamId#fromSteam64OrElse(Long, SteamId)}
	 *     <li>{@link SteamId#fromSteamXuidOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam64OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam2OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteam3OrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromInviteCodeOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromCsgoCodeOrElse(String, SteamId)}
	 *     <li>{@link SteamId#fromSteamUrlOrElse(String, SteamId)}
	 * </ul>
	 *
	 * <p>Possible failure case:
	 * <ul>
	 *     <li>Object type doesn't match w/ Integer, Long, and String classes
	 *     <li>Provided argument is invalid for the all implemented methods
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#fromSteamAnyOrElse(Object, SteamId)}
	 * w/ {@code null} as the default value.
	 *
	 * @param obj	value of the Steam related object
	 * @return		new {@link SteamId} instance or {@code null}
	 */
	public static SteamId fromSteamAnyOrNull(Object obj) {
		return fromSteamAnyOrElse(obj, (SteamId) null);
	}

	/**
	 * A builder class for the {@link SteamId} creation.
	 */
	public static final class Builder implements Serializable, Cloneable {

		/**
		 * A unique account type-32 identifier.
		 */
		private Integer xuid;

		/**
		 * An enum account universe type.
		 */
		private ESteamUniverse eUniverse;

		/**
		 * An enum account instance type.
		 */
		private ESteamInstance eInstance;

		/**
		 * An enum account type.
		 */
		private ESteamAccount eAccount;

		/**
		 * An account universe type identifier.
		 */
		private Integer iUniverse;

		/**
		 * An account instance type identifier.
		 */
		private Integer iInstance;

		/**
		 * An account type identifier.
		 */
		private Integer iAccount;

		/**
		 * An account type character.
		 */
		private Character cAccount;

		/**
		 * Initialize a {@link SteamId.Builder} instance
		 * w/ all fields beaing assigned to {@code null}.
		 */
		public Builder() {
		}

		/**
		 * Initialize a {@link SteamId.Builder} instance.
		 *
		 * <p>Defines a copy constructor.
		 *
		 * @param xuid			integer value of the unique account type-32 identifier
		 * @param eUniverse		enum value of the account universe type
		 * @param eInstance		enum value of the account instance type
		 * @param eAccount		enum value of the account type
		 * @param iUniverse		integer value of the account universe type
		 * @param iInstance		integer value of the account instance type
		 * @param iAccount		integer value of the account type
		 * @param cAccount		character value of the account type
		 */
		private Builder(
				Integer xuid,
				ESteamUniverse eUniverse,
				ESteamInstance eInstance,
				ESteamAccount eAccount,
				Integer iUniverse,
				Integer iInstance,
				Integer iAccount,
				Character cAccount
		) {
			this.xuid = xuid;
			this.eUniverse = eUniverse;
			this.eInstance = eInstance;
			this.eAccount = eAccount;
			this.iUniverse = iUniverse;
			this.iInstance = iInstance;
			this.iAccount = iAccount;
			this.cAccount = cAccount;
		}

		/**
		 * Create a new {@link SteamId} instance
		 * w/ pre-setted field values.
		 *
		 * @return	new {@link SteamId} instance.
		 */
		public SteamId build() {
			if (this.iUniverse != null) {
				this.eUniverse = ESteamUniverse.fromIdOrNull(this.iUniverse);
			}

			if (this.iInstance != null) {
				this.eInstance = ESteamInstance.fromIdOrNull(this.iInstance);
			}

			if (this.iAccount != null) {
				this.eAccount = ESteamAccount.fromIdOrNull(this.iAccount);
			} else if (this.cAccount != null) {
				this.eAccount = ESteamAccount.fromCharOrNull(this.cAccount);
			}

			return new SteamId(this.xuid, this.eUniverse, this.eInstance, this.eAccount);
		}

		/**
		 * Get this unique account type-32 identifier
		 *
		 * @return	unique account type-32 identifier
		 */
		public Integer getXuid() {
			return this.xuid;
		}

		/**
		 * Get this enum universe type
		 *
		 * @return	enum universe type
		 */
		public ESteamUniverse getUniverse() {
			return this.eUniverse;
		}

		/**
		 * Get this enum instance type
		 *
		 * @return	enum instance type
		 */
		public ESteamInstance getInstance() {
			return this.eInstance;
		}

		/**
		 * Get this enum account type
		 *
		 * @return	enum account type
		 */
		public ESteamAccount getAccount() {
			return this.eAccount;
		}

		/**
		 * Get this universe type identifier
		 *
		 * @return	universe type identifier
		 */
		public Integer getUniverseId() {
			return this.iUniverse;
		}

		/**
		 * Get this instance type identifier
		 *
		 * @return	instance type identifier
		 */
		public Integer getInstanceId() {
			return this.iInstance;
		}

		/**
		 * Get this account type identifier
		 *
		 * @return	account type identifier
		 */
		public Integer getAccountId() {
			return this.iAccount;
		}

		/**
		 * Get this account type character
		 *
		 * @return	account type character
		 */
		public Character getAccountChar() {
			return this.cAccount;
		}

		/**
		 * Set this unique account type-32 identifier.
		 *
		 * @param xuid	integer value of the unique account type-32 identifier
		 * @return		this {@link SteamId.Builder} instance
		 */
		public Builder setXuid(Integer xuid) {
			this.xuid = xuid;
			return this;
		}

		/**
		 * Set this account universe type.
		 *
		 * @param universe	enum value of the account universe type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setUniverse(ESteamUniverse universe) {
			this.eUniverse = universe;
			this.iUniverse = null;
			return this;
		}

		/**
		 * Set this account universe type.
		 *
		 * @param universe	integer value of the account universe type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setUniverse(Integer universe) {
			this.iUniverse = universe;
			this.eUniverse = null;
			return this;
		}

		/**
		 * Set this account instance type.
		 *
		 * @param instance	enum value of the account instance type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setInstance(ESteamInstance instance) {
			this.eInstance = instance;
			this.iUniverse = null;
			return this;
		}

		/**
		 * Set this account instance type.
		 *
		 * @param instance	integer value of the account instance type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setInstance(Integer instance) {
			this.iInstance = instance;
			this.eInstance = null;
			return this;
		}

		/**
		 * Set this account type.
		 *
		 * @param account	enum value of the account type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setAccount(ESteamAccount account) {
			this.eAccount = account;
			this.iAccount = null;
			this.cAccount = null;
			return this;
		}

		/**
		 * Set this account type.
		 *
		 * @param account	integer value of the account type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setAccount(Integer account) {
			this.iAccount = account;
			this.eAccount = null;
			this.cAccount = null;
			return this;
		}

		/**
		 * Set this account type.
		 *
		 * @param account	character value of the account type
		 * @return			this {@link SteamId.Builder} instance
		 */
		public Builder setAccount(Character account) {
			this.cAccount = account;
			this.eAccount = null;
			this.iAccount = null;
			return this;
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #xuid} value.
		 *
		 * @param xuid	new integer value of the unique account type-32 identifier
		 * @return		new {@link SteamId.Builder} instance or this if xuids are equal
		 */
		public Builder withXuid(Integer xuid) {
			if (Objects.equals(this.xuid, xuid)) {
				return this;
			}

			return this.copy().setXuid(xuid);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #eUniverse} value.
		 *
		 * @param universe	new enum value of the account universe type
		 * @return			new {@link SteamId.Builder} instance or this if enum universes are equal
		 */
		public Builder withUniverse(ESteamUniverse universe) {
			if (this.eUniverse == universe) {
				return this;
			}

			return this.copy().setUniverse(universe);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #iUniverse} value.
		 *
		 * @param universe	new integer value of the account universe type
		 * @return			new {@link SteamId.Builder} instance or this if integer universes are equal
		 */
		public Builder withUniverse(Integer universe) {
			if (Objects.equals(this.iUniverse, universe)) {
				return this;
			}

			return this.copy().setUniverse(universe);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #eInstance} value.
		 *
		 * @param instance	new enum value of the account instance type
		 * @return			new {@link SteamId.Builder} instance or this if enum instances are equal
		 */
		public Builder withInstance(ESteamInstance instance) {
			if (this.eInstance == instance) {
				return this;
			}

			return this.copy().setInstance(instance);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #iInstance} value.
		 *
		 * @param instance	new integer value of the account instance type
		 * @return			new {@link SteamId.Builder} instance or this if integer instances are equal
		 */
		public Builder withInstance(Integer instance) {
			if (Objects.equals(this.iInstance, instance)) {
				return this;
			}

			return this.copy().setInstance(instance);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #eAccount} value.
		 *
		 * @param account	new enum value of the account type
		 * @return			new {@link SteamId.Builder} instance or this if enum account types are equal
		 */
		public Builder withAccount(ESteamAccount account) {
			if (this.eAccount == account) {
				return this;
			}

			return this.copy().setAccount(account);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #iAccount} value.
		 *
		 * @param account	new integer value of the account type
		 * @return			new {@link SteamId.Builder} instance or this if integer account types are equal
		 */
		public Builder withAccount(Integer account) {
			if (Objects.equals(this.iAccount, account)) {
				return this;
			}

			return this.copy().setAccount(account);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #cAccount} value.
		 *
		 * @param account	new character value of the account type
		 * @return			new {@link SteamId.Builder} instance or this if character account types are equal
		 */
		public Builder withAccount(Character account) {
			if (Objects.equals(this.cAccount, account)) {
				return this;
			}

			return this.copy().setAccount(account);
		}

		/**
		 * Copy this {@link SteamId.Builder} instance.
		 *
		 * @return	new {@link SteamId.Builder} instance w/ the same filed values
		 */
		public Builder copy() {
			return new Builder(
					this.xuid,
					this.eUniverse,
					this.eInstance,
					this.eAccount,
					this.iUniverse,
					this.iInstance,
					this.iAccount,
					this.cAccount
			);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (obj == null || this.getClass() != obj.getClass()) {
				return false;
			}

			Builder that = (Builder) obj;
			return Objects.equals(this.xuid, that.xuid)
					&& this.eUniverse == that.eUniverse
					&& this.eInstance == that.eInstance
					&& this.eAccount == that.eAccount
					&& Objects.equals(this.iUniverse, that.iUniverse)
					&& Objects.equals(this.iInstance, that.iInstance)
					&& Objects.equals(this.iAccount, that.iAccount)
					&& Objects.equals(this.cAccount, that.cAccount);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return Objects.hash(
					this.xuid,
					this.eUniverse,
					this.eInstance,
					this.eAccount,
					this.iUniverse,
					this.iInstance,
					this.iAccount,
					this.cAccount
			);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
					.add("xuid=" + this.xuid)
					.add("eUniverse=" + this.eUniverse)
					.add("eInstance=" + this.eInstance)
					.add("eAccount=" + this.eAccount)
					.add("iUniverse=" + this.iUniverse)
					.add("iInstance=" + this.iInstance)
					.add("iAccount=" + this.iAccount)
					.add("cAccount=" + this.cAccount)
					.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException ignored) {
			}

			return this.copy();
		}
	}
}
