
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
@WebServlet(urlPatterns = {"/frequency/*"})
public class FrequencyServlet extends HttpServlet
{
    private Logger logger;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {        
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        
        // remove the leading forward slash
        String pi = request.getPathInfo();
        String fequencyUnit = pi.substring(1);
        String result = updateFrequency(camera, fequencyUnit);
        
        OutputStream os = response.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.println(result);
        pw.flush();
        pw.close();
    }
    
    @Override
    public void init() throws ServletException 
    {
        super.init();
        
        logger = Logger.getLogger(getClass().getName());
    }
    
    private String updateFrequency(Camera camera, String frequency)
    {
        String result = "frequency ";
        try
        {
            Long d = Long.valueOf(frequency);
            long delay = d.longValue();
            camera.setTimelapse(delay, FrequencyUnits.SECONDS);
            
            result += "updated to " + delay;
        }
        catch(Exception e)
        {
            String message = e.getMessage();            
            logger.log(Level.SEVERE, message, e);
            
            result = "could not update " + result + "to " + frequency;
        }    
        
        return result;
    }    
}
