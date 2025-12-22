package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.GUIs.DuelRequestGUI;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import hu.jgj52.duels.Utils.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class DuelRequestHandler extends Replacer {
    public static boolean duelRequest(PlayerD player, PlayerD enemy) {
        Inventory gui = Bukkit.createInventory(new DuelRequestGUI(player, enemy), 54, playerName(getMessage("duelRequestGui.title"), enemy));

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(getMessage("duelRequestGui.glassName"));
        glass.setItemMeta(glassMeta);

        ItemStack content = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta contentMeta = content.getItemMeta();
        contentMeta.setDisplayName(getMessage("duelRequestGui.contentGlassName"));
        content.setItemMeta(contentMeta);

        for (int i = 0; i < 54; i++) {
            if ((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) || (i >= 37 && i <= 43)) {
                gui.setItem(i, content);
            } else {
                gui.setItem(i, glass);
            }
        }

        int slot = 10;
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
        if (section == null) return false;
        for (String key : section.getKeys(false)) {
            Kit kit = new Kit(Integer.parseInt(key));
            gui.setItem(slot, kit.getIcon());
            if (slot == 16 || slot == 25 || slot == 34 || slot == 17 || slot == 26 || slot == 35 || slot == 44) {
               slot += 3;
            } else {
               slot++;
            }
        }

        ItemStack rounds = new ItemStack(Material.ENDER_EYE);
        rounds.setAmount(plugin.getConfig().getInt("data.players." + player.getUniqueId() + ".rounds", 1));
        ItemMeta roundsMeta = rounds.getItemMeta();
        roundsMeta.setDisplayName(getMessage("duelRequestGui.roundsName"));
        rounds.setItemMeta(roundsMeta);

        gui.setItem(4, rounds);

        ItemStack spectate = new ItemStack(plugin.getConfig().getBoolean("data.players." + player.getUniqueId() + ".spectators", true) ? Material.LIME_STAINED_GLASS : Material.RED_STAINED_GLASS);
        ItemMeta spectateMeta = spectate.getItemMeta();
        spectateMeta.setDisplayName(getMessage("duelRequestGui.spectatorsName"));
        spectate.setItemMeta(spectateMeta);

        //gui.setItem(49, spectate);

        player.openInventory(gui);
        Sound.open(player);

        return true;
    }
    public static void sendDuelRequest(PlayerD player, PlayerD enemy, InventoryClickEvent event) {
        Component acceptButton = Component.text(getMessage("duelRequestAcceptButton"))
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.runCommand("/acceptduel " + player.getName()))
                .hoverEvent(HoverEvent.showText(Component.text(getMessage("duelRequestAcceptButtonHover"))));

        MiniMessage mm = MiniMessage.miniMessage();

        Component message = mm.deserialize(playerName(getMessage("duelRequestMessage"), player), Placeholder.component("accept", acceptButton));

        enemy.sendMessage(message);

        player.sendMessage(playerName(getMessage("duelRequestSentMessage"), enemy));

        Map<String, Object> data = new HashMap<>();
        data.put("rounds", plugin.getConfig().getInt("data.players." + player.getUniqueId() + ".rounds"));
        data.put("spectators", plugin.getConfig().getBoolean("data.players." + player.getUniqueId() + ".spectators"));
        data.put("kit", event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER));
        data.put("expire", System.currentTimeMillis() + plugin.getConfig().getLong("duelRequestExpire") * 1000L);
        player.duelRequest(enemy, data);
        player.closeInventory();
    }
}
