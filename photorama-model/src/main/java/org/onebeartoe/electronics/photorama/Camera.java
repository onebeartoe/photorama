
package org.onebeartoe.electronics.photorama;

import java.io.File;

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
    public void setOutputPath(String path) throws Exception
    {
        File outdir = new File(path);
        
        if( ! outdir.exists() )
        {
            // the output directory does not exist,
            // try creating it
            boolean dirCreated = outdir.mkdirs();
            
            if( !dirCreated )
            {
                String message = "could not set output directory: " + path;
                
                throw new Exception(message);
            }
        }

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