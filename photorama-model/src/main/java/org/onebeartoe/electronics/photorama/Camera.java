
package org.onebeartoe.electronics.photorama;

/**
 * @author Roberto Marquez
 */
public abstract class Camera 
{
    protected PhotoramaModes mode;
    
    protected String outputPath;
    
    public PhotoramaModes getMode()
    {
        return mode;
    }
    
    public String getOutputPath()
    {
        return outputPath;
    }
    
    public abstract long getTimelapse();
            
    public void setMode(PhotoramaModes mode)
    {
        this.mode = mode;
        
        stopTimelapse();
    }
    
    /**
     * Make sure the path has a path separator character at the end.
     * @param path 
     */
    public void setOutputPath(String path)
    {
        outputPath = path;
    }
    
    public abstract void setTimelapse(long delay, FrequencyUnits unit);    
    
    public abstract void startTimelapse();
    
    public abstract void stopTimelapse();
    
    public abstract void takeSnapshot();
}