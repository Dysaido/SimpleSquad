package xyz.dysaido.squad.user;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamInvite;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.util.Format;
import xyz.dysaido.squad.util.Logger;

import java.util.Optional;
import java.util.UUID;

public class UserImpl implements User {

    private final SimpleSquad plugin = JavaPlugin.getPlugin(SimpleSquad.class);
    private final UUID id;
    private Team team;
    private TeamInvite invite;
    private double balance;

    public UserImpl(UUID id) {
        this.id = id;
    }

    @Override
    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public Optional<Team> getTeam() {
        return Optional.ofNullable(team);
    }

    @Override
    public void setInvite(TeamInvite invite) {
        this.invite = invite;
    }

    @Override
    public Optional<TeamInvite> getInvite() {
        return Optional.ofNullable(invite);
    }

    @Override
    public boolean isSame(User user) {
        if (user.getTeam().isPresent()) {
            Team team = user.getTeam().get();
            return team.equals(this.team) && !team.canDamage();
        }
        return false;
    }

    @Override
    public boolean isLeader() {
        Player player = Bukkit.getPlayer(id);
        return team != null && player != null && team.isLeader(player);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void withdraw(double amount) {
        Format.ifElse(plugin.getVaultHook(), vaultHook -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(id);
            vaultHook.getEconomy().withdrawPlayer(player, amount);
        }, () -> Format.hookWarningMessage("User", "Vault", "withdraw"));
    }

    @Override
    public void deposit(double amount) {
        Format.ifElse(plugin.getVaultHook(), vaultHook -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(id);
            vaultHook.getEconomy().depositPlayer(player, amount);
        }, () -> Format.hookWarningMessage("User", "Vault", "deposit"));
    }

    @Override
    public double getBalance() {
        Format.ifElse(plugin.getVaultHook(), vaultHook -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(id);
            balance = vaultHook.getEconomy().getBalance(player);
        }, () -> {
            balance = 0.00;
            Format.hookWarningMessage("User", "Vault", "getBalance");
        });
        return balance;
    }
}
