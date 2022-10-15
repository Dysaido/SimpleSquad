package xyz.dysaido.squad.config;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;

import java.util.List;

public class DefaultYaml {
    private static final Plugin PLUGIN = JavaPlugin.getPlugin(SimpleSquad.class);

    public static boolean DEBUG = PLUGIN.getConfig().getBoolean("debug");

    public static int INVITE_TIME = PLUGIN.getConfig().getInt("invite-time");

    public static boolean CHAT_PREFIX = PLUGIN.getConfig().getBoolean("chat-prefix.enable");

    public static String CHAT_PREFIX_FORMAT = PLUGIN.getConfig().getString("chat-prefix.format");

    public static String COMMAND_NAME = PLUGIN.getConfig().getString("command.name");

    public static List<String> COMMAND_ALIASES = PLUGIN.getConfig().getStringList("command.aliases");

    public static String COMMAND_PERMISSION = PLUGIN.getConfig().getString("command.permission");

}
