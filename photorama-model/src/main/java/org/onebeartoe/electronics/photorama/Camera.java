
package org.onebeartoe.electronics.photorama;

/**
 * @author Roberto Marquez
 */
public interface Camera 
{
    long getTimelapse();
    
    void setTimelapse(long delay, FrequencyUnits unit);    
    
    void startTimelapse();
    
    void stopTimelapse();
    
    void takeSnapshot();
}



