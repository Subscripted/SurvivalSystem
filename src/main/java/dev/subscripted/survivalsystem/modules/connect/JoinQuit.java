package dev.subscripted.survivalsystem.modules.connect;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.scoreboard.PlayerScoreboard;
import dev.subscripted.survivalsystem.modules.tablist.TablistService;
import dev.subscripted.survivalsystem.modules.vanish.VanishService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinQuit implements Listener {

    final VanishService service;
    final TablistService tablistService;
    final LuckpermsService luckpermsService;

    public JoinQuit(VanishService service, TablistService tablistService, LuckpermsService luckpermsService) {
        this.service = service;
        this.tablistService = tablistService;
        this.luckpermsService = luckpermsService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        Main.getInstance().getCoins().setPlayerData(player.getUniqueId(), 0, 0);
        setPlayerName(player);

        PlayerScoreboard scoreboard = new PlayerScoreboard(Main.getInstance().getLpservice());
        scoreboard.setPlayerScoreboard(player);
        tablistService.setTabList(player);
        tablistService.updateAllTabLists();

        if (!luckpermsService.hasDefaultGroup(player)) {
            luckpermsService.setDefaultGroup(player);
            return;
        }
        if (service.isVanished(player)) {
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage("§8[§a+§8] §7" + playerName);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        removePlayerName(player);

        if (service.isVanished(player)) {
            event.setQuitMessage(null);
        } else {
            event.setQuitMessage("§8[§c-§8] §7" + playerName);
        }
        PlayerScoreboard.stopUpdate();
    }

    public void setPlayerName(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(player.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName());
        }
        team.setPrefix(luckpermsService.getPlayerRang(player.getUniqueId()));
        team.addEntry(player.getName());
    }

    public void removePlayerName(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
            team.unregister();
        }
    }
}
