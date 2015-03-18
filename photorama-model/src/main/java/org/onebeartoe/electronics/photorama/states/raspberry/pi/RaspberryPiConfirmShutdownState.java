
package org.onebeartoe.electronics.photorama.states.raspberry.pi;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiConfirmShutdownState extends RaspberryPiStates
{
    private String value = "Confirm Shutdown?";
    
    @Override
    public String getValue()
    {
        return value;
    }
    
    @Override
    public PhotoramaState selectButton()
    {
        value = "Shutting Down";

        System.out.println( value.trim() );

// TODO: actually shutdown        
        
        return this;
    }
    
    @Override
    public PhotoramaState upButton()
    {
        printMovingTo(upState);
        
        return upState;
    }
    
    
}
