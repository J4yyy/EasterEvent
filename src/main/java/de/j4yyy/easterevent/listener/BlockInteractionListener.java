package de.j4yyy.easterevent.listener;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.services.EggService;
import de.j4yyy.easterevent.services.entities.Egg;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BlockInteractionListener implements Listener {

    private static final ArrayList<Player> timeoutEvent = new ArrayList<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        if(brokenBlock == null) {
            return;
        }
        if(EggService.isEgg(brokenBlock)) {
            if(!EggService.isBuilderModeEnabled(event.getPlayer())) {
                event.setCancelled(true);
            } else {
                EggService.deleteEgg(EggService.getEggFromBlock(brokenBlock));
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if(EggService.isBuilderModeEnabled(event.getPlayer())) {
            EggService.createEgg(event.getBlockPlaced());
        }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) {
            return;
        }
        Player player = event.getPlayer();
        if(EggService.isEgg(event.getClickedBlock()) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !timeoutEvent.contains(player) && !EggService.isBuilderModeEnabled(player)) {
            Egg egg = EggService.getEggFromBlock(event.getClickedBlock());
            if(!EggService.hasPlayerFoundEgg(player.getUniqueId(), egg)) {
                EggService.addPlayerClaimEgg(player, egg);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 0.8F);
            } else {
                player.sendMessage("Dieses Ei hast du schon gefunden, begib dich weiter auf die suche");
            }
            player.clearTitle();
            timeoutEvent.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    timeoutEvent.remove(player);
                }
            }.runTaskLater(EasterEvent.instance, 20L*2L);
        }
    }
}