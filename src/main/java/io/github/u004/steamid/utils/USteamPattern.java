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

import java.util.regex.Pattern;

/**
 * A Steam patterns utility.
 *
 * <p>{@code USteamPattern} is the utility class
 * for compiled {@link USteamRegex}'s regular expressions.
 *
 * @since 0.1.0
 */
public final class USteamPattern {

	/**
	 * A Steam ID2 pattern.
	 *
	 * <p>Wraps {@link USteamRegex#STEAM_2}.
	 */
	public static final Pattern STEAM_2 = Pattern.compile(USteamRegex.STEAM_2);

	/**
	 * A Steam ID3 pattern.
	 *
	 * <p>Wraps {@link USteamRegex#STEAM_3}.
	 */
	public static final Pattern STEAM_3 = Pattern.compile(USteamRegex.STEAM_3);

	/**
	 * A Steam profile url pattern.
	 *
	 * <p>Wraps {@link USteamRegex#PROFILE_URL}.
	 */
	public static final Pattern PROFILE_URL = Pattern.compile(USteamRegex.PROFILE_URL);

	/**
	 * A Steam user url pattern.
	 *
	 * <p>Wraps {@link USteamRegex#USER_URL}.
	 */
	public static final Pattern USER_URL = Pattern.compile(USteamRegex.USER_URL);

	private USteamPattern() {
		throw new UnsupportedOperationException();
	}
}
