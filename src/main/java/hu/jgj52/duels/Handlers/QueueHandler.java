package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.GUIs.QueueGUI;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import hu.jgj52.duels.Utils.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class QueueHandler extends Replacer {
    public static boolean queue(PlayerD player) {
        Inventory gui = Bukkit.createInventory(new QueueGUI(), 54, getMessage("queueGui.title"));

        ItemStack outline = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta outlineMeta = outline.getItemMeta();
        outlineMeta.setDisplayName(getMessage("queueGui.outlineName"));
        outline.setItemMeta(outlineMeta);

        ItemStack inline = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta inlineMeta = inline.getItemMeta();
        inlineMeta.setDisplayName(getMessage("queueGui.inlineName"));
        inline.setItemMeta(inlineMeta);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || List.of(17, 18, 26, 27, 35, 36).contains(i) || i > 43) gui.setItem(i, outline);
            else gui.setItem(i, inline);
        }

        int slot = 10;
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
        if (section == null) return false;
        for (String key : section.getKeys(false)) {
            Kit kit = new Kit(Integer.parseInt(key));
            ItemStack icon = kit.getIcon();
            ItemMeta iconMeta = icon.getItemMeta();
            iconMeta.setLore(List.of(value(getMessage("queueKitIconLore"), String.valueOf(RuntimeVariables.queue.computeIfAbsent(kit.getId(), k -> new ArrayList<>()).size()))));
            icon.setItemMeta(iconMeta);
            gui.setItem(slot, icon);
            if (List.of(16, 25, 34).contains(slot)) slot += 3; else slot++;
        }

        player.openInventory(gui);
        Sound.open(player);

        return true;
    }
}
