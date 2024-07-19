package dev.subscripted.survivalsystem.modules.economy;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.utils.BankPaymentSerivce;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankUIListener implements Listener {

    final BankPaymentSerivce service;
    final SoundLibrary library;


    public BankUIListener(BankPaymentSerivce service, SoundLibrary library) {
        this.service = service;
        this.library = library;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        String title = event.getView().getTitle();
        if (!title.equals("§8» §8| §a§lBank")) return;

        event.setCancelled(true);

        if (clickedItem.getType() == Material.LIME_SHULKER_BOX) {
            player.closeInventory();
            service.addToAddingMoney(player);
            player.sendMessage(Main.getInstance().getPrefix() + "§aBitte gib den Betrag ein, den du einzahlen möchtest:");
            library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
        } else if (clickedItem.getType() == Material.RED_SHULKER_BOX) {
            player.closeInventory();
            service.addToRemovingMoney(player);
            player.sendMessage(Main.getInstance().getPrefix() + "§cBitte gib den Betrag ein, den du abheben möchtest:");
            library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (service.isAddingMoney(player)) {
            event.setCancelled(true);
            event.setMessage("");
            service.removeFromAddingMoney(player);

            try {
                int amount = Integer.parseInt(message);
                processDeposit(player, amount);
            } catch (NumberFormatException e) {
                player.sendMessage(Main.getInstance().getPrefix() + "§cUngültige Zahl, bitte versuche es erneut.");
                library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
            }
        } else if (service.isRemovingMoney(player)) {
            event.setCancelled(true);
            service.removeFromRemovingMoney(player);

            try {
                int amount = Integer.parseInt(message);
                processWithdrawal(player, amount);
            } catch (NumberFormatException e) {
                player.sendMessage(Main.getInstance().getPrefix() + "§cUngültige Zahl, bitte versuche es erneut.");
                library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
            }
        }
    }

    private void processDeposit(Player player, int amount) {
        Coins coins = Main.getInstance().getCoins();
        UUID playerUID = player.getUniqueId();
        int userbankcoins = coins.getBankCoins(playerUID);

        if (coins.getCoins(playerUID) >= amount) {
            coins.removeCoins(playerUID, amount);
            coins.depositToBank(playerUID, amount);
            player.sendMessage(Main.getInstance().getPrefix() + "§aDu hast erfolgreich " + amount + "€ auf dein Bankkonto eingezahlt.");
            player.sendMessage(Main.getInstance().getPrefix() + "§7Dein Kontostand beträgt nun §e" + userbankcoins + "€");
            library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Geld, um diesen Betrag einzuzahlen.");
            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
        }
    }

    private void processWithdrawal(Player player, int amount) {
        Coins coins = Main.getInstance().getCoins();
        UUID playerUID = player.getUniqueId();

        if (coins.getBankCoins(playerUID) >= amount) {
            coins.withdrawFromBank(playerUID, amount);
            coins.addCoins(playerUID, amount);
            player.sendMessage("§aDu hast erfolgreich " + amount + "€ von deinem Bankkonto abgehoben.");
        } else {
            player.sendMessage("§cDu hast nicht genug Geld auf deinem Bankkonto, um diesen Betrag abzuheben.");
        }
    }
}
