package dev.subscripted.survivalsystem.modules.vanish;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    private static final String NOT_A_PLAYER_MSG = "You must be a player to perform this command!";
    private static final String NO_PERMISSION_MSG = "Keine Rechte!";
    private static final String VANISH_ON_MSG = "§aDu bist jetzt im Vanish!";
    private static final String VANISH_OFF_MSG = "§aDu bist jetzt nicht mehr im Vanish!";

    VanishService service = new VanishService();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(NOT_A_PLAYER_MSG);
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("survival.vanish")) {
            player.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        if (!service.isVanished(player)) {
            performVanishOn(player);
        } else {
            performVanishOff(player);
        }
        return true;
    }

    private void performVanishOn(Player player) {
        service.setVanished(player);
        player.sendMessage(VANISH_ON_MSG);
        broadcastMessageToOnlinePlayers("§8[§c-§8] §7" + player.getDisplayName());
    }

    private void performVanishOff(Player player) {
        service.removeFromVanish(player);
        player.sendMessage(VANISH_OFF_MSG);
        broadcastMessageToOnlinePlayers("§8[§a+§8] §7" + player.getDisplayName());
    }

    private void broadcastMessageToOnlinePlayers(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
