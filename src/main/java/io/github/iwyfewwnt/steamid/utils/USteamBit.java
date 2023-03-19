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

/**
 * A Steam ID bits utility.
 *
 * <p>{@code USteamBit} is the utility class
 * for all offsets and masks of the Steam ID
 * bit vector.
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
	 * Get a Steam type-32 account identifier
	 * from a Steam type-64 account identifier.
	 *
	 * @param id64	Steam type-64 account identifier
	 * @return		Steam type-32 account identifier
	 */
	public static int getAccountXuid(long id64) {
		return get(id64, ACCOUNT_ID_OFFSET, ACCOUNT_ID_MASK);
	}

	/**
	 * Get a Steam account instance type identifier
	 * from a Steam type-64 account identifier.
	 *
	 * @param id64	Steam type-64 account identifier
	 * @return		Steam account instance type identifier
	 */
	public static int getAccountInstance(long id64) {
		return get(id64, ACCOUNT_INSTANCE_OFFSET, ACCOUNT_INSTANCE_MASK);
	}

	/**
	 * Get a Steam account type identifier
	 * from a Steam type-64 account identifier.
	 *
	 * @param id64	Steam type-64 account identifier
	 * @return		Steam account type identifier
	 */
	public static int getAccountType(long id64) {
		return get(id64, ACCOUNT_TYPE_OFFSET, ACCOUNT_TYPE_MASK);
	}

	/**
	 * Get a Steam account universe type identifier
	 * from a Steam type-64 account identifier.
	 *
	 * @param id64	Steam type-64 account identifier
	 * @return		Steam account universe type identifier
	 */
	public static int getAccountUniverse(long id64) {
		return get(id64, ACCOUNT_UNIVERSE_OFFSET, ACCOUNT_UNIVERSE_MASK);
	}

	/**
	 * Get a value of the bit variable from the bit vector.
	 *
	 * @param vec		long value of the bit vector
	 * @param offset	offset of the bit variable
	 * @param mask		mask of the bit variable
	 * @return			value of the bit variable
	 */
	private static int get(long vec, int offset, long mask) {
		return (int) ((vec >> offset) & mask);
	}

	private USteamBit() {
		throw new UnsupportedOperationException();
	}
}
