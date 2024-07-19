package dev.subscripted.survivalsystem.modules.connect;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.scoreboard.PlayerScoreboard;
import dev.subscripted.survivalsystem.modules.vanish.VanishService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinQuit implements Listener {

    final VanishService service;

    public JoinQuit(VanishService service) {
        this.service = service;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Main.getInstance().getCoins().setPlayerData(player.getUniqueId(), 0, 0);
        String playerName = player.getName();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!service.isVanished(onlinePlayer)) {
                onlinePlayer.sendMessage("§8[§a+§8] §7" + playerName);
            }
        }
        event.setJoinMessage(null);
        PlayerScoreboard scoreboard = new PlayerScoreboard(Main.getInstance().getLpservice());
        scoreboard.setPlayerScoreboard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!service.isVanished(onlinePlayer)) {
                onlinePlayer.sendMessage("§8[§c-§8] §7" + playerName);
            }
        }
        event.setQuitMessage(null);
        PlayerScoreboard.stopUpdate();
    }
}
