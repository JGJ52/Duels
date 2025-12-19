package hu.jgj52.duels.Types;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerD {
    private boolean isInDuel;
    private final Map<PlayerD, Map<String, Object>> duelRequests = new HashMap<>();
    private final Player player;

    public PlayerD(Player player) {
        this.player = player;
    }

    public boolean isInDuel() {
        return isInDuel;
    }

    public void isInDuel(Boolean is) {
        isInDuel = is;
    }

    public Map<String, Object> duelRequest(PlayerD enemy) {
        Map<String, Object> request = null;
        for (PlayerD key : duelRequests.keySet()) {
            if (key == enemy) {
                request = duelRequests.get(key);
                break;
            }
        }
        return request;
    }

    public void duelRequest(PlayerD enemy, Map<String, Object> duelData) {
        if (duelData.isEmpty()) {
            duelRequests.remove(enemy);
            return;
        }
        duelRequests.put(enemy, duelData);
    }

    public Map<PlayerD, Map<String, Object>> duelRequests() {
        return duelRequests;
    }

    //bukkit things
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    public String getName() {
        return player.getName();
    }

    public boolean hasPermission(String name) {
        return player.hasPermission(name);
    }

    public InventoryView openInventory(Inventory inventory) {
        return player.openInventory(inventory);
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public void closeInventory() {
        player.closeInventory();
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public void setGameMode(GameMode mode) {
        player.setGameMode(mode);
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    public void removePotionEffect(PotionEffectType type) {
        player.removePotionEffect(type);
    }

    public void setFallDistance(float distance) {
        player.setFallDistance(distance);
    }

    @Deprecated(since = "1.11")
    public void setMaxHealth(double health) {
        player.setMaxHealth(health);
    }

    public void setHealth(double health) {
        player.setHealth(health);
    }

    @Deprecated(since = "1.11")
    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    public void setFoodLevel(int value) {
        player.setFoodLevel(value);
    }

    public void setSaturation(float value) {
        player.setSaturation(value);
    }

    public void setExperienceLevelAndProgress(int totalExperience) {
        player.setExperienceLevelAndProgress(totalExperience);
    }

    public void setFireTicks(int ticks) {
        player.setFireTicks(ticks);
    }

    public void setVelocity(Vector velocity) {
        player.setVelocity(velocity);
    }

    public void setFlying(boolean value) {
        player.setFlying(value);
    }

    public void setAllowFlight(boolean flight) {
        player.setAllowFlight(flight);
    }

    public void setInvulnerable(boolean flag) {
        player.setInvulnerable(flag);
    }

    public void setCollidable(boolean collidable) {
        player.setCollidable(collidable);
    }

    public void teleport(Location location) {
        player.teleport(location);
    }

    @Deprecated
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public void setInvisible(boolean invisible) {
        player.setInvisible(invisible);
    }
}
