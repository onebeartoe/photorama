
package org.onebeartoe.electronics.photorama.states;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiConfirmShutdownState extends RaspberryPiStates
{
    private String value = "\tConfirm Shutdown?\t";
    
    @Override
    public String getValue()
    {
        return value;
    }
    
    @Override
    public PhotoramaState selectButton()
    {
        value = "\tShutting Down\t";

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
