package xyz.dysaido.squad.api;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.util.YamlBuilder;

public interface Squad extends Plugin {

    SimpleCommandMap getCommandMap();

    YamlBuilder getDataYaml();

    TeamManager getTeamManager();

    UserManager getUserManager();
}
