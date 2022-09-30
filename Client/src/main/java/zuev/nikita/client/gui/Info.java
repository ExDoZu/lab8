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

public class Info implements Initializable {
    @FXML
    private Text keyText;
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
        typeChoice.setText(resourceBundle.getString(organization.getType().toString()));

        idInfoText.setText(String.valueOf(organization.getId()));
        authorInfoText.setText(organization.getAuthor());
        creationDateInfoText.setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(organization.getCreationDate()));

    }
    public void setKey(String key){
        this.key=key;
        keyInfoText.setText(key);
    }

    @FXML private Text nameText;
    @FXML private Text typeText;
    @FXML private Text addressText;
    @FXML private Text coordText;
    @FXML private Text nameField;
    @FXML private Text addressField;
    @FXML private Text yField;
    @FXML private Text typeChoice;
    @FXML private Text annualTurnoverText;
    @FXML private Text annualTurnoverField;
    @FXML private Text xField;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resourceBundle=resources;
        typeChoice.setText(resourceBundle.getString("organizationType"));

        keyText.setText("Key");
        idText.setText("ID");
        authorText.setText(resourceBundle.getString("author"));
        creationDateText.setText(resourceBundle.getString("creationDate"));


        nameText.setText(resourceBundle.getString("name"));
        typeText.setText(resourceBundle.getString("organizationType"));
        addressText.setText(resourceBundle.getString("address"));
        coordText.setText(resourceBundle.getString("coordinates"));
        annualTurnoverText.setText(resourceBundle.getString("annualTurnover"));


    }

}
