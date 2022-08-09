package xyz.dysaido.squad.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.command.BaseCommand;
import xyz.dysaido.squad.config.DefaultYaml;

public class SquadCommand extends BaseCommand {

    public SquadCommand(SimpleSquad plugin) {
        super(plugin, DefaultYaml.COMMAND_NAME);
        setAliases(DefaultYaml.COMMAND_ALIASES);
        setPermission(DefaultYaml.COMMAND_PERMISSION);
    }

    /**
     * Player Commands
     * gui*
     *
     * <p>
     * Admin Commands
     * Reset
     * Delete
     * Edit(Maybe)
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
        if (args.length >= 1) {
            String alias = args[0];
            Player player = (Player) sender;
            if (alias.equalsIgnoreCase("create")) {
                onCreate(player, args);
            } else if (alias.equalsIgnoreCase("delete")) {
                onDelete(player, args);
            } else if (alias.equalsIgnoreCase("kick")) {
                onKick(player, args);
            } else if (alias.equalsIgnoreCase("leave")) {
                onLeave(player, args);
            } else if (alias.equalsIgnoreCase("appoint")) {
                onAppoint(player, args);
            } else if (alias.equalsIgnoreCase("leaderboard")) {
                onLeaderboard(player, args);
            } else if (alias.equalsIgnoreCase("invite")) {
                onInvite(player, args);
            } else if (alias.equalsIgnoreCase("accept")) {
                onInviteAccept(player, args);
            } else if (alias.equalsIgnoreCase("deny")) {
                onInviteDeny(player, args);
            } else if (alias.equalsIgnoreCase("profile")) {
                onProfile(player, args);
            } else if (alias.equalsIgnoreCase("withdraw")) {
                onBankWithdraw(player, args);
            } else if (alias.equalsIgnoreCase("deposit")) {
                onBankDeposit(player, args);
            } else if (alias.equalsIgnoreCase("damage")) {
                onDamage(player, args);
            } else if (alias.equalsIgnoreCase("host")) {
            } else if (alias.equalsIgnoreCase("chat")) {
            } else if (alias.equalsIgnoreCase("deputy")) {
            }
        } else {
            sender.sendMessage("TODO: Help message");
        }

    }

    private void onChat(Player player, String[] args) {

    }

    private void onCreate(Player player, String[] args) {
        if (args.length == 3) {
            String alias = args[0];
            String name = args[1];
            String initial = args[2];

        } else {
            player.sendMessage("/clan create <name> <initial>");
        }
    }

    private void onDelete(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String name = args[1];
        } else {
            player.sendMessage("/clan delete <name>");
        }
    }

    private void onKick(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String member = args[1];
        } else {
            player.sendMessage("/clan kick <member>");
        }
    }

    private void onLeave(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String name = args[1];
        } else {
            player.sendMessage("/clan leave <name>");
        }
    }

    private void onAppoint(Player player, String[] args) {
        if (args.length == 3) {
            String alias = args[0];
            String name = args[1];
            String member = args[2];
        } else {
            player.sendMessage("/clan appoint <name> <member>");
        }
    }

    private void onLeaderboard(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String type = args[1];
        } else {
            player.sendMessage("/clan leaderboard <kill|death|money>");
        }
    }

    private void onInvite(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String member = args[1];
        } else {
            player.sendMessage("/clan invite <member>");
        }
    }

    private void onInviteAccept(Player player, String[] args) {
        if (args.length == 1) {
            String alias = args[0];
        } else {
            player.sendMessage("/clan accept");
        }
    }

    private void onInviteDeny(Player player, String[] args) {
        if (args.length == 1) {
            String alias = args[0];
        } else {
            player.sendMessage("/clan deny");
        }
    }

    private void onProfile(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String inspected = args[1];
        } else {
            player.sendMessage("/clan profile <player>");
        }
    }

    private void onBankWithdraw(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String amount = args[1];
        } else {
            player.sendMessage("/clan withdraw <amount>");
        }
    }

    private void onBankDeposit(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String amount = args[1];
        } else {
            player.sendMessage("/clan deposit <amount>");
        }
    }

    private void onDamage(Player player, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String bool = args[1];
        } else {
            player.sendMessage("/clan damage <true|false>");
        }
    }
}