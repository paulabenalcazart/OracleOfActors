package ec.edu.uees.oracleofactors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.application.HostServices;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Scene scene;
    private static HostServices hostServices;

    @Override
    public void start(Stage stage) throws IOException {
        hostServices = getHostServices();
        scene = new Scene(loadFXML("main"));
        Image icon = new Image(getClass().getResourceAsStream("/images/logo.png"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static HostServices getHostServicesInstance() {
        return hostServices;
    }

    public static void main(String[] args) {
        launch();
    }

}