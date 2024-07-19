package dev.subscripted.survivalsystem.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankPaymentSerivce {

    final Set<UUID> addingMoney = new HashSet<>();
    final Set<UUID> removingMoney = new HashSet<>();


    public boolean isAddingMoney(Player player) {
        return addingMoney.contains(player.getUniqueId());
    }

    public boolean isRemovingMoney(Player player) {
        return removingMoney.contains(player.getUniqueId());
    }

    public void addToAddingMoney(Player player) {
        addingMoney.add(player.getUniqueId());
    }

    public void addToRemovingMoney(Player player) {
        removingMoney.add(player.getUniqueId());
    }

    public void removeFromAddingMoney(Player player) {
        addingMoney.remove(player.getUniqueId());
    }

    public void removeFromRemovingMoney(Player player) {
        removingMoney.remove(player.getUniqueId());
    }

}
