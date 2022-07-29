package xyz.dysaido.squad.api.user;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserManager {

    User add(UUID id);

    Optional<User> get(UUID id);

    Optional<User> remove(UUID id);

    Collection<User> getUsers();
}
