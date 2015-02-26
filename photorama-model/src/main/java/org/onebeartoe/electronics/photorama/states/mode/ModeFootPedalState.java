
package org.onebeartoe.electronics.photorama.states.mode;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class ModeFootPedalState extends ModeStates
{
    @Override
    public String getValue()
    {
        return "\tFoot Pedal\t->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        printMovingTo(rightState);
        
        return rightState;
    }
}