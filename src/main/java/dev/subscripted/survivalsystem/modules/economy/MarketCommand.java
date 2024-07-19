package dev.subscripted.survivalsystem.modules.economy;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.InventoryAdvancer;
import dev.subscripted.survivalsystem.utils.ItemBuilder;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarketCommand implements CommandExecutor {

    final SoundLibrary library;

    public MarketCommand(SoundLibrary library) {
        this.library = library;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        openMainMenu(player);
        return true;
    }

    public void openMainMenu(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();
        int size = config.getInt("main-menu.size", 27);
        Inventory mainMenu = Bukkit.createInventory(null, size, ChatColor.GREEN + config.getString("main-menu.title", "Main Menu"));
        ItemBuilder nulled = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
        InventoryAdvancer.fillNulledInventory(nulled, mainMenu);

        for (String key : config.getConfigurationSection("main-menu").getKeys(false)) {
            int slot = config.getInt("main-menu." + key + ".slot");
            String materialName = config.getString("main-menu." + key + ".item");

            if (materialName == null) {
                Bukkit.getLogger().warning("Material name is null for item key: " + key);
                continue;
            }

            Material material;
            try {
                material = Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Invalid material name '" + materialName + "' for item key: " + key);
                continue;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("main-menu." + key + ".name")));
                item.setItemMeta(meta);
            }
            mainMenu.setItem(slot, item);
        }
        library.playLibrarySound(player, CustomSound.GUI_OPEN, 1f, 2f);
        player.openInventory(mainMenu);
    }
}
