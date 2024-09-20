package dev.subscripted.survivalsystem.modules.gamemode;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class GamemodeSwitcher implements CommandExecutor, TabCompleter {

    static final String PLAYER_ONLY_MESSAGE = "This command can only be executed by a player";
    static final String PERMISSION_ERROR_MESSAGE = "§cDu hast keine Rechte auf diesen Command";
    static final String MODES_AVAILABLE_MESSAGE = "§7Du hast diese Spielmodi zur Auswahl: §ec, s, a, sp";

    final SoundLibrary library;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY_MESSAGE);
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Main.getInstance().getPrefix() + MODES_AVAILABLE_MESSAGE);
            library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
            return false;
        }
        if (player.hasPermission("survival.gamemode")) {
            String mode = args[0].toLowerCase();
            switch (mode) {
                case "c","1":
                    setNewMode(player, GameMode.CREATIVE, "§7Gamemode zu §eKreativ §7geändert");
                    break;
                case "s","0":
                    setNewMode(player, GameMode.SURVIVAL, "§7Gamemode zu §eÜberleben §7geändert");
                    break;
                case "a","4":
                    setNewMode(player, GameMode.ADVENTURE, "§7Gamemode zu §eAbenteuer §7geändert");
                    break;
                case "sp","3":
                    setNewMode(player, GameMode.SPECTATOR, "§7Gamemode zu §eBeobachter §7geändert");
                    break;
                default:
                    player.sendMessage(Main.getInstance().getPrefix() + "§ec, s, a, sp");
                    library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
                    return false;
            }
        }else {
            player.sendMessage(Main.getInstance().getPrefix() + PERMISSION_ERROR_MESSAGE);
            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
        }
        return true;
    }

    private void setNewMode(Player player, GameMode mode, String message){
        player.setGameMode(mode);
        player.sendMessage(Main.getInstance().getPrefix() + message);
        library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 2f);
    }


    private boolean isInCreativeMode(@NotNull Player player) {
        return player.getGameMode().equals(GameMode.CREATIVE);
    }

    private boolean isInSurvivalMode(@NotNull Player player) {
        return player.getGameMode().equals(GameMode.SURVIVAL);
    }

    private boolean isInSpectatorMode(@NotNull Player player) {
        return player.getGameMode().equals(GameMode.SPECTATOR);
    }

    private boolean isInAdventureMode(@NotNull Player player) {
        return player.getGameMode().equals(GameMode.ADVENTURE);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("survival.gamemode")) {
            List<String> completions = new ArrayList<>();
            completions.add("c");
            completions.add("1");
            completions.add("s");
            completions.add("0");
            completions.add("a");
            completions.add("4");
            completions.add("sp");
            completions.add("3");
            return completions;
        } else {
            return null;
        }
    }
}
