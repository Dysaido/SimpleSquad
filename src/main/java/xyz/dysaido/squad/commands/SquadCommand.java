package xyz.dysaido.squad.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.command.BaseCommand;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamInvite;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.config.DefaultYaml;
import xyz.dysaido.squad.user.UserManagerImpl;
import xyz.dysaido.squad.util.Format;
import xyz.dysaido.squad.util.NumericParser;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class SquadCommand extends BaseCommand {
    private final UserManager userManager = UserManagerImpl.getInstance();
    private final TeamManager teamManager = plugin.getTeamManager();

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
            String playerName = player.getName();
            UUID playerId = player.getUniqueId();
            User user = userManager.get(playerId).orElseGet(() -> plugin.getUserManager().add(playerId, playerName));

            if (alias.equalsIgnoreCase("create")) {
                onCreate(user, args);
            } else if (alias.equalsIgnoreCase("delete")) {
                onDelete(user, args);
            } else if (alias.equalsIgnoreCase("kick")) {
                onKick(user, args);
            } else if (alias.equalsIgnoreCase("leave")) {
                onLeave(user, args);
            } else if (alias.equalsIgnoreCase("appoint")) {
                onAppoint(user, args);
            } else if (alias.equalsIgnoreCase("leaderboard")) {
                onLeaderboard(player, args);
            } else if (alias.equalsIgnoreCase("invite")) {
                onInvite(user, args);
            } else if (alias.equalsIgnoreCase("accept")) {
                onInviteAccept(user, args);
            } else if (alias.equalsIgnoreCase("deny")) {
                onInviteDeny(user, args);
            } else if (alias.equalsIgnoreCase("profile")) {
                myClan(user, args);
            } else if (alias.equalsIgnoreCase("withdraw")) {
                onBankWithdraw(user, args);
            } else if (alias.equalsIgnoreCase("deposit")) {
                onBankDeposit(user, args);
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

    private void onCreate(User user, String[] args) {
        if (args.length == 3) {
            String alias = args[0];
            String name = args[1];
            String initial = args[2];
            if (user.getTeam().isPresent()) {
                if (user.getTeam().get().isLeader(user)) {
                    user.sendMessage("&cNeked már van klánod, tulaj vagy!");
                } else {
                    user.sendMessage("&cLépj ki a klánodból, ha szeretnél saját klánt!");
                }
            } else if (args[1].length() < 3 || args[0].length() > 8) {
                user.sendMessage("&cLegalább 3, maximum 8 karaktert tartalmazhat a név!");
            } else if (args[2].length() > 8) {
                user.sendMessage("&cMaximum 8 karaktert tartalmazhat a prefix!");
            } else {
                boolean isLetterName = args[1].chars().allMatch(Character::isLetter);
                boolean isLetterLogo = args[2].chars().allMatch(Character::isLetter);
                if (isLetterName && isLetterLogo) {
                    if (plugin.getTeamManager().createTeam(name, user, initial)) {
                        user.sendMessage("&aSikeresen létrehoztad a klánod!");
                    } else {
                        user.sendMessage("&cA klán már létezik!");
                    }

                } else {
                    user.sendMessage("&cCsak betűből állhatnak a karakterek!");
                }
            }
        } else {
            user.sendMessage("/clan create <name> <initial>");
        }
    }

    private void onDelete(User user, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String name = args[1];
            if (!user.isLeader()) {
                user.sendMessage("&cCsak a klánvezető tudja törölni a klánt!");
                return;
            }
            TeamManager teamManager = plugin.getTeamManager();
            if (teamManager.removeTeam(teamManager.generateId(name))) {
                user.sendMessage("&aSikeresen törölted a klánod.");
            } else {
                user.sendMessage("&cHelytelenul irtad le a squad nevet.");
            }
        } else {
            user.sendMessage("/clan delete <name>");
        }
    }

    private void onKick(User sender, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String member = args[1];
            if (!sender.isAuthorized()) {
                sender.sendMessage("&cNem vagy felhatalmazva ehhez a muvelethez!");
                return;
            }
            Team team = sender.getTeam().get();
            User target = team.findDataByName(member);
            if (target == null) {
                sender.sendMessage("Nincs klanodban ilyen jatekos");
                return;
            }
            if (sender.equals(target)) {
                sender.sendMessage("Nem rughatod ki magad");
            } else {
                team.kick(member);
                sender.sendMessage("&aSikeresen kirúgtad &7" + target.getName() + "&a-t a klánodból.");
                target.sendMessage("&cKi lettél rúgva &7" + team.getName() + " csapatbol.");
            }
        } else {
            sender.sendMessage("/clan kick <member>");
        }
    }

    private void onLeave(User user, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            String name = args[1];
            if (user.getTeam().isPresent()) {
                Team team = user.getTeam().get();
                if (user.isLeader()) {
                    user.sendMessage("&aTe vagy a klánvezető, nem hagyhatod el a klánod.");
                } else {
                    if (name.equals(team.getName())) {
                        team.kick(user.getName());
                        user.sendMessage("&aSikeresen elhagytad a klánod.");
                    } else {
                        user.sendMessage("&aKerlek ird be a klanod nevet is.");
                        user.sendMessage("/clan leave <name>");
                    }
                }
            } else {
                user.sendMessage("&cNem tartozol egyik klánhoz sem, így nem tudod elhagyni a semmit. :)");
            }
        } else {
            user.sendMessage("/clan leave <name>");
        }
    }

    private void onAppoint(User sender, String[] args) {
        if (args.length == 3) {
            String alias = args[0];
            String name = args[1];
            String member = args[2];
            if (!sender.isLeader()) {
                sender.sendMessage("&cNem vagy tulajdonos!");
                return;
            }
            Team team = sender.getTeam().get();
            if (!team.getId().equals(teamManager.generateId(name))) {
                sender.sendMessage("&aKerlek ird be a klanod nevet is.");
                sender.sendMessage("/clan appoint <name> <member>");
                return;
            }
            User target = team.findDataByName(member);
            if (target == null) {
                sender.sendMessage("&eA felhasználó nem nevezheted ki, mert nem a te klánodban van!");
                return;
            }
            if (sender.equals(target)) {
                sender.sendMessage("Nem nevezheted ki magad mikozben te vagy a vezeto");
            } else {
                team.setLeader(target);
                sender.sendMessage(String.format("&aSikeresen kinevezted &7%s&a-t!", target.getName()));
                target.sendMessage(String.format("&aTe lettél a klánod vezetője &7%s&a által!", sender.getName()));
            }
        } else {
            sender.sendMessage("/clan appoint <name> <member>");
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

    private void onInvite(User sender, String[] args) {
        int maxMember = 5;
        if (args.length == 2) {
            String alias = args[0];
            String member = args[1];
            if (!sender.isAuthorized()) {
                sender.sendMessage("&cNem vagy felhatalmazva ehhez a muvelethez!");
                return;
            }
            Player targetPlayer = Bukkit.getPlayer(member);
            if (targetPlayer == null) {
                sender.sendMessage("Target is null");
                return;
            }
            User target = userManager.get(targetPlayer.getUniqueId())
                    .orElseGet(() -> userManager.add(targetPlayer.getUniqueId(), targetPlayer.getName()));
            if (sender.equals(target)) {
                sender.sendMessage("Nem hivhatod meg magad");
            } else {
                Team team = sender.getTeam().get();
                if (sender.isSame(target)) {
                    sender.sendMessage("&cA saját klántársad nem hívhatod meg ismét.");
                    return;
                }
                if (target.getTeam().isPresent()) {
                    sender.sendMessage("&cNem hívhatod meg a játékost, amíg klánban van.");
                    return;
                }
                if (teamManager.hasInvited(target)) {
                    sender.sendMessage("&eMár meghívta egy másik klán a felhasználót.");
                    return;
                }
                if (team.getMembers().count() <= maxMember) {
                    sender.sendMessage("&c" + targetPlayer.getName() + "&7-t meghívtad.");
                    target.sendMessage("&c" + sender.getName() + "&7 által meg lettél hívva &c" + team.getName() + "&7-ba/be. &a/clan accept/deny");
                    target.setInvite(new TeamInvite(sender, target, DefaultYaml.INVITE_TIME));
                } else {
                    sender.sendMessage(String.format("&cElérted a maximális klántagok számát! &7(%d)", maxMember));
                }
            }
        } else {
            sender.sendMessage("/clan invite <member>");
        }
    }

    private void onInviteAccept(User user, String[] args) {
        if (args.length == 1) {
            String alias = args[0];
            Optional<TeamInvite> optionalInvite = user.getInvite();
            if (optionalInvite.isPresent()) {
                TeamInvite teamInvite = optionalInvite.get();
                User sender = teamInvite.getSender();
                User target = teamInvite.getTarget();
                if (teamInvite.isExpired()) {
                    target.sendMessage("&cLejárt a meghívód, próbálj meg újat kérni.");
                } else {
                    sender.getTeam().ifPresent(team -> {
                        team.join(target);
                        sender.sendMessage("&c" + target.getName() + " &7elfogadta a megívásodat.");
                        target.sendMessage("&c" + sender.getName() + " &7megívását elfogadtad.");
                    });
                    teamInvite.doLeft();
                }
            } else {
                user.sendMessage(Format.colored("&cNincs klán meghívásod."));
            }
        } else {
            user.sendMessage("/clan accept");
        }
    }

    private void onInviteDeny(User user, String[] args) {
        if (args.length == 1) {
            String alias = args[0];
            Optional<TeamInvite> optionalInvite = user.getInvite();
            if (optionalInvite.isPresent()) {
                TeamInvite teamInvite = optionalInvite.get();
                User sender = teamInvite.getSender();
                User target = teamInvite.getTarget();
                if (teamInvite.isExpired()) {
                    target.sendMessage("&cLejárt a meghívód, már nem kell visszautasítanod. :)");
                } else {
                    sender.getTeam().ifPresent(clan -> {
                        sender.sendMessage("&c" + target.getName() + " &7elutasította a meghívást.");
                        target.sendMessage("&c" + sender.getName() + " &7meghívástát elutasítottad.");
                    });
                    teamInvite.doLeft();
                }
            } else {
                user.sendMessage(Format.colored("&cNincs klán meghívásod."));
            }
        } else {
            user.sendMessage("/clan deny");
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

    private void myClan(User user, String[] args) {
        if (args.length == 1) {
            if (user.getTeam().isPresent()) {
                Team team = user.getTeam().get();
                user.sendMessage("&6Name&7: " + team.getName());
                user.sendMessage("&6Initial&7: " + team.getInitial());
                user.sendMessage("&6Leader&7: " + team.getLeader());
                user.sendMessage("&6Kills&7: " + team.getKills());
                user.sendMessage("&6Deaths&7: " + team.getDeaths());
                user.sendMessage("&6Money&7: " + team.getMoney());
                user.sendMessage("&6Members&7: " + team.getMembers());
                user.sendMessage("&6Deputies&7: " + team.getDeputies());
            } else {
                user.sendMessage("&cNem található klán!");
            }
        }

    }

    private void onBankWithdraw(User user, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            if (user.isLeader()) {
                Team team = user.getTeam().get();
                if (NumericParser.isDouble(args[1])) {
                    double amount = Double.parseDouble(args[1]);
                    if (team.getMoney() >= amount) {
                        team.withdraw(amount);
                        user.deposit(amount);
                        user.sendMessage("&aSikeres tranzakció");
                        user.sendMessage("&aKlánod pénze jelenleg: &e" + team.getMoney());
                    } else {
                        user.sendMessage("&cNincs elég pénze a klánodnak!");
                    }
                } else {
                    user.sendMessage("&cPénz összeget írj kérlek!");
                }
            } else {
                user.sendMessage("&cNem vagy klánvezető");
            }
        } else {
            user.sendMessage("/clan withdraw <amount>");
        }
    }

    private void onBankDeposit(User user, String[] args) {
        if (args.length == 2) {
            String alias = args[0];
            if (user.getTeam().isPresent()) {
                Team team = user.getTeam().get();
                if (NumericParser.isDouble(args[1])) {
                    double amount = Double.parseDouble(args[1]);
                    if (user.getBalance() >= amount) {
                        team.deposit(amount);
                        user.withdraw(amount);
                        user.sendMessage("&aSikeres tranzakció");
                        user.sendMessage("&aKlánod pénze jelenleg: &e" + team.getMoney());
                    } else {
                        user.sendMessage("&cNincs elég pénzed!");
                    }
                } else {
                    user.sendMessage("&cPénz összeget írj kérlek!");
                }
            } else {
                user.sendMessage(Format.colored("&cNincs klánod!"));
            }
        } else {
            user.sendMessage("/clan deposit <amount>");
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