
package org.onebeartoe.electronics.photorama.states.raspberry.pi;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiShutdownState extends RaspberryPiStates
{
    @Override
    public String getValue()
    {
        return "<- Shutdown";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        printMovingTo(leftState);
        
        return leftState;
    }
    
    @Override
    public PhotoramaState selectButton()
    {
        printMovingTo(selectState);
        
        return selectState;
    }
}
