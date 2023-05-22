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

import io.github.iwyfewwnt.steamid.utils.USteamDomain;
import io.github.iwyfewwnt.uwutils.*;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam domain type enums.
 *
 * <p>Wraps {@link USteamDomain}.
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

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = ESteamDomain.class.getSimpleName();

	/**
	 * An array of {@link ESteamDomain} instances.
	 */
	private static final ESteamDomain[] VALUES = UwEnum.values(ESteamDomain.class);

	/**
	 * A map of {@link ESteamDomain} instances by their domain field.
	 */
	private static final Map<String, ESteamDomain> MAP_BY_DOMAIN = UwMap.newMapByFieldOrNull(
			entry -> entry.value, ESteamDomain.class
	);

	/**
	 * A domain string.
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
	 * Initialize an {@link ESteamDomain} instance.
	 *
	 * @param value		domain string
	 */
	ESteamDomain(String value) {
		this.value = value;

		this.stringCacheMutex = new Object();
	}

	/**
	 * Get this domain string.
	 *
	 * @return	domain string
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
	 * Get the domain string from the provided {@link ESteamDomain} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamDomain} instance is {@code null}.
	 * </ul>
	 *
	 * @param domain		enum value of the domain type from which get the string
	 * @param defaultValue	default value to return on failure
	 * @return				domain string or the default value
	 */
	public static String getAsStringOrElse(ESteamDomain domain, String defaultValue) {
		return UwObject.ifNotNull(domain, ESteamDomain::getAsString, defaultValue);
	}

	/**
	 * Get the domain string from the provided {@link ESteamDomain} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamDomain} instance is {@code null}.
	 * </ul>
	 *
	 * @param domain				enum value of the domain type from which get the string
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						domain string or the default value
	 */
	public static String getAsStringOrElse(ESteamDomain domain, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(domain), defaultValueSupplier);
	}

	/**
	 * Get the domain string from the provided {@link ESteamDomain} instance
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamDomain} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getAsStringOrElse(ESteamDomain, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param domain	enum value of the domain type from which get the string
	 * @return			domain string or the empty one
	 */
	public static String getAsStringOrEmpty(ESteamDomain domain) {
		return getAsStringOrElse(domain, UwString.EMPTY);
	}

	/**
	 * Get the domain string from the provided {@link ESteamDomain} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamDomain} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getAsStringOrElse(ESteamDomain, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param domain	enum value of the domain type from which get the string
	 * @return			domain string or {@code null}
	 */
	public static String getAsStringOrNull(ESteamDomain domain) {
		return getAsStringOrElse(domain, (String) null);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its domain string
	 * or return a default value if failed.
	 *
	 * @param value			domain string of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamDomain} instance or the default value
	 */
	public static ESteamDomain fromStringOrElse(String value, ESteamDomain defaultValue) {
		return UwMap.getOrElse(value, MAP_BY_DOMAIN, defaultValue);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its domain string
	 * or return a default value if failed.
	 *
	 * @param value					domain string of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamDomain} instance or the default value
	 */
	public static ESteamDomain fromStringOrElse(String value, Supplier<ESteamDomain> defaultValueSupplier) {
		return UwObject.ifNull(fromStringOrNull(value), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its domain string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromStringOrElse(String, ESteamDomain)}
	 * w/ {@code null} as the default value.
	 *
	 * @param value		domain string of the instance
	 * @return			associated {@link ESteamDomain} instance or {@code null}
	 */
	public static ESteamDomain fromStringOrNull(String value) {
		return fromStringOrElse(value, (ESteamDomain) null);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamDomain} instance or the default value
	 */
	public static ESteamDomain fromIndexOrElse(Integer index, ESteamDomain defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its index
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamDomain} instance or the default value
	 */
	public static ESteamDomain fromIndexOrElse(Integer index, Supplier<ESteamDomain> defaultValueSupplier) {
		return UwObject.ifNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromIndexOrElse(Integer, ESteamDomain)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamDomain} instance or {@code null}
	 */
	public static ESteamDomain fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamDomain) null);
	}
}
