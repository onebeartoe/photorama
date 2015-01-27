/*
 * This code was ported from the AdaFruit_CharLCDPlate.py code, in which the following license notes were present:
 * # Python library for Adafruit RGB-backlit LCD plate for Raspberry Pi.
 * # Written by Adafruit Industries.  MIT license.
 * # This is essentially a complete rewrite, but the calling syntax
 * # and constants are based on code from lrvick and LiquidCrystal.
 * # lrvic - https://github.com/lrvick/raspi-hd44780/blob/master/hd44780.py
 * # LiquidCrystal - https://github.com/arduino/Arduino/blob/master/libraries/LiquidCrystal/LiquidCrystal.cpp
 * 
 * As far as I'm concerned you can do whatever you want with this Java port. For anything I've added, it's free code
 * per my standard license (see below). 
 *
 * /Marcus Hirt
 * 
 *
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
package se.hirt.pi.adafruitlcd.impl;

import java.io.IOException;

import se.hirt.pi.adafruitlcd.Button;
import se.hirt.pi.adafruitlcd.Color;
import se.hirt.pi.adafruitlcd.ILCD;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * Javaification of the python script example for the Adafruit LCD shield. I
 * have deliberately kept this close to the original python script, including
 * most comments, even though it leads to less than beautiful code.
 * 
 * Here is an example on how to use this class:
 * <code>
 * LCD lcd = new LCD();
 * lcd.setText("Hello World!\n2nd Hello World!");
 * </code>
 * 
 * Here is an example with buttons:
 * <code>
 * final LCD lcd = new LCD();
 * lcd.setText("LCD Test!\nPress up/down...");
 * ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
 * observer.addButtonListener(new ButtonListener() {
 * 		@Override
 *      public void onButtonPressed(Button button) {
 *      	lcd.clear();
 *      	lcd.setText(button.toString());
 *      }
 * });
 * </code>
 * 
 * For more examples, check out the se.hirt.adafruitlcd.test package.
 * 
 * @author Marcus Hirt
 */
public class RealLCD implements ILCD {
	public enum Direction {
		LEFT, RIGHT;
	}

	// LCD Commands
	private static final int LCD_CLEARDISPLAY = 0x01;
	private static final int LCD_RETURNHOME = 0x02;
	private static final int LCD_ENTRYMODESET = 0x04;
	private static final int LCD_DISPLAYCONTROL = 0x08;
	private static final int LCD_CURSORSHIFT = 0x10;
	// private static final int LCD_FUNCTIONSET = 0x20;
	// private static final int LCD_SETCGRAMADDR = 0x40;
	private static final int LCD_SETDDRAMADDR = 0x80;

	// Flags for display on/off control
	private static final int LCD_DISPLAYON = 0x04;
	// private static final int LCD_DISPLAYOFF = 0x00;
	private static final int LCD_CURSORON = 0x02;
	private static final int LCD_CURSOROFF = 0x00;
	private static final int LCD_BLINKON = 0x01;
	private static final int LCD_BLINKOFF = 0x00;

	// Flags for display entry mode
	// private static final int LCD_ENTRYRIGHT = 0x00;
	private static final int LCD_ENTRYLEFT = 0x02;
	private static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
	private static final int LCD_ENTRYSHIFTDECREMENT = 0x00;

	// Flags for display/cursor shift
	private static final int LCD_DISPLAYMOVE = 0x08;
	private static final int LCD_CURSORMOVE = 0x00;
	private static final int LCD_MOVERIGHT = 0x04;
	private static final int LCD_MOVELEFT = 0x00;

	// Port expander registers
	// IOCON when Bank 0 active
	private static final int MCP23017_IOCON_BANK0 = 0x0A;
	// IOCON when Bank 1 active
	private static final int MCP23017_IOCON_BANK1 = 0x15;

	// These are register addresses when in Bank 1 only:
	private static final int MCP23017_GPIOA = 0x09;
	private static final int MCP23017_IODIRB = 0x10;
	private static final int MCP23017_GPIOB = 0x19;

	// The LCD data pins (D4-D7) connect to MCP pins 12-9 (PORTB4-1), in
	// that order. Because this sequence is 'reversed,' a direct shift
	// won't work. This table remaps 4-bit data values to MCP PORTB
	// outputs, incorporating both the reverse and shift.
	private static final int[] SHIFT_REVERSE = { 0x00, 0x10, 0x08, 0x18, 0x04,
			0x14, 0x0C, 0x1C, 0x02, 0x12, 0x0A, 0x1A, 0x06, 0x16, 0x0E, 0x1E };

