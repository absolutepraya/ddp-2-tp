package assignments.assignment2;

import java.util.ArrayList;

public class Restaurant {
    // Private attribute untuk class Restaurant
    private String nama;
    private ArrayList<Menu> menu;

    // Constructor untuk class Restaurant, saat dibuat di menu Admin
    public Restaurant(String nama) {
        this.nama = nama;
        this.menu = new ArrayList<Menu>();
    }

    // Setter
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }
    public void addMenu(String namaMakanan, double harga) {
        Menu newMenu = new Menu(namaMakanan, harga);
        this.menu.add(newMenu);
    }

    // Getter
    public String getNama() {
        return this.nama;
    }
    public ArrayList<Menu> getMenu() {
        return this.menu;
    }

    // Method untuk menampilkan menu diurutkan dari harga terendah ke tertinggi
    public void printMenu() {
        // BUBBLE SORT ALGORITHM
        ArrayList<Menu> sortedMenu = this.menu;
        for (int i = 0; i < sortedMenu.size(); i++) { // Iterate setiap item di menu
            for (int j = i + 1; j < sortedMenu.size(); j++) { // Iterate setiap item di menu setelah item i
                if (sortedMenu.get(i).getHarga() > sortedMenu.get(j).getHarga()) { // Jika harga item i lebih besar dari harga item j (item selanjutnya)
                    Menu temp = sortedMenu.get(i); // Object temporary untuk menyimpan item i
                    sortedMenu.set(i, sortedMenu.get(j)); // Meletakkan item j di index lokasi item i
                    sortedMenu.set(j, temp); // Meletakkan item i di index lokasi item j
                }
            }
        }
        // Print menu yang sudah diurutkan
        System.out.println("Menu:");
        for (int i = 0; i < sortedMenu.size(); i++) { // Iterate setiap item di menu
            System.out.println((i + 1) + ". " + sortedMenu.get(i).getNamaMakanan() + " " + String.format("%.0f", sortedMenu.get(i).getHarga()));
        }
    }
}
