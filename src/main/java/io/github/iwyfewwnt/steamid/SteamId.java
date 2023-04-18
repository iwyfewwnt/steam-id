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
import io.github.iwyfewwnt.uwutils.UwSystem;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;

/**
 * A Steam ID representation.
 *
 * <p>{@code SteamId} is the class that
 * represents Steam ID as an entity and
 * includes a lot of methods to convert
 * and parse to/from any Steam related data.
 */
@SuppressWarnings({"unused", "MethodDoesntCallSuperMethod"})
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
	public static final long MAX_ID64 = 0x011000017FFFFFFFL;

	/**
	 * A minimum unique account type-2 identifier value.
	 */
	public static final int MIN_ID2 = 0x00000000;

	/**
	 * A maximum unique account type-2 identifier value.
	 */
	public static final int MAX_ID2 = 0x3FFFFFFF;

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = SteamId.class.getSimpleName();

	/**
	 * A Steam ID2 format string.
	 *
	 * <p>Arguments in order:
	 * <ul>
	 *     <li>Integer :: Account universe type identifier.
	 *     <li>Integer :: Account authentication type identifier.
	 *     <li>Integer :: Account type-2 identifier.
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
	 *     <li>Integer :: Account universe type identifier.
	 *     <li>Integer :: Account type-32 identifier.
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
	 * A cache of the conversion to a static account key.
	 */
	private transient Long staticKeyCache;

	/**
	 * A cache of the conversion to an account type-64 identifier.
	 */
	private transient Long id64Cache;

	/**
	 * A cache of the conversion to an account type-2 identifier.
	 */
	private transient String id2Cache;

	/**
	 * A cache of the conversion to an account type-3 identifier.
	 */
	private transient String id3Cache;

	/**
	 * A cache of the conversion to a Steam invite code.
	 */
	private transient String inviteCodeCache;

	/**
	 * A cache of the convertsion to a CS:GO friend code.
	 */
	private transient String csgoCodeCache;

	/**
	 * A cache of the conversion to a /profiles/%id-64% URL.
	 */
	private transient String id64UrlCache;

	/**
	 * A cache of the conversion to a /profiles/%id-3% URL.
	 */
	private transient String id3UrlCache;

	/**
	 * A cache of the conversion to a /user/%invite-code% URL.
	 */
	private transient String userUrlCache;

	/**
	 * A cache of the conversion to a /p/%invite-code% URL.
	 */
	private transient String inviteUrlCache;

	/**
	 * A cache of the conversion to a China /profiles/%id-64% URL.
	 */
	private transient String chinaUrlCache;

	/**
	 * A cache of the conversion to a string.
	 */
	private transient String stringCache;

	/**
	 * Intialize a {@link SteamId} instance.
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
	 * @param xuid	integer value of the account type-32 identifier
	 */
	private SteamId(Integer xuid) {
		this(xuid, ESteamUniverse.PUBLIC, ESteamInstance.DESKTOP, ESteamAccount.INDIVIDUAL);
	}

	/**
	 * Initialize a {@link SteamId} instance.
	 *
	 * <p>Defines a copy consutrctor.
	 *
	 * @param that	instance to copy field values from
	 */
	private SteamId(SteamId that) {
		this(that.xuid, that.universe, that.instance, that.account);

		this.staticKeyCache = that.staticKeyCache;
		this.id64Cache = that.id64Cache;
		this.id2Cache = that.id2Cache;
		this.id3Cache = that.id3Cache;
		this.inviteCodeCache = that.inviteCodeCache;
		this.csgoCodeCache = that.csgoCodeCache;
		this.id64UrlCache = that.id64UrlCache;
		this.id3UrlCache = that.id3UrlCache;
		this.userUrlCache = that.userUrlCache;
		this.inviteUrlCache = that.inviteUrlCache;
		this.chinaUrlCache = that.chinaUrlCache;
		this.stringCache = that.stringCache;
	}

	/**
	 * Check if this account type-32 identifier is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean hasXuid() {
		return this.xuid != null;
	}

	/**
	 * Check if this account universe type is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean hasUniverseType() {
		return this.universe != null;
	}

	/**
	 * Check if this account instance type is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean hasInstanceType() {
		return this.instance != null;
	}

	/**
	 * Check if this account type is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean hasAccountType() {
		return this.account != null;
	}

	/**
	 * Check if this static key cached is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isStaticKeyCached() {
		return this.staticKeyCache != null;
	}

	/**
	 * Check if this account type-64 identifier cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteam64Cached() {
		return this.id64Cache != null;
	}

	/**
	 * Check if this account type-2 identifier cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteam2Cached() {
		return this.id2Cache != null;
	}

	/**
	 * Check if this account type-3 identifier cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteam3Cached() {
		return this.id3Cache != null;
	}

	/**
	 * Check if this invite code cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isInviteCodeCached() {
		return this.inviteCodeCache != null;
	}

	/**
	 * Check if this CS:GO friend code cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isCsgoCodeCached() {
		return this.csgoCodeCache != null;
	}

	/**
	 * Check if this /profiles/%id-64% URL cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteam64UrlCached() {
		return this.id64UrlCache != null;
	}

	/**
	 * Check if this /profiles/%id-3% URL cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteam3UrlCached() {
		return this.id3UrlCache != null;
	}

	/**
	 * Check if this /user/%invite-code% URL cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteamUserUrlCached() {
		return this.userUrlCache != null;
	}

	/**
	 * Check if this /p/%invite-code% URL cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteamInviteUrlCached() {
		return this.inviteUrlCache != null;
	}

	/**
	 * Check if this /profiles/%id-64% China URL cache is not {@code null}.
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if {@code null}
	 */
	public boolean isSteam64ChinaUrlCached() {
		return this.chinaUrlCache != null;
	}

	/**
	 * Check if this string cache is not {@code null};
	 *
	 * @return	{@code true} if not {@code null}
	 * 			or {@code false} if not {@code null}
	 */
	public boolean isStringCached() {
		return this.stringCache != null;
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
	public ESteamUniverse getUniverseTypeOrElse(ESteamUniverse defaultValue) {
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
	public ESteamUniverse getUniverseTypeOrElse(Supplier<ESteamUniverse> defaultValueSupplier) {
		return UwObject.getIfNull(this.getUniverseTypeOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account universe enum type instance
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getUniverseTypeOrElse(ESteamUniverse)}
	 * w/ {@link ESteamUniverse#BASE} as the default value.
	 *
	 * @return	account universe enum type instance
	 */
	public ESteamUniverse getUniverseTypeOrBase() {
		return this.getUniverseTypeOrElse(ESteamUniverse.BASE);
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
	 * <p>Wraps {@link SteamId#getUniverseTypeOrElse(ESteamUniverse)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account universe enum type instance or {@code null}
	 */
	public ESteamUniverse getUniverseTypeOrNull() {
		return this.getUniverseTypeOrElse((ESteamUniverse) null);
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
	 * <p>Delegates {@link ESteamUniverse#getIdOrElse(ESteamUniverse, Integer)}.
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account universe type identifier or the default value
	 */
	public Integer getUniverseTypeIdOrElse(Integer defaultValue) {
		return ESteamUniverse.getIdOrElse(this.universe, defaultValue);
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
	 * <p>Delegates {@link ESteamUniverse#getIdOrElse(ESteamUniverse, Supplier)}.
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account universe type identifier or the default value
	 */
	public Integer getUniverseTypeIdOrElse(Supplier<Integer> defaultValueSupplier) {
		return ESteamUniverse.getIdOrElse(this.universe, defaultValueSupplier);
	}

	/**
	 * Get this account universe type identifier
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#universe} is {@code null}.
	 * </ul>
	 *
	 * <p>Delegates {@link ESteamUniverse#getIdOrBase(ESteamUniverse)}.
	 *
	 * @return	account universe type identifier
	 */
	public Integer getUniverseTypeIdOrBase() {
		return ESteamUniverse.getIdOrBase(this.universe);
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
	 * <p>Delegates {@link ESteamUniverse#getIdOrNull(ESteamUniverse)}.
	 *
	 * @return	account universe type identifier or {@code null}
	 */
	public Integer getUniverseTypeIdOrNull() {
		return ESteamUniverse.getIdOrNull(this.universe);
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
	public ESteamInstance getInstanceTypeOrElse(ESteamInstance defaultValue) {
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
	public ESteamInstance getInstanceTypeOrElse(Supplier<ESteamInstance> defaultValueSupplier) {
		return UwObject.getIfNull(this.getInstanceTypeOrNull(), defaultValueSupplier);
	}

	/**
	 * Get this account instance enum type
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getInstanceTypeOrElse(ESteamInstance)}
	 * w/ {@link ESteamInstance#MIN} as the default value.
	 *
	 * @return	account instance enum type
	 */
	public ESteamInstance getInstanceTypeOrBase() {
		return this.getInstanceTypeOrElse(ESteamInstance.MIN);
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
	 * <p>Wraps {@link SteamId#getInstanceTypeOrElse(ESteamInstance)}
	 * w/ {@code null} as the default value.
	 *
	 * @return	account instance enum type or {@code null}
	 */
	public ESteamInstance getInstanceTypeOrNull() {
		return this.getInstanceTypeOrElse((ESteamInstance) null);
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
	 * <p>Delegates {@link ESteamInstance#getIdOrElse(ESteamInstance, Integer)}.
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account instance type identifier or the default value
	 */
	public Integer getInstanceTypeIdOrElse(Integer defaultValue) {
		return ESteamInstance.getIdOrElse(this.instance, defaultValue);
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
	 * <p>Delegates {@link ESteamInstance#getIdOrElse(ESteamInstance, Supplier)}.
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account instance type identifier or the default value
	 */
	public Integer getInstanceTypeIdOrElse(Supplier<Integer> defaultValueSupplier) {
		return ESteamInstance.getIdOrElse(this.instance, defaultValueSupplier);
	}

	/**
	 * Get this account instance type identifier
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#instance} is {@code null}.
	 * </ul>
	 *
	 * <p>Delegates {@link ESteamInstance#getIdOrBase(ESteamInstance)}.
	 *
	 * @return	account instance type identifier
	 */
	public Integer getInstanceTypeIdOrBase() {
		return ESteamInstance.getIdOrBase(this.instance);
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
	 * <p>Delegates {@link ESteamInstance#getIdOrNull(ESteamInstance)}.
	 *
	 * @return	account instance type identifier or {@code null}
	 */
	public Integer getInstanceTypeIdOrNull() {
		return ESteamInstance.getIdOrNull(this.instance);
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
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link SteamId#getAccountTypeOrElse(ESteamAccount)}
	 * w/ {@link ESteamAccount#BASE} as the default value.
	 *
	 * @return	account type enum type
	 */
	public ESteamAccount getAccountTypeOrBase() {
		return this.getAccountTypeOrElse(ESteamAccount.BASE);
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
	 * <p>Delegates {@link ESteamAccount#getIdOrElse(ESteamAccount, Integer)}.
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account type identifier or the defualt value
	 */
	public Integer getAccountTypeIdOrElse(Integer defaultValue) {
		return ESteamAccount.getIdOrElse(this.account, defaultValue);
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
	 * <p>Delegates {@link ESteamAccount#getIdOrElse(ESteamAccount, Supplier)}.
	 *
	 * @param defaultValueSupplier	supplier from which get the default
	 * @return						account type identifier or the defualt value
	 */
	public Integer getAccountTypeIdOrElse(Supplier<Integer> defaultValueSupplier) {
		return ESteamAccount.getIdOrElse(this.account, defaultValueSupplier);
	}

	/**
	 * Get this account type identifier
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Delegates {@link ESteamAccount#getIdOrBase(ESteamAccount)}.
	 *
	 * @return	account type identifier
	 */
	public Integer getAccountTypeIdOrBase() {
		return ESteamAccount.getIdOrBase(this.account);
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
	 * <p>Delegates {@link ESteamAccount#getIdOrNull(ESteamAccount)}.
	 *
	 * @return	account type identifier or {@code null}
	 */
	public Integer getAccountTypeIdOrNull() {
		return ESteamAccount.getIdOrNull(this.account);
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
	 * <p>Delegates {@link ESteamAccount#getCharOrElse(ESteamAccount, Character)}.
	 *
	 * @param defaultValue	default value to return on failure
	 * @return				account type character or the default value
	 */
	public Character getAccountTypeCharOrElse(Character defaultValue) {
		return ESteamAccount.getCharOrElse(this.account, defaultValue);
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
	 * <p>Delegates {@link ESteamAccount#getCharOrElse(ESteamAccount, Supplier)}.
	 *
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account type character or the default value
	 */
	public Character getAccountTypeCharOrElse(Supplier<Character> defaultValueSupplier) {
		return ESteamAccount.getCharOrElse(this.account, defaultValueSupplier);
	}

	/**
	 * Get this account type character
	 * or return the base value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link SteamId#account} is {@code null}.
	 * </ul>
	 *
	 * <p>Delegates {@link ESteamAccount#getCharOrBase(ESteamAccount)}.
	 *
	 * @return	account type character
	 */
	public Character getAccountTypeCharOrBase() {
		return ESteamAccount.getCharOrBase(this.account);
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
	 * <p>Delegates {@link ESteamAccount#getCharOrNull(ESteamAccount)}.
	 *
	 * @return	account type character or {@code null}
	 */
	public Character getAccountTypeCharOrNull() {
		return ESteamAccount.getCharOrNull(this.account);
	}

	/**
	 * Get this static key cache.
	 *
	 * @return	static key cache
	 */
	public Long getStaticKeyCache() {
		return this.staticKeyCache;
	}

	/**
	 * Get this account type-64 identifier cache.
	 *
	 * @return	account type-64 identifier cache
	 */
	public Long getSteam64Cache() {
		return this.id64Cache;
	}

	/**
	 * Get this account type-2 identifier cache.
	 *
	 * @return	account type-2 identifier cache
	 */
	public String getSteam2Cache() {
		return this.id2Cache;
	}

	/**
	 * Get this account type-3 identifier cache.
	 *
	 * @return	account type-3 identifier cache
	 */
	public String getSteam3Cache() {
		return this.id3Cache;
	}

	/**
	 * Get this invite code cache.
	 *
	 * @return	invite code cache
	 */
	public String getInviteCodeCache() {
		return this.inviteCodeCache;
	}

	/**
	 * Get this CS:GO friend code cache.
	 *
	 * @return	CS:GO friend code cache
	 */
	public String getCsgoCodeCache() {
		return this.csgoCodeCache;
	}

	/**
	 * Get this /profiles/%id-64% URL cache.
	 *
	 * @return	/profiles/%id-64% URL cache
	 */
	public String getSteam64UrlCache() {
		return this.id64UrlCache;
	}

	/**
	 * Get this /profiles/%id-3% URL cache.
	 *
	 * @return	/profiles/%id-3% URL cache
	 */
	public String getSteam3UrlCache() {
		return this.id3UrlCache;
	}

	/**
	 * Get this /user/%invite-code% URL cache.
	 *
	 * @return	/user/%invite-code% URL cache
	 */
	public String getSteamUserUrlCache() {
		return this.userUrlCache;
	}

	/**
	 * Get this /p/%invite-code% URL cache.
	 *
	 * @return	/p/%invite-code% URL cache
	 */
	public String getSteamInviteUrlCache() {
		return this.inviteUrlCache;
	}

	/**
	 * Get this /profiles/%id-64% China URL cache.
	 *
	 * @return	/profiles/%id-64% China URL cache
	 */
	public String getStema64ChinaUrlCache() {
		return this.chinaUrlCache;
	}

	/**
	 * Get this string cache.
	 *
	 * @return	string cache
	 */
	public String getStringCache() {
		return this.stringCache;
	}

	/**
	 * Check if this instance is valid.
	 *
	 * @return	boolean value that describes validity of this instance
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isValid() {
		if (this.xuid == null || this.universe == null
				|| this.instance == null || this.account == null) {
			return false;
		}

		if (this.xuid < BASE_XUID/* && this.xuid > MAX_XUID*/) {
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
		if (this.staticKeyCache != null) {
			return this.staticKeyCache;
		}

		if (!this.isValid()) {
			return defaultValue;
		}

		BigInteger universe = BigInteger.valueOf(this.universe.getId())
				.shiftLeft(USteamBit.ACCOUNT_UNIVERSE_OFFSET);

		BigInteger account = BigInteger.valueOf(this.account.getId())
				.shiftLeft(USteamBit.ACCOUNT_ID_OFFSET);

		try {
			return (this.staticKeyCache = BigInteger.valueOf(this.xuid)
					.add(universe)
					.add(account)
					.longValueExact());
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
		if (this.id64Cache != null) {
			return this.id64Cache;
		}

		if (!this.isValid()) {
			return defaultValue;
		}

		long xuid = this.xuid;
		long instance = this.instance.getId();
		long account = this.account.getId();
		long universe = this.account.getId();

		return (this.id64Cache = xuid << USteamBit.ACCOUNT_ID_OFFSET
				| instance << USteamBit.ACCOUNT_INSTANCE_OFFSET
				| account << USteamBit.ACCOUNT_TYPE_OFFSET
				| universe << USteamBit.ACCOUNT_UNIVERSE_OFFSET);
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
		if (this.id2Cache != null) {
			return this.id2Cache;
		}

		if (!this.isValid()) {
			return defaultValue;
		}

		int x = this.universe.getId();
		int y = this.xuid & 1;
		int z = this.xuid >> 1;

		return (this.id2Cache = String.format(ID2_FMT, x, y, z));
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
	 * @return	string value of the unique account type-2 identifier or the empty one
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
	 * @return	string value of the unique account type-2 identifier or {@code null}
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
		if (this.id3Cache != null) {
			return this.id3Cache;
		}

		if (!this.isValid()) {
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

		return (this.id3Cache = String.format(ID3_FMT, ch, universe, this.xuid, bInstance ? ":" + instance : ""));
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
		if (this.inviteCodeCache != null) {
			return this.inviteCodeCache;
		}

		if (!this.isValid()) {
			return defaultValue;
		}

		return (this.inviteCodeCache = USteamInvite.fromXuidOrElse(this.xuid, defaultValue));
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
	 * @return	string value of the invite code or the empty one
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
	 * @return	string value of the invite code or {@code null}
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
		if (this.csgoCodeCache != null) {
			return this.csgoCodeCache;
		}

		if (!this.isValid()) {
			return defaultValue;
		}

		return (this.csgoCodeCache = USteamCsgo.fromXuidOrElse(this.xuid, defaultValue));
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
		if (this.id64UrlCache != null) {
			return this.id64UrlCache;
		}

		Long id64 = this.toSteam64OrNull();

		if (id64 == null) {
			return defaultValue;
		}

		return (this.id64UrlCache = USteamUrl.PROFILE + id64);
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
		if (this.id3UrlCache != null) {
			return this.id3UrlCache;
		}

		String id3 = this.toSteam3OrNull();

		if (id3 == null) {
			return defaultValue;
		}

		return (this.id3UrlCache = USteamUrl.PROFILE + id3);
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
		if (this.userUrlCache != null) {
			return this.userUrlCache;
		}

		String inviteCode = this.toInviteCodeOrNull();

		if (inviteCode == null) {
			return defaultValue;
		}

		return (this.userUrlCache = USteamUrl.USER + inviteCode);
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
		if (this.inviteUrlCache != null) {
			return this.inviteUrlCache;
		}

		String inviteCode = this.toInviteCodeOrNull();

		if (inviteCode == null) {
			return defaultValue;
		}

		return (this.inviteUrlCache = USteamUrl.INVITE + inviteCode);
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
		if (this.chinaUrlCache != null) {
			return this.chinaUrlCache;
		}

		Long id64 = this.toSteam64OrNull();

		if (id64 == null) {
			return defaultValue;
		}

		return (this.chinaUrlCache = USteamUrl.CHINA + id64);
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
	public SteamId withUniverseType(ESteamUniverse universe) {
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
	public SteamId withUniverseType(Integer universe) {
		return this.withUniverseType(ESteamUniverse.fromIdOrNull(universe));
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #instance} value.
	 *
	 * @param instance	new enum value of the account instance type
	 * @return			new {@link SteamId} instance or this if enum instances are equal
	 */
	public SteamId withInstanceType(ESteamInstance instance) {
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
	public SteamId withInstanceType(Integer instance) {
		return this.withInstanceType(ESteamInstance.fromIdOrNull(instance));
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #account} value.
	 *
	 * @param account	new enum value of the account type
	 * @return			new {@link SteamId} instance or this if enum account types are equal
	 */
	public SteamId withAccountType(ESteamAccount account) {
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
	public SteamId withAccountType(Integer account) {
		return this.withAccountType(ESteamAccount.fromIdOrNull(account));
	}

	/**
	 * Create a new {@link SteamId} instance w/ different {@link #account} value.
	 *
	 * @param account	new character value of the account type
	 * @return			new {@link SteamId} instance or this if account types are equal
	 */
	public SteamId withAccountType(Character account) {
		return this.withAccountType(ESteamAccount.fromCharOrNull(account));
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
		if (this.stringCache != null) {
			return this.stringCache;
		}

		return (this.stringCache = SIMPLE_NAME + "["
				+ "xuid=" + this.xuid
				+ ", universe=" + this.universe
				+ ", instance=" + this.instance
				+ ", account=" + this.account
				+ "]");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SteamId clone() {
		return new SteamId(this);
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
	 * @param xuid	long value of the account type-32 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamXuidValid(Long xuid) {
		return xuid != null
				&& xuid >= MIN_XUID
				&& xuid <= MAX_XUID;
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
	 * @param id2	long value of the type-2 identifier
	 * @return		boolean value that describes validity of the identifier
	 */
	public static boolean isSteamId2Valid(Long id2) {
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
		if (!isSteamXuidValid(xuid)) {
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
		if (!isSteamId64Valid(id64)) {
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
	 * @param id3	string value of the account type-3 identifier
	 * @return		new {@link SteamId} instance or {@code null}
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

		if (xuid == null) {
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
		return UwSystem.disableErrorPrint(() -> {
			return fromSteamAnyOrElse0(obj, defaultValue);
		});
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
	private static SteamId fromSteamAnyOrElse0(Object obj, SteamId defaultValue) {
		if (obj == null) {
			return defaultValue;
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
		 * A simple name of this class.
		 */
		private static final String SIMPLE_NAME = SteamId.SIMPLE_NAME + ":" + Builder.class.getSimpleName();

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
		 * @param that	instance to copy filed values from
		 */
		private Builder(Builder that) {
			this.xuid = that.xuid;
			this.eUniverse = that.eUniverse;
			this.eInstance = that.eInstance;
			this.eAccount = that.eAccount;
			this.iUniverse = that.iUniverse;
			this.iInstance = that.iInstance;
			this.iAccount = that.iAccount;
			this.cAccount = that.cAccount;
		}

		/**
		 * Create a new {@link SteamId} instance
		 * w/ pre-setted field values.
		 *
		 * @return	new {@link SteamId} instance.
		 */
		public SteamId build() {
			ESteamUniverse universe = ESteamUniverse.fromIdOrElse(this.iUniverse, this.eUniverse);
			ESteamInstance instance = ESteamInstance.fromIdOrElse(this.iInstance, this.eInstance);

			ESteamAccount account = ESteamAccount.fromIdOrElse(this.iAccount, this.eAccount);
			account = ESteamAccount.fromCharOrElse(this.cAccount, account);

			return new SteamId(this.xuid, universe, instance, account);
		}

		/**
		 * Check if this account type-32 identifier is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasXuid() {
			return this.xuid != null;
		}

		/**
		 * Check if this account universe type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasUniverseType() {
			return this.eUniverse != null;
		}

		/**
		 * Check if this account instance type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasInstanceType() {
			return this.eInstance != null;
		}

		/**
		 * Check if this account type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasAccountType() {
			return this.eAccount != null;
		}

		/**
		 * Check if this integer account universe type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasIntUniverseType() {
			return this.iUniverse != null;
		}

		/**
		 * Check if this integer account instance type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasIntInstanceType() {
			return this.iInstance != null;
		}

		/**
		 * Check if this integer account type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasIntAccountType() {
			return this.iAccount != null;
		}

		/**
		 * Check if this character account type is not {@code null}.
		 *
		 * @return	{@code true} if not {@code null}
		 * 			or {@code false} if {@code null}
		 */
		public boolean hasCharAccountType() {
			return this.cAccount != null ;
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
		public ESteamUniverse getUniverseType() {
			return this.eUniverse;
		}

		/**
		 * Get this enum instance type
		 *
		 * @return	enum instance type
		 */
		public ESteamInstance getInstanceType() {
			return this.eInstance;
		}

		/**
		 * Get this enum account type
		 *
		 * @return	enum account type
		 */
		public ESteamAccount getAccountType() {
			return this.eAccount;
		}

		/**
		 * Get this integer universe type
		 *
		 * @return	integer universe type
		 */
		public Integer getIntUniverseType() {
			return this.iUniverse;
		}

		/**
		 * Get this integer instance type
		 *
		 * @return	integer instance type
		 */
		public Integer getIntInstanceType() {
			return this.iInstance;
		}

		/**
		 * Get this integer account type
		 *
		 * @return	integer account type
		 */
		public Integer getIntAccountType() {
			return this.iAccount;
		}

		/**
		 * Get this character account type
		 *
		 * @return	character account type
		 */
		public Character getCharAccountType() {
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
		public Builder setUniverseType(ESteamUniverse universe) {
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
		public Builder setUniverseType(Integer universe) {
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
		public Builder setInstanceType(ESteamInstance instance) {
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
		public Builder setInstanceType(Integer instance) {
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
		public Builder setAccountType(ESteamAccount account) {
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
		public Builder setAccountType(Integer account) {
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
		public Builder setAccountType(Character account) {
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

			return this.clone().setXuid(xuid);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #eUniverse} value.
		 *
		 * @param universe	new enum value of the account universe type
		 * @return			new {@link SteamId.Builder} instance or this if enum universes are equal
		 */
		public Builder withUniverseType(ESteamUniverse universe) {
			if (this.eUniverse == universe) {
				return this;
			}

			return this.clone().setUniverseType(universe);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #iUniverse} value.
		 *
		 * @param universe	new integer value of the account universe type
		 * @return			new {@link SteamId.Builder} instance or this if integer universes are equal
		 */
		public Builder withUniverseType(Integer universe) {
			if (Objects.equals(this.iUniverse, universe)) {
				return this;
			}

			return this.clone().setUniverseType(universe);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #eInstance} value.
		 *
		 * @param instance	new enum value of the account instance type
		 * @return			new {@link SteamId.Builder} instance or this if enum instances are equal
		 */
		public Builder withInstanceType(ESteamInstance instance) {
			if (this.eInstance == instance) {
				return this;
			}

			return this.clone().setInstanceType(instance);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #iInstance} value.
		 *
		 * @param instance	new integer value of the account instance type
		 * @return			new {@link SteamId.Builder} instance or this if integer instances are equal
		 */
		public Builder withInstanceType(Integer instance) {
			if (Objects.equals(this.iInstance, instance)) {
				return this;
			}

			return this.clone().setInstanceType(instance);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #eAccount} value.
		 *
		 * @param account	new enum value of the account type
		 * @return			new {@link SteamId.Builder} instance or this if enum account types are equal
		 */
		public Builder withAccountType(ESteamAccount account) {
			if (this.eAccount == account) {
				return this;
			}

			return this.clone().setAccountType(account);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #iAccount} value.
		 *
		 * @param account	new integer value of the account type
		 * @return			new {@link SteamId.Builder} instance or this if integer account types are equal
		 */
		public Builder withAccountType(Integer account) {
			if (Objects.equals(this.iAccount, account)) {
				return this;
			}

			return this.clone().setAccountType(account);
		}

		/**
		 * Create a new {@link SteamId.Builder} instance w/ different {@link #cAccount} value.
		 *
		 * @param account	new character value of the account type
		 * @return			new {@link SteamId.Builder} instance or this if character account types are equal
		 */
		public Builder withAccountType(Character account) {
			if (Objects.equals(this.cAccount, account)) {
				return this;
			}

			return this.clone().setAccountType(account);
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
			return SIMPLE_NAME + "["
					+ "xuid=" + this.xuid
					+ ", eUniverse=" + this.eUniverse
					+ ", eInstance=" + this.eInstance
					+ ", eAccount=" + this.eAccount
					+ ", iUniverse=" + this.iUniverse
					+ ", iInstance=" + this.iInstance
					+ ", iAccount=" + this.iAccount
					+ ", cAccount='" + this.cAccount + "'"
					+ "]";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Builder clone() {
			return new Builder(this);
		}
	}
}
