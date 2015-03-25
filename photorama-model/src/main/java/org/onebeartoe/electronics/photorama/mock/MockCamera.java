
package org.onebeartoe.electronics.photorama.mock;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.TimeLapseConfiguration;

/**
 * @author Roberto Marquez
 */
public class MockCamera extends Camera
{
    private Logger logger;
    
    public MockCamera()
    {
        logger = Logger.getLogger( getClass().getName() );
        
        configuration = new TimeLapseConfiguration();
        configuration.delay = 5000;
    }
    
    @Override
    public long getTimelapse()
    {
        return configuration.delay;
    }

    @Override
    public void startTimelapse()
    {
        logger.log(Level.INFO, "start time lapse");
    }

    @Override
    public void stopTimelapse()
    {
        logger.log(Level.INFO, "stop time lapse");
    }

    @Override
    public void takeSnapshot()
    {
        System.out.println("taking a mock screenshot");
    }

}
