
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
public class RaspberryPiCamera extends Camera
{    
    private Timer timer;
    
    private TimeLapseConfiguration configuration;
    
    private Logger logger;
    
    private boolean timeLapseOn;
    
    private final String PI_HOME = "/home/pi/";
    
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
    public String getOutputPath()
    {
        String subpath;
        switch(mode)
        {
            case FOOT_PEDAL:
            {
                subpath = "foot-pedal/";
                break;
            }
            case PHOTO_BOOTH:
            {
                subpath = "photo-booth/";
                break;
            }
            case TIME_LAPSE:
            {
                subpath = "time-lapse/";
                break;
            }
            default:
            {
                // off
                subpath = "";
            }
        }
        outputPath = PI_HOME + subpath;
        
        // make the directory if it does not exist
        File f = new File(outputPath);
        if( !f.exists() )
        {
            f.mkdirs();
        }
        
        return outputPath;
    }
    
    @Override
    public long getTimelapse() 
    {
        return configuration.delay;
    }
    
    @Override
    public void setMode(PhotoramaModes mode)
    {
        super.setMode(mode);
        
        // update the output path
        String outpath = getOutputPath();
        
        setOutputPath(outpath);
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
        Timer  snapshotTimer = new Timer();
        Date now = new Date();
        snapshotTimer.schedule(captureTask, now);
    }
    
    private class CaptureTask extends TimerTask
    {
        @Override
        public void run() 
        {
            String parentPath = getOutputPath();
            String filename = Filesystem.systimeToFilename();            
            String outputPath = parentPath + filename;
            
            String command = "raspistill --quality 75 --output " + outputPath + ".jpg";
            Commander commander = new Commander(command);
            try 
            {
                commander.execute();
                StringBuilder sb = new StringBuilder();
                for(String line : commander.getStderr() )
                {
                    sb.append(line);
                    sb.append(System.lineSeparator() );                    
                }
                logger.log(Level.SEVERE, sb.toString() );
                
            } 
            catch (Exception ex) 
            {
                logger.log(Level.SEVERE, null, ex);
            }
        }        
    }
}
