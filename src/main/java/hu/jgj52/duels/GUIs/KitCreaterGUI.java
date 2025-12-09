package hu.jgj52.duels.GUIs;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitCreaterGUI implements InventoryHolder {
    List<Material> possible = new ArrayList<>();
    int index = 1;
    String name;
    int id;
    ItemStack[] content;
    public KitCreaterGUI (String kitName, int kitId, ItemStack[] kitContent) {
        Collections.addAll(possible, Material.values());
        name = kitName;
        id = kitId;
        content = kitContent;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int i) {
        index = i;
    }
    public List<Material> getPossible() {
        return possible;
    }
    public String getKitName() {
        return name;
    }
    public int getKitId() {
        return id;
    }
    public ItemStack[] getKitContent() {
        return content;
    }
}
