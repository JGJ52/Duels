package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.GUIs.EditKitGUI;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static hu.jgj52.duels.Duels.plugin;

public class EditKitHandler {
    public static boolean handle(Player player, Kit kit) {
        PlayerInventory inventory = player.getInventory();
        inventory.setContents(kit.getContents(player));
        for (int i = 0; i < 42; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "defaultSlot"), PersistentDataType.INTEGER, i);
            item.setItemMeta(meta);
        }
        Inventory gui = Bukkit.createInventory(new EditKitGUI(kit), 27, Replacer.value(MessageManager.getMessage("editKitGui.title"), kit.getName()));

        ItemStack save = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta saveMeta = save.getItemMeta();
        saveMeta.setDisplayName(MessageManager.getMessage("editKitGui.saveName"));
        save.setItemMeta(saveMeta);

        ItemStack info = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Replacer.value(MessageManager.getMessage("editKitGui.infoName"), kit.getName()));
        info.setItemMeta(infoMeta);

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(MessageManager.getMessage("editKitGui.cancelName"));
        cancel.setItemMeta(cancelMeta);

        for (int i = 0; i < 27; i++) {
            if (i < 3 || (i > 8 && i < 12) || (i > 17 && i < 21)) gui.setItem(i, cancel);
            if ((i > 2 && i < 6) || (i > 11 && i < 15) || (i > 20 && i < 24)) gui.setItem(i, info);
            if ((i > 5 && i < 9) || (i > 14 && i < 18) || i > 23) gui.setItem(i, save);
        }

        player.openInventory(gui);

        return true;
    }
}
