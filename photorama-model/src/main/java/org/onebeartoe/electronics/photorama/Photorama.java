
package org.onebeartoe.electronics.photorama;

import org.onebeartoe.electronics.photorama.states.mode.ModeFootPedalState;
import org.onebeartoe.electronics.photorama.states.raspberry.pi.RaspberryPiShutdownState;
import org.onebeartoe.electronics.photorama.states.raspberry.pi.RaspberryPiOnState;
import org.onebeartoe.electronics.photorama.states.raspberry.pi.RaspberryPiConfirmShutdownState;
import org.onebeartoe.electronics.photorama.states.PhotoramaModeState;
import org.onebeartoe.electronics.photorama.states.PhotoramaRaspberryPiState;
import org.onebeartoe.electronics.photorama.states.PhotoramaState;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalOffState;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalOnState;
import org.onebeartoe.electronics.photorama.states.foot.pedal.FootPedalSnapshotState;
import org.onebeartoe.electronics.photorama.states.mode.ModeTimeLapseState;
import org.onebeartoe.electronics.photorama.states.time.lapse.HoursState;
import org.onebeartoe.electronics.photorama.states.time.lapse.MinutesState;
import org.onebeartoe.electronics.photorama.states.time.lapse.SecondsState;
import org.onebeartoe.electronics.photorama.states.time.lapse.TimeLapseHoursState;
import org.onebeartoe.electronics.photorama.states.time.lapse.TimeLapseMinutesState;
import org.onebeartoe.electronics.photorama.states.time.lapse.TimeLapseSecondsState;

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

        PhotoramaState secondsState = new SecondsState();

        PhotoramaState timeLapseSecondsState = new TimeLapseSecondsState();
        timeLapseSecondsState.setSelectButton(secondsState);
        
        secondsState.setUpButton(timeLapseSecondsState);
        
        PhotoramaState hoursState = new HoursState();
        
        PhotoramaState timeLapseHoursState = new TimeLapseHoursState();
        timeLapseHoursState.setSelectButton(hoursState);
        
        hoursState.setUpButton(timeLapseHoursState);
        
        MinutesState minutesState = new MinutesState();
                
        PhotoramaState timeLapseMinutesState = new TimeLapseMinutesState();
        timeLapseMinutesState.setLeftButton(timeLapseSecondsState);
        timeLapseMinutesState.setRightButton(timeLapseHoursState);
        timeLapseMinutesState.setSelectButton(minutesState);
        
        timeLapseSecondsState.setRightButton(timeLapseMinutesState);
        timeLapseHoursState.setLeftButton(timeLapseMinutesState);
        minutesState.setUpButton(timeLapseMinutesState);
                
        PhotoramaState modeTimeLapseState = new ModeTimeLapseState();
        modeTimeLapseState.setUpButton(photoramaModeState);
        modeTimeLapseState.setLeftButton(modeFootPedalState);
        modeTimeLapseState.setSelectButton(timeLapseMinutesState);
        
        timeLapseMinutesState.setUpButton(modeTimeLapseState);
        
        modeFootPedalState.setUpButton(photoramaModeState);
        modeFootPedalState.setSelectButton(footPedalOffState);
        modeFootPedalState.setRightButton(modeTimeLapseState);
    }
}
