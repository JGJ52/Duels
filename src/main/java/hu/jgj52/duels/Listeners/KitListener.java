package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.GUIs.ArenaPickerGUI;
import hu.jgj52.duels.GUIs.KitCreaterGUI;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class KitListener extends Replacer implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player bukkitPlayer)) return;
        PlayerD player = PlayerManager.get(bukkitPlayer);
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
                    possible.remove(Material.BLACK_STAINED_GLASS_PANE);
                    possible.remove(Material.GRAY_STAINED_GLASS_PANE);
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
                    Inventory gui = Bukkit.createInventory(new ArenaPickerGUI(event.getClickedInventory()), 54, value(getMessage("kit.gui.arenaPickerGui.title"), ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitName()));

                    List<Integer> arenas = new ArrayList<>();
                    int arenasIndex = 0;
                    ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.arenas");
                    if (section != null) {
                        for (String key : section.getKeys(false)) {
                            arenas.add(Integer.parseInt(key));
                        }
                    }
                    if (arenas.size() > 28 || arenas.isEmpty()) return; // you cant have more than 28 arenas because im lazy to do pages for gui haha
                    ItemStack outline = event.getClickedInventory().getItem(0);
                    ItemStack inline = event.getClickedInventory().getItem(10);
                    for (int index = 0; index < 54; index++) {
                        if (index < 10 || List.of(17, 18, 26, 27, 35, 36).contains(index) || index > 43) gui.setItem(index, outline); else {
                            gui.setItem(index, inline);

                            if (arenasIndex < arenas.size()) {
                                ItemStack arena = new ItemStack(Material.WHITE_STAINED_GLASS);
                                ItemMeta arenaMeta = arena.getItemMeta();
                                arenaMeta.setDisplayName("§f" + plugin.getConfig().getString("data.arenas." + arenas.get(arenasIndex) + ".name"));
                                arenaMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, arenas.get(arenasIndex));
                                arena.setItemMeta(arenaMeta);
                                arenasIndex++;

                                gui.setItem(index, arena);
                            }
                        }
                    }

                    ItemStack save = new ItemStack(Material.LIME_CONCRETE);
                    ItemMeta saveMeta = save.getItemMeta();
                    saveMeta.setDisplayName(getMessage("kit.gui.arenaPickerGui.saveName"));
                    save.setItemMeta(saveMeta);
                    gui.setItem(53, save);

                    player.closeInventory();
                    player.openInventory(gui);
                    break;
                case 26:
                    int id = ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitId();
                    if (((KitCreaterGUI) event.getClickedInventory().getHolder()).getArenas() == null || ((KitCreaterGUI) event.getClickedInventory().getHolder()).getArenas().isEmpty()) return;
                    plugin.getConfig().set("data.kits." + id + ".arenas", ((KitCreaterGUI) event.getClickedInventory().getHolder()).getArenas());
                    plugin.getConfig().set("data.kits." + id + ".name", ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitName());
                    plugin.getConfig().set("data.kits." + id + ".maxHealth", event.getClickedInventory().getItem(11).getAmount());
                    plugin.getConfig().set("data.kits." + id + ".icon", event.getClickedInventory().getItem(13).getType().toString());
                    plugin.getConfig().set("data.kits." + id + ".content", ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitContent());
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    player.closeInventory();
                    player.sendMessage(value(getMessage("kit.created"), ((KitCreaterGUI) event.getClickedInventory().getHolder()).getKitName()));
                    break;
            }
        } else if (event.getClickedInventory().getHolder() instanceof ArenaPickerGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS) {
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if (!meta.hasEnchantmentGlintOverride()) {
                    meta.setEnchantmentGlintOverride(true);
                } else {
                    meta.setEnchantmentGlintOverride(false);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if (event.getSlot() == 53) {
                player.closeInventory();
                Inventory old = ((ArenaPickerGUI) event.getClickedInventory().getHolder()).getKitGUI();
                KitCreaterGUI oldHolder = (KitCreaterGUI) old.getHolder();
                List<Integer> arenas = new ArrayList<>();
                for (int i = 0; i < 54; i++) {
                    ItemStack item = event.getClickedInventory().getItem(i);
                    if (item.getItemMeta().hasEnchantmentGlintOverride()) {
                        arenas.add(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER));
                    }
                }
                Inventory newInventory = Bukkit.createInventory(new KitCreaterGUI(oldHolder.getKitName(), oldHolder.getKitId(), oldHolder.getKitContent(), arenas), 27, value(getMessage("kit.gui.title"), oldHolder.getKitName()));
                newInventory.setContents(old.getContents());
                player.openInventory(newInventory);
            }
        }
    }
}
