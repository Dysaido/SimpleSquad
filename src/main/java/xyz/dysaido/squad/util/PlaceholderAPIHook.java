package xyz.dysaido.squad.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.rmi.runtime.Log;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;

import java.util.Optional;

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
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Optional<Team> optionalTeam = plugin.getTeamManager().findTeamById(player.getUniqueId());
        Logger.debug("PAPI", params);
        if (optionalTeam.isPresent()) {
            if (params.startsWith("player_squad_")) {
                Team team = optionalTeam.get();
                switch (params) {
                    case "player_squad_initial":
                        return team.getInitial();
                    case "player_squad_name":
                        return team.getName();
                    case "player_squad_kills":
                        return String.valueOf(team.getKills());
                    case "player_squad_deaths":
                        return String.valueOf(team.getDeaths());
                    case "player_squad_balance":
                        return String.valueOf(team.getMoney());
                    default:
                        return null;
                }
            }
        }
        // TODO: Get by sorted kills, deaths and balance
        return null;
    }
}
