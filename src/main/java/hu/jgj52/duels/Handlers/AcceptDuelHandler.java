package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.Types.Arena;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import hu.jgj52.duels.Utils.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;
import static java.util.Collections.min;

public class AcceptDuelHandler extends Replacer {
    public static boolean acceptDuel(Team blue, Team red, Map<String, Object> duelDetails) {
        int z = RuntimeVariables.usedArenas.isEmpty() ? 0 : min(RuntimeVariables.usedArenas) - 1000;
        RuntimeVariables.usedArenas.add(z);
        World arenas = Bukkit.getWorld("arenas");

        Object kitObj = duelDetails.get("kit");
        Kit kit;

        if (kitObj instanceof Kit) {
            kit = (Kit) kitObj;
        } else {
            kit = new Kit(Integer.parseInt(duelDetails.get("kit").toString()));
        }

        Arena arena = new Arena(kit.getId(), new Location(arenas, 0, 100, z));
        if (arena.place()) {
            Location blueTeamLoc = new Location(arenas, arena.getDistance() + 0.5, 100, z + 0.5);
            Location redTeamLoc = new Location(arenas, -arena.getDistance() + 0.5, 100, z + 0.5);

            List<PlayerD> allPlayers = new ArrayList<>();
            for (Team team : List.of(blue, red)) {
                for (PlayerD player : team.getPlayers()) {
                    player.isInDuel(true);
                    player.getInventory().clear();
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }
                    player.setFallDistance(0);
                    player.setMaxHealth(kit.getMaxHealth());
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setSaturation(5);
                    player.setExperienceLevelAndProgress(0);
                    player.setFireTicks(0);
                    player.setVelocity(new Vector(0, 0, 0));
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setInvulnerable(true);
                    player.setCollidable(true);
                    player.getInventory().setContents(kit.getContents(player));
                    allPlayers.add(player);
                }
            }
            for (PlayerD player : blue.getPlayers()) {
                player.teleport(blueTeamLoc);
            }
            for (PlayerD player : red.getPlayers()) {
                player.teleport(redTeamLoc);
            }
            Map<String, Object> duelData = new HashMap<>();
            duelData.put("blue", blue);
            duelData.put("red", red);
            duelData.put("kit", kit);
            duelData.put("spectators", duelDetails.get("spectators"));
            duelData.put("rounds", duelDetails.get("rounds"));
            RuntimeVariables.duels.add(duelData);
            new BukkitRunnable() {
                int time = arena.getCooldown();

                @Override
                public void run() {
                    if (time > 0) {
                        for (PlayerD p : allPlayers) {
                            p.sendTitle(value(getMessage("duelStart.countback"), String.valueOf(time)), "", 0, 20, 0);
                            Sound.wait(p);
                        }
                        time--;
                        return;
                    }

                    for (PlayerD p : allPlayers) {
                        p.sendTitle(getMessage("duelStart.started"), "", 0, 20, 0);
                        p.setInvulnerable(false);
                        Sound.done(p);
                    }

                    cancel();
                }
            }.runTaskTimer(plugin, 0L, 20L);
        } else return false;

        return true;
    }
}
