package dev.subscripted.survivalsystem.modules.gui;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.utils.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankUI {

    public static void bankingGUI(Player player) {

        SoundLibrary library = Main.getInstance().getLibrary();
        Coins coins = Main.getInstance().getCoins();
        String playername = player.getName();
        UUID playerUID = player.getUniqueId();
        int playerCoins = coins.getCoins(playerUID);
        int bankeco = coins.getBankCoins(playerUID);

        Inventory inventory = Bukkit.createInventory(player, 4 * 9, "§8» §8| §a§lBank");

        ItemBuilder nulled = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder profile = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(playername).setDisplayName("§8» §b§lProfil")
                .addLoreLine(" §8• §8| §8» §7Dein Guthaben - §e" + CoinFormatter.formatCoins(playerCoins) + "€§r")
                .addLoreLine(" ")
                .addLoreLine(" §8• §8| §8» §7Dein Bankguthaben - §e" + CoinFormatter.formatCoins(bankeco) + "€§r");

        String depositTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19";
        ItemBuilder deposit = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(depositTexture)
                .setDisplayName("§a§lEinzahlen")
                .addLoreLine("§8» §aGeld lagern")
                .addLoreLine("§8| §7Klicke um §eGeld §7auf deinem Konto zu §alagern§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Du wirst aufgefordert den Gewünschten Betrag im §eChat §7zu schreiben§8.")
                .addLoreLine("§8| §7Weiter Informationen zur Transaktion erhältst du im Chat§8.");

        String payoffTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=";
        ItemBuilder payoff = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(payoffTexture)
                .setDisplayName("§c§lAuszahlen")
                .addLoreLine("§8» §cGeld abheben")
                .addLoreLine("§8| §7Klicke um §eGeld §7von deinem Konto abzuheben§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Du wirst aufgefordert den Gewünschten Betrag im §eChat §7zu schreiben§8.")
                .addLoreLine("§8| §7Weiter Informationen zur Transaktion erhältst du im Chat§8.");

        ItemBuilder instruction = new ItemBuilder(Material.BOOK).setDisplayName("§6§lAnleitung")
                .addLoreLine(" §8• §eInformationen")
                .addLoreLine("§8| §7Hier kannst du dein §eGeld §7lagern§8.")
                .addLoreLine("§8| §7und es auch wieder abheben§8. ")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Lagerst du dein §eGeld §7, verlierst du es nicht wenn du z.B. stirbst§8. ")
                .addLoreLine(" ")
                .addLoreLine("§8| §e§oKlicke§r §7um dein Geld zu lagern auf das §aGrüne Plus§8.")
                .addLoreLine("§8| §e§oKlicke§r §7um dein Geld abzuheben auf das §cRote Minus§8.");

        InventoryAdvancer.fillNulledInventory(nulled, inventory);
        inventory.setItem(11, deposit.build());
        inventory.setItem(15, payoff.build());
        inventory.setItem(13, instruction.build());
        inventory.setItem(31, profile.build());

        library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 1f);
        player.openInventory(inventory);
    }
}

