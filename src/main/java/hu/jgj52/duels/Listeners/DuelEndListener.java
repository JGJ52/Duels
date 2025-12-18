package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.Handlers.DuelEndHandler;
import hu.jgj52.duels.Types.PlayerD;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DuelEndListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player bukkitPlayer = event.getPlayer();
        PlayerD player = new PlayerD(bukkitPlayer);
        event.setCancelled(true);
        DuelEndHandler.duelEnd(player);
    }
}
