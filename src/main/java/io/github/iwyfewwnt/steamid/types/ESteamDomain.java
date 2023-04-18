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

import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A Steam domain type enums.
 *
 * <p>Wraps {@link USteamDomain}.
 */
@SuppressWarnings("unused")
public enum ESteamDomain implements Serializable {

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
			entry -> entry.domain, ESteamDomain.class
	);

	/**
	 * A domain string.
	 */
	private final String domain;

	/**
	 * Initialize an {@link ESteamDomain} instance.
	 *
	 * @param domain	domain string
	 */
	ESteamDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Get this domain string.
	 *
	 * @return	domain string
	 */
	public String getDomain() {
		return this.domain;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return SIMPLE_NAME + "::" + this.name() + "["
				+ "domain=\"" + this.domain + "\""
				+ "]";
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
	public static String getDomainOrElse(ESteamDomain domain, String defaultValue) {
		if (domain == null) {
			return defaultValue;
		}

		return domain.getDomain();
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
	public static String getDomainOrElse(ESteamDomain domain, Supplier<String> defaultValueSupplier) {
		return UwObject.getIfNull(getDomainOrNull(domain), defaultValueSupplier);
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
	 * <p>Wraps {@link ESteamDomain#getDomainOrElse(ESteamDomain, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param domain	enum value of the domain type from which get the string
	 * @return			domain string or the empty one
	 */
	public static String getDomainOrEmpty(ESteamDomain domain) {
		return getDomainOrElse(domain, UwString.EMPTY);
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
	 * <p>Wraps {@link ESteamDomain#getDomainOrElse(ESteamDomain, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param domain	enum value of the domain type from which get the string
	 * @return			domain string or {@code null}
	 */
	public static String getDomainOrNull(ESteamDomain domain) {
		return getDomainOrElse(domain, (String) null);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its domain string
	 * or return a default value if failed.
	 *
	 * @param domain		domain string of the instance
	 * @param defaultValue	default value to return on failure
	 * @return				associated {@link ESteamDomain} instance or the default value
	 */
	public static ESteamDomain fromDomainOrElse(String domain, ESteamDomain defaultValue) {
		return UwMap.getOrElse(domain, MAP_BY_DOMAIN, defaultValue);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its domain string
	 * or return a default value if failed.
	 *
	 * @param domain				domain string of the instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						associated {@link ESteamDomain} instance or the default value
	 */
	public static ESteamDomain fromDomainOrElse(String domain, Supplier<ESteamDomain> defaultValueSupplier) {
		return UwObject.getIfNull(fromDomainOrNull(domain), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its domain string
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamDomain#fromDomainOrElse(String, ESteamDomain)}
	 * w/ {@code null} as the default value.
	 *
	 * @param domain	domain string of the instance
	 * @return			associated {@link ESteamDomain} instance or {@code null}
	 */
	public static ESteamDomain fromDomainOrNull(String domain) {
		return fromDomainOrElse(domain, (ESteamDomain) null);
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
		return UwObject.getIfNull(fromIndexOrNull(index), defaultValueSupplier);
	}

	/**
	 * Get an {@link ESteamDomain} instance by its index
	 * or return {@code null} if failed.
	 *
	 * <p>Wraps {@link ESteamDomain#fromIndexOrElse(Integer, ESteamDomain)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the instance
	 * @return			associated {@link ESteamDomain} instance or {@code null}
	 */
	public static ESteamDomain fromIndexOrNull(Integer index) {
		return fromIndexOrElse(index, (ESteamDomain) null);
	}
}
