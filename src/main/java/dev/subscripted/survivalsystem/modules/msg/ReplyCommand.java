package dev.subscripted.survivalsystem.modules.msg;

import dev.subscripted.survivalsystem.Main;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyCommand implements CommandExecutor {
     final Main plugin;

    public ReplyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        MsgCommand msgCommand = (MsgCommand) plugin.getCommand("msg").getExecutor();
        Player lastMessaged = msgCommand.getLastMessaged(player);

        if (lastMessaged == null || !lastMessaged.isOnline()) {
            player.sendMessage("No one to reply to or the player is offline.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("Usage: /r <message>");
            return false;
        }

        String message = Arrays.toString(args).replace(player.getName(), "");
        lastMessaged.sendMessage(player.getName() + " -> you: " + message);
        player.sendMessage("You -> " + lastMessaged.getName() + ": " + message);

        return true;
    }
}
