
package org.onebeartoe.electronics.photorama.states.mode;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class ModeTimeLapseState extends ModeStates
{
    public ModeTimeLapseState()
    {
        value = "<- Time Lapse";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        printMovingTo(leftState);
        
        return leftState;
    }
}
