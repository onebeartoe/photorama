
package org.onebeartoe.electronics.photorama;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import static org.onebeartoe.electronics.photorama.ConfigurationServlet.CAMERA_KEY;
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
    
    private final long SNAPSHOT_DELAY = 3000;
    
    private void delayedSnapshots(int count)
    {
        ServletContext servletContext = getServletContext();
        Camera camera = (Camera) servletContext.getAttribute(CAMERA_KEY);
        
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
        
        takingSnapshots = false;
        
        ServletContext servletContext = getServletContext();
        GpioController gpio = (GpioController) servletContext.getAttribute(PHOTO_BOOTH_GPIO_CONTROLLER_KEY);
        if(gpio == null)
        {
            gpio = GpioFactory.getInstance();
            servletContext.setAttribute(PHOTO_BOOTH_GPIO_CONTROLLER_KEY, gpio);
            
            GpioPinDigitalInput photoBoothButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_18, 
                                                                                 "photo booth button", 
                                                                                 PinPullResistance.PULL_UP);
            photoBoothButton.addListener(this);
            servletContext.setAttribute(PHOTO_BOOTH_BUTTON_KEY, photoBoothButton);
        } 
    }
    
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
    {
        PinState state = event.getState();
        if(state == PinState.HIGH)
        {
            if(takingSnapshots)
            {
                String message = "The photobooth button was pressed, while already taking photos.";
                System.out.println(message);
            }
            else
            {
                takingSnapshots = true;
                delayedSnapshots(3);
                takingSnapshots = false;
            }
        }
    }    
}
