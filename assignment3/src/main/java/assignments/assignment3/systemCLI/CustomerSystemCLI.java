package assignments.assignment3.systemCLI;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import assignments.assignment3.MainMenu;
import assignments.assignment3.assignment2copy.*;
import assignments.assignment3.payment.*;

public class CustomerSystemCLI extends UserSystemCLI {

    @Override
    public boolean handleMenu(int choice) {
        switch(choice) {
            case 1 -> handleBuatPesanan();
            case 2 -> handleCetakBill();
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill();
            case 5 -> handleCekSaldo();
            case 6 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    @Override
    public void displayMenu() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    void handleBuatPesanan() {
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
            for (Restaurant resto : MainMenu.getRestoList()) {
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
            String orderID = Order.generateOrderID(namaResto, tanggalPesanan, MainMenu.getUserLoggedIn().getNomorTelepon());
            System.out.println("Pesanan dengan ID " + orderID + " diterima!");

            // Membuat object order (param: orderID, tanggalPesanan, ongkir, resto, items)
            Order newOrder = new Order(orderID, tanggalPesanan, MainMenu.getUserLoggedIn().calculateOngkir(), restoPesanan, listPesanan.toArray(new Menu[0]));
            // Menambahkan order ke orderHistory user
            MainMenu.getUserLoggedIn().addOrderHistory(newOrder);

            break;
        }
    }

    void handleCetakBill() {
        System.out.println("---------------Cetak Bill---------------");

        // Input order ID
        System.out.print("Masukkan Order ID: ");
        String orderID = input.nextLine();

        // Mencetak bill
        System.out.println(Order.generateBill(orderID));
    }

    void handleLihatMenu() {
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
            for (Restaurant resto : MainMenu.getRestoList()) {
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

    void handleBayarBill() {
        System.out.println("---------------Bayar Bill---------------");

        // Input order ID
        System.out.print("Masukkan Order ID: ");
        String orderID = input.nextLine();

        // Mencari order, sekaligus validasi order ID
        Order orderToPay = null;
        for (User user : MainMenu.getUserList()) {
            for (Order orderHistory : user.getOrderHistory()) {
                if (orderHistory.getOrderID().equals(orderID)) {
                    orderToPay = orderHistory;
                    break;
                }
            }
        }
        if (orderToPay == null) {
            System.out.println("Order ID tidak dapat ditemukan.");
            return;
        }
        // Cek apakah order sudah dibayar
        if (orderToPay.getOrderFinished()) {
            System.out.println("Pesanan dengan ID ini sudah lunas!");
            return;
        }

        // Mencetak bill
        System.out.println(Order.generateBill(orderID));

        // DEBUG
        // System.out.println("Metode Pembayaran: " + MainMenu.getUserLoggedIn().getPaymentSystem().getClass().getName());
        // Would print: "Metode Pembayaran: assignments.assignment3.payment.DebitPayment" or "Metode Pembayaran: assignments.assignment3.payment.CreditCardPayment"

        // Memilih metode pembayaran
        System.out.print("\nOpsi Pembayaran:\n" +
                         "1. Credit Card\n" + 
                         "2. Debit\n" +
                         "Pilihan Metode Pembayaran: ");
        String metodePembayaran = input.nextLine();

        // Mendapatkan payment system user dan total biaya order
        DepeFoodPaymentSystem paymentSystem = MainMenu.getUserLoggedIn().getPaymentSystem();
        
        long totalBiaya = (long) orderToPay.totalBiaya();
        long result;
        /* Value result bisa berarti error code, atau bisa berarti sisa saldo 
         * -1: Error karena metode pembayaran tidak cocok
         * -2: Error karena tidak memenuhi syarat minimum total price (untuk DebitPayment)
         * -3: Error karena saldo kurang
         * Else: Total biaya, tidak termasuk biaya transaksi (untuk CreditCardPayment)
        */
        // Proses pembayaran disertai validasi
        switch(metodePembayaran) {
            case "1": // Credit Card
                // Cek error (code: -1)
                if (MainMenu.getUserLoggedIn().getPaymentSystem() instanceof DebitPayment) {
                    result = -1;
                }

                result = paymentSystem.processPayment(totalBiaya);

                if (result == -1) {
                    System.out.println("User belum memiliki metode pembayaran ini!");
                    return;
                } else if (result == -2) {
                    // Pass
                } else if (result == -3) {
                    System.out.println("Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain");
                    return;
                } else {
                    paymentSystem.moveSaldo(orderToPay, totalBiaya);
                    System.out.println("\nBerhasil Membayar Bill sebesar Rp " + totalBiaya + " dengan biaya transaksi sebesar Rp " + (result - totalBiaya));
                }
                break;
            case "2": // Debit
                // Cek eror (code: -1)
                if (MainMenu.getUserLoggedIn().getPaymentSystem() instanceof CreditCardPayment) {
                    result = -1;
                }

                result = paymentSystem.processPayment(totalBiaya);

                if (result == -2) {
                    System.out.println("Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain");
                } else if (result == -3) {
                    System.out.println("Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain");
                    return;
                } else {
                    paymentSystem.moveSaldo(orderToPay, totalBiaya);
                    System.out.println("\nBerhasil Membayar Bill sebesar Rp " + totalBiaya);
                }
                break;
            default:
                System.out.println("Metode pembayaran tidak valid!");
                return;
        }
        // Set orderFinished menjadi true
        orderToPay.setOrderFinished(true);
    }

    public void handleCekSaldo() {
        // Mencetak saldo user
        System.out.println("\nSisa saldo sebesar Rp " + MainMenu.getUserLoggedIn().getSaldo());
    }  
}
