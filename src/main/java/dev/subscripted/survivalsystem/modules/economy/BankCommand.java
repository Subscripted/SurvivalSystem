package dev.subscripted.survivalsystem.modules.economy;

import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.modules.gui.BankUI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BankCommand implements CommandExecutor {

    final Coins coins;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a Player to perform this command!");
            return true;
        }
        Player player = (Player) sender;
        UUID playerUID = player.getUniqueId();
        int playerCoins = coins.getCoins(playerUID);
        BankUI.bankingGUI(player);

        return false;
    }
}
