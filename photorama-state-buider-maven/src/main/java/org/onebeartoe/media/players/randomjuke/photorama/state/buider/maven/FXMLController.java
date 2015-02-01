package org.onebeartoe.media.players.randomjuke.photorama.state.buider.maven;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.onebeartoe.electronics.photorama.states.Photorama;
import org.onebeartoe.electronics.photorama.states.PhotoramaState;

public class FXMLController implements Initializable 
{
    private PhotoramaState currentState;
    
    public FXMLController()
    {
        Photorama photorama = new Photorama();
        currentState = photorama.getRootState();
    }
    
    @FXML
    private Label stateValueLabel;
    
    @FXML
    private Label stateLabel;
    
    @FXML
    private void handleLeftButtonAction(ActionEvent event) 
    {
        System.out.println("Left was clicked.");
        
        currentState = currentState.leftButton();
        
        updateLabels();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        updateLabels();
    }
    
    @FXML
    private void onRightButtonAction(ActionEvent event)
    {
        System.out.println("Riht was clicked.");
        
        currentState = currentState.rightButton();
        
        updateLabels();
    }
    
    private void updateLabels()
    {
        String text = currentState.getLabel();
        stateLabel.setText(text);
        
        text = currentState.getValue();
        stateValueLabel.setText(text);
    }
}
