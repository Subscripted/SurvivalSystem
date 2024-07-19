package dev.subscripted.survivalsystem.modules.chat;

import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Format implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        LuckpermsService luckpermsService = new LuckpermsService();
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerRank = luckpermsService.getPlayerRang(player.getUniqueId()).replace("&", "§");

        String chatFormat = playerRank + " §8• §7" + playerName + " §8» §7" + event.getMessage();
        event.setFormat(chatFormat);
    }

}
