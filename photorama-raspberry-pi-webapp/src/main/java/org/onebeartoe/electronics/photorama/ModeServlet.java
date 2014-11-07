
package org.onebeartoe.electronics.photorama;

import java.io.File;
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
    private final String PI_HOME = "/home/pi/photorama/viewer-webapp/";
//    private final String PI_HOME = "/home/pi/";
        
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
    
    private String buildOutpath(PhotoramaModes mode)
    {
        String subpath;
        switch(mode)
        {
            case FOOT_PEDAL:
            {
                subpath = "foot-pedal/";
                break;
            }
            case PHOTO_BOOTH:
            {
                subpath = "photo-booth/";
                break;
            }
            case TIME_LAPSE:
            {
                subpath = "time-lapse/";
                break;
            }
            default:
            {
                // off
                subpath = "";
            }
        }
        
//        add a foleer counter
        
        String outputPath = PI_HOME + subpath;
        
        // make the directory if it does not exist
        File f = new File(outputPath);
        if( !f.exists() )
        {
            f.mkdirs();
        }
        
        return outputPath;
    }
    
    private String updateMode(PhotoramaModes mode)
    {        
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        camera.setMode(mode);
        String outpath = buildOutpath(mode);
        camera.setOutputPath(outpath);
        
        String result = "mode changed to: " + mode;
        
        return result;
    }
}
