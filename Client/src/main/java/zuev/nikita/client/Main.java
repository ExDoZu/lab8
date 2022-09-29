package zuev.nikita.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Locale.setDefault(new Locale("ru"));



        ResourceBundle bundle = ResourceBundle.getBundle("zuev.nikita.client.resources.Resource", Locale.getDefault());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/authorization.fxml"));
        fxmlLoader.setResources(bundle);

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle(bundle.getString("authorization"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}