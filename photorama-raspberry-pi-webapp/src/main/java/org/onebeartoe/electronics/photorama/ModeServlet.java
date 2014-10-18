
package org.onebeartoe.electronics.photorama;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onebeartoe.application.Enums;
import static org.onebeartoe.electronics.photorama.ConfigurationServlet.CAMERA_KEY;
import org.onebeartoe.web.PlainTextResponseServlet;

/**
 * @author Roberto Marquez
 */
@WebServlet(urlPatterns = {"/mode/*"})
public class ModeServlet extends PlainTextResponseServlet//HttpServlet
{
    @Override
    public String buildText(HttpServletRequest request, HttpServletResponse response)
    {
        // remove the leading forward slash
        String pi = request.getPathInfo();
        String name = pi.substring(1);
        name = Enums.stringToEnumName(name);
        PhotoramaModes mode = PhotoramaModes.valueOf(name);
        String result = updateMode(mode);
        
        return result;
    }
    
    private String updateMode(PhotoramaModes mode)
    {        
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        camera.setMode(mode);
                
        String result = "mode changed to: " + mode;
        
        return result;
    }
}
