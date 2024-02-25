package assignments.assignment1;

import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    /*
     * Method ini digunakan untuk mengenerate checksum dari string
     * 
     * @param str String yang akan di-generate checksum-nya
     * @return String checksum dari str
     */
    public static String generateChecksum(String str) {
        int checksum1 = 0; int checksum2 = 0;

        // Mencari Code 39 dari setiap karakter pada string
        for (int i = 0; i < str.length(); i++) {
            /*
             * Jika karakter adalah angka, code39 adalah angka itu sendiri
             * Jika karakter adalah huruf, code39 adalah nilai ASCII - 55
             */
            int code39 = Character.isDigit(str.charAt(i)) ? Character.getNumericValue(str.charAt(i)) : (int) str.charAt(i) - 55;

            if (i % 2 == 0) {
                checksum1 += code39;
            } else {
                checksum2 += code39;
            }
        }
        checksum1 = checksum1 % 36; checksum2 = checksum2 % 36;
    
        // Meng-convert kembali dari Code 39 ke ASCII
        String checksum1Str; String checksum2Str;
        if (checksum1 < 10) {
            checksum1Str = String.valueOf(checksum1);
        } else {
            checksum1Str = String.valueOf((char) (checksum1 + 55));
        }
        if (checksum2 < 10) {
            checksum2Str = String.valueOf(checksum2);
        } else {
            checksum2Str = String.valueOf((char) (checksum2 + 55));
        }

        // Return gabungan checksum1 dan checksum2
        return checksum1Str + checksum2Str;
    }

    /*
     * Method  ini untuk menampilkan menu
     */
    public static void showMenu(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.err.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
        System.out.println("Pilih menu:");
        System.err.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
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
            if (Character.isDigit(noTelepon.charAt(i))) {
                phoneSum += Character.getNumericValue(noTelepon.charAt(i));
            }
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
    public static String generateBill(String OrderID, String lokasi){
        String biaya;
        if (lokasi.equals("P")) {biaya = "10.000";}
        else if (lokasi.equals("U")) {biaya = "20.000";}
        else if (lokasi.equals("T")) {biaya = "35.000";}
        else if (lokasi.equals("S")) {biaya = "40.000";}
        else {biaya = "60.000";}

        return "Bill: \n" +
               "Order ID: " + OrderID + "\n" +
               "Tanggal Pemesanan: " + OrderID.substring(4, 6) + "/" + OrderID.substring(6, 8) + "/" + OrderID.substring(0, 4) + "\n" +
               "Lokasi Pengiriman: " + lokasi + "\n" +
               "Biaya Ongkos Kirim: Rp" + biaya;
    }

    public static void main(String[] args) {
        // TODO: Implementasikan program sesuai ketentuan yang diberikan
    }

    
}
