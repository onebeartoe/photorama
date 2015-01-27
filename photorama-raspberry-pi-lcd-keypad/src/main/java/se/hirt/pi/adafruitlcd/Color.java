/*
 *  Copyright (C) 2014 Marcus Hirt
 *                     www.hirt.se
 *
 * This software is free:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) Marcus Hirt, 2014
 */
package se.hirt.pi.adafruitlcd;

public enum Color {
	OFF(0x00), RED(0x01), GREEN(0x02), BLUE(0x04), YELLOW(RED.getValue()
			+ GREEN.getValue()), TEAL(GREEN.getValue() + BLUE.getValue()), VIOLET(
			RED.getValue() + BLUE.getValue()), WHITE(RED.getValue()
			+ GREEN.getValue() + BLUE.getValue()), ON(WHITE.getValue());

	private final int value;

	Color(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Returns the matching color value, or WHITE if no matching color could be found.
	 * 
	 * @param colorValue
	 * @return
	 */
	public static Color getByValue(int colorValue) {
		for (Color c : values()) {
			if (c.getValue() == colorValue) {
				return c;
			}
		}
		return WHITE;
	}
}