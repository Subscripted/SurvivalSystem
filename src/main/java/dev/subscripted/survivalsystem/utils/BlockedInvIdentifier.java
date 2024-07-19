package dev.subscripted.survivalsystem.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockedInvIdentifier implements InventoryHolder {

    final String identifier;

    public BlockedInvIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
