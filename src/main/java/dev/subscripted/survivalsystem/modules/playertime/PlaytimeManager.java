package dev.subscripted.survivalsystem.modules.playertime;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeManager {

    private File playtimeFile;
    private FileConfiguration playtimeConfig;
    private Map<UUID, Integer> playtimeMap;
    private static final String PLAYTIME_FILE_NAME = "playtime.yml";
    private static final String SAVE_ERROR_MSG = "Fehler beim Speichern der Spielzeit für %s: %s";
    private static final String TIME_FORMAT = "%dh %dm";
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;


    public PlaytimeManager(File dataFolder) {
        playtimeMap = new HashMap<>();
        loadPlaytimeConfig(dataFolder);
    }

    public void loadPlaytime(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!playtimeMap.containsKey(playerUUID)) {
            int playtimeInSeconds = playtimeConfig.getInt(playerUUID.toString(), 0);
            playtimeMap.put(playerUUID, playtimeInSeconds);
        }
    }

    public void savePlaytime(Player player) {
        UUID playerUUID = player.getUniqueId();
        int playtimeInSeconds = playtimeMap.getOrDefault(playerUUID, 0);
        playtimeConfig.set(playerUUID.toString(), playtimeInSeconds);

        try {
            savePlaytimeConfig();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(String.format(SAVE_ERROR_MSG, player.getName(), e.getMessage()));
        }
    }

    public void incrementPlaytime(Player player) {
        UUID playerUUID = player.getUniqueId();
        playtimeMap.put(playerUUID, playtimeMap.getOrDefault(playerUUID, 0) + 1);
    }

    public String getFormattedPlaytime(UUID player) {
        int playtimeInSeconds = playtimeMap.getOrDefault(player, 0);
        return formatPlaytime(playtimeInSeconds);
    }

    private void loadPlaytimeConfig(File dataFolder) {
        playtimeFile = new File(dataFolder, PLAYTIME_FILE_NAME);

        if (!playtimeFile.exists()) {
            try {
                playtimeFile.getParentFile().mkdirs();
                playtimeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        playtimeConfig = YamlConfiguration.loadConfiguration(playtimeFile);
    }

    private void savePlaytimeConfig() throws IOException {
        playtimeConfig.save(playtimeFile);
    }

    private String formatPlaytime(int seconds) {
        int hours = seconds / SECONDS_IN_HOUR;
        int minutes = (seconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        return String.format(TIME_FORMAT, hours, minutes);
    }
}
