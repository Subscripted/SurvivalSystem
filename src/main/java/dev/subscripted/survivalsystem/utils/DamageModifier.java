package dev.subscripted.survivalsystem.utils;

public class DamageModifier {
    public static double getDamageMultiplier(int clanLevel) {
        switch(clanLevel) {
            case 6:
                return 3.6;
            case 5:
                return 1.80;
            case 4:
                return 1.40;
            case 3:
                return 1.20;
            case 2:
                return 1.10;
            default:
                return 1.00;
        }
    }
}