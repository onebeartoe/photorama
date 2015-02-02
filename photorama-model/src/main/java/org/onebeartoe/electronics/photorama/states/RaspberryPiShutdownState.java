
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiShutdownState extends RaspberryPiStates
{


    
    @Override
    public String getValue()
    {
        return "<-\tShutdown\t";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        printMovingTo(leftState);
        
        return leftState;
    }
}
