package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
// import javafx.scene.control.Alert.AlertType;
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

// import java.util.ArrayList;
// import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
// import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomerMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter; // Instance of BillPrinter
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private Label saldoLabel = new Label();
    private Label saldoMenuLabel = new Label();
    // Set the saldoMenuLabel to bold and green colored
    // private static Label label = new Label();
    private MainApp mainApp;
    // private List<Restaurant> restoList = DepeFood.getRestoList();
    private User user;

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        setScene(scene);
        this.addOrderScene = createTambahPesananForm();
        this.billPrinter = new BillPrinter(stage, mainApp, scene);
        // billPrinter.setScene(scene);
        this.printBillScene = createBillPrinter();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
        this.saldoMenuLabel.setText("Saldo anda: Rp " + user.getSaldo());
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox();

        // Create the buttons
        Button addOrderButton = new Button("Buat Pesanan");
        Button printBillButton = new Button("Cetak Bill");
        Button payBillButton = new Button("Bayar Bill");
        Button cekSaldoButton = new Button("Cek Saldo");
        Button logOutButton = new Button("Log Out");

        // Set the button style
        setRegularButtonStyle(addOrderButton, printBillButton, payBillButton, cekSaldoButton);
        setLogoutButtonStyle(logOutButton);

        // Create title and subtitle label
        Label title = new Label("Customer Menu");
        Label subtitle = new Label("Welcome, " + user.getNama() + "!\nMau makan apa hari ini :D");

        // Create spacer
        Region spacer1 = createSpacer(15);
        Region spacer2 = createSpacer(15);

        // Add the components to the layout
        setUpVBoxLayout(true, menuLayout, title, subtitle, saldoMenuLabel, spacer1, addOrderButton, printBillButton, payBillButton, cekSaldoButton, spacer2, logOutButton);
        subtitle.setStyle("-fx-text-alignment: center; -fx-text-fill: white;"); // Center the text
        saldoMenuLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4dff00;"); // Bold and green colored

        // Set the action for the buttons
        addOrderButton.setOnAction(e -> {
            refresh(restaurantComboBox); // Refresh the combobox
            stage.setScene(addOrderScene);
        });
        printBillButton.setOnAction(e -> stage.setScene(printBillScene));
        payBillButton.setOnAction(e -> stage.setScene(payBillScene));
        cekSaldoButton.setOnAction(e -> {
            stage.setScene(cekSaldoScene);
            saldoLabel.setText("Saldo: Rp " + user.getSaldo());
        });
        logOutButton.setOnAction(e -> mainApp.logOut());

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createTambahPesananForm() {
        VBox menuLayout = new VBox();

        // Create the labels
        Label restaurantLabel = new Label("Restaurant:");
        Label dateLabel = new Label("Date (DD/MM/YYYY):");
        Label menuLabel = new Label("Menu Items:");

        // Create the text fields
        TextField orderDate = new TextField();
        orderDate.setPromptText("Enter date (DD/MM/YYYY)");

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

        // Set the button style
        setLoginButtonStyle(menuButton, orderButton);
        setLogoutButtonStyle(backButton);

        // Add the components to the layout
        setUpVBoxLayout(false, menuLayout, restaurantLabel, restaurantComboBox, dateLabel, orderDate, menuButton,  menuLabel, menuItemsListView, orderButton, backButton);

        // Set the action for the buttons
        menuButton.setOnAction(e -> {
            // Check if the restaurant combobox is empty
            if (restaurantComboBox.getSelectionModel().isEmpty()) {
                createAlert("Invalid Restaurant", "Pilih Restoran terlebih dahulu!");
                return;
            }

            // Update the list view with the menu items of the selected restaurant
            refreshListView(restaurantComboBox, menuItemsListView, false);
        });
        orderButton.setOnAction(e -> {
            // Get the selected restaurant
            String selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();

            // Get the date
            String selectedDate = orderDate.getText();

            // Get the selected menu items
            List<String> selectedMenuItems = menuItemsListView.getSelectionModel().getSelectedItems();

            // Call the handleBuatPesanan method
            handleBuatPesanan(selectedRestaurant, selectedDate, selectedMenuItems);
        });
        backButton.setOnAction(e -> {
            // Update the saldoMenuLabel
            saldoMenuLabel.setText("Saldo anda: Rp " + user.getSaldo());
            stage.setScene(scene);
        });
    
        return new Scene(menuLayout, 400, 600);
    }

    private Scene createBillPrinter(){
        // Create bill printer scene
        Scene billPrinterScene = billPrinter.getScene();
        return billPrinterScene;
    }

    private Scene createBayarBillForm() {
        VBox menuLayout = new VBox();

        // Create the labels
        Label orderIDLabel = new Label("Order ID:");

        // Create the text field
        TextField orderIDInput = new TextField();

        // Set placeholder text
        orderIDInput.setPromptText("XXXXDDMMYYYYXXXX");

        // Create combo box for payment method
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.setItems(FXCollections.observableArrayList("Credit Card", "Debit"));

        // Create the buttons
        Button payBillButton = new Button("Bayar");
        Button backButton = new Button("Kembali");

        // Set the button style
        setLoginButtonStyle(payBillButton);
        setLogoutButtonStyle(backButton);

        // Create spacer
        Region spacer = createSpacer(15);

        // Add the components to the layout
        setUpVBoxLayout(false, menuLayout, orderIDLabel, orderIDInput, paymentMethodComboBox, payBillButton, spacer, backButton);

        // Set the action for the buttons
        payBillButton.setOnAction(e -> {
            // Get the order ID
            String orderID = orderIDInput.getText();
            // Get the payment method
            int pilihanPembayaran = paymentMethodComboBox.getSelectionModel().getSelectedIndex();
            // Call the handleBayarBill method
            handleBayarBill(orderID, pilihanPembayaran);
        });
        backButton.setOnAction(e -> {
            // Update the saldoMenuLabel
            saldoMenuLabel.setText("Saldo anda: Rp " + user.getSaldo());
            stage.setScene(scene);
        });

        return new Scene(menuLayout, 400,600);
    }
    
    private Scene createCekSaldoScene() {
        VBox menuLayout = new VBox();

        // Getting user's payment system
        DepeFoodPaymentSystem paymentSystem = user.getPaymentSystem();
        String paymentSystemString;
        if (paymentSystem instanceof CreditCardPayment) {
            paymentSystemString = "Credit Card";
        } else {
            paymentSystemString = "Debit";
        }

        // Create the labels
        Label saldoTitleLabel = new Label("Informasi Saldo");
        Label nameLabel = new Label(user.getNama());
        Label paymentMethodLabel = new Label("Metode Pembayaran: " + paymentSystemString);
        saldoLabel = new Label("Saldo: Rp " + user.getSaldo());

        // Create the buttons
        Button backButton = new Button("Kembali");

        // Set the button style
        setLogoutButtonStyle(backButton);

        // Create spacer
        Region spacer1 = createSpacer(15);
        Region spacer2 = createSpacer(15);

        // Add the components to the layout
        setUpVBoxLayout(true, menuLayout, saldoTitleLabel, spacer1, nameLabel, paymentMethodLabel, saldoLabel, spacer2, backButton);

        // Set the action for the buttons
        backButton.setOnAction(e -> {
            // Update the saldoMenuLabel
            saldoMenuLabel.setText("Saldo anda: Rp " + user.getSaldo());
            stage.setScene(scene);
        });

        return new Scene(menuLayout, 400,600);
    }

    private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<String> menuItems) {
        // Check if namaRestoran is not selected
        if (namaRestoran == null) {
            createAlert("Restaurant Error", "Pilih salah satu restoran yang ada!");
            return;
        }

        // Check if tanggalPemesanan is empty
        if (tanggalPemesanan.isEmpty()) {
            createAlert("Date Error", "Tanggal pemesanan tidak boleh kosong!");
            return;
        }

        // Check if the date is in the correct format
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(tanggalPemesanan, formatter);
        } catch (DateTimeParseException e) {
            createAlert("Date Error", "Format tanggal salah! Gunakan format DD/MM/YYYY");
            return;
        }

        // Check if menuItems is empty
        if (menuItems.isEmpty()) {
            createAlert("Menu Error", "Pilih minimal satu menu!");
            return;
        }
        
        // Get the restaurant object
        Restaurant restaurant = DepeFood.getRestaurantByName(namaRestoran);

        // Get the menu items
        Menu[] selectedMenus = DepeFood.getMenuRequest(restaurant, menuItems);

        // Create the order
        Order order = new Order(Order.generateOrderID(namaRestoran, tanggalPemesanan, user.getNomorTelepon()), tanggalPemesanan, user.calculateOngkir(), restaurant, selectedMenus);

        // Add the order to the user's order list
        user.addOrderHistory(order);

        // Show a confirmation alert
        createOK("Order Success", "Order dengan ID " + order.getOrderID() + " berhasil ditambahkan!");
    }

    private void handleBayarBill(String orderID, int pilihanPembayaran) {
        // Check if the order ID is empty
        if (orderID.isEmpty()) {
            createAlert("Order ID Error", "Order ID tidak boleh kosong!");
            return;
        }
        
        // Get the order
        Order order = DepeFood.getOrderOrNull(orderID);
        
        // Check if the order exists
        if (order == null) {
            createAlert("Order ID Error", "Order ID tidak ditemukan!");
            return;
        }

        // Check if the order is already paid
        if (order.getOrderFinished()) {
            createAlert("Order ID Error", "Order ID sudah dibayar!");
            return;
        }
        // Get the total amount to be paid
        long totalAmount = (long) order.totalBiaya();
        
        // Check if the payment method is not selected
        if (pilihanPembayaran == -1) {
            createAlert("Payment Method Error", "Pilih metode pembayaran!");
            return;
        }

        // Pay the bill based on the user's payment method
        long result;
        DepeFoodPaymentSystem paymentSystem = user.getPaymentSystem();
        if (pilihanPembayaran == 0) { // Kartu Kredit
            // Check if the user has different payment method
            if (paymentSystem instanceof DebitPayment) {
                createAlert("Payment Method Error", "Anda tidak memiliki kartu kredit!");
                return;
            }

            result = paymentSystem.processPayment(totalAmount);

            if (result == -2) {
                // Pass
            } else if (result == -3) {
                createAlert("Payment Method Error", "Saldo tidak mencukupi!");
                return;
            } else {
                moveSaldo(order, result, 0);
                createOK("Payment Success", "Berhasil Membayar Bill sebesar Rp " + totalAmount + " dengan biaya transaksi sebesar Rp " + (result - totalAmount));
            }
        } else if (pilihanPembayaran == 1) { // Debit
            // Check if the user has different payment method
            if (paymentSystem instanceof CreditCardPayment) {
                createAlert("Payment Method Error", "Anda tidak memiliki kartu debit!");
                return;
            }

            result = paymentSystem.processPayment(totalAmount);

            if (result == -2) {
                createAlert("Payment Method Error", "Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain!");
                return;
            } else if (result == -3) {
                createAlert("Payment Method Error", "Saldo tidak mencukupi!");
                return;
            } else {
                moveSaldo(order, result, 1);
                createOK("Payment Success", "Berhasil Membayar Bill sebesar Rp " + result);
            }
        }
        // Set the order as finished
        order.setOrderFinished(true);
    }

    private void moveSaldo(Order order, long amount, int method) {
        user.subtractSaldo(amount);
        if (method == 0) order.getRestaurant().addSaldo(amount * 100 / 102); // 2% transaction fee
        else order.getRestaurant().addSaldo(amount);
    }
}