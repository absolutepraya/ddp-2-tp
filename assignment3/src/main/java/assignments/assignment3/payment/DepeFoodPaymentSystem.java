package assignments.assignment3.payment;

import assignments.assignment3.MainMenu;
import assignments.assignment3.assignment2copy.Order;

public interface DepeFoodPaymentSystem {
    // Attribute
    long saldo = 0; // Tidak bisa digunakan di subclass karena bersifat final (constant)

    // Method untuk memproses pembayaran
    public long processPayment(long amount);

    // Default method untuk memindahkan saldo dari customer ke restoran
    default void moveSaldo(Order order, long amount) {
        MainMenu.getUserLoggedIn().subtractSaldo(amount);
        order.getRestaurant().addSaldo(amount);
    }
}
