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

    final PlaytimeManager playtimeManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player!");
            return true;
        }

        Player player = (Player) sender;
        String PLAY_TIME = playtimeManager.getFormattedPlaytime(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Main.getInstance().getPrefix() + "§7Du spielst schon seit §e" + PLAY_TIME + " §7auf diesem §eServer§7!");
            return true;
        }

        String t = args[0];
        UUID tUUID = UUIDFetcher.getUUID(t);
        Player target = Bukkit.getServer().getPlayer(tUUID);

        // Hol die Spielzeit des Zielspielers, ob er online ist oder nicht
        String PLAY_TIME_OF_TARGET;
        if (target != null) {
            PLAY_TIME_OF_TARGET = playtimeManager.getFormattedPlaytime(target.getUniqueId());
        } else {
            // Falls der Zielspieler offline ist, versuche, seine Spielzeit aus der Konfiguration zu laden
            PLAY_TIME_OF_TARGET = playtimeManager.getFormattedPlaytime(tUUID);
            if (PLAY_TIME_OF_TARGET == null || PLAY_TIME_OF_TARGET.equals("0h 0m")) {
                PLAY_TIME_OF_TARGET = "Unbekannt"; // Alternativ: "0h 0m"
            }
        }

        player.sendMessage(Main.getInstance().getPrefix() + "§e" + t + " §7spielt schon seit §e" + PLAY_TIME_OF_TARGET + " §7auf diesem §eServer§7!");
        return true;
    }
}
