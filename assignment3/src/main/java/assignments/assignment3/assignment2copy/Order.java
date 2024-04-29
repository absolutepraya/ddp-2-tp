package assignments.assignment3.assignment2copy;

import java.util.ArrayList;

import assignments.assignment3.MainMenu;

public class Order {
    // Private attribute untuk class Order
    private String orderID;
    private String tanggalPemesanan;
    private int biayaOngkosKirim;
    private Restaurant restaurant;
    private ArrayList<Menu> items;
    private boolean orderFinished = false; // Default value

    // Constructor
    public Order(String orderID, String tanggal, int ongkir, Restaurant resto, Menu[] items) {
        this.orderID = orderID;
        this.tanggalPemesanan = tanggal;
        this.biayaOngkosKirim = ongkir;
        this.restaurant = resto;
        this.items = new ArrayList<Menu>();
        for (Menu item : items) {
            this.items.add(item);
        }
    }

    // Setter
    public void setOrderFinished(boolean orderFinished) {
        this.orderFinished = orderFinished;
    }

    // Getter
    public String getOrderID() {
        return this.orderID;
    }
    public String getTanggalPemesanan() {
        return this.tanggalPemesanan;
    }
    public int getBiayaOngkosKirim() {
        return this.biayaOngkosKirim;
    }
    public Restaurant getRestaurant() {
        return this.restaurant;
    }
    public boolean getOrderFinished() {
        return this.orderFinished;
    }

    // Method untuk me-return status pesanan
    public String statusPengiriman() {
        if (this.orderFinished) {
            return "Finished";
        } else {
            return "Not Finished";
        }
    }

    // Method untuk me-return list makanan yang dipesan
    public String listMakanan() {
        String listMakanan = "";
        for (Menu menu : this.items) {
            listMakanan += "- " + menu.getNamaMakanan() + " " + String.format("%.0f", menu.getHarga()) + "\n";
        }
        return listMakanan;
    }

    // Method untuk menghitung total harga pesanan
    public double totalBiaya() {
        double totalHarga = 0;
        for (Menu menu : this.items) {
            totalHarga += menu.getHarga();
        }
        return totalHarga + this.biayaOngkosKirim;
    }

    // Method untuk membuat order ID
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

    // Method untuk membuat checksum
    private static String generateChecksum(String str) {
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

    public static String generateBill(String orderID) {
        // Mencari order dengan orderID yang diinput, sekaligus validasi
        Order order = null;
        User orderOwner = null;
        boolean orderFound = false;
        for (User user : MainMenu.getUserList()) { // Mencari pemilik order
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
            return "Order ID tidak dapat ditemukan.";
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
