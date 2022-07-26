package xyz.dysaido.squad.team;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamInvite;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserType;
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
            TeamImpl team = new TeamImpl(uuid, plugin.getDataYaml());
            teamMap.put(uuid, team);
        }

    }

    @Override
    public boolean createTeam(String name, User leader, String initial) {
        UUID id = generateId(name);
        if (contains(id)) return false;

        ConfigurationSection section = dataYaml.createSection(id.toString());
        OfflinePlayer player = Bukkit.getOfflinePlayer(leader.getId());
        section.set("name", name);
        section.set("initial", initial);
        section.set("kills", 0);
        section.set("deaths", 0);
        section.set("money", 0.00D);
        ConfigurationSection membersSection;
        if (!section.isConfigurationSection("members")) {
            membersSection  = section.createSection("members");
        } else {
            membersSection = section.getConfigurationSection("members");
        }
        ConfigurationSection leaderSection = Objects.requireNonNull(membersSection)
                .createSection(leader.getId().toString());
        leaderSection.set("name", player.getName());
        leaderSection.set("type", UserType.LEADER.name());
        plugin.getDataYaml().saveFile();
        Team team = new TeamImpl(id, plugin.getDataYaml());
        teamMap.put(id, team);
        return true;
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
    public UUID generateId(String name) {
        return UUID.nameUUIDFromBytes(("Squad:" + name).getBytes(Charsets.UTF_8));
    }

    @Override
    public boolean removeTeam(UUID id) {
        Team team = teamMap.remove(id);
        if (team == null) return false;
        team.getUserMap().keySet().stream().map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(player -> UserManagerImpl.getInstance().get(player.getUniqueId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(user -> user.setTeam(null));
        dataYaml.set(id.toString(), null);
        plugin.getDataYaml().saveFile();
        return true;
    }

    @Override
    public Optional<Team> findTeamById(UUID id) {
        return teamMap.values().stream().filter(team -> team.getUserMap().containsKey(id)).findFirst();
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
