
package org.onebeartoe.electronics.photorama;

import com.pi4j.io.gpio.GpioController;
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
import static org.onebeartoe.electronics.photorama.PhotoBoothServlet.PHOTO_BOOTH_GPIO_CONTROLLER_KEY;

/**
 * @author Roberto Marquez
 */
@WebServlet(urlPatterns = {"/foot-pedal"})
public class FootPedalServlet extends HttpServlet implements GpioPinListenerDigital
{
    private final String PHOTO_BOOTH_FOOT_PEDAL_KEY = "PHOTO_BOOTH_FOOT_PEDAL_KEY";
    
    private volatile boolean takingSnapshot;
 
    @Override
    public void init() throws ServletException
    {
        super.init();
        
        takingSnapshot = false;
        
        ServletContext servletContext = getServletContext();
        GpioController gpio = (GpioController) servletContext.getAttribute(PHOTO_BOOTH_GPIO_CONTROLLER_KEY);
        if(gpio == null)
        {
            System.err.println("The foot pedal servlet could not obtain a GPIO contorller from the servlet context");
        }
        else
        {            
            GpioPinDigitalInput photoBoothButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, 
                                                                                 "foot pedal button", 
                                                                                 PinPullResistance.PULL_UP);
            photoBoothButton.addListener(this);
            servletContext.setAttribute(PHOTO_BOOTH_FOOT_PEDAL_KEY, photoBoothButton);
        }
    }
    
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
    {
        PinState state = event.getState();
        if(state == PinState.HIGH)
        {
            if(takingSnapshot)
            {
                String message = "The foot pedal was pressed, while already taking a photo.";
                System.out.println(message);
            }
            else
            {
                ServletContext servletContext = getServletContext();
                Camera camera = (Camera) servletContext.getAttribute(CAMERA_KEY);
                if(camera == null)
                {
                    System.err.println("The foot pedal servlet did not find the camera.");
                }
                else
                {
                    takingSnapshot = true;
                    camera.takeSnapshot();
                    takingSnapshot = false;                    
                }
            }
        }
    }    
}
