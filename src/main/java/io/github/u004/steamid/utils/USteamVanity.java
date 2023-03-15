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
 * A Steam vanity URL types utility.
 *
 * <p>{@code USteamVanity} is the utility class
 * for all types of vanity URLs that contain
 * their IDs.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class USteamVanity {

	/**
	 * A vanity URL type ID - Individual.
	 *
	 * <p>Used for /id/ endpoint.
	 */
	public static final int INDIVIDUAL = 1;

	/**
	 * A vanity URL type ID - Group.
	 *
	 * <p>Used for /groups/ endpoint.
	 */
	public static final int GROUP = 2;

	/**
	 * A vanity URL type ID - Game Group.
	 */
	public static final int GAME_GROUP = 3;

	/**
	 * A minimum vanity URL type ID.
	 *
	 * <p>Wraps {@link USteamVanity#INDIVIDUAL}.
	 */
	public static final int MIN = INDIVIDUAL;

	/**
	 * A maximum vanity URL type ID.
	 *
	 * <p>Wraps {@link USteamVanity#GAME_GROUP}.
	 */
	public static final int MAX = GAME_GROUP;

	private USteamVanity() {
		throw new UnsupportedOperationException();
	}
}
