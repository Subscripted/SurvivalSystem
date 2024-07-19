package dev.subscripted.survivalsystem.modules.chat;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WrongSyntax implements Listener {

    String prefix = Main.getInstance().getPrefix();
    SoundLibrary soundLibrary;

    public WrongSyntax(SoundLibrary soundLibrary) {
        this.soundLibrary = soundLibrary;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        String[] args = msg.split(" ");
        Player player = event.getPlayer();

        if (Bukkit.getServer().getHelpMap().getHelpTopic(args[0]) == null) {
            event.setCancelled(true);
            player.sendMessage(prefix + "§cDieser Command existiert nicht!");
            soundLibrary.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
        }
    }
}
