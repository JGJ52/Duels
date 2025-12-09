package hu.jgj52.duels.GUIs;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class DuelRequestGUI implements InventoryHolder {
    Player player;
    Player enemy;
    public DuelRequestGUI (Player player, Player enemy) {
        this.player = player;
        this.enemy = enemy;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public Player getPlayer() {
        return player;
    }
    public Player getEnemy() {
        return enemy;
    }
}
