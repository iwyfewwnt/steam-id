/*
 * Copyright 2023 u004
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

package io.github.iwyfewwnt.steamid;

import io.github.iwyfewwnt.steamid.utils.USteamCsgo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import io.github.iwyfewwnt.steamid.utils.USteamInvite;

import static org.junit.jupiter.api.Assertions.*;

public final class TestSteamId {

	@ParameterizedTest
	@ValueSource(longs = { Long.MIN_VALUE, SteamId.MIN_XUID - 1, SteamId.BASE_XUID, SteamId.MAX_XUID + 1, Long.MAX_VALUE })
	void Test_SteamId_ShouldBeInvalid_WhenPassedInvalidXuid(long xuid) {
		assertFalse(new SteamId(xuid).isValid());
	}

	@ParameterizedTest
	@CsvSource(value = {
			SteamId.MIN_XUID
					+ " " + SteamId.MIN_ID64
					+ " " + SteamId.MIN_ID2
					+ " " + SteamId.MIN_ID3
					+ " " + USteamCsgo.MIN_CODE
					+ " " + USteamInvite.MIN_CODE,

			SteamId.MAX_XUID
					+ " " + SteamId.MAX_ID64
					+ " " + SteamId.MAX_ID2
					+ " " + SteamId.MAX_ID3
					+ " " + USteamCsgo.MAX_CODE
					+ " " + USteamInvite.MAX_CODE
	}, delimiter = ' ')
	void Test_SteamId_MustBeCompatible(long xuid, long id64, String id2, String id3, String csgoCode, String inviteCode) {
		assertDoesNotThrow(() -> {
			SteamId defaultSteamId = new SteamId(xuid);

			// Conversion Equals
			assertEquals(xuid, defaultSteamId.getXuid().get());
			assertEquals(id64, defaultSteamId.getAsSteam64().get());
			assertEquals(id2, defaultSteamId.getAsSteam2().get());
			assertEquals(id3, defaultSteamId.getAsSteam3().get());
			assertEquals(csgoCode, defaultSteamId.getAsCsgoCode().get());
			assertEquals(inviteCode, defaultSteamId.getAsInviteCode().get());

			// Instance Equals
			assertEquals(defaultSteamId, SteamId.fromSteam64(id64).get());
			assertEquals(defaultSteamId, SteamId.fromSteam2(id2).get());
			assertEquals(defaultSteamId, SteamId.fromSteam3(id3).get());
			assertEquals(defaultSteamId, SteamId.fromCsgoCode(csgoCode).get());
			assertEquals(defaultSteamId, SteamId.fromInviteCode(inviteCode).get());

			// Invite Code Equals
			SteamId steamId = defaultSteamId;

			assertEquals(steamId.getAsInviteCode().get(),
					USteamInvite.toCode(steamId.getXuid().get()).get());

			steamId = SteamId.fromInviteCode(inviteCode).get();

			assertEquals(steamId.getXuid().get(),
					USteamInvite.fromCode(inviteCode).get());

			// CS:GO Code Equals
			steamId = defaultSteamId;

			assertEquals(steamId.getAsCsgoCode().get(),
					USteamCsgo.toCode(steamId.getXuid().get()).get());

			steamId = SteamId.fromCsgoCode(csgoCode)
					.get();

			assertEquals(steamId.getXuid().get(),
					USteamCsgo.fromCode(csgoCode).get());
		});
	}

	private TestSteamId() {
	}
}
