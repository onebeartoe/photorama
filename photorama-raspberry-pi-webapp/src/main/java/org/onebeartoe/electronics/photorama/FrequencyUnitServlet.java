
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
@WebServlet(urlPatterns = {"/frequency/unit/*"})
public class FrequencyUnitServlet extends HttpServlet
{
    private Logger logger;

//TODO: GET RID OF THIS for production, only used for debugging
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {        
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        
        // remove the leading forward slash
        String pi = request.getPathInfo();
        String fequencyUnit = pi.substring(1);
        String result = updateFrequencyUnit(camera, fequencyUnit);
        
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
    
    private String updateFrequencyUnit(Camera camera, String unitName)
    {
        String result = "frequency unit ";
        try
        {
            unitName = unitName.toUpperCase();
            FrequencyUnits unit = FrequencyUnits.valueOf(unitName);
            long delay = camera.getTimelapse();
            camera.setTimelapse(delay, unit);
            result += "updated to " + unitName;
        }
        catch(Exception e)
        {
            String message = e.getMessage();
            result = "could not update " + result + " to " + unitName;
            logger.log(Level.SEVERE, message, e);
        }    
        
        return result;
    }

}
