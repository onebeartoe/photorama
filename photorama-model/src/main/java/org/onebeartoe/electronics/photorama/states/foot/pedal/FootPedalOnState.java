
package org.onebeartoe.electronics.photorama.states.foot.pedal;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class FootPedalOnState extends FootPedalStates
{
    @Override
    public String getValue()
    {
        return "On ->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        printMovingTo(rightState);
        
        return rightState;
    }    
}
