package dev.subscripted.survivalsystem.utils;

import java.util.Random;

public class ChanceService {
    private static final Random random = new Random();

    /**
     * Überprüft, ob ein Ereignis mit der gegebenen Wahrscheinlichkeit eintritt.
     *
     * @param chance Die Wahrscheinlichkeit in Prozent (0-100)
     * @return true, wenn das Ereignis eintritt, sonst false
     */
    public static boolean checkChance(double chance) {
        return random.nextDouble() < (chance / 100.0);
    }
}
