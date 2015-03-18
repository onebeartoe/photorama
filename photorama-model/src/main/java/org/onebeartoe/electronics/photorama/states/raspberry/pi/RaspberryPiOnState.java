
package org.onebeartoe.electronics.photorama.states.raspberry.pi;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiOnState extends RaspberryPiStates
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
    
    @Override
    public PhotoramaState upButton()
    {
        printMovingTo(upState);
        
        return upState;
    }
    
    @Override
    public PhotoramaState selectButton()
    {
        String className = getClass().getSimpleName();
        System.out.println(className + " does nothing for SELECT button pushes.");
        
        return this;        
    }
}
