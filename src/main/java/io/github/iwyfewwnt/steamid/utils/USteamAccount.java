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
 * A Steam account types utility.
 *
 * <p>{@code USteamAccount} is the utility class
 * for all Steam account type identifiers {@literal &} characters.
 *
 * @see <a href="https://vk.cc/ch6ljb">Steam Account Types on Valve Developer Community</a>
 */
@SuppressWarnings("unused")
public final class USteamAccount {

	/**
	 * An account type character base.
	 */
	public static final String CHAR_BASE = "IUMGAPCgTiaLc";

	/**
	 * An account type ID - Invalid.
	 */
	public static final int INVALID_ID = 0;

	/**
	 * An account type ID - Individual.
	 */
	public static final int INDIVIDUAL_ID = 1;

	/**
	 * An account type ID - Multiseat.
	 */
	public static final int MULTISEAT_ID = 2;

	/**
	 * An account type ID - Game Server.
	 */
	public static final int GAME_SERVER_ID = 3;

	/**
	 * An account type ID - Anonymous Game Server.
	 */
	public static final int ANON_GAME_SERVER_ID = 4;

	/**
	 * An account type ID - Pending.
	 */
	public static final int PENDING_ID = 5;

	/**
	 * An account type ID - Content Server.
	 */
	public static final int CONTENT_SERVER_ID = 6;

	/**
	 * An account type ID - Clan.
	 */
	public static final int CLAN_ID = 7;

	/**
	 * An account type ID - Chat.
	 */
	public static final int CHAT_ID = 8;

	/**
	 * An account type ID - Console User/P2P SuperSeeder.
	 */
	public static final int CONSOLE_USER_ID = 9;

	/**
	 * An account type ID - Anonymous User.
	 */
	public static final int ANON_USER_ID = 10;

	/**
	 * An account type ID - Unknown.
	 */
	public static final int UNKNOWN_ID = 11;

	/**
	 * A base account type ID.
	 *
	 * <p>Wraps {@link USteamAccount#INVALID_ID}.
	 */
	public static final int BASE_ID = INVALID_ID;

	/**
	 * A minimum account type ID.
	 *
	 * <p>Wraps {@link USteamAccount#INDIVIDUAL_ID}.
	 */
	public static final int MIN_ID = INDIVIDUAL_ID;

	/**
	 * A maximum account type ID.
	 *
	 * <p>Wraps {@link USteamAccount#UNKNOWN_ID}.
	 */
	public static final int MAX_ID = UNKNOWN_ID;

	/**
	 * An account type char - Invalid.
	 */
	public static final char INVALID_CHAR = 'I';

	/**
	 * An account type char - Individual.
	 */
	public static final char INDIVIDUAL_CHAR = 'U';

	/**
	 * An account type char - Multiseat.
	 */
	public static final char MULTISEAT_CHAR = 'M';

	/**
	 * An account type char - Game Server.
	 */
	public static final char GAME_SERVER_CHAR = 'G';

	/**
	 * An account type char - Anonymous Game Server.
	 */
	public static final char ANON_GAME_SERVER_CHAR = 'A';

	/**
	 * An account type char - Pending.
	 */
	public static final char PENDING_CHAR = 'P';

	/**
	 * An account type char - Content Server.
	 */
	public static final char CONTENT_SERVER_CHAR = 'C';

	/**
	 * An account type char - Clan.
	 */
	public static final char CLAN_CHAR = 'g';

	/**
	 * An account type char - Chat.
	 */
	public static final char CHAT_CHAR = 'T';

	/**
	 * An account type char - Console User/P2P SuperSeeder.
	 *
	 * <p>There's no char variant for the {@link USteamAccount#CONSOLE_USER_ID}
	 * so this value is equal to {@code 0}.
	 */
	public static final char CONSOLE_USER_CHAR = 0;

	/**
	 * An account type char - Anonymous User.
	 */
	public static final char ANON_USER_CHAR = 'a';

	/**
	 * An account type char - Unknown.
	 */
	public static final char UNKNOWN_CHAR = 'i';

	/**
	 * An account type char - Lobby Chat.
	 */
	public static final char LOBBY_CHAT_CHAR = 'L';

	/**
	 * An account type char - Clan Chat.
	 */
	public static final char CLAN_CHAT_CHAR = 'c';

	/**
	 * A base account type char according to base account type ID.
	 *
	 * <p>Wraps {@link USteamAccount#INVALID_CHAR}.
	 */
	public static final char BASE_CHAR = INVALID_CHAR;

	/**
	 * A minimum account type char according to minimum account type ID.
	 *
	 * <p>Wraps {@link USteamAccount#INDIVIDUAL_CHAR}.
	 */
	public static final char MIN_CHAR = INDIVIDUAL_CHAR;

	/**
	 * A maximum account type char according to maximum account type ID.
	 *
	 * <p>Wraps {@link USteamAccount#UNKNOWN_CHAR}.
	 */
	public static final char MAX_CHAR = UNKNOWN_CHAR;

	private USteamAccount() {
		throw new UnsupportedOperationException();
	}
}
