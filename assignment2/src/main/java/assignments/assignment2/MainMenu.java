package assignments.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
// import assignments.assignment1.*;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    public static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;

    public static void main(String[] args) {
        initUser(); // Inisialisasi user dan admin
        restoList = new ArrayList<Restaurant>(); // Inisialisasi list restoran

        boolean programRunning = true;
        printHeader();

        while (programRunning) {
            startMenu();
            int command = input.nextInt();
            input.nextLine();

            if (command == 1) {
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine();
                System.out.print("Nomor Telepon: ");
                String noTelp = input.nextLine();

                // Mendapatkan user dengan nama dan nomor telepon yang diinput
                User userLoggedIn = getUser(nama, noTelp);
                // Validasi nama user
                if (userLoggedIn == null) {
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
                    continue;
                }
                boolean isLoggedIn = true;
                // Cetak pesan selamat datang
                System.out.println("Selamat Datang " + userLoggedIn.getNama() + "!");

                if (userLoggedIn.getRole() == "Customer") {
                    while (isLoggedIn) {
                        menuCustomer();
                        int commandCust = input.nextInt();
                        input.nextLine();

                        switch(commandCust) {
                            case 1 -> handleBuatPesanan(userLoggedIn); // Menambahkan parameter userLoggedIn
                            case 2 -> handleCetakBill();
                            case 3 -> handleLihatMenu();
                            case 4 -> handleUpdateStatusPesanan();
                            case 5 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                } else {
                    while (isLoggedIn) {
                        menuAdmin();
                        int commandAdmin = input.nextInt();
                        input.nextLine();

                        switch (commandAdmin) {
                            case 1 -> handleTambahRestoran();
                            case 2 -> handleHapusRestoran();
                            case 3 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }
            } else if (command == 2) {
                programRunning = false;
            } else {
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }

    public static User getUser(String nama, String nomorTelepon) {
        // Mendapatkan user dari userList berdasarkan nama dan nomor telepon
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null; // Jika tidak ditemukan
    }

/* ————————————————————— USERS ————————————————————— */

    public static void handleBuatPesanan(User userLoggedIn) {
        System.out.println("---------------Buat Pesanan---------------");
        // Loop validasi BuatPesanan
        while (true) {
            // Input nama restoran
            System.out.print("Nama Restoran: ");
            String namaResto = input.nextLine();
            // Validasi jumlah karakter nama restoran
            if (namaResto.length() < 4) {
                System.out.println("Nama restoran tidak valid!\n");
                continue;
            }
            // Mencari restoran dengan nama yang diinput, sekaligus validasi
            boolean found = false;
            Restaurant restoPesanan = null;
            for (Restaurant resto : restoList) {
                if (resto.getNama().toLowerCase().equals(namaResto.strip().toLowerCase())) { // Case insensitive
                    found = true;
                    restoPesanan = resto;
                    break;
                }
            }
            if (!found) {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            // Input tanggal order
            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            String tanggalPesanan = input.nextLine();
            // Validasi tanggal order menggunakan java.time dan try-catch
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate.parse(tanggalPesanan, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }

            // Input jumlah pesanan
            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = input.nextInt();
            input.nextLine(); // Mengosongkan buffer
            // Validasi jumlah pesanan
            if (jumlahPesanan < 1) {
                System.out.println("Jumlah pesanan tidak valid!\n");
                continue;
            }
            System.out.println("Order:");
            // Iterasi untuk input pesanan, sekaligus validasi
            String pesanan;
            Menu menuPesanan;
            ArrayList<Menu> listPesanan = new ArrayList<Menu>();
            boolean validPesanan = false;
            for (int i = 0; i < jumlahPesanan; i++) {
                pesanan = input.nextLine();
                // Cek apakah restoran memiliki menu tersebut
                for (Menu menu : restoPesanan.getMenu()) {
                    if (menu.getNamaMakanan().toLowerCase().equals(pesanan.toLowerCase())) { // Case insensitive
                        validPesanan = true;
                        // Membuat object menu
                        menuPesanan = new Menu(menu.getNamaMakanan(), menu.getHarga());
                        // Menambahkan menu ke array listPesanan
                        listPesanan.add(menuPesanan);
                        break;
                    }
                }
            }
            // Validasi pesanan
            if (!validPesanan) {
                System.out.println("Mohon memesan menu yang tersedia di Restoran!\n");
                // Mengosongkan array listPesanan
                listPesanan = new ArrayList<Menu>();
                continue;
            }

            // Membuat Order ID berdasarkan nama restoran, tanggal order, dan nomor telepon
            String orderID = generateOrderID(namaResto, tanggalPesanan, userLoggedIn.getNomorTelepon());
            System.out.println("Pesanan dengan ID " + orderID + " diterima!");

            // Membuat object order (param: orderID, tanggalPesanan, ongkir, resto, items)
            Order newOrder = new Order(orderID, tanggalPesanan, userLoggedIn.calculateOngkir(), restoPesanan, listPesanan.toArray(new Menu[0]));
            // Menambahkan order ke orderHistory user
            userLoggedIn.addOrderHistory(newOrder);

            break;
        }
    }

    public static void handleCetakBill() {
        System.out.println("---------------Cetak Bill---------------");

        // Input order ID
        System.out.print("Masukkan Order ID: ");
        String orderID = input.nextLine();

        // Mencetak bill
        System.out.println(generateBill(orderID));
    }

    public static void handleLihatMenu() {
        System.out.println("---------------Lihat Menu---------------");
        // Loop validasi LihatMenu
        while (true) {
            // Input nama restoran
            System.out.print("Nama Restoran: ");
            String namaResto = input.nextLine();
            // Validasi jumlah karakter nama restoran
            if (namaResto.length() < 4) {
                System.out.println("Nama restoran tidak valid!\n");
                continue;
            }
            // Mencari restoran dengan nama yang diinput, sekaligus validasi
            boolean found = false;
            Restaurant restoMenu = null;
            for (Restaurant resto : restoList) {
                if (resto.getNama().toLowerCase().equals(namaResto.strip().toLowerCase())) { // Case insensitive
                    found = true;
                    restoMenu = resto;
                    break;
                }
            }
            if (!found) {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            // Mencetak menu diurutkan dari harga yang paling murah ke paling mahal
            restoMenu.printMenu();

            break;
        }
    }

    public static void handleUpdateStatusPesanan() {
        // Loop validasi UpdateStatusPesanan
        while (true) {
            // Input order ID
            System.out.print("Masukkan Order ID: ");
            String orderID = input.nextLine();

            // Mencari order dengan orderID yang diinput, sekaligus validasi
            Order order = null;
            boolean orderFound = false;
            for (User user : userList) { // Mencari pemilik order
                for (Order orderHistory : user.getOrderHistory()) {
                    if (orderHistory.getOrderID().equals(orderID)) {
                        order = orderHistory;
                        orderFound = true;
                        break;
                    }
                }
            }
            if (!orderFound) {
                System.out.println("Order ID tidak dapat ditemukan!\n");
                continue;
            }

            // Assign status pengiriman sekarang
            boolean currentStatus = order.getOrderFinished();

            // Input status pengiriman
            System.out.print("Status Pengiriman: ");
            String statusPengiriman = input.nextLine().toLowerCase();

            // Mengganti status pengiriman
            if (statusPengiriman.equals("selesai")) {
                // Cek apakah status sama dengan sebelumnya
                if (currentStatus) {
                    System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");
                    continue;
                } else { 
                    order.setOrderFinished(true);
                    System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
                }
            } else if (statusPengiriman.equals("belum selesai") || statusPengiriman.equals("tidak selesai")) {
                // Cek apakah status sama dengan sebelumnya
                if (!currentStatus) {
                    System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");
                    continue;
                } else {
                    order.setOrderFinished(false);
                    System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
                }
            } else { // Jika input status tidak valid
                System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");
                continue;
            }

            break;
        }      
    }

/* ————————————————————— ADMIN ————————————————————— */

    public static void handleTambahRestoran() {
        System.out.println("---------------Tambah Restoran---------------");
        // Loop validasi TambahRestoran
        while (true) {
            // Input nama restoran
            System.out.print("Nama: ");
            String namaResto = input.nextLine();
            // Validasi jumlah karakter nama restoran
            if (namaResto.length() < 4) {
                System.out.println("Nama restoran tidak valid!\n");
                continue;
            }
            if (!restoList.isEmpty()) { // Jika list restoran tidak kosong
                boolean validNama = true;
                for (Restaurant resto : restoList) {
                    if (resto.getNama().equals(namaResto)) {
                        System.out.println("Restoran dengan nama " + namaResto + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda!\n");
                        validNama = false;
                    }
                }
                if (!validNama) {
                    continue;
                }
            }
            // Membuat restoran baru
            Restaurant newResto = new Restaurant(namaResto);
            restoList.add(newResto);

            // Input jumlah menu
            System.out.print("Jumlah Makanan: ");
            int jumlahMenu = input.nextInt();
            input.nextLine(); // Mengosongkan buffer
            // Iterasi untuk input menu, sekaligus validasi
            String[] menuSplit;
            String namaMenu;
            String hargaMenu;
            boolean validMenu = true;
            for (int i = 0; i < jumlahMenu; i++) {
                menuSplit = input.nextLine().split(" "); // Memecah ke array
                hargaMenu = menuSplit[menuSplit.length - 1]; // Elemen terakhir array
                // Validasi harga menu
                // try {
                //     double hargaMenuDouble = Double.parseDouble(hargaMenu);
                //     if (hargaMenuDouble <= 0) {
                //         validMenu = false;
                //     }
                //     namaMenu = String.join(" ", Arrays.copyOf(menuSplit, menuSplit.length - 1)); // Menggabungkan nama menu
                //     newResto.addMenu(namaMenu, hargaMenuDouble);
                // } catch (NumberFormatException e) {
                //     validMenu = false;
                // }
                if (hargaMenu == null || !hargaMenu.matches("[0-9]+")) {
                    validMenu = false;
                } else {
                    namaMenu = String.join(" ", Arrays.copyOf(menuSplit, menuSplit.length - 1)); // Menggabungkan nama menu
                    newResto.addMenu(namaMenu, Double.parseDouble(hargaMenu));
                }
            }
            // Validasi harga menu
            if (!validMenu) {
                System.out.println("Harga menu harus bilangan bulat!\n");
                // Mengosongkan array menu newResto jika harga menu tidak valid
                newResto.setMenu(new ArrayList<Menu>());
                // Menghapus restoran yang sudah dibuat
                restoList.remove(newResto);
                continue;
            }
            System.out.println("Restaurant " + namaResto + " berhasil terdaftar.");
            break;
        }

    }

    public static void handleHapusRestoran() {
        System.out.println("---------------Hapus Restoran---------------");
        // Loop validasi HapusRestoran
        while (true) {
            // Input nama restoran
            System.out.print("Nama: ");
            String namaResto = input.nextLine();
            // Validasi jumlah karakter nama restoran
            if (namaResto.length() < 4) {
                System.out.println("Nama restoran tidak valid!\n");
                continue;
            }
            // Mencari restoran untuk dihapus, sekaligus validasi
            boolean found = false;
            for (Restaurant resto : restoList) {
                if (resto.getNama().toLowerCase().equals(namaResto.toLowerCase())) { // Case insensitive
                    restoList.remove(resto); // Menghapus restoran dari list
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            System.out.println("Restoran berhasil dihapus.");
            break;
        }
    }

/* ————————————————————————————————————————————————— */

    public static void initUser() {
       userList = new ArrayList<User>();
       userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
       userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
       userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
       userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
       userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));

       userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
       userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }

    public static void printHeader() {
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu() {
        System.out.println("\nSelamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuAdmin() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

/* ———————————————— METHOD TAMBAHAN ———————————————— */

    /*
     * Method ini digunakan untuk membuat ID
     * dari nama restoran, tanggal order, dan nomor telepon
     */
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {

        // Mengambil 4 karakter pertama dari nama restoran
        String restoCode = namaRestoran.substring(0, 4).toUpperCase();

        // Menghilangkan slash (/) dari tanggal order
        String dateCode = tanggalOrder.replace("/", "");

        // Menambahkan semua angka di nomor telepon, kemudian modulo 100
        int phoneSum = 0;
        for (int i = 0; i < noTelepon.length(); i++) {
            phoneSum += Character.getNumericValue(noTelepon.charAt(i));
        }
        String phoneCode = String.valueOf(phoneSum % 100);
        phoneCode = phoneCode.length() == 1 ? "0" + phoneCode : phoneCode; // Jika 1 digit, tambahkan 0 di depannya

        // Membuat checksum
        String checksum = generateChecksum(restoCode + dateCode + phoneCode);

        // Menggabungkan semua kode di atas dan return
        return restoCode + dateCode + phoneCode + checksum;
    }

    /*
     * Method ini digunakan untuk mengenerate checksum dari string
     */
    public static String generateChecksum(String str) {
        int checksum1 = 0;
        int checksum2 = 0;

        // Mencari Code 39 dari setiap karakter pada string
        for (int i = 0; i < str.length(); i++) {
            /*
             * Jika karakter adalah angka, Code 39 adalah angka itu sendiri
             * Jika karakter adalah huruf, Code 39 adalah nilai ASCII - 55
             */
            int code39 = Character.isDigit(str.charAt(i)) ? Character.getNumericValue(str.charAt(i)) : (int) str.charAt(i) - 55;

            if (i % 2 == 0) {
                checksum1 += code39;
            } else {
                checksum2 += code39;
            }
        }

        checksum1 = checksum1 % 36; 
        checksum2 = checksum2 % 36;

        String checksum1Str; 
        String checksum2Str;
        
        /*
         * Meng-convert kembali dari Code 39 ke ASCII
         * 
         * Jika checksum < 10, maka hasilnya adalah angka itu sendiri
         * Jika checksum >= 10, maka hasilnya adalah huruf dengan nilai ASCII 55 + checksum
        */
        checksum1Str = (checksum1 < 10) ? String.valueOf(checksum1) : String.valueOf((char) (checksum1 + 55));
        checksum2Str = (checksum2 < 10) ? String.valueOf(checksum2) : String.valueOf((char) (checksum2 + 55));

        // Return gabungan checksum1 dan checksum2
        return checksum1Str + checksum2Str;
    }

    /*
     * Method ini digunakan untuk membuat bill dari order ID
     */
    public static String generateBill(String orderID) {
        // Mencari order dengan orderID yang diinput, sekaligus validasi
        Order order = null;
        User orderOwner = null;
        boolean orderFound = false;
        for (User user : userList) { // Mencari pemilik order
            for (Order orderHistory : user.getOrderHistory()) {
                if (orderHistory.getOrderID().equals(orderID)) {
                    order = orderHistory;
                    orderOwner = user;
                    orderFound = true;
                    break;
                }
            }
        }
        // Validasi order
        if (!orderFound) {
            return "Order ID tidak dapat ditemukan!";
        }

        // Return string bill
        return "\nBill:\n" +
               "Order ID: " + order.getOrderID() + "\n" +
               "Tanggal Pemesanan: " + order.getTanggalPemesanan() + "\n" +
               "Restaurant: " + order.getRestaurant().getNama() + "\n" +
               "Lokasi Pengiriman: " + orderOwner.getLokasi() + "\n" +
               "Status Pengiriman: " + order.statusPengiriman() + "\n" +
               "Pesanan:\n" + order.listMakanan() +
               "Biaya Ongkos Kirim: Rp " + order.getBiayaOngkosKirim() + "\n" +
               "Total Biaya: Rp " + String.format("%.0f", order.totalBiaya());
    }
}