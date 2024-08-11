package dev.subscripted.survivalsystem.modules.connect;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.modules.playertime.PlaytimeManager;
import dev.subscripted.survivalsystem.modules.scoreboard.PlayerScoreboard;
import dev.subscripted.survivalsystem.modules.tablist.TablistService;
import dev.subscripted.survivalsystem.modules.vanish.VanishService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class JoinQuit implements Listener {

    final VanishService service;
    final TablistService tablistService;
    final LuckpermsService luckpermsService;
    final ClanManager clanManager;
    final PlaytimeManager manager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        Main.getInstance().getCoins().setPlayerData(player.getUniqueId(), 0, 0);
        PlayerScoreboard scoreboard = new PlayerScoreboard(Main.getInstance().getLpservice(), clanManager, manager);
        scoreboard.setPlayerScoreboard(player);
        tablistService.setTabList(player);
        tablistService.updateAllTabLists();
        manager.loadPlaytime(player);

        player.setMaxHealth(24.0);
        player.setHealth(24.0);

        if (!luckpermsService.hasDefaultGroup(player)) {
            luckpermsService.setDefaultGroup(player);
        }
        event.setJoinMessage("§8[§a+§8] §7" + playerName);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        manager.savePlaytime(player);
        event.setQuitMessage("§8[§c-§8] §7" + playerName);
    }
}
