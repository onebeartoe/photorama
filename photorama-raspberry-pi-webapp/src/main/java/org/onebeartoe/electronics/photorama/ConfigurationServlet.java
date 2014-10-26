
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
 * @author Roberto Marquez
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
            
            servletContext.setAttribute(CAMERA_KEY, camera);
        }        
    }
}

