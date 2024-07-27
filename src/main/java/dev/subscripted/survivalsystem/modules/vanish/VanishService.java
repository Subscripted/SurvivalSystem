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
    }

    public void removeFromVanish(Player player) {
        teamlerInVanish.remove(player.getUniqueId());
        player.setInvisible(false);
    }

    private void updateVisibility(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (isVanished(player)) {
                if (!isVanished(onlinePlayer)) {
                    onlinePlayer.hidePlayer(player);
                } else {
                    onlinePlayer.showPlayer(player);
                }
            } else {
                onlinePlayer.showPlayer(player);
            }

            if (isVanished(onlinePlayer)) {
                player.showPlayer(onlinePlayer);
            } else {
                player.hidePlayer(onlinePlayer);
            }
        }
    }
}
