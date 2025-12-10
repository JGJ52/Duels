package hu.jgj52.duels.GUIs;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ArenaCreaterGUI implements InventoryHolder {
    String name;
    public ArenaCreaterGUI (String name) {
        this.name = name;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public String getName() {
        return name;
    }
}
