
package org.onebeartoe.electronics.photorama.states;

import org.onebeartoe.electronics.photorama.Camera;

/**
 * @author Roberto Marquez
 */
public class RootState implements PhotoramaState
{
    protected PhotoramaState upState;
    protected PhotoramaState downState;
    protected PhotoramaState leftState;
    protected PhotoramaState rightState;
    protected PhotoramaState selectState;
    
    static protected Camera camera;

    public String getLabel()
    {
        return getClass().getName();
    }

    public String getValue()
    {
        return "nothing";
    }

    public PhotoramaState leftButton()
    {
        String className = getClass().getSimpleName();
        
        System.out.println(className + " does nothing for LEFT button pushes.");
        
        return this;
    }
    
    protected void printMovingTo(PhotoramaState state)
    {
        String stateName = state.getClass().getSimpleName();
        
        System.out.println("Moving to " + stateName);
    }

    public PhotoramaState rightButton()
    {
        String className = getClass().getSimpleName();
        System.out.println(className + " does nothing for RIGHT button pushes.");
        
        return this;
    }

    @Override
    public PhotoramaState upButton()
    {
        printMovingTo(upState);
        
        return upState;
    }
//    public PhotoramaState upButton()
//    {
//        String className = getClass().getSimpleName();
//        System.out.println(className + " does nothing for UP button pushes.");
//        
//        return this;
//    }

    public PhotoramaState downButton()
    {
        String className = getClass().getSimpleName();
        System.out.println(className + " does nothing for DOWN button pushes.");
        
        return this;
    }

    @Override
    public PhotoramaState selectButton()
    {
        printMovingTo(selectState);

        return selectState;
    }    
//    public PhotoramaState selectButton()
//    {
//        String className = getClass().getSimpleName();
//        System.out.println(className + " does nothing for SELECT button pushes.");
//        
//        return this;
//    }
    
    @Override
    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    @Override
    public void setLeftButton(PhotoramaState leftState)
    {
        this.leftState = leftState;
    }

    @Override
    public void setRightButton(PhotoramaState rightState)
    {
        this.rightState = rightState;
    }

    @Override
    public void setUpButton(PhotoramaState upState)
    {
        this.upState = upState;
    }

    @Override
    public void setDownButton(PhotoramaState downState)
    {
        this.downState = downState;
    }

    @Override
    public void setSelectButton(PhotoramaState selectState)
    {
        this.selectState = selectState;
    }
}
