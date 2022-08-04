package xyz.dysaido.squad.leaderboard;

import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderBoard {

    public static List<Team> sortedByKills() {
        return SimpleSquad.getPlugin().getTeamManager().getTeams().stream()
                .sorted(Comparator.comparing(team -> ((Team) team).getKills()).reversed()).collect(Collectors.toList());
    }

    public static List<Team> sortedByDeaths() {
        return SimpleSquad.getPlugin().getTeamManager().getTeams().stream()
                .sorted(Comparator.comparing(team -> ((Team) team).getDeaths()).reversed()).collect(Collectors.toList());
    }

    public static List<Team> sortedByBalance() {
        return SimpleSquad.getPlugin().getTeamManager().getTeams().stream()
                .sorted(Comparator.comparing(team -> ((Team) team).getMoney()).reversed()).collect(Collectors.toList());
    }
}
