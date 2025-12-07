package hu.jgj52.duels.Types;

import org.bukkit.inventory.ItemStack;

import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class Kit {
    private final ItemStack[] contents;
    private final int id;
    private final double maxHealth;

    public Kit (int id) {
        this.id = id;

        Object obj = plugin.getConfig().get("data.kits." + id + ".content");
        if (obj instanceof List<?> list) this.contents = list.toArray(new ItemStack[0]); else this.contents = new ItemStack[0];

        this.maxHealth = plugin.getConfig().getDouble("data.kits." + id + ".maxHealth");
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public int getId() {
        return id;
    }

    public double getMaxHealth() {
        return maxHealth;
    }
}
