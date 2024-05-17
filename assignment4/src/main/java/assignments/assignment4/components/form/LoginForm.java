package assignments.assignment4.components.form;
import assignments.assignment3.assignment2copy.User;
import assignments.assignment3.MainMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Region;
import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.MemberMenu;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;
import javafx.scene.image.ImageView;

public class LoginForm {
    // Just to access the createAlert method
    private class DummyMemberMenu extends MemberMenu {
        @Override
        protected Scene createBaseMenu() {
            return null;
        }
    }

    // private Stage stage;
    private MainApp mainApp; // MainApp instance
    private TextField nameInput;
    private TextField phoneInput;
    private DummyMemberMenu dummyMemberMenu = new DummyMemberMenu();

    public LoginForm(Stage stage, MainApp mainApp) { // Pass MainApp instance to constructor
        // this.stage = stage;
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

        // Create illustration image
        Image illustration = new Image(MainApp.class.getResourceAsStream("/LoginIllustration.png"));
        ImageView illustrationView = new ImageView(illustration);
        illustrationView.setFitWidth(200);
        illustrationView.setFitHeight(200);

        // Create the labels
        Label welcomeLabel = new Label("Welcome to DepeFood!");
        Label nameLabel = new Label("Name:");
        Label phoneLabel = new Label("Phone Number:");
        dummyMemberMenu.setWhiteLabel(true, welcomeLabel, nameLabel, phoneLabel);

        // Create the text fields
        nameInput = new TextField();
        phoneInput = new TextField();

        // Set placeholder text
        nameInput.setPromptText("John Doe");
        phoneInput.setPromptText("81234567890");

        // Create the login button
        Button loginButton = new Button("Login");
        loginButton.setMinWidth(150);
        loginButton.setOnAction(e -> handleLogin());

        // Create a spacer
        Region spacer1 = dummyMemberMenu.createSpacer(20);
        Region spacer2 = dummyMemberMenu.createSpacer(20);
        Region spacer3 = dummyMemberMenu.createSpacer(20);

        // Put the components in the grid
        grid.add(welcomeLabel, 0, 0, 2, 1);
        grid.add(spacer1, 0, 1);
        grid.add(illustrationView, 0, 2, 2, 1);
        grid.add(spacer2, 0, 3);
        grid.add(nameLabel, 0, 4);
        grid.add(nameInput, 1, 4);
        grid.add(phoneLabel, 0, 5);
        grid.add(phoneInput, 1, 5);
        grid.add(spacer3, 0, 6);
        grid.add(loginButton, 0, 7, 2, 1);

        // Set the login button style
        dummyMemberMenu.setLoginButtonStyle(loginButton);

        // Center the welcome label and login button
        GridPane.setHalignment(welcomeLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(loginButton, javafx.geometry.HPos.CENTER);

        // Set the grid background color
        grid.setStyle("-fx-background-color: #373737;");
        return new Scene(grid, 400, 600);
    }


    private void handleLogin() {
        // Get the name and phone number from the input fields
        String name = nameInput.getText();
        String phone = phoneInput.getText();

        // Check if the name and phone number are empty
        if (name.isEmpty() || phone.isEmpty()) {
            dummyMemberMenu.createAlert("Login Failed", "Nama dan nomor telepon tidak boleh kosong!");
            return;
        }

        // Check if the phone number is not a number
        if (!phone.matches("[0-9]+")) {
            dummyMemberMenu.createAlert("Login Failed", "Nomor telepon harus berupa angka!");
            return;
        }

        // Use the DepeFood class to get the user
        User user = DepeFood.getUser(name, phone);

        // Check if the user is not found
        if (user == null) {
            dummyMemberMenu.createAlert("Login Failed", "User tidak ditemukan!");
            return;
        }

        // Set the user in the MainApp instance
        // mainApp.setUser(user);
        // Set the user in the MainApp in assignment3 (some methods are using the user from assignment3)
        MainMenu.setUserLoggedIn(user);

        // Initialize and populate adminMenuScene and customerMenuScene
        Scene adminMenuScene = new AdminMenu(mainApp.getWindow(), mainApp, user).getScene();
        mainApp.addScene("AdminMenu", adminMenuScene);
        Scene customerMenuScene = new CustomerMenu(mainApp.getWindow(), mainApp, user).getScene();
        mainApp.addScene("CustomerMenu", customerMenuScene);

        // Check if the user is an admin
        if (user.getRole().equals("Admin")) {
            mainApp.setScene(mainApp.getScene("AdminMenu"));
        } else {
            mainApp.setScene(mainApp.getScene("CustomerMenu"));
        }
    }

    public Scene getScene() {
        return this.createLoginForm();
    }
}
