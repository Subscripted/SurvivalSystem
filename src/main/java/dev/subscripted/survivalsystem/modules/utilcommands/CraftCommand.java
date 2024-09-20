package dev.subscripted.survivalsystem.modules.utilcommands;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CraftCommand implements CommandExecutor {

    final SoundLibrary library;

    private static final String NOT_A_PLAYER_MSG = "This command can only be executed by a player";
    private static final String NO_COMMAND_ACCESS_MSG = "§cDu hast keine Rechte auf diesen Command";

    public CraftCommand(SoundLibrary library) {
        this.library = library;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(NOT_A_PLAYER_MSG);
            return true;
        }
        Player player = (Player) sender;
        if (player.hasPermission("survival.craft")) {
            player.openWorkbench(null, true);
            library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 1f);
            return true;
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + NO_COMMAND_ACCESS_MSG);
        }
        return false;
    }
}