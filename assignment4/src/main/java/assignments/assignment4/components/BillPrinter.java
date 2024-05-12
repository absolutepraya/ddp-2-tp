package assignments.assignment4.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import assignments.assignment3.assignment2copy.Menu;
import assignments.assignment3.assignment2copy.Order;
import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment3.assignment2copy.User;

import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;
    private Scene scene = mainApp.getScene("CustomerMenu");

    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    private Scene createBillPrinterForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15, 15, 15, 15));

        // Create the labels
        Label orderIDLabel = new Label("Order ID:");

        // Create the text field
        TextField orderIDInput = new TextField();

        // create the buttons
        Button printBillButton = new Button("Print Bill");
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        printBillButton.setMinWidth(150);
        backButton.setMinWidth(150);

        // Center the text in the buttons
        printBillButton.setAlignment(Pos.CENTER);
        backButton.setAlignment(Pos.CENTER);

        // Add the components to the layout
        layout.getChildren().addAll(orderIDLabel, orderIDInput, printBillButton, backButton);

        // Set the action for the buttons
        printBillButton.setOnAction(e -> {printBill(orderIDInput.getText());});
        backButton.setOnAction(e -> stage.setScene(scene));

        return new Scene(layout, 400, 200);
    }

    private void printBill(String orderID) {
        // // TODO: Print bill implementation
        // // Check if the order ID is empty
        // if (orderID.isEmpty()) {
        //     MainApp.createAlert("Order ID error", "Order ID tidak boleh kosong!");
        //     return;
        // }

        // // Get the order
        // Order order = DepeFood.getOrderOrNull(orderID);

        // // Check if the order exists
        // if (order == null) {
        //     MainApp.createAlert("Order ID error", "Order ID tidak ditemukan!");
        // }

        // String billString = Order.generateBill(orderID);

        return;
    }

    public Scene getScene() {
        return this.createBillPrinterForm();
    }

    // Class ini opsional
    // public class MenuItem {
    //     private final StringProperty itemName;
    //     private final StringProperty price;

    //     public MenuItem(String itemName, String price) {
    //         this.itemName = new SimpleStringProperty(itemName);
    //         this.price = new SimpleStringProperty(price);
    //     }

    //     public StringProperty itemNameProperty() {
    //         return itemName;
    //     }

    //     public StringProperty priceProperty() {
    //         return price;
    //     }

    //     public String getItemName() {
    //         return itemName.get();
    //     }

    //     public String getPrice() {
    //         return price.get();
    //     }
    // }
}
