package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpectatorListener implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        PlayerD player = PlayerManager.get(event.getPlayer());

        if (player.spectating() != null) {
            event.setCancelled(true);
        }
    }
}
