
package org.onebeartoe.electronics.photorama.states.foot.pedal;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;
import org.onebeartoe.system.Sleeper;

/**
 * @author Roberto Marquez
 */
public class FootPedalSnapshotState extends FootPedalStates
{
    public FootPedalSnapshotState()
    {
        value = "Snapshot";
    }
    
    @Override
    public PhotoramaState selectButton()
    {
//        value = "taking snapshot";

        System.out.println( value.trim() );

        camera.takeSnapshot();
        
        Sleeper.sleepo(3000);
        
        System.out.println("snapshot taken");
        
        return this;
    }
}
