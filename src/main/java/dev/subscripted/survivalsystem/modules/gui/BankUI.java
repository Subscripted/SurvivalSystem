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
        ItemBuilder prifile = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(playername).setDisplayName("§8» §b§lProfil")
                .addLoreLine(" §8• §8| §8» §7Dein Guthaben - §e" + CoinFormatter.formatCoins(playerCoins) + "€§r")
                .addLoreLine(" ")
                .addLoreLine(" §8• §8| §8» §7Dein Bankguthaben - §e" + CoinFormatter.formatCoins(bankeco) + "€§r");
        ItemBuilder deposit = new ItemBuilder(Material.LIME_SHULKER_BOX).setDisplayName("§a§lEinzahlen");
        ItemBuilder payoff = new ItemBuilder(Material.RED_SHULKER_BOX).setDisplayName("§c§lAuszahlen");
        ItemBuilder instruction = new ItemBuilder(Material.BOOK).setDisplayName("§6§lAnleitung")
                .addLoreLine(" §8• ")
                .addLoreLine(" §8• ")
                .addLoreLine(" §8• ")
                .addLoreLine(" §8• ");

        InventoryAdvancer.fillNulledInventory(nulled, inventory);
        inventory.setItem(11, deposit.build());
        inventory.setItem(15, payoff.build());
        inventory.setItem(13, instruction.build());
        inventory.setItem(31, prifile.build());

        library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f,1f);
        player.openInventory(inventory);

    }
}
