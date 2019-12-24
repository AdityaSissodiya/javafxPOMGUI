package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private ImageView cernerLogo;
    @FXML
    private Button button;

    private void handleButtonAction(javafx.event.ActionEvent actionEvent) {
        Image image = new Image("imgRes/cerner_logo.png");
        cernerLogo.setImage(image);

    }

    public void initialize(URL url, ResourceBundle rb) {
        button.setOnAction(this::handleButtonAction);
        // TODO
    }

}
