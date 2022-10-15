package xyz.dysaido.squad.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;
import xyz.dysaido.squad.config.DefaultYaml;
import xyz.dysaido.squad.util.Format;
import xyz.dysaido.squad.util.Logger;

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
        User user = userManager.addOrGet(player.getUniqueId(), player.getName());
        Logger.debug("SquadListener", String.format("Registered %s", user));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Optional<Team> optional = teamManager.findTeamById(event.getPlayer().getUniqueId());
        if (optional.isPresent() && DefaultYaml.CHAT_PREFIX) {
            Team team = optional.get();
            String format = Format.colored(DefaultYaml.CHAT_PREFIX_FORMAT);
            format = format.replace("%initial%", team.getInitial());
            format = format + event.getFormat();
            event.setFormat(format);
        }

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
        if (victimEntity instanceof Player) {
            Player destination = (Player) victimEntity;
            if (killerEntity instanceof Player) {
                Player source = (Player) killerEntity;
                protectMembers(event, destination, source);
            } else if (killerEntity instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) killerEntity).getShooter();
                if (shooter instanceof Player) {
                    Player source = (Player) shooter;
                    if (!victimEntity.getUniqueId().equals(source.getUniqueId())) {
                        protectMembers(event, destination, source);
                    }
                }
            }
        }
    }

    private void protectMembers(Cancellable cancellable, Player victimPlayer, Player killerPlayer) {
        User victim = userManager.get(victimPlayer.getUniqueId())
                .orElseGet(() -> userManager.addOrGet(victimPlayer.getUniqueId(), victimPlayer.getName()));
        User killer = userManager.get(killerPlayer.getUniqueId())
                .orElseGet(() -> userManager.addOrGet(killerPlayer.getUniqueId(), killerPlayer.getName()));
        if (victim.isSame(killer)) {
            cancellable.setCancelled(true);
        }

    }

}
