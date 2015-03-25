
package org.onebeartoe.electronics.photorama.states.time.lapse;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;
import org.onebeartoe.electronics.photorama.states.RootState;

/**
 * @author Roberto Marquez
 */
public class MinutesState extends RootState
{
    protected int delta = 1;
    
    public MinutesState()
    {
        label = "Minutes";
        value = "5";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        Integer i = Integer.valueOf(value);

        i -= delta;
        
        value = String.valueOf(i);
        
        TimeLapseStates.setTimelapse(camera, this);
        
        String className = getClass().getSimpleName();
        System.out.println(className + " decremented minute value.");
        
        return this;
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        Integer i = Integer.valueOf(value);
        i += delta;
        
        value = String.valueOf(i);
        
        TimeLapseStates.setTimelapse(camera, this);
        
        String className = getClass().getSimpleName();
        System.out.println(className + " incremented minute value.");
        
        return this;
    }
    
    @Override
    public PhotoramaState upButton()
    {
        PhotoramaState upState = super.upButton();
        
        camera.stopTimelapse();
        
        return upState;
    }
}
