package dev.subscripted.survivalsystem.modules.clans.events;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.utils.ChanceService;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class EntityDeath implements Listener {

    final SoundLibrary library;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Wither) {
            if (ChanceService.checkChance(80)) {
                ItemStack emeraldStack = new ItemStack(Material.EMERALD);
                Item emeraldItem = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), emeraldStack);
                emeraldItem.setPickupDelay(0);
                emeraldItem.setCustomName("§aSpezial Smaragd");
                emeraldItem.setCustomNameVisible(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (item.getItemStack().getType() == Material.EMERALD && "§aSpezial Smaragd".equals(item.getCustomName())) {
            library.playLibrarySound(event.getPlayer(), CustomSound.MEMORY3, 1f, 0.5f);
            event.getPlayer().spawnParticle(Particle.END_ROD,  event.getPlayer().getLocation(), 1000);
            event.getPlayer().spawnParticle(Particle.SOUL_FIRE_FLAME,  event.getPlayer().getLocation(), 1000);
        }
    }

}
