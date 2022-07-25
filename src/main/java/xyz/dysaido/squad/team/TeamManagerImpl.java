package xyz.dysaido.squad.team;

import com.google.common.base.Charsets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManagerImpl {

    private final Map<UUID, TeamImpl> teamMap = new HashMap<>();
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

    public void createTeam(String name, String leader, String initial) {
        UUID id = UUID.nameUUIDFromBytes(("Squad:" + name).getBytes(Charsets.UTF_8));
        ConfigurationSection section = dataYaml.createSection(id.toString());
        section.set("name", name);
        section.set("initial", initial);
        section.set("leader", leader);
        section.set("kills", 0);
        section.set("deaths", 0);
        section.set("money", 0.00D);
        section.set("members", Collections.singletonList(leader));
        plugin.getDataYaml().saveFile();
        teamMap.put(id, new TeamImpl(id, plugin.getDataYaml()));
    }

    public void removeTeam(String name) {

    }
}
