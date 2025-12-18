package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.Handlers.DuelEndHandler;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        PlayerD player = PlayerManager.get(event.getPlayer());
        if (player.isInDuel()) {

            Team team = new Team(List.of());
            Map<String, Object> data = new HashMap<>();
            Iterator<Map<String, Object>> iterator = RuntimeVariables.duels.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> duel = iterator.next();
                Team blue = (Team) duel.get("blue");
                Team red = (Team) duel.get("red");

                if (blue.getPlayers().contains(event.getPlayer())) {
                    team = blue;
                    iterator.remove();
                    data = duel;
                    break;
                }

                if (red.getPlayers().contains(event.getPlayer())) {
                    team = red;
                    iterator.remove();
                    data = duel;
                    break;
                }
            }
            team.removePlayer(player);
            RuntimeVariables.duels.add(data);

            DuelEndHandler.duelEnd(player);
        }
        PlayerManager.remove(event.getPlayer());
    }
}
