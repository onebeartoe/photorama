
package org.onebeartoe.electronics.photorama.mock;

import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.FrequencyUnits;

/**
 * @author Roberto Marquez
 */
public class MockCamera extends Camera
{

    @Override
    public long getTimelapse()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTimelapse(long delay, FrequencyUnits unit)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startTimelapse()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stopTimelapse()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void takeSnapshot()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
