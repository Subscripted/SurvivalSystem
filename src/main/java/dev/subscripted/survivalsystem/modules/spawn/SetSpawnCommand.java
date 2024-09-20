package dev.subscripted.survivalsystem.modules.spawn;

import dev.subscripted.survivalsystem.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private static final String SPAWN_SET_MSG = "Spawn location set!";
    private static final String NOT_A_PLAYER_MSG = "This command can only be executed by a player";

    private final Main main;

    public SetSpawnCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println(NOT_A_PLAYER_MSG);
            return true;
        }
        Player player = (Player) sender;
        Location location = player.getLocation();

        main.getConfig().set("spawn", location);

        main.saveConfig();
        player.sendMessage(SPAWN_SET_MSG);

        return true;
    }
}