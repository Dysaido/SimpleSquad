package xyz.dysaido.squad.api.team;

import org.bukkit.entity.Player;
import xyz.dysaido.squad.api.user.User;
import xyz.dysaido.squad.util.Format;

public class TeamInvite {

    private final User sender;
    private final User target;
    private long expireTime;

    public TeamInvite(User sender, User target, int second) {
        this.sender = sender;
        this.target = target;
        this.expireTime = System.currentTimeMillis() + second * 1000L;
    }

    public int getDuration() {
        long currentTime = System.currentTimeMillis();
        return expireTime > currentTime ? Format.ceil((expireTime - currentTime) / 1000D) : 0;
    }

    public void doLeft() {
        this.expireTime = 0;
    }

    public boolean isExpired() {
        return getDuration() < 1;
    }

    public User getSender() {
        return sender;
    }

    public User getTarget() {
        return target;
    }
}
