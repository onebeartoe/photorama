
package org.onebeartoe.electronics.photorama.states.time.lapse;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class TimeLapseHoursState extends TimeLapseStates
{
    public TimeLapseHoursState()
    {
        value = "<- Hours";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        return leftState;
    }
}
