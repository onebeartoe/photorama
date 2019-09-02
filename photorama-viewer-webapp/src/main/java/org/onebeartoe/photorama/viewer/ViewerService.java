
package org.onebeartoe.photorama.viewer;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.filesystem.FileSystemSearcher;
import org.onebeartoe.filesystem.FileType;

/**
 * This class populates the model object used by the JSP front end.
 * @author Roberto Marquez
 */
public class ViewerService
{
    Logger logger = Logger.getLogger(ViewerService.class.getName());
    
    private List<File> findFiles(FileType fileType, File directory)
    {
        boolean recursive = false;
        
        List<FileType> targets = new ArrayList();
        
        targets.add(fileType);

        FileSystemSearcher searcher = new FileSystemSearcher(directory, targets, recursive);
        
        List<File> files = searcher.findTargetFiles();
        
        return files;
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
    
    private List<String> loadDirectories(File captureDirectory, String subpath)
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

        File [] fileArray = directory.listFiles( file -> file.isDirectory() );
        
        List<File> directories = Arrays.asList( fileArray );
        
        List<String> names = fileNames(directories);
        
        names.remove("META-INF");
        names.remove("WEB-INF");
        names.remove("images");
        
        return names;
    }    
    
    public DirectoryView loadDirectoryView(File captureDirectory, String subpath)
    {
        DirectoryView model = new DirectoryView();
        
        boolean validPath = validatePath(captureDirectory, subpath);
        
        if(validPath)
        {
            model.path = subpath;
            model.directories = loadDirectories(captureDirectory, subpath);
            model.images = loadImages(captureDirectory, subpath);
        }
        else
        {
            model.path = "Invalid path: " + subpath;
        }
        
        return model;
    }   

    private List<String> loadImages(File captureDirectory, String subpath)
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
    
    private boolean validatePath(File captureDirectory, String subpath)
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
