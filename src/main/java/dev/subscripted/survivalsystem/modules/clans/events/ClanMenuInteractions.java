package dev.subscripted.survivalsystem.modules.clans.events;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.clans.gui.ClanMenus;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import dev.subscripted.survivalsystem.utils.UUIDFetcher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

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
        String clanPrefix = clanManager.getClanPrefix(uuid);
        ItemStack clickedItem = event.getCurrentItem();
        int slot = event.getSlot();
        int level = clanManager.getClanLevel(clanPrefix);

        if (title.startsWith("§x§6§0§6§0§6§0§lClan §8»") && !title.endsWith(" §8| §x§6§0§6§0§6§0§lLevel: §e" + level) && !title.endsWith(" §8| §x§6§0§6§0§6§0§lMitglieder") && !title.endsWith(" §8| §x§6§0§6§0§6§0§lEinstellungen")) {
            event.setCancelled(true);
            if (clickedItem != null) {
                switch (slot) {
                    case 14:
                        if (!clanManager.isClanOwner(uuid)) {
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 2f);
                            return;
                        }
                        menus.openClanSettings(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                    case 40:
                        if (!clanManager.isClanOwner(uuid)) {
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 2f);
                            return;
                        }
                        menus.openClanLevel(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                    case 22:
                        menus.openClanBank(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;

                    case 11:
                        menus.openClanMemberMenu(player);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                        break;
                }
            }
        }
        if (title.endsWith(" §8| §x§6§0§6§0§6§0§lEinstellungen") && !title.startsWith("§x§6§0§6§0§6§0§lClanbank ")) {
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
                    case 38:
                        menus.openClanMemberMenu(player);
                        library.playLibrarySound(player, CustomSound.GUI_SOUND, 1f, 2f);
                        break;
                }
            }
        }
        if (title.equals("§8Clan Löschen")) {
            if (clickedItem == null) return;
            event.setCancelled(true);
            switch (slot) {
                case 11:
                    sendActionBar(player, "§7Du hast deinen Clan " + ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(uuid)) + " §7gelöscht");
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.sendMessage(" ");
                        p.sendMessage(Main.getInstance().getPrefix() + "§7Der Clan " + ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(uuid)) + " §7wurde aufgelöst!");
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
        if (title.startsWith("§x§6§0§6§0§6§0§lClanbank ")) {
            if (clickedItem == null) return;
            event.setCancelled(true);
            switch (slot) {
                case 49:
                    menus.openClanMenu(player);
                    library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                    break;

                case 29:
                    clanManager.addToAddingMoney(player);
                    player.closeInventory();
                    player.sendMessage(Main.getInstance().getPrefix() + "§aBitte gib den Betrag ein, den du einzahlen möchtest:");
                    library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
                    break;

                case 33:
                    if (clanManager.isClanOwner(player.getUniqueId())) {
                        clanManager.addToRemovingMoney(player);
                        player.closeInventory();
                        player.sendMessage(Main.getInstance().getPrefix() + "§aBitte gib den Betrag ein, den du auszahlen möchtest:");
                        library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
                        break;
                    } else {
                        library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                        return;
                    }
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

                case 31:
                    if (level < 6) {
                        try {
                            int levelUpCost = getLevelUpCost(level);
                            int currentEco = clanManager.getClanEco(clanPrefix);
                            if (currentEco < levelUpCost) {
                                library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                                sendActionBar(player, "§7Der §eClan §7besitzt nicht genug §eCoins §7für das nächste §aLevel");
                                return;
                            }
                            clanManager.removeMoneyFromClanBank(clanPrefix, levelUpCost);
                            clanManager.setClanToNextLevel(clanPrefix);
                            menus.openClanLevel(player);
                            int newlevel = Math.addExact(level, 1);

                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (clanManager.isMemberOfClan(p.getUniqueId(), clanPrefix)) {
                                    library.playLibrarySound(p, CustomSound.UPGRADING, 1f, 2f);
                                    sendTitle(p, "§7Dein Clan ist im §eLevel §aaufgestiegen", "§7Clanlevel §8• §e" + level + " §7-> §e" + newlevel);
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                    }
                    break;
            }
        }
        if (title.endsWith(" §8| §x§6§0§6§0§6§0§lMitglieder")) {
            if (clickedItem == null) return;
            event.setCancelled(true);
            if (slot >= 21 && slot <= 34) {
                UUID memberUUID = getMemberUUIDFromHead(clickedItem);
                if (memberUUID != null) {
                    if (clanManager.isClanOwner(uuid)) {
                        player.setMetadata("memberUUID", new FixedMetadataValue(Main.getInstance(), memberUUID.toString()));
                        menus.openMemberManager(player, memberUUID);
                        library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                    }
                }
            } else if (slot == 49) {
                menus.openClanMenu(player);
                library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
            }
        }
        if (title.startsWith("§x§6§0§6§0§6§0§lVerwalten von Mitglied")) {
            event.setCancelled(true);

            MetadataValue metadataValue = player.getMetadata("memberUUID").stream().findFirst().orElse(null);
            if (metadataValue != null) {
                UUID memberUUID = UUID.fromString(metadataValue.asString());
                switch (slot) {
                    case 13:
                        if (clanManager.isClanOwner(player.getUniqueId())) {
                            clanManager.grantToClanOwner(memberUUID, clanPrefix);
                            //
                            // player.sendMessage("§7Du hast §e" + UUIDFetcher.getName(memberUUID) + " §7zum neuen Owner befördert.");
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (clanManager.isMemberOfClan(p.getUniqueId(), clanPrefix)) {
                                    library.playLibrarySound(p, CustomSound.UPGRADING, 1f, 2f);
                                    sendTitle(p, "§7Dein Clan hat einen neuen Owner", "§7Alter §cInhaber §8• §e" + player.getName() + " §8| §7Neuer §cInhaber §7-> §e" + UUIDFetcher.getName(memberUUID));
                                }
                            }
                            //
                            menus.openClanMemberMenu(player);
                            library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                            player.removeMetadata("memberUUID", Main.getInstance());
                        } else {
                            player.sendMessage("§cDu hast keine Berechtigung, dieses Mitglied zu befördern.");
                        }
                        break;
                    case 15:
                        if (clanManager.isClanOwner(player.getUniqueId())) {
                            clanManager.removeMemberFromClan(clanPrefix, memberUUID);

                            player.sendMessage("§7Du hast §e" + UUIDFetcher.getName(memberUUID) + " §7aus dem Clan geworfen.");
                            menus.openClanMemberMenu(player);
                            library.playLibrarySound(player, CustomSound.PAGE_TURN, 1f, 2f);
                            player.removeMetadata("memberUUID", Main.getInstance());
                        } else {
                            player.sendMessage("§cDu hast keine Berechtigung, dieses Mitglied aus dem Clan zu werfen.");
                        }
                        break;
                }
            }
        }
    }

    private int getLevelUpCost(int currentLevel) {
        switch (currentLevel) {
            case 1:
                return 100000;
            case 2:
                return 500000;
            case 3:
                return 600000;
            case 4:
                return 700000;
            case 5:
                return 1200000;
            default:
                return 0;
        }
    }


    public void sendActionBar(Player player, String message) {
        player.sendActionBar(Main.getInstance().getPrefix() + message);
    }

    public void sendTitle(Player player, String title1, String title2) {
        player.sendTitle(title1, title2);
    }

    private UUID getMemberUUIDFromHead(ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;

        String displayName = ChatColor.stripColor(meta.getDisplayName());
        try {
            return UUIDFetcher.getUUID(displayName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }



}
