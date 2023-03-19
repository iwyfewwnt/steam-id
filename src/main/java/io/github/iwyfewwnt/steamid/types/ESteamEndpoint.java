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
 * A Steam endpoint enums.
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
	 * An array of {@link ESteamEndpoint} instances.
	 */
	private static final ESteamEndpoint[] VALUES = UwEnum.values(ESteamEndpoint.class);

	/**
	 * A map of {@link ESteamEndpoint} instances by their endpoint field.
	 */
	private static final Map<String, ESteamEndpoint> MAP_BY_ENDPOINT = UwMap.newMapByFieldOrNull(
			entry -> entry.endpoint, ESteamEndpoint.class
	);

	/**
	 * An endpoint string.
	 */
	private final String endpoint;

	/**
	 * Initialize an {@link ESteamEndpoint} instance.
	 *
	 * @param endpoint	endpoint string
	 */
	ESteamEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Get this endpoint string.
	 *
	 * @return	endpoint string
	 */
	public String getEndpoint() {
		return this.endpoint;
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
	public static String getEndpointOrElse(ESteamEndpoint endpoint, String defaultValue) {
		if (endpoint == null) {
			return defaultValue;
		}

		return endpoint.getEndpoint();
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
	public static String getEndpointOrElse(ESteamEndpoint endpoint, Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(getEndpintOrNull(endpoint), defaultValueSupplier);
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
	 * <p>Wraps {@link ESteamEndpoint#getEndpointOrElse(ESteamEndpoint, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param endpoint	enum value of the endpoint type from which get the string
	 * @return			endpoint string or the empty one
	 */
	public static String getEndpointOrEmpty(ESteamEndpoint endpoint) {
		return getEndpointOrElse(endpoint, UwString.EMPTY);
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
	 * <p>Wraps {@link ESteamEndpoint#getEndpointOrElse(ESteamEndpoint, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param endpoint	enum value of the endpoint type from which get the string
	 * @return			endpoint string or {@code null}
	 */
	public static String getEndpintOrNull(ESteamEndpoint endpoint) {
		return getEndpointOrElse(endpoint, (String) null);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return a default value if failed.
	 *
	 * @param endpoint		endpoint string of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamEndpoint} instance or the default value
	 */
	public static ESteamEndpoint fromEndpointOrElse(String endpoint, ESteamEndpoint defaultValue) {
		return UwMap.getOrElse(endpoint, MAP_BY_ENDPOINT, defaultValue);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return a default value if failed.
	 *
	 * @param endpoint				endpoint string of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamEndpoint} instance or the default value
	 */
	public static ESteamEndpoint fromEndpointOrElse(String endpoint, Supplier<ESteamEndpoint> defaultValueSupplier) {
		return UwObject.getIfNull(fromEndpointOrNull(endpoint), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamEndpoint#fromEndpointOrElse(String, ESteamEndpoint)}
	 * w/ {@code null} as the default value.
	 *
	 * @param endpoint		endpoint string of the instance
	 * @return				associated {@link ESteamEndpoint} instance or {@code null}
	 */
	public static ESteamEndpoint fromEndpointOrNull(String endpoint) {
		return fromEndpointOrElse(endpoint, (ESteamEndpoint) null);
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
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamEndpoint} instance by its endpoint string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamEndpoint#fromIndexOrElse(Integer, ESteamEndpoint)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamEndpoint} instance or {@code null}
	 */
	public static ESteamEndpoint fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamEndpoint) null);
	}
}
