package dev.subscripted.survivalsystem.utils;

public class DamageModifier {
    /**
     * Berechnet den Schadensmultiplikator basierend auf dem Clan-Level.
     *
     * @param clanLevel Der Clan-Level des Angreifers
     * @return Der Schadensmultiplikator (z.B. 1.05 für 5% mehr Schaden)
     */
    public static double getDamageMultiplier(int clanLevel) {
        if (clanLevel == 6) return 3.6;
        if (clanLevel == 5) return 1.80;
        if (clanLevel == 4) return 1.40;
        if (clanLevel == 3) return 1.20;
        if (clanLevel == 2) return 1.10;
        return 1.00; // Kein zusätzlicher Schaden
    }
}
