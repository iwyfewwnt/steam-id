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

/**
 * A Steam endpoints utility.
 *
 * <p>{@code USteamEndpoint} is the utility class
 * for Steam's endpoints that were used in this project.
 *
 * @since 0.1.0
 */
public final class USteamEndpoint {

	/**
	 * An /id/ endpoint string.
	 *
	 * <p>Used in conjunction with the {@link USteamDomain#COMMUNITY} domain.
	 */
	public static final String ID = "id";

	/**
	 * A /profiles/ endpoint string.
	 *
	 * <p>Used in conjunction with the {@link USteamDomain#COMMUNITY}
	 * and {@link USteamDomain#CHINA} domains.
	 */
	public static final String PROFILES = "profiles";

	/**
	 * An /user/ endpoint string.
	 *
	 * <p>Used in conjunction with the {@link USteamDomain#COMMUNITY} domain.
	 */
	public static final String USER = "user";

	/**
	 * A /p/ endpoint string.
	 *
	 * <p>Used in conjunction with the {@link USteamDomain#INVITE} domain.
	 */
	public static final String P = "p";

	private USteamEndpoint() {
		throw new UnsupportedOperationException();
	}
}
