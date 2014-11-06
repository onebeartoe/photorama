
package org.onebeartoe.electronics.photorama;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * @author Roberto Marquez
 */
public class PhotoBoothButtonTester implements GpioPinListenerDigital
{
        // On the Raspberry Pi model B, revision 2, this pin is labeled GPIO27
    public static final Pin buttonPin = RaspiPin.GPIO_02;
    
    public PhotoBoothButtonTester()
    {
        System.out.println("Provisioning GPIO.");
    
        GpioController gpio = GpioFactory.getInstance();
             
        
        GpioPinDigitalInput photoBoothButton = gpio.provisionDigitalInputPin(buttonPin, 
                                                                             "photo booth button", 
                                                                             PinPullResistance.PULL_UP);   // tried with the Adafruit button
// worked for the tacktile/simple push button                                PinPullResistance.PULL_DOWN);
        System.out.println("GPIO provisioned!");
        photoBoothButton.addListener(this);
    }
    
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
    {
        String s;
        PinState state = event.getState();
        if(state == PinState.LOW)
        {
            s = "low: ";
        }
        else
        {
            s = "high: ";
        }
           
        System.out.println("Photo booth button pin state changed to " + s + state + ".");
    }    
    
    public static void main(String[] args) throws Exception
    {
        PhotoBoothButtonTester app = new PhotoBoothButtonTester();

        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}
