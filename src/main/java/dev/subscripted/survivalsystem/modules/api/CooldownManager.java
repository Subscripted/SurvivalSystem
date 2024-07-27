package dev.subscripted.survivalsystem.modules.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    private final JavaPlugin plugin;
    private final HashMap<UUID, Cooldown> cooldowns;

    public CooldownManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }

    public void startCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            return;
        }

        Cooldown cooldown = new Cooldown(player);
        cooldowns.put(playerId, cooldown);

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.remove(playerId);
                player.sendMessage("Your PvP cooldown period has ended!");
            }
        }.runTaskLater(plugin, 200L); // 200L = 10 seconds (20 ticks per second)
    }

    public boolean isInCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId());
    }

    public void removeCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

    public void handlePlayerDeath(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            cooldowns.get(playerId).setDied(true);
        }
    }

    public void handlePlayerQuit(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId) && !cooldowns.get(playerId).hasDied()) {
            player.setHealth(0.0); // Kill the player
        }
        cooldowns.remove(playerId);
    }

    public class Cooldown {
        private final Player player;
        private boolean inMap;
        private boolean died;

        public Cooldown(Player player) {
            this.player = player;
            this.inMap = true;
            this.died = false;
        }

        public boolean isInMap() {
            return inMap;
        }

        public void setInMap(boolean inMap) {
            this.inMap = inMap;
        }

        public boolean hasDied() {
            return died;
        }

        public void setDied(boolean died) {
            this.died = died;
        }

        public Player getPlayer() {
            return player;
        }
    }
}
