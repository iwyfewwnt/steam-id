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

import io.github.u004.steamid.utils.USteamDomain;
import io.github.u004.uwutils.UwArray;
import io.github.u004.uwutils.UwMap;
import io.vavr.control.Option;

import java.util.Map;

/**
 * A Steam domain enums.
 *
 * <p>Wraps {@link USteamDomain}.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public enum ESteamDomain {

	/**
	 * A worldwide Steam community domain enum.
	 *
	 * <p>Wraps {@link USteamDomain#COMMUNITY}.
	 */
	COMMUNITY(USteamDomain.COMMUNITY),

	/**
	 * A Steam invite domain enum.
	 *
	 * <p>Wraps {@link USteamDomain#INVITE}.
	 */
	INVITE(USteamDomain.INVITE),

	/**
	 * A Steam China community domain enum.
	 *
	 * <p>Wraps {@link USteamDomain#CHINA}.
	 */
	CHINA(USteamDomain.CHINA);

	private static final Map<String, ESteamDomain> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamDomain.class
	);

	/**
	 * Domain string.
	 */
	private final String value;

	/**
	 * Initialize an {@code ESteamDomain} instance.
	 *
	 * @param value		domain string
	 */
	ESteamDomain(String value) {
		this.value = value;
	}

	/**
	 * Get this domain string.
	 *
	 * @return	domain string
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamDomain} instance by its domain string.
	 *
	 * @param value		domain string
	 * @return			{@code ESteamDomain} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamDomain> fromValue(String value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamDomain} instance by its index.
	 *
	 * @param index		{@code ESteamDomain} instance index
	 * @return			{@code ESteamDomain} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamDomain> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamDomain} instance by its domain string.
	 *
	 * @param value		domain string
	 * @return			{@code ESteamDomain} instance or null
	 */
	public static ESteamDomain fromValueRaw(String value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamDomain} instance by its index.
	 *
	 * @param index		{@code ESteamDomain} instance index
	 * @return			{@code ESteamDomain} instance or null
	 */
	public static ESteamDomain fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
