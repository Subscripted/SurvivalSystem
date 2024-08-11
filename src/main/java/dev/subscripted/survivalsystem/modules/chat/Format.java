package dev.subscripted.survivalsystem.modules.chat;

import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Format implements Listener {

    final ClanManager clanManager;

    @SneakyThrows
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        LuckpermsService luckpermsService = new LuckpermsService();

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String prefix = clanManager.getClanPrefix(uuid);
        String name = clanManager.getClanNameByPrefix(prefix);

        String playerName = player.getName();
        String playerRank = luckpermsService.getPlayerRang(player.getUniqueId()).replace("&", "§");


        if (clanManager.isMemberOfClan(uuid, prefix)) {
            String chatFormat ="§8[" + name.replace("&", "§") + "§8]§r " + playerRank + " §8• §7" + playerName + " §8» §7" + event.getMessage();
            event.setFormat(chatFormat);
        }else {
            String chatFormat = playerRank + " §8• §7" + playerName + " §8» §7" + event.getMessage();
            event.setFormat(chatFormat);
        }

    }

}
