package hu.jgj52.duels.Listeners;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import hu.jgj52.duels.GUIs.ArenaCreaterGUI;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static hu.jgj52.duels.Duels.plugin;

public class ArenaListener extends Replacer implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getHolder() instanceof ArenaCreaterGUI holder) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            if (List.of(11, 15).contains(event.getSlot())) {
                if (event.isLeftClick()) {
                    if (item.getAmount() < 64) {
                        item.setAmount(item.getAmount() + 1);
                    }
                } else {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            } else if (event.getSlot() == 26) {
                String name = holder.getName();
                try {
                    File folder = new File(plugin.getDataFolder(), "schematics");
                    if (!folder.exists()) folder.mkdirs();
                    File file = new File(folder, name + ".schem");
                    ClipboardHolder clipboard = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getClipboard();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        ClipboardWriter writer = ClipboardFormats.findByAlias("schem").getWriter(fos);
                        writer.write(clipboard.getClipboard());
                        writer.close();
                        Region region = clipboard.getClipboard().getRegion();
                        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(region.getWorld());
                        BlockVector3 min1 = clipboard.getClipboard().getMinimumPoint();
                        BlockVector3 max1 = clipboard.getClipboard().getMaximumPoint();
                        int centerX = (int) region.getCenter().x();
                        int centerY = (int) region.getCenter().y();
                        int centerZ = (int) region.getCenter().z();
                        List<ProtectedRegion> regions = new ArrayList<>();
                        for (ProtectedRegion rg : rm.getRegions().values()) {
                            BlockVector3 min2 = rg.getMinimumPoint();
                            BlockVector3 max2 = rg.getMaximumPoint();
                            if (min1.x() <= max2.x() && max1.x() >= min2.x() &&
                                min1.y() <= max2.y() && max1.y() >= min2.y() &&
                                min1.z() <= max2.z() && max1.z() >= min2.z()) {
                                regions.add(rg);
                            }
                        }
                        List<Integer> ids = new ArrayList<>();
                        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.arenas");
                        for (String key : section.getKeys(false)) {
                            ids.add(Integer.parseInt(key));
                        }
                        int id;
                        if (ids.isEmpty()) id = 1; else id = Collections.max(ids) + 1;
                        plugin.getConfig().set("data.arenas." + id + ".name", name);
                        plugin.getConfig().set("data.arenas." + id + ".distance", event.getClickedInventory().getItem(11).getAmount());
                        plugin.getConfig().set("data.arenas." + id + ".cooldown", event.getClickedInventory().getItem(15).getAmount());
                        int rgI = 0;
                        for (ProtectedRegion rg : regions) {
                            rgI++;
                            Map<Flag<?>, Object> rgs = rg.getFlags();
                            Map<String, Object> goodRgs = new HashMap<>();
                            for (Flag<?> flag : rgs.keySet()) {
                                Object value = rgs.get(flag);
                                if (value instanceof StateFlag.State state) {
                                    value = state.name();
                                }
                                goodRgs.put(flag.getName(), value);
                            }
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".flags", goodRgs);
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".x1", rg.getMinimumPoint().x() - centerX);
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".x2", rg.getMaximumPoint().x() - centerX);
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".y1", rg.getMinimumPoint().y() - centerY);
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".y2", rg.getMaximumPoint().y() - centerY);
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".z1", rg.getMinimumPoint().z() - centerZ);
                            plugin.getConfig().set("data.arenas." + id + ".regions." + rgI + ".z2", rg.getMaximumPoint().z() - centerZ);
                        }
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        player.sendMessage(value(getMessage("arena.create.success"), name));
                        player.closeInventory();
                    } catch (IOException e) {
                        player.sendMessage(value(getMessage("arena.create.failed"), name));
                        player.closeInventory();
                        throw new RuntimeException(e);
                    }
                } catch (EmptyClipboardException e) {
                    player.sendMessage(value(getMessage("arena.create.failed"), name));
                    player.closeInventory();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
