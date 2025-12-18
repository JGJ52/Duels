package hu.jgj52.duels.Managers;

import hu.jgj52.duels.Types.PlayerD;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class PlayerManager {
    public static void tpToSpawn(PlayerD player) {
        Location spawn = new Location(Bukkit.getWorld(plugin.getConfig().getString("spawn.world")), plugin.getConfig().getDouble("spawn.x"), plugin.getConfig().getDouble("spawn.y"), plugin.getConfig().getDouble("spawn.z"), (float) plugin.getConfig().getDouble("yaw"), (float) plugin.getConfig().getDouble("pitch"));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setFireTicks(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setExperienceLevelAndProgress(0);
        player.setFallDistance(0);
        player.setVelocity(new Vector(0, 0, 0));
        player.setFlying(false);
        player.setInvisible(false);
        player.setAllowFlight(false);
        player.setInvulnerable(true);
        player.setCollidable(true);
        player.setGameMode(GameMode.valueOf(plugin.getConfig().getString("spawn.gamemode")));
        player.getInventory().clear();
        player.teleport(spawn);
    }

    private static final Map<Player, PlayerD> cache = new HashMap<>();

    public static PlayerD get(Player player) {
        return cache.computeIfAbsent(
                player,
                PlayerD::new
        );
    }

    public static void remove(Player player) {
        cache.remove(player);
    }
}
