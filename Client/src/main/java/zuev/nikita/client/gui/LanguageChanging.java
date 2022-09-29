package zuev.nikita.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import zuev.nikita.client.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageChanging {

    ResourceBundle bundle=ResourceBundle.getBundle("zuev.nikita.client.resources.Resource", Locale.getDefault());
    private String ownerFXMLName;


    private void changeSceneLocale(ActionEvent actionEvent) throws IOException {
        bundle=ResourceBundle.getBundle("zuev.nikita.client.resources.Resource", Locale.getDefault());
        Stage thisStage = (Stage)((Node)actionEvent.getTarget()).getScene().getWindow();
        Stage primaryStage = (Stage) thisStage.getOwner();
        thisStage.setTitle(bundle.getString("languageChanging"));

        if(!primaryStage.getTitle().isEmpty())
            primaryStage.setTitle(bundle.getString(ownerFXMLName));

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/"+ownerFXMLName+".fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);

    }
    public void setOwnerFXML(String ownerFXMLName){
        this.ownerFXMLName=ownerFXMLName;
    }

    public void changeLocaleRu(ActionEvent actionEvent) throws IOException {
        Locale.setDefault(new Locale("ru"));
        changeSceneLocale(actionEvent);
    }

    public void changeLocaleFr(ActionEvent actionEvent) throws IOException {
        Locale.setDefault(new Locale("fr"));
        changeSceneLocale(actionEvent);
    }

    public void changeLocaleEs(ActionEvent actionEvent) throws IOException {
        Locale.setDefault(new Locale("es", "PR"));
        changeSceneLocale(actionEvent);
    }

    public void changeLocaleRo(ActionEvent actionEvent) throws IOException {
        Locale.setDefault(new Locale("ro"));
        changeSceneLocale(actionEvent);

    }


}
