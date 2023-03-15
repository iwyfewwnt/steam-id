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

/**
 * A Steam universe types utility.
 *
 * <p>{@code USteamUniverse} is the utility class
 * for all types of Steam account universes
 * that contain their IDs.
 *
 * @see <a href="https://vk.cc/ch6lh5">Steam Account Universe Types</a>
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class USteamUniverse {

	/**
	 * An account universe type ID - Invalid.
	 */
	public static final int INVALID = 0;

	/**
	 * An account universe type ID - Public.
	 */
	public static final int PUBLIC = 1;

	/**
	 * An account universe type ID - Beta.
	 */
	public static final int BETA = 2;

	/**
	 * An account universe type ID - Internal.
	 */
	public static final int INTERNAL = 3;

	/**
	 * An account universe type ID - Dev.
	 */
	public static final int DEV = 4;

	/**
	 * An account universe type ID - RC.
	 */
	public static final int RC = 5;

	/**
	 * A base account universe type ID.
	 *
	 * <p>Wraps {@link USteamUniverse#INVALID}.
	 */
	public static final int BASE = INVALID;

	/**
	 * A minimum account universe type ID.
	 *
	 * <p>Wraps {@link USteamUniverse#PUBLIC}.
	 */
	public static final int MIN = PUBLIC;

	/**
	 * A maximum account universe type ID.
	 *
	 * <p>Wraps {@link USteamUniverse#RC}.
	 */
	public static final int MAX = RC;

	private USteamUniverse() {
		throw new UnsupportedOperationException();
	}
}
