
package org.onebeartoe.electronics.photorama;

/**
 * @author Roberto Marquez
 */
public class LcdKeypadPhotorama 
{
    private Screens currentScreen;
    
    public void nextScreen()
    {
        switch(currentScreen)
        {
            case TIME_LAPSE:
            {
                currentScreen = Screens.FREQUENCY_UNIT;
                break;
            }
            case FREQUENCY_UNIT:
            {
                currentScreen = Screens.FEQUENCY;
                break;
            }
            case FEQUENCY:
            {
                currentScreen = Screens.FILENAMES;
                break;
            }                
            case FILENAMES:
            {
                currentScreen = Screens.MODE;
                break;
            }                
            case MODE:
            {
                currentScreen = Screens.TIME_LAPSE;
                break;
            }
        }
    }
}
