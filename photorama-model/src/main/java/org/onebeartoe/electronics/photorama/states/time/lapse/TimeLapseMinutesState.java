
package org.onebeartoe.electronics.photorama.states.time.lapse;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class TimeLapseMinutesState extends TimeLapseStates
{
    public TimeLapseMinutesState()
    {
        value = "<- Minutes ->";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        return leftState;
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        return rightState;
    }
}
