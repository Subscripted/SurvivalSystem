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
    final Map<Player, Player> lastMessaged = new HashMap<>();
    final String prefix = "§x§C§5§C§3§7§5§lM§x§C§5§C§3§7§5§lS§x§C§5§C§3§7§5§lG §8▪ ";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(Main.getInstance().getPrefix() + "§7Nutze: §e/msg <spieler> <nachricht>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§Der Spieler §e" + args[0] + " §7ist §cnicht §7online!");
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        target.sendMessage(prefix + "§e" +  player.getName() + "§7 -> dir: §x§C§5§C§3§7§5" + message);
        player.sendMessage(prefix + "§7Du -> §e" + target.getName() + "§7: §x§C§5§C§3§7§5" + message);

        lastMessaged.put(player, target);
        lastMessaged.put(target, player);
        return true;
    }

    public Player getLastMessaged(Player player) {
        return lastMessaged.get(player);
    }
}
