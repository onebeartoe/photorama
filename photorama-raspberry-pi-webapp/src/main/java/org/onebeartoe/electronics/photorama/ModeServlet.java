
package org.onebeartoe.electronics.photorama;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
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
    private static final String PI_HOME = "/home/pi/photorama-viewer-webapp/instances/current/";
        
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
    
    static String buildOutpath(PhotoramaModes mode)
    {
        String subpath = modePath(mode);        
        
        String outputPath = PI_HOME + subpath;
        
        // make the directory for each mode, if it does not exist
        File modeDirectory = new File(outputPath);
        if( !modeDirectory.exists() )
        {
            modeDirectory.mkdirs();
        }
        
        File[] files = modeDirectory.listFiles( new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isDirectory();
            }
        });
        if(files == null)
        {
            System.out.println("Could not list directories, is this the first time the app is run?");
        }
        else
        {
            File latestDirectory;
            if(files.length == 0)
            {
                latestDirectory = new File(modeDirectory, "000");
                latestDirectory.mkdirs();
            }
            else
            {
                // find the latest sub folder
                latestDirectory = files[0];
                for(File dir : files)
                {
                    if(dir.lastModified() > latestDirectory.lastModified())
                    {
                        latestDirectory = dir;
                    }
                }                
            }
            
//            String currentSubParent = latestDirectory.getName();  // this should be something like 001, 003, or 012
//            File currentParent = new File(modeDirectory, currentSubParent);
            String[] imageList = latestDirectory.list( new FilenameFilter()
            {
                @Override
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".jpg");
                }
            });
            
            outputPath = latestDirectory.getAbsolutePath();
            
            // check if the directory has reached the limit
            int imageLimit = 21;
            if(imageList != null && imageList.length > imageLimit)
            {
                try
                {
                    String currentSubParent = latestDirectory.getName();
                    Integer c = Integer.valueOf(currentSubParent);
                    c++; // up the directory number by one
                    
                    String name = String.valueOf(c);
                    if(c < 10)
                    {
                        name = "00" + name;
                    }
                    else if(c < 100)
                    {
                        name = "0" + name;
                    }
                    
                    File nextSubParent = new File(modeDirectory, name);                    
                    if( nextSubParent.mkdirs() )
                    {
                        outputPath = nextSubParent.getAbsolutePath();
                    }                    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }                
            }
        }
                       
        return outputPath;
    }
    
    static String modePath(PhotoramaModes mode)
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
        
        return subpath;
    }
    
    private String updateMode(PhotoramaModes mode)
    {        
        ServletContext context = getServletContext();
        Camera camera = (Camera) context.getAttribute(CAMERA_KEY);
        camera.setMode(mode);

// Are we sure this is good?        
//        String outpath = buildOutpath(mode);
//        camera.setOutputPath(outpath);
        
        String result = "mode changed to: " + mode;
        
        return result;
    }
}
