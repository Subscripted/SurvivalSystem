package dev.subscripted.survivalsystem.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemBuilder {

    ItemStack item;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder setDisplayName(String diplayName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(diplayName);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLoreLine(String loreLine) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(loreLine);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(itemFlag);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        if (item.getType().name().contains("LEATHER_")) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(color);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setMapId(int mapId) {
        if (item.getType() == Material.FILLED_MAP) {
            MapMeta meta = (MapMeta) item.getItemMeta();
            meta.setMapId(mapId);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setPotionEffect(PotionEffectType type, int duration, int amplifier) {
        if (item.getType() == Material.POTION) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        if (glowing) {
            item.addUnsafeEnchantment(Enchantment.LURE, 1);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        } else {
            item.removeEnchantment(Enchantment.LURE);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(String playerName) {
        if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(playerName);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setOfflineSkullOwner(UUID playerUUID) {
        if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            meta.setOwningPlayer(offlinePlayer);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setStackable(boolean stackable) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(stackable);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int stackSize) {
        item.setAmount(stackSize);
        return this;
    }

    public ItemBuilder setDyeColor(DyeColor dyeColor) {
        if (item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_CHESTPLATE ||
                item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_HELMET) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(dyeColor.getColor());
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        ItemMeta meta = item.getItemMeta();
        meta.removeItemFlags(flags);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setFireworkEffects(List<FireworkEffect> fireworkEffects) {
        if (item.getType() == Material.FIREWORK_ROCKET) {
            FireworkMeta meta = (FireworkMeta) item.getItemMeta();
            meta.addEffects(fireworkEffects);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setSkullTexture(String textureBase64) {
        if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(null); // Nullify any existing owner

            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", textureBase64));

            try {
                Field profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
