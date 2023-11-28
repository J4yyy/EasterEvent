package de.j4yyy.easterevent.services;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.services.entities.Egg;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleService {
    public static void startParticleSpawn() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    for(Egg egg : EggService.getEgg_list()) {
                        if(!EggService.hasPlayerFoundEgg(p.getUniqueId(), egg)) {
                            p.spawnParticle(Particle.GLOW, egg.getPos().clone().add(0.3, 0, 0.3), 5);
                        }
                    }
                }
            }
        }.runTaskTimer(EasterEvent.instance, 0L, 20L*5);
    }
}