
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiOnState extends RaspberryPiStates
{
 
    
    @Override
    public String getValue()
    {
        return "\tOn\t->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        printMovingTo(rightState);
        
        return rightState;
    }
}