	private static final int[] ROW_OFFSETS = new int[] { 0x00, 0x40, 0x14, 0x54 };

	private final I2CDevice i2cDevice;
	private int portA = 0x00;
	private int portB = 0x00;
	private int ddrB = 0x10;
	private int displayShift = LCD_CURSORMOVE | LCD_MOVERIGHT;
	private int displayMode = LCD_ENTRYLEFT | LCD_ENTRYSHIFTDECREMENT;
	private int displayControl = LCD_DISPLAYON | LCD_CURSOROFF | LCD_BLINKOFF;
	private Color color = Color.WHITE;

	public RealLCD() throws IOException {
		// This seems to be the default for AdaFruit 1115.
		this(I2CBus.BUS_1, 0x20);
	}

	public RealLCD(int bus, int address) throws IOException {
		i2cDevice = I2CFactory.getInstance(bus).getDevice(address);
		initialize();
	}

	private void initialize() throws IOException {
		// Set MCP23017 IOCON register to Bank 0 with sequential operation.
		// If chip is already set for Bank 0, this will just write to OLATB,
		// which won't seriously bother anything on the plate right now
		// (blue backlight LED will come on, but that's done in the next
		// step anyway).
		i2cDevice.write(MCP23017_IOCON_BANK1, (byte) 0);

		// Brute force reload ALL registers to known state. This also
		// sets up all the input pins, pull-ups, etc. for the Pi Plate.
		// NOTE(marcus/9 dec 2013): 0x3F assumes that GPA5 is input too -
		// it is however not connected.
		byte[] registers = { 0x3F, // IODIRA R+G LEDs=outputs, buttons=inputs
				(byte) ddrB, // IODIRB LCD D7=input, Blue LED=output
				0x3F, // IPOLA Invert polarity on button inputs
				0x00, // IPOLB
				0x00, // GPINTENA Disable interrupt-on-change
				0x00, // GPINTENB
				0x00, // DEFVALA
				0x00, // DEFVALB
				0x00, // INTCONA
				0x00, // INTCONB
				0x00, // IOCON
				0x00, // IOCON
				0x3F, // GPPUA Enable pull-ups on buttons
				0x00, // GPPUB
				0x00, // INTFA
				0x00, // INTFB
				0x00, // INTCAPA
				0x00, // INTCAPB
				(byte) portA, // GPIOA
				(byte) portB, // GPIOB
				(byte) portA, // OLATA 0 on all outputs; side effect of
				(byte) portB // OLATB turning on R+G+B backlight LEDs.
		};
		i2cDevice.write(0, registers, 0, registers.length);

		// Switch to Bank 1 and disable sequential operation.
		// From this point forward, the register addresses do NOT match
		// the list immediately above. Instead, use the constants defined
		// at the start of the class. Also, the address register will no
		// longer increment automatically after this -- multi-byte
		// operations must be broken down into single-byte calls.
		i2cDevice.write(MCP23017_IOCON_BANK0, (byte) 0xA0);

		write(0x33); // Init
		write(0x32); // Init
		write(0x28); // 2 line 5x8 matrix
		write(LCD_CLEARDISPLAY);
		write(LCD_CURSORSHIFT | displayShift);
		write(LCD_ENTRYMODESET | displayMode);
		write(LCD_DISPLAYCONTROL | displayControl);
		write(LCD_RETURNHOME);
	}

	private void write(int value) throws IOException {
		waitOnLCDBusyFlag();
		int bitmask = portB & 0x01; // Mask out PORTB LCD control bits

		byte[] data = out4(bitmask, value);
		i2cDevice.write(MCP23017_GPIOB, data, 0, 4);
		portB = data[3];

		// If a poll-worthy instruction was issued, reconfigure D7
		// pin as input to indicate need for polling on next call.
		if (value == LCD_CLEARDISPLAY || value == LCD_RETURNHOME) {
			ddrB |= 0x10;
			i2cDevice.write(MCP23017_IODIRB, (byte) ddrB);
		}
	}

