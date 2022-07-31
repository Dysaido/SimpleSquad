package xyz.dysaido.squad;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.api.Squad;
import xyz.dysaido.squad.api.command.CommandManager;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.commands.SquadCommand;
import xyz.dysaido.squad.team.TeamManagerImpl;
import xyz.dysaido.squad.user.UserManagerImpl;
import xyz.dysaido.squad.util.PlaceholderApiHook;
import xyz.dysaido.squad.util.Reflection;
import xyz.dysaido.squad.util.VaultHook;
import xyz.dysaido.squad.util.YamlBuilder;

import java.lang.reflect.Field;
import java.util.Optional;

public final class SimpleSquad extends JavaPlugin implements Squad {

    private YamlBuilder dataYaml;
    private TeamManagerImpl teamManager;
    private CommandManager commandManager;

    private PlaceholderApiHook placeholderApiHook;

    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vaultHook = new VaultHook();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderApi")) {
            placeholderApiHook = new PlaceholderApiHook();
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

    public Optional<PlaceholderApiHook> getPlaceholderApiHook() {
        return Optional.ofNullable(placeholderApiHook);
    }
}
