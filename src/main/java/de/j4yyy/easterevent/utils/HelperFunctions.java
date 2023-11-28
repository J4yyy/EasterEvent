package de.j4yyy.easterevent.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class HelperFunctions {
    public static Block getBlockPlayerLookingAt(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();

        while (iter.hasNext()) {
            lastBlock = iter.next();
            if(lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
}