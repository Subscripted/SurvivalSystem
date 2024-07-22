package dev.subscripted.survivalsystem.modules.scoreboard;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
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

    public PlayerScoreboard(LuckpermsService service) {
        this.service = service;
    }

    public void setPlayerScoreboard(Player player) {
        String playername = player.getName();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        Scoreboard scoreboard = new ScoreboardBuilder("§x§B§F§A§3§B§A§lɴ§x§B§5§9§8§B§5§lᴏ§x§A§B§8§D§B§0§lᴠ§x§A§1§8§3§A§B§lɪ§x§9§7§7§8§A§6§lʙ§x§8§D§6§D§A§0§lᴇ§x§8§3§6§2§9§B§lꜱ§x§7§9§5§8§9§6§l.§x§6§F§4§D§9§1§lᴅ§x§6§5§4§2§8§C§lᴇ")
                .addLine("line15", "§8✦§m" + "  ".repeat(30) + "§r§8✦", "", 14)
                .addLine("line14", "     §x§E§2§F§8§F§A§lᴘ§x§E§2§F§8§F§A§lʟ§x§E§2§F§8§F§A§lᴀ§x§E§2§F§8§F§A§lʏ§x§E§2§F§8§F§A§lᴇ§x§E§2§F§8§F§A§lʀ", "", 13)
                .addLine("line13", "        §8▪ §7ɴᴀᴍᴇ: ", "§x§C§0§E§8§E§8§l" + playername, 12)
                .addLine("line12", "        §8▪ §7ʀᴀɴɢ: ", "Loading...", 11)
                .addLine("line11", "        §8▪ §7ᴄᴏɪɴꜱ: ", "", 10)
                .addLine("line10", " ", "", 9)
                .addLine("line9", "     §x§E§2§F§8§F§A§lꜱ§x§E§2§F§8§F§A§lᴛ§x§E§2§F§8§F§A§lᴀ§x§E§2§F§8§F§A§lᴛ§x§E§2§F§8§F§A§lꜱ", "", 8)
                .addLine("line8", "        §8▪ §7ᴛᴏᴅᴇ: ", "", 7)
                .addLine("line7", "        §8▪ §7ᴋɪʟʟꜱ: ", "", 6)
                .addLine("line6", " ", "", 5)
                .addLine("line5", "     §x§E§2§F§8§F§A§lꜱ§x§E§2§F§8§F§A§lᴇ§x§E§2§F§8§F§A§lʀ§x§E§2§F§8§F§A§lᴠ§x§E§2§F§8§F§A§lᴇ§x§E§2§F§8§F§A§lʀ", "", 4)
                .addLine("line4", "        §8▪ §7ᴏɴʟɪɴᴇ", "§x§C§0§E§8§E§8§l " + onlinePlayers, 3)
                .addLine("line2", "§8✦§m" + "  ".repeat(30) + "§r§8✦", "", 2)
                .addLine("line1", "             §8ɴᴏᴠɪʙᴇꜱ.ᴅᴇ ꜱᴜʀᴠɪᴠᴀʟ", "", 1)
                .addLine("line0", " ", "", 0)
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

        Team team = scoreboard.getTeam("line12");
        Team cointeam = scoreboard.getTeam("line11");
        Team deathteam = scoreboard.getTeam("line8");
        Team killteam = scoreboard.getTeam("line7");

        if (team == null) {
            team = scoreboard.registerNewTeam("line12");
            team.addEntry("       §8» §7ʀᴀɴɢ: ");
        }
        team.setSuffix("§x§C§0§E§8§E§8§l" + group);

        if (cointeam == null) {
            cointeam = scoreboard.registerNewTeam("line11");
            cointeam.addEntry("       §8» §7ᴄᴏɪɴꜱ: ");
        }
        cointeam.setSuffix("§x§C§0§E§8§E§8§l" + playercoins + "€");

        if (deathteam == null) {
            deathteam = scoreboard.registerNewTeam("line8");
            deathteam.addEntry("       §8» §7ᴛᴏᴅᴇ: ");
        }
        deathteam.setSuffix("§x§C§0§E§8§E§8§l" + playerdeath);

        if (killteam == null) {
            killteam = scoreboard.registerNewTeam("line7");
            killteam.addEntry("       §8» §7ᴋɪʟʟꜱ: ");
        }
        killteam.setSuffix("§x§C§0§E§8§E§8§l" + kills);
    }

}