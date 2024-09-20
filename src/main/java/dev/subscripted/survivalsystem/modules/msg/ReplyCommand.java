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
    private static final String NOT_A_PLAYER_MSG = "Only players can use this command.";
    private static final String NO_REPLY_PLAYER_MSG = "No one to reply to or the player is offline.";
    private static final String USAGE_MSG = "Usage: /r <message>";

    final Main plugin;

    public ReplyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(NOT_A_PLAYER_MSG);
            return true;
        }

        Player player = (Player) sender;
        MsgCommand msgCommand = (MsgCommand) plugin.getCommand("msg").getExecutor();
        Player lastMessaged = msgCommand.getLastMessaged(player);

        if (lastMessaged == null || !lastMessaged.isOnline()) {
            player.sendMessage(NO_REPLY_PLAYER_MSG);
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(USAGE_MSG);
            return false;
        }

        String message = String.join(" ", args);
        lastMessaged.sendMessage(player.getName() + " -> you: " + message);
        player.sendMessage("You -> " + lastMessaged.getName() + ": " + message);

        return true;
    }
}
