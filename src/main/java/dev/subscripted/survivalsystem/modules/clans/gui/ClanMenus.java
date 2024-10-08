package dev.subscripted.survivalsystem.modules.clans.gui;

import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.modules.database.MySQL;
import dev.subscripted.survivalsystem.utils.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ClanMenus {

    final MySQL mySQL;
    final ClanManager clanManager;
    private static final Map<Integer, String> CLAN_LEVEL_ICONS = new HashMap<>();

    static {
        CLAN_LEVEL_ICONS.put(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBhNDcxZTM3YzUxM2ZkNzVmM2YxYWEzZWUyYzVkNzEwZjYxZGE1YmIxYTJjZTg0NDI2OWEyZTRkYjEyYTAwZCJ9fX0=");
        CLAN_LEVEL_ICONS.put(2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JiNzk5YWRmMTIxMDZlMDJhMjJmZDFmMDgzMzM3NTk0Y2JlY2Y3ZDQ4NTdlNDM2NDg1ODk1Yjc5NmFjZjIzMyJ9fX0=");
        CLAN_LEVEL_ICONS.put(3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTgyZDcyYjU2ZTY4NzliMmJkZjc5NTIyNGRkZmQyODRjMjI1MzQwNWQyMDRmNWJkYzZkZjEwNjVjYmUzY2RmOSJ9fX0=");
        CLAN_LEVEL_ICONS.put(4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQzMzJjZDgzNWQwNWJmY2EzZjBlZjQ0NWQ4MmNhMjEyY2Q5MTAyNDljYzczNzE2NTNhYjdiMjk3MGQxYzBmMyJ9fX0=");
        CLAN_LEVEL_ICONS.put(5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRmNmFhNGVmNjI0Y2YyZDgxNDkyMjJiOGU4MThjZWUzZTBiMDBiMzQxMzY2ZDI1NjBiMzE5ZmE0ZDk4NzA4ZCJ9fX0=");
        CLAN_LEVEL_ICONS.put(6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjAyYTExNjkzMDlmMDVlZjJmMDYxYjFmYTBmZTIyNWYyOWQ3M2EyNGY4ZjA3Y2NjMmE3MDVkZWVhY2EwNjlkMSJ9fX0=");
    }

    @SneakyThrows
    public void openClanMenu(Player player) {
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));
        String clanPrefix = clanManager.getClanPrefix(player.getUniqueId());
        String rang = clanManager.isClanOwner(player.getUniqueId()) ? "§c§lOwner" : "§7§lMitglied";
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(player, 6 * 9, "§x§6§0§6§0§6§0§lClan §8» " + clanName + " §8| §x§6§0§6§0§6§0§lRang §8» " + rang);
        int clanLevel = clanManager.getClanLevel(clanPrefix);
        String levelTexture = CLAN_LEVEL_ICONS.getOrDefault(clanLevel, CLAN_LEVEL_ICONS.get(1));
        ItemBuilder pattern = new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setDisplayName(" ");
        if (clanLevel == 6){
            pattern.addEnchant(Enchantment.SILK_TOUCH, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
        }
        ItemBuilder corners = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");

        ItemBuilder clanwar = new ItemBuilder(Material.CROSSBOW).setDisplayName("§x§6§0§6§0§6§0§lC§x§7§3§6§6§5§C§ll§x§8§6§6§C§5§8§la§x§9§8§7§2§5§4§ln§x§A§B§7§8§5§1§lk§x§B§E§7§D§4§D§lr§x§D§1§8§3§4§9§li§x§E§3§8§9§4§5§le§x§F§6§8§F§4§1§lg")
                .addLoreLine("§8» §7Der §cInhaber §7des Clans kann hier einen Clankrieg ")
                .addLoreLine("§8| §7zwischen seinem und dem §eGegnerclan §7verwalten§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Ist der Clankrieg vorbei, werden hier die §eStatistiken §7angezeigt§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §8§lStatus§8 : §c§lInaktiv");
        ItemBuilder settings = new ItemBuilder(Material.REPEATER).setDisplayName("§x§F§A§4§7§4§7§lE§x§F§6§4§E§4§E§li§x§F§3§5§5§5§5§ln§x§E§F§5§C§5§C§ls§x§E§C§6§3§6§3§lt§x§E§8§6§A§6§A§le§x§E§5§7§1§7§1§ll§x§E§1§7§8§7§8§ll§x§D§D§7§F§7§F§lu§x§D§A§8§6§8§6§ln§x§D§6§8§D§8§D§lg§x§D§3§9§4§9§4§le§x§C§F§9§B§9§B§ln")
                .addLoreLine("§8» §7Der §cInhaber §7des Clans kann hier den §eClan §7verwalten§8. ")
                .addLoreLine(" ")
                .addLoreLine("§8| §aMitglieder§7, §aClan§7, §aLöschen");
        ItemBuilder bank = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==").setDisplayName("§x§E§9§F§A§4§7§lC§x§E§8§F§6§5§C§ll§x§E§7§F§2§7§2§la§x§E§6§E§E§8§7§ln§x§E§6§E§9§9§D§lb§x§E§5§E§5§B§2§la§x§E§4§E§1§C§8§ln§x§E§3§D§D§D§D§lk")
                .addLoreLine("§8» §7Hier kannst du §eGeld §7in die §eClanbank §7einzahlen§8.")
                .addLoreLine("")
                .addLoreLine("§8| §7Das eingezahlte §eGeld §7kann von dem §cClaninhaber §7wieder §aausgezahlt §7werden")
                .addLoreLine("§7oder in die §eWeiterentwicklung §7des §aClanlevels §7gesteckt werden§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Deine Berechtigungen : " + (clanManager.isClanMember(player.getUniqueId()) ? "§aEinzahlen" : "§a§mEinzahlen") + " §8| " + (clanManager.isClanOwner(player.getUniqueId()) ? "§eAuszahlen" : "§e§mAuszahlen"));
        ItemBuilder clanMember = new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName("§b§lMitglieder")
                .setSkullOwner(UUIDFetcher.getName(clanManager.getClanOwner(clanPrefix)));

        ItemBuilder clanLevelItem = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(levelTexture).setDisplayName("§x§6§E§F§A§4§7§lC§x§7§D§F§6§5§A§ll§x§8§B§F§3§6§D§la§x§9§A§E§F§7§F§ln§x§A§9§E§C§9§2§ll§x§B§7§E§8§A§5§le§x§C§6§E§4§B§8§lv§x§D§4§E§1§C§A§le§x§E§3§D§D§D§D§ll")
                .addLoreLine(" §8• §eInformationen")
                .addLoreLine("§8| §7Hier kannst §edu §7das §aLevel §7deines §eClans §7einsehen§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Dein §eClan §7hat ein §eLevel §7von §e" + clanLevel + "§8.")
                .addLoreLine("§8| §7Der §cInhaber §7des §eClans§7, kann hier das §aClanlevel §7verwalten")
                .addLoreLine("§8| §7Deine Berechtigungen : " + (clanManager.isClanOwner(player.getUniqueId()) ? "§aZugriff" : "§cKeinen Zugriff"));


        InventoryAdvancer.makePattern(inventory, pattern);
        InventoryAdvancer.fillCorners(inventory, corners);
        inventory.setItem(13, clanwar.build());
        inventory.setItem(14, settings.build());
        inventory.setItem(22, bank.build());
        inventory.setItem(11, clanMember.build());
        inventory.setItem(40, clanLevelItem.build());
        player.openInventory(inventory);
    }


    @SneakyThrows
    public void openClanSettings(Player player) {
        String deleteClanValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==";
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));
        String clanPrefix = clanManager.getClanPrefix(player.getUniqueId());
        String rang = clanManager.isClanOwner(player.getUniqueId()) ? "§c§lOwner" : "§7§lMitglied";
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(player, 6 * 9, "§x§6§0§6§0§6§0§lClan §8» " + clanName + " §8| §x§6§0§6§0§6§0§lEinstellungen");
        ItemBuilder corners = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder fill = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder deleteclan = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(deleteClanValue).setDisplayName("§7Clan §cLöschen");
        ItemBuilder member = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(UUIDFetcher.getName(clanManager.getClanOwner(clanPrefix))).setDisplayName("§b§lMitglieder");
        InventoryAdvancer.fillCorners(inventory, corners);
        InventoryAdvancer.makePattern(inventory, fill);
        inventory.setItem(49, backitem());
        inventory.setItem(40, deleteclan.build());
        inventory.setItem(38, member.build());
        setClanSettingsMiddle(inventory, fill);


        player.openInventory(inventory);
    }

    @SneakyThrows
    public void openDeleteMenu(Player player) {
        String deleteClanYes = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=";
        String deleteClanNo = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==";
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));
        String clanPrefix = clanManager.getClanPrefix(player.getUniqueId());
        String rang = clanManager.isClanOwner(player.getUniqueId()) ? "§c§lOwner" : "§7§lMitglied";
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(player, 3 * 9, "§8Clan Löschen");
        ItemBuilder greystainedglass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder explain = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(exclamation()).setDisplayName("§6§lErklärung")
                .addLoreLine(" §8• §eInformationen")
                .addLoreLine("§8| §7Hier entscheidest §edu §7ob du deinen §eClan§8.")
                .addLoreLine(" ")
                .addLoreLine("§8» §cLöschen§8.")
                .addLoreLine(" ")
                .addLoreLine("§7 - oder§8.")
                .addLoreLine(" ")
                .addLoreLine("§8» §7nicht §eLöschen §7möchtest§8")
                .addLoreLine(" ")
                .addLoreLine("§8| §eKlicke §7auf das §aGrüne Häckchen§7, zum §elöschen §7des §eClans§8.")
                .addLoreLine("§8| §eKlicke §7auf das §cRote Kreuz§7, um den §eClan §7nicht zu §elöschen§8.");
        ItemBuilder delete = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(deleteClanYes).setDisplayName("§8» §7Clan §aLöschen");
        ItemBuilder dontdelete = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(deleteClanNo).setDisplayName("§8» §7Clan nicht §cLöschen");
        ItemBuilder green = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder red = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ");
        InventoryAdvancer.fillNulledInventory(greystainedglass, inventory);
        inventory.setItem(11, delete.build());
        inventory.setItem(15, dontdelete.build());
        inventory.setItem(4, explain.build());
        fillSoroundingsinDelete(inventory, green);
        fillSoroundingsinNoDelete(inventory, red);
        player.openInventory(inventory);
    }
    @SneakyThrows
    public void openClanLevel(Player player) {
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));
        String clanPrefix = clanManager.getClanPrefix(player.getUniqueId());
        UUID uuid = player.getUniqueId();
        int level = clanManager.getClanLevel(clanPrefix);
        int nextLevelCost = getLevelUpCost(level);
        int currentEco = clanManager.getClanEco(clanPrefix);

        double percentage = Math.min((currentEco / (double) nextLevelCost) * 100, 100);

        Inventory inventory = Bukkit.createInventory(player, 6 * 9, "§x§6§0§6§0§6§0§lClan §8» " + clanName + " §8| §x§6§0§6§0§6§0§lLevel: §e" + level);

        Material glassMaterial;
        switch (level) {
            case 1:
                glassMaterial = Material.LIME_STAINED_GLASS_PANE;
                break;
            case 2:
                glassMaterial = Material.GREEN_STAINED_GLASS_PANE;
                break;
            case 3:
                glassMaterial = Material.BLUE_STAINED_GLASS_PANE;
                break;
            case 4:
                glassMaterial = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                break;
            case 5:
                glassMaterial = Material.CYAN_STAINED_GLASS_PANE;
                break;
            case 6:
                glassMaterial = Material.ORANGE_STAINED_GLASS_PANE;
                break;
            default:
                glassMaterial = Material.WHITE_STAINED_GLASS_PANE;
                break;
        }

        ItemBuilder pattern = new ItemBuilder(glassMaterial).setDisplayName(" ");
        ItemBuilder corners = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");
        InventoryAdvancer.makePattern(inventory, pattern);
        InventoryAdvancer.fillCorners(inventory, corners);

        String levelTexture = CLAN_LEVEL_ICONS.getOrDefault(level, CLAN_LEVEL_ICONS.get(1));
        ItemBuilder clanLevelItem = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(levelTexture).setDisplayName("§a§lLevel");
        ItemBuilder upgrade = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(upgrade());

        if (level == 6) {
            upgrade.setDisplayName("§c§l§mUpgrade")
                    .addLoreLine("§8» §7Hier hast du die §eMöglichkeit §7das §eLevel §7deines §eClans §7zu §aerhöhen§8.")
                    .addLoreLine(" ")
                    .addLoreLine("§c§l! §7Dein §eClan §7ist schon auf dem §6Maximalen §eLevel§8.");
        } else {
            upgrade.setDisplayName("§a§lUpgrade")
                    .addLoreLine("§8» §7Hier hast du die §eMöglichkeit §7das §eLevel §7deines §eClans §7zu §aerhöhen§8.")
                    .addLoreLine(" ")
                    .addLoreLine("§8| §eKlicke §7um deinen §eClan §aaufzuleveln§8.")
                    .addLoreLine("§8| §7Dein Clan ist §aLevel " + level + "§8.")
                    .addLoreLine("§8| §7Kosten für das nächste Level: §e" + CoinFormatter.formatCoins(nextLevelCost) + "€§8.")
                    .addLoreLine("§8| §7Aktueller Stand der Clanbank: §e" + CoinFormatter.formatCoins(currentEco) + "€§8.")
                    .addLoreLine("§8| §7Bereits gesammelte %: §e" + String.format("%.2f", percentage) + "%§8.");
        }

        inventory.setItem(13, clanLevelItem.build());

        for (int i = 2; i <= 6; i++) {
            ItemBuilder shard = new ItemBuilder(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setDisplayName("§e§lLevel " + i).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).addItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS)
                    .addLoreLine("§8§l» §c§l\uD83D\uDDE1§7 : §e" + DamageModifier.getDamageMultiplier(i))
                    .addLoreLine(" ");

            if (i <= level) {
                shard.addLoreLine("§aFreigeschaltet");
                shard.addEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
            } else {
                shard.addLoreLine("§cNicht verfügbar");
            }
            setLevelItemShard(inventory, shard, i - 2);
        }
        inventory.setItem(49, backitem());
        inventory.setItem(31, upgrade.build());

        player.openInventory(inventory);
    }

    private int getLevelUpCost(int currentLevel) {
        return switch (currentLevel) {
            case 1 -> 100000;
            case 2 -> 500000;
            case 3 -> 600000;
            case 4 -> 700000;
            case 5 -> 1200000;
            default -> 0;
        };
    }


    @SneakyThrows
    public void openClanBank(Player player) {
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));
        String clanPrefix = clanManager.getClanPrefix(player.getUniqueId());
        String rang = clanManager.isClanOwner(player.getUniqueId()) ? "§c§lOwner" : "§7§lMitglied";
        UUID uuid = player.getUniqueId();
        int clanbankguthaben = clanManager.getClanEco(clanPrefix);
        String claneco = CoinFormatter.formatCoins(clanbankguthaben);
        Inventory inventory = Bukkit.createInventory(player, 6 * 9, "§x§6§0§6§0§6§0§lClanbank §8» " + clanName);

        ItemBuilder coins = new ItemBuilder(Material.PLAYER_HEAD)
                .setSkullTexture(valueOfCoins())
                .setDisplayName("§x§F§A§C§A§4§7§lC§x§E§E§B§C§4§3§ll§x§E§2§A§E§4§0§la§x§D§6§A§0§3§C§ln§x§C§B§9§2§3§8§lb§x§B§F§8§4§3§4§la§x§B§3§7§6§3§1§ln§x§A§7§6§8§2§D§lk")
                .addLoreLine("§8» §7Hier siehst du wie viel §eGeld §7auf der §eClanbank §cdeponiert §7ist§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Clanbankguthaben §8» §e" + claneco + "€");

        ItemBuilder pattern = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder corners = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");

        ItemBuilder minus = new ItemBuilder(Material.PLAYER_HEAD)
                .setSkullTexture(widhtdraw());
        if (clanManager.isClanOwner(player.getUniqueId())) {
            minus.setDisplayName("§c§lAuszahlen")
                    .addLoreLine("§8» §cGeld abheben")
                    .addLoreLine("§8| §7Klicke um §eGeld §7von deiner §eClanbank §7abzuheben§8.")
                    .addLoreLine(" ")
                    .addLoreLine("§8| §7Du wirst aufgefordert den gewünschten Betrag im §eChat §7zu schreiben§8.")
                    .addLoreLine("§8| §7Weitere Informationen zur Transaktion erhältst du im Chat§8.");
        } else {
            minus.setDisplayName("§c§l§mAuszahlen")
                    .addLoreLine("§8» §cGeld abheben")
                    .addLoreLine("§8| §7Du hast §ckeine §eBerechtigung §eGeld §7von der §eClanbank §cabzuheben§8.");
        }

        ItemBuilder plus = new ItemBuilder(Material.PLAYER_HEAD)
                .setSkullTexture(deposit())
                .setDisplayName("§a§lEinzahlen")
                .addLoreLine("§8» §aGeld lagern")
                .addLoreLine("§8| §7Klicke um §eGeld §7auf deiner §eClanbank §7zu §alagern§8.")
                .addLoreLine(" ")
                .addLoreLine("§8| §7Du wirst aufgefordert den gewünschten Betrag im §eChat §7zu schreiben§8.")
                .addLoreLine("§8| §7Weitere Informationen zur Transaktion erhältst du im Chat§8.");

        InventoryAdvancer.fillCorners(inventory, corners);
        InventoryAdvancer.makePattern(inventory, pattern);
        inventory.setItem(13, coins.build());
        inventory.setItem(49, backitem());
        inventory.setItem(29, plus.build());
        inventory.setItem(33, minus.build());

        player.openInventory(inventory);
    }

    @SneakyThrows
    public void openClanMemberMenu(Player player) {
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));
        String clanPrefix = clanManager.getClanPrefix(player.getUniqueId());
        String rang = clanManager.isClanOwner(player.getUniqueId()) ? "§c§lOwner" : "§7§lMitglied";
        UUID uuid = player.getUniqueId();
        int clanbankguthaben = clanManager.getClanEco(clanPrefix);
        String claneco = CoinFormatter.formatCoins(clanbankguthaben);
        List<UUID> clanMembers = clanManager.getClanMembers(clanPrefix);
        int membercount = clanMembers.size();

        Inventory inventory = Bukkit.createInventory(player, 6 * 9, "§x§6§0§6§0§6§0§lClan §8» " + clanName + " §8| §x§6§0§6§0§6§0§lMitglieder");

        ItemBuilder pattern = new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder corners = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");

        InventoryAdvancer.makePattern(inventory, pattern);
        InventoryAdvancer.fillCorners(inventory, corners);

        UUID clanOwnerUUID = clanManager.getClanOwner(clanPrefix);
        String clanOwnerName = UUIDFetcher.getName(clanOwnerUUID);
        ItemBuilder ownerHead = new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(clanOwnerName != null ? "§7" + clanOwnerName : "§7Unknown")
                .setSkullOwner(clanOwnerName != null ? clanOwnerName : "Steve")
                .addLoreLine((clanManager.isClanOwner(clanOwnerUUID) ? "§cOwner" : "§7Member"));
        inventory.setItem(13, ownerHead.build());

        clanMembers.remove(clanOwnerUUID);

        int[] memberSlots = {21, 22, 23, 28, 29, 30, 31, 32, 33, 34};
        int slotIndex = 0;

        for (UUID memberUUID : clanMembers) {
            if (slotIndex >= memberSlots.length) {
                break;
            }
            String memberName = UUIDFetcher.getName(memberUUID);
            ItemBuilder memberHead = new ItemBuilder(Material.PLAYER_HEAD)
                    .setDisplayName(memberName != null ? "§7" + memberName : "§7Unknown")
                    .setSkullOwner(memberName != null ? memberName : "Steve")
                    .addLoreLine("§7Rank: " + (clanManager.isClanOwner(memberUUID) ? "§cOwner" : "§7Member"));
            inventory.setItem(memberSlots[slotIndex], memberHead.build());
            slotIndex++;
        }

        inventory.setItem(49, backitem());
        player.openInventory(inventory);
    }

    @SneakyThrows
    public void openMemberManager(Player player, UUID memberUUID) {
        String memberName = UUIDFetcher.getName(memberUUID);
        String memberRank = clanManager.isClanOwner(memberUUID) ? "§c§lOwner" : "§7§lMitglied";
        String clanName = ChatColor.translateAlternateColorCodes('&', clanManager.getClanName(player.getUniqueId()));

        Inventory inventory = Bukkit.createInventory(player, 3 * 9, "§x§6§0§6§0§6§0§lVerwalten von Mitglied §8» §7" + (memberName != null ? memberName : "Unknown"));

        ItemBuilder memberHead = new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(memberName != null ? "§7" + memberName : "§7Unknown")
                .setSkullOwner(memberName != null ? memberName : "Steve")
                .addLoreLine(memberRank);

        ItemBuilder promoteItem = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(promote())
                .setDisplayName("§aBefördern zum Owner")
                .addLoreLine("§7Befördere dieses Mitglied zum Clan-Owner.");

        ItemBuilder kickItem = new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(widhtdraw())
                .setDisplayName("§cAus Clan werfen")
                .addLoreLine("§8| §7Werfe dieses Mitglied aus dem Clan.");

        inventory.setItem(11, memberHead.build());
        inventory.setItem(13, promoteItem.build());
        inventory.setItem(15, kickItem.build());

        player.openInventory(inventory);
    }



    public void setClanSettingsMiddle(Inventory inventory, ItemBuilder item) {
        int[] slots = {13, 22, 31, 30, 32, 39, 41};
        for (int slot : slots) {
            if (slot < inventory.getSize()) {
                inventory.setItem(slot, item.build());
            }
        }
    }

    public void fillSoroundingsinDelete(Inventory inventory, ItemBuilder item) {
        int[] slots = {0, 1, 2, 3, 9, 10, 12, 18, 19, 20, 21};
        for (int slot : slots) {
            if (slot < inventory.getSize()) {
                inventory.setItem(slot, item.build());
            }
        }
    }

    public void fillSoroundingsinNoDelete(Inventory inventory, ItemBuilder item) {
        int[] slots = {5, 6, 7, 8, 14, 16, 17, 23, 24, 25, 26};
        for (int slot : slots) {
            if (slot < inventory.getSize()) {
                inventory.setItem(slot, item.build());
            }
        }
    }

    public void setLevelItemShard(Inventory inventory, ItemBuilder item, int index) {
        int[] slots = {38, 39, 40, 41, 42};
        if (index < slots.length) {
            int slot = slots[index];
            if (slot < inventory.getSize()) {
                inventory.setItem(slot, item.build());
            }
        }
    }

    private String backHeadValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NzFkZDg4MWRiYWY0ZmQ2YmNhYTkzNjE0NDkzYzYxMmY4Njk2NDFlZDU5ZDFjOTM2M2EzNjY2YTVmYTYifX19";
    }

    private String exclamation() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJjM2U2NzdiYzM1ZmY4OGFiZDRiNmRjYTU0ZjAwYWRhNDQwNzA2YjExY2VjODEyOWM3Zjg4MGJkNjVmNjBkIn19fQ==";
    }

    private String valueOfCoins() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2QyNGRjNTgwNjljMTIxMmI1MjlhNGFlNWQ0ZTczYmUwOTkwZDQ2ZmU5MzcxYjFmNzllODE2NGI0Mjg1OWFjOCJ9fX0=";
    }

    private ItemStack backitem() {
        return new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(backHeadValue()).setDisplayName("§8» §cZurück").build();
    }

    private String upgrade() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19";
    }

    private String deposit() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRmZmYxYjNjNWQ4NWZlM2NkZDU2NTY4NjliYWEwZWFkZTVlNTNhY2E5ZDU2MTQyNzY0OGNjNzJmNWUyNWE5In19fQ==";
    }

    private String widhtdraw() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=";
    }
    private String promote(){
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U0ZjJmOTY5OGMzZjE4NmZlNDRjYzYzZDJmM2M0ZjlhMjQxMjIzYWNmMDU4MTc3NWQ5Y2VjZDcwNzUifX19";
    }
}
