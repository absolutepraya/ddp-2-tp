package assignments.assignment1;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

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
     * Method  ini untuk menampilkan header text
     */
    public static void showHeader(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
    }

    /*
     * Method  ini untuk menampilkan menu
     */
    public static void showMenu(){
        System.out.println("Pilih menu:");
        System.out.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
        System.out.print("--------------------------------------------\n");
    }

    /*
     * Method ini digunakan untuk membuat ID
     * dari nama restoran, tanggal order, dan nomor telepon
     * 
     * @return String Order ID dengan format sesuai pada dokumen soal
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
     * Method ini digunakan untuk membuat bill
     * dari order id dan lokasi
     * 
     * @return String Bill dengan format sesuai di bawah:
     *          Bill:
     *          Order ID: [Order ID]
     *          Tanggal Pemesanan: [Tanggal Pemesanan]
     *          Lokasi Pengiriman: [Kode Lokasi]
     *          Biaya Ongkos Kirim: [Total Ongkos Kirim]
     */
    public static String generateBill(String OrderID, String lokasi) {
        String biaya;
        lokasi = lokasi.toUpperCase(); // Memastikan lokasi dalam huruf besar

        // Set biaya berdasarkan lokasi
        if (lokasi.equals("P")) {biaya = "10.000";}
        else if (lokasi.equals("U")) {biaya = "20.000";}
        else if (lokasi.equals("T")) {biaya = "35.000";}
        else if (lokasi.equals("S")) {biaya = "40.000";}
        else {biaya = "60.000";}

        // Return string bill
        return "Bill:\n" +
               "Order ID: " + OrderID + "\n" +
               "Tanggal Pemesanan: " + OrderID.substring(4, 6) + "/" + OrderID.substring(6, 8) + "/" + OrderID.substring(8, 12) + "\n" +
               "Lokasi Pengiriman: " + lokasi + "\n" +
               "Biaya Ongkos Kirim: Rp " + biaya + "\n";
    }

    public static void main(String[] args) {
        showHeader();

        while (true) {
            // Menampilkan menu
            showMenu();
            // Meminta input dari user
            System.out.print("Pilih menu: ");
            String pilihan = input.nextLine();

            if (pilihan.equals("1")) { // Pilihan 1: Order ID
                // Pengulangan input jika validasi gagal
                while (true) {
                    // Input nama restoran
                    System.out.print("Nama Restoran: ");
                    String namaRestoran = input.nextLine();
                    // Validasi nama restoran
                    if (namaRestoran.length() < 4) {
                        System.out.println("Nama restoran tidak valid!\n");
                        continue;
                    }

                    // Input tanggal order
                    System.out.print("Tanggal Pemesanan: ");
                    String tanggalOrder = input.nextLine();
                    // Validasi tanggal order menggunakan java.time dan try-catch
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate.parse(tanggalOrder, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Tanggal pemesanan dalam format DD/MM/YYYY!\n");
                        continue;
                    }

                    // Input nomor telepon
                    System.out.print("No. Telepon: ");
                    String noTelepon = input.nextLine();
                    // Validasi nomor telepon dengan mengecek setiap karakter
                    boolean isDigit = true;
                    for (char c : noTelepon.toCharArray()) {
                        if (!Character.isDigit(c)) {
                            System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif.\n");
                            isDigit = false;
                            break;
                        }
                    }
                    if (!isDigit) {continue;}

                    // Menampilkan Order ID
                    System.out.println("Order ID " + generateOrderID(namaRestoran, tanggalOrder, noTelepon) + " diterima!");
                    break;
                }
            } 
            
            else if (pilihan.equals("2")) { // Pilihan 2: Bill
                String orderID;
                String lokasi;

                // Pengulangan input jika validasi Order ID gagal
                while (true) {
                    // Input dan validasi Order ID
                    System.out.print("Order ID: ");
                    orderID = input.nextLine();
                    if (orderID.length() != 16) { // Cek panjang
                        System.out.println("Order ID minimal 16 karakter!\n");
                        continue;
                    }
                    // Check apakah tanggal yang tertera di Order ID valid
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                        LocalDate.parse(orderID.substring(4, 12), formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Silahkan masukkan Order ID yang valid!\n");
                        continue;
                    }
                    // Check apakah checksum valid
                    if (!generateChecksum(orderID.substring(0, 14)).equals(orderID.substring(14, 16))) {
                        System.out.println("Silahkan masukkan Order ID yang valid!\n");
                        continue;
                    }
                    break;
                }

                // Pengulangan input jika validasi lokasi gagal
                while (true) {
                    // Input dan validasi lokasi pengiriman
                    System.out.print("Lokasi Pengiriman: ");
                    lokasi = input.nextLine();
                    lokasi = lokasi.toUpperCase();
                    if (!lokasi.equals("P") && !lokasi.equals("U") && !lokasi.equals("T") && !lokasi.equals("S") && !lokasi.equals("L")) {
                        System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!\n");
                        System.out.println("Order ID: " + orderID);
                        continue;
                    }
                    break;
                }

                // Menampilkan Bill
                System.out.println('\n' + generateBill(orderID, lokasi));
            } 

            else if (pilihan.equals("3")) { // Pilihan 3: Keluar
                break;
            } 

            else { // Validasi pilihan
                System.out.println("Pilihan tidak valid!");
            }

            System.out.print("--------------------------------------------\n");
        }

        // Closing, terima kasih!
        input.close();
        System.out.println("Terima kasih telah menggunakan DepeFood!");
    }
}