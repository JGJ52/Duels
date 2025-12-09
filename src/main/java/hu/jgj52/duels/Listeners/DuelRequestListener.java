package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.GUIs.DuelRequestGUI;
import hu.jgj52.duels.Handlers.DuelRequestHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static hu.jgj52.duels.Duels.plugin;

public class DuelRequestListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getHolder() instanceof DuelRequestGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) return;
            Player enemy = RuntimeVariables.duelRequests.get(player);
            if (!enemy.isOnline()) {
                player.sendMessage(Replacer.playerName(MessageManager.getMessage("noPlayer"), enemy));
                return;
            }
            if (event.getClickedInventory() == null) return;
            if (event.getSlot() == 4) {
                ItemStack rounds = event.getClickedInventory().getItem(4);
                if (rounds == null) return;
                if (event.getClick().isLeftClick()) {
                    if (rounds.getAmount() < 64) {
                        rounds.setAmount(rounds.getAmount() + 1);
                    }
                } else if (event.getClick().isRightClick()) {
                    if (rounds.getAmount() > 1) {
                        rounds.setAmount(rounds.getAmount() - 1);
                    }
                }
                plugin.getConfig().set("data.players." + player.getUniqueId() + ".rounds", rounds.getAmount());
                plugin.saveConfig();
                plugin.reloadConfig();
                return;
            }
            if (event.getSlot() == 49) {
                ItemStack spectate = event.getClickedInventory().getItem(49);
                if (spectate == null) return;
                if (spectate.getType() == Material.LIME_STAINED_GLASS) {
                    event.getClickedInventory().setItem(49, spectate.withType(Material.RED_STAINED_GLASS));
                    plugin.getConfig().set("data.players." + player.getUniqueId() + ".spectators", false);
                } else if (spectate.getType() == Material.RED_STAINED_GLASS) {
                    event.getClickedInventory().setItem(49, spectate.withType(Material.LIME_STAINED_GLASS));
                    plugin.getConfig().set("data.players." + player.getUniqueId() + ".spectators", true);
                }
                plugin.saveConfig();
                plugin.reloadConfig();
                return;
            }
            RuntimeVariables.duelRequests.remove(player);
            DuelRequestHandler.sendDuelRequest(player, enemy, event);
        }
    }
}
