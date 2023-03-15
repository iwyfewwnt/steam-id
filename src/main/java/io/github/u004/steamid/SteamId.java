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

package io.github.u004.steamid;

import io.github.u004.steamid.exceptions.*;
import io.github.u004.steamid.utils.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import io.github.u004.steamid.types.ESteamAccount;
import io.github.u004.steamid.types.ESteamInstance;
import io.github.u004.steamid.types.ESteamUniverse;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;

import static java.lang.String.format;

/**
 * A Steam ID representation.
 *
 * <p>{@code SteamId} is the class that
 * represents Steam ID as an entity and
 * includes a lot of methods to convert
 * and parse to/from any Steam-like string.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class SteamId {

	/**
	 * A base unique account identifier value.
	 */
	public static final long BASE_XUID = 0x00000000L;

	/**
	 * A minimum unique account identifier value.
	 */
	public static final long MIN_XUID = 0x00000001L;

	/**
	 * A maximum unique account identifier value.
	 */
	public static final long MAX_XUID = 0xFFFFFFFFL;

	/**
	 * A base Steam ID64 value.
	 */
	public static final long BASE_ID64 = 0x0110000100000000L;

	/**
	 * A minimum Steam ID64 value.
	 */
	public static final long MIN_ID64 = 0x0110000100000001L;

	/**
	 * A maximum Steam ID64 value.
	 */
	public static final long MAX_ID64 = 0x01100001FFFFFFFFL;

	/**
	 * A minimum Steam ID2 value.
	 */
	public static final String MIN_ID2 = "STEAM_1:1:0";

	/**
	 * A maximum Steam ID2 value.
	 */
	public static final String MAX_ID2 = "STEAM_1:1:2147483647";

	/**
	 * A minimum Steam ID3 value.
	 */
	public static final String MIN_ID3 = "[U:1:1]";

	/**
	 * A maximum Steam ID3 value.
	 */
	public static final String MAX_ID3 = "[U:1:4294967295]";

	/**
	 * Unique account identifier.
	 */
	private Option<Long> xuid;

	/**
	 * Enum account universe type that wrapped in {@link Option}.
	 */
	private Option<ESteamUniverse> universe;

	/**
	 * Enum account instance type that wrapped in {@link Option}.
	 */
	private Option<ESteamInstance> instance;

	/**
	 * Enum account type that wrapped in {@link Option}.
	 */
	private Option<ESteamAccount> account;

	/**
	 * Initialize a {@code SteamId} instance.
	 *
	 * <p>Wraps {@link SteamId#SteamId(Long)} with
	 * {@link SteamId#BASE_XUID}.
	 */
	public SteamId() {
		this(BASE_XUID);
	}

	/**
	 * Initialize a {@code SteamId} instance.
	 *
	 * <p>Wraps {@link SteamId#setAsIndividual(Long)}.
	 *
	 * @param xuid		unique account identifier
	 */
	public SteamId(Long xuid) {
		this.setAsIndividual(xuid);
	}

	/**
	 * Initialize a {@code SteamId} instance.
	 *
	 * <p>Defines the copy constructor.
	 *
	 * @param xuid			unique account identifier
	 *                      that wrapped in {@link Option}
	 * @param universe		enum account universe type
	 *                      that wrapped in {@link Option}
	 * @param instance		enum account instance type
	 *                      that wrapped in {@link Option}
	 * @param account		enum account type
	 *                   	that wrapped in {@link Option}
	 */
	private SteamId(Option<Long> xuid, Option<ESteamUniverse> universe, Option<ESteamInstance> instance, Option<ESteamAccount> account) {
		this.xuid = xuid;
		this.universe = universe;
		this.instance = instance;
		this.account = account;
	}

	/**
	 * Check if this {@code SteamId} instance is valid.
	 *
	 * @return		boolean value that describes validity
	 * 				of this {@code SteamId} instance
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isValid() {
		Long xuid = this.getXuidRaw();
		ESteamAccount account = this.getAccountRaw();
		ESteamUniverse universe = this.getUniverseRaw();
		ESteamInstance instance = this.getInstanceRaw();

		return xuid >= BASE_XUID
				&& xuid <= MAX_XUID
				&& account != null
				&& universe != null
				&& instance != null
				&& account != ESteamAccount.INVALID
				&& universe != ESteamUniverse.INVALID
				&& (account != ESteamAccount.INDIVIDUAL || xuid >= MIN_XUID && instance != ESteamInstance.WEB)
				&& (account != ESteamAccount.CLAN || xuid >= MIN_XUID && instance != ESteamInstance.ALL)
				&& (account != ESteamAccount.GAME_SERVER || xuid >= MIN_XUID);
	}

	/**
	 * Get this {@code Steamid} instance as a static account key.
	 *
	 * <p>Used for grouping accounts with different account instances.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @return		long value of the static account key
	 * 				that wrapped in {@link Try}
	 *
	 * @throws UnsupportedOperationException	always, method is not implemented
	 */
	public Try<Long> getAsStaticKey() {
		throw new UnsupportedOperationException();
//
//		if (!this.isValid()) {
//			return Try.failure(new InvalidSteamIdStateException());
//		}
//
//		long universe = this.getRawUniverseValue();
//		long account = this.getRawAccountValue();
//
//		return Try.success(this.xuid << USteamBit.ACCOUNT_ID_OFFSET
//				+ universe << ESteamUniverse.OFFSET
//				+ account << ESteamAccount.OFFSET
//		);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam ID64.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link InvalidSteamId64StateException}
	 * </ul>
	 *
	 * @return		long value of the Steam community ID
	 * 				that wrapped in {@link Try}
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 64-bit Systems</a>
	 */
	public Try<Long> getAsSteam64() {
		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		long xuid = this.getXuidRaw();
		long instance = this.getInstanceValueRaw();
		long account = this.getAccountValueRaw();
		long universe = this.getUniverseValueRaw();

		long id64 = xuid << USteamBit.ACCOUNT_ID_OFFSET
				| instance << ESteamInstance.OFFSET
				| account << ESteamAccount.OFFSET
				| universe << ESteamUniverse.OFFSET;

		if (!isSteamId64Valid(id64)) {
			return Try.failure(new InvalidSteamId64StateException());
		}

		return Try.success(id64);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam ID2.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link InvalidSteamId2StateException}
	 * </ul>
	 *
	 * @return		string value of the Steam ID2
	 * 				that wrapped in {@link Try}
	 *
	 * @see <a href="https://vk.cc/ch9ea2">Steam ID2 as Represented in Computer Programs</a>
	 */
	public Try<String> getAsSteam2() {
		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		long xuid = this.getXuidRaw();

		int x = this.getUniverseValueRaw();
		int y = (int) (xuid & 1);
		int z = (int) (xuid >> 1);

		String id2 = format("STEAM_%d:%d:%d", x, y, z);

		if (!isSteamId2Valid(id2)) {
			return Try.failure(new InvalidSteamId2StateException());
		}

		return Try.success(id2);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam ID3.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link InvalidSteamId3StateException}
	 * </ul>
	 *
	 * @return		string value of the Steam ID3
	 * 				that wrapped in {@link Try}
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 32-bit Systems</a>
	 */
	public Try<String> getAsSteam3() {
		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		long xuid = this.getXuidRaw();
		ESteamAccount account = this.getAccountRaw();
		ESteamUniverse universe = this.getUniverseRaw();
		ESteamInstance instance = this.getInstanceRaw();

		boolean bInstance = false;

		char ch = account.getChar();

		switch (account) {
			case CHAT:
				switch (instance) {
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

		int universeId = universe.getValue();
		int instanceId = instance.getValue();

		String id3 = format("[%c:%d:%d%s]", ch, universeId, xuid, bInstance ? ":" + instanceId : "");

		if (!isSteamId3Valid(id3)) {
			return Try.failure(new InvalidSteamId3StateException());
		}

		return Try.success(id3);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam invite code.
	 *
	 * <p>Wraps {@link USteamInvite#toCode(Long)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamInviteCodeStateException}
	 * </ul>
	 *
	 * @return		string value of the Steam invite code
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsSteamInviteCode() {
		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		return USteamInvite.toCode(this.getXuidRaw());
	}

	/**
	 * Get this {@code SteamId} instance as a CS:GO friend code.
	 *
	 * <p>Wraps {@link USteamCsgo#toFriendCode(Long)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidCsgoFriendCodeStateException}
	 * </ul>
	 *
	 * @return		string value of the CS:GO friend code
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsCsgoFriendCode() {
		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		return USteamCsgo.toFriendCode(this.getXuidRaw());
	}

	/**
	 * Get this {@code Steamid} instance as a raw static account key.
	 *
	 * <p>Used for grouping accounts with different account instances.
	 *
	 * @return		long value of the static account key or null
	 *
	 * @throws UnsupportedOperationException	always, method is not implemented
	 */
	public Long getAsStaticKeyRaw() {
		return this.getAsStaticKey().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam ID64.
	 *
	 * @return		long value of the Steam community ID or null
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 64-bit Systems</a>
	 */
	public Long getAsSteam64Raw() {
		return this.getAsSteam64().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam ID2.
	 *
	 * @return		string value of the Steam ID2 or null
	 *
	 * @see <a href="https://vk.cc/ch9ea2">Steam ID2 as Represented in Computer Programs</a>
	 */
	public String getAsSteam2Raw() {
		return this.getAsSteam2().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam ID3.
	 *
	 * @return		string value of the Steam ID3 or null
	 *
	 * @see <a href="https://vk.cc/ch9dMy">Steam ID as a Steam Community ID for 32-bit Systems</a>
	 */
	public String getAsSteam3Raw() {
		return this.getAsSteam3().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam invite code.
	 *
	 * <p>Wraps {@link USteamInvite#toCode(Long)}.
	 *
	 * @return		string value of the Steam invite code or null
	 */
	public String getAsSteamInviteCodeRaw() {
		return this.getAsSteamInviteCode().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw CS:GO friend code.
	 *
	 * <p>Wraps {@link USteamCsgo#toFriendCode(Long)}.
	 *
	 * @return		string value of the CS:GO friend code or null
	 */
	public String getAsCsgoFriendCodeRaw() {
		return this.getAsCsgoFriendCode().getOrNull();
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam ID64.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id64		long value of the Steam ID64
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteam64(Long id64) {
		if (!isSteamId64Valid(id64)) {
			return Try.failure(new IllegalArgumentException());
		}

		this.setXuid(USteamBit.ACCOUNT_ID.get(id64));

		this.instance = ESteamInstance.fromValue((int) USteamBit.ACCOUNT_INSTANCE.get(id64));
		this.account = ESteamAccount.fromValue((int) USteamBit.ACCOUNT_TYPE.get(id64));
		this.universe = ESteamUniverse.fromValue((int) USteamBit.ACCOUNT_UNIVERSE.get(id64));

		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		return Try.success(this);
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam ID64.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam64(Long)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id64		string value of the Steam ID64
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteam64(String id64) {
		id64 = StringUtils.trimToEmpty(id64);

		if (id64.matches(USteamRegex.STEAM_64)) {
			try {
				return this.setAsSteam64(Long.parseUnsignedLong(id64));
			} catch (NumberFormatException ignored) {
			}
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam ID2.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id2		string value of the Steam ID2
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteam2(String id2) {
		id2 = StringUtils.trimToEmpty(id2);

		Matcher m = USteamPattern.STEAM_2.matcher(id2);

		if (m.matches()) {
			try {
				long id = Long.parseUnsignedLong(m.group(USteamRegex.Group.ID));
				long auth = Long.parseUnsignedLong(m.group(USteamRegex.Group.AUTH));

				return this.setAsIndividual((id << 1) | auth);
			} catch (NumberFormatException ignored) {
			}
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam ID3.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id3		string value of the Steam ID3
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteam3(String id3) {
		id3 = StringUtils.trimToEmpty(id3);

		Matcher m = USteamPattern.STEAM_3.matcher(id3);

		if (m.matches()) {
			try {
				long id = Long.parseUnsignedLong(m.group(USteamRegex.Group.ID));
				int universe = Integer.parseUnsignedInt(m.group(USteamRegex.Group.UNIVERSE));

				String typeStr = m.group(USteamRegex.Group.ACCOUNT);
				if (typeStr.length() == 1) {
					char type = typeStr.charAt(0);

					this.setXuid(id);

					this.universe = ESteamUniverse.fromValue(universe);

					try {
						this.instance = ESteamInstance.fromValue(
								Integer.parseUnsignedInt(m.group(USteamRegex.Group.INSTANCE))
						);
					} catch (NumberFormatException ignored) {
						switch (type) {
							case USteamAccount.CLAN_CHAR:
							case USteamAccount.CHAT_CHAR:
							case USteamAccount.CLAN_CHAT_CHAR:
							case USteamAccount.LOBBY_CHAT_CHAR:
								this.setInstance(ESteamInstance.ALL);
								break;

							default:
								this.setInstance(ESteamInstance.DESKTOP);
								break;
						}
					}

					switch (type) {
						case USteamAccount.CLAN_CHAT_CHAR:
						case USteamAccount.LOBBY_CHAT_CHAR:
							this.setAccount(ESteamAccount.CHAT);
							break;

						default:
							this.account = ESteamAccount.fromChar(type);
							break;
					}

					if (!this.isValid()) {
						return Try.failure(new InvalidSteamIdStateException());
					}

					return Try.success(this);
				}
			} catch (NumberFormatException ignored) {
			}
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam invite code.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param code		string value of the Steam invite code
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteamInviteCode(String code) {
		return USteamInvite.fromCode(code)
				.flatMap(this::setAsIndividual);
	}

	/**
	 * Initialize this {@code SteamId} instance from interface-friendly CS:GO friend code.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param code		string value of the interface-friendly CS:GO friend code
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsCsgoFriendCode(String code) {
		return USteamCsgo.fromFriendCode(code)
				.flatMap(this::setAsIndividual);
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam /profiles/ URL.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param url		string value of the Steam /profiles/ URL
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteamProfileUrl(String url) {
		url = StringUtils.trimToEmpty(url);

		Matcher m = USteamPattern.PROFILE_URL.matcher(url);

		if (m.matches()) {
			String id = m.group(USteamRegex.Group.ID);

			try {
				return this.setAsSteam64(Long.parseUnsignedLong(id));
			} catch (NumberFormatException ignored) {
			}

			return this.setAsSteam3(id);
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam /user/ URL.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param url		string value of the Steam /user/ URL
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteamUserUrl(String url) {
		url = StringUtils.trimToEmpty(url);

		Matcher m = USteamPattern.USER_URL.matcher(url);

		if (m.matches()) {
			String code = m.group(USteamRegex.Group.ID);
			return this.setAsSteamInviteCode(code);
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam URL
	 *
	 * <p>Wraps all {@code SteamId#setAsSteam*Url} methods in order:
	 * <ul>
	 *     <li>{@link SteamId#setAsSteamProfileUrl(String)}
	 *     <li>{@link SteamId#setAsSteamUserUrl(String)}
	 * </ul>
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param url		string value of the Steam /profiles/ or /user/ URL
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteamUrl(String url) {
		return this.setAsSteamProfileUrl(url)
				.recoverWith($ -> this.setAsSteamUserUrl(url));
	}

	/**
	 * Initialize this {@code SteamId} instance from Steam-like string.
	 *
	 * <p>Wraps all {@code SteamId#setAs*} methods in order:
	 * <ul>
	 *     <li>{@link SteamId#setAsSteam64(String)}
	 *     <li>{@link SteamId#setAsSteam2(String)}
	 *     <li>{@link SteamId#setAsSteam3(String)}
	 *     <li>{@link SteamId#setAsSteamInviteCode(String)}
	 *     <li>{@link SteamId#setAsCsgoFriendCode(String)}
	 *     <li>{@link SteamId#setAsSteamUrl(String)}
	 * </ul>
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param str		Steam-like string
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsSteam(String str) {
		return this.setAsSteam64(str)
				.recoverWith($ -> this.setAsSteam2(str))
				.recoverWith($ -> this.setAsSteam3(str))
				.recoverWith($ -> this.setAsSteamInviteCode(str))
				.recoverWith($ -> this.setAsCsgoFriendCode(str))
				.recoverWith($ -> this.setAsSteamUrl(str));
	}

	/**
	 * Get this {@code SteamId} instance as a Steam URL that includes Steam ID64.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link InvalidSteamId64StateException}
	 * </ul>
	 *
	 * @return		string value of the Steam /profiles/%id64% URL
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsSteam64Url() {
		return this.getAsSteam64()
				.map(id64 -> USteamUrl.PROFILE + id64);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam URL that includes Steam ID3.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link InvalidSteamId3StateException}
	 * </ul>
	 *
	 * @return		string value of the Steam /profiles/%id3% URL
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsSteam3Url() {
		return this.getAsSteam3()
				.map(id3 -> USteamUrl.PROFILE + id3);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam /user/ URL.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamInviteCodeStateException}
	 * </ul>
	 *
	 * @return		string value of the Steam /user/%invite-code% URL
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsSteamUserUrl() {
		return this.getAsSteamInviteCode()
				.map(code -> USteamUrl.USER + code);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam /p/ URL.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamInviteCodeStateException}
	 * </ul>
	 *
	 * @return		string value of the Steam /p/%invite-code% URL
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsSteamInviteUrl() {
		return this.getAsSteamInviteCode()
				.map(code -> USteamUrl.INVITE + code);
	}

	/**
	 * Get this {@code SteamId} instance as a Steam China /profiles/ URL.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 *     <li>{@link InvalidSteamId64StateException}
	 * </ul>
	 *
	 * @return		string value of the Steam China /profiles/%id64% URL
	 * 				that wrapped in {@link Try}
	 */
	public Try<String> getAsSteam64ChinaUrl() {
		return this.getAsSteam64()
				.map(id64 -> USteamUrl.CHINA + id64);
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam URL that includes Steam ID64.
	 *
	 * @return		string value of the Steam /profiles/%id64% URL or null
	 */
	public String getAsSteam64UrlRaw() {
		return this.getAsSteam64Url().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam URL that includes Steam ID3.
	 *
	 * @return		string value of the Steam /profiles/%id3% URL or null
	 */
	public String getAsSteam3UrlRaw() {
		return this.getAsSteam3Url().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam /user/ URL.
	 *
	 * @return		string value of the Steam /user/%invite-code% URL or null
	 */
	public String getAsSteamUserUrlRaw() {
		return this.getAsSteamUserUrl().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam /p/ URL.
	 *
	 * @return		string value of the Steam /p/%invite-code% URL or null
	 */
	public String getAsSteamInviteUrlRaw() {
		return this.getAsSteamInviteUrl().getOrNull();
	}

	/**
	 * Get this {@code SteamId} instance as a raw Steam China /profiles/ URL.
	 *
	 * @return		string value of the Steam China /profiles/%id64% URL or null
	 */
	public String getAsSteam64ChinaUrlRaw() {
		return this.getAsSteam64ChinaUrl().getOrNull();
	}

	/**
	 * Initialize this {@code SteamId} instance from unique Steam account identifier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param xuid		unique account identifier
	 * @return			this {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public Try<SteamId> setAsIndividual(Long xuid) {
		this.setXuid(xuid)
				.setUniverse(ESteamUniverse.PUBLIC)
				.setInstance(ESteamInstance.DESKTOP)
				.setAccount(ESteamAccount.INDIVIDUAL);

		if (!this.isValid()) {
			return Try.failure(new InvalidSteamIdStateException());
		}

		return Try.success(this);
	}

	/**
	 * Get this unique account identifier.
	 *
	 * @return		long value of this unique account identifier
	 * 				that wrapped in {@link Option}
	 */
	public Option<Long> getXuid() {
		return this.xuid;
	}

	/**
	 * Get this enum account universe type.
	 *
	 * @return		this enum account universe type
	 * 				that wrapped in {@link Option}
	 */
	public Option<ESteamUniverse> getUniverse() {
		return this.universe;
	}

	/**
	 * Get this enum account instance type.
	 *
	 * @return		this enum account instance type
	 * 				that wrapped in {@link Option}
	 */
	public Option<ESteamInstance> getInstance() {
		return this.instance;
	}

	/**
	 * Get this enum account type.
	 *
	 * @return		this enum account type
	 * 				that wrapped in {@link Option}
	 */
	public Option<ESteamAccount> getAccount() {
		return this.account;
	}

	/**
	 * Get this raw unique account identifier.
	 *
	 * @return		long value of this unique account identifier
	 * 				or null
	 */
	public Long getXuidRaw() {
		return this.xuid.getOrNull();
	}

	/**
	 * Get this raw enum account universe type.
	 *
	 * @return		this enum account universe type
	 * 				or null
	 */
	public ESteamUniverse getUniverseRaw() {
		return this.universe.getOrNull();
	}

	/**
	 * Get this raw enum account instance type.
	 *
	 * @return		this enum account instance type
	 * 				or null
	 */
	public ESteamInstance getInstanceRaw() {
		return this.instance.getOrNull();
	}

	/**
	 * Get this raw enum account type.
	 *
	 * @return		this enum account type
	 * 				or null
	 */
	public ESteamAccount getAccountRaw() {
		return this.account.getOrNull();
	}

	/**
	 * Get the value of this enum account universe type.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link NoSuchElementException}
	 * </ul>
	 *
	 * @return		integer value of this enum account universe type
	 * 				that wrapped in {@link Try}
	 */
	public Try<Integer> getUniverseValue() {
		return this.universe.toTry()
				.map(ESteamUniverse::getValue);
	}

	/**
	 * Get the value of this enum account instance type.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link NoSuchElementException}
	 * </ul>
	 *
	 * @return		integer value of this enum account instance type
	 * 				that wrapped in {@link Try}
	 */
	public Try<Integer> getInstanceValue() {
		return this.instance.toTry()
				.map(ESteamInstance::getValue);
	}

	/**
	 * Get the value of this enum account type.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link NoSuchElementException}
	 * </ul>
	 *
	 * @return		integer value of this enum account type
	 * 				that wrapped in {@link Try}
	 */
	public Try<Integer> getAccountValue() {
		return this.account.toTry()
				.map(ESteamAccount::getValue);
	}

	/**
	 * Get the char of this enum account type.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link NoSuchElementException}
	 * </ul>
	 *
	 * @return		char value of this enum account type
	 * 				that wrapped in {@link Try}
	 */
	public Try<Character> getAccountChar() {
		return this.account.toTry()
				.map(ESteamAccount::getChar);
	}

	/**
	 * Get the raw value of this enum account universe type.
	 *
	 * @return		integer value of this enum account universe type
	 * 				or null
	 */
	public Integer getUniverseValueRaw() {
		return this.getUniverseValue()
				.getOrNull();
	}


	/**
	 * Get the raw value of this enum account instance type.
	 *
	 * @return		integer value of this enum account instance type
	 * 				or null
	 */
	public Integer getInstanceValueRaw() {
		return this.getInstanceValue()
				.getOrNull();
	}

	/**
	 * Get the raw value of this enum account type.
	 *
	 * @return		integer value of this enum account type
	 * 				or null
	 */
	public Integer getAccountValueRaw() {
		return this.getAccountValue()
				.getOrNull();
	}

	/**
	 * Get the raw char of this enum account type.
	 *
	 * @return		char value of this enum account type
	 * 				or null
	 */
	public Character getAccountCharRaw() {
		return this.getAccountChar()
				.getOrNull();
	}

	/**
	 * Set this unique account identifier to a new value.
	 *
	 * @param xuid		new value of the unique account identifier
	 * @return			this {@code SteamId} instance
	 */
	public SteamId setXuid(Long xuid) {
		this.xuid = Option.of(xuid);
		return this;
	}

	/**
	 * Set this enum account universe type to a new value.
	 *
	 * @param universe		new value of the enum account universe type
	 * @return				this {@code SteamId} instance
	 */
	public SteamId setUniverse(ESteamUniverse universe) {
		this.universe = Option.of(universe);
		return this;
	}

	/**
	 * Set this enum account instance type to a new value.
	 *
	 * @param instance		new value of the enum account instance typ
	 * @return				this {@code SteamId} instance
	 */
	public SteamId setInstance(ESteamInstance instance) {
		this.instance = Option.of(instance);
		return this;
	}

	/**
	 * Set this enum account type to a new value.
	 *
	 * @param account		new value of the enum account type
	 * @return				this {@code SteamId} instance
	 */
	public SteamId setAccount(ESteamAccount account) {
		this.account = Option.of(account);
		return this;
	}

	/**
	 * Create a {@code SteamId} instance with new unique account identifier value.
	 *
	 * @param xuid		new value of the unique account identifier
	 * @return			new {@code SteamId} instance
	 */
	public SteamId withXuid(Long xuid) {
		return new SteamId(Option.of(xuid), this.universe, this.instance, this.account);
	}

	/**
	 * Create a {@code SteamId} instance with new enum account universe type value.
	 *
	 * @param universe		new value of the enum account universe type
	 * @return				new {@code SteamId} instance
	 */
	public SteamId withUniverse(ESteamUniverse universe) {
		return new SteamId(this.xuid, Option.of(universe), this.instance, this.account);
	}

	/**
	 * Create a {@code SteamId} instance with new enum account instance type value.
	 *
	 * @param instance		new value of the enum account instance type
	 * @return				new {@code SteamId} instance
	 */
	public SteamId withInstance(ESteamInstance instance) {
		return new SteamId(this.xuid, this.universe, Option.of(instance), this.account);
	}

	/**
	 * Create a {@code SteamId} instance with new enum account type value.
	 *
	 * @param account		new value of the enum account type
	 * @return				new {@code SteamId} instance
	 */
	public SteamId withAccount(ESteamAccount account) {
		return new SteamId(this.xuid, this.universe, this.instance, Option.of(account));
	}

	/**
	 * Override {@link Object#equals(Object)}.
	 *
	 * @param o		reference object with which to compare
	 * @return		{@code true} if this {@code SteamId} instance is the same
	 * 				as the object argument, {@code false} otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		SteamId steamId = (SteamId) o;

		return new EqualsBuilder()
				.append(this.xuid, steamId.xuid)
				.append(this.universe, steamId.universe)
				.append(this.instance, steamId.instance)
				.append(this.account, steamId.account)
				.isEquals();
	}


	/**
	 * Override {@link Object#hashCode()}.
	 *
	 * @return	hash code value of this {@code SteamId} instance
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(this.xuid)
				.append(this.universe)
				.append(this.instance)
				.append(this.account)
				.toHashCode();
	}

	/**
	 * Override {@link Object#toString()}.
	 *
	 * @return		string representation of this {@code SteamId} instance
	 */
	@Override
	public String toString() {
		return "SteamId{"
				+ "xuid=" + this.getXuidRaw()
				+ ", universe=" +  this.getUniverseRaw()
				+ ", instance=" + this.getInstanceRaw()
				+ ", account=" + this.getAccountRaw()
				+ "}";
	}

	/**
	 * Check if provided Steam XUID is valid.
	 *
	 * @param xuid		Steam XUID, may be null
	 * @return			boolean value as a result
	 */
	public static boolean isSteamXuidValid(Long xuid) {
		return xuid != null
				&& xuid >= MIN_XUID
				&& xuid <= MAX_XUID;
	}

	/**
	 * Check if provided Steam ID64 is valid.
	 *
	 * @param id64		Steam ID64, may be null
	 * @return			boolean value as a result
	 */
	public static boolean isSteamId64Valid(Long id64) {
		return id64 != null
				&& id64 >= MIN_ID64
				&& id64 <= MAX_ID64;
	}

	/**
	 * Check if provided Steam ID64 is valid.
	 *
	 * @param id64		Steam ID64, may be null
	 * @return			boolean value as a result
	 */
	public static boolean isSteamId64Valid(String id64) {
		id64 = ObjectUtils.defaultIfNull(id64, "");

		if (id64.matches(USteamRegex.STEAM_64)) {
			String finalId64 = id64;

			Long id = Try.of(() -> Long.parseUnsignedLong(finalId64))
					.getOrNull();

			return isSteamId64Valid(id);
		}

		return false;
	}

	/**
	 * Check if provided Steam ID2 is valid.
	 *
	 * @param id2		Steam ID2, may be null
	 * @return			boolean value as a result
	 */
	public static boolean isSteamId2Valid(String id2) {
		id2 = ObjectUtils.defaultIfNull(id2, "");

		Matcher m = USteamPattern.STEAM_2.matcher(id2);

		if (m.matches()) {
			Long id = Try.of(() -> Long.parseUnsignedLong(m.group(USteamRegex.Group.ID)))
					.getOrNull();

			Long auth = Try.of(() -> Long.parseUnsignedLong(m.group(USteamRegex.Group.AUTH)))
					.getOrNull();

			Long xuid = Try.of(() -> id << 1 | auth)
					.getOrNull();

			return isSteamXuidValid(xuid);
		}

		return false;
	}

	/**
	 * Check if provided Steam ID3 is valid.
	 *
	 * @param id3		Steam ID3, may be null
	 * @return			boolean value as a result
	 */
	public static boolean isSteamId3Valid(String id3) {
		id3 = ObjectUtils.defaultIfNull(id3, "");

		Matcher m = USteamPattern.STEAM_3.matcher(id3);

		if (m.matches()) {
			Long xuid = Try.of(() -> Long.parseUnsignedLong(m.group(USteamRegex.Group.ID)))
					.getOrNull();

			return isSteamXuidValid(xuid);
		}

		return false;
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID64.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam64(Long)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id64		long value of the Steam ID64
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteam64(Long id64) {
		return new SteamId().setAsSteam64(id64);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID64.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam64(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id64		string value of the Steam ID64
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteam64(String id64) {
		return new SteamId().setAsSteam64(id64);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID2.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam2(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id2		string value of the Steam ID2
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteam2(String id2) {
		return new SteamId().setAsSteam2(id2);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID3.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam3(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param id3		string value of the Steam ID3
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteam3(String id3) {
		return new SteamId().setAsSteam3(id3);
	}


	/**
	 * Create a {@code SteamId} instance and initialize it from Steam invite code.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamInviteCode(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param code		string value of the Steam invite code
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteamInviteCode(String code) {
		return new SteamId().setAsSteamInviteCode(code);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from interface-friendly CS:GO friend code.
	 *
	 * <p>Wraps {@link SteamId#setAsCsgoFriendCode(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param code		string value of the interface-friendly CS:GO friend code
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromCsgoFriendCode(String code) {
		return new SteamId().setAsCsgoFriendCode(code);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam /profiles/ URL.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamProfileUrl(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param url		string value of the Steam /profiles/ URL
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteamProfileUrl(String url) {
		return new SteamId().setAsSteamProfileUrl(url);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam /user/ URL.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamUserUrl(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param url		string value of the Steam /user/ URL
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteamUserUrl(String url) {
		return new SteamId().setAsSteamUserUrl(url);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam URL.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamUrl(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param url		string value of the Steam /profiles/ or /user/ URL
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteamUrl(String url) {
		return new SteamId().setAsSteamUrl(url);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam-like string.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamIdStateException}
	 * </ul>
	 *
	 * @param str		Steam-like string
	 * @return			new {@code SteamId} instance
	 * 					that wrapped in {@link Try}
	 */
	public static Try<SteamId> fromSteam(String str) {
		return new SteamId().setAsSteam(str);
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID64.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam64(Long)}.
	 *
	 * @param id64		long value of the Steam ID64
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteam64Raw(Long id64) {
		return fromSteam64(id64).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID64.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam64(String)}.
	 *
	 * @param id64		string value of the Steam ID64
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteam64Raw(String id64) {
		return fromSteam64(id64).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID2.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam2(String)}.
	 *
	 * @param id2		string value of the Steam ID2
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteam2Raw(String id2) {
		return fromSteam2(id2).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam ID3.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam3(String)}.
	 *
	 * @param id3		string value of the Steam ID3
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteam3Raw(String id3) {
		return fromSteam3(id3).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam invite code.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamInviteCode(String)}.
	 *
	 * @param code		string value of the Steam invite code
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteamInviteCodeRaw(String code) {
		return fromSteamInviteCode(code).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from interface-friendly CS:GO friend code.
	 *
	 * <p>Wraps {@link SteamId#setAsCsgoFriendCode(String)}.
	 *
	 * @param code		string value of the interface-friendly CS:GO friend code
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromCsgoFriendCodeRaw(String code) {
		return fromCsgoFriendCode(code).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam /profiles/ URL.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamProfileUrl(String)}.
	 *
	 * @param url		string value of the Steam /profiles/ URL
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteamProfileUrlRaw(String url) {
		return fromSteamProfileUrl(url).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam /user/ URL.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamUserUrl(String)}.
	 *
	 * @param url		string value of the Steam /user/ URL
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteamUserUrlRaw(String url) {
		return fromSteamUserUrl(url).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam URL.
	 *
	 * <p>Wraps {@link SteamId#setAsSteamUrl(String)}.
	 *
	 * @param url		string value of the Steam /profiles/ or /user/ URL
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteamUrlRaw(String url) {
		return fromSteamUrl(url).getOrNull();
	}

	/**
	 * Create a {@code SteamId} instance and initialize it from Steam-like string.
	 *
	 * <p>Wraps {@link SteamId#setAsSteam(String)}.
	 *
	 * @param str		Steam-like string
	 * @return			new {@code SteamId} instance or null
	 */
	public static SteamId fromSteamRaw(String str) {
		return fromSteam(str).getOrNull();
	}
}
