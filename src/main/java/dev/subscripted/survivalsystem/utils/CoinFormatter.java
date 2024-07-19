package dev.subscripted.survivalsystem.utils;

import java.text.DecimalFormat;

public class CoinFormatter {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public static String formatCoins(int coins) {
        return decimalFormat.format(coins);
    }
}
