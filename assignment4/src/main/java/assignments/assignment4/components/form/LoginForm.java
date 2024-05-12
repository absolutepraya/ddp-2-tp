package assignments.assignment4.components.form;

// import assignments.assignment3.assignment2copy.Menu;
// import assignments.assignment3.assignment2copy.Order;
// import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment3.assignment2copy.User;


// import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// import javafx.geometry.VPos;
import javafx.scene.Scene;
// import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;

import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;

// import java.util.function.Consumer;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp; // MainApp instance
    private TextField nameInput;
    private TextField phoneInput;

    public LoginForm(Stage stage, MainApp mainApp) { // Pass MainApp instance to constructor
        this.stage = stage;
        this.mainApp = mainApp; // Store MainApp instance
    }

    private Scene createLoginForm() {
        GridPane grid = new GridPane();

        // Set column constraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);

        // Set the pane details
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Create the labels
        Label welcomeLabel = new Label("Welcome to DepeFood");
        Label nameLabel = new Label("Name:");
        Label phoneLabel = new Label("Phone Number:");

        // Create the text fields
        nameInput = new TextField();
        phoneInput = new TextField();

        // Create the login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());

        // Put the components in the grid
        grid.add(welcomeLabel, 0, 0, 2, 1);
        grid.add(nameLabel, 0, 1);
        grid.add(nameInput, 1, 1);
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneInput, 1, 2);
        grid.add(loginButton, 1, 3, 2, 1);

        return new Scene(grid, 400, 600);
    }


    private void handleLogin() {
        // Get the name and phone number from the input fields
        String name = nameInput.getText();
        String phone = phoneInput.getText();

        // Check if the name and phone number are empty
        if (name.isEmpty() || phone.isEmpty()) {
            MainApp.createAlert("Login Failed", "Nama dan nomor telepon tidak boleh kosong!");
            return;
        }

        // Check if the phone number is not a number
        if (!phone.matches("[0-9]+")) {
            MainApp.createAlert("Login Failed", "Nomor telepon harus berupa angka!");
            return;
        }

        // Use the DepeFood class to get the user
        User user = DepeFood.getUser(name, phone);

        // Check if the user is not found
        if (user == null) {
            MainApp.createAlert("Login Failed", "User tidak ditemukan!");
            return;
        }

        // Set the user in the MainApp instance
        mainApp.setUser(user);

        // Initialize and populate adminMenuScene [DEBUG]
        Scene adminMenuScene = new AdminMenu(mainApp.getWindow(), mainApp).getScene();
        if (adminMenuScene != null) {
            System.out.println("AdminMenu scene created successfully");
        } else {
            System.out.println("Failed to create AdminMenu scene");
        }
        mainApp.addScene("AdminMenu", adminMenuScene);
        // Initialize and populate adminMenuScene [DEBUG]
        Scene customerMenuScene = new CustomerMenu(mainApp.getWindow(), mainApp, user).getScene();
        if (customerMenuScene != null) {
            System.out.println("CustomerMenu scene created successfully");
        } else {
            System.out.println("Failed to create CustomerMenu scene");
        }
        mainApp.addScene("CustomerMenu", customerMenuScene);

        // Check if the user is an admin
        if (user.getRole().equals("Admin")) {
            mainApp.setScene(mainApp.getScene("AdminMenu"));
        } else {
            mainApp.setScene(mainApp.getScene("CustomerMenu"));
        }
    }

    public Scene getScene(){
        return this.createLoginForm();
    }
}
