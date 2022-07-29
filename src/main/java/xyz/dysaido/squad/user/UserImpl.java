package xyz.dysaido.squad.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamInvite;
import xyz.dysaido.squad.api.user.User;

import java.util.Optional;
import java.util.UUID;

public class UserImpl implements User {

    private final UUID id;
    private Team team;
    private TeamInvite invite;

    public UserImpl(UUID id) {
        this.id = id;
    }

    @Override
    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public Optional<Team> getTeam() {
        return Optional.ofNullable(team);
    }

    @Override
    public void setInvite(TeamInvite invite) {
        this.invite = invite;
    }

    @Override
    public Optional<TeamInvite> getInvite() {
        return Optional.ofNullable(invite);
    }

    @Override
    public boolean isSimilar(User user) {
        if (user.getTeam().isPresent()) {
            return user.getTeam().get().equals(team);
        }
        return false;
    }

    @Override
    public boolean isLeader() {
        Player player = Bukkit.getPlayer(id);
        return team != null && player != null && team.isLeader(player);
    }

    @Override
    public UUID getId() {
        return id;
    }
}
