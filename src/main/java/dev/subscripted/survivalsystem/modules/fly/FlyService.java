package dev.subscripted.survivalsystem.modules.fly;


import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlyService {
    final Set<UUID> flyingplayer = new HashSet<>();

    public boolean isInFlyMode(Player player) {
        return flyingplayer.contains(player.getUniqueId());
    }

    public void setFlying(Player player) {
        flyingplayer.add(player.getUniqueId());
        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §eFliegen §aAktiviert!");
    }

    public void unsetFlying(Player player) {
        flyingplayer.remove(player.getUniqueId());
        player.setAllowFlight(false);
        player.setFlying(false);
        player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §eFliegen §cDeaktiviert!");
    }

}
