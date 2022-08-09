package xyz.dysaido.squad.team;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.user.UserType;
import xyz.dysaido.squad.util.FilterHelper;
import xyz.dysaido.squad.util.YamlBuilder;

import java.util.*;
import java.util.stream.Stream;

public class TeamImpl implements Team {

    private final SimpleSquad plugin = JavaPlugin.getPlugin(SimpleSquad.class);
    private final UUID id;
    private final String name;
    private final String initial;
    private final YamlBuilder dataYaml;
    private final ConfigurationSection section;
    private String leader;
    private final Map<UUID, UserType> userMap = new HashMap<>();
    private int kills;
    private int deaths;
    private double money;
    private boolean damage = false;

    public TeamImpl(UUID id, YamlBuilder dataYaml) {
        this.id = id;
        this.dataYaml = dataYaml;
        this.section = dataYaml.getFile().getConfigurationSection(id.toString());

        Objects.requireNonNull(section);
        this.name = section.getString("name");
        this.initial = section.getString("initial");
        this.kills =  section.getInt("kills");
        this.deaths = section.getInt("deaths");
        this.money = section.getDouble("money");
        ConfigurationSection membersSection = section.getConfigurationSection("members");

        for (String memberId : Objects.requireNonNull(membersSection).getKeys(false)) {
            ConfigurationSection member = membersSection.getConfigurationSection(memberId);
            UUID uuid = UUID.fromString(memberId);
            UserType type = UserType.valueOf(Objects.requireNonNull(member).getString("type"));
            userMap.putIfAbsent(uuid, type);
        }
        UUID assumed = FilterHelper.getKey(userMap, UserType.LEADER).orElseThrow(NullPointerException::new);
        this.leader = Bukkit.getOfflinePlayer(assumed).getName();
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
    public void setLeader(Player player) {
        ConfigurationSection membersSection = section.getConfigurationSection("members");
        UUID leader = FilterHelper.getKey(userMap, UserType.LEADER).orElseThrow(NullPointerException::new);
        
        ConfigurationSection oldLeader = Objects.requireNonNull(membersSection)
                .getConfigurationSection(leader.toString());
        ConfigurationSection newLeader = Objects.requireNonNull(membersSection)
                .getConfigurationSection(player.getUniqueId().toString());
        
        Objects.requireNonNull(oldLeader).set("type", UserType.DEPUTY.name());
        Objects.requireNonNull(newLeader).set("type", UserType.LEADER.name());

        this.leader = player.getName();
        this.dataYaml.saveFile();
    }

    @Override
    public String getLeader() {
        return leader;
    }

    @Override
    public void addDeputy(Player player) {
        ConfigurationSection membersSection = section.getConfigurationSection("members");
        ConfigurationSection newDeputy = Objects.requireNonNull(membersSection)
                .getConfigurationSection(player.getUniqueId().toString());
        Objects.requireNonNull(newDeputy).set("type", UserType.DEPUTY.name());
        
        this.dataYaml.saveFile();
    }

    @Override
    public void removeDeputy(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        ConfigurationSection membersSection = section.getConfigurationSection("members");
        ConfigurationSection subject = Objects.requireNonNull(membersSection)
                .getConfigurationSection(player.getUniqueId().toString());
        Objects.requireNonNull(subject).set("type", UserType.MEMBER.name());

        this.dataYaml.saveFile();
    }

    @Override
    public Stream<UUID> getDeputies() {
        return FilterHelper.getKeys(userMap, UserType.DEPUTY);
    }

    @Override
    public void join(Player player) {
        ConfigurationSection membersSection = section.getConfigurationSection("members");
        ConfigurationSection newMember = Objects.requireNonNull(membersSection)
                .createSection(player.getUniqueId().toString());
        newMember.set("name", player.getName());
        newMember.set("type", UserType.MEMBER.name());

        this.dataYaml.saveFile();
    }

    @Override
    public void kick(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        ConfigurationSection membersSection = section.getConfigurationSection("members");
        Objects.requireNonNull(membersSection).set(player.getUniqueId().toString(), null);

        this.userMap.remove(player.getUniqueId());
        this.dataYaml.saveFile();
    }

    @Override
    public Stream<UUID> getMembers() {
        return FilterHelper.getKeys(userMap, UserType.MEMBER);
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

    @Override
    public void setDamage(boolean damage) {
        this.damage = damage;
    }

    @Override
    public boolean canDamage() {
        return damage;
    }

    @Override
    public Map<UUID, UserType> getUserMap() {
        return Collections.unmodifiableMap(userMap);
    }
}
