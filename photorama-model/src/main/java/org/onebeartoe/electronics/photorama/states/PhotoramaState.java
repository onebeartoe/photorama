
package org.onebeartoe.electronics.photorama.states;

/**
 * This interface is used to move between the various screen in Photorama.
 * @author Roberto Marquez
 */
public interface PhotoramaState
{
    public String getLabel();
    public String getValue();
    
    public PhotoramaState leftButton();
    public PhotoramaState rightButton();
    public PhotoramaState upButton();
    public PhotoramaState downButton();
    public PhotoramaState selectButton();
    
    public void setLeftButton(PhotoramaState leftState);
    public void setRightButton(PhotoramaState rightState);
    public void setUpButton(PhotoramaState upState);
    public void setDownButton(PhotoramaState downState);
    public void setSelectButton(PhotoramaState selectState);
}
