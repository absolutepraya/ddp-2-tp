package assignments.assignment3.payment;

import assignments.assignment3.MainMenu;

public class CreditCardPayment implements DepeFoodPaymentSystem {
    private final double TRANSACTION_FEE_PERCENTAGE = 0.02;

    // Method untuk memindahkan saldo ke restoran, dan mereturn sisa saldo customer
    @Override
    public long processPayment(long amount) {
        long finalAmount = amount + countTransactionFee(amount);

        // Cek error (code: -3)
        if (MainMenu.getUserLoggedIn().getSaldo() < finalAmount) {
            return -3;
        }

        return finalAmount;
    }

    // Method untuk menghitung biaya transaksi
    private long countTransactionFee(long amount) {
        return (long) (amount * TRANSACTION_FEE_PERCENTAGE);
    }

    // Method untuk memindahkan saldo merupakan default method dari inteface DepeFoodPaymentSystem
}
