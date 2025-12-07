package hu.jgj52.duels.Types;

import org.bukkit.entity.Player;

import java.util.List;

public class Team {
    private final List<Player> players;

    public Team (List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
