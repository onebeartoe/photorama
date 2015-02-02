
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
        return "\tMode\t->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        System.out.println("moving to " + rightState.getClass().getSimpleName());
        
        return rightState;
    }
}