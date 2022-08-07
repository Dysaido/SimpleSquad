package xyz.dysaido.squad.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Format {

    public static final String LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------";
    public static final String LINE_WITH_16_CHARS = "                ";
    public static final String LINE_WITH_24_CHARS = "                        ";
    public static final String LINE_WITH_32_CHARS = "                                ";

    public static <T> Optional<T> ifElse(Optional<T> optional, Consumer<T> present, Runnable elseAction) {
        if (optional.isPresent()) {
            present.accept(optional.get());
        } else {
            elseAction.run();
        }

        return optional;
    }

    public static void hookWarningMessage(String tag, String hookedPlugin, String method) {
        Logger.warning(tag, String.format("%s is not implemented on this server. The plugin cannot execute this method (%s) without %s.", hookedPlugin, method, hookedPlugin));
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
