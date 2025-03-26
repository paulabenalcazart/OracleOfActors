package ec.edu.uees.oracleofactors;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML private AnchorPane root;
    @FXML ImageView tmdbLogo;
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private Button minimizar, cerrar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            stage = (Stage) cerrar.getScene().getWindow();
            if (!stage.isFullScreen()) {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        });
        
        tmdbLogo.setOnMouseClicked(event -> {
        try {
            App.getHostServicesInstance().showDocument("https://www.themoviedb.org/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
    }
    
    @FXML
    private void cerrarStage() {
        stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizarStage() {
        stage = (Stage) minimizar.getScene().getWindow();
        stage.setIconified(true);
    }

}
