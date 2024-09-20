package dev.subscripted.survivalsystem.modules.vanish;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class VanishService {

    final Set<UUID> teamlerInVanish = new HashSet<>();

    public boolean isVanished(Player player) {
        return teamlerInVanish.contains(player.getUniqueId());
    }

    public void setVanished(Player player) {
        teamlerInVanish.add(player.getUniqueId());
        player.setInvisible(true);
        updateVisibility(player, true);
    }

    public void removeFromVanish(Player player) {
        teamlerInVanish.remove(player.getUniqueId());
        player.setInvisible(false);
        updateVisibility(player, false);
    }

    private void updateVisibility(Player player, boolean vanished) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (vanished) {
                onlinePlayer.hidePlayer(player);
            } else {
                onlinePlayer.showPlayer(player);
            }
        }
    }
}
