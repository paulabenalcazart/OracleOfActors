package ec.edu.uees.oracleofactors;

import ec.edu.uees.controller.ActorManager;
import ec.edu.uees.controller.AutoCompleteHelper;
import ec.edu.uees.model.ActorContext;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable {

    @FXML private AnchorPane root, bannerPane;
    @FXML ImageView tmdbLogo, maintitle, banner1;
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private Button minimizar, cerrar;
    @FXML private TextField textfield1, textfield2;
    @FXML private Label labelWelcome,labelCredits, labelHIW, labelFabian, labelPaula, labelOracle;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(textfield1 != null && textfield2 != null) {
           Set<String> actores = new ActorManager().leerActores();
            AutoCompleteHelper.enableAutoComplete(textfield1, actores);
            AutoCompleteHelper.enableAutoComplete(textfield2, actores);
        }
        
        if(bannerPane.getChildren().size() == 1){
            ImageView banner2 = new ImageView(new Image(getClass().getResourceAsStream("/images/banner.png")));
            banner2.setLayoutX(970);
            banner2.setOpacity(0.4);
            bannerPane.getChildren().add(banner2);
            moveBanner(banner1).play();
            moveBanner(banner2).play();
        }
        
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
        
        maintitle.setOnMouseClicked(e -> {
            try {
                App.setRoot("main");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        labelWelcome.setOnMouseClicked(e -> {
            try {
                App.setRoot("welcome");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        labelCredits.setOnMouseClicked(e -> {
            try {
                App.setRoot("credits");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        labelHIW.setOnMouseClicked(e -> {
            try {
                App.setRoot("howitworks");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        tmdbLogo.setOnMouseClicked(event -> {
        try {
            App.getHostServicesInstance().showDocument("https://www.themoviedb.org/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
        
        if(labelFabian != null || labelPaula != null || labelOracle != null) {
            labelFabian.setOnMouseClicked(event -> {
            try {
                App.getHostServicesInstance().showDocument("https://github.com/Fabianrodas");
            } catch (Exception e) {
                e.printStackTrace();
            }
            });    
            labelPaula.setOnMouseClicked(event -> {
            try {
                App.getHostServicesInstance().showDocument("https://github.com/paulabenalcazart");
            } catch (Exception e) {
                e.printStackTrace();
            }
            });

            labelOracle.setOnMouseClicked(event -> {
            try {
                App.getHostServicesInstance().showDocument("https://oracleofbacon.org");
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
        }
    }
    
    private TranslateTransition moveBanner(ImageView iv) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(10), iv);
        tt.setFromX(0);
        tt.setToX(-970);
        tt.setInterpolator(javafx.animation.Interpolator.LINEAR);
        tt.setCycleCount(javafx.animation.Animation.INDEFINITE);
        return tt;
    }
    
    @FXML
    private void findLink() throws IOException {
        if (textfield1.getText().isBlank() || textfield2.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Fatal Error");
            alert.setContentText("Please fill the blanks with actors.");
            alert.showAndWait();
            return;
        }
        ActorContext.actor1 = textfield1.getText().trim();
        ActorContext.actor2 = textfield2.getText().trim();
        App.setRoot("actorlinks");
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