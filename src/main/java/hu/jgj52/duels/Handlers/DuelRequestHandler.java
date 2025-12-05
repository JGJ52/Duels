package hu.jgj52.duels.Handlers;

import hu.jgj52.database.Database;
import hu.jgj52.duels.Duels;
import hu.jgj52.duels.GUIs.DuelRequestGUI;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class DuelRequestHandler {
    public static boolean duelRequest(Player player, Player enemy) {
        //todo: remove database entirely, store everything in config.yml because im lazy to make more files
        RuntimeVariables.duelRequests.put(player, enemy);

        Inventory gui = Bukkit.createInventory(new DuelRequestGUI(), 54, Replacer.playerName(MessageManager.getMessage("duelRequestGui.title"), enemy));

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(MessageManager.getMessage("duelRequestGui.glassName"));
        glass.setItemMeta(glassMeta);

        ItemStack content = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta contentMeta = content.getItemMeta();
        contentMeta.setDisplayName(MessageManager.getMessage("duelRequestGui.contentGlassName"));

        for (int i = 0; i < 54; i++) {
            if ((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) || (i >= 37 && i <= 43)) {
                gui.setItem(i, content);
            } else {
                gui.setItem(i, glass);
            }
        }

        Database.asnycPostgreSQL(Database.postgres.from("duels_kits").order("id")).thenAccept(result -> {
           int slot = 10;
           for (Map<String, Object> kit : result.data) {
               ItemStack icon = new ItemStack(Material.matchMaterial(kit.get("icon").toString()));
               ItemMeta iconMeta = icon.getItemMeta();
               iconMeta.setDisplayName(kit.get("name").toString());
               iconMeta.getPersistentDataContainer().set(new NamespacedKey(Duels.plugin, "id"), PersistentDataType.INTEGER, Integer.valueOf(kit.get("id").toString()));
               icon.setItemMeta(iconMeta);
               gui.setItem(slot, icon);
               if (slot == 16 || slot == 25 || slot == 34 || slot == 17 || slot == 26 || slot == 35 || slot == 44) {
                   slot =+ 3;
               } else {
                   slot++;
               }
            }
        });

        Database.asnycPostgreSQL(Database.postgres.from("duels_players").eq("uuid", player.getUniqueId())).thenAccept(result -> {
            ItemStack rounds = new ItemStack(Material.ENDER_EYE);
            ItemMeta roundsMeta = rounds.getItemMeta();
            roundsMeta.setDisplayName(Replacer.value(MessageManager.getMessage("duelRequestGui.roundsName"), result.data.get(0).get("rounds") == null ? String.valueOf(Duels.plugin.getConfig().getInt("defaultRounds")) : result.data.get(0).get("rounds").toString()));
            rounds.setItemMeta(roundsMeta);

            gui.setItem(4, rounds);

            ItemStack spectate = new ItemStack(Material.TARGET);
            ItemMeta spectateMeta = spectate.getItemMeta();
            spectateMeta.setDisplayName(Replacer.value(MessageManager.getMessage("duelRequestGui.spectatorsName"), result.data.get(0).get("spectators") == null ? String.valueOf(Duels.plugin.getConfig().getBoolean("defaultSpectators")) : result.data.get(0).get("spctators").toString()));
            spectate.setItemMeta(spectateMeta);

            gui.setItem(49, spectate);
        });

        player.openInventory(gui);

        return true;
    }
    public static void sendDuelRequest(Player player, Player enemy, InventoryClickEvent event) {
        Database.asnycPostgreSQL(Database.postgres.from("duels_kits").eq("id", event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Duels.plugin, "id"), PersistentDataType.INTEGER))).thenAccept(result -> {
            Component acceptButton = Component.text(MessageManager.getMessage("duelRequestAcceptButton"))
                    .color(NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.runCommand("/acceptduel " + enemy.getName()))
                    .hoverEvent(HoverEvent.showText(Component.text(MessageManager.getMessage("duelRequestAcceptButtonHover"))));

            MiniMessage mm = MiniMessage.miniMessage();

            Component message = mm.deserialize(MessageManager.getMessage("duelRequestMessage"), Placeholder.component("accept", acceptButton));

            enemy.sendMessage(message);

            player.sendMessage(Replacer.playerName(MessageManager.getMessage("duelRequestSentMessage"), enemy));

            Database.asnycPostgreSQL(Database.postgres.from("duels_players").eq("uuid", player.getUniqueId())).thenAccept(queryResult -> {
                Map<String, Object> data = new HashMap<>();
                data.put("rounds", queryResult.data.get(0).get("rounds"));
                data.put("spectators", queryResult.data.get(0).get("spectators"));
                data.put("kit", result.data.get(0).get("id"));
                data.put("expire", System.currentTimeMillis() + Duels.plugin.getConfig().getLong("duelRequestExpire") * 1000L);
                RuntimeVariables.sentDuelRequests.put(Map.of(player, enemy), data);
            });
        });
    }
}
