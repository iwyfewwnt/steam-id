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

import java.util.regex.Pattern;

/**
 * A Steam patterns utility.
 *
 * <p>{@code USteamPattern} is the utility class
 * for compiled Steam related regular expressions.
 */
public final class USteamPattern {

	/**
	 * A Steam ID2 pattern.
	 *
	 * <p>Wraps {@link USteamRegex#ID2}.
	 */
	public static final Pattern ID2 = Pattern.compile(USteamRegex.ID2);

	/**
	 * A Steam ID3 pattern.
	 *
	 * <p>Wraps {@link USteamRegex#ID3}.
	 */
	public static final Pattern ID3 = Pattern.compile(USteamRegex.ID3);

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
