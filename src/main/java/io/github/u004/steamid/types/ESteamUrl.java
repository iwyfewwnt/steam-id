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

import io.github.u004.steamid.utils.USteamUrl;
import io.github.u004.uwutils.UwArray;
import io.github.u004.uwutils.UwMap;
import io.vavr.control.Option;

import java.util.Map;

/**
 * A Steam URL enums.
 *
 * <p>Wraps {@link USteamUrl}.
 */
@SuppressWarnings("unused")
public enum ESteamUrl {

	/**
	 * A Steam vanity /id/ URL enum.
	 *
	 * <p>Wraps {@link USteamUrl#VANITY}.
	 */
	VANITY(USteamUrl.VANITY),

	/**
	 * A Steam worldwide /profiles/ URL enum.
	 *
	 * <p>Wraps {@link USteamUrl#PROFILE}.
	 */
	PROFILE(USteamUrl.PROFILE),

	/**
	 * A Steam /user/ URL enum.
	 *
	 * <p>Wraps {@link USteamUrl#USER}.
	 */
	USER(USteamUrl.USER),

	/**
	 * A Steam invite /p/ URL enum.
	 *
	 * <p>Wraps {@link USteamUrl#INVITE}.
	 */
	INVITE(USteamUrl.INVITE),

	/**
	 * A Steam China /profiles/ URL enum.
	 *
	 * <p>Wraps {@link USteamUrl#CHINA}.
	 */
	CHINA(USteamUrl.CHINA);

	private static final Map<String, ESteamUrl> MAP_BY_VALUE = UwMap.newEnumMapByFieldRaw(
			entry -> entry.value, ESteamUrl.class
	);

	/**
	 * URL string.
	 */
	private final String value;

	/**
	 * Initialize a {@code ESteamUrl} instance.
	 *
	 * @param value		URL string
	 */
	ESteamUrl(String value) {
		this.value = value;
	}

	/**
	 * Get this URL string.
	 *
	 * @return		URL string
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Get the {@code ESteamUrl} instance by its URL string.
	 *
	 * @param value		URL string
	 * @return			{@code ESteamUrl} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamUrl> fromValue(String value) {
		return UwMap.get(value, MAP_BY_VALUE);
	}

	/**
	 * Get the {@code ESteamUrl} instance by its index.
	 *
	 * @param index		{@code ESteamUrl} instance index
	 * @return			{@code ESteamUrl} instance
	 * 					that wrapped in {@link Option}
	 */
	public static Option<ESteamUrl> fromIndex(Integer index) {
		return UwArray.get(index, values());
	}

	/**
	 * Get the {@code ESteamUrl} instance by its URL string.
	 *
	 * @param value		URL string
	 * @return			{@code ESteamUrl} instance or null
	 */
	public static ESteamUrl fromValueRaw(String value) {
		return fromValue(value).getOrNull();
	}

	/**
	 * Get the {@code ESteamUrl} instance by its index.
	 *
	 * @param index		{@code ESteamUrl} instance index
	 * @return			{@code ESteamUrl} instance or null
	 */
	public static ESteamUrl fromIndexRaw(Integer index) {
		return fromIndex(index).getOrNull();
	}
}
