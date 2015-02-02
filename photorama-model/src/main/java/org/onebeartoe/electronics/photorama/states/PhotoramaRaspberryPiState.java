package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;

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
