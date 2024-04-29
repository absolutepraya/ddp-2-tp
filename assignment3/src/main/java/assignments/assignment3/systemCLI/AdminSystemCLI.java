package assignments.assignment3.systemCLI;

import java.util.Arrays;

import assignments.assignment3.MainMenu;
import assignments.assignment3.assignment2copy.Restaurant;

public class AdminSystemCLI extends UserSystemCLI {

    @Override
    public boolean handleMenu(int command) {
        switch(command){
            case 1 -> handleTambahRestoran();
            case 2 -> handleHapusRestoran();
            case 3 -> {return false;}
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    @Override
    public void displayMenu() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    protected void handleTambahRestoran() {
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
            // Validasi jika restoran sudah ada
            if (!MainMenu.getRestoList().isEmpty()) {
                boolean validNama = true;
                for (Restaurant resto : MainMenu.getRestoList()) {
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
            MainMenu.addRestoList(newResto);

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
                hargaMenu = menuSplit[menuSplit.length - 1]; // Elemen terakhir array adalah harga
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
                newResto.getMenu().clear();
                // Menghapus restoran yang sudah dibuat
                MainMenu.removeRestoList(newResto);
                continue;
            }
            System.out.println("Restaurant " + namaResto + " berhasil terdaftar.");
            break;
        }
    }

    protected void handleHapusRestoran() {
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
            for (Restaurant resto : MainMenu.getRestoList()) {
                if (resto.getNama().toLowerCase().equals(namaResto.toLowerCase())) { // Case insensitive
                    MainMenu.removeRestoList(resto); // Menghapus restoran dari list
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
}
