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

import io.github.iwyfewwnt.steamid.utils.USteamEndpoint;
import io.github.iwyfewwnt.uwutils.*;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam endpoint type enums.
 *
 * <p>Wraps {@link USteamEndpoint}.
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

	/**
	 * A simple name of this class.
	 */
	private static final String SIMPLE_NAME = ESteamEndpoint.class.getSimpleName();

	/**
	 * An array of {@link ESteamEndpoint} instances.
	 */
	private static final ESteamEndpoint[] VALUES = UwEnum.values(ESteamEndpoint.class);

	/**
	 * A map of {@link ESteamEndpoint} instances by their endpoint field.
	 */
	private static final Map<String, ESteamEndpoint> MAP_BY_ENDPOINT = UwMap.newMapByFieldOrNull(
			entry -> entry.value, ESteamEndpoint.class
	);

	/**
	 * An endpoint string.
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
	 * Initialize an {@link ESteamEndpoint} instance.
	 *
	 * @param value		endpoint string
	 */
	ESteamEndpoint(String value) {
		this.value = value;

		this.stringCacheMutex = new Object();
	}

	/**
	 * Get this endpoint string.
	 *
	 * @return	endpoint string
	 */
	public String getAsString() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (this.stringCache != null) {
			return this.stringCache;
		}

		synchronized (this.stringCacheMutex) {
			if (this.stringCache != null) {
				return this.stringCache;
			}

			return (this.stringCache = SIMPLE_NAME + "::" + this.name() + "["
					+ "endpoint=\"" + this.value + "\""
					+ "]");
		}
	}

	/**
	 * Get the endpoint string from the provided {@link ESteamEndpoint} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamEndpoint} instance is {@code null}.
	 * </ul>
	 *
	 * @param endpoint		enum value of the endpoint type from which get the string
	 * @param defaultValue  default value to return on failure
	 * @return				endpoint string or the default value
	 */
	public static String getAsStringOrElse(ESteamEndpoint endpoint, String defaultValue) {
		return UwObject.ifNotNull(endpoint, ESteamEndpoint::getAsString, defaultValue);
	}

	/**
	 * Get the endpoint string from the provided {@link ESteamEndpoint} instance
	 * or return a default value if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamEndpoint} instance is {@code null}.
	 * </ul>
	 *
	 * @param endpoint				enum value of the endpoint type from which get the string
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @return						endpoint string or the default value
	 */
	public static String getAsStringOrElse(ESteamEndpoint endpoint, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(endpoint), defaultValueSupplier);
	}

	/**
	 * Get the endpoint string from the provided {@link ESteamEndpoint} instance
	 * or return an empty string if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamEndpoint} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getAsStringOrElse(ESteamEndpoint, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param endpoint	enum value of the endpoint type from which get the string
	 * @return			endpoint string or the empty one
	 */
	public static String getAsStringOrEmpty(ESteamEndpoint endpoint) {
		return getAsStringOrElse(endpoint, UwString.EMPTY);
	}

	/**
	 * Get the endpoint string from the provided {@link ESteamEndpoint} instance
	 * or return {@code null} if failed.
	 *
	 * <p>Possible failure cases:
	 * <ul>
	 *     <li>{@link ESteamEndpoint} instance is {@code null}.
	 * </ul>
	 *
	 * <p>Wraps {@link #getAsStringOrElse(ESteamEndpoint, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param endpoint	enum value of the endpoint type from which get the string
	 * @return			endpoint string or {@code null}
	 */
	public static String getAsStringOrNull(ESteamEndpoint endpoint) {
		return getAsStringOrElse(endpoint, (String) null);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return a default value if failed.
	 *
	 * @param value			endpoint string of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamEndpoint} instance or the default value
	 */
	public static ESteamEndpoint fromStringOrElse(String value, ESteamEndpoint defaultValue) {
		return UwMap.getOrElse(value, MAP_BY_ENDPOINT, defaultValue);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return a default value if failed.
	 *
	 * @param value					endpoint string of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamEndpoint} instance or the default value
	 */
	public static ESteamEndpoint fromStringOrElse(String value, Supplier<ESteamEndpoint> defaultValueSupplier) {
		return UwObject.ifNull(fromStringOrNull(value), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromStringOrElse(String, ESteamEndpoint)}
	 * w/ {@code null} as the default value.
	 *
	 * @param value		endpoint string of the instance
	 * @return			associated {@link ESteamEndpoint} instance or {@code null}
	 */
	public static ESteamEndpoint fromStringOrNull(String value) {
		return fromStringOrElse(value, (ESteamEndpoint) null);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return a default value if failed.
	 *
	 * @param index			index of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamEndpoint} instance or the default value
	 */
	public static ESteamEndpoint fromIndexOrElse(Integer index, ESteamEndpoint defaultValue) {
		return UwArray.getOrElse(index, VALUES, defaultValue);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return a default value if failed.
	 *
	 * @param index					index of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamEndpoint} instance or the default value
	 */
	public static ESteamEndpoint fromIndexOrElse(Integer index, Supplier<ESteamEndpoint> defaultValueSupplier) {
		return UwObject.ifNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link #fromIndexOrElse(Integer, ESteamEndpoint)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamEndpoint} instance or {@code null}
	 */
	public static ESteamEndpoint fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamEndpoint) null);
	}
}
