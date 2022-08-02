package xyz.dysaido.squad;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.api.Squad;
import xyz.dysaido.squad.api.command.CommandManager;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.commands.SquadCommand;
import xyz.dysaido.squad.team.TeamManagerImpl;
import xyz.dysaido.squad.user.UserManagerImpl;
import xyz.dysaido.squad.util.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public final class SimpleSquad extends JavaPlugin implements Squad {

    private YamlBuilder dataYaml;
    private TeamManagerImpl teamManager;
    private CommandManager commandManager;

    private PlaceholderAPIHook placeholderApiHook;

    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vaultHook = new VaultHook(this);
        } else {
            Logger.warning("JavaPlugin", "Vault is not found");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderApiHook = new PlaceholderAPIHook(this);
            placeholderApiHook.register();
        } else {
            Logger.warning("JavaPlugin", "PlaceholderAPI is not found");
        }

        getConfig().options().copyDefaults(true);
        saveConfig();
        dataYaml = new YamlBuilder(this, "squads");

        UserManagerImpl.getInstance().enable();
        teamManager = new TeamManagerImpl();
        teamManager.loadFromFile();
        commandManager = new CommandManager(this);
        commandManager.register("simplesquad", new SquadCommand(this));

    }

    /**
     * void t() {
     *     Stream<Team> topKills = teamManager.getTeams().stream().sorted((first, second) -> Integer.compare(second.getKills(), first.getKills()));
     *     Stream<Team> topDeaths = teamManager.getTeams().stream().sorted((first, second) -> Integer.compare(second.getDeaths(), first.getDeaths()));
     *     Stream<Team> topMoney = teamManager.getTeams().stream().sorted((first, second) -> Double.compare(second.getMoney(), first.getMoney()));
     * }
     */


    @Override
    public void reload() {
        dataYaml.reloadFile();
        commandManager.unregisterAll();
        commandManager.register("simplesquad", new SquadCommand(this));
    }

    @Override
    public void onDisable() {
        dataYaml.saveFile();
        commandManager.unregisterAll();
        UserManagerImpl.getInstance().disable();
    }

    @Override
    public SimpleCommandMap getCommandMap() {
        Field field = Reflection.getField(getServer().getClass(), SimpleCommandMap.class);
        return Reflection.fetch(getServer(), field);
    }

    @Override
    public Map<String, Command> getKnownCommands(SimpleCommandMap commandMap) {
        Field knownCommandsField = Reflection.getField(SimpleCommandMap.class, Map.class);
        return Reflection.fetch(commandMap, knownCommandsField);
    }

    @Override
    public YamlBuilder getDataYaml() {
        return dataYaml;
    }

    @Override
    public TeamManager getTeamManager() {
        return teamManager;
    }

    @Override
    public UserManager getUserManager() {
        return UserManagerImpl.getInstance();
    }

    public Optional<VaultHook> getVaultHook() {
        return Optional.ofNullable(vaultHook);
    }

    public Optional<PlaceholderAPIHook> getPlaceholderAPIHook() {
        return Optional.ofNullable(placeholderApiHook);
    }
}
