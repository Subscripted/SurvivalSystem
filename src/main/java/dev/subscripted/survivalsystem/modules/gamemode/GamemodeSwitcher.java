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

    final SoundLibrary library;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast diese Spielmodi zur Auswahl: §ec, s, a, sp");
            library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
            return false;
        }
        if (player.hasPermission("survival.gamemode")) {
            String mode = args[0].toLowerCase();
            switch (mode) {
                case "c","1":
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(Main.getInstance().getPrefix() + "§7Gamemode zu §eKreativ §7geändert");
                    library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 2f);
                    break;
                case "s","0":
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(Main.getInstance().getPrefix() + "§7Gamemode zu §eÜberleben §7geändert");
                    library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 2f);
                    break;
                case "a","4":
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(Main.getInstance().getPrefix() + "§7Gamemode zu §eAbenteuer §7geändert");
                    library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 2f);
                    break;
                case "sp","3":
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(Main.getInstance().getPrefix() + "§7Gamemode zu §eBeobachter §7geändert");
                    library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 2f);
                    break;
                default:
                    player.sendMessage(Main.getInstance().getPrefix() + "§ec, s, a, sp");
                    library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);
                    return false;
            }
        }else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast keine Rechte auf diesen Command");
            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
        }
        return true;
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
