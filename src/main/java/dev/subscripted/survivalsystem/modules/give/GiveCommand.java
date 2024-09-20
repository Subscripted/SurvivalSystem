package dev.subscripted.survivalsystem.modules.give;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand implements CommandExecutor, TabCompleter {

    private static final String PLAYER_ONLY_MESSAGE = "Dieser Befehl kann nur von einem Spieler ausgeführt werden.";
    private static final String PLAYER_NOT_FOUND_MESSAGE = "Spieler nicht gefunden.";
    private static final String INVALID_ITEM_MESSAGE = "Ungültiges Item.";
    private static final String INVALID_AMOUNT_MESSAGE = "Ungültige Menge.";
    private static final String COMMAND_USAGE_MESSAGE = "Verwendung: /give <Spieler> <Item> <Menge>";
    private static final String GAVE_MESSAGE = "Du hast %d %s an %s gegeben.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY_MESSAGE);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 3) {
            player.sendMessage(COMMAND_USAGE_MESSAGE);
            return true;
        }

        Player targetPlayer = player;
        int itemIndex = 0;
        int amount = 1;

        if (args.length >= 2) {
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                player.sendMessage(PLAYER_NOT_FOUND_MESSAGE);
                return true;
            }
            itemIndex = 1;
        }

        Material material = Material.getMaterial(args[itemIndex].toUpperCase());
        if (material == null) {
            player.sendMessage(INVALID_ITEM_MESSAGE);
            return true;
        }

        if (args.length == itemIndex + 2) {
            try {
                amount = Integer.parseInt(args[itemIndex + 1]);
            } catch (NumberFormatException e) {
                player.sendMessage(INVALID_AMOUNT_MESSAGE);
                return true;
            }
        }

        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);
        player.sendMessage(String.format(GAVE_MESSAGE, amount, material.toString(), targetPlayer.getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 2 || args.length == 1) {
            for (Material material : Material.values()) {
                completions.add(material.name().toLowerCase());
            }
        } else if (args.length == 3) {
            completions.add("1");
            completions.add("64");
        }

        return completions;
    }
}

