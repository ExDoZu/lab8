package zuev.nikita.client.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zuev.nikita.client.Main;
import zuev.nikita.client.PropOrganization;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Coordinates;
import zuev.nikita.structure.Organization;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Show implements Initializable {

    private volatile double maxX;
    private volatile double maxY;




    @FXML
    private volatile Canvas canvas;

    private volatile GraphicsContext context;
    private volatile Map<String, Color> colors = new HashMap<>();
    private volatile ObservableList<PropOrganization> oldOrgs = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Table.autoSyncCollection = true;
        context = canvas.getGraphicsContext2D();
        draw();
        canvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            for(int i = oldOrgs.size()-1; i>=0; i--){
                double orgX = oldOrgs.get(i).getX()*(canvas.getWidth()-100) / maxX;
                double orgY = oldOrgs.get(i).getY()*(canvas.getHeight()-100) / maxY;
                if(x>=orgX && x<=orgX+80 && y>=orgY && y<=orgY+80){
                    try {


                        Organization organization =new Organization(oldOrgs.get(i).getId(), oldOrgs.get(i).getName(), new Coordinates(oldOrgs.get(i).getX(), oldOrgs.get(i).getY()), oldOrgs.get(i).getCreationDate(), oldOrgs.get(i).getAnnualTurnover(), oldOrgs.get(i).getOrganizationTypeAsEnum(), new Address(oldOrgs.get(i).getPostalAddress()));
                        if(Authorization.authorizationData.getLogin().equals(oldOrgs.get(i).getAuthor())){
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/edit.fxml"));
                            fxmlLoader.setResources(resources);
                            Scene scene = new Scene(fxmlLoader.load());
                            organization.setAuthor(oldOrgs.get(i).getAuthor());
                            fxmlLoader.<Edit>getController().setOrganization(organization);
                            fxmlLoader.<Edit>getController().setKey(oldOrgs.get(i).getKey());
                            Stage stage = new Stage();
                            stage.setResizable(false);
                            stage.setScene(scene);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.show();
                        }else{
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/info.fxml"));
                            fxmlLoader.setResources(resources);
                            Scene scene = new Scene(fxmlLoader.load());
                            organization.setAuthor(oldOrgs.get(i).getAuthor());
                            fxmlLoader.<Info>getController().setOrganization(organization);
                            fxmlLoader.<Info>getController().setKey(oldOrgs.get(i).getKey());
                            Stage stage = new Stage();
                            stage.setResizable(false);
                            stage.setScene(scene);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.show();
                        }
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void draw(){
        Platform.runLater(() -> {
            Timeline timeline = new Timeline();
            timeline.setCycleCount(-1);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), event -> {
                context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                fillColorsMap();
                drawOrgs();
                if(!Table.autoSyncCollection) timeline.stop();
            }));
            timeline.play();
        });
    }
    private void fillColorsMap() {
        int size = Table.organizations.size();
        for (int i = 0; i < size; i++) {
            if (!colors.containsKey(Table.organizations.get(i).getAuthor())) {
                colors.put(Table.organizations.get(i).getAuthor(), Color.color(Math.random(), Math.random(), Math.random()));
            }
        }
    }

    private void drawOrgs() {

        double locMaxX=0;
        double locMaxY=0;


        for (int i = 0; i<Table.organizations.size();i++) {
            if(Table.organizations.get(i).getX()>locMaxX){
                locMaxX=Table.organizations.get(i).getX();
            }
            if(Table.organizations.get(i).getY()>locMaxY){
                locMaxY=Table.organizations.get(i).getY();
            }
        }
        if(Table.organizations.size()>1  || maxX==0) {
            maxX = locMaxX;
            maxY = locMaxY;
        }

        boolean sthWasDeleted = false;
        for (PropOrganization oldOrg : oldOrgs) {
            if (!Table.organizations.contains(oldOrg)) {
                drawDelOrg(oldOrg);
                sthWasDeleted = true;
            }
        }


        for(PropOrganization propOrganization : Table.organizations){
            if(!oldOrgs.contains(propOrganization)){
                drawNewOrg(propOrganization);
            }else{
                drawOrg(propOrganization, 1.0);
            }

        }

        oldOrgs.setAll(Table.organizations);
    }


    private void drawDelOrg(PropOrganization org){
        AtomicReference<Double> coef = new AtomicReference<>( 1.0);
        Platform.runLater(() -> {
            Timeline timeline = new Timeline();
            timeline.setCycleCount(20);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), event -> {
                drawOrg(org, coef.get());
                coef.updateAndGet(v -> v - 0.05);
            }));
            timeline.play();
        });
    }

    private void drawNewOrg(PropOrganization org) {
        AtomicReference<Double> coef = new AtomicReference<>( 0.0);
        Platform.runLater(() -> {
            Timeline timeline = new Timeline();
            timeline.setCycleCount(20);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), event -> {
                drawOrg(org, coef.get());
                coef.updateAndGet(v -> v + 0.05);
            }));
            timeline.play();
        });
    }

    private void drawOrg(PropOrganization org, double sizeCoef) {
        double coefX = (canvas.getWidth()-100) / maxX;
        double coefY = (canvas.getHeight()-100) / maxY;

        double width = 80 * sizeCoef;
        double height = width;
        double sizeDeltaX = 40-40 * sizeCoef;
        double sizeDeltaY = sizeDeltaX;

        double x = ((double)org.getX()) * coefX;
        double y = org.getY() * coefY;

        Paint p;
        context.setFill(colors.get(org.getAuthor()));
        switch (org.getOrganizationTypeAsEnum()) {
            case COMMERCIAL:
                context.clearRect(sizeDeltaX+x, sizeDeltaY+y, width, width);
                context.fillRect(sizeDeltaX+x, sizeDeltaY+y, width, width);
                context.setFill(Color.BLACK);
                context.setFont(new Font(80*sizeCoef));
                context.fillText("$", x + 18, y + 67);
                break;
            case PUBLIC:
                p = context.getFill();
                context.setFill(Color.WHITE);
                context.fillOval(sizeDeltaX+x, sizeDeltaY+y, width, height);
                context.setFill(p);
                Image imageP = new Image("zuev/nikita/client/gui/images/person.png");
                context.fillOval(sizeDeltaX+x, sizeDeltaY+y, width, height);
                context.drawImage(imageP, x+20, y+10, 40*sizeCoef, 60*sizeCoef);
                break;
            case TRUST:
                p = context.getFill();
                context.setFill(Color.WHITE);
                context.fillPolygon(new double[]{x+40*sizeCoef, x + 80*sizeCoef, x}, new double[]{y, y + 60*sizeCoef, y + 60*sizeCoef}, 3);
                context.setFill(p);
                Image imageT = new Image("zuev/nikita/client/gui/images/trust.png");
                context.fillPolygon(new double[]{x+40*sizeCoef, x + 80*sizeCoef, x}, new double[]{y, y + 60*sizeCoef, y + 60*sizeCoef}, 3);
                context.drawImage(imageT, x+20, y+20, 40*sizeCoef, 40*sizeCoef);
                break;
        }
    }

}
