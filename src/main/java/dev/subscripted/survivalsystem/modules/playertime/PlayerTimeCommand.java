package dev.subscripted.survivalsystem.modules.playertime;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.UUIDFetcher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PlayerTimeCommand implements CommandExecutor {

    private static final String PLAYER_ONLY_COMMAND_MSG = "This command can only be executed by a player!";
    private static final String SELF_PLAY_TIME_MSG = "§7Du spielst schon seit §e%s §7auf diesem §eServer§7!";
    private static final String TARGET_PLAY_TIME_MSG = "§e%s §7spielt schon seit §e%s §7auf diesem §eServer§7!";
    private static final String UNKNOWN_PLAY_TIME = "Unbekannt"; // Alternativ: "0h 0m"

    final PlaytimeManager playtimeManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY_COMMAND_MSG);
            return true;
        }

        Player player = (Player) sender;
        String PLAY_TIME = playtimeManager.getFormattedPlaytime(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Main.getInstance().getPrefix() + String.format(SELF_PLAY_TIME_MSG, PLAY_TIME));
            return true;
        }

        String t = args[0];
        UUID tUUID = UUIDFetcher.getUUID(t);
        Player target = Bukkit.getServer().getPlayer(tUUID);

        String PLAY_TIME_OF_TARGET;
        if (target != null) {
            PLAY_TIME_OF_TARGET = playtimeManager.getFormattedPlaytime(target.getUniqueId());
        } else {
            PLAY_TIME_OF_TARGET = playtimeManager.getFormattedPlaytime(tUUID);
            if (PLAY_TIME_OF_TARGET == null || PLAY_TIME_OF_TARGET.equals("0h 0m")) {
                PLAY_TIME_OF_TARGET = UNKNOWN_PLAY_TIME;
            }
        }

        player.sendMessage(Main.getInstance().getPrefix() + String.format(TARGET_PLAY_TIME_MSG, t, PLAY_TIME_OF_TARGET));
        return true;
    }
}
