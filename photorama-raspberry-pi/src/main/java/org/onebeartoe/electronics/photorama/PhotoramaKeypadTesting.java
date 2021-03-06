
package org.onebeartoe.electronics.photorama;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This only tries to get the buttons and pins mapped.
 * I had trouble with the Adafruit LCD with keypad buttons, so I removed references 
 * to the LCD.
 * @author Roberto Marquez
 */
public class PhotoramaKeypadTesting
{
    static final int BUS_NO = 1;  // 0 for Raspberry Pi B version 1 and 1 for version 2
    static final int BUS_ADDRESS = 0x20;
    
    private Logger logger;

    private Camera camera;

    private static GpioController gpio;
    
    public PhotoramaKeypadTesting() 
    {
        logger = Logger.getLogger( getClass().getName() );
        
        try 
        {
            camera = new RaspberryPiCamera();

            
            
            Thread.sleep(1000);

            gpio = GpioFactory.getInstance();

            configureKeypadPins(gpio, camera);
        } 
        catch (Exception ex) 
        {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void configureKeypadPins(GpioController gpio, final Camera camera)
    {
        
        // button numbering SELECT = 0 
        // RIGHT = 1 
        // DOWN = 2 
        // UP = 3 
        // LEFT = 4
        // provision gpio pin #00 as an input pin with its internal pull down resistor enabled
        // (configure pin edge to both rising and falling to get notified for HIGH and LOW state
        // changes)
        GpioPinDigitalInput selectButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00,             // PIN NUMBER
                                                                       "Select Button",                   // PIN FRIENDLY NAME (optional)
                                                                       PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
        selectButton.addListener(new GpioPinListenerDigital()
        {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) 
            {
                System.out.println("select");
                

                camera.takeSnapshot();
            }
        });
        selectButton.addTrigger( new GpioCallbackTrigger( new Callable<Void>() 
        {
            public Void call() throws Exception 
            {
                System.out.println("hey hey select");
                
//                camera.takeSnapshot();     

                return null;
            }
        }));

        GpioPinDigitalInput rightButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01,             // PIN NUMBER
                                                                       "Right Button",                   // PIN FRIENDLY NAME (optional)
                                                                       PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
        rightButton.addListener(new GpioPinListenerDigital()
        {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) 
            {
                System.out.println("right");
                
            }
        });

/*            
        GpioPinDigitalInput downButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,             // PIN NUMBER
                                                                       "Down Button",                   // PIN FRIENDLY NAME (optional)
                                                                       PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
        downButton.addListener(new GpioPinListenerDigital()
        {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) 
            {
                System.out.println("down");

                lcd.clear();
                lcd.write("Down");
            }
        });

        GpioPinDigitalInput upButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03,             // PIN NUMBER
                                                                       "Up Button",                   // PIN FRIENDLY NAME (optional)
                                                                       PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
        upButton.addListener(new GpioPinListenerDigital() 
        {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) 
            {
                System.out.println("up");

                lcd.clear();
                lcd.write("Up");
            }
        });
*/

        GpioPinDigitalInput leftButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,             // PIN NUMBER
                                                                       "Left Button",                   // PIN FRIENDLY NAME (optional)
                                                                       PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
        leftButton.addListener(new GpioPinListenerDigital() 
        {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) 
            {
                System.out.println("left");

                
            }
        });        
    }
    
    public static void main(String[] args) throws Exception
    {
        PhotoramaKeypadTesting app = new PhotoramaKeypadTesting();
        
        
        Thread.sleep(15000);

        shutdown();
        
        System.out.println("end of run");
    }

    private static void shutdown() 
    {
        gpio.shutdown();
    }
}
