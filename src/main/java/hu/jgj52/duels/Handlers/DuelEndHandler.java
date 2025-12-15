package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
            RuntimeVariables.duels.remove(data);
            enemy.addPoints();
            if (color.equals("blue")) data.put("blue", team); else if (color.equals("red")) data.put("red", team); else return false;
            player.setGameMode(GameMode.SPECTATOR);
            if (team.getAlivePlayers().isEmpty()) {
                if (enemy.getPoints() < (int) data.get("rounds")) {
                    //todo: test this, im not sure if this is right but probably it is
                    AcceptDuelHandler.acceptDuel(color.equals("blue") ? team : enemy, color.equals("red") ? enemy : team, data);
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
