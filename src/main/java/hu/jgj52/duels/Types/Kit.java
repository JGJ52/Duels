package hu.jgj52.duels.Types;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class Kit {
    private final ItemStack[] contents;
    private final int id;
    private final double maxHealth;
    private final String name;
    private final ItemStack icon;

    public Kit (int id) {
        this.id = id;

        Object obj = plugin.getConfig().get("data.kits." + id + ".content");
        if (obj instanceof List<?> list) this.contents = list.toArray(new ItemStack[0]); else this.contents = new ItemStack[0];

        this.maxHealth = plugin.getConfig().getDouble("data.kits." + id + ".maxHealth");

        this.name = plugin.getConfig().getString("data.kits." + id + ".name");

        ItemStack icon = new ItemStack(Material.matchMaterial(plugin.getConfig().getString("data.kits." + id + ".icon")));
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("§f" + this.name);
        iconMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, this.id);
        icon.setItemMeta(iconMeta);
        this.icon = icon;
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

    public ItemStack[] getContents(PlayerD player) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits." + id + ".players." + player.getUniqueId());
        if (section == null) return contents;
        ItemStack[] playerContents = new ItemStack[contents.length];
        Map<Integer, Integer> items = new HashMap<>();
        for (String key : section.getKeys(false)) {
            items.put(Integer.parseInt(key), Integer.parseInt(section.getString(key)));
        }
        for (Integer key : items.keySet()) {
            playerContents[items.get(key)] = contents[key];
        }
        return playerContents;
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

    public ItemStack getIcon() {
        return icon;
    }
}
