
//TODO: rename this package to org.onebeartoe.photorama.state.builder
package org.onebeartoe.media.players.randomjuke.photorama.state.buider.maven;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.onebeartoe.electronics.photorama.Camera;
import org.onebeartoe.electronics.photorama.mock.MockCamera;
import org.onebeartoe.electronics.photorama.states.Photorama;
import org.onebeartoe.electronics.photorama.states.PhotoramaState;

public class FXMLController implements Initializable 
{
    private PhotoramaState currentState;
    
    public FXMLController()
    {
        Camera camera = new MockCamera();
        Photorama photorama = new Photorama(camera);
        currentState = photorama.getRootState();
    }
    
    @FXML
    private Label stateValueLabel;
    
    @FXML
    private Label stateLabel;
    
    @FXML
    private void onLeftButtonAction(ActionEvent event) 
    {
        printButtonAction(event);
        
        currentState = currentState.leftButton();
        
        updateUiLabels();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        updateUiLabels();
    }

    @FXML
    private void onDownButtonAction(ActionEvent event)
    {
        printButtonAction(event);
        
        currentState = currentState.downButton();
        
        updateUiLabels();
    }
    
    @FXML
    private void onRightButtonAction(ActionEvent event)
    {
        printButtonAction(event);
        
        currentState = currentState.rightButton();
        
        updateUiLabels();
    }
    
    @FXML
    private void onSelectButttonAction(ActionEvent event)
    {
        printButtonAction(event);
        
        currentState = currentState.selectButton();
        
        updateUiLabels();
    }
    
    @FXML
    private void onUpButtonAction(ActionEvent event)
    {
        printButtonAction(event);
        
        currentState = currentState.upButton();
        
        updateUiLabels();
    }
    
    private void printButtonAction(ActionEvent event)
    {
        Object o = event.getSource();
        Button button = (Button) o;
        String text = button.getText();
        
        System.out.println(text + " was clicked.");
    }
    
    private void updateUiLabels()
    {
        String text = currentState.getLabel();
        stateLabel.setText(text);
        
        text = currentState.getValue();
        stateValueLabel.setText(text);
    }
}
