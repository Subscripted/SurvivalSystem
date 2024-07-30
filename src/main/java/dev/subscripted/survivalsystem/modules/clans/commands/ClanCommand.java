package dev.subscripted.survivalsystem.modules.clans.commands;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.clans.gui.ClanMenus;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.utils.CoinFormatter;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ClanCommand implements CommandExecutor, TabCompleter {

    final ClanManager manager;
    final ClanMenus menus;
    final SoundLibrary library;

    private static final Pattern VALID_CLAN_PREFIX_PATTERN = Pattern.compile("^[a-zA-Z&]*$");
    private static final Pattern VALID_CLAN_NAME_PATTERN = Pattern.compile("^[a-zA-Z& ]*$");

    @Override
    @SneakyThrows
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        Coins coins = Main.getInstance().getCoins();
        int buyprice = 50000;

        if (args.length == 0) {
            if (manager.isClanMember(playerUUID)) {
                menus.openClanMenu(player);
                library.playLibrarySound(player, CustomSound.CLAN_OPEN, 1f, 2f);
                return true;
            } else {
                sendActionBar(player, "");
            }
            sendActionBar(player, "");
        }

        String subcommand = args[0].toLowerCase();
        try {
            switch (subcommand) {
                case "create":
                    if (args.length == 3) {
                        String clanPrefix = args[1];
                        String clanName = args[2];

                        String clanNameStripped = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', clanName));

                        if (clanPrefix.length() > 12 || clanNameStripped.length() > 12) {
                            sendActionBar(player, "§7Der Clan-Prefix und Name dürfen maximal 12 Zeichen lang sein!");
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                            return true;
                        }

                        if (!isValidClanPrefix(clanPrefix) || !isValidClanName(clanName)) {
                            sendActionBar(player, "§7Der Clan-Prefix und Name dürfen keine isolierten Zahlen enthalten!");
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                            return true;
                        }

                        if (manager.clanExists(clanPrefix)) {
                            sendActionBar(player, "§7Ein Clan mit dem Prefix §r" + clanPrefix + " §7existiert bereits!");
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 2f);
                            return true;
                        }

                        if (manager.isClanNameTaken(clanName)) {
                            sendActionBar(player, "§7Ein Clan mit dem Namen §r" + clanName.replace("&", "§") + " §7existiert bereits!");
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 2f);
                            return true;
                        }

                        if (manager.isClanMember(playerUUID)) {
                            sendActionBar(player, "§7Du bist schon Mitglied in einem Clan!");
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 2f);
                            return true;
                        }

                        int playerCoins = coins.getCoins(playerUUID);
                        if (playerCoins < buyprice) {
                            int mCoins = buyprice - playerCoins;
                            String missingCoins = CoinFormatter.formatCoins(mCoins);
                            sendActionBar(player, "§7Du hast nicht genügend Geld, um einen Clan zu erstellen! Du benötigst noch §e" + missingCoins + " €.");
                            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                            return true;
                        }

                        manager.createClan(playerUUID, clanPrefix, clanName);
                        coins.removeCoins(playerUUID, buyprice);
                        sendActionBar(player, "§7Du hast erfolgreich deinen Clan gegründet! §8(§7Name: " + ChatColor.translateAlternateColorCodes('&', clanName) + " §8| §7Prefix: §e" + clanPrefix + "§8)");
                        library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                    } else {
                        sendActionBar(player, "§7Benutze §e/clan create §c<prefix> <name>");
                        library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
                    }
                    break;
                case "myclan":
                    String clanInfo = manager.getClan(playerUUID);
                    if (clanInfo != null) {
                        sendActionBar(player, "§7Du bist in diesem Clan: §e" + ChatColor.translateAlternateColorCodes('&', clanInfo.replace("-", "|")));
                        library.playLibrarySound(player, CustomSound.LOADING_FINISHED, 1f, 1f);
                    } else {
                        sendActionBar(player, "§7Du bist in keinem Clan!");
                        library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                    }
                    break;
                case "leave":
                    if (args.length == 2) {
                        String clanPrefix = args[1];
                        String currentClan = manager.getMemberClan(playerUUID);
                        String clanName = manager.getClanName(playerUUID);

                        if (currentClan != null && currentClan.equals(clanPrefix)) {
                            if (manager.isOwnerOfClan(playerUUID)) {
                                sendActionBar(player, "§7Du kannst deinen eigenen Clan nicht verlassen §8- §eTransferiere §7die Führung an ein §eMitglied §7oder §cLösche §7den Clan!");
                                library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                            } else {
                                sendActionBar(player, "§7Du hast den Clan §e" + (clanName != null ? ChatColor.translateAlternateColorCodes('&', clanName) : "N/A") + " §7verlassen!");
                                library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                                manager.leaveClan(playerUUID);
                            }
                        } else {
                            sendActionBar(player, "§7Du bist nicht im Clan - §e" + clanPrefix);
                            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                        }
                    } else {
                        sendActionBar(player, "§7Benutze §7/clan leave §c<clanPrefix>");
                        library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
                    }
                    break;
                case "delete":
                    if (args.length == 2) {
                        String clanPrefix = args[1];
                        String currentClan = manager.getMemberClan(playerUUID);
                        String clanName = manager.getClanName(playerUUID);

                        if (currentClan != null && currentClan.equals(clanPrefix)) {
                            if (manager.isOwnerOfClan(playerUUID)) {
                                sendActionBar(player, "§7Du hast deinen Clan " + ChatColor.translateAlternateColorCodes('&', clanName != null ? clanName : "N/A") + " §7gelöscht");
                                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                    p.sendMessage(" ");
                                    p.sendMessage(Main.getInstance().getPrefix() + "§7Der Clan " + ChatColor.translateAlternateColorCodes('&', clanName != null ? clanName : "N/A") + " §7wurde aufgelöst!");
                                    p.sendMessage(" ");
                                }
                                library.playSoundForAll(CustomSound.WARNING, 1f, 2f);
                                manager.deleteClan(clanPrefix);
                            } else if ((player.hasPermission("survival.clans.delete")) && !manager.isOwnerOfClan(playerUUID)) {
                                sendActionBar(player, "§7Du hast deinen Clan " + ChatColor.translateAlternateColorCodes('&', clanName != null ? clanName : "N/A") + " §7gelöscht");
                                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                    p.sendMessage(" ");
                                    p.sendMessage(Main.getInstance().getPrefix() + "§7Der Clan " + ChatColor.translateAlternateColorCodes('&', clanName != null ? clanName : "N/A") + " §7wurde von einem §c§lTeammitglied aufgelöst!");
                                    p.sendMessage(" ");
                                }
                            } else {
                                sendActionBar(player, "§7Du hast keine Berechtigung diesen Clan zu löschen!");
                                library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
                            }
                        } else {
                            sendActionBar(player, "§7Du bist nicht im Clan - §e" + clanPrefix);
                            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                        }
                    } else {
                        sendActionBar(player, "§7Benutze §7/clan delete §c<clanPrefix>");
                        library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
                    }
                    break;
                default:
                    player.sendMessage("Usage: /clan create <prefix> <name> | /clan myclan | /clan leave <prefix> | /clan delete <prefix>");
            }
        } catch (SQLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while executing the command.");
        }

        return true;
    }

    @Override
    @Nullable
    @SneakyThrows
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (args.length == 1) {
            if (!manager.isMemberOfClan(playerUUID)) {
                tab.add("create");
            } else if (manager.isMemberOfClan(playerUUID)) {
                tab.add("myclan");
                tab.add("leave");
            }
            if (manager.isClanMember(playerUUID) || player.hasPermission("survival.clans.delete") || manager.isClanOwner(playerUUID)) {
                tab.add("delete");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") && player.hasPermission("survival.clans.delete")) {
                try {
                    tab.addAll(manager.getClans());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return tab;
    }

    private boolean isValidClanPrefix(String prefix) {
        return VALID_CLAN_PREFIX_PATTERN.matcher(prefix).matches() && !prefix.matches(".*\\d+.*");
    }

    private boolean isValidClanName(String name) {
        return VALID_CLAN_NAME_PATTERN.matcher(name).matches() && !name.matches(".*\\d+.*");
    }

    public void sendActionBar(Player player, String message) {
        player.sendActionBar(Main.getInstance().getPrefix() + message);
    }
}
