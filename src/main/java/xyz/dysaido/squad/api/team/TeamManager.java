package xyz.dysaido.squad.api.team;

import org.bukkit.entity.Player;
import xyz.dysaido.squad.api.user.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TeamManager {

    boolean createTeam(String name, User leader, String initial);

    Team get(UUID id);

    boolean contains(UUID id);

    UUID generateId(String name);

    boolean removeTeam(UUID id);

    Optional<Team> findTeamById(UUID id);

    boolean hasInvited(User user);

    Collection<Team> getTeams();
}
