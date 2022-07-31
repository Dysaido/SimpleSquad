package xyz.dysaido.squad.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;

import java.util.Optional;

public class SquadListener implements Listener {

    private final SimpleSquad plugin;
    private final TeamManager teamManager;
    private final UserManager userManager;

    public SquadListener(SimpleSquad plugin) {
        this.plugin = plugin;
        this.teamManager = plugin.getTeamManager();
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = userManager.add(player.getUniqueId());
        Team team = teamManager.get(user.getId());
        user.setTeam(team);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Optional<User> vUser = userManager.get(victim.getUniqueId());
        vUser.flatMap(User::getTeam).ifPresent(Team::addDeath);
        if (killer != null) {
            Optional<User> kUser = userManager.get(killer.getUniqueId());
            kUser.flatMap(User::getTeam).ifPresent(Team::addKill);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity victimEntity = event.getEntity();
        Entity killerEntity = event.getDamager();
        if (victimEntity instanceof Player && killerEntity instanceof Player) {
            protectMembers(event, (Player) victimEntity, (Player) killerEntity);
        } else if (victimEntity instanceof Player && killerEntity instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) killerEntity).getShooter();
            if (shooter instanceof Player) {
                if (!victimEntity.getUniqueId().equals(((Player) shooter).getUniqueId())) {
                    protectMembers(event, (Player) victimEntity, (Player) shooter);
                }
            }
        }
    }

    private void protectMembers(Cancellable cancellable, Player victimPlayer, Player killerPlayer) {
        User victim = userManager.get(victimPlayer.getUniqueId()).orElseThrow(NullPointerException::new);
        User killer = userManager.get(killerPlayer.getUniqueId()).orElseThrow(NullPointerException::new);

        if (victim.isSimilar(killer)) {
            cancellable.setCancelled(true);
        }

    }

}
