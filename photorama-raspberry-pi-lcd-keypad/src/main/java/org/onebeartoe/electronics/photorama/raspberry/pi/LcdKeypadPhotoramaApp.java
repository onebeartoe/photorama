
package org.onebeartoe.electronics.photorama.raspberry.pi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.RaspberryPiCamera;
import org.onebeartoe.electronics.photorama.Photorama;
import org.onebeartoe.electronics.photorama.states.PhotoramaState;
import se.hirt.pi.adafruitlcd.Button;
import static se.hirt.pi.adafruitlcd.Button.DOWN;
import static se.hirt.pi.adafruitlcd.Button.RIGHT;
import static se.hirt.pi.adafruitlcd.Button.SELECT;
import static se.hirt.pi.adafruitlcd.Button.UP;
import se.hirt.pi.adafruitlcd.ButtonListener;
import se.hirt.pi.adafruitlcd.ButtonPressedObserver;
import se.hirt.pi.adafruitlcd.Color;
import se.hirt.pi.adafruitlcd.ILCD;
import se.hirt.pi.adafruitlcd.impl.RealLCD;

/**
 * For the demo from the Hirt API, see the main() method in 
 * the se.hirt.pi.adafruitlcd.demo.Demo class.
 * 
 * @author Roberto Marquez
 */
public class LcdKeypadPhotoramaApp
{
    private PhotoramaState currentState;

    static final int BUS_NO = 1;  // 0 for Raspberry Pi B version 1 and 1 for version 2
    static final int BUS_ADDRESS = 0x20;

    private Logger logger;

    private Camera camera;

    private ILCD lcd;
    
    public LcdKeypadPhotoramaApp()
    {
        logger = Logger.getLogger(LcdKeypadPhotoramaApp.class.getName());

        try
        {
            String userHome = System.getProperty("user.home");

            String outpath = userHome + "/" + "onebeartoe/photorama/";
                    
            File outDirectory = new File(outpath);
            outDirectory.mkdirs();
            
            camera = new RaspberryPiCamera();
            camera.setOutputPath(outpath);
            
            Photorama photorama = new Photorama(camera);
            currentState = photorama.getRootState();

            lcd = new RealLCD();
            lcd.setBacklight(Color.ON);

            // set the start-up label
            updateLcd();
//            currentState.upButton();
//            lcd.setText("LCD Test!\nPress up/down...");

            ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
            observer.addButtonListener(new ButtonListener()
            {
                @Override
                public void onButtonPressed(Button button)
                {
                    try
                    {
                        lcd.clear();
                        
                        switch (button)
                        {
                            case UP:
                            {
                                printButtonEvent(button);
        
                                currentState = currentState.upButton();

                                updateLcd();
                                
                                break;
                            }
                            case DOWN:
                            {
                                printButtonEvent(button);
        
                                currentState = currentState.downButton();

                                updateLcd();
                                
                                break;
                            }    
                            case RIGHT:
                            {
                                printButtonEvent(button);
        
                                currentState = currentState.rightButton();

                                updateLcd();
                                
                                break;
                            }   
                            case LEFT:
                            {
                                printButtonEvent(button);
                                
                                currentState = currentState.leftButton();
                                
                                updateLcd();

                                break;
                            }   
                            case SELECT:
                            {
                                printButtonEvent(button);
        
                                currentState = currentState.selectButton();

                                updateLcd();
                                
                                break;
                            }
                            default:
                            {
                                String label = button.toString();
                                String text = String.format("Button %s is not in use...", label);
                                lcd.setText(text);
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        handleException(e);
                    }
                }
            });

            System.out.println("Press enter to quit!");
            System.in.read();
            lcd.stop();
        }
        catch (Exception ex)
        {
            StringBuilder sb = new StringBuilder();
            
            sb.append(ex.getMessage());
            
            String message = sb.toString();
                    
            logger.log(Level.SEVERE, message, ex);
        }
    }

    private static void handleException(IOException e)
    {
        System.out.println("Problem talking to LCD! Exiting!");
        e.printStackTrace();
        System.exit(2);
    }

    public static void main(String[] args) throws Exception
    {
        LcdKeypadPhotoramaApp app = new LcdKeypadPhotoramaApp();

        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
    
    private void printButtonEvent(Button button)
    {
        String text = button  + " was clicked.";
        
        System.out.println(text);
    }
    
    private void updateLcd() throws IOException
    {
        String text = currentState.getLabel();
        lcd.setText(0 , text);

        
        text = currentState.getValue();
        lcd.setText(1, text);
    }    
}
