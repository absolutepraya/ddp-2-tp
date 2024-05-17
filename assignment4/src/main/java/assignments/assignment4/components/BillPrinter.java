package assignments.assignment4.components;

// import javafx.beans.property.SimpleStringProperty;
// import javafx.beans.property.StringProperty;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
import javafx.scene.Scene;
// import javafx.scene.canvas.Canvas;
// import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
// import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// import assignments.assignment3.assignment2copy.Menu;
import assignments.assignment3.assignment2copy.Order;
// import assignments.assignment3.assignment2copy.Restaurant;
// import assignments.assignment3.assignment2copy.User; 

import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.MemberMenu;

public class BillPrinter {
    // Just to access the createAlert method
    private class DummyMemberMenu extends MemberMenu {
        @Override
        protected Scene createBaseMenu() {
            return null;
        }
    }

    // private Stage stage;
    private MainApp mainApp;
    // private Scene scene;
    private Label billLabel;
    private DummyMemberMenu dummyMemberMenu = new DummyMemberMenu();

    public BillPrinter(Stage stage, MainApp mainApp, Scene scene) {
        // this.stage = stage;
        this.mainApp = mainApp;
        // this.scene = scene;
    }

    private Scene createBillPrinterForm() {
        VBox layout = new VBox();

        // Create the labels
        Label orderIDLabel = new Label("Order ID:");
        billLabel = new Label("   "); // Acts as spacer at first

        // Create the text field
        TextField orderIDInput = new TextField();

        // Create the buttons
        Button printBillButton = new Button("Print Bill");
        Button backButton = new Button("Kembali");

        // Set the button styles
        dummyMemberMenu.setLoginButtonStyle(printBillButton);
        dummyMemberMenu.setLogoutButtonStyle(backButton);

        // Add the components to the layout
        dummyMemberMenu.setUpVBoxLayout(false, layout, orderIDLabel, orderIDInput, printBillButton, billLabel, backButton);

        // Set the action for the buttons
        backButton.setOnAction(e -> {
            billLabel.setText("");
            mainApp.setCustomerScene();
        }); 
        printBillButton.setOnAction(e -> {
            printBill(orderIDInput.getText(), billLabel);
        });

        return new Scene(layout, 400, 600);
    }

    private void printBill(String orderID, Label billLabel) {
        // Check if the order ID is empty
        if (orderID.isEmpty()) {
            dummyMemberMenu.createAlert("Order ID error", "Order ID tidak boleh kosong!");
            return;
        }

        // Get the order
        Order order = DepeFood.getOrderOrNull(orderID);

        // Check if the order exists
        if (order == null) {
            dummyMemberMenu.createAlert("Order ID error", "Order ID tidak ditemukan!");
            return;
        }

        // Get the bill string
        String bill = DepeFood.generateBill(orderID);

        // Create label for the bill
        billLabel.setText(bill);

        return;
    }

    public Scene getScene() {
        return this.createBillPrinterForm();
    }

    // public void setScene(Scene scene) {
    //     this.scene = scene;
    // }
}
