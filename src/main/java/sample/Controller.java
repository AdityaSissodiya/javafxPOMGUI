package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //object to call database functions
    JdbcOracleConnection jdbcOracleConnection = new JdbcOracleConnection();
    boolean dispDown;
    boolean collDown;
    boolean rMode;

    File fileGreenTick = new File("src/main/resources/greenTick.png");
    Image imageGreenTick = new Image(fileGreenTick.toURI().toString());

    File fileRedCross = new File("src/main/resources/redCross.png");
    Image imageRedCross = new Image(fileRedCross.toURI().toString());

    File hourGlassFile = new File("src/main/resources/hourglass.png");
    Image imageHourGlass = new Image(hourGlassFile.toURI().toString());

    File cernerLogoFile = new File ("src/main/resources/cerner_logo.png");
    Image imageCernerLogo = new Image(cernerLogoFile.toURI().toString());

    @FXML
    private ImageView dispatchDownloadStatusPic;
    @FXML
    private  ImageView collectedDownloadStatusPic;
    @FXML
    private ImageView rModeStatusPic;
    @FXML
    private ImageView cernerLogoUI;
    @FXML
    private ComboBox spinnerList ;


    public void changePictureAsPerStatus(){
        if(dispDown){
            dispatchDownloadStatusPic.setImage(imageGreenTick);
        }
        if (!dispDown){
            dispatchDownloadStatusPic.setImage(imageRedCross);
        }
        if(collDown){
            collectedDownloadStatusPic.setImage(imageGreenTick);
        }
        if(!collDown){
            collectedDownloadStatusPic.setImage(imageRedCross);
        }
        if(rMode){
            rModeStatusPic.setImage(imageGreenTick);
        }
        if(!rMode){
            rModeStatusPic.setImage(imageRedCross);
        }
    }


    @FXML
    protected void handleInstNameChange(ActionEvent event) {
        String instrumentName = (String) spinnerList.getValue();

        dispDown = jdbcOracleConnection.executeQueryDispDown(instrumentName);
        collDown = jdbcOracleConnection.executeQueryCollDown(instrumentName);
        rMode = jdbcOracleConnection.executeQueryR_Mode(instrumentName);

        //Function To Change The Pictures (Green Tick ,Red Cross)
        changePictureAsPerStatus();
    }


    public void initialize (URL location, ResourceBundle resources){
        //Connect To Database
        jdbcOracleConnection.makeDatabaseConnection();
        jdbcOracleConnection.executeQueryInstrumentName();
        String[] cleanedArray = Arrays.stream(jdbcOracleConnection.instrumentNameArray).filter(Objects::nonNull).toArray(String[]::new);
        //Populates The Drop Down Menu (Combo Box)
        ObservableList<String> listOfInstruments = FXCollections.observableArrayList(cleanedArray);
        spinnerList.setItems(listOfInstruments);
        //Set The Waiting Icon
        cernerLogoUI.setImage(imageCernerLogo);
        dispatchDownloadStatusPic.setImage(imageHourGlass);
        collectedDownloadStatusPic.setImage(imageHourGlass);
        rModeStatusPic.setImage(imageHourGlass);
    }
}
