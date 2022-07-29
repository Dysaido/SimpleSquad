package xyz.dysaido.squad.api.team;

import org.bukkit.entity.Player;
import xyz.dysaido.squad.api.user.User;

import java.util.Collection;
import java.util.UUID;

public interface TeamManager {

    void createTeam(String name, String leader, String initial);

    Team get(UUID id);

    boolean contains(UUID id);

    void removeTeam(UUID id);

    Team findTeamByPlayer(Player player);

    boolean hasInvited(User user);

    Collection<Team> getTeams();
}
