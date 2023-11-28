package de.j4yyy.easterevent.utils;

import de.j4yyy.easterevent.EasterEvent;
import de.j4yyy.easterevent.services.entities.Egg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class DBConnection {

    private Connection connection;

    public DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            this.connection = DriverManager.getConnection(getConnectionString(), EasterEvent.instance.getMainConfig().toFileConfiguration().get("DB.CONNECTION.USER")+"", EasterEvent.instance.getMainConfig().toFileConfiguration().get("DB.CONNECTION.PASSWORD")+"");
            setUp();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private String getConnectionString() {

        return "jdbc:mysql://" +
                EasterEvent.instance.getMainConfig().toFileConfiguration().get("DB.CONNECTION.HOST") +
                ":" +
                EasterEvent.instance.getMainConfig().toFileConfiguration().get("DB.CONNECTION.PORT") +
                "/" +
                EasterEvent.instance.getMainConfig().toFileConfiguration().get("DB.CONNECTION.DB");
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void setUp() {
        // Egg Table
        try {
            Statement stmt = this.connection.createStatement();
            if(!stmt.execute("CREATE TABLE IF NOT EXISTS eggs (id INT AUTO_INCREMENT PRIMARY KEY, world VARCHAR(50), x_cord DOUBLE, y_cord DOUBLE, z_cord DOUBLE, reward INT DEFAULT 1);")) {
                Bukkit.getLogger().log(Level.WARNING, "Eggs Table - Was not created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Claimed Table
        try {
            Statement stmt = this.connection.createStatement();
            if(!stmt.execute("CREATE TABLE IF NOT EXISTS claimed (id INT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(120), egg_id INT, FOREIGN KEY (egg_id) REFERENCES eggs(id));")) {
                Bukkit.getLogger().log(Level.WARNING, "Claimed Table - Was not created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Balance Table
        try {
            Statement stmt = this.connection.createStatement();
            if(!stmt.execute("CREATE TABLE IF NOT EXISTS balance (uuid VARCHAR(120) PRIMARY KEY, amount INT);")) {
                Bukkit.getLogger().log(Level.WARNING, "Balance Table - Was not created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Egg> getAllEggs() {
        try {
            Statement stmt = this.connection.createStatement();
            if(stmt.execute("SELECT * FROM eggs;")) {
                ArrayList<Egg> eggs = new ArrayList<>();
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
                    eggs.add(new Egg(rs.getInt(1), new Location(Bukkit.getWorld(rs.getString(2)), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)), rs.getInt((6))));
                }
                return eggs;
            } else {
                return new ArrayList<Egg>();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Egg createEgg(Block block) {
        try {
            PreparedStatement pstm = this.connection.prepareStatement("INSERT INTO eggs(world, x_cord, y_cord, z_cord) VALUES (?, ?, ?, ?)");
            pstm.setString(1, block.getWorld().getName());
            pstm.setDouble(2, block.getX());
            pstm.setDouble(3, block.getY());
            pstm.setDouble(4, block.getZ());
            pstm.executeUpdate();

            Statement stmt = this.connection.createStatement();
            if(stmt.execute("SELECT * FROM eggs WHERE id=(SELECT max(id) FROM eggs);")) {
                ResultSet rs = stmt.getResultSet();
                if (rs.next()) {
                    return new Egg(rs.getInt(1), new Location(Bukkit.getWorld(rs.getString(2)), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)), rs.getInt(6));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeEgg(Egg egg) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("DELETE FROM claimed WHERE egg_id=?");
            stmt.setInt(1, egg.getDb_id());
            stmt.executeUpdate();
            stmt = this.connection.prepareStatement("DELETE FROM eggs WHERE id=?");
            stmt.setInt(1, egg.getDb_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClaim(Egg egg) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("DELETE FROM claimed WHERE egg_id=?");
            stmt.setInt(1, egg.getDb_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Egg> getPlayerClaims(UUID uuid) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT c.uuid, e.id, e.world, e.x_cord, e.y_cord, e.z_cord, e.reward FROM claimed as c LEFT JOIN eggs as e ON c.egg_id=e.id WHERE c.uuid=?;");
            ps.setString(1, uuid.toString());
            if(ps.execute()) {
                ArrayList<Egg> eggs = new ArrayList<>();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    eggs.add(new Egg(rs.getInt(2), new Location(Bukkit.getWorld(rs.getString(3)), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)), rs.getInt((7))));
                }
                return eggs;
            } else {
                return new ArrayList<Egg>();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPlayerClaim(UUID uuid, Egg egg) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("INSERT INTO claimed(uuid, egg_id) VALUES (?, ?);");
            ps.setString(1, uuid.toString());
            ps.setInt(2, egg.getDb_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initPlayerBalance(UUID uuid) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("INSERT INTO balance(uuid, amount) VALUES (?, 1) ON DUPLICATE KEY UPDATE amount=amount;");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerBalance(UUID uuid, int balance) {
        initPlayerBalance(uuid);
        try {
            PreparedStatement ps = this.connection.prepareStatement("UPDATE balance SET amount=amount+? WHERE uuid=?;");
            ps.setInt(1, balance);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePlayerBalance(UUID uuid, int balance) {
        initPlayerBalance(uuid);
        try {
            PreparedStatement ps = this.connection.prepareStatement("UPDATE balance SET amount=amount-? WHERE uuid=?;");
            ps.setInt(1, balance);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerBalance(UUID uuid, int balance) {
        initPlayerBalance(uuid);
        try {
            PreparedStatement ps = this.connection.prepareStatement("UPDATE balance SET amount=? WHERE uuid=?;");
            ps.setInt(1, balance);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerEasterBalance(UUID uuid) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT amount FROM balance WHERE uuid=?;");
            ps.setString(1, uuid.toString());
            if(ps.execute()) {
                ResultSet rs = ps.getResultSet();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void resetBalance() {
        try {
            PreparedStatement ps = this.connection.prepareStatement("UPDATE balance SET amount=0;");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}