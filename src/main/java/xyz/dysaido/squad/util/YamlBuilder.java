package xyz.dysaido.squad.util;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class YamlBuilder {
    private final static String TAG = "YamlBuilder";
    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration configuration;

    public YamlBuilder(JavaPlugin plugin, String name) {
        this.plugin = Objects.requireNonNull(plugin);
        this.file = createFile(Objects.requireNonNull(name));
    }

    public YamlBuilder(JavaPlugin plugin, File file) {
        this.plugin = Objects.requireNonNull(plugin);
        this.file = Objects.requireNonNull(file);
    }

    private FileConfiguration createDataFile(File file) {
        Objects.requireNonNull(file);
        Logger.debug(TAG, "createDataFile");
        FileConfiguration ymlFormat = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                ymlFormat.save(file);
            } catch (IOException e) {
                Logger.error(TAG, e.getMessage());
            }
            return ymlFormat;
        }
        return ymlFormat;
    }

    private File createFile(String name) {
        Objects.requireNonNull(name);
        Logger.debug(TAG, "createFile");
        return new File(plugin.getDataFolder(), name + ".yml");
    }

    private void reloadFile(String filename) {
        Objects.requireNonNull(filename);
        Logger.debug(TAG, "reloadFile");
        final InputStream defConfigStream = getResource(filename);
        if (defConfigStream == null) return;
        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    private InputStream getResource(String filename) {
        Objects.requireNonNull(filename);
        Logger.debug(TAG, "getResource");
        try {
            URL url = plugin.getClass().getClassLoader().getResource(filename);
            if (url == null) return null;
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            Logger.warning(TAG, e.getMessage());
            return null;
        }
    }

    public FileConfiguration getFile() {
        Logger.debug(TAG, "getFile");
        if (configuration == null) {
            configuration = Objects.requireNonNull(createDataFile(file));
            reloadFile(file.getName());
        }
        return configuration;
    }

    public void reloadFile() {
        Logger.information(TAG, "reloadFile");
        configuration = YamlConfiguration.loadConfiguration(file);
        final InputStream defConfigStream = getResource(file.getName());
        if (defConfigStream == null) return;
        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public void saveFile() {
        Logger.debug(TAG, "saveFile");
        try {
            getFile().save(file);
        } catch (IOException e) {
            Logger.error(TAG, e.getMessage());
        }
    }
}
