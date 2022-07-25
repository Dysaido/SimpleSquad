package xyz.dysaido.squad.commands;

import org.bukkit.command.CommandSender;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.command.BaseCommand;
import xyz.dysaido.squad.config.DefaultYaml;

public class SquadCommand extends BaseCommand {

    public SquadCommand(SimpleSquad plugin) {
        super(plugin, DefaultYaml.COMMAND_NAME);
        setAliases(DefaultYaml.COMMAND_ALIASES);
    }

    /**
     * Player Commands
     * Clan chat
     * create
     * delete
     * kick
     * leave
     * appoint
     * leaderboard
     * invite (accept/deny)
     * profile/about
     * bank
     * gui
     * host
     */

    @Override
    public void handle(CommandSender sender, String label, String[] args) {

    }

}