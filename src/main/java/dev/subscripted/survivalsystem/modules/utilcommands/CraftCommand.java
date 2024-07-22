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

    public CraftCommand(SoundLibrary library) {
        this.library = library;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }
        Player player = (Player) sender;
        if (player.hasPermission("survival.craft")) {

            player.openWorkbench(null, true);
            library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 1f);
            return true;
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast keine Rechte auf diesen Command");
        }
        return false;
    }
}
