package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Map;

public class DuelEndListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (RuntimeVariables.isInDuel.getOrDefault(player, false)) {
            Team team = new Team(List.of());
            Team enemy = new Team(List.of());
            for (Map<String, Object> duel : RuntimeVariables.duels) {
                Team blue = (Team) duel.get("blue");
                Team red = (Team) duel.get("red");
                if (blue.getPlayers().contains(player)) { team = blue; enemy = red; }
                if (red.getPlayers().contains(player)) { team = red; enemy = blue; }
            }
            team.setDefeated(player);
            player.setGameMode(GameMode.SPECTATOR);
            if (team.getAlivePlayers().isEmpty()) {

            } else {

            }
        } else {
            PlayerManager.tpToSpawn(player);
        }
    }
}
