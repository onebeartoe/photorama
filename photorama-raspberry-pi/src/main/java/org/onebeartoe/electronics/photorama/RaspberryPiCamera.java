
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
    
    private Logger logger;
    
    private boolean timeLapseOn;
    
    public RaspberryPiCamera()
    {
        logger = Logger.getLogger( getClass().getName() );
                
        configuration = new TimeLapseConfiguration();
        configuration.delay = 5000;
        configuration.outputDirectory = new File(".");
    }
    
    /**
     * calculates the number of milliseconds for the current time lapse setting
     * @return 
     */
    private long calculateDelay()
    {
        int multiplier;
        switch(configuration.unit)
        {
            case DAYS:
            {
                multiplier = 1000 * 60 * 60 * 24;
                break;
            }
            case HOURS:
            {
                multiplier = 1000 * 60 * 60;
                break;
            }
            case MINUTES:
            {
                multiplier = 1000 * 60;
                break;
            }
            default:
            {
                // SECONDS            
                multiplier = 1000;
            }
        }
                
        long delay = configuration.delay * multiplier;
                
        return delay;
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
        
        if(timeLapseOn)
        {
            startTimelapse();
        }
    }

    @Override
    public void startTimelapse() 
    {
        stopTimelapse();
    
        timeLapseOn = true;
                
        TimerTask captureTask = new CaptureTask();
        Date now = new Date();
        long delay = calculateDelay();
        
        logger.log(Level.INFO, "start time lapse: " + delay);
        
        timer = new Timer();                
        timer.schedule(captureTask, now, delay);//configuration.delay);
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
        
        timeLapseOn = false;
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
                for(String line : commander.getStderr() )
                {
                    System.err.println(line);
                }
                
            } 
            catch (Exception ex) 
            {
                logger.log(Level.SEVERE, null, ex);
            }
        }        
    }
}
