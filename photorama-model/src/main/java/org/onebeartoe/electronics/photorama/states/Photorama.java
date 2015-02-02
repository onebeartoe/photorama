
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;

/**
 * @author Roberto Marquez
 */
public class Photorama 
{
    private PhotoramaState rootState;

    private Camera camera;

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }
    
    public Photorama(Camera camera)
    {
        this.camera = camera;
        populateStates();
    }
    
    public PhotoramaState getRootState()
    {
        return rootState;
    }
    
    private void populateStates()
    {
        PhotoramaState photoramaRaspberryPiState = new PhotoramaRaspberryPiState();
        
        PhotoramaState raspberryPiOffState = new RaspberryPiShutdownState();
        
        PhotoramaState raspberryPiOnState = new RaspberryPiOnState();
        
        raspberryPiOnState.setRightButton(raspberryPiOffState);
        raspberryPiOnState.setUpButton(photoramaRaspberryPiState);
        
        raspberryPiOffState.setLeftButton(raspberryPiOnState);
        
        rootState = new PhotoramaModeState();
        rootState.setRightButton(photoramaRaspberryPiState);
        rootState.setCamera(camera);
        
        photoramaRaspberryPiState.setLeftButton(rootState);
        photoramaRaspberryPiState.setSelectButton(raspberryPiOnState);
    }
}
