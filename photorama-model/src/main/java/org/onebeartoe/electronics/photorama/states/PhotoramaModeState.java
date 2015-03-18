
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;

/**
 * @author Roberto Marquez
 */
public class PhotoramaModeState extends PhotoramaStates
{
   
    @Override
    public String getValue()
    {
        return "Mode ->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        System.out.println("moving to " + rightState.getClass().getSimpleName());
        
        return rightState;
    }
    
    public PhotoramaState upButton()
    {
        String className = getClass().getSimpleName();
        System.out.println(className + " does nothing for UP button pushes.");
        
        return this;
    }
}
