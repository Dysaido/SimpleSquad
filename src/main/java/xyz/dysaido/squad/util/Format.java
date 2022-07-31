package xyz.dysaido.squad.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Format {

    public static final String LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------";
    public static final String LINE_WITH_16_CHARS = "                ";
    public static final String LINE_WITH_24_CHARS = "                        ";
    public static final String LINE_WITH_32_CHARS = "                                ";

    public static int floor(float value) {
        int i = (int)value;
        return value < (float)i ? i - 1 : i;
    }

    public static int floor(double value) {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }

    public static int ceil(float value) {
        int i = (int)value;
        return value > (float)i ? i + 1 : i;
    }

    public static int ceil(double value) {
        int i = (int)value;
        return value > (double)i ? i + 1 : i;
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static String colored(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void broadcast(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colored(text)));
    }

    public static void broadcast(List<String> texts) {
        texts.stream().map(Format::colored).forEach(Format::broadcast);
    }

    public static void broadcastClickable(String text, String hoverText, String command) {
        ComponentBuilder builder = translate(text);
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(colored(hoverText))));
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(builder.create());
        }
    }

    public static ComponentBuilder translate(String... strings) {
        ComponentBuilder componentBuilder = new ComponentBuilder("");

        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                componentBuilder.append("\n");
            }
            componentBuilder.append(colored(strings[i]));
        }
        return componentBuilder;
    }
}
