
package org.onebeartoe.electronics.photorama;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.system.Commander;
import org.onebeartoe.system.Filesystem;

/**
 * @author Roberto Marquez
 */
public class RaspberryPiCamera implements Camera
{    
    private Timer timer;
    
    private TimeLapseConfiguration configuration;

//    private TimerTask captureTask;
    
    private Logger logger;
    
    public RaspberryPiCamera()
    {
        logger = Logger.getLogger( getClass().getName() );
        
//        captureTask = new CaptureTask();
                
        configuration = new TimeLapseConfiguration();
        configuration.delay = 5000;
        configuration.outputDirectory = new File(".");
    }

    @Override
    public long getTimelapse() 
    {
        return configuration.delay;
    }
    
    
    
    @Override
    public void setTimelapse(long delay, FrequencyUnits unit)
    {
        configuration.delay = delay;
        configuration.unit = unit;
    }

    @Override
    public void startTimelapse() 
    {
        stopTimelapse();
        
        timer = new Timer();
        Date now = new Date();
        TimerTask captureTask = new CaptureTask();
        timer.schedule(captureTask, now, configuration.delay);
    }

    @Override
    public void stopTimelapse() 
    {
        if(timer == null)
        {
            logger.log(Level.WARNING, "stopTimeLapse called when the timer is null");
        }
        else
        {
            timer.cancel();
        }
    }

    @Override
    public void takeSnapshot() 
    {
        TimerTask captureTask = new CaptureTask();
        timer.schedule(captureTask, null);
    }
    
    private class CaptureTask extends TimerTask
    {
        @Override
        public void run() 
        {
            String filename = Filesystem.systimeToFilename();
            String command = "raspistill -o /home/pi/" + filename + ".jpg";
            Commander commander = new Commander(command);
            try 
            {
                commander.execute();
            } 
            catch (Exception ex) 
            {
                logger.log(Level.SEVERE, null, ex);
            }
        }        
    }
}
