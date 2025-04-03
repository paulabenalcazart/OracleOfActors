package ec.edu.uees.oracleofactors;

import ec.edu.uees.controller.ActorManager;
import ec.edu.uees.controller.AutoCompleteHelper;
import ec.edu.uees.controller.GraphExpander;
import ec.edu.uees.controller.GraphLoader;
import ec.edu.uees.controller.TMDBConnection;
import ec.edu.uees.model.ActorContext;
import ec.edu.uees.model.ConfigLoader;
import ec.edu.uees.model.GraphLA;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ActorLinksController implements Initializable {

    @FXML private AnchorPane root, bannerPane;
    @FXML ImageView tmdbLogo, maintitle, banner1;
    private Stage stage;
    private ScrollPane scrollPane;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private Button minimizar, cerrar;
    @FXML private TextField textfield1, textfield2;
    @FXML private Label labelWelcome,labelCredits, labelHIW, labelFabian, labelPaula, labelOracle;
    @FXML private VBox container;
    @FXML private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Set<String> actores = new ActorManager().leerActores();
        AutoCompleteHelper.enableAutoComplete(textfield1, actores);
        AutoCompleteHelper.enableAutoComplete(textfield2, actores);
        
        Platform.runLater(() -> linkActors(ActorContext.actor1, ActorContext.actor2));
        
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
    
    private String capitalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String[] partes = nombre.trim().toLowerCase().split(" ");
        StringBuilder capitalizado = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                capitalizado.append(Character.toUpperCase(parte.charAt(0)))
                            .append(parte.substring(1))
                            .append(" ");
            }
        }
        return capitalizado.toString().trim();
    }
    
    private void linkActors(String actor1, String actor2) {
        final String Actor1 = capitalizarNombre(actor1);
        final String Actor2 = capitalizarNombre(actor2);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String apiKey = ConfigLoader.getTmdbApiKey();
                TMDBConnection tmdb = new TMDBConnection(apiKey);
                ActorManager actorManager = new ActorManager();
                GraphLoader graphLoader = new GraphLoader();
                GraphLA<String> grafo = graphLoader.cargarGrafo();

                if (grafo == null) grafo = new GraphLA<>(false);
                GraphExpander expander = new GraphExpander(tmdb, actorManager, graphLoader);

                boolean exito1 = actorManager.contieneActor(Actor1) || expander.agregarActorAlGrafo(grafo, Actor1);
                boolean exito2 = actorManager.contieneActor(Actor2) || expander.agregarActorAlGrafo(grafo, Actor2);

                if (exito1 && exito2 && grafo.getPeliculaEntre(Actor1, Actor2) == null) {
                    String id1 = tmdb.buscarActorId(Actor1);
                    String id2 = tmdb.buscarActorId(actor2);

                    if (id1 != null && id2 != null) {
                        List<String> pelis1 = tmdb.obtenerPeliculas(id1);
                        List<String> pelis2 = tmdb.obtenerPeliculas(id2);
                        for (String pid : pelis1) {
                            if (pelis2.contains(pid)) {
                                String nombre = tmdb.obtenerNombrePelicula(pid);
                                grafo.addEdge(Actor1, Actor2, 1, nombre);
                                graphLoader.guardarGrafo(grafo);
                                System.out.println("Conexión directa forzada: " + Actor1 + " ↔ " + Actor2 + " por " + nombre);
                                break;
                            }
                        }
                    }
                }

                List<String> camino = grafo.caminoMinimo(Actor1, Actor2);
                final GraphLA<String> grafofinal = grafo;

                Platform.runLater(() -> {
                    progressIndicator.setVisible(false); // Ocultar al terminar
                    if (camino == null || camino.isEmpty()) {
                        mostrarError("No se encontró un camino entre " + Actor1 + " y " + Actor2);
                    } else {
                        mostrarRutaVisual(camino, grafofinal);
                    }
                });

                return null;
            }
        };
        container.getChildren().clear();
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(-1); // Modo indeterminado

        new Thread(task).start();
    }

    private void mostrarRutaVisual(List<String> camino, GraphLA<String> grafo) {
    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);

    for (int i = 0; i < camino.size(); i++) {
        String actor = camino.get(i);
        Label actorLabel = new Label(actor);
        actorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black; "
                + "-fx-background-color: #a8e6a1; -fx-padding: 10px 20px; -fx-border-radius: 10; "
                + "-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.5, 0, 1);");
        actorLabel.setAlignment(Pos.CENTER);
        vbox.getChildren().add(actorLabel);

        if (i < camino.size() - 1) {
            Label arrow = new Label("↓");
            arrow.setStyle("-fx-font-size: 20px; -fx-text-fill: gray;");
            vbox.getChildren().add(arrow);

            String nextActor = camino.get(i + 1);
            String pelicula = grafo.getPeliculaEntre(actor, nextActor);
            if (pelicula == null) pelicula = "Unknown Movie";

            Label movieLabel = new Label(pelicula);
            movieLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-background-color: #8faaff; "
                    + "-fx-padding: 10px 20px; -fx-border-radius: 10; -fx-background-radius: 10; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0.5, 0, 1);");
            movieLabel.setAlignment(Pos.CENTER);
            vbox.getChildren().add(movieLabel);

            Label arrow2 = new Label("↓");
            arrow2.setStyle("-fx-font-size: 20px; -fx-text-fill: gray;");
            vbox.getChildren().add(arrow2);
        }
    }

    // Crear ScrollPane dinámico
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vbox);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    scrollPane.getStyleClass().add("scroll-pane");

    // Estilo visible (opcional si usas CSS externo)
    scrollPane.lookupAll(".scroll-bar").forEach(bar -> {
        bar.setStyle("-fx-background-color: transparent;");
    });

    // Scroll animado
    scrollPane.setOnScroll(event -> {
        double deltaY = event.getDeltaY() * 0.005;
        scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
    });

    // Centrado vertical si hay espacio
    VBox wrapper = new VBox(scrollPane);
    wrapper.setAlignment(Pos.CENTER);
    wrapper.setFillWidth(true);

    // Reemplazar contenido
    container.getChildren().clear();
    container.getChildren().add(wrapper);
}


    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void handleFindAgain() {
        String a1 = textfield1.getText().trim();
        String a2 = textfield2.getText().trim();
        if (a1.isEmpty() || a2.isEmpty()) {
            mostrarError("Please enter both actor names.");
            return;
        }
        linkActors(a1, a2);
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
