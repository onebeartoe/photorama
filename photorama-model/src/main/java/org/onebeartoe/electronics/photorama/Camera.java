
package org.onebeartoe.electronics.photorama;

/**
 * @author Roberto Marquez
 */
public abstract class Camera 
{
    private PhotoramaModes mode;
    
    public abstract long getTimelapse();
    
    public void setMode(PhotoramaModes mode)
    {
        this.mode = mode;
        
        stopTimelapse();
    }
    
    public abstract void setTimelapse(long delay, FrequencyUnits unit);    
    
    public abstract void startTimelapse();
    
    public abstract void stopTimelapse();
    
    public abstract void takeSnapshot();
}