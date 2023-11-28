package de.j4yyy.easterevent;

import de.j4yyy.easterevent.utils.Config;
import de.j4yyy.easterevent.utils.DBConnection;
import de.j4yyy.easterevent.commands.EasterCommand;
import de.j4yyy.easterevent.commands.tabcomp.EasterCommandTabComp;
import de.j4yyy.easterevent.eventshop.OsterShop;
import de.j4yyy.easterevent.listener.BlockInteractionListener;
import de.j4yyy.easterevent.listener.ConnectionListener;
import de.j4yyy.easterevent.services.EggService;
import de.j4yyy.easterevent.services.InventoryService;
import de.j4yyy.easterevent.services.ParticleService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class EasterEvent extends JavaPlugin {

    private Config mainConfig;
    private DBConnection dbConnection;

    public static EasterEvent instance;

    @Override
    public void onEnable() {
        instance = this;
        mainConfig = new Config("config.yml", getDataFolder());
        dbConnection = new DBConnection();

        registerCommands();
        registerListener();

        EggService.loadAllEggs();
        for(Player p : Bukkit.getOnlinePlayers()) {
            EggService.loadPlayerClaims(p);
        }

        ParticleService.startParticleSpawn();
    }

    @Override
    public void onDisable() {
        for(Map.Entry<UUID, Inventory> inv : InventoryService.getInventorySaves().entrySet()) {
            Player player = Bukkit.getPlayer(inv.getKey());
            if(player != null) {
                player.getInventory().clear();
                for(ItemStack item : InventoryService.loadInventory(player).getContents()) {
                    if(item != null) {
                        player.getInventory().addItem(item);
                    }
                }
            }
        }
    }

    private void registerCommands() {
        // Easter Command
        Objects.requireNonNull(this.getCommand("easter")).setExecutor(new EasterCommand());
        Objects.requireNonNull(this.getCommand("easter")).setTabCompleter(new EasterCommandTabComp());

        // Oster Shop Command
        Objects.requireNonNull(this.getCommand("ostershop")).setExecutor(new OsterShop(this));
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new BlockInteractionListener(), this);
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public DBConnection getDbConnection() {
        return this.dbConnection;
    }
}