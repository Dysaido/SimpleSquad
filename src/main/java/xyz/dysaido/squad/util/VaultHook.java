package xyz.dysaido.squad.util;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private final Economy economy;
    private final Chat chat;

    public VaultHook() {
        RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        economy = economyServiceProvider.getProvider();

        RegisteredServiceProvider<Chat> chatServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        chat = chatServiceProvider.getProvider();
    }

    public Chat getChat() {
        return chat;
    }

    public Economy getEconomy() {
        return economy;
    }
}
