package xyz.dysaido.squad.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.team.TeamImpl;

import java.util.List;
import java.util.Objects;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final SimpleSquad plugin;
    public PlaceholderAPIHook(SimpleSquad plugin) {
        this.plugin = plugin;
    }
    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.startsWith("player_team")) {
            Team team = SimpleSquad.getPlugin().getTeamManager().findTeamByPlayer(player);
            switch (params) {
                case "player_team_name":
                    return Objects.isNull(team) ? "-" : team.getName();
                case "player_team_kills":
                    return Objects.isNull(team) ? "-1" : String.valueOf(team.getKills());
                case "player_team_deaths":
                    return Objects.isNull(team) ? "-1" : String.valueOf(team.getDeaths());
                case "player_team_balance":
                    return Objects.isNull(team) ? "-1" : String.valueOf(team.getMoney());
                default:
                    return null;
            }
        }
        // TODO: Get by sorted kills, deaths and balance
        return null;
    }
}
