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
import io.github.u004.bits.utils.UBitMask;
//import io.vavr.control.Try;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.StringUtils;
//import io.github.iwyfewwnt.steamid.exceptions.InvalidCsgoCodeStateException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

/**
 * A Steam CS:GO utility.
 *
 * <p>{@code USteamCsgo} is the utility class to make conversions
 * between {@code SteamId}'s xuid and CS:GO friend code.
 *
 * @see <a href="https://vk.cc/ch6eMh">De- and encoding CS:GO friend codes on UnKnoWnCheaTs</a>
 * @see <a href="https://vk.cc/ch6eOT">go-csgofriendcode by emily33901 on GitHub</a>
 */
@SuppressWarnings("unused")
public final class USteamCsgo {

	/**
	 * A minimum CS:GO friend code value.
	 */
	public static final String MIN_CODE = "AJJJS-ABAA";

	/**
	 * A maximum CS:GO friend code value.
	 */
	public static final String MAX_CODE = "S9ZZR-9997";

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
	private static final long HASH_MASK = 0x4353474F00000000L;

	private static final MessageDigest MD5 = initMd5MessageDigest();

	private static MessageDigest initMd5MessageDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

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

	private static long base32(String val) {
		val = val.replace(CODE_DELIMITER, "");

		long res = 0;
		for (int i = 0; i < CODE_LENGTH; i++) {
			res |= (long) CODE_BASE.indexOf(val.charAt(i)) << (5 * i);
		}

		return Long.reverseBytes(res);
	}

