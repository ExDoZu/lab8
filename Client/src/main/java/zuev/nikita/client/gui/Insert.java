package zuev.nikita.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Coordinates;
import zuev.nikita.structure.Organization;
import zuev.nikita.structure.OrganizationType;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class Insert implements Initializable {

    @FXML private Button saveButton;
    @FXML private Text keyText;
    @FXML private Text nameText;
    @FXML private Text typeText;
    @FXML private Text addressText;
    @FXML private Text coordText;
    @FXML private TextField keyField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField yField;
    @FXML private ComboBox typeChoice;
    @FXML private Text annualTurnoverText;
    @FXML private TextField annualTurnoverField;
    @FXML private TextField xField;
    private String ownerFXMLName;
    public void setOwnerFXML(String ownerFXMLName){
        this.ownerFXMLName=ownerFXMLName;
    }

    private ResourceBundle resourceBundle;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle=resources;
        typeChoice.getItems().add(resourceBundle.getString("COMMERCIAL"));
        typeChoice.getItems().add(resourceBundle.getString("PUBLIC"));
        typeChoice.getItems().add(resourceBundle.getString("TRUST"));
        typeChoice.getSelectionModel().selectFirst();



        saveButton.setText(resourceBundle.getString("save"));

        keyText.setText("key");
        nameText.setText(resourceBundle.getString("name"));
        typeText.setText(resourceBundle.getString("organizationType"));
        addressText.setText(resourceBundle.getString("address"));
        coordText.setText(resourceBundle.getString("coordinates"));
        annualTurnoverText.setText(resourceBundle.getString("annualTurnover"));

        xField.setPromptText(resourceBundle.getString("integer")+" < 923");
        yField.setPromptText(resourceBundle.getString("float"));


    }

    public void saveHandle(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        nameField.setStyle("");
        addressField.setStyle("");
        annualTurnoverField.setStyle("");
        xField.setStyle("");
        yField.setStyle("");
        keyField.setStyle("");

        String key = keyField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String annualTurnover = annualTurnoverField.getText();
        String x = xField.getText();
        String y = yField.getText();
        int type = typeChoice.getSelectionModel().getSelectedIndex();

        boolean valid = true;

        if(key.equals("")) {
            keyField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        if(name.equals("")) {
            nameField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        if(address.equals("")) {
            addressField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        if(annualTurnover.equals("")) {
            annualTurnoverField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        try{
            Double.parseDouble(annualTurnover);
        }catch (NumberFormatException e){
            annualTurnoverField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        if(x.equals("")) {
            xField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        try {
            if(Long.parseLong(x)>=923){
                xField.setStyle("-fx-control-inner-background: #ff0000");
                valid = false;
            }
        }catch (NumberFormatException e){
            xField.setStyle("-fx-control-inner-background: #ff0000");
            valid = false;
        }

        if(y.equals("")) {
            yField.setStyle("-fx-control-inner-background: #ff0000");
            valid=false;
        }
        try {
            Double.parseDouble(y);
        }catch (NumberFormatException e){
            yField.setStyle("-fx-control-inner-background: #ff0000");
            valid = false;
        }

        if(!valid) return;

        Organization organization = new Organization(-1, name, new Coordinates(Long.parseLong(x), Double.parseDouble(y)), new Date(), Double.parseDouble(annualTurnover), OrganizationType.values()[type],new Address(address));
        SocketIO.write(new String[]{"insert" ,key}, organization, Authorization.authorizationData, null);

        ServerResponse serverResponse = SocketIO.read();

        Table.updateObservableList(serverResponse.getCollection());

        Stage stage = (Stage)saveButton.getScene().getWindow();
        stage.close();
    }

}
