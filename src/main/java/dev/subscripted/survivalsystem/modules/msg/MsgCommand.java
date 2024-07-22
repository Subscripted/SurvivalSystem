package dev.subscripted.survivalsystem.modules.msg;

import dev.subscripted.survivalsystem.Main;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MsgCommand implements CommandExecutor {
    final Main plugin;
    final Map<Player, Player> lastMessaged = new HashMap<>();

    public MsgCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage("Usage: /msg <player> <message>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("Player not found or not online.");
            return true;
        }

        String message = Arrays.toString(args).replace(target.getName(), "");
        target.sendMessage(player.getName() + " -> you: " + message);
        player.sendMessage("You -> " + target.getName() + ": " + message);

        lastMessaged.put(player, target);
        lastMessaged.put(target, player);
        return true;
    }

    public Player getLastMessaged(Player player) {
        return lastMessaged.get(player);
    }
}
