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

import static java.lang.String.format;

/**
 * A Steam regular expressions utility.
 *
 * <p>{@code USteamRegex} is the utility class
 * for regular expressions that were used in
 * this project for pattern matching.
 */
@SuppressWarnings("unused")
public final class USteamRegex {

	/**
	 * A {@code USteamRegex}'s groups utility class.
	 *
	 * <p>{@code USteamRegex.Group} is the utility class
	 * for all group names that were used in this project
	 * for pattern matching.
	 */
	public static final class Group {

		/**
		 * An authentication server group name.
		 */
		public static final String AUTH = "auth";

		/**
		 * A Steam account universe group name.
		 */
		public static final String UNIVERSE = "universe";

		/**
		 * A Steam ID group name.
		 */
		public static final String ID = "id";

		/**
		 * A Steam account type group name.
		 */
		public static final String ACCOUNT = "account";

		/**
		 * A Steam account instance type group name.
		 */
		public static final String INSTANCE = "instance";

		private Group() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * A Steam ID2 regular expression.
	 *
	 * <p>Possible matching group names:
	 * <ul>
	 *     <li>{@code USteamRegex.Group.UNIVERSE}
	 *     <li>{@code USteamRegex.Group.AUTH}
	 *     <li>{@code USteamRegex.Group.ID}
	 * </ul>
	 */
	public static final String ID2 = format("^STEAM_(?<%s>[%s-%s]):(?<%s>[%s-%s]):(?<%s>\\d+)$",
			Group.UNIVERSE, USteamUniverse.BASE, USteamUniverse.MAX,
			Group.AUTH, USteamAuth.MIN, USteamAuth.MAX,
			Group.ID
	);

	/**
	 * A Steam ID3 regular expression.
	 *
	 * <p>Possible matching group names:
	 * <ul>
	 *     <li>{@code USteamRegex.Group.ACCOUNT}
	 *     <li>{@code USteamRegex.Group.UNIVERSE}
	 *     <li>{@code USteamRegex.Group.ID}
	 *     <li>{@code USteamRegex.Group.INSTANCE}
	 * </ul>
	 */
	public static final String ID3 = format("^\\[?(?<%s>[%s]){1}:(?<%s>[%s-%s]):(?<%s>\\d+)(:(?<%s>[%s-%s]))?]?$",
			Group.ACCOUNT, USteamAccount.CHAR_BASE,
			Group.UNIVERSE, USteamUniverse.BASE, USteamUniverse.MAX,
			Group.ID,
			Group.INSTANCE, USteamInstance.MIN, USteamInstance.MAX
	);

	/**
	 * A Steam ID64 regular expression.
	 */
	public static final String ID64 = "^[0-9]{17}$";

	/**
	 * A Steam vanity ID regular expression.
	 */
	public static final String VANITY_ID = "^[a-zA-Z0-9_-]{2,32}$";

	/**
	 * A Steam invite code regular expression.
	 */
	public static final String INVITE_CODE = format("^[%s]+$",
			USteamInvite.CODE_BASE + USteamInvite.CODE_DELIMITER
	);

	/**
	 * A CS:GO friend code regular expression.
	 */
	public static final String CSGO_CODE = format("^[%s]{10}$",
			USteamCsgo.CODE_BASE + USteamCsgo.CODE_DELIMITER
	);

	/**
	 * A Steam /profiles/ URL regular expression.
	 *
	 * <p>Possible matching group names:
	 * <ul>
	 *     <li>{@code USteamRegex.Group.ID}
	 * </ul>
	 */
	public static final String PROFILE_URL = format("^https?://(?:www\\.|(?!www))(?:my\\.steamchina|steamcommunity)\\.com/(profiles|gid)\\/(?<%s>.+?)(?:/|$)$",
			Group.ID
	);

	/**
	 * A Steam /user/ URL regular expression.
	 *
	 * <p>Possible matching group names:
	 * <ul>
	 *     <li>{@code USteamRegex.Group.ID}
	 * </ul>
	 */
	public static final String USER_URL = format("^https?://(?:www\\.|(?!www))(?:(?:my\\.steamchina|steamcommunity)\\.com/user|s\\.team/p)/(?<%s>[\\w-]+)(?:/|$)$",
			Group.ID
	);

	private USteamRegex() {
		throw new UnsupportedOperationException();
	}
}
