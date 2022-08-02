package xyz.dysaido.squad.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.dysaido.squad.SimpleSquad;

public class VaultHook {

    private final Economy economy;
    public VaultHook(SimpleSquad plugin) {
        RegisteredServiceProvider<Economy> economyServiceProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        economy = economyServiceProvider.getProvider();
    }

    public Economy getEconomy() {
        return economy;
    }
}
