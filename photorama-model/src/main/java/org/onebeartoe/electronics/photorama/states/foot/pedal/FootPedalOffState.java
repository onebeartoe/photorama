
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
        return "<- Off";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        printMovingTo(leftState);
        
        return leftState;
    }    
}
