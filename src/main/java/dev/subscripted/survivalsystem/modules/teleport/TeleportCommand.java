package dev.subscripted.survivalsystem.modules.teleport;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeleportCommand implements CommandExecutor, TabCompleter {
    Main plugin;
    SoundLibrary library;
    Map<UUID, UUID> teleportRequests = new HashMap<>();

    public TeleportCommand(Main plugin, SoundLibrary library) {
        this.plugin = plugin;
        this.library = library;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                if (targetPlayer != null) {
                    if (!targetPlayer.equals(player)) {
                        sendTeleportRequest(player, targetPlayer);
                    } else {
                        player.sendMessage(Main.getInstance().getPrefix() + "§cDu kannst dir selber keine Anfrage senden!");
                        library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);

                    }
                } else {
                    player.sendMessage(Main.getInstance().getPrefix() + "§cDer Spieler §e" + targetPlayerName + " §cist nicht online oder existiert nicht.");
                    library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                }
            } else {
                player.sendMessage(Main.getInstance().getPrefix() + "§cBenutzung: /tpa §e<spieler>");
                library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
            }
            return true;
        } else if (label.equalsIgnoreCase("tpaaccept")) {
            acceptTeleportRequest(player);
            return true;
        } else if (label.equalsIgnoreCase("tpadeny")) {
            denyTeleportRequest(player);
            return true;
        } else if (label.equalsIgnoreCase("tp")) {
            if (args.length == 1) {
                String targetName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetName);
                if (targetPlayer != null) {
                    player.teleport(targetPlayer);
                    player.sendMessage(Main.getInstance().getPrefix() + "§aDu wurdest zu §e" + targetPlayer.getName() + " §ateleportiert!");
                    library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                } else {
                    player.sendMessage(Main.getInstance().getPrefix() + "§cDer Spieler §e" + targetName + " §cist nicht online oder existiert nicht.");
                    library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                }
            } else if (args.length == 3) {
                try {
                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);
                    Location location = new Location(player.getWorld(), x, y, z);
                    player.teleport(location);
                    player.sendMessage(Main.getInstance().getPrefix() + "§7Du wurdest zu §e" + x + ", " + y + ", " + z + " §7teleportiert");
                    library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                } catch (NumberFormatException e) {
                    player.sendMessage(Main.getInstance().getPrefix() + "§cInvalide Koordinatenzahl, bitte gib eine gültige Zahl an.");
                    library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                }
            } else {
                player.sendMessage(Main.getInstance().getPrefix() + "§cBenutzung: /tp <spieler> §eoder §c/tp <x> <y> <z>");
                library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
            }
            return true;
        }

        return false;
    }

    private void sendTeleportRequest(Player sender, Player target) {
        this.teleportRequests.put(target.getUniqueId(), sender.getUniqueId());
        sender.sendMessage(Main.getInstance().getPrefix() + "§7Teleportationsanfrage an §e" + target.getName() + " §7gesendet!");
        library.playLibrarySound(sender, CustomSound.SUCCESSFULL, 1f, 1f);
        target.sendMessage(Main.getInstance().getPrefix() + "§7Teleportationsanfrage von §e" + sender.getName() + " §7erhalten! Benutze §a/tpaaccept " + sender.getName() + " §7zum Annehmen oder §c/tpadeny " + sender.getName() + " §7zum Ablehnen.");
        library.playLibrarySound(target, CustomSound.QUESTION, 1f, 1f);
        startRequestTimer(target.getUniqueId());
    }

    private void acceptTeleportRequest(Player player) {
        UUID playerId = player.getUniqueId();
        if (this.teleportRequests.containsKey(playerId)) {
            UUID senderId = this.teleportRequests.get(playerId);
            Player sender = Bukkit.getPlayer(senderId);
            if (sender != null) {
                player.teleport(sender.getLocation());
                player.sendMessage(Main.getInstance().getPrefix() + "§aTeleportationsanfrage von §e" + sender.getName() + " §aangenommen!");
                library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                this.teleportRequests.remove(playerId);
            } else {
                player.sendMessage(Main.getInstance().getPrefix() + "§cDer Spieler, der die Teleportationsanfrage gesendet hat, ist nicht mehr online.");
                library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
            }
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast keine ausstehenden Teleportationsanfragen.");
            library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
        }
    }

    private void denyTeleportRequest(Player player) {
        UUID playerId = player.getUniqueId();
        if (this.teleportRequests.containsKey(playerId)) {
            UUID senderId = this.teleportRequests.get(playerId);
            Player sender = Bukkit.getPlayer(senderId);
            if (sender != null) {
                player.sendMessage(Main.getInstance().getPrefix() + "§7Teleportationsanfrage von §e" + sender.getName() + " §7abgelehnt!");
                library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                sender.sendMessage(Main.getInstance().getPrefix() + player.getName() + " hat deine Teleportationsanfrage abgelehnt.");
                library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
            }
            this.teleportRequests.remove(playerId);
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast keine ausstehenden Teleportationsanfragen.");
            library.playLibrarySound(player, CustomSound.QUESTION, 1f, 1f);
        }
    }

    private void startRequestTimer(UUID targetId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (teleportRequests.containsKey(targetId)) {
                    Player target = Bukkit.getPlayer(targetId);
                    if (target != null) {
                        UUID senderId = teleportRequests.get(targetId);
                        Player sender = Bukkit.getPlayer(senderId);
                        if (sender != null) {
                            sender.sendMessage(Main.getInstance().getPrefix() + "§cDie Teleportationsanfrage an §e" + target.getName() + " §cist abgelaufen.");
                            library.playLibrarySound(sender, CustomSound.NO_PERMISSION, 1f, 1f);
                        }
                    }
                    teleportRequests.remove(targetId);
                }
            }
        }.runTaskLater(plugin, 20 * 60);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    completions.add(onlinePlayer.getName());
                }
            }
        } else if (command.getName().equalsIgnoreCase(Main.getInstance().getPrefix() + "tpaaccept") || command.getName().equalsIgnoreCase(Main.getInstance().getPrefix() + "tpadeny")) {
            if (args.length == 1) {
                for (UUID targetId : teleportRequests.keySet()) {
                    Player target = Bukkit.getPlayer(targetId);
                    if (target != null) {
                        completions.add(target.getName());
                    }
                }
            }
        }

        return completions;
    }
}
