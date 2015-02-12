
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.states.mode.ModeFootPedalState;
import org.onebeartoe.electronics.photorama.states.raspberry.pi.RaspberryPiShutdownState;
import org.onebeartoe.electronics.photorama.states.raspberry.pi.RaspberryPiOnState;
import org.onebeartoe.electronics.photorama.states.raspberry.pi.RaspberryPiConfirmShutdownState;
import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalOffState;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalOnState;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalSnapshotState;
import org.onebeartoe.electronics.photorama.states.mode.ModeTimeLapseState;

/**
 * @author Roberto Marquez
 */
public class Photorama 
{
    private PhotoramaState photoramaModeState;

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
        return photoramaModeState;
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
        
        // this is the root state
        photoramaModeState = new PhotoramaModeState();
        photoramaModeState.setRightButton(photoramaRaspberryPiState);
        photoramaModeState.setSelectButton(modeFootPedalState);
        photoramaModeState.setCamera(camera);
        
        photoramaRaspberryPiState.setLeftButton(photoramaModeState);
        photoramaRaspberryPiState.setSelectButton(raspberryPiOnState);
        
        PhotoramaState footPedalOnState = new FootPedalOnState();
        
        PhotoramaState footPedalOffState = new FootPedalOffState();
        footPedalOffState.setLeftButton(footPedalOnState);
        
        PhotoramaState footPedalSnapshotState = new FootPedalSnapshotState();
        footPedalSnapshotState.setUpButton(footPedalOnState);
        
        footPedalOnState.setUpButton(modeFootPedalState);
        footPedalOnState.setSelectButton(footPedalSnapshotState);
        footPedalOnState.setRightButton(footPedalOffState);
        
        PhotoramaState modeTimeLapseState = new ModeTimeLapseState();
        modeTimeLapseState.setUpButton(photoramaModeState);
        modeTimeLapseState.setLeftButton(modeFootPedalState);
        
        modeFootPedalState.setUpButton(photoramaModeState);
        modeFootPedalState.setSelectButton(footPedalOffState);
        modeFootPedalState.setRightButton(modeTimeLapseState);
    }
}
