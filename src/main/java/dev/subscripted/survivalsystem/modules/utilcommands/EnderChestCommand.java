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
public class EnderChestCommand implements CommandExecutor {

    final SoundLibrary library;
    private static final String NOT_A_PLAYER_MSG = "You must be a player to use this command!";
    private static final String NO_COMMAND_ACCESS_MSG = "§cDu hast auf diesen Command keine Rechte!";

    public EnderChestCommand(SoundLibrary library) {
        this.library = library;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(NOT_A_PLAYER_MSG);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("survival.ec")) {
            sender.sendMessage(Main.getInstance().getPrefix() + NO_COMMAND_ACCESS_MSG);
            return true;
        }

        player.openInventory(player.getEnderChest());
        library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 3f);
        return true;
    }
}