package xyz.dysaido.squad.api.team;

import org.bukkit.entity.Player;
import xyz.dysaido.squad.api.user.User;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface Team {

    User findDataById(UUID id);

    User findDataByName(String name);

    UUID getId();

    String getName();

    String getInitial();

    boolean isLeader(User user);

    void setLeader(Player player);

    String getLeader();

    void addDeputy(Player player);

    void removeDeputy(String name);

    Stream<UUID> getDeputies();

    void join(Player player);

    void kick(String name);

    Stream<UUID> getMembers();

    void addKill();

    void setKills(int kills);

    int getKills();

    void addDeath();

    void setDeaths(int deaths);

    int getDeaths();

    void deposit(double amount);

    void withdraw(double amount);

    void setMoney(double money);

    double getMoney();

    void setDamage(boolean damage);

    boolean canDamage();

    Map<UUID, User> getUserMap();
}
