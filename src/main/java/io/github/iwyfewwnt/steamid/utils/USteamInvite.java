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

package io.github.iwyfewwnt.steamid.utils;

import io.github.iwyfewwnt.steamid.SteamId;
import io.github.iwyfewwnt.uwutils.UwObject;
import io.github.iwyfewwnt.uwutils.UwString;

import java.util.function.Supplier;

/**
 * A Steam invite utility.
 *
 * <p>{@code USteamInvite} is the utility class to make
 * conversions between {@code SteamId}'s xuid and Steam invite code.
 */
@SuppressWarnings("unused")
public final class USteamInvite {

	/**
	 * A minimum Steam invite code value.
	 */
	public static final String MIN_CODE = "c";

	/**
	 * A maximum Steam invite code value.
	 */
	public static final String MAX_CODE = "wwww-wwww";

	/**
	 * A Steam xuid base.
	 */
	public static final String XUID_BASE = "0123456789abcdef";

	/**
	 * A Steam invite code base.
	 */
	public static final String CODE_BASE = "bcdfghjkmnpqrtvw";

	/**
	 * A Steam invite code delimiter.
	 */
	public static final String CODE_DELIMITER = "-";

	/**
	 * Convert a unique Steam account identifier to a Steam invite code
	 * or return a default value if failed.
	 *
	 * <p>Converts a unique account identifier to hex string and replaces characters using two
	 * bases in order - {@value USteamInvite#XUID_BASE} and {@value USteamInvite#CODE_BASE}.
	 * Then places {@value USteamInvite#CODE_DELIMITER} if the length of the resulting
	 * code is greater than three.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam unique account identifier isn't valid.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) defualtValue
	 *     USteamInvite.fromXuidOrElse(0L, <defaultValue>);
	 *
	 *     // (String) defaultValue
	 *     USteamInvite.fromXuidOrElse(Long.MAX_VALUE, <defaultValue>);
	 *
	 *     // (String) "c"
	 *     USteamInvite.fromXuidOrElse(1L, <defaultValue>);
	 *
	 *     // (String) "gqkj-gkbr"
	 *     USteamInvite.fromXuidOrElse(1266042636L, <defaultValue>);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid			Steam unique account identifier to convert
	 * @param defaultValue	default value to return on failure
	 * @return				Steam invite code or the default value
	 */
	public static String fromXuidOrElse(Long xuid, String defaultValue) {
		if (SteamId.isSteamXuidValid(xuid)) {
			return defaultValue;
		}

		String code = UwString.toBaseOrNull(Long.toHexString(xuid), XUID_BASE, CODE_BASE);

		if (code == null) {
			return defaultValue;
		}

		int idx = code.length() >> 1;

		if (idx > 1) {
			code = code.substring(0, idx)
					+ CODE_DELIMITER
					+ code.substring(idx);
		}

//		if (!code.matches(USteamRegex.INVITE_CODE)) {
//			return defaultValue;
//		}

		return code;
	}

	/**
	 * Convert a unique Steam account identifier to a Steam invite code
	 * or return a default value if failed.
	 *
	 * <p>Converts a unique account identifier to hex string and replaces characters using two
	 * bases in order - {@value USteamInvite#XUID_BASE} and {@value USteamInvite#CODE_BASE}.
	 * Then places {@value USteamInvite#CODE_DELIMITER} if the length of the resulting
	 * code is greater than three.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam unique account identifier isn't valid.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) defualtValue
	 *     USteamInvite.fromXuidOrElse(0L, <defaultValueSupplier>);
	 *
	 *     // (String) defaultValue
	 *     USteamInvite.fromXuidOrElse(Long.MAX_VALUE, <defaultValueSupplier>);
	 *
	 *     // (String) "c"
	 *     USteamInvite.fromXuidOrElse(1L, <defaultValueSupplier>);
	 *
	 *     // (String) "gqkj-gkbr"
	 *     USteamInvite.fromXuidOrElse(1266042636L, <defaultValueSupplier>);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid					Steam unique account identifier to convert
	 * @param defaultValueSupplier	supplie from which get the default value
	 * @return						Steam invite code or the default value
	 */
	public static String fromXuidOrElse(Long xuid, Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(fromXuidOrNull(xuid), defaultValueSupplier);
	}

