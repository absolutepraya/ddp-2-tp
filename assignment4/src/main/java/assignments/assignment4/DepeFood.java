package assignments.assignment4;

// import java.text.DecimalFormat;
// import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
// import java.util.Arrays;
import java.util.List;
import java.util.Optional;
// import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// import assignments.assignment3.MainMenu;
import assignments.assignment3.assignment2copy.Menu;
import assignments.assignment3.assignment2copy.Order;
import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment3.assignment2copy.User;

import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
// import assignments.assignment3.payment.DepeFoodPaymentSystem;
// import assignments.assignment3.systemCLI.AdminSystemCLI;
// import assignments.assignment3.systemCLI.CustomerSystemCLI;
// import assignments.assignment3.systemCLI.UserSystemCLI;

public class DepeFood {
    private static ArrayList<User> userList;
    private static List<Restaurant> restoList = new ArrayList<>();
    private static User userLoggedIn;

    public static User getUserLoggedIn() {
        return userLoggedIn;
    }

    public static String getUserLoggedInRole(){
        return userLoggedIn.getRole();
    }

    public static void initUser() {
        userList = new ArrayList<>();

        userList.add(
            new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer", new DebitPayment(), 500000));
            userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer", new CreditCardPayment(), 2000000));
            userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer", new DebitPayment(), 750000));
            userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer", new CreditCardPayment(), 1800000));
            userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer", new DebitPayment(), 650000));
            userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin", new CreditCardPayment(), 0));
            userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin", new CreditCardPayment(), 0));
    }

    public static User getUser(String nama, String nomorTelepon) {

        for (User user : userList) {
            if (user.getNama().equals(nama.trim()) && user.getNomorTelepon().equals(nomorTelepon.trim())) {
                return user;
            }
        }
        return null;
    }

    public static User handleLogin(String nama, String nomorTelepon) {
        User user = getUser(nama, nomorTelepon);

        if (user == null) {
            System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
            return null;
        }

        userLoggedIn = user;
        return user;
    }

    // public static void handleTambahRestoran(String nama) {
    //     Restaurant restaurant = new Restaurant(nama);
    //     while (restaurant == null) {
    //         String namaRestaurant = getValidRestaurantName(nama);
    //         restaurant = new Restaurant(namaRestaurant);
    //     }
    //     restoList.add(restaurant);
    //     System.out.print("Restaurant " + restaurant.getNama() + " Berhasil terdaftar.");
    //     System.out.print(restoList.get(0).getNama());
    // }

    public static String getValidRestaurantName(String inputName) {
        if (inputName.isEmpty()) {
            return "2";
        }

        String name = "";
        boolean isRestaurantNameValid = false;
    
        while (!isRestaurantNameValid) {
            boolean isRestaurantExist = restoList.stream().anyMatch(restoran -> restoran.getNama().toLowerCase().equals(inputName.toLowerCase()));
            boolean isRestaurantNameLengthValid = inputName.length() >= 4;
    
            if (isRestaurantExist) {
                // Restaurant name already exists
                return "0";
            } else if (!isRestaurantNameLengthValid) {
                // Restaurant name is too short
                return "1";
            } else {
                name = inputName;
                isRestaurantNameValid = true;
            }
        }
        return name;
    }

    // public static Restaurant findRestaurant(String nama) {
    //     for (Restaurant resto : restoList) {
    //         if (resto.getNama().equals(nama)) {
    //             return resto;
    //         }
    //     }
    //     return null; 
    // }

    public static String getValidMenuItemName(String itemName, String price) {
        // If the menu item name is already exist
        for (Restaurant resto : restoList) {
            for (Menu menu : resto.getMenu()) {
                if (menu.getNamaMakanan().equals(itemName)) {
                    return "0";
                }
            }
        }
        // If the price is not a number (use try-catch) or the price is less than 0
        try {
            if (Double.parseDouble(price) < 0) {
                return "1";
            }
        } catch (NumberFormatException e) {
            return "1";
        }
        // If the menu item name or price is empty
        if (itemName.isEmpty() || price.isEmpty()) {
            return "2";
        }

        return itemName;
    }
    
    public static void handleTambahMenuRestoran(Restaurant restoran, String namaMakanan, double harga){
        restoran.addMenu(namaMakanan, harga);
    }

    public static void addRestoList(Restaurant resto) {
        restoList.add(resto);
    }

    public static List<Restaurant> getRestoList() {
        return restoList;
    }

    public static Restaurant getRestaurantByName(String name) {
        Optional<Restaurant> restaurantMatched = restoList.stream()
                .filter(restoran -> restoran.getNama().equalsIgnoreCase(name)).findFirst();
        if (restaurantMatched.isPresent()) {
            return restaurantMatched.get();
        }
        return null;
    }

    public static String handleBuatPesanan(String namaRestoran, String tanggalPemesanan, int jumlahPesanan, List<String> listMenuPesananRequest) {
        System.out.println("--------------Buat Pesanan----------------");
    
        Restaurant restaurant = getRestaurantByName(namaRestoran);
        if (restaurant == null) {
            System.out.println("Restoran tidak terdaftar pada sistem.\n");
            return null;
        }
    
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(tanggalPemesanan, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
            return null;
        }
    
        if (!validateRequestPesanan(restaurant, listMenuPesananRequest)) {
            System.out.println("Mohon memesan menu yang tersedia di Restoran!");
            return null;
        }
    
        Order order = new Order(
                Order.generateOrderID(namaRestoran, tanggalPemesanan, userLoggedIn.getNomorTelepon()),
                tanggalPemesanan,
                userLoggedIn.calculateOngkir(),
                restaurant,
                getMenuRequest(restaurant, listMenuPesananRequest));
    
        System.out.printf("Pesanan dengan ID %s diterima!", order.getOrderID());
        userLoggedIn.addOrderHistory(order);
        return order.getOrderID();
    }

    public static String generateBill(String orderID) {
        // Mencari order dengan orderID yang diinput
        Order order = null;
        User orderOwner = null;
        for (User user : userList) { // Mencari pemilik order
            for (Order orderHistory : user.getOrderHistory()) {
                if (orderHistory.getOrderID().equals(orderID)) {
                    order = orderHistory;
                    orderOwner = user;
                    break;
                }
            }
        }

        // Return string bill
        return "Bill:\n" +
               "Order ID: " + order.getOrderID() + "\n" +
               "Tanggal Pemesanan: " + order.getTanggalPemesanan() + "\n" +
               "Restaurant: " + order.getRestaurant().getNama() + "\n" +
               "Lokasi Pengiriman: " + orderOwner.getLokasi() + "\n" +
               "Status Pengiriman: " + order.statusPengiriman() + "\n" +
               "Pesanan:\n" + order.listMakanan() +
               "Biaya Ongkos Kirim: Rp " + order.getBiayaOngkosKirim() + "\n" +
               "Total Biaya: Rp " + String.format("%.0f", order.totalBiaya());
    }

    public static Order getOrderOrNull(String orderId) {
        for (User user : userList) {
            for (Order order : user.getOrderHistory()) {
                if (order.getOrderID().equals(orderId)) {
                    return order;
                }
            }
        }
        return null;
    }

    public static boolean validateRequestPesanan(Restaurant restaurant, List<String> listMenuPesananRequest) {
        return listMenuPesananRequest.stream().allMatch(
                pesanan -> restaurant.getMenu().stream().anyMatch(menu -> menu.getNamaMakanan().equals(pesanan)));
    }

    public static Menu[] getMenuRequest(Restaurant restaurant, List<String> listMenuPesananRequest) {
        Menu[] menu = new Menu[listMenuPesananRequest.size()];
        for (int i = 0; i < menu.length; i++) {
            Menu matchedMenu = null;
            for (Menu existMenu : restaurant.getMenu()) {
                if (existMenu.getNamaMakanan().equals(listMenuPesananRequest.get(i))) {
                    matchedMenu = existMenu;
                    break;
                }
            }
            if (matchedMenu == null) {
                System.out.println("No match found for menu item: " + listMenuPesananRequest.get(i));
            }
            menu[i] = matchedMenu;
        }
        return menu;
    }

    public static Order findUserOrderById(String orderId) {
        List<Order> orderHistory = userLoggedIn.getOrderHistory();
        
        for (Order order : orderHistory) {
            if (order.getOrderID() == orderId) {
                return order; 
            }   
        }
        return null; 
    }

    public static void handleUpdateStatusPesanan(Order order) {
        order.setOrderFinished(true);
    }
    
    // public static void setPenggunaLoggedIn(User user){
    //     userLoggedIn = user;
    // }
}
