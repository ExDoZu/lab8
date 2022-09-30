package zuev.nikita.client.gui;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zuev.nikita.client.Main;
import zuev.nikita.client.PropOrganization;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Coordinates;
import zuev.nikita.structure.Organization;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class Table implements Initializable {

    @FXML
    private MenuItem insertItem;
    @FXML
    private MenuItem editItem;
    @FXML
    private MenuItem removeItem;
    @FXML
    private MenuItem clearItem;
    @FXML
    private MenuItem removeLowerItem;
    @FXML
    private MenuItem removeGreaterKeyItem;
    @FXML
    private MenuItem filterGTPA;

    @FXML private MenuItem sortAsc;
    @FXML private MenuItem sortDesc;
    @FXML
    private TableColumn<PropOrganization, String> key;
    @FXML
    private TableColumn<PropOrganization, Integer> id;
    @FXML
    private TableColumn<PropOrganization, String> author;
    @FXML
    private TableColumn<PropOrganization, String> name;
    @FXML
    private TableColumn coordinates;
    @FXML
    private TableColumn<PropOrganization, Double> y;
    @FXML
    private TableColumn<PropOrganization, Long> x;
    @FXML
    private TableColumn<PropOrganization, String> creationDate;
    @FXML
    private TableColumn<PropOrganization, Double> annualTurnover;
    @FXML
    private TableColumn<PropOrganization, String> organizationType;
    @FXML
    private TableColumn<PropOrganization, String> address;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private Button showButton;

    @FXML
    private Button langButton;
    @FXML
    private Text loginText;
    @FXML
    private TableView<PropOrganization> table;

    public static volatile ObservableList<PropOrganization> organizations = FXCollections.observableArrayList();
    private static final ObservableList<PropOrganization> newOrganizations = FXCollections.observableArrayList();
    public static volatile boolean autoSyncCollection = false;

    private static ResourceBundle resourceBundle;


    public void openLanguageSelector(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/languageChanging.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxmlLoader.<LanguageChanging>getController().setOwnerFXML("table");

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) actionEvent.getTarget()).getScene().getWindow());

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Table.resourceBundle = resourceBundle;
        try {
            loadCollection();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        pingServer();

        loginText.setText(Authorization.authorizationData.getLogin());

        showButton.setText(resourceBundle.getString("show"));


        id.setCellValueFactory((cellData) -> cellData.getValue().idProperty().asObject());
        key.setCellValueFactory((cellData) -> cellData.getValue().keyProperty());

        author.setText(resourceBundle.getString("author"));
        author.setCellValueFactory((cellData) -> cellData.getValue().authorProperty());

        name.setText(resourceBundle.getString("name"));
        name.setCellValueFactory((cellData) -> cellData.getValue().nameProperty());

        coordinates.setText(resourceBundle.getString("coordinates"));
        x.setCellValueFactory((cellData) -> cellData.getValue().xProperty().asObject());
        y.setCellValueFactory((cellData) -> cellData.getValue().yProperty().asObject());

        creationDate.setText(resourceBundle.getString("creationDate"));
        creationDate.setCellValueFactory((cellData) -> cellData.getValue().creationDateProperty());

        annualTurnover.setText(resourceBundle.getString("annualTurnover"));
        annualTurnover.setCellValueFactory((cellData) -> cellData.getValue().annualTurnoverProperty().asObject());

        organizationType.setText(resourceBundle.getString("organizationType"));
        organizationType.setCellValueFactory((cellData) -> cellData.getValue().organizationTypeProperty());

        address.setText(resourceBundle.getString("address"));
        address.setCellValueFactory((cellData) -> cellData.getValue().postalAddressProperty());

        table.setItems(organizations);

        insertItem.setText(resourceBundle.getString("insert"));
        editItem.setText(resourceBundle.getString("edit"));
        removeItem.setText(resourceBundle.getString("remove"));
        clearItem.setText(resourceBundle.getString("clear"));
        removeLowerItem.setText(resourceBundle.getString("removeLower"));
        removeGreaterKeyItem.setText(resourceBundle.getString("removeGreaterKey"));
        filterGTPA.setText(resourceBundle.getString("filterGTPA"));
        sortAsc.setText(resourceBundle.getString("sortAsc"));
        sortDesc.setText(resourceBundle.getString("sortDesc"));;

    }

    private void pingServer() {
        Platform.runLater(() -> {
            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), event -> {
                try {
                    if (autoSyncCollection){
                        loadCollection();}
                    else {
                        SocketIO.write(new String[]{"ping"}, null, Authorization.authorizationData, null);
                        SocketIO.read();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/authorization.fxml"));
                    fxmlLoader.setResources(resourceBundle);

                    Scene scene;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Stage stage = ((Stage) table.getScene().getWindow());
                    stage.setTitle(resourceBundle.getString("authorization"));
                    stage.setResizable(false);
                    stage.setScene(scene);
                    timeline.stop();
                }
            }));
            timeline.play();
        });
    }

    public synchronized static void loadCollection() throws IOException, ClassNotFoundException {
        SocketIO.write(new String[]{"show"}, null, Authorization.authorizationData, null);
        ServerResponse serverResponse = SocketIO.read();
        updateObservableList(serverResponse.getCollection());
    }

    public synchronized static void updateObservableList(Map<String, Organization> collection) {
        newOrganizations.clear();
        collection.entrySet().stream().map(e -> {
            return new PropOrganization(e.getKey(),
                    e.getValue().getId(),
                    e.getValue().getAuthor(),
                    e.getValue().getName(),
                    e.getValue().getCoordinates().getX(),
                    e.getValue().getCoordinates().getY(),
                    e.getValue().getCreationDate(),
                    e.getValue().getAnnualTurnover(),
                    e.getValue().getType(),
                    e.getValue().getPostalAddress(),
                    resourceBundle
            );
        }).forEach(newOrganizations::add);
        if (!organizations.equals(newOrganizations)) {
            organizations.setAll(newOrganizations);
        }
    }

    public void showButtonHandle() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/show.fxml"));
            fxmlLoader.setResources(resourceBundle);
            Scene scene = new Scene(fxmlLoader.load());

            stage.initOwner(table.getScene().getWindow());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                autoSyncCollection = false;
            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertHandle() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/insert.fxml"));
            fxmlLoader.setResources(resourceBundle);
            Scene scene = new Scene(fxmlLoader.load());
            fxmlLoader.<Insert>getController().setOwnerFXML("table");

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(table.getScene().getWindow());
            stage.setTitle(resourceBundle.getString("newElement"));
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean checkSelected(){
        if (table.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("notChooseElement"));
            alert.show();
            return false;
        }
        return true;
    }
    private boolean checkAuthor(){
        if (!table.getSelectionModel().getSelectedItem().getAuthor().equals(Authorization.authorizationData.getLogin())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("notYourElement"));
            alert.show();
            return false;
        }
        return true;
    }

    public void editHandle() {
        if (!checkSelected())return;
        if(!checkAuthor())return;

        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/edit.fxml"));
            fxmlLoader.setResources(resourceBundle);
            Scene scene = new Scene(fxmlLoader.load());

            Organization organization = new Organization(
                    table.getSelectionModel().getSelectedItem().getId(),
                    table.getSelectionModel().getSelectedItem().getName(),
                    new Coordinates(table.getSelectionModel().getSelectedItem().getX(), table.getSelectionModel().getSelectedItem().getY()),
                    table.getSelectionModel().getSelectedItem().getCreationDate(),
                    table.getSelectionModel().getSelectedItem().getAnnualTurnover(),
                    table.getSelectionModel().getSelectedItem().getOrganizationTypeAsEnum(),
                    new Address(table.getSelectionModel().getSelectedItem().getPostalAddress())
            );
            organization.setAuthor(table.getSelectionModel().getSelectedItem().getAuthor());
            fxmlLoader.<Edit>getController().setOrganization(organization);
            fxmlLoader.<Edit>getController().setKey(table.getSelectionModel().getSelectedItem().getKey());

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(table.getScene().getWindow());

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeHandle() {
        if (!checkSelected())return;
        if(!checkAuthor())return;
        try {
            SocketIO.write(new String[]{"remove_key", table.getSelectionModel().getSelectedItem().getKey()}, null, Authorization.authorizationData, null);
            ServerResponse serverResponse = SocketIO.read();
            if (serverResponse.getCollection() == null) {
                return;
            }
            updateObservableList(serverResponse.getCollection());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearHandle() {
        try {
            SocketIO.write(new String[]{"clear"}, null, Authorization.authorizationData, null);
            ServerResponse serverResponse = SocketIO.read();
            if (serverResponse.getCollection() == null) {
                return;
            }
            updateObservableList(serverResponse.getCollection());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeLowerHandle() {
        if (!checkSelected())return;
        try {

            Organization organization = new Organization(
                    table.getSelectionModel().getSelectedItem().getId(),
                    table.getSelectionModel().getSelectedItem().getName(),
                    new Coordinates(table.getSelectionModel().getSelectedItem().getX(), table.getSelectionModel().getSelectedItem().getY()),
                    table.getSelectionModel().getSelectedItem().getCreationDate(),
                    table.getSelectionModel().getSelectedItem().getAnnualTurnover(),
                    table.getSelectionModel().getSelectedItem().getOrganizationTypeAsEnum(),
                    new Address(table.getSelectionModel().getSelectedItem().getPostalAddress())
            );
            SocketIO.write(new String[]{"remove_lower"}, organization, Authorization.authorizationData, null);
            ServerResponse serverResponse = SocketIO.read();
            if (serverResponse.getCollection() == null) {
                return;
            }
            updateObservableList(serverResponse.getCollection());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeGreaterKeyHandle() {
        if (!checkSelected())return;
        try {
            SocketIO.write(new String[]{"remove_greater_key", table.getSelectionModel().getSelectedItem().getKey()}, null, Authorization.authorizationData, null);
            ServerResponse serverResponse = SocketIO.read();
            if (serverResponse.getCollection() == null) {
                return;
            }
            updateObservableList(serverResponse.getCollection());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void filterGreaterThanPostalAddress() {
        if (!checkSelected())return;
        String filter = table.getSelectionModel().getSelectedItem().getPostalAddress();
        ObservableList<PropOrganization> organizationsCopy = FXCollections.observableArrayList(organizations);
        organizations.clear();
        organizationsCopy.stream().filter(e -> e.getPostalAddress().compareTo(filter) > 0).forEach(organizations::add);

    }

    public void sortAscending(){
        ObservableList<PropOrganization> organizationsCopy = FXCollections.observableArrayList(organizations);
        organizations.clear();
        organizationsCopy.stream().sorted().forEach(organizations::add);
    }
    public void sortDescending(){
        ObservableList<PropOrganization> organizationsCopy = FXCollections.observableArrayList(organizations);
        organizations.clear();
        organizationsCopy.stream().sorted(Comparator.reverseOrder()).forEach(organizations::add);
    }
}
