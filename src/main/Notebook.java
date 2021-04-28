package main;

import controllers.MainPaneController;
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
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("stylesheets/light-theme.css");
        Notebook.stage = stage;

        stage.setTitle("Notebook");

        stage.setScene(scene);
        stage.show();
    }

    public static Scene getScene() {
        return stage.getScene();
    }
}