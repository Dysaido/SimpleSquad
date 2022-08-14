package xyz.dysaido.squad.team;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.api.user.UserType;
import xyz.dysaido.squad.user.UserManagerImpl;
import xyz.dysaido.squad.util.FilterHelper;
import xyz.dysaido.squad.util.Logger;
import xyz.dysaido.squad.util.YamlBuilder;

import java.util.*;
import java.util.stream.Stream;

public class TeamImpl implements Team {

    private final SimpleSquad plugin = JavaPlugin.getPlugin(SimpleSquad.class);
    private final UserManager userManager = UserManagerImpl.getInstance();
    private final Map<UUID, User> userMap = new HashMap<>();
    private final UUID id;
    private final String name;
    private final String initial;
    private final YamlBuilder dataYaml;
    private final ConfigurationSection section, membersSection;
    private String leader;
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

            UUID userId = UUID.fromString(memberId);
            String name = member.getString("name");
            UserType type = UserType.valueOf(member.getString("type"));

            User user = userManager.add(userId, name);
            user.setTeam(this);
            user.setType(type);
            userMap.putIfAbsent(userId, user);
            Logger.debug("Team", "Loaded " + user);
        }

        User leader = FilterHelper
                .findValueByPredicate(userMap, user -> user.getType() == UserType.LEADER)
                .orElseThrow(NullPointerException::new);

        this.leader = leader.getName();
    }

    @Override
    public User findDataById(UUID id) {
        return userMap.get(id);
    }

    @Override
    public User findDataByName(String name) {
        return FilterHelper.findValueByPredicate(userMap, user -> Objects.equals(user.getName(), name))
                .orElse(null);
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
    public boolean isLeader(User user) {
        return this.leader.equals(user.getName());
    }

    @Override
    public void setLeader(User newLeaderData) {
        User oldLeaderData = FilterHelper
                .findValueByPredicate(userMap, user -> user.getType() == UserType.LEADER)
                .orElseThrow(NullPointerException::new);
        UUID oldLeaderId = oldLeaderData.getId();

        UUID newLeaderId = newLeaderData.getId();

        ConfigurationSection oldLeader = membersSection.getConfigurationSection(oldLeaderId.toString());
        ConfigurationSection newLeader = membersSection.getConfigurationSection(newLeaderId.toString());

        Objects.requireNonNull(oldLeader).set("type", UserType.DEPUTY.name());
        Objects.requireNonNull(newLeader).set("type", UserType.LEADER.name());

        oldLeaderData.setType(UserType.DEPUTY);
        newLeaderData.setType(UserType.LEADER);

        this.leader = newLeaderData.getName();
        this.dataYaml.saveFile();
    }

    @Override
    public String getLeader() {
        return leader;
    }

    @Override
    public void addDeputy(Player player) {
        UUID deputyId = player.getUniqueId();
        User deputyData = findDataById(deputyId);

        ConfigurationSection newDeputy = membersSection.getConfigurationSection(deputyId.toString());
        Objects.requireNonNull(newDeputy).set("type", UserType.DEPUTY.name());

        deputyData.setType(UserType.DEPUTY);

        this.dataYaml.saveFile();
    }

    @Override
    public void removeDeputy(String name) {
        User oldDeputyData = this.findDataByName(name);
        UUID oldDeputyId = oldDeputyData.getId();

        ConfigurationSection subject = membersSection.getConfigurationSection(oldDeputyId.toString());
        Objects.requireNonNull(subject).set("type", UserType.MEMBER.name());

        oldDeputyData.setType(UserType.MEMBER);

        this.dataYaml.saveFile();
    }

    @Override
    public Stream<UUID> getDeputies() {
        return FilterHelper.findValuesByPredicate(userMap, user -> user.getType() == UserType.DEPUTY)
                .map(user -> FilterHelper.findKeyByValue(userMap, user).orElse(null))
                .filter(Objects::nonNull);
    }

    @Override
    public void join(User user) {
        user.setTeam(this);
        user.setType(UserType.MEMBER);

        ConfigurationSection newMember = membersSection.createSection(user.getId().toString());
        newMember.set("name", user.getName());
        newMember.set("type", UserType.MEMBER.name());

        this.dataYaml.saveFile();
    }

    @Override
    public void kick(String name) {
        User oldMember = this.findDataByName(name);
        UUID oldMemberId = oldMember.getId();

        membersSection.set(oldMemberId.toString(), null);

        oldMember.setTeam(null);
        this.userMap.remove(oldMemberId);
        this.dataYaml.saveFile();
    }

    @Override
    public Stream<UUID> getMembers() {
        return FilterHelper.findValuesByPredicate(userMap, user -> user.getType() == UserType.MEMBER)
                .map(user -> FilterHelper.findKeyByValue(userMap, user).orElse(null))
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

    @Override
    public Map<UUID, User> getUserMap() {
        return userMap;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", initial='" + initial + '\'' +
                ", leader='" + leader + '\'' +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", money=" + money +
                ", damage=" + damage +
                '}';
    }
}
