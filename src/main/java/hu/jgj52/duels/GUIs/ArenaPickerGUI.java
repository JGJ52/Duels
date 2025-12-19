package hu.jgj52.duels.GUIs;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArenaPickerGUI implements InventoryHolder {
    Inventory gui;
    int page;
    List<ItemStack> arenas;
    List<ItemStack> visibleArenas;
    public ArenaPickerGUI (Inventory kitGUI, int page, List<ItemStack> arenas) {
        gui = kitGUI;
        this.page = page;
        this.arenas = arenas;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public Inventory getKitGUI() {
        return gui;
    }
    public int getPage() {
        return page;
    }
    public List<ItemStack> getArenas() {
        return arenas;
    }
    public void setArenas(List<ItemStack> arenas) {
        this.arenas = arenas;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public List<ItemStack> getVisibleArenas() {
        return visibleArenas;
    }
    public void setVisibleArenas(List<ItemStack> visibleArenas) {
        this.visibleArenas = visibleArenas;
    }
}
