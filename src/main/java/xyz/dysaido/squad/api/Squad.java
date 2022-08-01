package xyz.dysaido.squad.api;

import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.util.YamlBuilder;

import java.util.Map;

public interface Squad extends Plugin {

    void reload();

    SimpleCommandMap getCommandMap();

    Map<String, Command> getKnownCommands(SimpleCommandMap commandMap);

    YamlBuilder getDataYaml();

    TeamManager getTeamManager();

    UserManager getUserManager();
}
