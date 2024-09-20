package dev.subscripted.survivalsystem.modules.api;

import dev.subscripted.survivalsystem.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileAPI {

    private final File file;
    private final YamlConfiguration configuration;

    public FileAPI(String fileName) {
        this.file = new File(Main.getInstance().getDataFolder(), fileName);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileAPI setValue(String path, Object value) {
        this.configuration.set(path, value);
        return this;
    }

    public boolean doesValueExist(String path) {
        return (this.getObject(path) != null);
    }

    public Object getObject(String path) {
        return this.configuration.get(path);
    }

    public int getInt(@NotNull String path) {
        return this.configuration.getInt(path);
    }

    public long getLong(@NotNull String path) {
        return this.configuration.getLong(path);
    }

    public String getString(@NotNull String path) {
        return this.configuration.getString(path);
    }

    public boolean getBoolean(@NotNull String path) {
        return this.configuration.getBoolean(path);
    }

    public List<String> getStringList(@NotNull String path) {
        return this.configuration.getStringList(path);
    }

    public Set<String> getKeys(boolean deep) {
        return this.configuration.getKeys(deep);
    }

    public ConfigurationSection getConfigurationSection(@NotNull String section) {
        return this.configuration.getConfigurationSection(section);
    }

    public ConfigurationSection getSection(@NotNull String path) {
        return this.configuration.getConfigurationSection(path);
    }

    public FileAPI save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}