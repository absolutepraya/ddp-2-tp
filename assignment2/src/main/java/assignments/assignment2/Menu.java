package assignments.assignment2;

public class Menu {
    // Private attribute untuk class Menu
    private String namaMakanan;
    private double harga;

    // Constructor
    public Menu(String namaMakanan, double harga) {
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    // Setter
    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }
    public void setHarga(double harga) {
        this.harga = harga;
    }

    // Getter
    public String getNamaMakanan() {
        return this.namaMakanan;
    }
    public double getHarga() {
        return this.harga;
    }
}
