package dev.subscripted.survivalsystem.modules.vanish;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    VanishService service = new VanishService();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to perform this command!");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("survival.vanish")) {
            player.sendMessage("Keine Rechte!");
            return true;
        }

        if (!service.isVanished(player)) {
            service.setVanished(player);
            player.sendMessage("§aDu bist jetzt im Vanish!");
            for (Player allOnline : Bukkit.getOnlinePlayers()) {
                allOnline.sendMessage("§8[§c-§8] §7" + player.getDisplayName());
            }
        } else {
            service.removeFromVanish(player);
            player.sendMessage("§aDu bist jetzt nicht mehr im Vanish!");
            for (Player allOnline : Bukkit.getOnlinePlayers()) {
                allOnline.sendMessage("§8[§a+§8] §7" + player.getDisplayName());
            }
        }
        return true;
    }
}
