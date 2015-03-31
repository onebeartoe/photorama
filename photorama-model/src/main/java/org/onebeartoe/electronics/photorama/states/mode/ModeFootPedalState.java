
package org.onebeartoe.electronics.photorama.states.mode;

import org.onebeartoe.electronics.photorama.states.PhotoramaState;

/**
 * @author Roberto Marquez
 */
//TODO: rename this calss to ModeSnapshotState
public class ModeFootPedalState extends ModeStates
{
    @Override
    public String getValue()
    {
        return "Snapshot ->";
    }
    
    @Override
    public PhotoramaState rightButton()
    {
        printMovingTo(rightState);
        
        return rightState;
    }
}
