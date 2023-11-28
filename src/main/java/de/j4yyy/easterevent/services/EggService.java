package de.j4yyy.easterevent.services;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.services.entities.Egg;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EggService {
    private static HashMap<UUID, ArrayList<Egg>> found_eggs = new HashMap<>();
    private static ArrayList<Egg> egg_list = new ArrayList<>();
    private static ArrayList<UUID> easter_builder = new ArrayList<>();

    public static boolean isEgg(Block block) {
        return getEggFromBlock(block) != null;
    }

    public static boolean hasPlayerFoundEgg(UUID uuid, Egg clickedEgg) {
        for(Egg egg : found_eggs.get(uuid)) {
            if(egg.getDb_id() == clickedEgg.getDb_id()) {
                return true;
            }
        }
        return false;
    }

    public static void addPlayerClaimEgg(Player player, Egg egg) {
        EasterEvent.instance.getDbConnection().addPlayerClaim(player.getUniqueId(), egg);
        found_eggs.get(player.getUniqueId()).add(egg);
        EasterEvent.instance.getDbConnection().addPlayerBalance(player.getUniqueId(), 1);
        player.sendMessage("Glückwunsch du hast " + found_eggs.get(player.getUniqueId()).size() + "/" + egg_list.size() + " Eier gefunden");
    }

    public static Egg getEggFromBlock(Block block) {
        for(Egg egg : egg_list) {
            if(egg.getPos().getWorld().equals(block.getWorld())) {
                if(egg.getPos().distance(block.getLocation()) == 0) {
                    return egg;
                }
            }
        }
        return null;
    }

    public static boolean isBuilderModeEnabled(Player player) {
        for(UUID uuid : easter_builder) {
            if(uuid.equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public static void toggleBuilderMode(Player player) {
        if(easter_builder.contains(player.getUniqueId())) {
            easter_builder.remove(player.getUniqueId());
        } else {
            easter_builder.add(player.getUniqueId());
        }
    }

    public static void createEgg(Block block) {
        egg_list.add(EasterEvent.instance.getDbConnection().createEgg(block));
    }

    public static void deleteEgg(Egg egg) {
        EasterEvent.instance.getDbConnection().removeEgg(egg);
        for(Map.Entry<UUID, ArrayList<Egg>> entry : found_eggs.entrySet()) {
            entry.getValue().removeIf(foundEggs -> foundEggs.getDb_id() == egg.getDb_id());
        }
        egg_list.removeIf(eggs -> eggs.getDb_id() == egg.getDb_id());
    }

    public static void clearPlayerClaims(Egg egg) {
        for(Map.Entry<UUID, ArrayList<Egg>> entry : found_eggs.entrySet()) {
            entry.getValue().removeIf(foundEggs -> foundEggs.getDb_id() == egg.getDb_id());
        }
    }

    public static void loadPlayerClaims(Player player) {
        ArrayList<Egg> foundEggs = EasterEvent.instance.getDbConnection().getPlayerClaims(player.getUniqueId());
        if(foundEggs != null) {
            found_eggs.put(player.getUniqueId(), foundEggs);
        }
    }

    public static boolean playerHasEnoughBalance(Player player, int price) {
        return EasterEvent.instance.getDbConnection().getPlayerEasterBalance(player.getUniqueId()) >= price;
    }

    public static void freePlayerClaims(Player player) {
        found_eggs.remove(player.getUniqueId());
    }

    public static void loadAllEggs() {
        egg_list = EasterEvent.instance.getDbConnection().getAllEggs();
    }

    public static ArrayList<Egg> getEgg_list() {
        return egg_list;
    }
}