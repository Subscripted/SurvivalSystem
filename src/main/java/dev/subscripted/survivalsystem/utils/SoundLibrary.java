package dev.subscripted.survivalsystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SoundLibrary {


    public void playLibrarySound(Player player, CustomSound customSound, float volume, float pitch) {
        if (player != null && customSound != null) {
            player.playSound(player.getLocation(), customSound.getSound(), volume, pitch);
        }
    }

    public void playSoundForAll(CustomSound customSound, float volume, float pitch) {
        if (customSound != null) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                playLibrarySound(player, customSound, volume, pitch);
            }
        }
    }
}
