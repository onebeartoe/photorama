
package org.onebeartoe.photorama.viewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private Logger logger;
    
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
        
        Collections.sort(names);
        Collections.reverse(names);
        
        return names;
    }
    
    private List<File> findFiles(FileType fileType, File directory)
    {
        boolean recursive = false;
        List<FileType> targets = new ArrayList();
        targets.add(fileType);

        FileSystemSearcher searcher = new FileSystemSearcher(directory, targets, recursive);
        List<File> files = searcher.findTargetFiles();
        
        return files;
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
    
    private List<String> loadDirectories(String subpath)
    {
        File directory;
        if(subpath == null)
        {
            directory = captureDirectory;
        }
        else
        {
            directory = new File(captureDirectory, subpath);
        }
        List<File> directories = findFiles(FileType.DIRECTORY, directory);
        List<String> names = fileNames(directories);
        
        names.remove("META-INF");
        names.remove("WEB-INF");
        names.remove("images");
        
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
        
    private List<String> loadImages(String subpath)
    {

        File directory;
        if(subpath == null)
        {
            directory = captureDirectory;
        }
        else
        {
            directory = new File(captureDirectory, subpath);
        }                        
        
        List<File> images = findFiles(FileType.IMAGE, directory);
        
        List<String> names;
        if(images.size() == 0)
        {
            File newestSubdir = findNewestSubdir(directory);
            images = findFiles(FileType.IMAGE, newestSubdir);
            
            String sub = newestSubdir.getName();
            
            names = new ArrayList();
            if(images != null && images.size() > 0)
            {
                for(File f : images)
                {
                    names.add(sub + "/" + f.getName() );
                }
            }
        }
        else
        {
            names = fileNames(images);
        }
        
        Collections.sort(names);
        Collections.reverse(names);
        
        int imageMax = 3;
                
        List<String> displayedNames = new ArrayList();
        // only display imageMax photos on the preview
        for(String n : names)
        {
            // only show a limited amount of pictures if it is the main page, not for folder views
            if(displayedNames.size() == imageMax && images.size() == 0)
            {
                break;
            }
                    
            displayedNames.add(n);
        }
        
        return displayedNames;
    }
    
    private File findNewestSubdir(File parent)
    {
        File latest = parent;
        List<File> directories = findFiles(FileType.DIRECTORY, parent);
        if(directories == null || directories.size() == 0)
        {
            logger.log(Level.INFO, "No subdirectories were found for: " + parent.getAbsolutePath() );
        }
        else
        {
            latest = directories.get(0);
            for(File dir : directories)
            {
                if(dir.lastModified() > latest.lastModified() )
                {
                    latest = dir;
                }
            }
        }
                
        return latest;
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
