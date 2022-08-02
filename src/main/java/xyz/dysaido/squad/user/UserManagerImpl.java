package xyz.dysaido.squad.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;

import java.util.*;

public class UserManagerImpl implements UserManager {

    private final Map<UUID, User> userMap = new HashMap<>();

    private static UserManagerImpl instance;

    public static UserManagerImpl getInstance() {
        return instance == null ? instance = new UserManagerImpl() : instance;
    }

    public void enable() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> this.add(player.getUniqueId()));
    }

    public void disable() {
        Bukkit.getServer().getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(this::remove);
        userMap.clear();
    }

    public User add(UUID id) {
        Objects.requireNonNull(id);
        User user = new UserImpl(id);
        userMap.put(id, user);
        return user;
    }

    public Optional<User> get(UUID id) {
        Objects.requireNonNull(id);
        return Optional.ofNullable(userMap.get(id));
    }

    public Optional<User> remove(UUID id) {
        Objects.requireNonNull(id);
        User user = userMap.remove(id);
        return Optional.ofNullable(user);
    }

    public Collection<User> getUsers() {
        return userMap.values();
    }

}
