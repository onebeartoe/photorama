
package org.onebeartoe.electronics.photorama.states.time.lapse;

import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.FrequencyUnits;
import org.onebeartoe.electronics.photorama.states.PhotoramaState;
import org.onebeartoe.electronics.photorama.states.RootState;

/**
 * @author Roberto Marquez
 */
public class TimeLapseStates extends RootState
{
    @Override
    public String getLabel()
    {
        return "Time Lapse";
    }
    
    @Override
    public PhotoramaState selectButton()
    {
        PhotoramaState minutesState = super.selectButton();

        setTimelapse(camera, minutesState);
        camera.startTimelapse();
        
        return minutesState;
    }
    
    public static void setTimelapse(Camera camera, PhotoramaState state)
    {
        String v = state.getValue();
        long delay = Long.valueOf(v);
                
        String minutesLabel = state.getLabel();
        minutesLabel = minutesLabel.toUpperCase();
        FrequencyUnits unit = FrequencyUnits.valueOf(minutesLabel);

        camera.setTimelapse(delay, unit);
        
        System.out.println("the time lapse was reset");
    }
}
