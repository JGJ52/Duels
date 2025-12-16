package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.Arena;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class DuelEndHandler {
    public static boolean duelEnd(Player player) {
        if (RuntimeVariables.isInDuel.getOrDefault(player, false)) {
            Team team = new Team(List.of());
            Team enemy = new Team(List.of());
            String color = "";
            Map<String, Object> data = new HashMap<>();
            Iterator<Map<String, Object>> iterator = RuntimeVariables.duels.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> duel = iterator.next();
                Team blue = (Team) duel.get("blue");
                Team red = (Team) duel.get("red");

                if (blue.getPlayers().contains(player)) {
                    team = blue;
                    enemy = red;
                    data = duel;
                    color = "blue";
                    iterator.remove();
                    break;
                }

                if (red.getPlayers().contains(player)) {
                    team = red;
                    enemy = blue;
                    color = "red";
                    data = duel;
                    iterator.remove();
                    break;
                }
            }
            team.setDefeated(player);
            enemy.addPoints();
            player.setGameMode(GameMode.SPECTATOR);
            if (team.getRemovedPlayers().size() == team.getPlayers().size()) {
                for (Player players : team.getPlayers()) {
                    if (players.isOnline()) {
                        players.sendTitle(MessageManager.getMessage("youLost"), "", 0, 20, 0);
                        PlayerManager.tpToSpawn(players);
                    }
                    RuntimeVariables.isInDuel.remove(players);
                }
                for (Player players : enemy.getPlayers()) {
                    players.sendTitle(MessageManager.getMessage("youWon"), "", 0, 20, 0);
                    PlayerManager.tpToSpawn(players);
                    RuntimeVariables.isInDuel.remove(players);
                }
                return true;
            }
            if (team.getAlivePlayers().isEmpty()) {
                if (enemy.getPoints() < (int) data.get("rounds")) {
                    for (Player p : team.getPlayers()) {
                        team.setAlive(p);
                    }
                    for (Player p : enemy.getPlayers()) {
                        enemy.setAlive(p);
                    }
                    if (color.equals("blue")) data.put("blue", team); else if (color.equals("red")) data.put("red", team); else return false;
                    Map<String, Object> finalData = data;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> AcceptDuelHandler.acceptDuel((Team) finalData.get("blue"), (Team) finalData.get("red"), finalData), 40L);
                } else {
                    for (Player players : team.getPlayers()) {
                        players.sendTitle(MessageManager.getMessage("youLost"), "", 0, 20, 0);
                        PlayerManager.tpToSpawn(players);
                        RuntimeVariables.isInDuel.remove(players);
                    }
                    for (Player players : enemy.getPlayers()) {
                        players.sendTitle(MessageManager.getMessage("youWon"), "", 0, 20, 0);
                        PlayerManager.tpToSpawn(players);
                        RuntimeVariables.isInDuel.remove(players);
                    }
                }
            } else {
                if (color.equals("blue")) data.put("blue", team); else if (color.equals("red")) data.put("red", team); else return false;
                for (Player players : team.getPlayers()) {
                    players.sendMessage(Replacer.playerName(MessageManager.getMessage("playerDefeated"), player));
                }
                for (Player players : enemy.getPlayers()) {
                    players.sendMessage(Replacer.playerName(MessageManager.getMessage("playerDefeatedYay"), player));
                }
                RuntimeVariables.duels.add(data);
            }
        } else {
            PlayerManager.tpToSpawn(player);
        }
        return true;
    }
}
