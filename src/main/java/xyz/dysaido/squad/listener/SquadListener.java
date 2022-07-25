package xyz.dysaido.squad.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.dysaido.squad.SimpleSquad;
import xyz.dysaido.squad.team.TeamManagerImpl;

public class SquadListener implements Listener {

    private final SimpleSquad plugin;
    private final TeamManagerImpl teamManager;

    public SquadListener(SimpleSquad plugin) {
        this.plugin = plugin;
        this.teamManager = plugin.getTeamManager();
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
