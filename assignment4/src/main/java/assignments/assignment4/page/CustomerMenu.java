package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import assignments.assignment3.assignment2copy.Menu;
import assignments.assignment3.assignment2copy.Order;
import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment3.assignment2copy.User;
import assignments.assignment3.payment.DepeFoodPaymentSystem;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;

import assignments.assignment4.DepeFood;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter; // Instance of BillPrinter
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    // private static Label label = new Label();
    private MainApp mainApp;
    private List<Restaurant> restoList = DepeFood.getRestoList();
    private User user;

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        setScene(scene);
        this.addOrderScene = createTambahPesananForm();
        // this.billPrinter = new BillPrinter(stage, mainApp, this.user); // Pass user to BillPrinter constructor
        // this.printBillScene = createBillPrinter();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(15, 15, 15, 15));

        // Create the buttons
        Button addOrderButton = new Button("Buat Pesanan");
        Button printBillButton = new Button("Cetak Bill");
        Button payBillButton = new Button("Bayar Bill");
        Button cekSaldoButton = new Button("Cek Saldo");
        Button logOutButton = new Button("Log Out");

        // Set the buttons to have the same width, around 150px
        addOrderButton.setMinWidth(150);
        printBillButton.setMinWidth(150);
        payBillButton.setMinWidth(150);
        cekSaldoButton.setMinWidth(150);
        logOutButton.setMinWidth(150);

        // Center the text in the buttons
        addOrderButton.setAlignment(Pos.CENTER);
        printBillButton.setAlignment(Pos.CENTER);
        payBillButton.setAlignment(Pos.CENTER);
        cekSaldoButton.setAlignment(Pos.CENTER);
        logOutButton.setAlignment(Pos.CENTER);

        // Add the buttons to the layout
        menuLayout.getChildren().addAll(addOrderButton, printBillButton, payBillButton, cekSaldoButton, logOutButton);

        // Set the action for the buttons
        addOrderButton.setOnAction(e -> stage.setScene(addOrderScene));
        // printBillButton.setOnAction(e -> stage.setScene(printBillScene));
        payBillButton.setOnAction(e -> stage.setScene(payBillScene));
        cekSaldoButton.setOnAction(e -> stage.setScene(cekSaldoScene));
        logOutButton.setOnAction(e -> mainApp.logOut());

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createTambahPesananForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(15, 15, 15, 15));

        // Create the labels
        Label restaurantLabel = new Label("Restaurant:");
        Label dateLabel = new Label("Date (MM/DD/YYYY):");
        Label menuLabel = new Label("Menu Items:");

        // Create combo box for restaurant
        restaurantComboBox.setItems(FXCollections.observableArrayList(restoList.stream().map(Restaurant::getNama).collect(Collectors.toList())));

        // Create the date picker
        DatePicker orderDate = new DatePicker();

        // Create the list view for menu items
        ListView<String> menuItemsListView = new ListView<>();
        ObservableList<String> menuItems = FXCollections.observableArrayList();
        menuItemsListView.setItems(menuItems);
        // Enable multiple selection
        menuItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Create the buttons
        Button menuButton = new Button("Lihat Menu");
        Button orderButton = new Button("Buat Pesanan");
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        menuButton.setMinWidth(150);
        orderButton.setMinWidth(150);
        backButton.setMinWidth(150);

        // Center the text in the buttons
        menuButton.setAlignment(Pos.CENTER);
        orderButton.setAlignment(Pos.CENTER);
        backButton.setAlignment(Pos.CENTER);

        // Add the components to the layout
        menuLayout.getChildren().addAll(restaurantLabel, restaurantComboBox, dateLabel, orderDate, menuButton, menuLabel, menuItemsListView, orderButton, backButton);

        // Set the action for the buttons
        menuButton.setOnAction(e -> {
            // Get the selected restaurant
            String selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();
            // Get the restaurant object
            Restaurant restaurant = DepeFood.getRestaurantByName(selectedRestaurant);
            if (restaurant != null) {
                // Get the menu items
                ArrayList<Menu> menuList = restaurant.getMenu();
                // Clear the list view
                menuItems.clear();
                // Add the menu items to the list view
                menuItems.addAll(menuList.stream().map(Menu::getNamaMakanan).collect(Collectors.toList()));
            } else {
                // Show an alert if no restaurant is selected
                MainApp.createAlert("Invalid Restaurant Name", "Pilih salah satu restoran!");
            }
        });
        orderButton.setOnAction(e -> {
            // Get the selected restaurant
            String selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();

            // Get the restaurant object
            Restaurant restaurant = DepeFood.getRestaurantByName(selectedRestaurant);

            if (restaurant != null) {
                // Check if the user doesn't choose any menu items
                if (menuItemsListView.getSelectionModel().getSelectedItems().isEmpty()) {
                    MainApp.createAlert("Invalid Menu Items", "Pilih minimal 1 menu!");
                    return;
                }

                // Get the Menu objects
                List<String> selectedMenuItems = menuItemsListView.getSelectionModel().getSelectedItems();
                Menu[] selectedMenus = DepeFood.getMenuRequest(restaurant, selectedMenuItems);

                // Get the order date and change the date format to DD/MM/YYYY
                String orderDateString = orderDate.getValue().getDayOfMonth() + "/" + orderDate.getValue().getMonthValue() + "/" + orderDate.getValue().getYear();

                // Create the order
                Order order = new Order(Order.generateOrderID(selectedRestaurant, orderDateString, user.getNomorTelepon()), orderDateString, user.calculateOngkir(), DepeFood.getRestaurantByName(selectedRestaurant), selectedMenus);

                // Add the order to the user
                user.addOrderHistory(order);

                // Show a confirmation alert
                MainApp.createOK("Order Success", "Order dengan ID " + order.getOrderID() + " berhasil ditambahkan!");
            } else {
                // Show an alert if no restaurant is selected
                MainApp.createAlert("Invalid Restaurant Name", "Pilih salah satu restoran!");
            }
        });
    
        return new Scene(menuLayout, 400, 600);
    }
    // TODO
    // private Scene createBillPrinter(){
    //     // Create bill printer scene
    //     billPrinter = new BillPrinter(stage, mainApp, user);
    //     Scene billPrinterScene = billPrinter.getScene();
    //     return billPrinterScene;
    // }

    private Scene createBayarBillForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(15, 15, 15, 15));

        // Create the labels
        Label orderIDLabel = new Label("Order ID:");

        // Create the text field
        TextField orderIDInput = new TextField();

        // Create combo box for payment method
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.setItems(FXCollections.observableArrayList("Credit Card", "Debit"));

        // Create the buttons
        Button payBillButton = new Button("Bayar");
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        payBillButton.setMinWidth(150);
        backButton.setMinWidth(150);

        // Center the text in the buttons
        payBillButton.setAlignment(Pos.CENTER);
        backButton.setAlignment(Pos.CENTER);

        // Add the components to the layout
        menuLayout.getChildren().addAll(orderIDLabel, orderIDInput, paymentMethodComboBox, payBillButton, backButton);

        // Set the action for the buttons
        payBillButton.setOnAction(e -> {
            // Get the order ID
            String orderID = orderIDInput.getText();
            // Get the payment method
            int pilihanPembayaran = paymentMethodComboBox.getSelectionModel().getSelectedIndex();
            // Call the handleBayarBill method
            handleBayarBill(orderID, pilihanPembayaran);
        });

        return new Scene(menuLayout, 400,600);
    }
    
    private Scene createCekSaldoScene() {
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(15, 15, 15, 15));

        // Getting user's payment system
        DepeFoodPaymentSystem paymentSystem = user.getPaymentSystem();
        String paymentSystemString;
        if (paymentSystem instanceof CreditCardPayment) {
            paymentSystemString = "Credit Card";
        } else {
            paymentSystemString = "Debit";
        }

        // Create the labels
        Label nameLabel = new Label(user.getNama());
        Label paymentMethodLabel = new Label("Metode Pembayaran: " + paymentSystemString);
        Label saldoLabel = new Label("Saldo: Rp " + user.getSaldo());

        // Create the buttons
        Button backButton = new Button("Kembali");

        // Set the buttons to have the same width, around 150px
        backButton.setMinWidth(150);

        // Center the text in the buttons
        backButton.setAlignment(Pos.CENTER);

        // Add the components to the layout (// paymentMethodLabel, )
        menuLayout.getChildren().addAll(nameLabel, paymentMethodLabel, saldoLabel, backButton);

        // Set the action for the buttons
        backButton.setOnAction(e -> stage.setScene(scene));

        return new Scene(menuLayout, 400,600);
    }

    // private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<String> menuItems) {
    //     //TODO: Implementasi validasi isian pesanan
    //     try {

    //     } catch (Exception e) {

    //     }
    // }

    private void handleBayarBill(String orderID, int pilihanPembayaran) {
        // Check if the order ID is empty
        if (orderID.isEmpty()) {
            MainApp.createAlert("Order ID Error", "Order ID tidak boleh kosong!");
            return;
        }
        
        // Get the order
        Order order = DepeFood.getOrderOrNull(orderID);
        
        // Check if the order exists
        if (order == null) {
            MainApp.createAlert("Order ID Error", "Order ID tidak ditemukan!");
            return;
        }

        // Check if the order is already paid
        if (order.getOrderFinished()) {
            MainApp.createAlert("Order ID Error", "Order ID sudah dibayar!");
            return;
        }
        // Get the total amount to be paid
        long totalAmount = (long) order.totalBiaya();
        
        // Check if the payment method is not selected
        if (pilihanPembayaran == -1) {
            MainApp.createAlert("Payment Method Error", "Pilih metode pembayaran!");
            return;
        }

        // Pay the bill based on the user's payment method
        long result;
        DepeFoodPaymentSystem paymentSystem = user.getPaymentSystem();
        if (pilihanPembayaran == 0) {
            // Check if the user has different payment method
            if (paymentSystem instanceof DebitPayment) {
                MainApp.createAlert("Payment Method Error", "Anda tidak memiliki kartu kredit!");
                return;
            }

            result = paymentSystem.processPayment(totalAmount);

            if (result == -2) {
                // Pass
            } else if (result == -3) {
                MainApp.createAlert("Payment Method Error", "Saldo tidak mencukupi!");
                return;
            } else {
                moveSaldo(order, totalAmount);
                MainApp.createOK("Payment Success", "Berhasil Membayar Bill sebesar Rp " + totalAmount + " dengan biaya transaksi sebesar Rp " + (result - totalAmount));
            }
        } else if (pilihanPembayaran == 1) {
            // Check if the user has different payment method
            if (paymentSystem instanceof CreditCardPayment) {
                MainApp.createAlert("Payment Method Error", "Anda tidak memiliki kartu kredit!");
                return;
            }

            result = paymentSystem.processPayment(totalAmount);

            if (result == -2) {
                MainApp.createAlert("Payment Method Error", "Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain!");
                return;
            } else if (result == -3) {
                MainApp.createAlert("Payment Method Error", "Saldo tidak mencukupi!");
                return;
            } else {
                moveSaldo(order, totalAmount);
                MainApp.createOK("Payment Success", "Berhasil Membayar Bill sebesar Rp " + totalAmount + " dengan biaya transaksi sebesar Rp " + (result - totalAmount));
            }
        }
    }

    private void moveSaldo(Order order, long amount) {
        user.subtractSaldo(amount);
        order.getRestaurant().addSaldo(amount);
    }
}