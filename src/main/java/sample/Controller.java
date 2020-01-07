package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Controller extends Thread implements Initializable {
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
    protected ImageView dispatchDownloadStatusPic;
    @FXML
    protected ImageView collectedDownloadStatusPic;
    @FXML
    protected ImageView rModeStatusPic;
    @FXML
    protected ImageView cernerLogoUI;
    @FXML
    protected ComboBox spinnerList ;
    @FXML
    private TreeView<String> instrumetOrderablesUiTree;


    public void setupUiTreeForOrderableList(String instrumentNameForTree){
        TreeItem<String> instrumentNameInTree = new TreeItem<>(instrumentNameForTree);
        instrumetOrderablesUiTree.setRoot(instrumentNameInTree);
        String[] cleanedOrderableNameArray = Arrays.stream(jdbcOracleConnection.orderablesNameArray).filter(Objects::nonNull).toArray(String[]::new);
        for(int i=0; i<cleanedOrderableNameArray.length;i++){
            TreeItem<String> orderableDispKey = new TreeItem<String> (cleanedOrderableNameArray[i]);
            instrumentNameInTree.getChildren().add(orderableDispKey);
        }
    }

    public void executeUiUserQuery(){
        String instrumentName = (String) spinnerList.getValue();

            jdbcOracleConnection.getSrcdOfInstName(instrumentName);
            jdbcOracleConnection.executeQueryOrderableNames();
            //Function to fetch the orderable names for treeView
            setupUiTreeForOrderableList(instrumentName);

            dispDown = jdbcOracleConnection.executeQueryDispDown(instrumentName);
            collDown = jdbcOracleConnection.executeQueryCollDown(instrumentName);
            rMode = jdbcOracleConnection.executeQueryR_Mode(instrumentName);
            //Function To Change The Pictures (Green Tick ,Red Cross)
            changePictureAsPerStatus();
    }

    public int changePictureAsPerStatus(){
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
        return 1;
    }

    @FXML
    protected void handleInstNameChange(ActionEvent event) {
        //Function to execute all Queries
        executeUiUserQuery();
    }

    public void initialize (URL location, ResourceBundle resources){
        //Connect To Database
        jdbcOracleConnection.makeDatabaseConnection();
        jdbcOracleConnection.executeQueryInstrumentName();
        String[] cleanedInstNameArray = Arrays.stream(jdbcOracleConnection.instrumentNameArray).filter(Objects::nonNull).toArray(String[]::new);
        ObservableList<String> listOfInstruments = FXCollections.observableArrayList(cleanedInstNameArray);
        //Populates The Drop Down Menu (Combo Box)
        spinnerList.setItems(listOfInstruments);
        //Set The Waiting Icon
        cernerLogoUI.setImage(imageCernerLogo);
        dispatchDownloadStatusPic.setImage(imageHourGlass);
        collectedDownloadStatusPic.setImage(imageHourGlass);
        rModeStatusPic.setImage(imageHourGlass);
    }
}
