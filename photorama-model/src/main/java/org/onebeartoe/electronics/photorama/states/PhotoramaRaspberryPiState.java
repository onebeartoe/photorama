package org.onebeartoe.electronics.photorama.states;

/**
 * @author Roberto Marquez
 */
public class PhotoramaRaspberryPiState extends PhotoramaStates
{    
    @Override
    public String getValue()
    {
        return "<-\tRaspberry Pi\t";
    }
    
    @Override
    public PhotoramaState leftButton()
    {
        System.out.println("moving to " + leftState.getClass().getSimpleName());
        
        return leftState;
    }
}
