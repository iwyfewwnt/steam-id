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

package io.github.u004.steamid.types;

import io.github.u004.steamid.utils.USteamEndpoint;
import io.github.u004.uwutils.UwArray;
import io.github.u004.uwutils.UwMap;
import io.vavr.control.Option;

import java.util.Map;

/**
 * A Steam endpoint enums.
 *
 * <p>Wraps {@link USteamEndpoint}.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public enum ESteamEndpoint {

	/**
	 * An /id/ endpoint enum.
	 *
	 * <p>Wraps {@link USteamEndpoint#ID}.
	 */
	ID(USteamEndpoint.ID),

	/**
	 * A /profiles/ endpoint enum.
	 *
	 * <p>Wraps {@link USteamEndpoint#PROFILES}.
	 */
	PROFILES(USteamEndpoint.PROFILES),

	/**
	 * An /user/ endpoint enum.
	 *
	 * <p>Wraps {@link USteamEndpoint#USER}.
	 */
	USER(USteamEndpoint.USER),

	/**
	 * A /p/ endpoint enum.
	 *
	 * <p>Wraps {@link USteamEndpoint#P}.
	 */
	P(USteamEndpoint.P);

	private static final Map<String, ESteamEndpoint> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamEndpoint.class
	);

	/**
	 * Endpoint string.
	 */
	private final String value;

	/**
	 * Initialize an {@code ESteamEndpoint} instance.
	 *
	 * @param value		endpoint string
	 */
	ESteamEndpoint(String value) {
		this.value = value;
	}

	/**
	 * Get this endpoint string.
	 *
	 * @return	endpoint string
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamEndpoint} instance by its endpoint string.
	 *
	 * @param value		endpoint string
	 * @return			{@code ESteamEndpoint} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamEndpoint> fromValue(String value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamEndpoint} instance by its index.
	 *
	 * @param index		{@code ESteamEndpoint} instance index
	 * @return			{@code ESteamEndpoint} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamEndpoint> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamEndpoint} instance by its endpoint string.
	 *
	 * @param value		endpoint string
	 * @return			{@code ESteamEndpoint} instance or null
	 */
	public static ESteamEndpoint fromValueRaw(String value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamEndpoint} instance by its index.
	 *
	 * @param index		{@code ESteamEndpoint} instance index
	 * @return			{@code ESteamEndpoint} instance or null
	 */
	public static ESteamEndpoint fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
