package de.j4yyy.easterevent.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private FileConfiguration fileConfiguration;
    private File file;

    public Config(String name, File path) {
        boolean initialSetup = false;
        this.file = new File(path, name);

        if(!this.file.exists()) {
            initialSetup = true;
            path.mkdirs();
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.fileConfiguration = new YamlConfiguration();

        try {
            this.fileConfiguration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if(initialSetup) {
            this.fileConfiguration.set("DB.CONNECTION.HOST", "CHANGE_ME");
            this.fileConfiguration.set("DB.CONNECTION.PORT", 3306);
            this.fileConfiguration.set("DB.CONNECTION.USER", "CHANGE_ME");
            this.fileConfiguration.set("DB.CONNECTION.PASSWORD", "CHANGE_ME");
            this.fileConfiguration.set("DB.CONNECTION.DB", "CHANGE_ME");
            this.fileConfiguration.set("DB.PREFIX", "easter");
            save();
        }

    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration toFileConfiguration() {
        return this.fileConfiguration;
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            this.fileConfiguration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}