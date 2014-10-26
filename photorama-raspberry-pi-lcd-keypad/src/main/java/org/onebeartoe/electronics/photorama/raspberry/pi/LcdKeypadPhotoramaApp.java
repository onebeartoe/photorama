package org.onebeartoe.electronics.photorama.raspberry.pi;

//import com.oracle.raspberry.pi.lcd.AdafruitLcdPlate;
//import com.oracle.raspberry.pi.lcd.Lcd;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.RaspberryPiCamera;
import se.hirt.pi.adafruitlcd.Button;
import static se.hirt.pi.adafruitlcd.Button.DOWN;
import static se.hirt.pi.adafruitlcd.Button.RIGHT;
import static se.hirt.pi.adafruitlcd.Button.SELECT;
import static se.hirt.pi.adafruitlcd.Button.UP;
import se.hirt.pi.adafruitlcd.ButtonListener;
import se.hirt.pi.adafruitlcd.ButtonPressedObserver;
import se.hirt.pi.adafruitlcd.Color;
import se.hirt.pi.adafruitlcd.ILCD;
import se.hirt.pi.adafruitlcd.demo.AutoScrollDemo;
import se.hirt.pi.adafruitlcd.demo.ColorDemo;
import se.hirt.pi.adafruitlcd.demo.CursorDemo;
import se.hirt.pi.adafruitlcd.demo.DisplayDemo;
import se.hirt.pi.adafruitlcd.demo.ExitTest;
import se.hirt.pi.adafruitlcd.demo.HelloWorldTest;
import se.hirt.pi.adafruitlcd.demo.LCDTest;
import se.hirt.pi.adafruitlcd.demo.ScrollTest;
import se.hirt.pi.adafruitlcd.impl.RealLCD;
import se.hirt.pi.adafruitlcd.impl.RealLCD.Direction;

/**
 * @author Roberto Marquez
 */
public class LcdKeypadPhotoramaApp
{

    static final int BUS_NO = 1;  // 0 for Raspberry Pi B version 1 and 1 for version 2
    static final int BUS_ADDRESS = 0x20;

    private Logger logger;

    private Camera camera;

    private ILCD lcd;

    private static int currentTest = -1;

    private final static LCDTest[] TESTS =
    {
        new HelloWorldTest(),
        new ScrollTest(), new CursorDemo(), new DisplayDemo(),
        new ColorDemo(), new AutoScrollDemo(), new ExitTest()
    };

    public LcdKeypadPhotoramaApp()
    {
        logger = Logger.getLogger(LcdKeypadPhotoramaApp.class.getName());

        try
        {
            camera = new RaspberryPiCamera();

            lcd = new RealLCD();
            lcd.setBacklight(Color.ON);

            lcd.setText("LCD Test!\nPress up/down...");

            ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
            observer.addButtonListener(new ButtonListener()
            {
                @Override
                public void onButtonPressed(Button button)
                {
                    try
                    {
                        switch (button)
                        {
                            case UP:
                                currentTest = --currentTest < 0 ? 0 : currentTest;
                                lcd.clear();
                                lcd.setText(String.format("#%d:%s\nPress Sel to run!",
                                        currentTest, TESTS[currentTest].getName()));
                                break;
                            case DOWN:
                                currentTest = ++currentTest > (TESTS.length - 1) ? TESTS.length - 1
                                        : currentTest;
                                lcd.clear();
                                lcd.setText(String.format("#%d:%s\nPress Sel to run!",
                                        currentTest, TESTS[currentTest].getName()));
                                break;
                            case RIGHT:
                                lcd.scrollDisplay(Direction.LEFT);
                                break;
                            case LEFT:
                                lcd.scrollDisplay(Direction.RIGHT);
                                break;
                            case SELECT:
                                runTest(currentTest);
                                break;
                            default:
                                lcd.clear();
                                lcd.setText(String.format(
                                        "Button %s\nis not in use...",
                                        button.toString()));
                        }
                    }
                    catch (IOException e)
                    {
                        handleException(e);
                    }
                }

                private void runTest(int currentTest)
                {
                    LCDTest test = TESTS[currentTest];
                    System.out.println("Running test " + test.getName());
                    try
                    {
                        test.run(lcd);
                    }
                    catch (IOException e)
                    {
                        handleException(e);
                    }
                }
            });

            System.out.println("Press enter to quit!");
            System.in.read();
            lcd.stop();
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private static void handleException(IOException e)
    {
        System.out.println("Problem talking to LCD! Exiting!");
        e.printStackTrace();
        System.exit(2);
    }

    public static void main(String[] args) throws Exception
    {
        LcdKeypadPhotoramaApp app = new LcdKeypadPhotoramaApp();

        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}
