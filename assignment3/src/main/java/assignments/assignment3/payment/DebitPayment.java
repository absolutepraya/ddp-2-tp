package assignments.assignment3.payment;

import assignments.assignment3.MainMenu;

public class DebitPayment implements DepeFoodPaymentSystem {
    private final double MINIMUM_TOTAL_PRICE = 50000;

    // Method untuk memindahkan saldo ke restoran, dan mereturn sisa saldo customer
    @Override
    public long processPayment(long amount) {
        // Cek error (code: -2)
        if (amount < MINIMUM_TOTAL_PRICE) {
            return -2;
        }

        // Cek error (code: -3)
        if (MainMenu.getUserLoggedIn().getSaldo() < amount) {
            return -3;
        }
        
        return amount;
    }

    // Method untuk memindahkan saldo merupakan default method dari inteface DepeFoodPaymentSystem
}
