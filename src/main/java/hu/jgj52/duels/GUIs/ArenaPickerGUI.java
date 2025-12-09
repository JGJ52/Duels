package hu.jgj52.duels.GUIs;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ArenaPickerGUI implements InventoryHolder {
    Inventory gui;
    public ArenaPickerGUI (Inventory kitGUI) {
        gui = kitGUI;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public Inventory getKitGUI() {
        return gui;
    }
}
