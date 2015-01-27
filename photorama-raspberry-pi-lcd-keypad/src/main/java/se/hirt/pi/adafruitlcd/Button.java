/*
 *  Copyright (C) 2013 Marcus Hirt
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
 * Copyright (C) Marcus Hirt, 2013
 */
package se.hirt.pi.adafruitlcd;

import java.util.HashSet;
import java.util.Set;

import se.hirt.pi.adafruitlcd.impl.RealLCD;

/**
 * Enumeration of the Buttons on the LCD shield.
 * 
 * @author Marcus Hirt
 */
public enum Button {
	SELECT(0), RIGHT(1), DOWN(2), UP(3), LEFT(4);

	// Port expander input pin definition
	private final int pin;

	Button(int pin) {
		this.pin = pin;
	}

	/**
	 * The pin corresponding to the button.
	 * 
	 * @return the pin of the button.
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * Checks if a button is pressed, given an input mask.
	 * 
	 * @param mask
	 *            the input mask.
	 * @return true if the button is pressed, false otherwise.
	 * 
	 * @see RealLCD#buttonsPressedBitmask()
	 */
	public boolean isButtonPressed(int mask) {
		return ((mask >> getPin()) & 1) > 0;
	}

	/**
	 * Returns a set of the buttons that are pressed, according to the input
	 * mask.
	 * 
	 * @param mask
	 *            the input mask.
	 * @return a set of the buttons pressed.
	 * 
	 * @see RealLCD#buttonsPressedBitmask()
	 */
	public static Set<Button> getButtonsPressed(int mask) {
		Set<Button> buttons = new HashSet<Button>();
		for (Button button : values()) {
			if (button.isButtonPressed(mask)) {
				buttons.add(button);
			}
		}
		return buttons;
	}
}