package assignments.assignment3.assignment2copy;

import java.util.ArrayList;

import assignments.assignment3.payment.DepeFoodPaymentSystem;

public class User {
    // Private attribute untuk class User
    private String nama;
    private String nomorTelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory;
    private DepeFoodPaymentSystem paymentSystem;
    private long saldo;

    // Constructor baru untuk assignment3
    public User(String nama, String nomorTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem paymentSystem, long saldo) {
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
        this.orderHistory = new ArrayList<Order>();
        this.paymentSystem = paymentSystem;
        this.saldo = saldo;
    }

    // Setter
    public void addOrderHistory(Order orderHistory) {
        this.orderHistory.add(orderHistory);
    }
    public void subtractSaldo(long amount) { // Subtractor
        this.saldo = saldo - amount;
    }

    // Getter
    public String getNama() {
        return this.nama;
    }
    public String getNomorTelepon() {
        return this.nomorTelepon;
    }
    public String getEmail() {
        return this.email;
    }
    public String getLokasi() {
        return this.lokasi;
    }
    public String getRole() {
        return this.role;
    }
    public ArrayList<Order> getOrderHistory() {
        return this.orderHistory;
    }
    public DepeFoodPaymentSystem getPaymentSystem() {
        return this.paymentSystem;
    }
    public long getSaldo() {
        return this.saldo;
    }

    // Method untuk menghitung biaya ongkir
    public int calculateOngkir() {
        if (this.lokasi.equals("P")) {
            return 10000;
        } else if (this.lokasi.equals("U")) {
            return 20000;
        } else if (this.lokasi.equals("T")) {
            return 35000;
        } else if (this.lokasi.equals("S")) {
            return 40000;
        } else if (this.lokasi.equals("B")) {
            return 60000;
        } else { // Untuk admin
            return 0;
        }
    }
}

