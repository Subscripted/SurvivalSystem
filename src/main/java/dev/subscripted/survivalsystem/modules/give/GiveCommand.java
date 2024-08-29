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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Überprüfe, ob der Absender ein Spieler ist
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 3) {
            player.sendMessage("Verwendung: /give <Spieler> <Item> <Menge>");
            return true;
        }

        Player targetPlayer = player;
        int itemIndex = 0;
        int amount = 1;

        // Wenn ein Spielername angegeben wurde
        if (args.length >= 2) {
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                player.sendMessage("Spieler nicht gefunden.");
                return true;
            }
            itemIndex = 1;
        }

        // Überprüfe, ob das Item existiert
        Material material = Material.getMaterial(args[itemIndex].toUpperCase());
        if (material == null) {
            player.sendMessage("Ungültiges Item.");
            return true;
        }

        // Menge setzen, falls angegeben
        if (args.length == itemIndex + 2) {
            try {
                amount = Integer.parseInt(args[itemIndex + 1]);
            } catch (NumberFormatException e) {
                player.sendMessage("Ungültige Menge.");
                return true;
            }
        }

        // Gebe dem Spieler das Item
        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);
        player.sendMessage("Du hast " + amount + " " + material.toString() + " an " + targetPlayer.getName() + " gegeben.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Spieler vorschlagen
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 2 || args.length == 1) {
            // Items vorschlagen
            for (Material material : Material.values()) {
                completions.add(material.name().toLowerCase());
            }
        } else if (args.length == 3) {
            // Mengen vorschlagen
            completions.add("1");
            completions.add("64");
        }

        return completions;
    }
}