	/**
	 * Convert a unique Steam account identifier to a Steam invite code
	 * or return an empty string if failed.
	 *
	 * <p>Converts a unique account identifier to hex string and replaces characters using two
	 * bases in order - {@value USteamInvite#XUID_BASE} and {@value USteamInvite#CODE_BASE}.
	 * Then places {@value USteamInvite#CODE_DELIMITER} if the length of the resulting
	 * code is greater than three.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam unique account identifier isn't valid.
	 * </ul>
	 *
	 * <p>Wraps {@link USteamInvite#fromXuidOrElse(Long, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) ""
	 *     USteamInvite.fromXuidOrEmpty(0L);
	 *
	 *     // (String) ""
	 *     USteamInvite.fromXuidOrEmpty(Long.MAX_VALUE);
	 *
	 *     // (String) "c"
	 *     USteamInvite.fromXuidOrEmpty(1L);
	 *
	 *     // (String) "gqkj-gkbr"
	 *     USteamInvite.fromXuidOrEmpty(1266042636L);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	Steam unique account identifier to convert
	 * @return		Steam invite code or the empty string
	 */
	public static String fromXuidOrEmpty(Long xuid) {
		return fromXuidOrElse(xuid, UwString.EMPTY);
	}

	/**
	 * Convert a unique Steam account identifier to a Steam invite code
	 * or return {@code null} if failed.
	 *
	 * <p>Converts a unique account identifier to hex string and replaces characters using two
	 * bases in order - {@value USteamInvite#XUID_BASE} and {@value USteamInvite#CODE_BASE}.
	 * Then places {@value USteamInvite#CODE_DELIMITER} if the length of the resulting
	 * code is greater than three.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam unique account identifier isn't valid.
	 * </ul>
	 *
	 * <p>Wraps {@link USteamInvite#fromXuidOrElse(Long, String)}
	 * w/ {@code null} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) null
	 *     USteamInvite.fromXuidOrNull(0L);
	 *
	 *     // (String) null
	 *     USteamInvite.fromXuidOrNull(Long.MAX_VALUE);
	 *
	 *     // (String) "c"
	 *     USteamInvite.fromXuidOrNull(1L);
	 *
	 *     // (String) "gqkj-gkbr"
	 *     USteamInvite.fromXuidOrNull(1266042636L);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	Steam unique account identifier to convert
	 * @return		Steam invite code or the default value
	 */
	public static String fromXuidOrNull(Long xuid) {
		return fromXuidOrElse(xuid, (String) null);
	}

