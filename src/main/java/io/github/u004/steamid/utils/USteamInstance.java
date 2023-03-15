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
 * A Steam instance types utility.
 *
 * <p>{@code USteamInstance} is the utility class
 * for all types of Steam account instances
 * that contain their IDs.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class USteamInstance {

	/**
	 * An account instance type ID - All.
	 */
	public static final int ALL = 0;

	/**
	 * An account instance type ID - Desktop.
	 */
	public static final int DESKTOP = 1;

	/**
	 * An account instance type ID - Console.
	 */
	public static final int CONSOLE = 2;

	/**
	 * An account instance type ID - Web.
	 */
	public static final int WEB = 4;

	/**
	 * A minimum account instance type ID.
	 *
	 * <p>Wraps {@link USteamInstance#ALL}.
	 */
	public static final int MIN = ALL;

	/**
	 * A maximum account instance type ID.
	 *
	 * <p>Wraps {@link USteamInstance#WEB}.
	 */
	public static final int MAX = WEB;

	/**
	 * A chat flag's magic value.
	 *
	 * <p>Used to get a chat flag IDs for the "Chat"
	 * account type.
	 */
	private static final int MAGIC = (int) (USteamBit.ACCOUNT_INSTANCE_MASK + 1);

	/**
	 * A chat flag - Clan.
	 */
	public static final int CLAN = MAGIC >> 1;

	/**
	 * A chat flag - Lobby.
	 */
	public static final int LOBBY = MAGIC >> 2;

	/**
	 * A chat flag - Matchmaking Lobby.
	 */
	public static final int MM_LOBBY = MAGIC >> 3;

	private USteamInstance() {
		throw new UnsupportedOperationException();
	}
}
