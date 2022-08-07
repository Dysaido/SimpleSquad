package xyz.dysaido.squad.team;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamInvite;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.user.UserManagerImpl;

import java.util.*;

public class TeamManagerImpl implements TeamManager {

    private final Map<UUID, Team> teamMap = new HashMap<>();
    private final SimpleSquad plugin = JavaPlugin.getPlugin(SimpleSquad.class);
    private final FileConfiguration dataYaml;

    public TeamManagerImpl() {
        this.dataYaml = this.plugin.getDataYaml().getFile();
    }

    public void loadFromFile() {
        for (String id : dataYaml.getKeys(false)) {
            ConfigurationSection section = dataYaml.getConfigurationSection(id);
            UUID uuid = UUID.fromString(id);
            if (section == null || teamMap.containsKey(UUID.fromString(id))) continue;
            TeamImpl clan = new TeamImpl(uuid, plugin.getDataYaml());
            teamMap.put(uuid, clan);
        }

    }

    @Override
    public void createTeam(String name, String leader, String initial) {
        UUID id = UUID.nameUUIDFromBytes(("Squad:" + name).getBytes(Charsets.UTF_8));
        ConfigurationSection section = dataYaml.createSection(id.toString());
        section.set("name", name);
        section.set("initial", initial);
        section.set("leader", leader);
        section.set("kills", 0);
        section.set("deaths", 0);
        section.set("money", 0.00D);
        section.set("deputies", new ArrayList<>());
        section.set("members", Collections.singletonList(leader));
        plugin.getDataYaml().saveFile();
        teamMap.put(id, new TeamImpl(id, plugin.getDataYaml()));
    }

    @Override
    public Team get(UUID id) {
        return teamMap.get(id);
    }

    @Override
    public boolean contains(UUID id) {
        return teamMap.containsKey(id);
    }

    @Override
    public void removeTeam(UUID id) {
        Team team = teamMap.remove(id);
        team.getMembers().stream().map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(player -> UserManagerImpl.getInstance().get(player.getUniqueId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(user -> user.setTeam(null));
        dataYaml.set(id.toString(), null);
        plugin.getDataYaml().saveFile();
    }

    @Override
    public Team findTeamByPlayer(Player player) {
        return teamMap.values().stream().filter(clan -> clan.getMembers().contains(player.getName())).findFirst().orElse(null);
    }

    @Override
    public boolean hasInvited(User user) {
        TeamInvite invite = UserManagerImpl.getInstance().getUsers().stream()
                .map(User::getInvite)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(teamInvite -> !teamInvite.isExpired())
                .filter(teamInvite -> teamInvite.getTarget().equals(user))
                .findFirst().orElse(null);
        return invite != null;
    }

    @Override
    public Collection<Team> getTeams() {
        return teamMap.values();
    }
}
