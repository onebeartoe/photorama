
package org.onebeartoe.electronics.photorama.states;

/**
 * @author Roberto Marquez
 */
public class Photorama 
{
    private PhotoramaState rootState;

    public Photorama()
    {
        populateStates();
    }
    
    public PhotoramaState getRootState()
    {
        return rootState;
    }
    
    private void populateStates()
    {        
        PhotoramaState rightState = new PhotoramaRaspberryPiState();
        
        rootState = new PhotoramaModeState();
        rootState.setRightButton(rightState);
        
        rightState.setLeftButton(rootState);
       
    }
}
