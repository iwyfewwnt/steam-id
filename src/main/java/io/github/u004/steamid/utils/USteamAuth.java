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
 * A Steam authentication types utility.
 *
 * <p>{@code USteamAuth} is the utility class
 * for all types of Steam authentication
 * that contain their IDs.
 *
 * @since 0.1.0
 */
public final class USteamAuth {

	/**
	 * An authentication type ID - No.
	 */
	public static final int NO = 0;

	/**
	 * An authentication type ID - Yes.
	 */
	public static final int YES = 1;

	/**
	 * A minimum authentication type ID.
	 *
	 * <p>Wraps {@link USteamAuth#NO}.
	 */
	public static final int MIN = NO;

	/**
	 * A maximum authentication type ID.
	 *
	 * <p>Wraps {@link USteamAuth#YES}.
	 */
	public static final int MAX = YES;

	private USteamAuth() {
		throw new UnsupportedOperationException();
	}
}