	private void waitOnLCDBusyFlag() throws IOException {
		// The speed of LCD accesses is inherently limited by I2C through the
		// port expander. A 'well behaved program' is expected to poll the
		// LCD to know that a prior instruction completed. But the timing of
		// most instructions is a known uniform 37 ms. The enable strobe
		// can't even be twiddled that fast through I2C, so it's a safe bet
		// with these instructions to not waste time polling (which requires
		// several I2C transfers for reconfiguring the port direction).
		// The D7 pin is set as input when a potentially time-consuming
		// instruction has been issued (e.g. screen clear), as well as on
		// startup, and polling will then occur before more commands or data
		// are issued.

		// If pin D7 is in input state, poll LCD busy flag until clear.
		if ((ddrB & 0x10) != 0) {
			int lo = (portB & 0x01) | 0x40;
			int hi = lo | 0x20; // E=1 (strobe)
			i2cDevice.write(MCP23017_GPIOB, (byte) lo);
			while (true) {
				i2cDevice.write((byte) hi); // Strobe high (enable)
				int bits = i2cDevice.read(); // First nybble contains busy state
				i2cDevice.write(MCP23017_GPIOB, new byte[] { (byte) lo,
						(byte) hi, (byte) lo }, 0, 3); // Strobe low, high, low.
														// Second nybble (A3) is
														// ignored.
				if ((bits & 0x02) == 0)
					break; // D7=0, not busy
			}
			portB = lo;
			ddrB &= 0xEF; // Polling complete, change D7 pin to output
			i2cDevice.write(MCP23017_IODIRB, (byte) ddrB);
		}
	}

