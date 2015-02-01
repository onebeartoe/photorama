
package org.onebeartoe.electronics.photorama.states;

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
        System.out.println(getClass().getName() + " does nothing for LEFT button pushes.");
        
        return this;
    }

    public PhotoramaState rightButton()
    {
        System.out.println(getClass().getName() + " does nothing for RIGHT button pushes.");
        
        return this;
    }

    public PhotoramaState upButton()
    {
        System.out.println(getClass().getName() + " does nothing for UP button pushes.");
        
        return this;
    }

    public PhotoramaState downButton()
    {
        System.out.println(getClass().getName() + " does nothing for DOWN button pushes.");
        
        return this;
    }

    public PhotoramaState selectButton()
    {
        System.out.println(getClass().getName() + " does nothing for SELECT button pushes.");
        
        return this;
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
