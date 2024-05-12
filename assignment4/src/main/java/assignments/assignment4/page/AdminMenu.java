package assignments.assignment4.page;

// import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import assignments.assignment3.assignment2copy.Menu;
// import assignments.assignment3.assignment2copy.Order;
import assignments.assignment3.assignment2copy.Restaurant;
// import assignments.assignment3.assignment2copy.User;

import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
// import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    // private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private List<Restaurant> restoList = DepeFood.getRestoList();
    private MainApp mainApp; // Reference to MainApp instance
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private ListView<String> menuItemsListView = new ListView<>();

    public AdminMenu(Stage stage, MainApp mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
        // this.user = user;
        this.scene = createBaseMenu();
        setScene(scene);
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);

        // Create buttons for the menu
        Button addRestaurantButton = new Button("Tambah Restoran");
        Button addMenuButton = new Button("Tambah Menu Restoran");
        Button viewRestaurantsButton = new Button("Lihat Daftar Restoran");
        Button logOutButton = new Button("Log Out");

        // Set the buttons to have the same width, around 150px
        addRestaurantButton.setMinWidth(150);
        addMenuButton.setMinWidth(150);
        viewRestaurantsButton.setMinWidth(150);
        logOutButton.setMinWidth(150);

        // Center the text inside the buttons
        addRestaurantButton.setAlignment(Pos.CENTER);
        addMenuButton.setAlignment(Pos.CENTER);
        viewRestaurantsButton.setAlignment(Pos.CENTER);
        logOutButton.setAlignment(Pos.CENTER);

        // Add the buttons to the layout
        menuLayout.getChildren().addAll(addRestaurantButton, addMenuButton, viewRestaurantsButton, logOutButton);

        // Set the action for each button
        addRestaurantButton.setOnAction(e -> {stage.setScene(addRestaurantScene);});
        addMenuButton.setOnAction(e -> {stage.setScene(addMenuScene);});
        viewRestaurantsButton.setOnAction(e -> {stage.setScene(viewRestaurantsScene);});
        logOutButton.setOnAction(e -> {mainApp.logOut();});

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createAddRestaurantForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15, 15, 15, 15));

        // Create the labels
        Label nameLabel = new Label("Restaurant Name:");

        // Create the text fields
        TextField nameInput = new TextField();

        // Create the buttons
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        submitButton.setMinWidth(150);
        backButton.setMinWidth(150);

        // Center the text inside the buttons
        submitButton.setAlignment(Pos.CENTER);
        backButton.setAlignment(Pos.CENTER);

        // Add the components to the layout
        layout.getChildren().addAll(nameLabel, nameInput, submitButton, backButton);

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
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15, 15, 15, 15));

        // Create the labels
        Label restaurantLabel = new Label("Restaurant Name:");
        Label menuNameLabel = new Label("Menu Item Name:");
        Label priceLabel = new Label("Price:");

        // Create combobox for restaurant
        restaurantComboBox.setItems(FXCollections.observableArrayList(restoList.stream().map(Restaurant::getNama).collect(Collectors.toList())));

        // Create the text fields
        TextField menuNameInput = new TextField();
        TextField priceInput = new TextField();

        // Create the buttons
        Button submitButton = new Button("Add Menu Item");
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        submitButton.setMinWidth(150);
        backButton.setMinWidth(150);

        // Set the combobox size
        restaurantComboBox.setMinWidth(200);

        // Center the text inside the buttons and combobox
        submitButton.setAlignment(Pos.CENTER);
        backButton.setAlignment(Pos.CENTER);
        
        // Add the components to the layout
        layout.getChildren().addAll(restaurantLabel, restaurantComboBox, menuNameLabel, menuNameInput, priceLabel, priceInput, submitButton, backButton);

        // Set the action for the submit button
        submitButton.setOnAction(e -> {
            Restaurant selectedRestaurant = restoList.get(restaurantComboBox.getSelectionModel().getSelectedIndex());
            handleTambahMenuRestoran(selectedRestaurant, menuNameInput.getText(), priceInput.getText());
        });
        backButton.setOnAction(e -> {
            stage.setScene(scene);
        });
    
        return new Scene(layout, 400, 600);
    }
    
    private Scene createViewRestaurantsForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15, 15, 15, 15));
        layout.setPadding(new Insets(15, 15, 15, 15));

        // Create the labels
        Label restaurantLabel = new Label("Restaurant Name:");
        Label menuItemsLabel = new Label("Menu:");

        // Create combobox for restaurant
        restaurantComboBox.setItems(FXCollections.observableArrayList(restoList.stream().map(Restaurant::getNama).collect(Collectors.toList())));

        // Create the list view for menu items
        menuItemsListView.setItems(FXCollections.observableArrayList());

        // Create the buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        searchButton.setMinWidth(150);
        backButton.setMinWidth(150);

        // Set the combobox size
        restaurantComboBox.setMinWidth(200);

        // Center the text inside the buttons and combobox
        searchButton.setAlignment(Pos.CENTER);
        backButton.setAlignment(Pos.CENTER);

        // Add the components to the layout
        layout.getChildren().addAll(restaurantLabel, restaurantComboBox, searchButton, menuItemsLabel, menuItemsListView, backButton);

        // Set the action for the search button
        searchButton.setOnAction(e -> {
            Restaurant selectedRestaurant = restoList.get(restaurantComboBox.getSelectionModel().getSelectedIndex());
            menuItemsListView.setItems(FXCollections.observableArrayList(selectedRestaurant.getMenu().stream().map(Menu::getNamaMakanan).collect(Collectors.toList())));
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
            MainApp.createAlert("Invalid Restaurant Name", (String.format("Restoran dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!", nama)));
        } else if (restaurantName.equals("1")) {
            // Restaurant name is too short
            MainApp.createAlert("Invalid Restaurant Name", "Nama Restoran tidak valid! Minimal 4 karakter diperlukan.");
        } else if (restaurantName.equals("2")) {
            // Restaurant name is empty
            MainApp.createAlert("Invalid Restaurant Name", "Nama Restoran tidak boleh kosong!");
        } else {
            // Create a new restaurant
            Restaurant newRestaurant = new Restaurant(restaurantName);
            restoList.add(newRestaurant);
            MainApp.createOK("Success", "Restoran berhasil ditambahkan!");
        }
    }

    private void handleTambahMenuRestoran(Restaurant restaurant, String itemName, String price) {
        // Validate the menu item name
        String menuItemName = DepeFood.getValidMenuItemName(itemName, price);
        if (menuItemName.equals("0")) {
            // Menu item name already exists
            MainApp.createAlert("Invalid Menu Item Name", (String.format("Menu dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!", itemName)));
        } else if (menuItemName.equals("1")) {
            // Price is not a number or less than 0
            MainApp.createAlert("Invalid Price", "Harga harus berupa angka dan lebih dari 0!");
        } else if (menuItemName.equals("2")) {
            // Menu item name or price is empty
            MainApp.createAlert("Invalid Menu Item", "Nama Menu atau Harga tidak boleh kosong!");
        } else {
            restaurant.addMenu(menuItemName, Double.parseDouble(price));
            MainApp.createOK("Success", "Menu berhasil ditambahkan!");
        }
    }
}
