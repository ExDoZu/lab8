package zuev.nikita.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Coordinates;
import zuev.nikita.structure.Organization;
import zuev.nikita.structure.OrganizationType;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Edit implements Initializable {
    @FXML private Text keyText;
    @FXML private Text idText;
    @FXML private Text authorText;

    @FXML private Text creationDateText;
    @FXML private Text keyInfoText;
    @FXML private Text idInfoText;
    @FXML private Text authorInfoText;
    @FXML private Text creationDateInfoText;
    private Organization organization;
    private String key;

    public void setOrganization(Organization organization){
        this.organization=organization;

        nameField.setText(organization.getName());
        annualTurnoverField.setText(String.valueOf(organization.getAnnualTurnover()));
        addressField.setText(organization.getPostalAddress().getZipCode());
        xField.setText(String.valueOf(organization.getCoordinates().getX()));
        yField.setText(String.valueOf(organization.getCoordinates().getY()));
        typeChoice.getSelectionModel().select(resourceBundle.getString(organization.getType().toString()));

        idInfoText.setText(String.valueOf(organization.getId()));
        authorInfoText.setText(organization.getAuthor());
        creationDateInfoText.setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(organization.getCreationDate()));

    }
    public void setKey(String key){
        this.key=key;
        keyInfoText.setText(key);
    }
    @FXML
    private Button saveButton;
    @FXML private Text nameText;
    @FXML private Text typeText;
    @FXML private Text addressText;
    @FXML private Text coordText;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField yField;
    @FXML private ComboBox typeChoice;
    @FXML private Text annualTurnoverText;
    @FXML private TextField annualTurnoverField;
    @FXML private TextField xField;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resourceBundle=resources;
        typeChoice.getItems().add(resourceBundle.getString("COMMERCIAL"));
        typeChoice.getItems().add(resourceBundle.getString("PUBLIC"));
        typeChoice.getItems().add(resourceBundle.getString("TRUST"));
        typeChoice.getSelectionModel().selectFirst();

        keyText.setText("Key");
        idText.setText("ID");
        authorText.setText(resourceBundle.getString("author"));
        creationDateText.setText(resourceBundle.getString("creationDate"));
        saveButton.setText(resourceBundle.getString("save"));

        nameText.setText(resourceBundle.getString("name"));
        typeText.setText(resourceBundle.getString("organizationType"));
        addressText.setText(resourceBundle.getString("address"));
        coordText.setText(resourceBundle.getString("coordinates"));
        annualTurnoverText.setText(resourceBundle.getString("annualTurnover"));

        xField.setPromptText(resourceBundle.getString("integer")+" < 923");
        yField.setPromptText(resourceBundle.getString("float"));


    }

    public void saveHandle(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (!organization.getAuthor().equals(Authorization.authorizationData.getLogin())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("notYourElement"));
            alert.show();
            return;
        }


        nameField.setStyle("");
        addressField.setStyle("");
        annualTurnoverField.setStyle("");
        xField.setStyle("");
        yField.setStyle("");

        String name = nameField.getText();
        String address = addressField.getText();
        String annualTurnover = annualTurnoverField.getText();
        String x = xField.getText();
        String y = yField.getText();
        int type = typeChoice.getSelectionModel().getSelectedIndex();

        boolean valid = true;
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
        organization.setAuthor(this.organization.getAuthor());
        SocketIO.write(new String[]{"update" ,String.valueOf(this.organization.getId())}, organization, Authorization.authorizationData, null);

        ServerResponse serverResponse = SocketIO.read();

        Table.updateObservableList(serverResponse.getCollection());

        Stage stage = (Stage)saveButton.getScene().getWindow();
        stage.close();
    }

}
