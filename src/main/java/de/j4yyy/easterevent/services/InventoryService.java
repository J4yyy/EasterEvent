package de.j4yyy.easterevent.services;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InventoryService {

    private static final HashMap<UUID, Inventory> inventorySaves = new HashMap<>();

    public static void saveInventory(Player player, Inventory inventory) {
        Inventory save = Bukkit.createInventory(null, 54);
        for(ItemStack item : inventory.getContents()) {
            if(item != null) {
                save.addItem(item);
            }
        }
        inventorySaves.put(player.getUniqueId(), save);
    }

    public static Inventory loadInventory(Player player) {
        Inventory loaded = inventorySaves.get(player.getUniqueId());
        inventorySaves.remove(player.getUniqueId());
        return loaded;
    }

    public static HashMap<UUID, Inventory> getInventorySaves() {
        return inventorySaves;
    }
}