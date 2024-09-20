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
    private static final String PREFIX = "§x§C§5§C§3§7§5§lM§x§C§5§C§3§7§5§lS§x§C§5§C§3§7§5§lG §8▪ ";
    private static final String USAGE_MSG = "§7Nutze: §e/msg <spieler> <nachricht>";
    private static final String NOT_A_PLAYER_MSG = "Only players can use this command.";
    private static final String NOT_ONLINE_MSG = "§Der Spieler §e%s §7ist §cnicht §7online!";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(NOT_A_PLAYER_MSG);
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(Main.getInstance().getPrefix() + USAGE_MSG);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(String.format(NOT_ONLINE_MSG, args[0]));
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        target.sendMessage(PREFIX + "§e" +  player.getName() + "§7 -> dir: §x§C§5§C§3§7§5" + message);
        player.sendMessage(PREFIX + "§7Du -> §e" + target.getName() + "§7: §x§C§5§C§3§7§5" + message);

        lastMessaged.put(player, target);
        lastMessaged.put(target, player);
        return true;
    }

    public Player getLastMessaged(Player player) {
        return lastMessaged.get(player);
    }
}