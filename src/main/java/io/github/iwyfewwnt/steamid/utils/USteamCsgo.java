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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

/**
 * A Steam CS:GO utility.
 *
 * <p>{@code USteamCsgo} is the utility class to make conversions
 * between the Steam account type-32 identifier and the CS:GO friend code.
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
	public static final String MAX_CODE = "S5999-9988";

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

	/**
	 * A message digest utility class instance.
	 */
	private static final MessageDigest MD5 = initMessageDigest("MD5");

	/**
	 * Convert unique Steam account identifier
	 * to the interface-friendly CS:GO friend code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam account identifier isn't valid.
	 *     <li>{@link #MD5} is {@code null}.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) defaultValue
	 *     USteamCsgo.fromXuidOrElse(0, <defaultValue>);
	 *
	 *     // (String) defaultValue
	 *     USteamCsgo.fromXuidOrElse(Integer.MAX_VALUE, <defaultValue>);
	 *
	 *     // (String) "AJJJS-ABAA"
	 *     USteamCsgo.fromXuidOrElse(1, <defaultValue>);
	 *
	 *     // (String) "AEVDG-WQTQ"
	 *     USteamCsgo.fromXuidOrElse(1266042636, <defaultValue>);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid			Steam account identifier to convert
	 * @param defaultValue 	default value to return on failure
	 * @return				interface-friendly CS:GO friend code or the default value
	 */
	public static String fromXuidOrElse(Integer xuid, String defaultValue) {
		if (!SteamId.isSteamXuidValid(xuid) || MD5 == null) {
			return defaultValue;
		}

		long hash = hash(xuid);
		long res = 0;

		for (int i = 0; i < 8; i++) {
			long a = ((res << 4) & 0xFFFFFFFFL) | (xuid & 0xF);

			res = res >> 28 << 32 | a & 0xFFFF;
			res = res >> 31 << 32 | a << 1 | hash >> i & 1;

			xuid >>= 4;
		}

		return base32(res)
				.substring(CODE_PREFIX.length());
	}

	/**
	 * Convert a unique Steam account identifier
	 * to the interface-friendly CS:GO friend code
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam account identifier isn't valid.
	 *     <li>{@link #MD5} is {@code null}.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) defaultValue
	 *     USteamCsgo.fromXuidOrElse(0, <defaultValueSupplier>);
	 *
	 *     // (String) defaultValue
	 *     USteamCsgo.fromXuidOrElse(Integer.MAX_VALUE, <defaultValueSupplier>);
	 *
	 *     // (String) "AJJJS-ABAA"
	 *     USteamCsgo.fromXuidOrElse(1, <defaultValueSupplier>);
	 *
	 *     // (String) "AEVDG-WQTQ"
	 *     USteamCsgo.fromXuidOrElse(1266042636, <defaultValueSupplier>);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid					Steam account identifier to convert
	 * @param defaultValueSupplier 	supplier from which get the defualt value
	 * @return						interface-friendly CS:GO friend code or the default value
	 */
	public static String fromXuidOrElse(Integer xuid, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(fromXuidOrNull(xuid), defaultValueSupplier);
	}

	/**
	 * Convert a unique Steam account identifier
	 * to the interface-friendly CS:GO friend code
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam account identifier isn't valid.
	 *     <li>{@link #MD5} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #fromXuidOrElse(Integer, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) ""
	 *     USteamCsgo.fromXuidOrEmpty(0);
	 *
	 *     // (String) ""
	 *     USteamCsgo.fromXuidOrEmpty(Integer.MAX_VALUE);
	 *
	 *     // (String) "AJJJS-ABAA"
	 *     USteamCsgo.fromXuidOrEmpty(1);
	 *
	 *     // (String) "AEVDG-WQTQ"
	 *     USteamCsgo.fromXuidOrEmpty(1266042636);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	Steam account identifier to convert
	 * @return		interface-friendly CS:GO friend code or the empty string
	 */
	public static String fromXuidOrEmpty(Integer xuid) {
		return fromXuidOrElse(xuid, UwString.EMPTY);
	}

	/**
	 * Convert a unique Steam account identifier
	 * to the interface-friendly CS:GO friend code
	 * or return {@code null}.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>Steam account identifier isn't valid.
	 *     <li>{@link #MD5} is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #fromXuidOrElse(Integer, String)}
	 * w/ {@code null} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (String) null
	 *     USteamCsgo.fromXuidOrNull(0);
	 *
	 *     // (String) null
	 *     USteamCsgo.fromXuidOrNull(Integer.MAX_VALUE);
	 *
	 *     // (String) "AJJJS-ABAA"
	 *     USteamCsgo.fromXuidOrNull(1);
	 *
	 *     // (String) "AEVDG-WQTQ"
	 *     USteamCsgo.fromXuidOrNull(1266042636);
	 * }</pre>
	 * <hr>
	 *
	 * @param xuid	Steam account identifier to convert
	 * @return		interface-friendly CS:GO friend code or {@code null}
	 */
	public static String fromXuidOrNull(Integer xuid) {
		return fromXuidOrElse(xuid, (String) null);
	}

	/**
	 * Convert an interface-friendly CS:GO friend code
	 * to a unique Steam account identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is {@code null}.
	 *     <li>CS:GO friend code doesn't match w/ the {@link USteamRegex#CSGO_CODE}.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Integer) defualtValue
	 *     USteamCsgo.toXuidOrElse(null, <defaultValue>);
	 *
	 *     // (Integer) defaultValue
	 *     USteamCsgo.toXuidOrElse("", <defaultValue>);
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrElse("AJJJS-ABAA", <defaultValue>);
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrElse("  AJJJS-ABAA  ", <defaultValue>);
	 *
	 *     // (Integer) 1266042636
	 *     USteamCsgo.toXuidOrElse("AEVDG-WQTQ", <defaultValue>);
	 * }</pre>
	 * <hr>
	 *
	 * @param code			interface-friendly CS:GO friend code to convert
	 * @param defaultValue 	default value to return on failure
	 * @return				Steam unique account identifier or the default value
	 */
	public static Integer toXuidOrElse(String code, Integer defaultValue) {
		if (code == null) {
			return defaultValue;
		}

		code = code.trim();
		if (!code.matches(USteamRegex.CSGO_CODE)) {
			return defaultValue;
		}

		long val = base32(CODE_PREFIX + code);
		long xuid = 0;

		for (int i = 0; i < 8; i++, val >>= 4) {
			xuid = xuid << 4 | (val >>= 1) & 0xF;
		}

		return (int) xuid;
	}

	/**
	 * Convert an interface-friendly CS:GO friend code
	 * to a unique Steam account identifier
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is {@code null}.
	 *     <li>CS:GO friend code doesn't match w/ the {@link USteamRegex#CSGO_CODE}.
	 * </ul>
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Integer) defualtValue
	 *     USteamCsgo.toXuidOrElse(null, <defaultValueSupplier>);
	 *
	 *     // (Integer) defaultValue
	 *     USteamCsgo.toXuidOrElse("", <defaultValueSupplier>);
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrElse("AJJJS-ABAA", <defaultValueSupplier>);
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrElse("  AJJJS-ABAA  ", <defaultValueSupplier>);
	 *
	 *     // (Integer) 1266042636
	 *     USteamCsgo.toXuidOrElse("AEVDG-WQTQ", <defaultValueSupplier>);
	 * }</pre>
	 * <hr>
	 *
	 * @param code					interface-friendly CS:GO friend code to convert
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @return						Steam unique account identifier or the default value
	 */
	public static Integer toXuidOrElse(String code, Supplier<Integer> defaultValueSupplier) {
		return UwObject.ifNull(toXuidOrNull(code), defaultValueSupplier);
	}

	/**
	 * Convert an interface-friendly CS:GO friend code
	 * to a unique Steam account identifier
	 * or return a zero value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is {@code null}.
	 *     <li>CS:GO friend code doesn't match w/ the {@link USteamRegex#CSGO_CODE}.
	 * </ul>
	 *
	 * <p>Wraps {@link #toXuidOrElse(String, Integer)}
	 * w/ {@code 0} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Integer) 0
	 *     USteamCsgo.toXuidOrZero(null);
	 *
	 *     // (Integer) 0
	 *     USteamCsgo.toXuidOrZero("");
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrZero("AJJJS-ABAA");
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrZero("  AJJJS-ABAA  ");
	 *
	 *     // (Integer) 1266042636
	 *     USteamCsgo.toXuidOrZero("AEVDG-WQTQ");
	 * }</pre>
	 * <hr>
	 *
	 * @param code	interface-friendly CS:GO friend code to convert
	 * @return		Steam unique account identifier or the {@code 0} value
	 */
	public static Integer toXuidOrZero(String code) {
		return toXuidOrElse(code, 0);
	}

	/**
	 * Convert an interface-friendly CS:GO friend code
	 * to a unique Steam account identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>CS:GO friend code is {@code null}.
	 *     <li>CS:GO friend code doesn't match w/ the {@link USteamRegex#CSGO_CODE}.
	 * </ul>
	 *
	 * <p>Wraps {@link #toXuidOrElse(String, Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * <hr>
	 * <pre>{@code
	 *     // (Integer) null
	 *     USteamCsgo.toXuidOrNull(null);
	 *
	 *     // (Integer) null
	 *     USteamCsgo.toXuidOrNull("");
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrNull("AJJJS-ABAA");
	 *
	 *     // (Integer) 1
	 *     USteamCsgo.toXuidOrNull("  AJJJS-ABAA  ");
	 *
	 *     // (Integer) 1266042636
	 *     USteamCsgo.toXuidOrNull("AEVDG-WQTQ");
	 * }</pre>
	 * <hr>
	 *
	 * @param code	interface-friendly CS:GO friend code to convert
	 * @return		Steam unique account identifier or {@code null}
	 */
	public static Integer toXuidOrNull(String code) {
		return toXuidOrElse(code, (Integer) null);
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

	@SuppressWarnings("SameParameterValue")
	private static MessageDigest initMessageDigest(String algorithm) {
		if (algorithm == null) {
			throw new IllegalArgumentException("Algorithm mustn't be <null>");
		}

		if (algorithm.isEmpty()) {
			throw new IllegalArgumentException("Algorithm mustn't be <empty>");
		}

		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	private USteamCsgo() {
		throw new UnsupportedOperationException();
	}
}
