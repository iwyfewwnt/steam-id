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

import io.github.iwyfewwnt.steamid.utils.USteamUrl;
import io.github.iwyfewwnt.uwutils.*;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam URL type enums.
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

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = ESteamUrl.class.getSimpleName();

	/**
	 * An array of {@link ESteamUrl} instances.
	 */
	private static final ESteamUrl[] VALUES = UwEnum.values(ESteamUrl.class);

	/**
	 * A map of {@link ESteamUrl} instances by their URL field.
	 */
	private static final Map<String, ESteamUrl> MAP_BY_URL = UwMap.newMapByFieldOrNull(
			entry -> entry.value, ESteamUrl.class
	);

	/**
	 * An URL string.
	 */
	private final String value;

	/**
	 * A {@link #toString()} cache.
	 */
	private volatile String stringCache;

	/**
	 * A {@link #stringCache} mutex.
	 */
	private final Object stringCacheMutex;

	/**
	 * Initialize an {@link ESteamUrl} instance.
	 *
	 * @param value		URL string
	 */
	ESteamUrl(String value) {
		this.value = value;

		this.stringCacheMutex = new Object();
	}

	/**
	 * Get this URL string.
	 *
	 * @return	URL string
	 */
	public String getAsString() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		//noinspection DuplicatedCode
		if (this.stringCache != null) {
			return this.stringCache;
		}

		synchronized (this.stringCacheMutex) {
			if (this.stringCache != null) {
				return this.stringCache;
			}

			return (this.stringCache = SIMPLE_NAME + "::" + this.name() + "["
					+ "value=\"" + this.value + "\""
					+ "]");
		}
	}

	/**
	 * Get the URL string from the provided {@link ESteamUrl} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUrl} instance is {@code null}.
	 * </ul>
	 *
	 * @param url			enum value of the URL type from which get the string
	 * @param defaultValue	default value to return on failure
	 * @return				URL string or the default value
	 */
	public static String getAsStringOrElse(ESteamUrl url, String defaultValue) {
		return UwObject.ifNotNull(url, ESteamUrl::getAsString, defaultValue);
	}

	/**
	 * Get the URL string from the provided {@link ESteamUrl} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUrl} instance is {@code null}.
	 * </ul>
	 *
	 * @param url					enum value of the URL type from which get the string
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						URL string or the default value
	 */
	public static String getAsStringOrElse(ESteamUrl url, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(url), defaultValueSupplier);
	}

	/**
	 * Get the URL string from the provided {@link ESteamUrl} instance
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUrl} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getAsStringOrElse(ESteamUrl, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param url	enum value of the URL type from which get the string
	 * @return		URL string or the empty one
	 */
	public static String getAsStringOrEmpty(ESteamUrl url) {
		return getAsStringOrElse(url, UwString.EMPTY);
	}

	/**
	 * Get the URL string from the provided {@link ESteamUrl} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamUrl} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getAsStringOrElse(ESteamUrl, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param url	enum value of the URL type from which get the string
	 * @return		URL string or {@code null}
	 */
	public static String getAsStringOrNull(ESteamUrl url) {
		return getAsStringOrElse(url, (String) null);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its URL string
	 * or return a default if failed.
	 *
	 * @param value			URL string of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamUrl} instance or the default value
	 */
	public static ESteamUrl fromStringOrElse(String value, ESteamUrl defaultValue) {
		return UwMap.getOrElse(value, MAP_BY_URL, defaultValue);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its URL string
	 * or return a default if failed.
	 *
	 * @param value					URL string of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamUrl} instance or the defualt value
	 */
	public static ESteamUrl fromStringOrElse(String value, Supplier<ESteamUrl> defaultValueSupplier) {
		return UwObject.ifNull(fromStringOrNull(value), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its URL string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromStringOrElse(String, ESteamUrl)}
	 * w/ {@code null} as the default value.
	 *
	 * @param value	URL string of the instance
	 * @return		associated {@link ESteamUrl} instance or {@code null}
	 */
	public static ESteamUrl fromStringOrNull(String value) {
		return fromStringOrElse(value, (ESteamUrl) null);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamUrl} instance or the default value
	 */
	public static ESteamUrl fromIndexOrElse(Integer index, ESteamUrl defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamUrl} instance or the default value
	 */
	public static ESteamUrl fromIndexOrElse(Integer index, Supplier<ESteamUrl> defaultValueSupplier) {
		return UwObject.ifNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromIndexOrElse(Integer, ESteamUrl)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamUrl} instance or {@code null}
	 */
	public static ESteamUrl fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamUrl) null);
	}
}
