
package org.onebeartoe.electronics.photorama.states.foot.pedal;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class FootPedalOffState extends FootPedalStates
{
    @Override
    public String getValue()
    {
        return "<-\tOff\t";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        printMovingTo(leftState);
        
        return leftState;
    }    
}
