package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.GUIs.EditKitGUI;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class EditKitListener extends Replacer implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player bukkitPlayer)) return;
        PlayerD player = PlayerManager.get(bukkitPlayer);
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getHolder() instanceof EditKitGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
                player.sendMessage(getMessage("didntSaveKit"));
            }
            if (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                PlayerInventory inventory = player.getInventory();
                Map<Integer, Integer> items = new HashMap<>();
                for (int i = 0; i < 42; i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item == null) continue;
                    ItemMeta meta = item.getItemMeta();
                    Integer defaultI = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "defaultSlot"), PersistentDataType.INTEGER);
                    if (defaultI == null) return;
                    items.put(defaultI, i);
                    meta.getPersistentDataContainer().remove(new NamespacedKey(plugin, "defaultSlot"));
                    item.setItemMeta(meta);
                }
                plugin.getConfig().set("data.kits." + ((EditKitGUI) event.getClickedInventory().getHolder()).getKit().getId() + ".players." + player.getUniqueId(), items);
                plugin.saveConfig();
                plugin.reloadConfig();
                player.sendMessage(value(getMessage("savedKit"), ((EditKitGUI) event.getClickedInventory().getHolder()).getKit().getName()));
                player.closeInventory();
                player.getInventory().clear();
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof EditKitGUI) {
            event.getPlayer().getInventory().clear();
        }
    }
}
