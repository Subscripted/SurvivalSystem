package dev.subscripted.survivalsystem.modules.scoreboard;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.modules.tablist.TablistService;
import dev.subscripted.survivalsystem.utils.CoinFormatter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerScoreboard {

    final LuckpermsService service;
    static BukkitRunnable updateTask;
    final TablistService service1 = Main.getInstance().getTablistService();

    public PlayerScoreboard(LuckpermsService service) {
        this.service = service;
    }

    public void setPlayerScoreboard(Player player) {
        String playername = player.getName();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        Scoreboard scoreboard = new ScoreboardBuilder("§x§B§F§A§3§B§A§lɴ§x§B§5§9§8§B§5§lᴏ§x§A§B§8§D§B§0§lᴠ§x§A§1§8§3§A§B§lɪ§x§9§7§7§8§A§6§lʙ§x§8§D§6§D§A§0§lᴇ§x§8§3§6§2§9§B§lꜱ§x§7§9§5§8§9§6§l.§x§6§F§4§D§9§1§lᴅ§x§6§5§4§2§8§C§lᴇ")
                .addLine("line14", "§8✦§m" + "  ".repeat(30) + "§r§8✦", "", 14)
                .addLine("line13", "     §x§D§3§B§1§E§7§lᴘ§x§D§3§B§1§E§7§lʟ§x§D§3§B§1§E§7§lᴀ§x§D§3§B§1§E§7§lʏ§x§D§3§B§1§E§7§lᴇ§x§D§3§B§1§E§7§lʀ", "", 13)
                .addLine("line12", "        §8▪ §7ɴᴀᴍᴇ: ", "§x§D§3§B§1§E§7§l" + playername, 12)
                .addLine("line11", "        §8▪ §7ʀᴀɴɢ: ", "Loading...", 11)
                .addLine("line10", "        §8▪ §7ᴄᴏɪɴꜱ: ", "", 10)
                .addLine("line9", " ", "", 9)
                .addLine("line8", "     §x§D§3§B§1§E§7§lꜱ§x§D§3§B§1§E§7§lᴛ§x§D§3§B§1§E§7§lᴀ§x§D§3§B§1§E§7§lᴛ§x§D§3§B§1§E§7§lꜱ", "", 8)
                .addLine("line7", "        §8▪ §7ᴛᴏᴅᴇ: ", "", 7)
                .addLine("line6", "        §8▪ §7ᴋɪʟʟꜱ: ", "", 6)
                .addLine("line5", " ", "", 5)
                .addLine("line4", "     §x§D§3§B§1§E§7§lꜱ§x§D§3§B§1§E§7§lᴇ§x§D§3§B§1§E§7§lʀ§x§D§3§B§1§E§7§lᴠ§x§D§3§B§1§E§7§lᴇ§x§D§3§B§1§E§7§lʀ", "", 4)
                .addLine("line3", "        §8▪ §7ᴏɴʟɪɴᴇ", "§x§D§3§B§1§E§7§l " + onlinePlayers, 3)
                .addLine("line2", "§8✦§m" + "  ".repeat(30) + "§r§8✦", "", 2)
                .addLine("line1", "                      §8ɴᴏᴠɪʙᴇꜱ.ᴅᴇ ꜱᴜʀᴠɪᴠᴀʟ", "", 1)
                .addLine("line0", " ".repeat(25) + " ", "", 0)
                .build();

        player.setScoreboard(scoreboard);
        updatePlayerScoreboard(player);
        if (updateTask != null) {
            updateTask.cancel();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePlayerScoreboard(player);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public static void stopUpdate() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
    }

    public void updatePlayerScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Coins coins = Main.getInstance().getCoins();
        String playercoins = CoinFormatter.formatCoins(coins.getCoins(player.getUniqueId()));
        String group = service.getPlayerRang(player.getUniqueId()).replace("&", "§");

        int playerdeath = player.getStatistic(Statistic.DEATHS);
        int kills = player.getStatistic(Statistic.PLAYER_KILLS);

        Team team = scoreboard.getTeam("line11");
        Team cointeam = scoreboard.getTeam("line10");
        Team deathteam = scoreboard.getTeam("line7");
        Team killteam = scoreboard.getTeam("line6");
        Team time = scoreboard.getTeam("line0");

        if (team == null) {
            team = scoreboard.registerNewTeam("line11");
            team.addEntry("       §8» §7ʀᴀɴɢ: ");
        }
        team.setSuffix("§x§D§3§B§1§E§7§l" + group);

        if (cointeam == null) {
            cointeam = scoreboard.registerNewTeam("line10");
            cointeam.addEntry("       §8» §7ᴄᴏɪɴꜱ: ");
        }
        cointeam.setSuffix("§x§D§3§B§1§E§7§l" + playercoins + "€");

        if (deathteam == null) {
            deathteam = scoreboard.registerNewTeam("line7");
            deathteam.addEntry("       §8» §7ᴛᴏᴅᴇ: ");
        }
        deathteam.setSuffix("§x§D§3§B§1§E§7§l" + playerdeath);

        if (killteam == null) {
            killteam = scoreboard.registerNewTeam("line6");
            killteam.addEntry("       §8» §7ᴋɪʟʟꜱ: ");
        }
        killteam.setSuffix("§x§D§3§B§1§E§7§l" + kills);

        if (time == null) {
            time = scoreboard.registerNewTeam("line0");
            time.addEntry("       §8» §7Zeit: ");
        }
        time.setSuffix("§x§D§3§B§1§E§7§l" + service1.getFormattedTimeWithSeconds());

    }
}