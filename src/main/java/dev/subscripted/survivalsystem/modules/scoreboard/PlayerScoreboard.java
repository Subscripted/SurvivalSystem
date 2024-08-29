package dev.subscripted.survivalsystem.modules.scoreboard;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.modules.playertime.PlaytimeManager;
import dev.subscripted.survivalsystem.modules.tablist.TablistService;
import dev.subscripted.survivalsystem.utils.CoinFormatter;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerScoreboard {

    final LuckpermsService service;
    final TablistService tablistService = Main.getInstance().getTablistService();
    final ClanManager manager;
    final PlaytimeManager playtimeManager;
    static BukkitRunnable updateTask;

    public PlayerScoreboard(LuckpermsService service, ClanManager manager, PlaytimeManager playtimeManager) {
        this.service = service;
        this.manager = manager;
        this.playtimeManager = playtimeManager;
        startUpdate();
    }

    public void setPlayerScoreboard(Player player) {
        Scoreboard playerScoreboard = player.getScoreboard();
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (playerScoreboard.equals(mainScoreboard)) {
            playerScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        String playerName = player.getName();
        String playerRank = "Loading...";
        String playerClan = "§c✕";
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        Scoreboard scoreboard = new ScoreboardBuilder("§x§B§F§A§3§B§A§lɴ§x§B§5§9§8§B§5§lᴏ§x§A§B§8§D§B§0§lᴠ§x§A§1§8§3§A§B§lɪ§x§9§7§7§8§A§6§lʙ§x§8§D§6§D§A§0§lᴇ§x§8§3§6§2§9§B§lꜱ§x§7§9§5§8§9§6§l.§x§6§F§4§D§9§1§lᴅ§x§6§5§4§2§8§C§lᴇ")
                .addLine("line15", "     §x§D§3§B§1§E§7§lᴘ§x§D§3§B§1§E§7§lʟ§x§D§3§B§1§E§7§lᴀ§x§D§3§B§1§E§7§lʏ§x§D§3§B§1§E§7§lᴇ§x§D§3§B§1§E§7§lʀ", "", 15)
                .addLine("line14", "        §8▪ §7ɴᴀᴍᴇ: " + playerName, "", 14)
                .addLine("line13", "        §8▪ §7ʀᴀɴɢ: ", "Loading...", 13)
                .addLine("line12", "        §8▪ §7ᴄᴏɪɴꜱ: ", "", 12)
                .addLine("line11", " ", "", 11)
                .addLine("line10", "     §x§D§3§B§1§E§7§lꜱ§x§D§3§B§1§E§7§lᴛ§x§D§3§B§1§E§7§lᴀ§x§D§3§B§1§E§7§lᴛ§x§D§3§B§1§E§7§lꜱ", "", 10)
                .addLine("line9", "        §8▪ §7ᴛᴏᴅᴇ: ", "", 9)
                .addLine("line8", "        §8▪ §7ᴋɪʟʟꜱ: ", "", 8)
                .addLine("line7", "        §8▪ §7ᴄʟᴀɴ: ", "", 7)
                .addLine("line6", "        §8▪ §7ꜱᴘɪᴇʟᴢᴇɪᴛ: ", "", 6)
                .addLine("line5", " ", "", 5)
                .addLine("line4", "     §x§D§3§B§1§E§7§lꜱ§x§D§3§B§1§E§7§lᴇ§x§D§3§B§1§E§7§lʀ§x§D§3§B§1§E§7§lᴠ§x§D§3§B§1§E§7§lᴇ§x§D§3§B§1§E§7§lʀ", "", 4)
                .addLine("line3", "        §8▪ §7ᴏɴʟɪɴᴇ", "§x§D§3§B§1§E§7§l " + onlinePlayers, 3)
                .addLine("line2", "§8✦§m" + "  ".repeat(30) + "§r§8✦", "", 2)
                .addLine("line1", " ".repeat(24) + " ", "", 1)
                .addLine("line0", " ".repeat(24) + " ", "", 0)
                .build();

        player.setScoreboard(scoreboard);
    }

    @SneakyThrows
    public void updatePlayerScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Coins coins = Main.getInstance().getCoins();
        String playerCoins = CoinFormatter.formatCoins(coins.getCoins(player.getUniqueId()));
        String group = service.getPlayerRang(player.getUniqueId()).replace("&", "§");
        String playtime = playtimeManager.getFormattedPlaytime(player.getUniqueId());
        String playerClan = manager.isClanMember(player.getUniqueId())
                ? "§8[" + ChatColor.translateAlternateColorCodes('&', manager.getClanName(player.getUniqueId())) + "§8]"
                : "§8[§c✕§8]";

        int playerDeath = player.getStatistic(Statistic.DEATHS);
        int kills = player.getStatistic(Statistic.PLAYER_KILLS);

        updateTeam(scoreboard, "line14", "        §8▪ §7ɴᴀᴍᴇ: §x§D§3§B§1§E§7§l" + player.getName(), " ");
        updateTeam(scoreboard, "line13", "        §8▪ §7ʀᴀɴɢ: ", group);
        updateTeam(scoreboard, "line12", "        §8▪ §7ᴄᴏɪɴꜱ: ", "§x§D§3§B§1§E§7§l" + playerCoins + "€");
        updateTeam(scoreboard, "line9", "        §8▪ §7ᴛᴏᴅᴇ: ", "§x§D§3§B§1§E§7§l" + playerDeath);
        updateTeam(scoreboard, "line8", "        §8▪ §7ᴋɪʟʟꜱ: ", "§x§D§3§B§1§E§7§l" + kills);
        updateTeam(scoreboard, "line7", "        §8▪ §7ᴄʟᴀɴ: ", playerClan);
        updateTeam(scoreboard, "line6", "        §8▪ §7ꜱᴘɪᴇʟᴢᴇɪᴛ: ", "§x§D§3§B§1§E§7§l" + playtime);
        updateTeam(scoreboard, "line1", " ".repeat(24), "§x§D§3§B§1§E§7§l" + tablistService.getFormattedTimeWithSeconds());
    }

    private void updateTeam(Scoreboard scoreboard, String teamName, String prefix, String suffix) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.addEntry(prefix.trim());
        }
        team.setPrefix(prefix);
        team.setSuffix(suffix);
    }

    @SneakyThrows
    private void updatePlayerNameTag(Player player) {
        String rank = service.getPlayerRang(player.getUniqueId()).replace("&", "§");
        String clanName = manager.isClanMember(player.getUniqueId())
                ? ChatColor.translateAlternateColorCodes('&', "§7[" + manager.getClanName(player.getUniqueId()) + "§7]")
                : "";
        String nameTag = rank.replace("&", "§") + " §8▪ ";

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(player.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName());
            team.addEntry(player.getName());
        }

        team.setPrefix(nameTag);
        team.setSuffix(" " + clanName);
        team.setColor(ChatColor.GRAY);
        player.setScoreboard(scoreboard);
    }

    private void startUpdate() {
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
        }
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayerScoreboard(player);
                    updatePlayerNameTag(player);
                }
            }
        };
        updateTask.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
