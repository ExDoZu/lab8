package zuev.nikita.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zuev.nikita.AuthorizationData;
import zuev.nikita.client.Main;
import zuev.nikita.client.net.Connection;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Authorization implements Initializable {
    @FXML
    private Text infoText;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField;
    @FXML
    private Text loginText;
    @FXML
    private Text passwordText;
    @FXML
    private Button signUpButton;
    @FXML
    private Button signInButton;

    @FXML
    private Button langButton;


    private ResourceBundle resourceBundle;

    public static AuthorizationData authorizationData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loginText.setText(resourceBundle.getString("login"));
        passwordText.setText(resourceBundle.getString("password"));
        signInButton.setText(resourceBundle.getString("signIn"));
        signUpButton.setText(resourceBundle.getString("signUp"));

    }


    public void openLanguageSelector(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/languageChanging.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxmlLoader.<LanguageChanging>getController().setOwnerFXML("authorization");

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) actionEvent.getTarget()).getScene().getWindow());
        stage.setTitle(resourceBundle.getString("languageChanging"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public void signUpHandle(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        tryAuthorize("registration", actionEvent);
    }

    public void signInHandle(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        tryAuthorize("login", actionEvent);

    }

    private void tryAuthorize(String signMethod, ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        infoText.setText("");
        authorizationData = new AuthorizationData(loginField.getText(), passwordField.getText());
        if ((authorizationData.getLogin().equals("") || authorizationData.getLogin() == null) ||
                (authorizationData.getPassword().equals("") || authorizationData.getPassword() == null)) {
            infoText.setText(resourceBundle.getString("emptyFields"));
        }

        Connection connection = null;
        try {
            connection = new Connection("localhost", 52300);
        } catch (IOException e) {
            infoText.setText(resourceBundle.getString("serverNotAvailable"));
            return;
        }

        SocketIO.setConnection(connection);
        ServerResponse serverResponse;
        try {
            SocketIO.write(new String[]{signMethod}, null, authorizationData, null);
            serverResponse = SocketIO.read();
        }catch (IOException e){
            infoText.setText(resourceBundle.getString("serverNotAvailable"));
            return;
        }
        if (serverResponse.getStatusCode() == ServerResponse.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/table.fxml"));
            fxmlLoader.setResources(resourceBundle);
            Scene scene = new Scene(fxmlLoader.load());
            ((Stage) ((Node) actionEvent.getTarget()).getScene().getWindow()).setTitle("");
            ((Stage) ((Node) actionEvent.getTarget()).getScene().getWindow()).setScene(scene);
        } else {
            connection.close();
            loginField.setText("");
            passwordField.setText("");
            if (serverResponse.getStatusCode() == ServerResponse.OLD_USER) {
                infoText.setText(resourceBundle.getString("userAlreadyExists"));
            } else {
                infoText.setText(resourceBundle.getString("wrongLoginOrPassword"));
            }
        }
    }
}
