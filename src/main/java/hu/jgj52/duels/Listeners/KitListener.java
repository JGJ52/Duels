package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.GUIs.KitCreaterGUI;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class KitListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getHolder() instanceof KitCreaterGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            switch (event.getSlot()) {
                case 11:
                    if (event.getClick().isLeftClick()) {
                        event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() + 1);
                    } else {
                        if (event.getCurrentItem().getAmount() > 1) {
                            event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() - 1);
                        }
                    }
                    break;
                case 13:
                    List<Material> possible = ((KitCreaterGUI) event.getClickedInventory().getHolder()).getPossible();
                    int i = ((KitCreaterGUI) event.getClickedInventory().getHolder()).getIndex();
                    if (event.getClick().isLeftClick()) {
                        if (i < possible.size()) {
                            ((KitCreaterGUI) event.getClickedInventory().getHolder()).setIndex(i + 1);
                            ItemStack newItem = event.getCurrentItem().withType(possible.get(i + 1));
                            event.getInventory().setItem(13, newItem);
                        }
                    } else {
                        if (i > 1) {
                            ((KitCreaterGUI) event.getClickedInventory().getHolder()).setIndex(i - 1);
                            ItemStack newItem = event.getCurrentItem().withType(possible.get(i - 1));
                            event.getInventory().setItem(13, newItem);
                        }
                    }
                    break;
                case 15:

                    break;
                case 26:
                    int id = ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitId();
                    plugin.getConfig().set("data.kits." + id + ".name", ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitName());
                    plugin.getConfig().set("data.kits." + id + ".maxHealth", event.getClickedInventory().getItem(11).getAmount());
                    plugin.getConfig().set("data.kits." + id + ".icon", event.getClickedInventory().getItem(13).getType().toString());
                    plugin.getConfig().set("data.kits." + id + ".content", ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitContent());
                    //todo: arenas
                    player.closeInventory();
                    player.sendMessage(Replacer.value(MessageManager.getMessage("kit.created"), ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitName()));
                    break;
                default:
                    return;
            }
        }
    }
}
