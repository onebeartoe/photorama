
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalOffState;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalOnState;

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
        PhotoramaState modeFootPedalState = new ModeFootPedalState();
        
        PhotoramaState photoramaRaspberryPiState = new PhotoramaRaspberryPiState();
        
        PhotoramaState raspberryPiConfirmShutdownState = new RaspberryPiConfirmShutdownState();
        
        PhotoramaState raspberryPiShutdownState = new RaspberryPiShutdownState();
        raspberryPiShutdownState.setSelectButton(raspberryPiConfirmShutdownState);

        raspberryPiConfirmShutdownState.setUpButton(raspberryPiShutdownState);
        
        PhotoramaState raspberryPiOnState = new RaspberryPiOnState();
        
        raspberryPiOnState.setRightButton(raspberryPiShutdownState);
        raspberryPiOnState.setUpButton(photoramaRaspberryPiState);
        
        raspberryPiShutdownState.setLeftButton(raspberryPiOnState);
        
        rootState = new PhotoramaModeState();
        rootState.setRightButton(photoramaRaspberryPiState);
        rootState.setSelectButton(modeFootPedalState);
        rootState.setCamera(camera);
        
        photoramaRaspberryPiState.setLeftButton(rootState);
        photoramaRaspberryPiState.setSelectButton(raspberryPiOnState);
        
        PhotoramaState footPedalOnState = new FootPedalOnState();
        
        PhotoramaState footPedalOffState = new FootPedalOffState();
        footPedalOffState.setLeftButton(footPedalOnState);
        
        footPedalOnState.setRightButton(footPedalOffState);
                
        modeFootPedalState.setUpButton(rootState);
        modeFootPedalState.setSelectButton(footPedalOffState);
    }
}
