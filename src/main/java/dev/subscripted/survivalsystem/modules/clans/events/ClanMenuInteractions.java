package dev.subscripted.survivalsystem.modules.clans.events;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.clans.gui.ClanMenus;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ClanMenuInteractions implements Listener {

    final ClanManager clanManager;
    final SoundLibrary library;
    final ClanMenus menus;

    @SneakyThrows
    @EventHandler
    public void onClanMenuInteraction(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        String title = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        String rang = clanManager.isClanOwner(uuid) ? "§c§lOwner" : "§7§lMitglied";
        String clanPrefix = clanManager.getClanPrefix(uuid);
        String clanName = clanManager.getClanName(uuid);
        ItemStack clickedItem = event.getCurrentItem();
        int slot = event.getSlot();
        int level = clanManager.getClanLevel(clanPrefix);


        if (title.startsWith("§x§6§0§6§0§6§0§lClan §8»")) {
            event.setCancelled(true);
            if (clickedItem != null) {
                switch (slot) {
                    case 14:
                        System.out.println("Slot 14 Clicked in Clan Menu");
                        if (!clanManager.isClanOwner(uuid)) {
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 2f);
                            return;
                        }
                        menus.openClanSettings(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                    case 40:
                        menus.openClanLevel(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                    case 22:
                        menus.openClanBank(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                }
            }
        }
        if (title.endsWith(" §x§6§0§6§0§6§0§lEinstellungen")) {
            event.setCancelled(true);
            if (clickedItem != null) {
                switch (slot) {
                    case 40:
                        menus.openDeleteMenu(player);
                        library.playLibrarySound(player, CustomSound.GUI_SOUND, 1f, 2f);
                        break;
                    case 49:
                        menus.openClanMenu(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                }
            }
        }
        if (title.equals("§8Clan Löschen")) {
            if (clickedItem == null) return;
            event.setCancelled(true);
            switch (slot) {
                case 11:
                    sendActionBar(player, "§7Du hast deinen Clan " + ChatColor.translateAlternateColorCodes('&', clanName != null ? clanName : "N/A") + " §7gelöscht");
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.sendMessage(" ");
                        p.sendMessage(Main.getInstance().getPrefix() + "§7Der Clan " + ChatColor.translateAlternateColorCodes('&', clanName != null ? clanName : "N/A") + " §7wurde aufgelöst!");
                        p.sendMessage(" ");
                    }
                    player.closeInventory();
                    library.playSoundForAll(CustomSound.WARNING, 1f, 2f);
                    clanManager.deleteClan(clanPrefix);
                    break;
                case 15:
                    menus.openClanSettings(player);
                    library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                    break;
            }
        }
        if (title.endsWith(" §8| §x§6§0§6§0§6§0§lBank")) {
            if (clickedItem == null) return;
            event.setCancelled(true);
            switch (slot) {
                case 49:
                    menus.openClanMenu(player);
                    library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                    break;
            }
        }
        if (title.endsWith(" §8| §x§6§0§6§0§6§0§lLevel: §e" + level)) {
            if (clickedItem == null) return;
            event.setCancelled(true);
            switch (slot) {
                case 49:
                    menus.openClanMenu(player);
                    library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                    break;
            }
        }
    }

    public void sendActionBar(Player player, String message) {
        player.sendActionBar(Main.getInstance().getPrefix() + message);
    }
}
