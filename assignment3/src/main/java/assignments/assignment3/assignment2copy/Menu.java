package assignments.assignment3.assignment2copy;

public class Menu {
    // Private attribute untuk class Menu
    private String namaMakanan;
    private double harga;

    // Constructor
    public Menu(String namaMakanan, double harga) {
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    // Setter tidak dibutuhkan

    // Getter
    public String getNamaMakanan() {
        return this.namaMakanan;
    }
    public double getHarga() {
        return this.harga;
    }
}
