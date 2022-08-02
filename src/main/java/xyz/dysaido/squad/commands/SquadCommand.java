package xyz.dysaido.squad.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.command.BaseCommand;
import xyz.dysaido.squad.config.DefaultYaml;

import java.util.Arrays;

public class SquadCommand extends BaseCommand {

    public SquadCommand(SimpleSquad plugin) {
        super(plugin, DefaultYaml.COMMAND_NAME);
        setAliases(DefaultYaml.COMMAND_ALIASES);
        setPermission(DefaultYaml.COMMAND_PERMISSION);
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
     * pvpprotection - enable|disable
     * second leader
     * <p>
     * Admin Commands
     * Reset
     * Delete
     * Edit(Maybe)
     *
     */

    @Override
    public void handle(CommandSender sender, String label, String[] args) {
        if (DefaultYaml.DEBUG) {
            System.out.println(sender.getName());
            System.out.println(label);
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + "# " + args[i]);
            }
        }
    }

    private void onChat(CommandSender sender, String[] args) {

    }

    private void onCreate(CommandSender sender, String[] args) {

    }
    private void onDelete(CommandSender sender, String[] args) {

    }
    private void onKick(CommandSender sender, String[] args) {

    }

    private void onLeave(Player player, String[] args) {

    }

    private void onAppoint(Player player, String[] args) {

    }
    private void onLeaderboard(CommandSender sender, String[] args) {

    }

    private void onInvite(CommandSender sender, String[] args) {

    }

    private void onProfile(CommandSender sender, String[] args) {

    }

    private void onBank(Player player, String[] args) {

    }

    private void onHost(CommandSender sender, String[] args) {

    }
}