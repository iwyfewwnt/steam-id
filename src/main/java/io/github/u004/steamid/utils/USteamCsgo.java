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

import io.github.u004.bits.utils.UBitMask;
import io.github.u004.steamid.SteamId;
import io.vavr.control.Try;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import io.github.u004.steamid.exceptions.InvalidCsgoFriendCodeStateException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A Steam CS:GO utility.
 *
 * <p>{@code USteamCsgo} is the utility class to make conversions
 * between {@code SteamId}'s xuid and CS:GO friend code.
 *
 * @see <a href="https://vk.cc/ch6eMh">De- and encoding CS:GO friend codes on UnKnoWnCheaTs</a>
 * @see <a href="https://vk.cc/ch6eOT">go-csgofriendcode by emily33901 on GitHub</a>
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class USteamCsgo {

	/**
	 * A minimum CS:GO friend code value.
	 */
	public static final String MIN_FRIEND_CODE = "AJJJS-ABAA";

	/**
	 * A maximum CS:GO friend code value.
	 */
	public static final String MAX_FRIEND_CODE = "S9ZZR-9997";

	/**
	 * A CS:GO friend code base.
	 */
	public static final String CODE_BASE = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

	/**
	 * A CS:GO friend code delimiter.
	 */
	public static final String CODE_DELIMITER = "-";

	/**
	 * A CS:GO friend code prefix.
	 *
	 * <p>Used to convert the interface-friendly code
	 * into the actual CS:GO friend code.
	 */
	public static final String CODE_PREFIX = "AAAA" + CODE_DELIMITER;

	/**
	 * A CS:GO friend code length.
	 */
	public static final int CODE_LENGTH = 13;

	/**
	 * A CS:GO friend code delimiter points.
	 */
	public static final int[] CODE_POINTS = {4, 9};

	/**
	 * A CS:GO friend code bit mask.
	 *
	 * <p>Used to normalize the {@code SteamId}'s xuid.
	 */
	private static final long MASK = 0x4353474F00000000L;

	// Unsafe. Should be wrapped at a higher level.
	private static String base32(long val) {
		val = Long.reverseBytes(val);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < CODE_LENGTH; i++, val >>= 5) {
			sb.append(CODE_BASE.charAt((int) (val & 0x1F)));
		}

		for (int i = 0; i < CODE_POINTS.length; i++) {
			sb.insert(CODE_POINTS[i] + i, CODE_DELIMITER);
		}

		return sb.toString();
	}

	// Unsafe. Should be wrapped at a higher level.
	private static long base32(String val) {
		val = val.replace(CODE_DELIMITER, "");

		long res = 0;
		for (int i = 0; i < CODE_LENGTH; i++) {
			res |= (long) CODE_BASE.indexOf(val.charAt(i)) << (5 * i);
		}

		return Long.reverseBytes(res);
	}

	// Unsafe. Should be wrapped at a higher level.
	private static long hash(long xuid) {
		xuid |= MASK;

		byte[] bytes = ByteBuffer.allocate(Long.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putLong(xuid)
				.array();

		bytes = DigestUtils.md5(bytes);

		return ByteBuffer.wrap(bytes)
				.order(ByteOrder.LITTLE_ENDIAN)
				.getLong();
	}

	// Safe, but is still in need to check the parameters at a higher level.
	private static Try<String> toExtendedFriendCode(long val) {
		return Try.of(() -> {
			long xuid = val;

			long hash = hash(xuid);
			long res = 0;

			for (int i = 0; i < 8; i++) {
				byte idNibble = (byte) (xuid & UBitMask.UINT4);
				byte hashNibble = (byte) ((hash >> i) & 1);

				long a = ((res << 4) & UBitMask.UINT32) | (idNibble & UBitMask.UINT32);

				res = (res >> 28) << 32 | (a & UBitMask.UINT16);
				res = (res >> 31) << 32 | (a << 1 | hashNibble);

				xuid >>= 4;
			}

			return base32(res);
		});
	}

	// Safe, but is still in need to check the parameters at a higher level.
	private static Try<Long> fromExtendedFriendCode(String code) {
		return Try.of(() -> {
			long val = base32(code);
			long xuid = 0;

			for (int i = 0; i < 8; i++) {
				val >>= 1;

				long idNibble = (val & UBitMask.UINT4) & UBitMask.UINT32;

				xuid <<= 4;
				xuid |= idNibble;

				val >>= 4;
			}

			return xuid;
		});
	}

	/**
	 * Convert unique Steam account identifier
	 * to the interface-friendly CS:GO friend code.
	 *
	 * <p>Calls private static method {@code USteamCsgo#toExtendedFriendCode(long)}
	 * and cuts {@value USteamCsgo#CODE_PREFIX} from the start of the code.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 *     <li>{@link InvalidCsgoFriendCodeStateException}
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamCsgo.toFriendCode(0);
	 *
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamCsgo.toFriendCode(Long.MAX_VALUE);
	 *
	 *     // Try.succes("AJJJS-ABAA")
	 *     USteamCsgo.toFriendCode(1);
	 *
	 *     // Try.succes("AEVDG-WQTQ")
	 *     USteamCsgo.toFriendCode(1266042636);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	{@code SteamId}'s xuid
	 * @return		interface-friendly CS:GO friend code
	 * 				that wrapped in {@link Try}
	 */
	public static Try<String> toFriendCode(Long xuid) {
		if (!SteamId.isSteamXuidValid(xuid)) {
			return Try.failure(new IllegalArgumentException());
		}

		return toExtendedFriendCode(xuid)
				.map(code -> code.substring(CODE_PREFIX.length()))
				.filter(code -> code.matches(USteamRegex.CSGO_CODE), InvalidCsgoFriendCodeStateException::new);
	}

	/**
	 * Convert interface-friendly CS:GO friend code
	 * to the unique Steam account identifier.
	 *
	 * <p>Adds {@value USteamCsgo#CODE_PREFIX} to the code and calls
	 * private static method {@code USteamCsgo#fromExtendedFriendCode(String)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamCsgo.fromFriendCode(null);
	 *
	 *     // Try.failure(new IllegalArgumentException())
	 *     USteamCsgo.fromFriendCode("");
	 *
	 *     // Try.success(1)
	 *     USteamCsgo.fromFriendCode("AJJJS-ABAA");
	 *
	 *     // Try.success(1)
	 *     USteamCsgo.fromFriendCode("  AJJJS-ABAA  ");
	 *
	 *     // Try.success(1266042636)
	 *     USteamCsgo.fromFriendCode("AEVDG-WQTQ");
	 * }</pre>
	 * <hr>
	 *
	 * @param code	interface-friendly CS:GO friend code
	 * @return		{@code SteamId}'s xuid that wrapped
	 * 				in {@link Try}
	 */
	public static Try<Long> fromFriendCode(String code) {
		code = StringUtils.trimToEmpty(code);

		if (code.matches(USteamRegex.CSGO_CODE)) {
			return fromExtendedFriendCode(CODE_PREFIX + code);
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Rawly convert unique Steam account identifier
	 * to the interface-friendly CS:GO friend code.
	 *
	 * <p>Calls private static method {@code USteamCsgo#toExtendedFriendCode(long)}
	 * and cuts {@value USteamCsgo#CODE_PREFIX} from the start of the code.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // null
	 *     USteamCsgo.toFriendCodeRaw(0);
	 *
	 *     // null
	 *     USteamCsgo.toFriendCodeRaw(Long.MAX_VALUE);
	 *
	 *     // "AJJJS-ABAA"
	 *     USteamCsgo.toFriendCodeRaw(1);
	 *
	 *     // "AEVDG-WQTQ"
	 *     USteamCsgo.toFriendCodeRaw(1266042636);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	{@code SteamId}'s xuid
	 * @return		interface-friendly CS:GO friend code or null
	 */
	public static String toFriendCodeRaw(Long xuid) {
		return toFriendCode(xuid).getOrNull();
	}

	/**
	 * Rawly convert interface-friendly CS:GO friend code
	 * to the unique Steam account identifier.
	 *
	 * <p>Adds {@value USteamCsgo#CODE_PREFIX} to the code and calls
	 * private static method {@code USteamCsgo#fromExtendedFriendCode(String)}.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // null
	 *     USteamCsgo.fromFriendCodeRaw(null);
	 *
	 *     // null
	 *     USteamCsgo.fromFriendCodeRaw("");
	 *
	 *     // 1
	 *     USteamCsgo.fromFriendCodeRaw("AJJJS-ABAA");
	 *
	 *     // 1
	 *     USteamCsgo.fromFriendCodeRaw("  AJJJS-ABAA  ");
	 *
	 *     // 1266042636
	 *     USteamCsgo.fromFriendCodeRaw("AEVDG-WQTQ");
	 * }</pre>
	 * <hr>
	 *
	 * @param code	interface-friendly CS:GO friend code
	 * @return		{@code SteamId}'s xuid or null
	 */
	public static Long fromFriendCodeRaw(String code) {
		return fromFriendCode(code).getOrNull();
	}

	private USteamCsgo() {
		throw new UnsupportedOperationException();
	}
}
