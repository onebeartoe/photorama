
package org.onebeartoe.photorama.viewer;

import java.util.List;

/**
 * @author Roberto Marquez
 */
public class DirectoryView 
{
    public String path;

    public List<String> directories;
    
    public List<String> images;
    
    public List<String> videos;

    public List<String> getDirectories() 
    {
        return directories;
    }

    public void setDirectories(List<String> directories) {
        this.directories = directories;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPath() 
    {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) 
    {
        this.videos = videos;
    }
}
