package dev.subscripted.survivalsystem.utils;

import java.text.DecimalFormat;

public class CoinFormatter {
    private static final ThreadLocal<DecimalFormat> decimalFormat =
            ThreadLocal.withInitial(() -> new DecimalFormat("#,###"));

    public static String formatCoins(int coins) {
        return decimalFormat.get().format(coins);
    }
}