	private static long hash(long xuid) {
		xuid |= HASH_MASK;

		byte[] bytes = ByteBuffer.allocate(Long.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putLong(xuid)
				.array();

		//noinspection ConstantConditions
		bytes = MD5.digest(bytes); MD5.reset();

		return ByteBuffer.wrap(bytes)
				.order(ByteOrder.LITTLE_ENDIAN)
				.getLong();
	}

	public static String fromXuidOrElse(Long xuid, String defaultValue) {
		if (!SteamId.isSteamXuidValid(xuid) || MD5 == null) {
			return defaultValue;
		}

		long hash = hash(xuid);
		long res = 0;

		for (int i = 0; i < 8; i++) {
			long a = ((res << 4) & UBitMask.UINT32) | (xuid & UBitMask.UINT4);

			res = res >> 28 << 32 | a & UBitMask.UINT16;
			res = res >> 31 << 32 | a << 1 | hash >> i & 1;

			xuid >>= 4;
		}

		//noinspection UnnecessaryLocalVariable
		String code = base32(res)
				.substring(CODE_PREFIX.length());

//		if (!code.matches(USteamRegex.CSGO_CODE)) {
//			return defaultValue;
//		}

		return code;
	}

	public static String fromXuidOrElse(Long xuid, Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(fromXuidOrNull(xuid), defaultValueSupplier);
	}

	public static String fromXuidOrEmpty(Long xuid) {
		return fromXuidOrElse(xuid, UwString.EMPTY);
	}

	public static String fromXuidOrNull(Long xuid) {
		return fromXuidOrElse(xuid, (String) null);
	}


	public static Long toXuidOrElse(String code, Long defaultValue) {
		if (code == null) {
			return defaultValue;
		}

		long val = base32(code.trim());
		long xuid = 0;

		for (int i = 0; i < 8; i++, val >>= 4) {
			xuid = xuid << 4 | (val >>= 1) & UBitMask.UINT4;
		}

//		if (!SteamId.isSteamXuidValid(xuid)) {
//			return defaultValue;
//		}

		return xuid;
	}

	public static Long toXuidOrElse(String code, Supplier<Long> defaultValueSupplier) {
		return UwObject.getIfNull(toXuidOrNull(code), defaultValueSupplier);
	}

	public static Long toXuidOrZero(String code) {
		return toXuidOrElse(code, 0L);
	}

	public static Long toXuidOrNull(String code) {
		return toXuidOrElse(code, (Long) null);
	}

//	private static Try<String> toExtendedCode(long val) {
//		return Try.of(() -> {
//			long xuid = val;
//
//			long hash = hash(xuid);
//			long res = 0;
//
//			for (int i = 0; i < 8; i++) {
//				byte idNibble = (byte) (xuid & UBitMask.UINT4);
//				byte hashNibble = (byte) ((hash >> i) & 1);
//
//				long a = ((res << 4) & UBitMask.UINT32) | idNibble;
//
//				res = (res >> 28) << 32 | (a & UBitMask.UINT16);
//				res = (res >> 31) << 32 | (a << 1 | hashNibble);
//
//				xuid >>= 4;
//			}
//
//			return base32(res);
//		});
//	}

//	private static Try<Long> fromExtendedCode(String code) {
//		return Try.of(() -> {
//			long val = base32(code);
//			long xuid = 0;
//
//			for (int i = 0; i < 8; i++) {
//				val >>= 1;
//
//				long idNibble = val & UBitMask.UINT4;
//
//				xuid <<= 4;
//				xuid |= idNibble;
//
//				val >>= 4;
//			}
//
//			return xuid;
//		});
//	}

//	/**
//	 * Convert unique Steam account identifier
//	 * to the interface-friendly CS:GO friend code.
//	 *
//	 * <p>Calls private static method {@code USteamCsgo#toExtendedCode(long)}
//	 * and cuts {@value USteamCsgo#CODE_PREFIX} from the start of the code.
//	 *
//	 * <p>Possible failure exceptions:
//	 * <ul>
//	 *     <li>{@link IllegalArgumentException}
//	 *     <li>{@link InvalidCsgoCodeStateException}
//	 * </ul>
//	 *
//	 * <hr>
//	 * <pre>{@code
//	 *     // Try.failure(new IllegalArgumentException())
//	 *     USteamCsgo.toFriendCode(0);
//	 *
//	 *     // Try.failure(new IllegalArgumentException())
//	 *     USteamCsgo.toFriendCode(Long.MAX_VALUE);
//	 *
//	 *     // Try.succes("AJJJS-ABAA")
//	 *     USteamCsgo.toFriendCode(1);
//	 *
//	 *     // Try.succes("AEVDG-WQTQ")
//	 *     USteamCsgo.toFriendCode(1266042636);
//	 * }</pre>
//	 * <hr>
//	 *
//	 * @param xuid	{@code SteamId}'s xuid
//	 * @return		interface-friendly CS:GO friend code
//	 * 				that wrapped in {@link Try}
//	 */
//	public static Try<String> toCode(Long xuid) {
//		if (!SteamId.isSteamXuidValid(xuid)) {
//			return Try.failure(new IllegalArgumentException());
//		}
//
//		return toExtendedCode(xuid)
//				.map(code -> code.substring(CODE_PREFIX.length()))
//				.filter(code -> code.matches(USteamRegex.CSGO_CODE), InvalidCsgoCodeStateException::new);
//	}

//	/**
//	 * Convert interface-friendly CS:GO friend code
//	 * to the unique Steam account identifier.
//	 *
//	 * <p>Adds {@value USteamCsgo#CODE_PREFIX} to the code and calls
//	 * private static method {@code USteamCsgo#fromExtendedCode(String)}.
//	 *
//	 * <p>Possible failure exceptions:
//	 * <ul>
//	 *     <li>{@link IllegalArgumentException}
//	 * </ul>
//	 *
//	 * <hr>
//	 * <pre>{@code
//	 *     // Try.failure(new IllegalArgumentException())
//	 *     USteamCsgo.fromFriendCode(null);
//	 *
//	 *     // Try.failure(new IllegalArgumentException())
//	 *     USteamCsgo.fromFriendCode("");
//	 *
//	 *     // Try.success(1)
//	 *     USteamCsgo.fromFriendCode("AJJJS-ABAA");
//	 *
//	 *     // Try.success(1)
//	 *     USteamCsgo.fromFriendCode("  AJJJS-ABAA  ");
//	 *
//	 *     // Try.success(1266042636)
//	 *     USteamCsgo.fromFriendCode("AEVDG-WQTQ");
//	 * }</pre>
//	 * <hr>
//	 *
//	 * @param code	interface-friendly CS:GO friend code
//	 * @return		{@code SteamId}'s xuid that wrapped
//	 * 				in {@link Try}
//	 */
//	public static Try<Long> fromCode(String code) {
//		code = StringUtils.trimToEmpty(code);
//
//		if (code.matches(USteamRegex.CSGO_CODE)) {
//			return fromExtendedCode(CODE_PREFIX + code);
//		}
//
//		return Try.failure(new IllegalArgumentException());
//	}

//	/**
//	 * Rawly convert unique Steam account identifier
//	 * to the interface-friendly CS:GO friend code.
//	 *
//	 * <p>Calls private static method {@code USteamCsgo#toExtendedCode(long)}
//	 * and cuts {@value USteamCsgo#CODE_PREFIX} from the start of the code.
//	 *
//	 * <hr>
//	 * <pre>{@code
//	 *     // null
//	 *     USteamCsgo.toFriendCodeRaw(0);
//	 *
//	 *     // null
//	 *     USteamCsgo.toFriendCodeRaw(Long.MAX_VALUE);
//	 *
//	 *     // "AJJJS-ABAA"
//	 *     USteamCsgo.toFriendCodeRaw(1);
//	 *
//	 *     // "AEVDG-WQTQ"
//	 *     USteamCsgo.toFriendCodeRaw(1266042636);
//	 * }</pre>
//	 * <hr>
//	 *
//	 * @param xuid	{@code SteamId}'s xuid
//	 * @return		interface-friendly CS:GO friend code or null
//	 */
//	public static String toCodeRaw(Long xuid) {
//		return toCode(xuid).getOrNull();
//	}

//	/**
//	 * Rawly convert interface-friendly CS:GO friend code
//	 * to the unique Steam account identifier.
//	 *
//	 * <p>Adds {@value USteamCsgo#CODE_PREFIX} to the code and calls
//	 * private static method {@code USteamCsgo#fromExtendedCode(String)}.
//	 *
//	 * <hr>
//	 * <pre>{@code
//	 *     // null
//	 *     USteamCsgo.fromFriendCodeRaw(null);
//	 *
//	 *     // null
//	 *     USteamCsgo.fromFriendCodeRaw("");
//	 *
//	 *     // 1
//	 *     USteamCsgo.fromFriendCodeRaw("AJJJS-ABAA");
//	 *
//	 *     // 1
//	 *     USteamCsgo.fromFriendCodeRaw("  AJJJS-ABAA  ");
//	 *
//	 *     // 1266042636
//	 *     USteamCsgo.fromFriendCodeRaw("AEVDG-WQTQ");
//	 * }</pre>
//	 * <hr>
//	 *
//	 * @param code	interface-friendly CS:GO friend code
//	 * @return		{@code SteamId}'s xuid or null
//	 */
//	public static Long fromCodeRaw(String code) {
//		return fromCode(code).getOrNull();
//	}

	private USteamCsgo() {
		throw new UnsupportedOperationException();
	}
}
