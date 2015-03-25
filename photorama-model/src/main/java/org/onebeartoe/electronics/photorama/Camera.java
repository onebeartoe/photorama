
package org.onebeartoe.electronics.photorama;

/**
 * A call to the  setCameraOutputPath() method is needed after the object is 
 * instantiated.
 * 
 * @author Roberto Marquez
 */
public abstract class Camera 
{
    protected PhotoramaModes mode;
    
    protected String outputPath;
    
    protected TimeLapseConfiguration configuration;
    
    protected boolean timeLapseOn;
    
    public PhotoramaModes getMode()
    {
        return mode;
    }
    
    public String getOutputPath()
    {
        return outputPath;
    }
    
    public abstract long getTimelapse();
    
    public FrequencyUnits getTimelapseUnit()
    {
        return configuration.unit;
    }
            
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

    public void setTimelapse(long delay, FrequencyUnits unit)
    {
        configuration.delay = delay;
        configuration.unit = unit;
        
        if(timeLapseOn)
        {
            startTimelapse();
        }
    }
    
    public abstract void startTimelapse();
    
    public abstract void stopTimelapse();
    
    public abstract void takeSnapshot();
}