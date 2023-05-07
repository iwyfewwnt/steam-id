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

import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam URL type enums.
 *
 * <p>Wraps {@link USteamUrl}.
 */
@SuppressWarnings("unused")
public enum ESteamUrl implements Serializable {

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
			entry -> entry.url, ESteamUrl.class
	);

	/**
	 * An URL string.
	 */
	private final String url;

	/**
	 * A {@link ESteamUrl#toString()} cache.
	 */
	private transient String stringCache;

	/**
	 * Initialize an {@link ESteamUrl} instance.
	 *
	 * @param url	URL string
	 */
	ESteamUrl(String url) {
		this.url = url;
	}

	/**
	 * Get this URL string.
	 *
	 * @return	URL string
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (this.stringCache != null) {
			return this.stringCache;
		}

		return (this.stringCache = SIMPLE_NAME + "::" + this.name() + "["
				+ "url=\"" + this.url + "\""
				+ "]");
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
	public static String getUrlOrElse(ESteamUrl url, String defaultValue) {
		if (url == null) {
			return defaultValue;
		}

		return url.getUrl();
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
	public static String getUrlOrElse(ESteamUrl url, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getUrlOrNull(url), defaultValueSupplier);
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
	 * <p>Wraps {@link ESteamUrl#getUrlOrElse(ESteamUrl, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param url	enum value of the URL type from which get the string
	 * @return		URL string or the empty one
	 */
	public static String getUrlOrEmpty(ESteamUrl url) {
		return getUrlOrElse(url, UwString.EMPTY);
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
	 * <p>Wraps {@link ESteamUrl#getUrlOrElse(ESteamUrl, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param url	enum value of the URL type from which get the string
	 * @return		URL string or {@code null}
	 */
	public static String getUrlOrNull(ESteamUrl url) {
		return getUrlOrElse(url, (String) null);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its URL string
	 * or return a default if failed.
	 *
	 * @param url			URL string of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamUrl} instance or the default value
	 */
	public static ESteamUrl fromUrlOrElse(String url, ESteamUrl defaultValue) {
		return UwMap.getOrElse(url, MAP_BY_URL, defaultValue);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its URL string
	 * or return a default if failed.
	 *
	 * @param url					URL string of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamUrl} instance or the defualt value
	 */
	public static ESteamUrl fromUrlOrElse(String url, Supplier<ESteamUrl> defaultValueSupplier) {
		return UwObject.ifNull(fromUrlOrNull(url), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamUrl} instance by its URL string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamUrl#fromUrlOrElse(String, ESteamUrl)}
	 * w/ {@code null} as the default value.
	 *
	 * @param url	URL string of the instance
	 * @return		associated {@link ESteamUrl} instance or {@code null}
	 */
	public static ESteamUrl fromUrlOrNull(String url) {
		return fromUrlOrElse(url, (ESteamUrl) null);
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
	 * <p>Wraps {@link ESteamUrl#fromIndexOrElse(Integer, ESteamUrl)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamUrl} instance or {@code null}
	 */
	public static ESteamUrl fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamUrl) null);
	}
}
