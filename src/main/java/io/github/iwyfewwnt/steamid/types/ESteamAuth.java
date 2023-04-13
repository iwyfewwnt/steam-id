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

import io.github.iwyfewwnt.steamid.utils.USteamAuth;
import io.github.iwyfewwnt.uwutils.UwArray;
import io.github.iwyfewwnt.uwutils.UwEnum;
import io.github.iwyfewwnt.uwutils.UwMap;
import io.github.iwyfewwnt.uwutils.UwObject;

import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * A Steam authentication type enums.
 *
 * <p>Wraps {@link USteamAuth}.
 */
@SuppressWarnings("unused")
public enum ESteamAuth implements Serializable {

	/**
	 * An authentication type enum - No.
	 *
	 * <p>Wraps {@link USteamAuth#NO}.
	 */
	NO(USteamAuth.NO),

	/**
	 * An authentication type enum - Yes.
	 *
	 * <p>Wraps {@link USteamAuth#YES}.
	 */
	YES(USteamAuth.YES);

	/**
	 * A minimum authentication type according to its utility alternative.
	 *
	 * @see USteamAuth#MIN
	 */
	public static final ESteamAuth MIN = NO;

	/**
	 * A maximum authentication type according to its utility alternative.
	 *
	 * @see USteamAuth#MAX
	 */
	public static final ESteamAuth MAX = YES;

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = ESteamAuth.class.getSimpleName();

	/**
	 * An array of {@link ESteamAuth} instances.
	 */
	private static final ESteamAuth[] VALUES = UwEnum.values(ESteamAuth.class);

	/**
	 * A map of {@link ESteamAuth} instances by their identifier field.
	 */
	private static final Map<Integer, ESteamAuth> MAP_BY_ID = UwMap.newMapByFieldOrNull(
			entry -> entry.id, ESteamAuth.class
	);

	/**
	 * An account authentication type identifier.
	 */
	private final int id;

	/**
	 * Initialize an {@link ESteamAuth} instance.
	 *
	 * @param id	account authentication type indentifier
	 */
	ESteamAuth(int id) {
		this.id = id;
	}

	/**
	 * Get this account authentication type identifier.
	 *
	 * @return	account authentication type identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringJoiner(", ", SIMPLE_NAME + "::" + this.name() + "[", "]")
				.add("id=" + this.id)
				.toString();
	}

	/**
	 * Get the account authentication type identifier from the provided {@link ESteamAuth} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAuth} instance is {@code null}.
	 * </ul>
	 *
	 * @param auth			enum value of the account authentication type from which get the identifier
	 * @param defaultValue	default value to return on failure
	 * @return				account authentication type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamAuth auth, Integer defaultValue) {
		if (auth == null) {
			return defaultValue;
		}

		return auth.getId();
	}

	/**
	 * Get the account authentication type identifier from the provided {@link ESteamAuth} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAuth} instance is {@code null}.
	 * </ul>
	 *
	 * @param auth					enum value of the account authentication type from which get the identifier
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						account authentication type identifier or the default value
	 */
	public static Integer getIdOrElse(ESteamAuth auth, Supplier<Integer> defaultValueSupplier) {
		return UwObject.getIfNull(getIdOrNull(auth), defaultValueSupplier);
	}

	/**
	 * Get the account authentication type identifier from the provided {@link ESteamAuth} instance
	 * or return the {@link USteamAuth#MIN} value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAuth} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamAuth#getIdOrElse(ESteamAuth, Integer)}
	 * w/ {@link USteamAuth#MIN} as the default value.
	 *
	 * @param auth	enum value of the account authentication type from which get the identifier
	 * @return		account authentication type identifier or the {@link USteamAuth#MIN} value
	 */
	public static Integer getIdOrBase(ESteamAuth auth) {
		return getIdOrElse(auth, USteamAuth.MIN);
	}

	/**
	 * Get the account authentication type identifier from the provided {@link ESteamAuth} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamAuth} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link ESteamAuth#getIdOrElse(ESteamAuth, Integer)}
	 * w/ {@code null} as the default value.
	 *
	 * @param auth	enum value of the account authentication type from which get the identifier
	 * @return		account authentication type identifier or {@code null}
	 */
	public static Integer getIdOrNull(ESteamAuth auth) {
		return getIdOrElse(auth, (Integer) null);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its account authentication type identifier
	 * or return a default value if failed.
	 *
	 * @param id			account authentication type identifier of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamAuth} instance or the default value
	 */
	public static ESteamAuth fromIdOrElse(Integer id, ESteamAuth defaultValue) {
		return UwMap.getOrElse(id, MAP_BY_ID, defaultValue);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its account authentication type identifier
	 * or return a default value if failed.
	 *
	 * @param id					account authentication type identifier of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamAuth} instance or the default value
	 */
	public static ESteamAuth fromIdOrElse(Integer id, Supplier<ESteamAuth> defaultValueSupplier) {
		return UwObject.getIfNull(fromIdOrNull(id), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its account authentication type identifier
	 * or return the {@link ESteamAuth#MIN} value if failed.
	 *
	 * <p>Wraps {@link ESteamAuth#fromIdOrElse(Integer, ESteamAuth)}
	 * w/ {@link ESteamAuth#MIN} as the default value.
	 *
	 * @param id	account authentication type identifier of the instance
	 * @return		associated {@link ESteamAuth} instance or the {@link ESteamAuth#MIN} value
	 */
	public static ESteamAuth fromIdOrBase(Integer id) {
		return fromIdOrElse(id, MIN);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its account authentication type identifier
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamAuth#fromIdOrElse(Integer, ESteamAuth)}
	 * w/ {@code null} as the default value.
	 *
	 * @param id	account authentication type identifier of the instance
	 * @return		associated {@link ESteamAuth} instance or {@code null}
	 */
	public static ESteamAuth fromIdOrNull(Integer id) {
		return fromIdOrElse(id, (ESteamAuth) null);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamAuth} instance or the default value
	 */
	public static ESteamAuth fromIndexOrElse(Integer index, ESteamAuth defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamAuth} instance or the default value
	 */
	public static ESteamAuth fromIndexOrElse(Integer index, Supplier<ESteamAuth> defaultValueSupplier) {
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its index
	 * or return the {@link ESteamAuth#MIN} value if failed.
	 *
	 * <p>Wrapas {@link ESteamAuth#fromIndexOrElse(Integer, ESteamAuth)}
	 * w/ {@link ESteamAuth#MIN} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamAuth} instance or the {@link ESteamAuth#MIN} value
	 */
	public static ESteamAuth fromIndexOrBase(Integer index) {
		return fromIndexOrElse(index, MIN);
	}

	/**
	 * Get an {@link ESteamAuth} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wrapas {@link ESteamAuth#fromIndexOrElse(Integer, ESteamAuth)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamAuth} instance or {@code null}
	 */
	public static ESteamAuth fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamAuth) null);
	}
}
