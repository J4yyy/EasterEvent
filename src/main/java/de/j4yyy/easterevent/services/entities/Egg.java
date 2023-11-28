package de.j4yyy.easterevent.services.entities;

import org.bukkit.Location;

public class Egg {
    private final int db_id;
    private final Location pos;
    private final int reward;

    public Egg(int db_id, Location pos, int reward) {
        this.db_id = db_id;
        this.pos = pos;
        this.reward = reward;
    }

    public int getDb_id() {
        return this.db_id;
    }

    public Location getPos() {
        return this.pos;
    }

    public int getReward() {
        return this.reward;
    }
}