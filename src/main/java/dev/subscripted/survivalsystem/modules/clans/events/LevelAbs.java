package dev.subscripted.survivalsystem.modules.clans.events;

import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.utils.DamageModifier;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelAbs implements Listener {

    final SoundLibrary library;
    final ClanManager clanManager;


    @SneakyThrows
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();
        if (damager instanceof Player) {
            Player attacker = (Player) damager;
            int clanLevel = getClanLevel(attacker);

            double damageMultiplier = DamageModifier.getDamageMultiplier(clanLevel);

            double originalDamage = event.getDamage();
            double increasedDamage = originalDamage * damageMultiplier;
            UUID uuid = damager.getUniqueId();
            String clanPrefix = clanManager.getClanPrefix(uuid);
            if (clanManager.isMemberOfClan(damager.getUniqueId(), clanPrefix)) {
                event.setDamage(increasedDamage);
                System.out.println(event.getDamage());
            }
        }
    }

    @SneakyThrows
    public int getClanLevel(Player p) {
        UUID uuid = p.getUniqueId();
        String clanPrefix = clanManager.getClanPrefix(uuid);
        return clanManager.getClanLevel(clanPrefix);
    }
}


