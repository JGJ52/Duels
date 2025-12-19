package hu.jgj52.duels.Listeners;

import hu.jgj52.duels.GUIs.QueueGUI;
import hu.jgj52.duels.Handlers.AcceptDuelHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class QueueListener extends MessageManager implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player bukkitPlayer)) return;
        PlayerD player = PlayerManager.get(bukkitPlayer);
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getHolder() instanceof QueueGUI) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;

            Integer id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER);
            if (id == null) return;

            Kit kit = new Kit(id);

            List<PlayerD> players = RuntimeVariables.queue.computeIfAbsent(kit.getId(), i -> new ArrayList<>());

            if (players.contains(player)) {
                player.sendMessage(getMessage("alreadyInQueue"));
                return;
            }
            for (Integer integer : RuntimeVariables.queue.keySet()) {
                List<PlayerD> ps = RuntimeVariables.queue.get(integer);
                ps.remove(player);
                RuntimeVariables.queue.put(kit.getId(), players);
            }

            players.add(player);
            if (players.size() >= 2) {
                Team blue = new Team(List.of(players.removeFirst()));
                Team red = new Team(List.of(players.removeFirst()));
                Map<String, Object> data = new HashMap<>();
                data.put("rounds", plugin.getConfig().getInt("defaultRounds"));
                data.put("spectators", plugin.getConfig().getBoolean("defaultSpectators"));
                data.put("kit", kit.getId());
                data.put("expire", System.currentTimeMillis() + plugin.getConfig().getLong("duelRequestExpire") * 1000L);
                AcceptDuelHandler.acceptDuel(blue, red, data);
            } else {
                player.closeInventory();
                player.sendMessage(getMessage("enteredQueue"));
            }
            RuntimeVariables.queue.put(kit.getId(), players);
        }
    }
}
