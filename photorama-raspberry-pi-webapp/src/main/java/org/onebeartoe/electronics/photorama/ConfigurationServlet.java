
package org.onebeartoe.electronics.photorama;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Roberto Marquez
 * 
 * An instance of this servlet is created on startup to ensure a Camera object 
 * is always an attribute in the ServletContext for the application.
 * 
 */
@WebServlet(urlPatterns = {"/configuration"}, loadOnStartup = 1)
public class ConfigurationServlet extends HttpServlet
{
    private Logger logger;
    
    public static final String CAMERA_KEY = "camera";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        
        request.setAttribute(CAMERA_KEY, camera);
                        
        RequestDispatcher rd = context.getRequestDispatcher("/configuration.jsp");
        rd.forward(request, response);
    }
    
    @Override
    public void init() throws ServletException
    {
        super.init();
        
        logger = Logger.getLogger(getClass().getName());
        
        ServletContext servletContext = getServletContext();
        Camera camera = (Camera) servletContext.getAttribute(CAMERA_KEY);
        if(camera == null)
        {
            camera = new RaspberryPiCamera();
            camera.setMode(PhotoramaModes.OFF);
            
            // The mode was updated for the Spurs party!
            camera.setMode(PhotoramaModes.PHOTO_BOOTH);
            
            servletContext.setAttribute(CAMERA_KEY, camera);
        }        
    }
}

