package assignments.assignment4.page;

import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.stream.Collectors;

import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment4.DepeFood;

public abstract class MemberMenu {
    private Scene scene;

    abstract protected Scene createBaseMenu();

    // Method to create an alert
    public void createAlert(String title, String contextText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // Hide the header
        alert.setContentText(contextText);
        alert.showAndWait();
        return;
    }

    // Method to create an OK alert
    public void createOK(String title, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Hide the header
        alert.setContentText(contextText);
        alert.showAndWait();
        return;
    }

    // Method to set up a vertical layout
    public void setUpVBoxLayout(VBox vbox, Node... elements) {
        // Set up the VBox
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.setAlignment(Pos.CENTER);

        // Add the elements to the VBox
        for (Node element : elements) {
            // If it's a button, set the width to 150px and center the text
            if (element instanceof Button) {
                ((Button) element).setMinWidth(150);
                ((Button) element).setAlignment(Pos.CENTER);
            }
            vbox.getChildren().add(element);
        }
    }

    // Method to set up the title font
    public void setTitleFont(Label title) {
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    }

    // Method to create a spacing
    public Region createSpacer(int height) {
        Region spacer = new Region();
        spacer.setPrefHeight(height);
        return spacer;
    }
    
    // Method to update the restaurant combo box available restaurants
    protected void refresh(ComboBox<String> restaurantComboBox) {
        restaurantComboBox.setItems(FXCollections.observableArrayList(getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList())));
    }

    // Method to update the menu items list view
    protected void refreshListView(ComboBox<String> restaurantComboBox, ListView<String> menuItemsListView, boolean withPrice) {
        Restaurant selectedRestaurant = getRestoList().get(restaurantComboBox.getSelectionModel().getSelectedIndex());
        if (withPrice) {
            menuItemsListView.setItems(FXCollections.observableArrayList(
                selectedRestaurant.getMenu().stream()
                    .map(Menu -> Menu.getNamaMakanan() + " - Rp" + Menu.getHarga())
                    .collect(Collectors.toList())));
        } else {
            
            menuItemsListView.setItems(FXCollections.observableArrayList(
                selectedRestaurant.getMenu().stream()
                    .map(Menu -> Menu.getNamaMakanan())
                    .collect(Collectors.toList())));
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    protected List<Restaurant> getRestoList() {
        return DepeFood.getRestoList();
    }

    protected void addRestoList(Restaurant resto) {
        DepeFood.addRestoList(resto);
    }
}
