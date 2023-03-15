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

package io.github.iwyfewwnt.steamid.exceptions;

/**
 * An invalid CS:GO friend code state exception.
 *
 * <p>Describes that the CS:GO friend code is invalid.
 * <b>Stands for illegal state usage.</b>
 */
public final class InvalidCsgoCodeStateException extends IllegalStateException {

	/**
	 * Initialize an {@code InvalidCsgoCodeException} instance.
	 *
	 * <p>Default exception message: "CS:GO friend code has an invalid state".
	 */
	public InvalidCsgoCodeStateException() {
		super("CS:GO friend code has an invalid state");
	}
}