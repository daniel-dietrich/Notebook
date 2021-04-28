package main;

import controllers.MainPaneController;
import javafx.scene.image.Image;
import models.MainPaneModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Daniel Dietrich
 */
public class Notebook extends Application {

    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/MainPane.fxml"));
        fxmlLoader.setControllerFactory(t -> new MainPaneController(new MainPaneModel()));
        Image image = new Image("icon.png");
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("stylesheets/light-theme.css");

        stage.getIcons().add(image);
        stage.setTitle("Notebook");
        stage.setScene(scene);
        stage.show();

        Notebook.stage = stage;
    }

    public static Scene getScene() {
        return stage.getScene();
    }
}