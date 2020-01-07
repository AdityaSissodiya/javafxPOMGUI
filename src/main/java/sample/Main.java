package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

public class Main extends Application {

    private static final int v = 550;
    private static final int v1 = 350;

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/main/resources/sample.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Service Resource List");
        primaryStage.setScene(new Scene(root, v, v1));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
