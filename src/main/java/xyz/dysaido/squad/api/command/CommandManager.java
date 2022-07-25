package xyz.dysaido.squad.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import xyz.dysaido.squad.api.Squad;
import xyz.dysaido.squad.util.Reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CommandManager {

    private final Map<String, Command> knownCommands = new HashMap<>();
    private final CommandMap commandMap;

    public CommandManager(Squad plugin) {
        this.commandMap = plugin.getCommandMap();
    }

    public void register(String prefix, Command command) {
        this.knownCommands.put(command.getName(), command);
        this.commandMap.register(prefix, command);
    }

    public void unregisterAll() {
        Field knownCommandsField = Reflection.getField(SimpleCommandMap.class, Map.class);
        Map<String, Command> knownCommands = Reflection.fetch(this.commandMap, knownCommandsField);
        knownCommands.values().removeAll(this.knownCommands.values());
        this.knownCommands.values().forEach(command -> command.unregister(this.commandMap));
        this.knownCommands.clear();
    }

    public void unregister(Command command) {
        Field knownCommandsField = Reflection.getField(SimpleCommandMap.class, Map.class);
        Map<String, Command> knownCommands = Reflection.fetch(this.commandMap, knownCommandsField);
        knownCommands.remove(command.getName()).unregister(this.commandMap);
    }

    public Stream<Command> stream() {
        return knownCommands.values().stream();
    }
}
