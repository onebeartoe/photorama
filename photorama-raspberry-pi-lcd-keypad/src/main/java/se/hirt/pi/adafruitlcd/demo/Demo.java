/* *  Copyright (C) 2013 Marcus Hirt *                     www.hirt.se * * This software is free: * * Redistribution and use in source and binary forms, with or without * modification, are permitted provided that the following conditions * are met: * * 1. Redistributions of source code must retain the above copyright *    notice, this list of conditions and the following disclaimer. * 2. Redistributions in binary form must reproduce the above copyright *    notice, this list of conditions and the following disclaimer in the *    documentation and/or other materials provided with the distribution. * 3. The name of the author may not be used to endorse or promote products *    derived from this software without specific prior written permission. * * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. * * Copyright (C) Marcus Hirt, 2013 */package se.hirt.pi.adafruitlcd.demo;import java.io.IOException;import se.hirt.pi.adafruitlcd.Button;import se.hirt.pi.adafruitlcd.ButtonListener;import se.hirt.pi.adafruitlcd.ButtonPressedObserver;import se.hirt.pi.adafruitlcd.ILCD;import se.hirt.pi.adafruitlcd.impl.RealLCD;import se.hirt.pi.adafruitlcd.impl.RealLCD.Direction;/** * Here is a demonstration of things that can be done with the LCD shield. Run * this as a stand alone java program. Do not forget to have sudo rights. *  * @author Marcus Hirt */public class Demo {	private final static LCDTest[] TESTS = { new HelloWorldTest(),			new ScrollTest(), new CursorDemo(), new DisplayDemo(),			new ColorDemo(), new AutoScrollDemo(), new ExitTest() };	private static int currentTest = -1;	public static void main(String[] args) throws IOException,			InterruptedException {		final ILCD lcd = new RealLCD();		lcd.setText("LCD Test!\nPress up/down...");		ButtonPressedObserver observer = new ButtonPressedObserver(lcd);		observer.addButtonListener(new ButtonListener() {			@Override			public void onButtonPressed(Button button) {				try {					switch (button) {					case UP:						currentTest = --currentTest < 0 ? 0 : currentTest;						lcd.clear();						lcd.setText(String.format("#%d:%s\nPress Sel to run!",								currentTest, TESTS[currentTest].getName()));						break;					case DOWN:						currentTest = ++currentTest > (TESTS.length - 1) ? TESTS.length - 1								: currentTest;						lcd.clear();						lcd.setText(String.format("#%d:%s\nPress Sel to run!",								currentTest, TESTS[currentTest].getName()));						break;					case RIGHT:						lcd.scrollDisplay(Direction.LEFT);						break;					case LEFT:						lcd.scrollDisplay(Direction.RIGHT);						break;					case SELECT:						runTest(currentTest);						break;					default:						lcd.clear();						lcd.setText(String.format(								"Button %s\nis not in use...",								button.toString()));					}				} catch (IOException e) {					handleException(e);				}			}			private void runTest(int currentTest) {				LCDTest test = TESTS[currentTest];				System.out.println("Running test " + test.getName());				try {					test.run(lcd);				} catch (IOException e) {					handleException(e);				}			}		});		System.out.println("Press enter to quit!");		System.in.read();		lcd.stop();	}	private static void handleException(IOException e) {		System.out.println("Problem talking to LCD! Exiting!");		e.printStackTrace();		System.exit(2);	}}