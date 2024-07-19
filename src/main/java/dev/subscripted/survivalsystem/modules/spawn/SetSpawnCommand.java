package dev.subscripted.survivalsystem.modules.spawn;

import dev.subscripted.survivalsystem.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final Main main;

    public SetSpawnCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            Location location = player.getLocation();
            
          main.getConfig().set("spawn.x", location.getX());
            main.getConfig().set("spawn.y", location.getY());
            main.getConfig().set("spawn.z", location.getZ());
            
            main.getConfig().set("spawn", location);
            
            main.saveConfig();
            
            player.sendMessage("Spawn location set!");

        } else {
            System.out.println("Bruh get yo ass on the server.");
        }

        return true;
    }
}