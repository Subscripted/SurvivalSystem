package dev.subscripted.survivalsystem.modules.economy;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.utils.BankPaymentSerivce;
import dev.subscripted.survivalsystem.utils.CoinFormatter;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankUIListener implements Listener {

    final BankPaymentSerivce service;
    final SoundLibrary library;

    private static final int DEPOSIT_SLOT = 11;
    private static final int WITHDRAW_SLOT = 15;

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

        int slot = event.getSlot();

        if (slot == DEPOSIT_SLOT) {
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                player.closeInventory();
                service.addToAddingMoney(player);
                player.sendMessage(Main.getInstance().getPrefix() + "§aBitte gib den Betrag ein, den du einzahlen möchtest:");
                library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
            }
        } else if (slot == WITHDRAW_SLOT) {
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                player.closeInventory();
                service.addToRemovingMoney(player);
                player.sendMessage(Main.getInstance().getPrefix() + "§cBitte gib den Betrag ein, den du abheben möchtest:");
                library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (service.isAddingMoney(player)) {
            event.setCancelled(true);
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

        // Formatierung der Beträge
        String coinspayedin = CoinFormatter.formatCoins(amount);

        if (coins.getCoins(playerUID) >= amount) {
            coins.removeCoins(playerUID, amount);
            coins.depositToBank(playerUID, amount);

            // Neuer Kontostand nach der Einzahlung
            String UserBankCoins = CoinFormatter.formatCoins(coins.getBankCoins(playerUID));

            player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast erfolgreich §e" + coinspayedin + "€ §7auf dein Bankkonto eingezahlt.");
            player.sendMessage(Main.getInstance().getPrefix() + "§7Dein Kontostand beträgt nun §e" + UserBankCoins + "€");
            library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Geld, um diesen Betrag einzuzahlen.");
            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
        }
    }


    private void processWithdrawal(Player player, int amount) {
        Coins coins = Main.getInstance().getCoins();
        UUID playerUID = player.getUniqueId();

        // Formatierung der Beträge
        String coinspayedin = CoinFormatter.formatCoins(amount);

        if (coins.getBankCoins(playerUID) >= amount) {
            coins.withdrawFromBank(playerUID, amount);
            coins.addCoins(playerUID, amount);

            // Neuer Kontostand nach der Abhebung
            String UserBankCoins = CoinFormatter.formatCoins(coins.getBankCoins(playerUID));

            player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast erfolgreich §e" + coinspayedin + "€ §7von deinem Bankkonto abgehoben.");
            player.sendMessage(Main.getInstance().getPrefix() + "§7Dein Kontostand beträgt nun §e" + UserBankCoins + "€");
            library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Geld auf deinem Bankkonto, um diesen Betrag abzuheben.");
            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
        }
    }

}
