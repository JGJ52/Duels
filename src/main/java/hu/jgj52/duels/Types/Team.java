package hu.jgj52.duels.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {
    private final Map<PlayerD, Boolean> players = new HashMap<>();
    private int points;
    private final List<PlayerD> removedPlayers = new ArrayList<>();

    public Team (List<PlayerD> players) {
        for (PlayerD player : players) {
            this.players.put(player, true);
        }
        points = 0;
    }

    public List<PlayerD> getPlayers() {
        return new ArrayList<>(this.players.keySet());
    }

    public boolean setDefeated(PlayerD player) {
        if (players.get(player) != null) {
            players.put(player, false);
            return true;
        }
        return false;
    }

    public boolean setAlive(PlayerD player) {
        if (players.get(player) != null) {
            players.put(player, true);
        }
        return false;
    }

    public List<PlayerD> getAlivePlayers() {
        List<PlayerD> players = new ArrayList<>();
        for (PlayerD player : this.players.keySet()) {
            if (this.players.get(player)) {
                players.add(player);
            }

        }
        return players;
    }

    public List<PlayerD> getRemovedPlayers() {
        return removedPlayers;
    }

    public void removePlayer(PlayerD player) {
        removedPlayers.add(player);
    }

    public int getPoints() {
        return points;
    }

    public void addPoints() {
        points++;
    }
}
