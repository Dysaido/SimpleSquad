package xyz.dysaido.squad.api;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import xyz.dysaido.squad.team.TeamManagerImpl;
import xyz.dysaido.squad.util.YamlBuilder;

public interface Squad extends Plugin {

    SimpleCommandMap getCommandMap();

    YamlBuilder getDataYaml();

    TeamManagerImpl getTeamManager();
}
