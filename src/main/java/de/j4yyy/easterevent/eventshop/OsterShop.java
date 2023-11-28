package de.j4yyy.easterevent.eventshop;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.utils.Constants;
import de.j4yyy.easterevent.utils.ItemBuilder;
import de.j4yyy.easterevent.services.EggService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OsterShop implements Listener, CommandExecutor {

    private enum SHOP_PAGE {
        HOME,
        TIER_ONE,
        TIER_TWO,
        TIER_THREE,
        TIER_FOUR,
        NO_SHOP
    }

    // Shop Names
    private final String invName = ChatColor.GREEN + "Oster Shop";
    private final String tierOneName = ChatColor.GRAY + "Oster Shop";
    private final String tierTwoName = ChatColor.GOLD + "Oster Shop";
    private final String tierThreeName = ChatColor.DARK_GREEN + "Oster Shop";
    private final String tierFourName = ChatColor.AQUA + "Oster Shop";

    private final String closeButtonName = ChatColor.RED + "Schließen";
    private final String backButtonName = ChatColor.DARK_PURPLE + "Zurück";

    public OsterShop(EasterEvent plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        SHOP_PAGE page = getPageType(event.getView().getTitle());
        if(page.equals(SHOP_PAGE.NO_SHOP)) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int playerBalance = EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId());

        if(event.getSlot()==18 && !page.equals(SHOP_PAGE.HOME)) {
            openHomeShop(player, playerBalance);
        } else if(event.getSlot()==26) {
            player.closeInventory();
            return;
        }

        switch (page) {
            case HOME -> {
                int slot = event.getSlot();
                switch (slot) {
                    case 10 -> {
                        openTierOneShop(player, playerBalance, true);
                    }
                    case 12 -> {
                        openTierTwoShop(player, playerBalance, true);
                    }
                    case 14 -> {
                        openTierThreeShop(player, playerBalance, true);
                    }
                    case 16 -> {
                        openTierFourShop(player, playerBalance, true);
                    }
                }
            }
            case TIER_ONE -> {
                if(!EggService.playerHasEnoughBalance(player, 1) && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    player.sendMessage("Du hast nicht genügend Eier gefunden um dir diesen Preis zu kaufen");
                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 0F);
                    return;
                }
                if(event.getSlot() == 10) {
                    EasterEvent.instance.getServer().dispatchCommand(EasterEvent.instance.getServer().getConsoleSender(), "cmi kit Osterkopf1 " + player.getName());
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 1);
                    openTierOneShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                } else if(event.getSlot() == 11) {
                    EasterEvent.instance.getServer().dispatchCommand(EasterEvent.instance.getServer().getConsoleSender(), "cmi kit Osterkopf2 " + player.getName());
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 1);
                    openTierOneShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                } else if(event.getSlot() == 12) {
                    EasterEvent.instance.getServer().dispatchCommand(EasterEvent.instance.getServer().getConsoleSender(), "cmi kit Osterkopf3 " + player.getName());
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 1);
                    openTierOneShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                } else if(event.getSlot() == 13) {
                    EasterEvent.instance.getServer().dispatchCommand(EasterEvent.instance.getServer().getConsoleSender(), "cmi kit Osterkopf4 " + player.getName());
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 1);
                    openTierOneShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                }
            }
            case TIER_TWO -> {
                if(!EggService.playerHasEnoughBalance(player, 5) && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    player.sendMessage("Du hast nicht genügend Eier gefunden um dir diesen Preis zu kaufen");
                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 0F);
                    return;
                }
                if(event.getCurrentItem() != null && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    ItemStack reward = event.getCurrentItem().clone();
                    ItemMeta meta = reward.getItemMeta();
                    meta.setLore(new ArrayList<>(List.of("", ChatColor.DARK_AQUA + "Osterevent " + ChatColor.GREEN +"2023")));
                    reward.setItemMeta(meta);
                    player.getInventory().addItem(reward);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 5);
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    openTierTwoShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                }
            }
            case TIER_THREE -> {
                if(!EggService.playerHasEnoughBalance(player, 15) && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    player.sendMessage("Du hast nicht genügend Eier gefunden um dir diesen Preis zu kaufen");
                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 0F);
                    return;
                }
                if(event.getCurrentItem() != null && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    ItemStack reward = event.getCurrentItem().clone();
                    ItemMeta meta = reward.getItemMeta();
                    meta.setLore(new ArrayList<>(List.of("", ChatColor.DARK_AQUA + "Osterevent " + ChatColor.GREEN +"2023")));
                    reward.setItemMeta(meta);
                    player.getInventory().addItem(reward);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 15);
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    openTierThreeShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                }
            }
            case TIER_FOUR -> {
                if(!EggService.playerHasEnoughBalance(player, 25) && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    player.sendMessage("Du hast nicht genügend Eier gefunden um dir diesen Preis zu kaufen");
                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 0F);
                    return;
                }
                if(event.getCurrentItem() != null && isRewardItem(event.getCurrentItem(), event.getSlot())) {
                    ItemStack reward = event.getCurrentItem().clone();
                    ItemMeta meta = reward.getItemMeta();
                    meta.setLore(new ArrayList<>(List.of("", ChatColor.DARK_AQUA + "Osterevent " + ChatColor.GREEN +"2023")));
                    reward.setItemMeta(meta);
                    player.getInventory().addItem(reward);
                    EasterEvent.instance.getDbConnection().removePlayerBalance(player.getUniqueId(), 25);
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
                    openTierFourShop(player, EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()), false);
                }
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by Players");
        }
        Player player = (Player) sender;
        int playerBalance = EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId());
        openHomeShop(player, playerBalance);
        return false;
    }

    private void openHomeShop(Player customer, int playerBalance) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, invName);
        customer.openInventory(inv);
        inv.setItem(10, new ItemBuilder(Material.IRON_INGOT).setDisplayname(ChatColor.GRAY + "Tier 1 Belohnungen").build());
        inv.setItem(12, new ItemBuilder(Material.GOLD_INGOT).setDisplayname(ChatColor.GOLD + "Tier 2 Belohnungen").setLore("&7»Rabbit Spawnegg", "&7»Glow Squid Spawnegg", "&7»Ocelot Spawnegg", "&7»Fox Spawnegg", "&7»Parrot Spawnegg").build());
        inv.setItem(14, new ItemBuilder(Material.EMERALD).setDisplayname(ChatColor.GREEN + "Tier 3 Belohnungen").setLore("&7»Axolotl Spawnegg", "&7»Panda Spawnegg", "&7»Magma Cube Spawnegg").build());
        inv.setItem(16, new ItemBuilder(Material.DIAMOND).setDisplayname(ChatColor.AQUA + "Tier 4 Belohnungen").setLore("&7»Slime Spawnegg").build());
        inv.setItem(26, new ItemBuilder(Material.BARRIER).setDisplayname(closeButtonName).build());

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                inv.setItem(Constants.shopFrameNineByThreeHome.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
                if(i >= Constants.shopFrameNineByThreeHome.size()-1) {
                    cancel();
                } else {
                    i++;
                }
            }
        }.runTaskTimer(EasterEvent.instance, 0L, 2L);
    }

    private void openTierOneShop(Player customer, int playerBalance, boolean initialOpen) {
        Inventory tierTwo = Bukkit.createInventory(null, 9 * 3, tierOneName);
        customer.closeInventory();

        int c = 0;

        for(ItemStack reward : Constants.tierOneRewards()) {
            tierTwo.setItem(Constants.shopSlotsNineByThree.get(c), reward);
            c++;
        }

        tierTwo.setItem(18, new ItemBuilder(Material.CHARCOAL).setDisplayname(backButtonName).build());
        tierTwo.setItem(26, new ItemBuilder(Material.BARRIER).setDisplayname(closeButtonName).build());

        customer.openInventory(tierTwo);

        if(initialOpen) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
                    if(i >= Constants.shopFrameNineByThree.size()-1) {
                        cancel();
                    } else {
                        i++;
                    }
                }
            }.runTaskTimer(EasterEvent.instance, 0L, 2L);
        } else {
            for(int i = 0; i < Constants.shopFrameNineByThree.size(); i++) {
                tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
            }
        }
    }

    private void openTierTwoShop(Player customer, int playerBalance, boolean initialOpen) {
        Inventory tierTwo = Bukkit.createInventory(null, 9 * 3, tierTwoName);
        customer.closeInventory();

        int c = 0;

        for(ItemStack reward : Constants.tierTwoRewards()) {
            tierTwo.setItem(Constants.shopSlotsNineByThree.get(c), reward);
            c++;
        }

        tierTwo.setItem(18, new ItemBuilder(Material.CHARCOAL).setDisplayname(backButtonName).build());
        tierTwo.setItem(26, new ItemBuilder(Material.BARRIER).setDisplayname(closeButtonName).build());

        customer.openInventory(tierTwo);

        if(initialOpen) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
                    if(i >= Constants.shopFrameNineByThree.size()-1) {
                        cancel();
                    } else {
                        i++;
                    }
                }
            }.runTaskTimer(EasterEvent.instance, 0L, 2L);
        } else {
            for(int i = 0; i < Constants.shopFrameNineByThree.size(); i++) {
                tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
            }
        }
    }

    private void openTierThreeShop(Player customer, int playerBalance, boolean initialOpen) {
        Inventory tierTwo = Bukkit.createInventory(null, 9 * 3, tierThreeName);
        customer.closeInventory();

        int c = 0;

        for(ItemStack reward : Constants.tierThreeRewards()) {
            tierTwo.setItem(Constants.shopSlotsNineByThree.get(c), reward);
            c++;
        }

        tierTwo.setItem(18, new ItemBuilder(Material.CHARCOAL).setDisplayname(backButtonName).build());
        tierTwo.setItem(26, new ItemBuilder(Material.BARRIER).setDisplayname(closeButtonName).build());

        customer.openInventory(tierTwo);

        if(initialOpen) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
                    if(i >= Constants.shopFrameNineByThree.size()-1) {
                        cancel();
                    } else {
                        i++;
                    }
                }
            }.runTaskTimer(EasterEvent.instance, 0L, 2L);
        } else {
            for(int i = 0; i < Constants.shopFrameNineByThree.size(); i++) {
                tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
            }
        }
    }

    private void openTierFourShop(Player customer, int playerBalance, boolean initialOpen) {
        Inventory tierTwo = Bukkit.createInventory(null, 9 * 3, tierFourName);
        customer.closeInventory();

        int c = 0;

        for(ItemStack reward : Constants.tierFourRewards()) {
            tierTwo.setItem(Constants.shopSlotsNineByThree.get(c), reward);
            c++;
        }

        tierTwo.setItem(18, new ItemBuilder(Material.CHARCOAL).setDisplayname(backButtonName).build());
        tierTwo.setItem(26, new ItemBuilder(Material.BARRIER).setDisplayname(closeButtonName).build());

        customer.openInventory(tierTwo);

        if(initialOpen) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
                    if(i >= Constants.shopFrameNineByThree.size()-1) {
                        cancel();
                    } else {
                        i++;
                    }
                }
            }.runTaskTimer(EasterEvent.instance, 0L, 2L);
        } else {
            for(int i = 0; i < Constants.shopFrameNineByThree.size(); i++) {
                tierTwo.setItem(Constants.shopFrameNineByThree.get(i), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("&2Frohe Ostern").setLore("&7Dein Guthaben: " + ChatColor.GREEN + playerBalance).build());
            }
        }
    }

    private SHOP_PAGE getPageType(String invTitle) {
        if(invTitle.equalsIgnoreCase(invName)) {
            return SHOP_PAGE.HOME;
        } else if(invTitle.equalsIgnoreCase(tierOneName)) {
            return SHOP_PAGE.TIER_ONE;
        } else if(invTitle.equalsIgnoreCase(tierTwoName)) {
            return SHOP_PAGE.TIER_TWO;
        } else if(invTitle.equalsIgnoreCase(tierThreeName)) {
            return SHOP_PAGE.TIER_THREE;
        } else if(invTitle.equalsIgnoreCase(tierFourName)) {
            return SHOP_PAGE.TIER_FOUR;
        } else {
            return SHOP_PAGE.NO_SHOP;
        }
    }

    private boolean isRewardItem(ItemStack item, int slot) {
        if(!Constants.shopSlotsNineByThree.contains(slot)) {
            return false;
        }
        if(item == null) {
            return false;
        }
        for(ItemStack i : Constants.tierOneRewards()) {
            if(item.equals(i)) {
                return true;
            }
        }
        for(ItemStack i : Constants.tierTwoRewards()) {
            if(item.equals(i)) {
                return true;
            }
        }
        for(ItemStack i : Constants.tierThreeRewards()) {
            if(item.equals(i)) {
                return true;
            }
        }
        for(ItemStack i : Constants.tierFourRewards()) {
            if(item.equals(i)) {
                return true;
            }
        }
        return false;
    }
}