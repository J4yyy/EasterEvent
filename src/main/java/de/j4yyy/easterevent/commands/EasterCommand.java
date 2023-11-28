package de.j4yyy.easterevent.commands;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.services.EggService;
import de.j4yyy.easterevent.services.InventoryService;
import de.j4yyy.easterevent.services.entities.Egg;
import de.j4yyy.easterevent.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class EasterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only can used by Player");
        }
        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "builder" -> {
                if(!player.hasPermission("easter.builder") && !player.hasPermission("easter.admin")) {
                    sender.sendMessage("You're lacking permissions");
                    return false;
                }
                if(EggService.isBuilderModeEnabled(player)) {
                    player.getInventory().clear();
                    for(ItemStack item : InventoryService.loadInventory(player).getContents()) {
                        if(item != null) {
                            player.getInventory().addItem(item);
                        }
                    }
                    player.sendMessage("Oster-Bau modus jetzt inaktiv");
                } else {
                    InventoryService.saveInventory(player, player.getInventory());
                    player.getInventory().clear();

                    for(ItemStack item : Constants.eggs()) {
                        player.getInventory().addItem(item);
                    }
                    player.sendMessage("Oster-Bau modus jetzt aktiv");
                }
                EggService.toggleBuilderMode(player);
            }
            case "give" -> {
                if(!player.hasPermission("easter.admin")) {
                    sender.sendMessage("You're lacking permissions");
                    return false;
                }
                if(args.length != 3) {
                    player.sendMessage("Fehlende Argumente");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null) {
                    EasterEvent.instance.getDbConnection().addPlayerBalance(target.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage("Dem Spieler " + target.getName() + " wurden " + Integer.parseInt(args[2]) + " Ostereier gegeben");
                } else {
                    OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                    EasterEvent.instance.getDbConnection().addPlayerBalance(offlineTarget.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage("Dem Spieler " + offlineTarget.getName() + " wurden " + Integer.parseInt(args[2]) + " Ostereier gegeben");
                }
            }
            case "remove" -> {
                if(!player.hasPermission("easter.admin")) {
                    sender.sendMessage("You're lacking permissions");
                    return false;
                }
                if(args.length != 3) {
                    player.sendMessage("Fehlende Argumente");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null) {
                    EasterEvent.instance.getDbConnection().removePlayerBalance(target.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage("Dem Spieler " + target.getName() + " wurden " + Integer.parseInt(args[2]) + " Ostereier abgezogen");
                } else {
                    OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                    EasterEvent.instance.getDbConnection().addPlayerBalance(offlineTarget.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage("Dem Spieler " + offlineTarget.getName() + " wurden " + Integer.parseInt(args[2]) + " Ostereier abgezogen");
                }
            }
            case "set" -> {
                if(!player.hasPermission("easter.admin")) {
                    sender.sendMessage("You're lacking permissions");
                    return false;
                }
                if(args.length != 3) {
                    player.sendMessage("Fehlende Argumente");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null) {
                    EasterEvent.instance.getDbConnection().setPlayerBalance(target.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage("Dem Spieler " + target.getName() + " wurden " + Integer.parseInt(args[2]) + " Ostereier gesetzt");
                } else {
                    OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                    EasterEvent.instance.getDbConnection().addPlayerBalance(offlineTarget.getUniqueId(), Integer.parseInt(args[2]));
                    player.sendMessage("Dem Spieler " + offlineTarget.getName() + " wurden " + Integer.parseInt(args[2]) + " Ostereier gesetzt");
                }
            }
            case "show" -> {
                if(!player.hasPermission("easter.admin")) {
                    sender.sendMessage("Dir fehlen Berechtigungen");
                    return false;
                }
                if(args.length != 2) {
                    player.sendMessage("Fehlende Argumente");
                    return false;
                }
                Player onlineTarget = Bukkit.getPlayer(args[1]);
                int amount = 0;
                if(onlineTarget != null) {
                    amount = EasterEvent.instance.getDbConnection().getPlayerEasterBalance(onlineTarget.getUniqueId());
                } else {
                    amount = EasterEvent.instance.getDbConnection().getPlayerEasterBalance(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                }
                player.sendMessage("Der Spieler " + args[1] + " hat " + amount + " Eier");
            }
            case "reset" -> {
                if(!player.hasPermission("easter.admin")) {
                    sender.sendMessage("You're lacking permissions");
                    return false;
                }
                if(args.length != 2) {
                    player.sendMessage("Fehlende Argumente");
                    return false;
                }
                for(Egg egg : EggService.getEgg_list()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(args[1].toLowerCase().equalsIgnoreCase("full")) {
                                EggService.deleteEgg(egg);
                                egg.getPos().getBlock().setType(Material.AIR);
                            } else {
                                EasterEvent.instance.getDbConnection().deleteClaim(egg);
                                EggService.clearPlayerClaims(egg);
                            }
                        }
                    }.runTaskLater(EasterEvent.instance, 20L);
                }
                EasterEvent.instance.getDbConnection().resetBalance();
            }
        }
        return false;
    }
}

//easter builder - done
//easter give player amount
//easter remove player amount
//easter set player amount
//easter show player
//easter reset full|balance