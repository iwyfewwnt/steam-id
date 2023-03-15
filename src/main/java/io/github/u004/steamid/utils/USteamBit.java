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

import io.github.u004.bits.BitVar;

/**
 * A Steam Id bits utility.
 *
 * <p>{@code USteamBit} is the utility class
 * for all offsets and masks of the Steam Id
 * bit vector.
 *
 * @see <a href="https://vk.cc/ch6gId">bits by u004 on GitHub</a>
 *
 * @since 0.1.0
 */
public final class USteamBit {

	/**
	 * An account ID offset in the bit vector.
	 */
	public static final int ACCOUNT_ID_OFFSET = 0;

	/**
	 * An account instance offset in the bit vector.
	 */
	public static final int ACCOUNT_INSTANCE_OFFSET = 32;

	/**
	 * An account type offset in the bit vector.
	 */
	public static final int ACCOUNT_TYPE_OFFSET = 52;

	/**
	 * An account universe offset in the bit vector.
	 */
	public static final int ACCOUNT_UNIVERSE_OFFSET = 56;

	/**
	 * An account ID mask to normalize the account ID value.
	 */
	public static final long ACCOUNT_ID_MASK = 0xFFFFFFFFL;

	/**
	 * An account instance mask to normalize the account instance value.
	 */
	public static final long ACCOUNT_INSTANCE_MASK = 0x000FFFFFL;

	/**
	 * An account type mask to normalize the account type value.
	 */
	public static final long ACCOUNT_TYPE_MASK = 0x0000000FL;

	/**
	 * An account universe mask to normalize the account universe value.
	 */
	public static final long ACCOUNT_UNIVERSE_MASK = 0x000000FFL;

	/**
	 * An account ID bit variable.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_ID_OFFSET} and {@link USteamBit#ACCOUNT_ID_MASK}.
	 */
	public static final BitVar ACCOUNT_ID = new BitVar(ACCOUNT_ID_OFFSET, ACCOUNT_ID_MASK);

	/**
	 * An account instance bit variable.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_INSTANCE_OFFSET} and {@link USteamBit#ACCOUNT_INSTANCE_MASK}.
	 */
	public static final BitVar ACCOUNT_INSTANCE = new BitVar(ACCOUNT_INSTANCE_OFFSET, ACCOUNT_INSTANCE_MASK);

	/**
	 * An account type bit variable.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_TYPE_OFFSET} and {@link USteamBit#ACCOUNT_TYPE_MASK}.
	 */
	public static final BitVar ACCOUNT_TYPE = new BitVar(ACCOUNT_TYPE_OFFSET, ACCOUNT_TYPE_MASK);

	/**
	 * An account universe bit variable.
	 *
	 * <p>Wraps {@link USteamBit#ACCOUNT_UNIVERSE_OFFSET} and {@link USteamBit#ACCOUNT_UNIVERSE_MASK}.
	 */
	public static final BitVar ACCOUNT_UNIVERSE = new BitVar(ACCOUNT_UNIVERSE_OFFSET, ACCOUNT_UNIVERSE_MASK);

	private USteamBit() {
		throw new UnsupportedOperationException();
	}
}
