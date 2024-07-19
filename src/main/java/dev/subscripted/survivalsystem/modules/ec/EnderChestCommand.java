package dev.subscripted.survivalsystem.modules.ec;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnderChestCommand implements CommandExecutor {

    final SoundLibrary library;

    public EnderChestCommand(SoundLibrary library) {
        this.library = library;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("survival.ec")) {
            sender.sendMessage(Main.getInstance().getPrefix() + "§cDu hast auf diesen Command keine Rechte!");
            return true;
        }

        player.openInventory(player.getEnderChest());
        library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 3f);
        return false;
    }
}
