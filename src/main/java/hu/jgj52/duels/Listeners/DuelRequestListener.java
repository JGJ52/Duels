package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.GUIs.DuelRequestGUI;
import hu.jgj52.duels.Handlers.DuelRequestHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DuelRequestListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!(event.getInventory().getHolder() instanceof DuelRequestGUI)) {
            event.setCancelled(true);
            Player enemy = RuntimeVariables.duelRequests.get(player);
            RuntimeVariables.duelRequests.remove(player);
            if (!enemy.isOnline()) {
                player.sendMessage(Replacer.playerName(MessageManager.getMessage("noPlayer"), enemy));
                return;
            }
            DuelRequestHandler.sendDuelRequest(player, enemy, event);
        }
    }
}
