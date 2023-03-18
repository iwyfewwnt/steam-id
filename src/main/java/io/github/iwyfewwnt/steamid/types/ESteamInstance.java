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

package io.github.iwyfewwnt.steamid.types;

//import io.github.iwyfewwnt.steamid.utils.USteamBit;
import io.github.iwyfewwnt.steamid.utils.USteamInstance;
import io.github.iwyfewwnt.uwutils.UwArray;
import io.github.iwyfewwnt.uwutils.UwEnum;
import io.github.iwyfewwnt.uwutils.UwMap;
import io.github.iwyfewwnt.uwutils.UwObject;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam account instance type enums.
 *
 * <p>Wraps {@link USteamInstance}.
 */
@SuppressWarnings("unused")
public enum ESteamInstance {

	/**
	 * An account instance type enum - All.
	 *
	 * <p>Wraps {@link USteamInstance#ALL}.
	 */
	ALL(USteamInstance.ALL),

	/**
	 * An account instance type enum - Desktop.
	 *
	 * <p>Wraps {@link USteamInstance#DESKTOP}.
	 */
	DESKTOP(USteamInstance.DESKTOP),

	/**
	 * An account instance type enum - Console.
	 *
	 * <p>Wraps {@link USteamInstance#CONSOLE}.
	 */
	CONSOLE(USteamInstance.CONSOLE),

	/**
	 * An account instance type enum - Web.
	 *
	 * <p>Wraps {@link USteamInstance#WEB}.
	 */
	WEB(USteamInstance.WEB),

	/**
	 * A chat flag enum - Clan.
	 *
	 * <p>Wraps {@link USteamInstance#CLAN}.
	 */
	CLAN(USteamInstance.CLAN),

	/**
	 * A chat flag enum - Lobby.
	 *
	 * <p>Wraps {@link USteamInstance#LOBBY}.
	 */
	LOBBY(USteamInstance.LOBBY),

	/**
	 * A chat flag enum - Matchmaking Lobby.
	 *
	 * <p>Wraps {@link USteamInstance#MM_LOBBY}.
	 */
	MM_LOBBY(USteamInstance.MM_LOBBY);

//	/**
//	 * An account instance offset in the bit vector.
//	 *
//	 * <p>Wraps {@link USteamBit#ACCOUNT_INSTANCE_OFFSET}.
//	 */
//	public static final int OFFSET = USteamBit.ACCOUNT_INSTANCE_OFFSET;

	/**
	 * An array of {@link ESteamInstance} instances.
	 */
	private static final ESteamInstance[] VALUES = UwEnum.values(ESteamInstance.class);

	/**
	 * A map of {@link ESteamInstance} instances by their identifier field.
	 */
	private static final Map<Integer, ESteamInstance> MAP_BY_ID = UwMap.newMapByFieldOrNull(
			entry -> entry.id, ESteamInstance.class
	);

	/**
	 * An account instance type identifier.
	 */
	private final int id;

	/**
	 * Initialize a {@link ESteamInstance} instance.
	 *
	 * @param id	account instance type identifier
	 */
	ESteamInstance(int id) {
		this.id = id;
	}

	/**
	 * Get this account instance type identifeir.
	 *
	 * @return	account instance type identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return a default value if failed.
	 *
	 * @param id			account instance type identifier of the instance
	 * @param defaultValue	default value to return of failure
	 * @return				associated {@link ESteamInstance} instance or the default value
	 */
	public static ESteamInstance fromIdOrElse(Integer id, ESteamInstance defaultValue) {
		return UwMap.getOrElse(id, MAP_BY_ID, defaultValue);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return a default value if failed.
	 *
	 * @param id					account instance type identifier of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamInstance} instance or the default value
	 */
	public static ESteamInstance fromIdOrElse(Integer id, Supplier<ESteamInstance> defaultValueSupplier) {
		return UwObject.getIfNull(fromIdOrNull(id), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its account instance type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamInstance#fromIdOrElse(Integer, ESteamInstance)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id			account instance type identifier of the instance
	 * @return				associated {@link ESteamInstance} instance or {@code null}
	 */
	public static ESteamInstance fromIdOrNull(Integer id) {
		return fromIdOrElse(id, (ESteamInstance) null);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return of failure
	 * @return				associated {@link ESteamInstance} instance or the default value
	 */
	public static ESteamInstance fromIndexOrElse(Integer index, ESteamInstance defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	suppleir from which get the default value
	 * @return						associated {@link ESteamInstance} instance or the defualt value
	 */
	public static ESteamInstance fromIndexOrElse(Integer index, Supplier<ESteamInstance> defaultValueSupplier) {
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamInstance} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamInstance#fromIndexOrElse(Integer, ESteamInstance)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index			index of the instance
	 * @return				associated {@link ESteamInstance} instance or {@code null}
	 */
	public static ESteamInstance fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamInstance) null);
	}
}
