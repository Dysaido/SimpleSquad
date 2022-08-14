package xyz.dysaido.squad.user;

import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.api.user.UserManager;

import java.util.*;

public class UserManagerImpl implements UserManager {

    private final Map<UUID, User> userMap = new HashMap<>();

    private static UserManagerImpl instance;

    public static UserManagerImpl getInstance() {
        return instance == null ? instance = new UserManagerImpl() : instance;
    }

    @Override
    public User add(UUID id, String name) {
        Objects.requireNonNull(id);
        return userMap.computeIfAbsent(id, uuid -> new UserImpl(uuid, name));
    }

    @Override
    public Optional<User> get(UUID id) {
        Objects.requireNonNull(id);
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> remove(UUID id) {
        Objects.requireNonNull(id);
        User user = userMap.remove(id);
        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> getUsers() {
        return userMap.values();
    }

}
