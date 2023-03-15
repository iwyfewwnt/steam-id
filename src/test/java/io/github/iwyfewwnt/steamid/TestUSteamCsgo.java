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

package io.github.iwyfewwnt.steamid;

//import io.github.iwyfewwnt.steamid.utils.USteamCsgo;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.NullAndEmptySource;
//import org.junit.jupiter.params.provider.ValueSource;
//
//import static org.junit.jupiter.api.Assertions.*;

final class TestUSteamCsgo {

//	@ParameterizedTest
//	@CsvSource(value = {
//			USteamCsgo.MIN_CODE + " " + SteamId.MIN_XUID,
//			USteamCsgo.MAX_CODE + " " + SteamId.MAX_XUID
//	}, delimiter = ' ')
//	void Test_FromFriendCode_ToFriendCode_MustBeCompatible(String code, long xuid) {
//		assertDoesNotThrow(() -> {
//			assertEquals(code, USteamCsgo.toCode(xuid)
//					.get());
//
//			assertEquals(xuid, USteamCsgo.fromCode(code)
//					.get());
//		});
//	}
//
//	@ParameterizedTest
//	@NullAndEmptySource
//	void Test_FromCode_ShouldFail_WhenPassedInvalidCode(String code) {
//		assertTrue(USteamCsgo.fromCode(code).isFailure());
//	}
//
//	@ParameterizedTest
//	@ValueSource(longs = { Long.MIN_VALUE, SteamId.MIN_XUID - 1, SteamId.BASE_XUID, SteamId.MAX_XUID + 1, Long.MAX_VALUE })
//	void Test_ToCode_ShouldFail_WhenPassedInvalidXuid(long xuid) {
//		assertTrue(USteamCsgo.toCode(xuid).isFailure());
//	}
//
//	@ParameterizedTest
//	@ValueSource(strings = { USteamCsgo.MIN_CODE, USteamCsgo.MAX_CODE})
//	void Test_FromCode_ShouldSuccess_WhenPassedUntrimmedString(String code) {
//		assertTrue(USteamCsgo.fromCode(" " + code + " ").isSuccess());
//	}

	private TestUSteamCsgo() {
	}
}
