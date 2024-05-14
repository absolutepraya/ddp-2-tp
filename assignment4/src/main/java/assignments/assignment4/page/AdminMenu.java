package assignments.assignment4.page;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// import assignments.assignment3.assignment2copy.Menu;
// import assignments.assignment3.assignment2copy.Order;
import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment3.assignment2copy.User;

import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;

import javafx.collections.FXCollections;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
import javafx.scene.Scene;
// import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class AdminMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    // private List<Restaurant> restoList = DepeFood.getRestoList();
    private MainApp mainApp; // Reference to MainApp instance
    private ComboBox<String> restaurantComboBox1 = new ComboBox<>(); // ComboBox for add menu
    private ComboBox<String> restaurantComboBox2 = new ComboBox<>(); // ComboBox for view restaurant
    private ListView<String> menuItemsListView = new ListView<>();

    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.scene = createBaseMenu();
        setScene(scene);
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox();
        
        // Create buttons for the menu
        Button addRestaurantButton = new Button("Tambah Restoran");
        Button addMenuButton = new Button("Tambah Menu Restoran");
        Button viewRestaurantsButton = new Button("Lihat Daftar Restoran");
        Button logOutButton = new Button("Log Out");

        // Create title and subtitle label
        Label title = new Label("Admin Menu");
        setTitleFont(title);
        Label subtitle = new Label("Welcome, mimin " + user.getNama() + "!\nPilih menu yang ingin diakses.");
        subtitle.setStyle("-fx-text-alignment: center;"); // Center the text

        // Create spacer
        Region spacer1 = createSpacer(20);
        Region spacer2 = createSpacer(10);
        
        // Add the components to the layout
        setUpVBoxLayout(menuLayout, title, subtitle, spacer1, addRestaurantButton, addMenuButton, viewRestaurantsButton, spacer2, logOutButton);

        // Set the action for each button
        addRestaurantButton.setOnAction(e -> {stage.setScene(addRestaurantScene);});
        addMenuButton.setOnAction(e -> {
            refresh(restaurantComboBox1); // Refresh the combobox
            stage.setScene(addMenuScene);
        });
        viewRestaurantsButton.setOnAction(e -> {
            refresh(restaurantComboBox2); // Refresh the combobox
            stage.setScene(viewRestaurantsScene);
        });
        logOutButton.setOnAction(e -> {mainApp.logOut();});

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createAddRestaurantForm() {
        VBox layout = new VBox();
        
        // Create the labels
        Label nameLabel = new Label("Restaurant Name:");

        // Create the text fields
        TextField nameInput = new TextField();
        
        // Create the buttons
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Kembali");
        
        // Add the components to the layout
        setUpVBoxLayout(layout, nameLabel, nameInput, submitButton, backButton);

        // Set the action for the submit button
        submitButton.setOnAction(e -> {
            handleTambahRestoran(nameInput.getText());
        });
        backButton.setOnAction(e -> {
            stage.setScene(scene);
        });

        return new Scene(layout, 400, 600);
    }

    private Scene createAddMenuForm() {
        VBox layout = new VBox();

        // Create the labels
        Label restaurantLabel = new Label("Restaurant Name:");
        Label menuNameLabel = new Label("Menu Item Name:");
        Label priceLabel = new Label("Price:");

        // Create the text fields
        TextField menuNameInput = new TextField();
        TextField priceInput = new TextField();

        // Create the buttons
        Button submitButton = new Button("Add Menu Item");
        Button backButton = new Button("Kembali");
        
        // Add the components to the layout
        setUpVBoxLayout(layout, restaurantLabel, restaurantComboBox1, menuNameLabel, menuNameInput, priceLabel, priceInput, submitButton, backButton);

        // Set the action for the submit button
        submitButton.setOnAction(e -> {
            // Check if the restaurant combobox is empty
            if (restaurantComboBox1.getSelectionModel().isEmpty()) {
                createAlert("Invalid Restaurant", "Pilih Restoran terlebih dahulu!");
                return;
            }

            Restaurant selectedRestaurant = getRestoList().get(restaurantComboBox1.getSelectionModel().getSelectedIndex());
            handleTambahMenuRestoran(selectedRestaurant, menuNameInput.getText(), priceInput.getText());
        });
        backButton.setOnAction(e -> {
            stage.setScene(scene);
        });
    
        return new Scene(layout, 400, 600);
    }
    
    private Scene createViewRestaurantsForm() {
        VBox layout = new VBox();

        // Create the labels
        Label restaurantLabel = new Label("Restaurant Name:");
        Label menuItemsLabel = new Label("Menu:");

        // Create the list view for menu items
        menuItemsListView.setItems(FXCollections.observableArrayList());

        // Create the buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Kembali");

        // Add the components to the layout
        setUpVBoxLayout(layout, restaurantLabel, restaurantComboBox2, searchButton,  menuItemsLabel, menuItemsListView, backButton);

        // Set the action for the search button
        searchButton.setOnAction(e -> {
            // Check if the restaurant combobox is empty
            if (restaurantComboBox2.getSelectionModel().isEmpty()) {
                createAlert("Invalid Restaurant", "Pilih Restoran terlebih dahulu!");
                return;
            }

            // Update the list view with the menu items of the selected restaurant
            refreshListView(restaurantComboBox2, menuItemsListView, true);
        });
        backButton.setOnAction(e -> {
            stage.setScene(scene);
        });
    
        return new Scene(layout, 400, 600);
    }
    

    private void handleTambahRestoran(String nama) {
        // Validate the restaurant name
        String restaurantName = DepeFood.getValidRestaurantName(nama);
        if (restaurantName.equals("0")) {
            // Restaurant name already exists
            createAlert("Invalid Restaurant Name", (String.format("Restoran dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!", nama)));
        } else if (restaurantName.equals("1")) {
            // Restaurant name is too short
            createAlert("Invalid Restaurant Name", "Nama Restoran tidak valid! Minimal 4 karakter diperlukan.");
        } else if (restaurantName.equals("2")) {
            // Restaurant name is empty
            createAlert("Invalid Restaurant Name", "Nama Restoran tidak boleh kosong!");
        } else {
            // Create a new restaurant
            Restaurant newRestaurant = new Restaurant(restaurantName);
            addRestoList(newRestaurant);
            createOK("Success", "Restoran berhasil ditambahkan!");
        }
    }

    private void handleTambahMenuRestoran(Restaurant restaurant, String itemName, String price) {
        // Validate the menu item name
        String menuItemName = DepeFood.getValidMenuItemName(itemName, price);
        if (menuItemName.equals("0")) {
            // Menu item name already exists
            createAlert("Invalid Menu Item Name", (String.format("Menu dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!", itemName)));
        } else if (menuItemName.equals("1")) {
            // Price is not a number or less than 0
            createAlert("Invalid Price", "Harga harus berupa angka dan lebih dari 0!");
        } else if (menuItemName.equals("2")) {
            // Menu item name or price is empty
            createAlert("Invalid Menu Item", "Nama Menu atau Harga tidak boleh kosong!");
        } else {
            restaurant.addMenu(menuItemName, Double.parseDouble(price));
            createOK("Success", "Menu berhasil ditambahkan!");
        }
    }
}
