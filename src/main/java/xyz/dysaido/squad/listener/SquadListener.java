package xyz.dysaido.squad.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.api.team.Team;
import xyz.dysaido.squad.api.team.TeamManager;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;

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

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

    }

}
