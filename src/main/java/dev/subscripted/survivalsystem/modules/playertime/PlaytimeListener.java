package dev.subscripted.survivalsystem.modules.playertime;

import dev.subscripted.survivalsystem.Main;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaytimeListener implements Listener {

    final PlaytimeManager playtimeManager;

    public PlaytimeListener(Main plugin, PlaytimeManager playtimeManager) {
        this.playtimeManager = playtimeManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    playtimeManager.incrementPlaytime(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
}

