
package org.onebeartoe.electronics.photorama;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import static org.onebeartoe.electronics.photorama.ConfigurationServlet.CAMERA_KEY;
import static org.onebeartoe.electronics.photorama.PhotoBoothButtonTester.buttonPin;
import org.onebeartoe.system.Sleeper;

/**
 * This class is responsible for the photo booth features of Photorama.
 * It also is responsible for creating and shutting down the GPIO controller.
 * @author Roberto Marquez
 */
@WebServlet(urlPatterns = {"/photo-booth"}, loadOnStartup = 1)
public class PhotoBoothServlet extends HttpServlet implements GpioPinListenerDigital
{
    private final String PHOTO_BOOTH_BUTTON_KEY = "PHOTO_BOOTH_BUTTON_KEY";
    
    public static final String PHOTO_BOOTH_GPIO_CONTROLLER_KEY = "PHOTO_BOOTH_GPIO_CONTROLLER_KEY";
    
    private volatile boolean takingSnapshots;
    
    // set the delay to 9.5 seconds between snapshots
    private final long SNAPSHOT_DELAY = 9500;
    
    // On the Raspberry Pi model B, revision 2, this pin is labeled GPIO27
//    public static final Pin buttonPin = RaspiPin.GPIO_02;
    
    Logger logger;
    
    private void delayedSnapshots(int count, Camera camera) throws Exception
    {        
        for(int c=0; c<count; c++)
        {
            Sleeper.sleepo(SNAPSHOT_DELAY);
            camera.takeSnapshot();
        }
    }
    
    @Override
    public void destroy()
    {
        ServletContext servletContext = getServletContext();
        GpioController gpio = (GpioController) servletContext.getAttribute(PHOTO_BOOTH_GPIO_CONTROLLER_KEY);
        gpio.shutdown();
    }
    
    @Override
    public void init() throws ServletException
    {
        super.init();
        
        logger = Logger.getLogger(getClass().getName());
        
        takingSnapshots = false;
        
        ServletContext servletContext = getServletContext();
        GpioController gpio = (GpioController) servletContext.getAttribute(PHOTO_BOOTH_GPIO_CONTROLLER_KEY);
        if(gpio == null)
        {
            System.out.println("Provisioning GPIO.");
            gpio = GpioFactory.getInstance();
            servletContext.setAttribute(PHOTO_BOOTH_GPIO_CONTROLLER_KEY, gpio);
            
        GpioPinDigitalInput photoBoothButton = gpio.provisionDigitalInputPin(buttonPin, 
                                                                             "photo booth button", 
                                                                             PinPullResistance.PULL_UP);   // tried with the Adafruit button
// worked for the tacktile/simple push button                                PinPullResistance.PULL_DOWN);
            System.out.println("GPIO provisioned :)");
            photoBoothButton.addListener(this);
            servletContext.setAttribute(PHOTO_BOOTH_BUTTON_KEY, photoBoothButton);
        } 
    }
    
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
    {
        PinState state = event.getState();
        if(state == PinState.LOW)
        {
            if(takingSnapshots)
            {
                String message = "The photobooth button was pressed, while already taking photos.";
                System.out.println(message);
            }
            else
            {
                ServletContext servletContext = getServletContext();
                Camera camera = (Camera) servletContext.getAttribute(CAMERA_KEY);
                
                if(camera.getMode() != PhotoramaModes.PHOTO_BOOTH)
                {
                    String message = "The photo booth button was pressed, but Photorama is not in photo booth mode.";
                    logger.log(Level.WARNING, message);
                }
                else
                {
                    System.out.println("Photo booth taking snapshots.");
                    takingSnapshots = true;
                    try
                    {
                        delayedSnapshots(3, camera);
                    } 
                    catch (Exception ex)
                    {
                        String message = "An exception was thrown while taking delayed snapshots.";
                        logger.log(Level.SEVERE, message, ex);
                    }
                    takingSnapshots = false;                    
                }
            }
        }
        else
        {
            System.out.println("Photo booth button pin state changed to HIGH.");
        }
    }    
}
