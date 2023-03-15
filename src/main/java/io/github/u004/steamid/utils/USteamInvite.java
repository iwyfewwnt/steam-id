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

package io.github.u004.steamid.utils;

import io.github.u004.steamid.SteamId;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import io.github.u004.steamid.exceptions.InvalidSteamInviteCodeStateException;

/**
 * A Steam invite utility.
 *
 * <p>{@code USteamInvite} is the utility class to make
 * conversions between {@code SteamId}'s xuid and Steam invite code.
 *
 * @since 0.1.0
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
	 * A {@code SteamId}'s xuid base.
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
	 * Convert unique Steam account identifier to the Steam invite code.
	 *
	 * <p>Converts unique account identifier to hex string and replaces characters using two
	 * bases in order - {@value USteamInvite#XUID_BASE} and {@value USteamInvite#CODE_BASE}.
	 * Then places {@value USteamInvite#CODE_DELIMITER} if the length of the resulting
	 * code is greater than three.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidSteamInviteCodeStateException}
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamInvite.toCode(0);
	 *
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamInvite.toCode(Long.MAX_VALUE);
	 *
	 *     // Try.success("c")
	 *     USteamInvite.toCode(1);
	 *
	 *     // Try.success("gqkj-gkbr")
	 *     USteamInvite.toCode(1266042636);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	{@code SteamId}'s xuid
	 * @return		Steam invite code that
	 * 				wrapped in {@link Try}
	 */
	public static Try<String> toCode(Long xuid) {
		if (!SteamId.isSteamXuidValid(xuid)) {
			return Try.failure(new IllegalArgumentException());
		}

		String code = Long.toHexString(xuid);
		code = StringUtils.replaceChars(code, XUID_BASE, CODE_BASE);

		int idx = code.length() / 2;

		if (idx > 1) {
			code = code.substring(0, idx)
					+ USteamInvite.CODE_DELIMITER
					+ code.substring(idx);
		}

		if (!code.matches(USteamRegex.INVITE_CODE)) {
			return Try.failure(new InvalidSteamInviteCodeStateException());
		}

		return Try.success(code);
	}

	/**
	 * Convert Steam invite code to the unique Steam account identifier.
	 *
	 * <p>Removes {@value USteamInvite#CODE_DELIMITER} and replaces characters using two
	 * bases in order - {@value USteamInvite#CODE_BASE} and {@value USteamInvite#XUID_BASE}.
	 * Then parses the resulting code to an unsigned 64-bit integer.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamInvite.fromCode(null);
	 *
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamInvite.fromCode("");
	 *
	 *     // Try.success(1)
	 *     USteamInvite.fromCode("c");
	 *
	 *     // Try.success(1)
	 *     USteamInvite.fromCode("  c  ");
	 *
	 *     // Try.success(1266042636)
	 *     USteamInvite.fromCode("gqkj-gkbr");
	 * }</pre>
	 * <hr>
	 *
	 * @param code	Steam invite code
	 * @return		{@code SteamId}'s xuid that
	 * 				wrapped in {@link Try}
	 */
	public static Try<Long> fromCode(String code) {
		code = StringUtils.trimToEmpty(code);

		if (code.matches(USteamRegex.INVITE_CODE)) {
			code = code.replace(CODE_DELIMITER, "");
			code = StringUtils.replaceChars(code, CODE_BASE, XUID_BASE);

			try {
				return Try.success(Long.parseUnsignedLong(code, 16));
			} catch (NumberFormatException ignored) {
			}
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Rawly convert unique Steam account identifier to the Steam invite code.
	 *
	 * <p>Converts unique account identifier to hex string and replaces characters using two
	 * bases in order - {@value USteamInvite#XUID_BASE} and {@value USteamInvite#CODE_BASE}.
	 * Then places {@value USteamInvite#CODE_DELIMITER} if the length of the resulting
	 * code is greater than three.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // null
	 *     USteamInvite.toCodeRaw(0);
	 *
	 *     // null
	 *     USteamInvite.toCodeRaw(Long.MAX_VALUE);
	 *
	 *     // "c"
	 *     USteamInvite.toCodeRaw(1);
	 *
	 *     // "gqkj-gkbr"
	 *     USteamInvite.toCodeRaw(1266042636);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	{@code SteamId}'s xuid
	 * @return		Steam invite code or null
	 */
	public static String toCodeRaw(Long xuid) {
		return toCode(xuid).getOrNull();
	}

	/**
	 * Rawly convert Steam invite code to the unique Steam account identifier.
	 *
	 * <p>Removes {@value USteamInvite#CODE_DELIMITER} and replaces characters using two
	 * bases in order - {@value USteamInvite#CODE_BASE} and {@value USteamInvite#XUID_BASE}.
	 * Then parses the resulting code to an unsigned 64-bit integer.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // null
	 *     USteamInvite.fromCodeRaw(null);
	 *
	 *     // null
	 *     USteamInvite.fromCodeRaw("");
	 *
	 *     // 1
	 *     USteamInvite.fromCodeRaw("c");
	 *
	 *     // 1
	 *     USteamInvite.fromCodeRaw("  c  ");
	 *
	 *     // 1266042636
	 *     USteamInvite.fromCodeRaw("gqkj-gkbr");
	 * }</pre>
	 * <hr>
	 *
	 * @param code	Steam invite code
	 * @return		{@code SteamId}'s xuid or null
	 */
	public static Long fromCodeRaw(String code) {
		return fromCode(code).getOrNull();
	}

	private USteamInvite() {
		throw new UnsupportedOperationException();
	}
}
