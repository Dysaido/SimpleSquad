package xyz.dysaido.squad.team;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.user.UserType;
import xyz.dysaido.squad.util.FilterHelper;
import xyz.dysaido.squad.util.Pair;
import xyz.dysaido.squad.util.YamlBuilder;

import java.util.*;
import java.util.stream.Stream;

public class TeamImpl implements Team {

    private final SimpleSquad plugin = JavaPlugin.getPlugin(SimpleSquad.class);
    private final UUID id;
    private final String name;
    private final String initial;
    private final YamlBuilder dataYaml;
    private final ConfigurationSection section, membersSection;
    private String leader;
    private final Map<UUID, Pair<String, UserType>> userMap = new HashMap<>();
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
        this.kills = section.getInt("kills");
        this.deaths = section.getInt("deaths");
        this.money = section.getDouble("money");
        this.membersSection = section.getConfigurationSection("members");
        Objects.requireNonNull(membersSection);

        for (String memberId : membersSection.getKeys(false)) {
            ConfigurationSection member = membersSection.getConfigurationSection(memberId);
            Objects.requireNonNull(member);

            UUID uuid = UUID.fromString(memberId);
            String name = member.getString("name");
            UserType type = UserType.valueOf(member.getString("type"));

            userMap.putIfAbsent(uuid, new Pair<>(name, type));
        }

        Pair<String, UserType> leaderData = FilterHelper
                .findValueByPredicate(userMap, pair -> pair.getValue() == UserType.LEADER)
                .orElseThrow(NullPointerException::new);
        UUID assumed = FilterHelper.findKeyByValue(userMap, leaderData)
                .orElseThrow(NullPointerException::new);

        this.leader = Bukkit.getOfflinePlayer(assumed).getName();
    }

    @Override
    public Pair<String, UserType> findDataById(UUID id) {
        return userMap.get(id);
    }

    @Override
    public Pair<String, UserType> findDataByName(String name) {
        return FilterHelper.findValueByPredicate(userMap, key -> Objects.equals(key.getKey(), name))
                .orElseThrow(NullPointerException::new);
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
        Pair<String, UserType> oldLeaderData = FilterHelper
                .findValueByPredicate(userMap, pair -> pair.getValue() == UserType.LEADER)
                .orElseThrow(NullPointerException::new);
        UUID oldLeaderId = FilterHelper.findKeyByValue(userMap, oldLeaderData)
                .orElseThrow(NullPointerException::new);

        UUID newLeaderId = player.getUniqueId();
        Pair<String, UserType> newLeaderData = findDataById(newLeaderId);

        ConfigurationSection oldLeader = membersSection.getConfigurationSection(oldLeaderId.toString());
        ConfigurationSection newLeader = membersSection.getConfigurationSection(newLeaderId.toString());

        Objects.requireNonNull(oldLeader).set("type", UserType.DEPUTY.name());
        Objects.requireNonNull(newLeader).set("type", UserType.LEADER.name());

        oldLeaderData.setValue(UserType.DEPUTY);
        newLeaderData.setValue(UserType.LEADER);

        this.leader = player.getName();
        this.dataYaml.saveFile();
    }

    @Override
    public String getLeader() {
        return leader;
    }

    @Override
    public void addDeputy(Player player) {
        UUID deputyId = player.getUniqueId();
        Pair<String, UserType> deputyData = findDataById(deputyId);

        ConfigurationSection newDeputy = membersSection.getConfigurationSection(deputyId.toString());
        Objects.requireNonNull(newDeputy).set("type", UserType.DEPUTY.name());

        deputyData.setValue(UserType.DEPUTY);

        this.dataYaml.saveFile();
    }

    @Override
    public void removeDeputy(String name) {
        Pair<String, UserType> oldDeputyData = this.findDataByName(name);
        UUID oldDeputyId = FilterHelper.findKeyByValue(userMap, oldDeputyData)
                .orElseThrow(NullPointerException::new);

        ConfigurationSection subject = membersSection.getConfigurationSection(oldDeputyId.toString());
        Objects.requireNonNull(subject).set("type", UserType.MEMBER.name());

        oldDeputyData.setValue(UserType.MEMBER);

        this.dataYaml.saveFile();
    }

    @Override
    public Stream<UUID> getDeputies() {
        return FilterHelper.findValuesByPredicate(userMap, data -> data.getValue() == UserType.DEPUTY)
                .map(data -> FilterHelper.findKeyByValue(userMap, data).orElse(null))
                .filter(Objects::nonNull);
    }

    @Override
    public void join(Player player) {
        ConfigurationSection newMember = membersSection.createSection(player.getUniqueId().toString());
        newMember.set("name", player.getName());
        newMember.set("type", UserType.MEMBER.name());

        this.dataYaml.saveFile();
    }

    @Override
    public void kick(String name) {
        Pair<String, UserType> oldMember = this.findDataByName(name);
        UUID oldMemberId = FilterHelper.findKeyByValue(userMap, oldMember)
                .orElseThrow(NullPointerException::new);

        membersSection.set(oldMemberId.toString(), null);

        this.userMap.remove(oldMemberId);
        this.dataYaml.saveFile();
    }

    @Override
    public Stream<UUID> getMembers() {
        return FilterHelper.findValuesByPredicate(userMap, data -> data.getValue() == UserType.MEMBER)
                .map(data -> FilterHelper.findKeyByValue(userMap, data).orElse(null))
                .filter(Objects::nonNull);
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
}
