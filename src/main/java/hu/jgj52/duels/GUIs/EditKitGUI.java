package hu.jgj52.duels.GUIs;

import hu.jgj52.duels.Types.Kit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class EditKitGUI implements InventoryHolder {
    private Kit kit;
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public EditKitGUI (Kit kit) {
        this.kit = kit;
    }
    public Kit getKit() {
        return kit;
    }
}
