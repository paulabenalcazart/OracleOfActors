package ec.edu.uees.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;
import javafx.scene.effect.DropShadow;
import java.util.stream.Collectors;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class AutoCompleteHelper {

    public static void enableAutoComplete(TextField textField, Set<String> dataSource) {
        ObservableList<String> items = FXCollections.observableArrayList(dataSource);
        ListView<String> suggestionList = new ListView<>();
        suggestionList.setMaxHeight(120);
        suggestionList.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: lightgray;");
        suggestionList.setEffect(new DropShadow());

        // Personalizar celdas con color al pasar el mouse
        suggestionList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setBackground(null);
                        } else {
                            setText(item);
                            setOnMouseEntered(e -> setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
                            setOnMouseExited(e -> setBackground(Background.EMPTY));
                        }
                    }
                };
            }
        });

        suggestionList.setOnMouseClicked(event -> {
            String selected = suggestionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                textField.setText(selected);
                hideSuggestions(suggestionList);
            }
        });

        textField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            String input = textField.getText().toLowerCase();
            if (input.isEmpty()) {
                hideSuggestions(suggestionList);
                return;
            }

            var filtered = items.stream()
                .filter(item -> item.toLowerCase().contains(input))
                .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                hideSuggestions(suggestionList);
                return;
            }

            suggestionList.setItems(FXCollections.observableArrayList(filtered));
            if (!((Pane) textField.getParent()).getChildren().contains(suggestionList)) {
                ((Pane) textField.getParent()).getChildren().add(suggestionList);
            }
            suggestionList.setLayoutX(textField.getLayoutX());
            suggestionList.setLayoutY(textField.getLayoutY() + textField.getHeight());
        });
    }

    private static void hideSuggestions(ListView<String> suggestionList) {
        if (suggestionList.getParent() instanceof Pane) {
            ((Pane) suggestionList.getParent()).getChildren().remove(suggestionList);
        }
    }
}