package xyz.dysaido.squad;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.api.Squad;
import xyz.dysaido.squad.api.command.CommandManager;
import xyz.dysaido.squad.commands.SquadCommand;
import xyz.dysaido.squad.team.TeamManagerImpl;
import xyz.dysaido.squad.util.Reflection;
import xyz.dysaido.squad.util.YamlBuilder;

import java.lang.reflect.Field;

public final class SimpleSquad extends JavaPlugin implements Squad {

    private YamlBuilder dataYaml;
    private TeamManagerImpl teamManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        dataYaml = new YamlBuilder(this, "squads");
        teamManager = new TeamManagerImpl();
        teamManager.loadFromFile();
        commandManager = new CommandManager(this);
        commandManager.register("simplesquad", new SquadCommand(this));
        getConfig().options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        dataYaml.reloadFile();
        commandManager.unregisterAll();
        commandManager.register("simplesquad", new SquadCommand(this));
    }

    @Override
    public void onDisable() {
        dataYaml.saveFile();
        commandManager.unregisterAll();
    }

    @Override
    public SimpleCommandMap getCommandMap() {
        Field field = Reflection.getField(getServer().getClass(), SimpleCommandMap.class);
        return Reflection.fetch(getServer(), field);
    }

    @Override
    public YamlBuilder getDataYaml() {
        return dataYaml;
    }

    @Override
    public TeamManagerImpl getTeamManager() {
        return teamManager;
    }
}
