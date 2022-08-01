package xyz.dysaido.squad.api.user;

import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamInvite;

import java.util.Optional;
import java.util.UUID;

public interface User {

    void setTeam(Team team);

    Optional<Team> getTeam();

    void setInvite(TeamInvite invite);

    Optional<TeamInvite> getInvite();

    boolean isSimilar(User user);

    boolean isLeader();

    UUID getId();

    void withdraw(double amount);

    void deposit(double amount);

    double getBalance();
}
