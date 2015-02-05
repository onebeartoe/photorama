
package org.onebeartoe.electronics.photorama.states;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiShutdownState extends RaspberryPiStates
{
    @Override
    public String getValue()
    {
        return "<-\tShutdown\t";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        printMovingTo(leftState);
        
        return leftState;
    }
    
    @Override
    public PhotoramaState selectButton()
    {
        printMovingTo(selectState);
        
        return selectState;
    }
}
