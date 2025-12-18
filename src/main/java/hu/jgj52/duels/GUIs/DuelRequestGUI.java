package hu.jgj52.duels.GUIs;

import hu.jgj52.duels.Types.PlayerD;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class DuelRequestGUI implements InventoryHolder {
    PlayerD player;
    PlayerD enemy;
    public DuelRequestGUI (PlayerD player, PlayerD enemy) {
        this.player = player;
        this.enemy = enemy;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
    public PlayerD getPlayer() {
        return player;
    }
    public PlayerD getEnemy() {
        return enemy;
    }
}
