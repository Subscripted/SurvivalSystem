package dev.subscripted.survivalsystem.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum InventoryAdvancer {
    LINE_1(0), LINE_2(9), LINE_3(18), LINE_4(27), LINE_5(36), LINE_6(45);

    final int startingIndex;

    InventoryAdvancer(int startingIndex) {
        this.startingIndex = startingIndex;
    }


    public static void fillInventoryLine(Inventory inventory, InventoryAdvancer line, ItemStack item) {
        int startingIndex = line.getStartingIndex();
        for (int i = startingIndex; i < startingIndex + 9; i++) {
            inventory.setItem(i, item);
        }
    }

    public static void fillNulledInventory(ItemBuilder itemBuilder, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemBuilder.build());
        }
    }
}