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

    public static void makePattern(Inventory inventory, ItemBuilder itemStack) {
        int[] slots = {9, 10, 1, 2, 3, 4, 5, 6, 7, 16, 17, 26, 35, 44, 43, 52, 51, 50, 49, 48, 47, 46, 37, 36, 27, 18, 9};

        for (int slot : slots) {
            if (slot < inventory.getSize()) {
                inventory.setItem(slot, itemStack.build());
            }
        }
    }

    public static void fillCorners(Inventory inventory, ItemBuilder itemStack) {
        int size = inventory.getSize();
        int rows = size / 9;

        if (rows < 1) {
            return;
        }

        int[] cornerSlots = {
                0, 8,
                (rows - 1) * 9, (rows - 1) * 9 + 8
        };

        for (int slot : cornerSlots) {
            if (slot < size) {
                inventory.setItem(slot, itemStack.build());
            }
        }
    }
}