package xyz.dysaido.squad.api.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.Squad;
import xyz.dysaido.squad.util.Format;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand extends Command {
    protected final Squad plugin;

    public BaseCommand(SimpleSquad plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(MESSAGE.NO_PERMISSION.format());
            return false;
        }
        this.handle(sender, label, args);
        return false;
    }

    public abstract void handle(CommandSender sender, String label, String[] args);

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
        Player player = sender instanceof Player ? (Player) sender : null;
        ArrayList<String> list = new ArrayList<>();
        for (Player player1 : sender.getServer().getOnlinePlayers()) {
            String name = player1.getName();
            if (player == null || player.canSee(player1)) {
                if (args.length != 0) {
                    if (StringUtil.startsWithIgnoreCase(name, args[args.length - 1])) {
                        list.add(name);
                    }
                }
            }
        }
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public enum MESSAGE {
        NO_PERMISSION("&cYou don't have permission to perform this command!"),
        ONLY_CONSOLE("&cEntity don't authorized to perform this command!"),
        ONLY_PLAYER("&cConsole don't authorized to perform this command!");

        private final String msg;

        MESSAGE(String msg) {
            this.msg = msg;
        }

        public String format() {
            return Format.colored(msg);
        }

    }
}
