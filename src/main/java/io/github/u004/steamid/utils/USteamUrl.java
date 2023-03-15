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

package io.github.u004.steamid.utils;

import static java.lang.String.format;

/**
 * A Steam URLs utility.
 *
 * <p>{@code USteamUrl} is the utility class
 * for Steam URLs that were used in this project.
 *
 * @since 0.1.0
 */
public final class USteamUrl {

	/**
	 * A Steam vanity /id/ URL string.
	 */
	public static final String VANITY = url(USteamDomain.COMMUNITY, USteamEndpoint.ID);

	/**
	 * A Steam worldwide /profiles/ URL string.
	 */
	public static final String PROFILE = url(USteamDomain.COMMUNITY, USteamEndpoint.PROFILES);

	/**
	 * A Steam /user/ URL string.
	 */
	public static final String USER = url(USteamDomain.COMMUNITY, USteamEndpoint.USER);

	/**
	 * A Steam invite /p/ URL string.
	 */
	public static final String INVITE = url(USteamDomain.INVITE, USteamEndpoint.P);

	/**
	 * A Steam China /profiles/ URL string.
	 */
	public static final String CHINA = url(USteamDomain.CHINA, USteamEndpoint.PROFILES);

	private static String url(String domain, String endpoint) {
		return format("https://%s/%s/", domain, endpoint);
	}

	private USteamUrl() {
		throw new UnsupportedOperationException();
	}
}
