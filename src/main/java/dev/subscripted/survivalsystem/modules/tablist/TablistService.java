package dev.subscripted.survivalsystem.modules.tablist;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TablistService {

    final LuckpermsService luckpermsService;
    final Main plugin;

    final DecimalFormat TPS_FORMAT = new DecimalFormat("0.00");
    final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
    final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm 'Uhr'");
    final SimpleDateFormat TIME_FORMAT_WITH_SECONDS = new SimpleDateFormat("HH:mm:ss 'Uhr'");
    final ClanManager clanManager;
    private static final String TIME_ZONE = "UTC";

    public TablistService(Main plugin, ClanManager clanManager) {
        this.plugin = plugin;
        this.clanManager = clanManager;
        this.luckpermsService = new LuckpermsService();
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        TIME_FORMAT_WITH_SECONDS.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        startTabListUpdater();
    }


    public void setTabList(Player player) {
        String playername = player.getName();
        int onlineplayers = Bukkit.getOnlinePlayers().size();


        String isInGamemode = isInAGamemode(player) ? "§c✕" : formatHealth(player);

        List<String> header = new ArrayList<>();
        header.add("§8✦§m" + "  ".repeat(40) + "§r§8✦");
        header.add(" ");
        header.add(" §x§B§F§A§3§B§A§lɴ§x§B§5§9§8§B§5§lᴏ§x§A§B§8§D§B§0§lᴠ§x§A§1§8§3§A§B§lɪ§x§9§7§7§8§A§6§lʙ§x§8§D§6§D§A§0§lᴇ§x§8§3§6§2§9§B§lꜱ§x§7§9§5§8§9§6§l.§x§6§F§4§D§9§1§lᴅ§x§6§5§4§2§8§C§lᴇ ");
        header.add(" ");
        header.add("§f§lWillkommen §e" + playername);
        header.add("§7Datum: §e" + getFormattedDate() + " §8│" + " §7Uhrzeit: §e" + getFormattedTime());
        header.add(" ");
        header.add("§8✦§m" + "  ".repeat(40) + "§r§8✦");

        List<String> footer = new ArrayList<>();
        footer.add("§8✦§m" + "  ".repeat(30) + "§r§8✦");
        footer.add(" ");
        footer.add("§x§8§D§6§D§A§0§lR§x§8§D§6§D§A§0§la§x§8§D§6§D§A§0§ln§x§8§D§6§D§A§0§lg: " + luckpermsService.getPlayerRang(player.getUniqueId()).replace("&", "§") + " §8│ §x§8§D§6§D§A§0§lP§x§8§D§6§D§A§0§li§x§8§D§6§D§A§0§ln§x§8§D§6§D§A§0§lg: §a" + player.getPing() + " §8│ §x§8§D§6§D§A§0§lL§x§8§D§6§D§A§0§le§x§8§D§6§D§A§0§lb§x§8§D§6§D§A§0§le§x§8§D§6§D§A§0§ln: §a" + isInGamemode);
        footer.add("§x§8§D§6§D§A§0§lD§x§8§D§6§D§A§0§li§x§8§D§6§D§A§0§ls§x§8§D§6§D§A§0§lc§x§8§D§6§D§A§0§lo§x§8§D§6§D§A§0§lr§x§8§D§6§D§A§0§ld: §7/discord §8│ §x§8§D§6§D§A§0§lW§x§8§D§6§D§A§0§le§x§8§D§6§D§A§0§lb§x§8§D§6§D§A§0§ls§x§8§D§6§D§A§0§le§x§8§D§6§D§A§0§li§x§8§D§6§D§A§0§lt§x§8§D§6§D§A§0§le: §7Novibes.de");
        footer.add("§x§8§D§6§D§A§0§lT§x§8§D§6§D§A§0§lP§x§8§D§6§D§A§0§lS: " + getFormattedTPS() + " §8│ §x§8§D§6§D§A§0§lO§x§8§D§6§D§A§0§ln§x§8§D§6§D§A§0§ll§x§8§D§6§D§A§0§li§x§8§D§6§D§A§0§ln§x§8§D§6§D§A§0§le: §a" + onlineplayers);

        String headerString = String.join("\n", header);
        String footerString = String.join("\n", footer);

        player.setPlayerListHeader(headerString);
        player.setPlayerListFooter(footerString);
    }

    @SneakyThrows
    private void updatePlayerListNames() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            String rank = luckpermsService.getPlayerRang(p.getUniqueId()).replace("&", "§");
            int ping = p.getPing();
            if (clanManager.isClanMember(p.getUniqueId())) {
                p.setPlayerListName("§8[" + clanManager.getClanName(p.getUniqueId()).replace("&", "§") + "§8] " + rank + " §8•§7 " + p.getName() + " §a" + ping);
            } else {
                p.setPlayerListName(rank + " §8•§7 " + p.getName() + " §a" + ping);
            }
        }
    }

    private void startTabListUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            updateAllTabLists();
            updatePlayerListNames();
        }, 0L, 20L);
    }

    public void updateAllTabLists() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setTabList(player);
        }
    }

    public String formatHealth(Player player){
        DecimalFormat format = new DecimalFormat("##.#");
        return format.format(player.getHealth());
    }

    @SneakyThrows
    private double getTPS() {
        try {
            Method getTPSMethod = Bukkit.getServer().getClass().getMethod("getTPS");
            double[] tps = (double[]) getTPSMethod.invoke(Bukkit.getServer());
            return Math.min(tps[0], 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1.0;
    }

    private String formatTPS(double tps) {
        return TPS_FORMAT.format(tps);
    }

    private String getFormattedTPS() {
        double tps = getTPS();
        String formattedTPS = formatTPS(tps);
        String color;
        if (tps >= 16) {
            color = "§a";
        } else if (tps >= 11) {
            color = "§e";
        } else if (tps >= 5) {
            color = "§c";
        } else {
            color = "§4";
        }
        return color + formattedTPS;
    }

    private String getFormattedDate() {
        return DATE_FORMAT.format(new Date());
    }

    public String getFormattedTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return TIME_FORMAT.format(calendar.getTime());
    }



    public String getFormattedTimeWithSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return TIME_FORMAT_WITH_SECONDS.format(calendar.getTime());
    }

    public boolean isInAGamemode(Player pl){
        return pl.getGameMode().equals(GameMode.CREATIVE) || pl.getGameMode().equals(GameMode.SPECTATOR);
    }
}
