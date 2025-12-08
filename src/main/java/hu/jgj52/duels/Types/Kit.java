package hu.jgj52.duels.Types;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class Kit {
    private final ItemStack[] contents;
    private final int id;
    private final double maxHealth;
    private final String name;

    public Kit (int id) {
        this.id = id;

        Object obj = plugin.getConfig().get("data.kits." + id + ".content");
        if (obj instanceof List<?> list) this.contents = list.toArray(new ItemStack[0]); else this.contents = new ItemStack[0];

        this.maxHealth = plugin.getConfig().getDouble("data.kits." + id + ".maxHealth");

        this.name = plugin.getConfig().getString("data.kits." + id + ".name");
    }

    public Kit (String name) {
        this(findByName(name));
    }

    private static int findByName(String name) {
        for (String key : plugin.getConfig().getConfigurationSection("data.kits").getKeys(false)) {
            if (plugin.getConfig().getString("data.kits." + key + ".name").equals(name)) return Integer.parseInt(key);
        }
        return 0;
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

    public String getName() {
        return name;
    }

    public void delete() {
        plugin.getConfig().set("data.kits." + id, null);
        plugin.saveConfig();
        plugin.reloadConfig();
    }
}
