package dev.subscripted.survivalsystem.modules.fly;

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
public class FlyCommand implements CommandExecutor {

    final FlyService service;
    final SoundLibrary library;

    public FlyCommand(FlyService service, SoundLibrary library) {
        this.service = service;
        this.library = library;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission("survival.fly")) {
            if (!service.isInFlyMode(player)) {
                service.setFlying(player);
                library.playLibrarySound(player, CustomSound.ACTIVATED, 1f, 2f);
            } else {
                service.unsetFlying(player);
                library.playLibrarySound(player, CustomSound.DEACTIVATED, 1f, 2f);
            }
            return false;
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast keine Rechte auf diesen Command!");
            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
            return true;
        }
    }
}
