
package org.onebeartoe.electronics.photorama.states.time.lapse;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class TimeLapseSecondsState extends TimeLapseStates
{
    public TimeLapseSecondsState()
    {
        value = "Seconds ->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        return rightState;
    }
}
