package xyz.dysaido.squad.team;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.util.YamlBuilder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeamImpl implements Team {

    private final UUID id;
    private final String name;
    private final String initial;
    private final YamlBuilder dataYaml;
    private final ConfigurationSection section;
    private String leader;
    private final List<String> members;
    private int kills;
    private int deaths;
    private double money;

    public TeamImpl(UUID id, YamlBuilder dataYaml) {
        this.id = id;
        this.dataYaml = dataYaml;
        this.section = dataYaml.getFile().getConfigurationSection(id.toString());

        Objects.requireNonNull(section);
        this.name = section.getString("name");
        this.initial = section.getString("initial");
        this.leader = section.getString("leader");
        this.kills =  section.getInt("kills");
        this.deaths = section.getInt("deaths");
        this.money = section.getDouble("money");
        this.members = section.getStringList("members");
    }

    public void forceReset() {
        setKills(0);
        setDeaths(0);
        setMoney(0.00D);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInitial() {
        return initial;
    }

    @Override
    public boolean isLeader(Player player) {
        return this.leader.equals(player.getName());
    }

    @Override
    public void setLeader(String leader) {
        this.leader = leader;
        this.section.set("leader", leader);
        this.dataYaml.saveFile();
    }

    @Override
    public String getLeader() {
        return leader;
    }

    @Override
    public void addMember(Player player) {
        this.members.add(player.getName());
        this.section.set("members", members);
        this.dataYaml.saveFile();
    }

    @Override
    public void removeMember(String name) {
        this.members.remove(name);
        this.section.set("members", members);
        this.dataYaml.saveFile();
    }

    @Override
    public List<String> getMembers() {
        return members;
    }

    @Override
    public void addKill() {
        int kills = this.kills;
        kills += 1;
        setKills(kills);
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
        this.section.set("kills", kills);
        this.dataYaml.saveFile();
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void addDeath() {
        int deaths = this.deaths;
        deaths += 1;
        setDeaths(deaths);
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        this.section.set("deaths", deaths);
        this.dataYaml.saveFile();
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public void deposit(double amount) {
        double money = this.money;
        money += amount;
        setMoney(money);
    }

    @Override
    public void withdraw(double amount) {
        double money = this.money;
        money -= amount;
        setMoney(money);
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
        this.section.set("money", money);
        this.dataYaml.saveFile();
    }

    @Override
    public double getMoney() {
        return money;
    }
}
