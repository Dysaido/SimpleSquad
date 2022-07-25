package xyz.dysaido.squad.api.team;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Team {

    UUID getId();

    String getName();

    String getInitial();

    boolean isLeader(Player player);

    void setLeader(String leader);

    String getLeader();

    void addMember(Player player);

    void removeMember(String name);

    List<String> getMembers();

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
}
