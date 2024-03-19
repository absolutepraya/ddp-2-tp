package assignments.assignment2;

import java.util.ArrayList;

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
}
