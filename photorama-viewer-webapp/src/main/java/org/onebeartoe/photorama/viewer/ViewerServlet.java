
package org.onebeartoe.photorama.viewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onebeartoe.filesystem.FileSystemSearcher;
import org.onebeartoe.filesystem.FileType;

/**
 * @author Roberto Marquez
 */
@WebServlet(urlPatterns = {"/view/*"})
public class ViewerServlet extends HttpServlet
{
    public static final String MODEL_KEY = "model";

    private File captureDirectory;
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String subpath = request.getPathInfo();        
        DirectoryView model = loadDirectoryView(subpath);
        
        request.setAttribute(MODEL_KEY, model);
        
        ServletContext context = getServletContext();                
        RequestDispatcher rd = context.getRequestDispatcher("/view.jsp");
        rd.forward(request, response);
    }
    
    private List<String> fileNames(List<File> files)
    {
        List<String> names = new ArrayList();
        for(File d : files)
        {
            String name = d.getName();
            names.add(name);
        }
        
        return names;
    }
    
    private List<File> findFiles(FileType fileType, String subpath)
    {
        boolean recursive = false;
        List<FileType> targets = new ArrayList();
        targets.add(fileType);
        File directory;
        if(subpath == null)
        {
            directory = captureDirectory;
        }
        else
        {
            directory = new File(captureDirectory, subpath);
        }
        FileSystemSearcher searcher = new FileSystemSearcher(directory, targets, recursive);
        List<File> files = searcher.findTargetFiles();
        
        return files;
    }

    @Override
    public void init() throws ServletException 
    {
        super.init();

        ServletContext servletContext = getServletContext();
//        Camera camera = (Camera) servletContext.getAttribute(CAMERA_KEY);
//        if(camera == null)
//        {
//            camera = new RaspberryPiCamera();
//            servletContext.setAttribute(CAMERA_KEY, camera);
//        } 
        
        String filesystemPath = servletContext.getRealPath("/index.jsp");        
        File f = new File(filesystemPath);
        captureDirectory = f.getParentFile();
    }
    
    private List<String> loadDirectories(String subpath)
    {
        List<File> directories = findFiles(FileType.DIRECTORY, subpath);
        List<String> names = fileNames(directories);
        
        names.remove("META-INF");
        names.remove("WEB-INF");
        
        return names;
    }
    
    private List<String> loadImages(String subpath)
    {
        List<File> images = findFiles(FileType.IMAGE, subpath);
        List<String> names = fileNames(images);
        
        return names;
    }
    
    private DirectoryView loadDirectoryView(String subpath)
    {
        DirectoryView model = new DirectoryView();
        boolean validPath = validatePath(subpath);
        
        if(validPath)
        {
            model.path = subpath;
            model.directories = loadDirectories(subpath);
            model.images = loadImages(subpath);
        }
        else
        {
            model.path = "Invalid path: " + subpath;
        }
        
        return model;
    }
    
    private boolean validatePath(String subpath)
    {
        String proposedPath = captureDirectory.getAbsolutePath() + subpath;
        
        boolean valid = false;
        
        File proposedDirectory = new File(proposedPath);
        String proposedFullPath = proposedDirectory.getAbsolutePath();
        String fullPath = captureDirectory.getAbsolutePath();
        
        // prevent a ../../../ attack, by making sure the proposed path is under the capture directory
        if( proposedFullPath.startsWith(fullPath) )
        {
            valid = true;
        }
        
        return valid;
    }
}