	private byte[] out4(int bitmask, int value) {
		int hi = bitmask | SHIFT_REVERSE[value >> 4];
		int lo = bitmask | SHIFT_REVERSE[value & 0x0F];

		return new byte[] { (byte) (hi | 0x20), (byte) hi, (byte) (lo | 0x20),
				(byte) lo };
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setText(java.lang.String)
	 */
	@Override
	public void setText(String s) throws IOException {
		String[] str = s.split("\n");
		for (int i = 0; i < str.length; i++) {
			setText(i, str[i]);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setText(int, java.lang.String)
	 */
	@Override
	public void setText(int row, String string) throws IOException {
		setCursorPosition(row, 0);
		internalWrite(string);
	}

	private void internalWrite(String s) throws IOException {
		int sLen = s.length();
		int bytesLen = 4 * sLen;
		if (sLen < 1) {
			return;
		}

		waitOnLCDBusyFlag();
		int bitmask = portB & 0x01; // Mask out PORTB LCD control bits
		bitmask |= 0x80; // Set data bit

		byte[] bytes = new byte[4 * sLen];
		for (int i = 0; i < sLen; i++) {
			byte[] data = out4(bitmask, s.charAt(i));
			for (int j = 0; j < 4; j++) {
				bytes[(i * 4) + j] = data[j];
			}
		}
		i2cDevice.write(MCP23017_GPIOB, bytes, 0, bytesLen);
		portB = bytes[bytesLen - 1];
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setCursorPosition(int, int)
	 */
	@Override
	public void setCursorPosition(int row, int column) throws IOException {
		write(LCD_SETDDRAMADDR | (column + ROW_OFFSETS[row]));
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#stop()
	 */
	@Override
	public void stop() throws IOException {
		portA = 0xC0; // Turn off LEDs on the way out
		portB = 0x01;
		sleep(2);
		i2cDevice.write(MCP23017_IOCON_BANK1, (byte) 0);
		byte[] registers = { 0x3F, // IODIRA
				(byte) ddrB, // IODIRB
				0x0, // IPOLA
				0x0, // IPOLB
				0x0, // GPINTENA
				0x0, // GPINTENB
				0x0, // DEFVALA
				0x0, // DEFVALB
				0x0, // INTCONA
				0x0, // INTCONB
				0x0, // IOCON
				0x0, // IOCON
				0x3F, // GPPUA
				0x0, // GPPUB
				0x0, // INTFA
				0x0, // INTFB
				0x0, // INTCAPA
				0x0, // INTCAPB
				(byte) portA, // GPIOA
				(byte) portB, // GPIOB
				(byte) portA, // OLATA
				(byte) portB // OLATB
		};
		i2cDevice.write(0, registers, 0, registers.length);
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// Don't care...
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#clear()
	 */
	@Override
	public void clear() throws IOException {
		write(LCD_CLEARDISPLAY);
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#home()
	 */
	@Override
	public void home() throws IOException {
		write(LCD_RETURNHOME);
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setCursorEnabled(boolean)
	 */
	@Override
	public void setCursorEnabled(boolean enable) throws IOException {
		if (enable) {
			displayControl |= LCD_CURSORON;
			write(LCD_DISPLAYCONTROL | displayControl);
		} else {
			displayControl &= ~LCD_CURSORON;
			write(LCD_DISPLAYCONTROL | displayControl);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#isCursorEnabled()
	 */
	@Override
	public boolean isCursorEnabled() {
		return (displayControl & LCD_CURSORON) > 0;
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setDisplayEnabled(boolean)
	 */
	@Override
	public void setDisplayEnabled(boolean enable) throws IOException {
		if (enable) {
			displayControl |= LCD_DISPLAYON;
			write(LCD_DISPLAYCONTROL | displayControl);
		} else {
			displayControl &= ~LCD_DISPLAYON;
			write(LCD_DISPLAYCONTROL | displayControl);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#isDisplayEnabled()
	 */
	@Override
	public boolean isDisplayEnabled() {
		return (displayControl & LCD_DISPLAYON) > 0;
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setBlinkEnabled(boolean)
	 */
	@Override
	public void setBlinkEnabled(boolean enable) throws IOException {
		if (enable) {
			displayControl |= LCD_BLINKON;
			write(LCD_DISPLAYCONTROL | displayControl);
		} else {
			displayControl &= ~LCD_BLINKON;
			write(LCD_DISPLAYCONTROL | displayControl);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#isBlinkEnabled()
	 */
	@Override
	public boolean isBlinkEnabled() {
		return (displayControl & LCD_BLINKON) > 0;
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setBacklight(se.hirt.pi.adafruitlcd.LCD.Color)
	 */
	@Override
	public void setBacklight(Color color) throws IOException {
		int c = ~color.getValue();
		portA = (portA & 0x3F) | ((c & 0x03) << 6);
		portB = (portB & 0xFE) | ((c & 0x04) >> 2);
		// Has to be done as two writes because sequential operation is off.
		i2cDevice.write(MCP23017_GPIOA, (byte) portA);
		i2cDevice.write(MCP23017_GPIOB, (byte) portB);
		this.color = color;
 	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#scrollDisplay(se.hirt.pi.adafruitlcd.LCD.Direction)
	 */
	@Override
	public void scrollDisplay(Direction direction) throws IOException {
		if (direction == Direction.LEFT) {
			displayShift = LCD_DISPLAYMOVE | LCD_MOVELEFT;
			write(LCD_CURSORSHIFT | displayShift);
		} else {
			displayShift = LCD_DISPLAYMOVE | LCD_MOVERIGHT;
			write(LCD_CURSORSHIFT | displayShift);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setTextFlowDirection(se.hirt.pi.adafruitlcd.LCD.Direction)
	 */
	@Override
	public void setTextFlowDirection(Direction direction) throws IOException {
		if (direction == Direction.LEFT) {
			// This is for text that flows right to left
			displayMode &= ~LCD_ENTRYLEFT;
			write(LCD_ENTRYMODESET | displayMode);
		} else {
			// This is for text that flows left to right
			displayMode |= LCD_ENTRYLEFT;
			write(LCD_ENTRYMODESET | displayMode);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#setAutoScrollEnabled(boolean)
	 */
	@Override
	public void setAutoScrollEnabled(boolean enable) throws IOException {
		if (enable) {
			// This will 'right justify' text from the cursor
			displayMode |= LCD_ENTRYSHIFTINCREMENT;
			write(LCD_ENTRYMODESET | displayMode);
		} else {
			// This will 'left justify' text from the cursor
			displayMode &= ~LCD_ENTRYSHIFTINCREMENT;
			write(LCD_ENTRYMODESET | displayMode);
		}
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#isAutoScrollEnabled()
	 */
	@Override
	public boolean isAutoScrollEnabled() {
		return (displayControl & LCD_ENTRYSHIFTINCREMENT) > 0;
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#isButtonPressed(se.hirt.pi.adafruitlcd.Button)
	 */
	@Override
	public boolean isButtonPressed(Button button) throws IOException {
		return ((i2cDevice.read(MCP23017_GPIOA) >> button.getPin()) & 1) > 0;
	}

	/* (non-Javadoc)
	 * @see se.hirt.pi.adafruitlcd.ILCD#buttonsPressedBitmask()
	 */
	@Override
	public int buttonsPressedBitmask() throws IOException {
		return i2cDevice.read(MCP23017_GPIOA) & 0x1F;
	}

	@Override
	public Color getBacklight() throws IOException {
		// Should probably read the registers instead of caching...
		return color;
	}

}
