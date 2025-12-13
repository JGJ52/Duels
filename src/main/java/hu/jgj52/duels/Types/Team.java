package hu.jgj52.duels.Types;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {
    private final Map<Player, Boolean> players = new HashMap<>();

    public Team (List<Player> players) {
        for (Player player : players) {
            this.players.put(player, true);
        }
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(this.players.keySet());
    }

    public boolean setDefeated(Player player) {
        if (players.get(player) != null) {
            players.put(player, false);
            return true;
        }
        return false;
    }

    public List<Player> getAlivePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : this.players.keySet()) {
            if (this.players.get(player)) players.add(player);
        }
        return players;
    }
}
