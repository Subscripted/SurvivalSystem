package dev.subscripted.survivalsystem.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankPaymentSerivce {

    final Set<UUID> addingMoney = new HashSet<>();
    final Set<UUID> removingMoney = new HashSet<>();

    public boolean isAddingMoney(UUID playerUUID) {
        return addingMoney.contains(playerUUID);
    }

    public boolean isRemovingMoney(UUID playerUUID) {
        return removingMoney.contains(playerUUID);
    }

    public void addToAddingMoney(UUID playerUUID) {
        addingMoney.add(playerUUID);
    }

    public void addToRemovingMoney(UUID playerUUID) {
        removingMoney.add(playerUUID);
    }

    public void removeFromAddingMoney(UUID playerUUID) {
        addingMoney.remove(playerUUID);
    }

    public void removeFromRemovingMoney(UUID playerUUID) {
        removingMoney.remove(playerUUID);
    }

}