	/**
	 * Convert a Steam invite code to a unique Steam account identifier
	 * or return a default value if failed.
	 *
	 * <p>Removes {@value USteamInvite#CODE_DELIMITER} and replaces characters using two
	 * bases in order - {@value USteamInvite#CODE_BASE} and {@value USteamInvite#XUID_BASE}.
	 * Then parses the resulting code to an unsigned 64-bit integer.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>Steam invite code is {@code null}.
	 *     <li>Steam invite code doesn't match w/ {@link USteamRegex#INVITE_CODE}.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Long) defaultValue
	 *     USteamInvite.toXuidOrElse(null, <defaultValue>);
	 *
	 *     // (Long) defaultValue
	 *     USteamInvite.toXuidOrElse("", <defaultValue>);
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrElse("c", <defaultValue>);
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrElse("  c  ", <defaultValue>);
	 *
	 *     // (Long) 1266042636
	 *     USteamInvite.toXuidOrElse("gqkj-gkbr", <defaultValue>);
	 * }</pre>
	 * <hr>
	 *
	 * @param code			Steam invite code to convert
	 * @param defaultValue 	default value to return on failure
	 * @return				Steam unique account identifier or the default value
	 */
	public static Long toXuidOrElse(String code, Long defaultValue) {
		if (code == null) {
			return defaultValue;
		}

		code = code.trim();

		if (!code.matches(USteamRegex.INVITE_CODE)) {
			return defaultValue;
		}

		code = code.replace(CODE_DELIMITER, "");
		code = UwString.toBaseOrNull(code, CODE_BASE, XUID_BASE);

		if (code == null) {
			return defaultValue;
		}

		try {
			return Long.parseUnsignedLong(code, 16);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Convert a Steam invite code to a unique Steam account identifier
	 * or return a default value if failed.
	 *
	 * <p>Removes {@value USteamInvite#CODE_DELIMITER} and replaces characters using two
	 * bases in order - {@value USteamInvite#CODE_BASE} and {@value USteamInvite#XUID_BASE}.
	 * Then parses the resulting code to an unsigned 64-bit integer.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>Steam invite code is {@code null}.
	 *     <li>Steam invite code doesn't match w/ {@link USteamRegex#INVITE_CODE}.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Long) defaultValue
	 *     USteamInvite.toXuidOrElse(null, <defaultValueSupplier>);
	 *
	 *     // (Long) defaultValue
	 *     USteamInvite.toXuidOrElse("", <defaultValueSupplier>);
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrElse("c", <defaultValueSupplier>);
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrElse("  c  ", <defaultValueSupplier>);
	 *
	 *     // (Long) 1266042636
	 *     USteamInvite.toXuidOrElse("gqkj-gkbr", <defaultValueSupplier>);
	 * }</pre>
	 * <hr>
	 *
	 * @param code					Steam invite code to convert
	 * @param defualtValueSupplier 	supplier from which get the default value
	 * @return						Steam unique account identifier or the default value
	 */
	public static Long toXuidOrElse(String code, Supplier<Long> defualtValueSupplier) {
		return UwObject.getIfNull(toXuidOrNull(code), defualtValueSupplier);
	}

	/**
	 * Convert a Steam invite code to a unique Steam account identifier
	 * or return {@code 0L} value if failed.
	 *
	 * <p>Removes {@value USteamInvite#CODE_DELIMITER} and replaces characters using two
	 * bases in order - {@value USteamInvite#CODE_BASE} and {@value USteamInvite#XUID_BASE}.
	 * Then parses the resulting code to an unsigned 64-bit integer.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>Steam invite code is {@code null}.
	 *     <li>Steam invite code doesn't match w/ {@link USteamRegex#INVITE_CODE}.
	 * </ul>
	 *
	 * <p>Wraps {@link USteamInvite#toXuidOrElse(String, Long)}
	 * w/ {@code 0L} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Long) 0
	 *     USteamInvite.toXuidOrZero(null);
	 *
	 *     // (Long) 0
	 *     USteamInvite.toXuidOrZero("");
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrZero("c");
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrZero("  c  ");
	 *
	 *     // (Long) 1266042636
	 *     USteamInvite.toXuidOrZero("gqkj-gkbr");
	 * }</pre>
	 * <hr>
	 *
	 * @param code			Steam invite code to convert
	 * @return				Steam unique account identifier or {@code 0L} value
	 */
	public static Long toXuidOrZero(String code) {
		return toXuidOrElse(code, 0L);
	}

	/**
	 * Convert a Steam invite code to a unique Steam account identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Removes {@value USteamInvite#CODE_DELIMITER} and replaces characters using two
	 * bases in order - {@value USteamInvite#CODE_BASE} and {@value USteamInvite#XUID_BASE}.
	 * Then parses the resulting code to an unsigned 64-bit integer.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>Steam invite code is {@code null}.
	 *     <li>Steam invite code doesn't match w/ {@link USteamRegex#INVITE_CODE}.
	 * </ul>
	 *
	 * <p>Wraps {@link USteamInvite#toXuidOrElse(String, Long)}
	 * w/ {@code null} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Long) null
	 *     USteamInvite.toXuidOrNull(null);
	 *
	 *     // (Long) null
	 *     USteamInvite.toXuidOrNull("");
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrNull("c");
	 *
	 *     // (Long) 1
	 *     USteamInvite.toXuidOrNull("  c  ");
	 *
	 *     // (Long) 1266042636
	 *     USteamInvite.toXuidOrNull("gqkj-gkbr");
	 * }</pre>
	 * <hr>
	 *
	 * @param code			Steam invite code to convert
	 * @return				Steam unique account identifier or {@code null}
	 */
	public static Long toXuidOrNull(String code) {
		return toXuidOrElse(code, (Long) null);
	}

	private USteamInvite() {
		throw new UnsupportedOperationException();
	}
}
