
package org.onebeartoe.electronics.photorama;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.onebeartoe.electronics.photorama.ConfigurationServlet.CAMERA_KEY;

/**
 * @author Roberto Marquez
 */
@WebServlet(urlPatterns = {"/time-lapse/*"})
public class TimeLapseServlet extends HttpServlet
{
    private Logger logger;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String m = "context path: " + request.getContextPath() + "\n" + 
                   "querystring: " + request.getQueryString() + "\n" +
                   "servlet path: " + request.getServletPath() + "\n" + 
                   "path info: " + request.getPathInfo() + "\n" + 
                   "path translated: " + request.getPathTranslated() + "\n" + 
                   "request uri : " + request.getRequestURI() + "\n" + 
                   "request url : " + request.getRequestURL();
        
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        
        // remove the leading forward slash
        String state = request.getPathInfo().substring(1);
        state = updateCameraState(camera, state);
        
        OutputStream os = response.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.println(state);
        pw.flush();
        pw.close();
    }    
    
    @Override
    public void init() throws ServletException 
    {
        super.init();
        
        logger = Logger.getLogger(getClass().getName());
    }    
    
    private String updateCameraState(Camera camera, String state)
    {
        String result = "state set to " + state;
        try
        {
            switch(state)
            {
                case "on":
                {
                    camera.startTimelapse();
                    break;
                }
                case "off":
                {
                    camera.stopTimelapse();
                    break;
                }
                default:
                {
                    state = "unknown state: " + state;
                }
            }
        }
        catch(Exception e)
        {
            result = "failed on " + result + ": " + e.getMessage();
            
            logger.log(Level.SEVERE, result, e);
        }    
        
        return result;
    }
    
}
