package de.j4yyy.easterevent.listener;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.services.EggService;
import de.j4yyy.easterevent.services.InventoryService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DateFormat dft = new SimpleDateFormat("dd.MM.yyyy");
        if(dft.format(new Date()).startsWith("09.04") || dft.format(new Date()).startsWith("10.04")) {
            event.getPlayer().sendMessage("Frohe Ostern");
        }
        EggService.loadPlayerClaims(event.getPlayer());
        EasterEvent.instance.getDbConnection().initPlayerBalance(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        EggService.freePlayerClaims(event.getPlayer());
        if(EggService.isBuilderModeEnabled(event.getPlayer())) {
            event.getPlayer().getInventory().clear();
            for(ItemStack item : InventoryService.loadInventory(event.getPlayer()).getContents()) {
                if(item != null) {
                    event.getPlayer().getInventory().addItem(item);
                }
            }
        }
    }
}