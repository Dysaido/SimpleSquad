package xyz.dysaido.squad.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import xyz.dysaido.squad.api.Squad;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CommandManager {

    private final Map<String, Command> knownCommands = new HashMap<>();
    private final Squad plugin;
    private final SimpleCommandMap commandMap;

    public CommandManager(Squad plugin) {
        this.plugin = plugin;
        this.commandMap = plugin.getCommandMap();
    }

    public void register(String prefix, Command command) {
        this.knownCommands.put(command.getName(), command);
        this.commandMap.register(prefix, command);
    }

    public void unregisterAll() {
        plugin.getKnownCommands(commandMap).values().removeAll(this.knownCommands.values());
        this.knownCommands.values().forEach(command -> command.unregister(this.commandMap));
        this.knownCommands.clear();
    }

    public void unregister(Command command) {
        plugin.getKnownCommands(commandMap).remove(command.getName()).unregister(this.commandMap);
    }

    public Stream<Command> stream() {
        return knownCommands.values().stream();
    }
}
