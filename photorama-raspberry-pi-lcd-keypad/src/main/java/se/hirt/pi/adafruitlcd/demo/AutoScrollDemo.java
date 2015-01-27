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
package se.hirt.pi.adafruitlcd.demo;

import java.io.IOException;

import se.hirt.pi.adafruitlcd.ILCD;

/**
 * Tests autoscroll. Check out the documentation for the HD44780 for more info
 * on how the buffer is handled.
 * 
 * @author Marcus Hirt
 */
public class AutoScrollDemo implements LCDTest {

	@Override
	public String getName() {
		return "AutoScroll";
	}

	@Override
	public void run(ILCD lcd) throws IOException {
		lcd.clear();
		lcd.setText("AutoScroll off:\n");
		lcd.setCursorPosition(1, 0);
		lcd.setAutoScrollEnabled(false);
		for (int i = 0; i < 24; i++) {
			Util.sleep(200);
			lcd.setText(1, "" + (i % 10));
		}

		lcd.clear();
		lcd.setText("AutoScroll on:\n");
		lcd.setAutoScrollEnabled(true);
		lcd.setCursorPosition(1, 14);
		for (int i = 0; i < 24; i++) {
			Util.sleep(200);
			lcd.setText(1, "" + (i % 10));
		}
		lcd.setAutoScrollEnabled(false);
		lcd.setCursorPosition(0, 0);
		lcd.clear();
		lcd.setText("AutoScroll:\nDone!");
	}

}
