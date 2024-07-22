package dev.subscripted.survivalsystem.modules.api;

import dev.subscripted.survivalsystem.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileAPI {

    private final File f;
    private final YamlConfiguration c;

    public FileAPI(String fileName) {
        f = new File(Main.getInstance().getDataFolder(), fileName);
        c = YamlConfiguration.loadConfiguration(f);
    }

    public FileAPI setValue(String path, Object value) {
        c.set(path, value);
        return this;
    }

    public boolean valueExist(String path) {
        return (getObject(path) != null);
    }

    public Object getObject(String path) {
        return c.get(path);
    }

    public int getInt(String path) {
        return c.getInt(path);
    }


    public long getLong(String path) {
        return c.getLong(path);
    }

    public String getString(String path) {
        return c.getString(path);
    }

    public boolean getBoolean(String path) {
        return c.getBoolean(path);
    }

    public List<String> getStringList(String path) {
        return c.getStringList(path);
    }

    public Set<String> getKeys(boolean deep) {
        return c.getKeys(deep);
    }

    public ConfigurationSection getConfigSection(String section) {
        return c.getConfigurationSection(section);
    }

    public ConfigurationSection getSection(String path) {
        return c.getConfigurationSection(path);
    }


    public FileAPI save() {
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

}