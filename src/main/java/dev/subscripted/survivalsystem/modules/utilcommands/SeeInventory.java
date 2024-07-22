package dev.subscripted.survivalsystem.modules.utilcommands;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeeInventory implements CommandExecutor {

    final SoundLibrary library;

    public SeeInventory(SoundLibrary library) {
        this.library = library;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("seeinventory")) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(Main.getInstance().getPrefix() + "§cUsage: /seeinventory <player>");
            library.playLibrarySound(player, CustomSound.WRONG_USAGE, 1f, 1f);

            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().getPrefix() + "You must be a player to use this command!");
            return true;
        }

        if (!player.hasPermission("survival.use") || !player.hasPermission("survival.all")) {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast keine Rechte auf diesen Command");
            library.playLibrarySound(player, CustomSound.NO_PERMISSION, 1f, 1f);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(Main.getInstance().getPrefix() + "§7Der Spieler §e" + target.getName() + " §7ist nicht online!");
            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
            return true;
        }


        player.closeInventory();
        player.openInventory(target.getInventory()).setTitle("§e" + target.getName() + ",s §7Inventar");
        library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 1f);
        return true;
    }
}
