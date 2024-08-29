package dev.subscripted.survivalsystem.modules.death;

import dev.subscripted.survivalsystem.Main;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.utils.CoinFormatter;
import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PlayerDeathService implements Listener {

    final SoundLibrary library;
    final Coins coins;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        String prefix = "§8» §8| §x§C§B§2§D§3§E§lT§x§D§D§3§A§3§C§lO§x§E§F§4§7§3§A§lD §8» ";

        Player player = event.getPlayer();
        DeathReason reason = determineDeathReason(event);
        event.setDeathMessage(prefix + "§e" + player.getName() + " §c" + reason.getMessage());
        library.playSoundForAll(CustomSound.DEATH, 1f, 3f);

        removeCoinsOnDeath(player);

    }

    private DeathReason determineDeathReason(PlayerDeathEvent event) {
        if (event.getEntity().getLastDamageCause() != null) {
            switch (event.getEntity().getLastDamageCause().getCause()) {
                case FALL:
                    return DeathReason.FALL_DAMAGE;
                case FIRE:
                case FIRE_TICK:
                    return DeathReason.FIRE;
                case DROWNING:
                    return DeathReason.DROWNING;
                case ENTITY_ATTACK:
                case ENTITY_SWEEP_ATTACK:
                    if (event.getEntity().getKiller() != null) {
                        return DeathReason.PLAYER_ATTACK;
                    } else {
                        return DeathReason.MOB_ATTACK;
                    }
                case SUFFOCATION:
                    return DeathReason.SUFFOCATION;
                case VOID:
                    return DeathReason.VOID;
                case MAGIC:
                    return DeathReason.MAGIC;
                case LAVA:
                    return DeathReason.BURNED_IN_LAVA;
                default:
                    return DeathReason.UNKNOWN;
            }
        } else {
            return DeathReason.UNKNOWN;
        }
    }

    public void removeCoinsOnDeath(Player player) {
        UUID playerUUID = player.getUniqueId();
        long playerCoins = coins.getCoins(playerUUID);

        if (playerCoins == 0) {
            return;
        }

        long coinsToRemove = (playerCoins * 24) / 100;

        long newCoinAmount = Math.max(playerCoins - coinsToRemove, 0);

        coins.setCoins(playerUUID, (int) newCoinAmount);

        // Ausgabe der Nachricht an den Spieler
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage("§7Du bist §cgestorben §7und hast dadurch §e24% §7(§e" + CoinFormatter.formatCoins((int) coinsToRemove) + "€§7) §7 deines §eGeldes §cverloren! §7Dein §eKontostand §7beträgt nun §e" + CoinFormatter.formatCoins((int) newCoinAmount));
        player.sendMessage(" ");
        player.sendMessage(" ");
    }


}
