package dev.subscripted.survivalsystem.modules.economy;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.utils.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarketListener implements Listener {

    final SoundLibrary library;

    public MarketListener(SoundLibrary library) {
        this.library = library;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        String title = event.getView().getTitle();
        ItemStack goback = new ItemStack(Material.RED_BANNER);
        ItemMeta meta = goback.getItemMeta();
        meta.setDisplayName("§cZurück!");
        goback.setItemMeta(meta);

        if (title != null) {
            if (!event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
                if (title.equals(getConfig().getString("main-menu.title")) && !event.getClickedInventory().equals(InventoryType.CHEST)) {
                    event.setCancelled(true);
                    ItemStack clickedItem = event.getCurrentItem();
                    if (clickedItem != null) {
                        handleMainMenuClick(event.getSlot(), (Player) event.getWhoClicked());
                    }
                } else if (title.endsWith(" Markt")) {
                    event.setCancelled(true);
                    ItemStack clickedItem = event.getCurrentItem();
                    if (clickedItem != null) {
                        handleSectionMenuClick(event.getSlot(), title.replace(" Markt", ""), (Player) event.getWhoClicked());
                    }
                    if (event.getCurrentItem().equals(goback)) {
                        MarketCommand marketCommand = new MarketCommand(library);
                        marketCommand.openMainMenu((Player) event.getWhoClicked());
                    }
                } else if (title.startsWith("Aktion Auswählen")) {
                    event.setCancelled(true);
                    ItemStack clickedItem = event.getCurrentItem();
                    if (clickedItem != null) {
                        handleActionChoice(event.getSlot(), (Player) event.getWhoClicked());
                    }
                    if (event.getCurrentItem().equals(goback)) {
                        MarketCommand marketCommand = new MarketCommand(library);
                        marketCommand.openMainMenu((Player) event.getWhoClicked());
                    }
                } else if (title.startsWith("Wähle deine Anzahl")) {
                    event.setCancelled(true);
                    ItemStack clickedItem = event.getCurrentItem();

                    if (clickedItem != null && clickedItem.getType() != Material.GREEN_STAINED_GLASS_PANE && clickedItem.getType() != Material.RED_STAINED_GLASS_PANE && clickedItem.getType() != goback.getType()) {
                        handleQuantityChoice(event.getSlot(), (Player) event.getWhoClicked());
                    }
                    if (event.getCurrentItem().equals(goback)) {
                        MarketCommand marketCommand = new MarketCommand(library);
                        marketCommand.openMainMenu((Player) event.getWhoClicked());
                    }
                }
            }
        }
    }

    private void handleMainMenuClick(int slot, Player player) {
        FileConfiguration config = getConfig();
        String section = null;

        for (String key : config.getConfigurationSection("main-menu").getKeys(false)) {
            if (key.equals("size") || key.equals("title")) {
                continue;
            }

            if (config.getInt("main-menu." + key + ".slot") == slot) {
                section = config.getString("main-menu." + key + ".section");
                break;
            }
        }

        if (section != null) {
            openSectionMenu(section, player);
        }
    }

    private void handleSectionMenuClick(int slot, String section, Player player) {
        FileConfiguration config = getConfig();
        String key = null;

        for (String itemKey : config.getConfigurationSection("sections." + section).getKeys(false)) {
            if (itemKey.equals("size") || itemKey.equals("title")) {
                continue;
            }

            if (config.getInt("sections." + section + "." + itemKey + ".slot") == slot) {
                key = itemKey;
                break;
            }
        }

        if (key != null) {
            int buyPrice = config.getInt("sections." + section + "." + key + ".buy-price");
            int sellPrice = config.getInt("sections." + section + "." + key + ".sell-price");
            String materialName = config.getString("sections." + section + "." + key + ".item");

            if (materialName == null) {
                player.sendMessage(" §8[§5Error§7] §cItem Konfiguration ist nicht vorhanden für §e-> §e" + key);
                return;
            }

            Material material;
            try {
                material = Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                player.sendMessage("§8[§5Error§7] Ungültiger Itemtyp für §e-> §e" + materialName + " §cbei: §e" + key);
                return;
            }

            ItemStack item = new ItemStack(material);
            openActionChoiceMenu(player, item, buyPrice, sellPrice);
        }
    }

    private void openSectionMenu(String section, Player player) {
        FileConfiguration config = getConfig();
        int size = config.getInt("sections." + section + ".size", 27);
        Inventory sectionMenu = Bukkit.createInventory(null, size, section + " Markt");
        ItemBuilder nulled = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
        ItemBuilder goback = new ItemBuilder(Material.RED_BANNER).setDisplayName("§cZurück!");
        InventoryAdvancer.fillNulledInventory(nulled, sectionMenu);
        int lastIndex = sectionMenu.getSize() - 1;

        sectionMenu.setItem(lastIndex, goback.build());

        for (String key : config.getConfigurationSection("sections." + section).getKeys(false)) {
            if (key.equals("size") || key.equals("title")) {
                continue;
            }

            int slot = config.getInt("sections." + section + "." + key + ".slot");
            String materialName = config.getString("sections." + section + "." + key + ".item");

            if (materialName == null) {
                Bukkit.getLogger().severe("Material name is null for section: " + section + ", key: " + key);
                continue;
            }

            Material material;
            try {
                material = Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().severe("Invalid material name: " + materialName + " for section: " + section + ", key: " + key);
                continue;
            }

            String displayName = "§8» §e" + formatMaterialName(material);

            ItemBuilder itemBuilder = new ItemBuilder(material)
                    .addLoreLine(" §8• §8| §7» Kaufpreis: §e" + CoinFormatter.formatCoins(config.getInt("sections." + section + "." + key + ".buy-price")) + "§e€")
                    .addLoreLine(" ")
                    .addLoreLine(" §8• §8| §7» Verkaufspreis: §e" + CoinFormatter.formatCoins(config.getInt("sections." + section + "." + key + ".sell-price")) + "§e€")
                    .setDisplayName(displayName);

            sectionMenu.setItem(slot, itemBuilder.build());
        }
        library.playLibrarySound(player, CustomSound.GUI_SOUND, 1f, 1f);
        player.openInventory(sectionMenu);
    }

    private void openActionChoiceMenu(Player player, ItemStack item, int buyPrice, int sellPrice) {
        Inventory actionMenu = Bukkit.createInventory(null, 9, "Aktion Auswählen");
        ItemBuilder nulled = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
        InventoryAdvancer.fillNulledInventory(nulled, actionMenu);

        ItemStack buyItem = new ItemStack(Material.LIME_DYE);
        ItemMeta buyMeta = buyItem.getItemMeta();
        buyMeta.setDisplayName(ChatColor.GREEN + "Kaufen");
        buyItem.setItemMeta(buyMeta);

        ItemStack sellItem = new ItemStack(Material.RED_DYE);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName(ChatColor.RED + "Verkaufen");
        sellItem.setItemMeta(sellMeta);

        ItemBuilder goback = new ItemBuilder(Material.RED_BANNER).setDisplayName("§cZurück!");

        actionMenu.setItem(3, buyItem);
        actionMenu.setItem(5, sellItem);
        actionMenu.setItem(8, goback.build());

        library.playLibrarySound(player, CustomSound.GUI_SOUND, 1f, 1f);
        player.openInventory(actionMenu);
        player.setMetadata("market_item", new FixedMetadataValue(Main.getInstance(), item));
        player.setMetadata("buy_price", new FixedMetadataValue(Main.getInstance(), buyPrice));
        player.setMetadata("sell_price", new FixedMetadataValue(Main.getInstance(), sellPrice));
    }

    private void handleActionChoice(int slot, Player player) {
        if (slot == 3) {
            openQuantityChoiceMenu(player, true);
        } else if (slot == 5) {
            openQuantityChoiceMenu(player, false);
        }
    }

    private void openQuantityChoiceMenu(Player player, boolean isBuying) {
        ItemStack item = (ItemStack) player.getMetadata("market_item").get(0).value();
        Material material = item.getType();

        Inventory quantityMenu = Bukkit.createInventory(null, 27, "Wähle deine Anzahl");
        ItemBuilder nulled = new ItemBuilder(isBuying ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ");
        InventoryAdvancer.fillNulledInventory(nulled, quantityMenu);

        if (material.getMaxStackSize() > 1) {
            ItemStack[] quantities = {
                    createQuantityItem(material, 1),
                    createQuantityItem(material, 2),
                    createQuantityItem(material, 4),
                    createQuantityItem(material, 8),
                    createQuantityItem(material, 16),
                    createQuantityItem(material, 32),
                    createQuantityItem(material, 64)
            };

            for (int i = 0; i < quantities.length; i++) {
                quantityMenu.setItem(10 + i, quantities[i]);
            }
            ItemBuilder goback = new ItemBuilder(Material.RED_BANNER).setDisplayName("§cZurück!");
            quantityMenu.setItem(26, goback.build());
        } else {
            ItemStack singleItem = createQuantityItem(material, 1);
            quantityMenu.setItem(13, singleItem);
        }

        ItemStack maxTradeItem = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta maxTradeMeta = maxTradeItem.getItemMeta();
        maxTradeMeta.setDisplayName(isBuying ? "§7Max. Anzahl §eKaufen" : "§7Max. Anzahl §cVerkaufen");
        maxTradeItem.setItemMeta(maxTradeMeta);

        quantityMenu.setItem(22, maxTradeItem);

        library.playLibrarySound(player, CustomSound.GUI_SOUND, 1f, 1f);
        player.openInventory(quantityMenu);
        player.setMetadata("is_buying", new FixedMetadataValue(Main.getInstance(), isBuying));
    }

    private ItemStack createQuantityItem(Material material, int quantity) {


        ItemBuilder item = new ItemBuilder(material, quantity).setDisplayName("§8» §a" + formatMaterialName(material) + " §e" + quantity + "x").addLoreLine(" §8• §8| §7» §eInteragieren");
        return item.build();
    }


    private void handleQuantityChoice(int slot, Player player) {
        if (slot == 22) {
            handleMaxTrade(player);
        } else {
            Coins coins = Main.getInstance().getCoins();
            Inventory inventory = player.getOpenInventory().getTopInventory();

            ItemStack clickedItem = inventory.getItem(slot);
            if (clickedItem != null) {
                int quantity = clickedItem.getAmount();
                boolean isBuying = player.getMetadata("is_buying").get(0).asBoolean();
                ItemStack item = (ItemStack) player.getMetadata("market_item").get(0).value();
                int buyPrice = player.getMetadata("buy_price").get(0).asInt();
                int sellPrice = player.getMetadata("sell_price").get(0).asInt();

                if (isBuying) {
                    int totalPrice = buyPrice * quantity;
                    int playerCoins = coins.getCoins(player.getUniqueId());

                    if (playerCoins >= totalPrice) {
                        ItemStack itemToAdd = item.clone();
                        itemToAdd.setAmount(quantity);
                        if (!isInventoryFull(player)) {
                            coins.removeCoins(player.getUniqueId(), totalPrice);
                            player.getInventory().addItem(itemToAdd);
                            String itemName = formatMaterialName(item.getType());
                            library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                            player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §e" + quantity + " §e" + itemName + "§7 für §e" + CoinFormatter.formatCoins(totalPrice) + " §7Coins gekauft.");
                        } else {
                            library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                            player.sendMessage(Main.getInstance().getPrefix() + "§cDein Inventar ist voll!");
                            player.closeInventory();
                        }
                    } else {
                        library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                        player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genügend Coins um dieses Item zu kaufen");
                    }
                } else {
                    int totalPrice = sellPrice * quantity;
                    if (player.getInventory().containsAtLeast(item, quantity)) {
                        player.getInventory().removeItem(new ItemStack(item.getType(), quantity));
                        coins.addCoins(player.getUniqueId(), totalPrice);
                        String itemName = formatMaterialName(item.getType());
                        library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                        player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §e" + quantity + " §e" + itemName + "§7 für §e" + CoinFormatter.formatCoins(totalPrice) + " §7Coins verkauft.");
                    } else {
                        library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                        player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Items zum Verkaufen!");
                    }
                }
            }
        }
    }

    private void handleMaxTrade(Player player) {
        Coins coins = Main.getInstance().getCoins();
        ItemStack item = (ItemStack) player.getMetadata("market_item").get(0).value();
        int buyPrice = player.getMetadata("buy_price").get(0).asInt();
        int sellPrice = player.getMetadata("sell_price").get(0).asInt();
        boolean isBuying = player.getMetadata("is_buying").get(0).asBoolean();

        if (isBuying) {
            int playerCoins = coins.getCoins(player.getUniqueId());
            int maxBuyable = playerCoins / buyPrice;
            int spaceInInventory = getAvailableInventorySpace(player, item);
            int quantityToBuy = Math.min(maxBuyable, spaceInInventory);

            if (quantityToBuy > 0) {
                int totalPrice = buyPrice * quantityToBuy;

                if (!isInventoryFull(player)) {
                    if (playerCoins >= totalPrice) {
                        coins.removeCoins(player.getUniqueId(), totalPrice);
                        splitAndAddItemsToInventory(player, item, quantityToBuy);
                        String itemName = formatMaterialName(item.getType());
                        library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                        player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §e" + quantityToBuy + " §e" + itemName + "§7 für §e" + CoinFormatter.formatCoins(totalPrice) + " §7Coins gekauft.");
                    } else {
                        library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                        player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Coins um dieses Item zu kaufen.");
                        player.closeInventory();
                    }
                } else {
                    library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                    player.sendMessage(Main.getInstance().getPrefix() + "§cDein Inventar ist voll!");
                    player.closeInventory();
                }
            } else {
                library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Coins, um dieses Item zu kaufen.");
                player.closeInventory();
            }
        } else {
            int quantityToSell = getAvailableItemCount(player, item);
            if (quantityToSell > 0) {
                int totalPrice = sellPrice * quantityToSell;
                player.getInventory().removeItem(new ItemStack(item.getType(), quantityToSell));
                coins.addCoins(player.getUniqueId(), totalPrice);
                String itemName = formatMaterialName(item.getType());
                library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 1f);
                player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §e" + quantityToSell + " §e" + itemName + "§7 für §e" + CoinFormatter.formatCoins(totalPrice) + " §7Coins verkauft.");
            } else {
                library.playLibrarySound(player, CustomSound.NOT_ALLOWED, 1f, 1f);
                player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast nicht genug Items zum Verkaufen!");
                player.closeInventory();
            }
        }
    }


    private void splitAndAddItemsToInventory(Player player, ItemStack item, int quantity) {
        int maxStackSize = item.getMaxStackSize();
        while (quantity > 0) {
            int amountToAdd = Math.min(maxStackSize, quantity);
            ItemStack itemToAdd = item.clone();
            itemToAdd.setAmount(amountToAdd);
            player.getInventory().addItem(itemToAdd);
            quantity -= amountToAdd;
        }
    }

    private int getAvailableInventorySpace(Player player, ItemStack item) {
        int space = 0;
        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (inventoryItem == null) {
                space += item.getMaxStackSize();
            } else if (inventoryItem.isSimilar(item) && inventoryItem.getAmount() < inventoryItem.getMaxStackSize()) {
                space += item.getMaxStackSize() - inventoryItem.getAmount();
            }
        }
        return space;
    }


    private int getAvailableItemCount(Player player, ItemStack item) {
        int count = 0;
        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (inventoryItem != null && inventoryItem.isSimilar(item)) {
                count += inventoryItem.getAmount();
            }
        }
        return count;
    }

    private boolean isInventoryFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }

    private FileConfiguration getConfig() {
        return Main.getInstance().getConfig();
    }

    private String formatMaterialName(Material material) {
        String name = material.toString().toLowerCase().replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return formattedName.toString().trim();
    }
}
