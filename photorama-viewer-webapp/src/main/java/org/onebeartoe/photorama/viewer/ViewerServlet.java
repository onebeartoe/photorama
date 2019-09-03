
package org.onebeartoe.photorama.viewer;

import java.io.File;
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
@WebServlet(urlPatterns = {"/view/*"})
public class ViewerServlet extends HttpServlet
{
    public static final String MODEL_KEY = "model";

    private File captureDirectory;
    
    private Logger logger;
    
    private final ViewerService viewerService = new ViewerService();
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String subpath = request.getPathInfo();
        
        DirectoryView model = viewerService.loadDirectoryView(captureDirectory, subpath);
        
        request.setAttribute(MODEL_KEY, model);
        
        ServletContext context = getServletContext();                
        
        RequestDispatcher rd = context.getRequestDispatcher("/view.jsp");
        
        rd.forward(request, response);
    }
    
    @Override
    public void init() throws ServletException 
    {
        super.init();
        
        logger = Logger.getLogger(getClass().getName());
        
        ServletContext servletContext = getServletContext();
        
        String filesystemPath = servletContext.getRealPath("/index.jsp");        
        File f = new File(filesystemPath);
        captureDirectory = f.getParentFile();
    }
}
