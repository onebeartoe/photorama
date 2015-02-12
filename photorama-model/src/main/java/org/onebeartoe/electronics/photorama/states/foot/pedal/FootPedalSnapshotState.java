
package org.onebeartoe.electronics.photorama.states.foot.pedal;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

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
        value = "\ttaking a snap shot\t";

        System.out.println( value.trim() );

        camera.takeSnapshot();
        
        return this;
    }
}
